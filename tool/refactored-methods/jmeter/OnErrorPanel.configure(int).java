public void configure(int errorAction) {
    buttonMap.forEach((key, button) -> button.setSelected(key == errorAction));
}