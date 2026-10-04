/**
 * Required during build `generate-blog-idx-*` goal
 *
 * @param args - [0] path to ${project.basedir}/src/site/markdown/blog folder
 *               [1] path to ${project.build.directory}/generated-site/markdown/blog folder
 * @throws Exception - in case of any error
 */
public static void main(String[] args) throws Exception {
    StringBuilder sb = new StringBuilder();
    sb.append("<!--").append(XmlExport.LICENSE).append("-->").append(System.lineSeparator()).append("# Apache OpenMeetings blog posts").append(System.lineSeparator()).append(System.lineSeparator());
    try (Stream<Path> walk = Files.walk(Paths.get(args[0]))) {
        walk.map(BlogIndexGenerator::parseLink).filter(Objects::nonNull).filter(Link::isValid).sorted((link1, link2) -> link1.published().compareTo(link2.published())).forEach(link -> appendLink(sb, link));
    }
    Path outDir = Paths.get(args[1]);
    Files.createDirectories(outDir);
    Files.write(outDir.resolve("index.md"), sb.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
}
// ---- helper method(s) introduced by the refactoring ----
private static void appendLink(StringBuilder sb, Link link) {
    sb.append('[').append(link.title()).append("](").append("https://openmeetings.apache.org/blog/").append(link.link()).append(".html)").append(System.lineSeparator()).append(System.lineSeparator());
}

private static String extractValue(String line, String prefix) {
    if (line.startsWith(prefix)) {
        return line.substring(prefix.length()).trim();
    }
    return null;
}

private static Link parseLink(Path post) {
    if (post.toFile().isDirectory()) {
        return null;
    }
    try {
        List<String> lines = readLines(post);
        String title = findValue(lines, "title:");
        String link = findValue(lines, "permalink:");
        String published = findValue(lines, "date:");
        return Link.of(title, link, published);
    } catch (IOException e) {
        log.error("Unexpected error", e);
    }
    return null;
}

private static List<String> readLines(Path post) throws IOException {
    return Files.readAllLines(post);
}

private static String findValue(List<String> lines, String prefix) {
    return lines.stream().map(line -> extractValue(line, prefix)).filter(Objects::nonNull).findFirst().orElse(null);
}

