public ServletConfigSource() {
    servletProperties = new ConcurrentHashMap<String, String>();
    initOrdinal(50);
}