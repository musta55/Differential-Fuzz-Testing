public String[] oapServices() {
    if (oapServices == null || oapServices.trim().length() == 0) {
        throw new IllegalArgumentException("oapServices cannot be null or empty");
    }
    return oapServices.split(",");
}