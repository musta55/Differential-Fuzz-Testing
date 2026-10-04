@Override
public void actionPerformed(ActionEvent action) {
    String command = action.getActionCommand();
    if (SYSTEM.equals(command)) {
        setUpData();
        return;
    } else if (JMETER.equals(command)) {
        setUpData();
        return;
    }
}