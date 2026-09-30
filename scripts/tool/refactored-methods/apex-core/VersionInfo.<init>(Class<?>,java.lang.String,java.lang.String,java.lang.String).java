public VersionInfo(Class<?> classInJar, String groupId, String artifactId, String gitPropertiesResource) {
    try {
        loadManifestInfo(classInJar);
        loadPomProperties(groupId, artifactId);
        loadGitProperties(gitPropertiesResource);
    } catch (IOException e) {
        org.slf4j.LoggerFactory.getLogger(VersionInfo.class).error("Failed to read version info", e);
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

