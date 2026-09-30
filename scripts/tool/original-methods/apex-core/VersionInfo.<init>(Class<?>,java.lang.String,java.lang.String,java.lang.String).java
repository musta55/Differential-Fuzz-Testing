public VersionInfo(Class<?> classInJar, String groupId, String artifactId, String gitPropertiesResource) {
    try {
        URL res = classInJar.getResource(classInJar.getSimpleName() + ".class");
        URLConnection conn = res.openConnection();
        if (conn instanceof JarURLConnection) {
            Manifest mf = ((JarURLConnection) conn).getManifest();
            Attributes mainAttribs = mf.getMainAttributes();
            String builtBy = mainAttribs.getValue("Built-By");
            if (builtBy != null) {
                this.user = builtBy;
            }
        }
        Enumeration<URL> resources = classInJar.getClassLoader().getResources("META-INF/maven/" + groupId + "/" + artifactId + "/pom.properties");
        while (resources.hasMoreElements()) {
            Properties pomInfo = new Properties();
            pomInfo.load(resources.nextElement().openStream());
            String v = pomInfo.getProperty("version", "unknown");
            this.version = v;
        }
        resources = VersionInfo.class.getClassLoader().getResources(gitPropertiesResource);
        while (resources.hasMoreElements()) {
            Properties gitInfo = new Properties();
            gitInfo.load(resources.nextElement().openStream());
            String commitAbbrev = gitInfo.getProperty("git.commit.id.abbrev", "unknown");
            String branch = gitInfo.getProperty("git.branch", "unknown");
            this.revision = "rev: " + commitAbbrev + " branch: " + branch;
            this.date = gitInfo.getProperty("git.build.time", this.date);
            this.user = gitInfo.getProperty("git.build.user.name", this.user);
            break;
        }
    } catch (IOException e) {
        org.slf4j.LoggerFactory.getLogger(VersionInfo.class).error("Failed to read version info", e);
    }
}