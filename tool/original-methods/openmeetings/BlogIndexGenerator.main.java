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
        walk.map(BlogIndexGenerator::toLink).filter(Objects::nonNull).filter(Link::isValid).sorted((link1, link2) -> link1.published().compareTo(link2.published())).forEach(link -> addLink(sb, link));
    }
    Path outDir = Paths.get(args[1]);
    Files.createDirectories(outDir);
    Files.write(outDir.resolve("index.md"), sb.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
}