@Override
public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals(ActionNames.EXIT) || e.getActionCommand().equals(ActionNames.UNDO) || e.getActionCommand().equals(ActionNames.REDO)) {
        doAction(e);
    }
}