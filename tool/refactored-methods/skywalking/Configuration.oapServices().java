public String[] oapServices() {
    if (oapServices == null || oapServices.trim().isEmpty()) {
        throw new IllegalArgumentException("oapServices cannot be null or empty");
    }
    return oapServices.split(",");
}