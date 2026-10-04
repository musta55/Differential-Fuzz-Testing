public String[] getTextLines() {
    int numLines = mTextArea.getLineCount();
    String[] lines = new String[numLines];
    for (int i = 0; i < numLines; i++) {
        try {
            int start = getLineStartOffset(i);
            int end = getLineEndOffset(i);
            lines[i] = extractText(start, end);
        } catch (BadLocationException e) {
            throw new IllegalStateException("Could not read line " + i, e);
        }
    }
    return lines;
}
// ---- helper method(s) introduced by the refactoring ----
private int getLineStartOffset(int line) throws BadLocationException {
    return mTextArea.getLineStartOffset(line);
}

private int getLineEndOffset(int line) throws BadLocationException {
    int end = mTextArea.getLineEndOffset(line);
    if (line == mTextArea.getLineCount() - 1) {
        end++;
    }
    return end;
}

private String extractText(int start, int end) throws BadLocationException {
    return mTextArea.getText(start, end - start - 1);
}

