static boolean verify(String str, String hash) {
    if (str == null) {
        return hash == null;
    }
    if (hash == null) {
        return false;
    }
    String[] ss = hash.split(":");
    if (ss.length != 3) {
        return false;
    }
    try {
        int iter = Integer.parseInt(ss[0]);
        String h1 = ss[1];
        byte[] salt = Base64.decodeBase64(ss[2]);
        String h2 = hash(str, salt, iter);
        return h2.equals(h1);
    } catch (Exception e) {
        return false;
    }
}