public ServletConfigSource() {
    servletProperties = new ConcurrentHashMap<>();
    initOrdinal(50);
}