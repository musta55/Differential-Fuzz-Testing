/**
 * Compares two version strings.
 *
 * @param str1 a string of ordinal numbers separated by decimal points.
 * @param str2 a string of ordinal numbers separated by decimal points.
 * @return The result is a negative integer if str1 is _numerically_ less than str2. The result is a positive integer
 * if str1 is _numerically_ greater than str2. The result is zero if the strings are _numerically_ equal.
 */
public static int compare(String str1, String str2) {
    String[] vals1 = normalizeVersion(str1).split("\\.");
    String[] vals2 = normalizeVersion(str2).split("\\.");
    int i = 0;
    while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
        i++;
    }
    if (i < vals1.length && i < vals2.length) {
        int diff = Integer.compare(Integer.parseInt(vals1[i]), Integer.parseInt(vals2[i]));
        return Integer.signum(diff);
    } else {
        return Integer.signum(vals1.length - vals2.length);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void loadManifestInfo(Class<?> classInJar) throws IOException {
    URL res = classInJar.getResource(classInJar.getSimpleName() + ".class");
    URLConnection conn = res.openConnection();
    if (conn instanceof JarURLConnection) {
        Manifest mf = ((JarURLConnection) conn).getManifest();
        Attributes mainAttribs = mf.getMainAttributes();
        setUser(mainAttribs.getValue("Built-By"));
    }
}

private void loadPomProperties(String groupId, String artifactId) throws IOException {
    Enumeration<URL> resources = VersionInfo.class.getClassLoader().getResources("META-INF/maven/" + groupId + "/" + artifactId + "/pom.properties");
    while (resources.hasMoreElements()) {
        Properties pomInfo = new Properties();
        pomInfo.load(resources.nextElement().openStream());
        setVersion(pomInfo.getProperty("version", "unknown"));
    }
}

private void loadGitProperties(String gitPropertiesResource) throws IOException {
    Enumeration<URL> resources = VersionInfo.class.getClassLoader().getResources(gitPropertiesResource);
    while (resources.hasMoreElements()) {
        Properties gitInfo = new Properties();
        gitInfo.load(resources.nextElement().openStream());
        setRevision(gitInfo.getProperty("git.commit.id.abbrev", "unknown"), gitInfo.getProperty("git.branch", "unknown"));
        setDate(gitInfo.getProperty("git.build.time", this.date));
        setUser(gitInfo.getProperty("git.build.user.name", this.user));
        break;
    }
}

private void setVersion(String version) {
    this.version = version;
}

private void setUser(String user) {
    if (user != null) {
        this.user = user;
    }
}

private void setDate(String date) {
    this.date = date;
}

private void setRevision(String commitAbbrev, String branch) {
    this.revision = "rev: " + commitAbbrev + " branch: " + branch;
}

