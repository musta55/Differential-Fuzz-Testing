public static ParseInfo ctxAndFilenameToParseInfo(ParserRuleContext ctx, String fname) {
    ParseInfo pi = new ParseInfo() {

        private int beginLine;

        private int beginColumn;

        private int endLine;

        private int endColumn;

        private String text;

        private String filename;

        @Override
        public void setBeginLine(int beginLine) {
            this.beginLine = beginLine;
        }

        @Override
        public void setBeginColumn(int beginColumn) {
            this.beginColumn = beginColumn;
        }

        @Override
        public void setEndLine(int endLine) {
            this.endLine = endLine;
        }

        @Override
        public void setEndColumn(int endColumn) {
            this.endColumn = endColumn;
        }

        @Override
        public void setText(String text) {
            this.text = text;
        }

        @Override
        public void setFilename(String filename) {
            this.filename = filename;
        }

        @Override
        public int getBeginLine() {
            return beginLine;
        }

        @Override
        public int getBeginColumn() {
            return beginColumn;
        }

        @Override
        public int getEndLine() {
            return endLine;
        }

        @Override
        public int getEndColumn() {
            return endColumn;
        }

        @Override
        public String getText() {
            return text;
        }

        @Override
        public String getFilename() {
            return filename;
        }
    };
    pi.setBeginLine(ctx.start.getLine());
    pi.setBeginColumn(ctx.start.getCharPositionInLine());
    pi.setEndLine(ctx.stop.getLine());
    pi.setEndColumn(ctx.stop.getCharPositionInLine());
    // preserve whitespace if possible
    if ((ctx.start != null) && (ctx.stop != null) && (ctx.start.getStartIndex() != -1) && (ctx.stop.getStopIndex() != -1) && (ctx.start.getStartIndex() <= ctx.stop.getStopIndex()) && (ctx.start.getInputStream() != null)) {
        String text = ctx.start.getInputStream().getText(Interval.of(ctx.start.getStartIndex(), ctx.stop.getStopIndex()));
        if (text != null) {
            text = text.trim();
        }
        pi.setText(text);
    } else {
        String text = ctx.getText();
        if (text != null) {
            text = text.trim();
        }
        pi.setText(text);
    }
    pi.setFilename(fname);
    return pi;
}