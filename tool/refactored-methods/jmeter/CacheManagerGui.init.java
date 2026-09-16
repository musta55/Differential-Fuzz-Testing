/**
 * Initialize the components and layout of this component.
 * WARNING: called from ctor so must not be overridden (i.e. must be private or final)
 */
private void init() {
    setLayout(new BorderLayout(0, 5));
    setBorder(makeBorder());
    // $NON-NLS-1$
    clearEachIteration = createCheckBox("clear_cache_per_iter", false);
    // $NON-NLS-1$
    controlledByThreadGroup = createCheckBox("cache_clear_controlled_by_threadgroup", false);
    controlledByThreadGroup.setActionCommand(CONTROLLED_BY_THREADGROUP);
    controlledByThreadGroup.addActionListener(this);
    // $NON-NLS-1$
    useExpires = createCheckBox("use_expires", false);
    JPanel northPanel = new JPanel();
    northPanel.setLayout(new VerticalLayout(5, VerticalLayout.BOTH));
    northPanel.add(makeTitlePanel());
    northPanel.add(clearEachIteration);
    northPanel.add(controlledByThreadGroup);
    northPanel.add(useExpires);
    // $NON-NLS-1$
    JLabel label = new JLabel(JMeterUtils.getResString("cache_manager_size"));
    maxCacheSize = new JTextField(20);
    maxCacheSize.setName(CacheManager.MAX_SIZE);
    label.setLabelFor(maxCacheSize);
    JPanel maxCacheSizePanel = new JPanel(new BorderLayout(5, 0));
    maxCacheSizePanel.add(label, BorderLayout.WEST);
    maxCacheSizePanel.add(maxCacheSize, BorderLayout.CENTER);
    northPanel.add(maxCacheSizePanel);
    add(northPanel, BorderLayout.NORTH);
}
// ---- helper method(s) introduced by the refactoring ----
private static JCheckBox createCheckBox(String resourceKey, boolean selected) {
    return new JCheckBox(JMeterUtils.getResString(resourceKey), selected);
}

