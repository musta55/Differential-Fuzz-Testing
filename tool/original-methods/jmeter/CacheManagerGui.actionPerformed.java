@Override
public void actionPerformed(ActionEvent e) {
    String action = e.getActionCommand();
    if (action.equals(CONTROLLED_BY_THREADGROUP)) {
        clearEachIteration.setEnabled(!controlledByThreadGroup.isSelected());
    }
}