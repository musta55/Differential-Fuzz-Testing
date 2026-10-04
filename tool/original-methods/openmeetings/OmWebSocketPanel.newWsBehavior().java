private WebSocketBehavior newWsBehavior() {
    return new WebSocketBehavior() {

        private static final long serialVersionUID = 1L;

        @Override
        public void renderHead(Component component, IHeaderResponse response) {
            super.renderHead(component, response);
            response.render(JavaScriptHeaderItem.forScript(String.format("Wicket.Event.subscribe(Wicket.Event.Topic.WebSocket.Opened, function() {Wicket.WebSocket.send('%s');});", CONNECTED_MSG), "ws-connected-script"));
        }

        @Override
        protected void onConnect(ConnectedMessage message) {
            super.onConnect(message);
            OmWebSocketPanel.this.onConnect(message);
        }

        @Override
        protected void onMessage(WebSocketRequestHandler handler, TextMessage msg) {
            if (CONNECTED_MSG.equals(msg.getText())) {
                if (connected.compareAndSet(false, true)) {
                    OmWebSocketPanel.this.onConnect(handler);
                }
            } else {
                final JSONObject m;
                try {
                    m = new JSONObject(msg.getText());
                    switch(m.optString("type", "")) {
                        case KURENTO_TYPE:
                            kHandler.onMessage(getWsClient(), m);
                            break;
                        case "mic":
                            micMessage(m);
                            break;
                        case "ping":
                            log.trace("Sending WebSocket PING");
                            handler.appendJavaScript("OmUtil.ping();");
                            WebSocketHelper.sendClient(getWsClient(), new byte[] { getUserId() == null ? 0 : getUserId().byteValue() });
                            break;
                        default:
                            OmWebSocketPanel.this.onMessage(handler, m);
                    }
                } catch (Exception e) {
                    log.error("Error while processing incoming message", e);
                }
            }
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