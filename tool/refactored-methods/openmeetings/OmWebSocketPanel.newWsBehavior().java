private WebSocketBehavior newWsBehavior() {
    return new WebSocketBehavior() {

        private static final long serialVersionUID = 1L;

        @Override
        public void renderHead(Component component, IHeaderResponse response) {
            super.renderHead(component, response);
            renderWebSocketOpenedScript(response);
        }

        private void renderWebSocketOpenedScript(IHeaderResponse response) {
            response.render(JavaScriptHeaderItem.forScript(String.format("Wicket.Event.subscribe(Wicket.Event.Topic.WebSocket.Opened, function() {Wicket.WebSocket.send('%s');});", CONNECTED_MSG), "ws-connected-script"));
        }

        @Override
        protected void onConnect(ConnectedMessage message) {
            super.onConnect(message);
            OmWebSocketPanel.this.onConnect(message);
        }

        @Override
        protected void onMessage(WebSocketRequestHandler handler, TextMessage msg) {
            handleTextMessage(handler, msg);
        }

        private void handleTextMessage(WebSocketRequestHandler handler, TextMessage msg) {
            if (CONNECTED_MSG.equals(msg.getText())) {
                handleConnectedMessage(handler);
            } else {
                handleCustomMessages(handler, msg);
            }
        }

        private void handleConnectedMessage(WebSocketRequestHandler handler) {
            if (connected.compareAndSet(false, true)) {
                OmWebSocketPanel.this.onConnect(handler);
            }
        }

        private void handleCustomMessages(WebSocketRequestHandler handler, TextMessage msg) {
            try {
                JSONObject m = new JSONObject(msg.getText());
                String messageType = m.optString("type", "");
                switch(messageType) {
                    case KURENTO_TYPE:
                        kHandler.onMessage(getWsClient(), m);
                        break;
                    case "mic":
                        micMessage(m);
                        break;
                    case "ping":
                        handlePingMessage(handler);
                        break;
                    default:
                        OmWebSocketPanel.this.onMessage(handler, m);
                }
            } catch (Exception e) {
                log.error("Error while processing incoming message", e);
            }
        }

        private void handlePingMessage(WebSocketRequestHandler handler) {
            log.trace("Sending WebSocket PING");
            handler.appendJavaScript("OmUtil.ping();");
            WebSocketHelper.sendClient(getWsClient(), new byte[] { getUserId() == null ? 0 : getUserId().byteValue() });
        }

        @Override
        protected void onAbort(AbortedMessage msg) {
            closeHandler(msg);
        }

        @Override
        protected void onClose(ClosedMessage msg) {
            closeHandler(msg);
        }

        @Override
        protected void onError(WebSocketRequestHandler handler, ErrorMessage msg) {
            closeHandler(msg);
        }

        private void micMessage(final JSONObject m) {
            IWsClient curClient = getWsClient();
            if (!(curClient instanceof Client)) {
                return;
            }
            Client c = (Client) curClient;
            if (c.getRoomId() == null) {
                return;
            }
            WebSocketHelper.sendRoomOthers(c.getRoomId(), c.getUid(), m.put("uid", c.getUid()));
        }
    };
}
// ---- helper method(s) introduced by the refactoring ----
private void addPingBehavior() {
    add(new Behavior() {

        private static final long serialVersionUID = 1L;

        @Override
        public void renderHead(Component component, IHeaderResponse response) {
            if (!pingable) {
                log.debug("pingTimer is attached");
                pingable = true;
                super.renderHead(component, response);
                response.render(OnDomReadyHeaderItem.forScript("OmUtil.ping();"));
            }
        }
    });
}

