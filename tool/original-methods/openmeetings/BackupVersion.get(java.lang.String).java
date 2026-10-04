public static BackupVersion get(String ver) {
    BackupVersion bv = new BackupVersion();
    String[] dashParts = ver.split("-");
    if (dashParts.length == 0) {
        return bv;
    }
    String[] dotParts = dashParts[0].split("\\.");
    if (dotParts.length > 0) {
        bv.major = toInt(dotParts[0]);
    }
    if (dotParts.length > 1) {
        bv.minor = toInt(dotParts[1]);
    }
    if (dotParts.length > 2) {
        bv.micro = toInt(dotParts[2]);
    }
    return bv;
}