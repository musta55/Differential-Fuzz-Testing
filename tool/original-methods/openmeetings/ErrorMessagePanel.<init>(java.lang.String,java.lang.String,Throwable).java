public ErrorMessagePanel(String id, String msg, Throwable err) {
    super(id);
    log.error(msg, err);
    add(new Label("msg", msg));
    StringBuilderWriter sw = new StringBuilderWriter();
    err.printStackTrace(new PrintWriter(sw));
    add(new Label("err", sw.toString()));
}