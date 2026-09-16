public static void toXml(Writer out, Document doc) throws IOException {
    OutputFormat outformat = OutputFormat.createPrettyPrint();
    outformat.setIndentSize(1);
    outformat.setIndent("\t");
    outformat.setEncoding(UTF_8.name());
    XMLWriter writer = new XMLWriter(out, outformat);
    writer.write(doc);
    writer.flush();
    out.flush();
    out.close();
}