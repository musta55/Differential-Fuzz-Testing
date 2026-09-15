@Override
public void run() {
    try {
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        try {
            while ((line = br.readLine()) != null) {
                if (!line.contains(" DEBUG ")) {
                    content.append(line);
                    content.append("\n");
                }
            }
        } finally {
            br.close();
        }
    } catch (IOException ex) {
        LOG.error("Caught exception", ex);
    }
}