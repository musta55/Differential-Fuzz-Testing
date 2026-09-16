private CsvFile(String pathname, char separator) {
    super(pathname);
    this.separator = separator;
}
// ---- helper method(s) introduced by the refactoring ----
private CsvFile(URI uri, char separator) {
    super(uri);
    this.separator = separator;
}

public static CsvFile from(File parent, String child, char separator) {
    return new CsvFile(new File(parent, child), separator);
}

public static CsvFile from(String parent, String child, char separator) {
    return new CsvFile(new File(parent, child), separator);
}

public static CsvFile from(String pathname, char separator) {
    return new CsvFile(pathname, separator);
}

public static CsvFile from(URI uri, char separator) {
    return new CsvFile(uri, separator);
}

