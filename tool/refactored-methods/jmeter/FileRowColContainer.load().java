private void load() throws IOException, FileNotFoundException {
    try (BufferedReader myBread = Files.newBufferedReader(FileServer.getFileServer().getResolvedFile(fileName).toPath(), Charset.defaultCharset())) {
        String line = myBread.readLine();
        /*
             * N.B. Stop reading the file if we get a blank line: This allows
             * for trailing comments in the file
             */
        while (line != null && !line.trim().isEmpty()) {
            fileData.add(splitLine(line, delimiter));
            line = myBread.readLine();
        }
    } catch (IOException e) {
        fileData.clear();
        log.warn(e.toString());
        throw e;
    }
}