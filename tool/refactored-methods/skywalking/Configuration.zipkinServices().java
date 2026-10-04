public String[] zipkinServices() {
    if (zipkinServices == null || zipkinServices.trim().isEmpty()) {
        throw new IllegalArgumentException("zipkinServices cannot be null or empty");
    }
    return zipkinServices.split(",");
}