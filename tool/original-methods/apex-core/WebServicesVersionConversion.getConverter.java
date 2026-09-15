public static Converter getConverter(String version) throws IncompatibleVersionException {
    if (!isVersionCompatible(version)) {
        throw new IncompatibleVersionException("Stram version " + version + " is incompatible with the current build (" + WebServices.VERSION + ")");
    }
    // Add old versions that ARE supported here in the future
    if (version.equals("v1")) {
        return new Converter() {

            @Override
            public String convertCommandPath(String path) {
                return path.replaceFirst("/ws/v2/", "/ws/v1/");
            }

            @Override
            public String convertResponse(String path, String response) {
                return response;
            }
        };
    }
    return null;
}