@Override
public void printUsage(PrintWriter pw, int width, String app, Options opts) {
    pw.println(String.format("usage: %1$s [%2$s] [options]", app, getReqOptionsString(opts)));
}