@Override
public void run() {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
        readStream(br);
    } catch (IOException ex) {
        LOG.error("Caught exception", ex);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void readStream(BufferedReader br) throws IOException {
    String line;
    while ((line = br.readLine()) != null) {
        if (!line.contains(" DEBUG ")) {
            content.append(line).append("\n");
        }
    }
}

