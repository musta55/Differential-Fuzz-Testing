public String[] getTextLines() {
    int numLines = mTextArea.getLineCount();
    String[] lines = new String[numLines];
    for (int i = 0; i < numLines; i++) {
        try {
            int start = mTextArea.getLineStartOffset(i);
            // treats last line specially
            int end = mTextArea.getLineEndOffset(i);
            if (i == numLines - 1) {
                // Last line
                // Allow for missing terminator
                end++;
            }
            lines[i] = mTextArea.getText(start, end - start - 1);
        } catch (BadLocationException e) {
            // should not happen
            throw new IllegalStateException("Could not read line " + i, e);
        }
    }
    return lines;
}