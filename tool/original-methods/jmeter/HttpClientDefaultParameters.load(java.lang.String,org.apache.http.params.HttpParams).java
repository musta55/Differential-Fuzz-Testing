/**
 * Loads a property file and converts parameters as necessary.
 *
 * @param file the file to load
 * @param params Apache HttpClient parameter instance
 */
public static void load(String file, final org.apache.http.params.HttpParams params) {
    load(file, new GenericHttpParams() {

        @Override
        public void setParameter(String name, Object value) {
            params.setParameter(name, value);
        }

        @Override
        public void setVersion(String name, String value) {
            String[] parts = value.split("\\.");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Version must have form m.n");
            }
            params.setParameter(name, new org.apache.http.HttpVersion(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
        }
    });
}