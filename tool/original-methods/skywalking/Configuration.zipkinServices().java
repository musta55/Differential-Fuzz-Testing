public String[] zipkinServices() {
    if (zipkinServices == null || zipkinServices.trim().length() == 0) {
        throw new IllegalArgumentException("zipkinServices cannot be null or empty");
    }
    return zipkinServices.split(",");
}