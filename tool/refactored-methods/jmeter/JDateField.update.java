/*
     * Update the current field. The addend is only expected to be +1/-1, but
     * other values will work. N.B. the roll() method only supports changes by a
     * single unit - up or down
     */
private void update(int addend, boolean shifted) {
    Calendar c = parseDate(getText());
    int pos = getCaretPosition();
    int field = posToField(pos);
    updateCalendarField(c, field, addend, shifted);
    String newDate = dateFormat.format(c.getTime());
    setText(newDate);
    if (pos > newDate.length()) {
        pos = newDate.length();
    }
    final int newPosition = pos;
    SwingUtilities.invokeLater(() -> setCaretPosition(newPosition));
}
// ---- helper method(s) introduced by the refactoring ----
private static void updateCalendarField(Calendar calendar, int field, int addend, boolean shifted) {
    if (shifted) {
        calendar.roll(field, true);
    } else {
        calendar.add(field, addend);
    }
}

