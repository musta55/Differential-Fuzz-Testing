public static void toXml(Writer out, Document doc) throws IOException {
    XMLWriter writer = new XMLWriter(out, createOutputFormat());
    writer.write(doc);
    writer.flush();
    out.flush();
    out.close();
}
// ---- helper method(s) introduced by the refactoring ----
private static OutputFormat createOutputFormat() {
    OutputFormat outformat = OutputFormat.createPrettyPrint();
    outformat.setIndentSize(1);
    outformat.setIndent("\t");
    outformat.setEncoding(UTF_8.name());
    return outformat;
}

