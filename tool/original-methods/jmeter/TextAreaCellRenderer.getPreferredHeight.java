public int getPreferredHeight() {
    // Allow override for unit testing only
    // TODO Find a better way
    if ("true".equals(System.getProperty("java.awt.headless"))) {
        // $NON-NLS-1$ $NON-NLS-2$
        return 10;
    } else {
        return rend.getPreferredSize().height + 5;
    }
}