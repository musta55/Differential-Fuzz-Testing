public static boolean isCompatible(String thisVersion, String requiredVersion) {
    String[] thisVersionComponent = normalizeVersion(thisVersion).split("\\.");
    String[] requiredVersionComponent = normalizeVersion(requiredVersion).split("\\.");
    // major version check
    if (!thisVersionComponent[0].equals(requiredVersionComponent[0])) {
        return false;
    }
    // minor version check
    if (Integer.parseInt(thisVersionComponent[1]) < Integer.parseInt(requiredVersionComponent[1])) {
        return false;
    }
    // patch version doesn't matter
    return true;
}