private <T extends PubSubWebSocketClient> T build(Class<T> clazz) {
    Preconditions.checkState(context != null, "Context not specified");
    String gatewayAddress = context.getValue(LogicalPlan.GATEWAY_CONNECT_ADDRESS);
    if (gatewayAddress != null) {
        int timeout = context.getValue(LogicalPlan.PUBSUB_CONNECT_TIMEOUT_MILLIS);
        boolean gatewayUseSsl = context.getValue(LogicalPlan.GATEWAY_USE_SSL);
        // The builder can be used to build different types of PubSub clients in future but for now only one is supported
        SharedPubSubWebSocketClient wsClient = null;
        try {
            wsClient = new SharedPubSubWebSocketClient((gatewayUseSsl ? "wss://" : "ws://") + gatewayAddress + "/pubsub", timeout);
            String gatewayUserName = context.getValue(LogicalPlan.GATEWAY_USER_NAME);
            String gatewayPassword = context.getValue(LogicalPlan.GATEWAY_PASSWORD);
            if (gatewayUserName != null && gatewayPassword != null) {
                wsClient.setLoginUrl((gatewayUseSsl ? "https://" : "http://") + gatewayAddress + GATEWAY_LOGIN_URL_PATH);
                wsClient.setUserName(gatewayUserName);
                wsClient.setPassword(gatewayPassword);
            }
            return (T) wsClient;
        } catch (URISyntaxException e) {
            logger.warn("Unable to initialize websocket for gateway address {}", gatewayAddress, e);
        }
        return null;
    }
    return null;
}