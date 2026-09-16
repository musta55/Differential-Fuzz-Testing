private void init() {
    // WARNING: called from ctor so must not be overridden (i.e. must be private or final)
    this.setLayout(new BorderLayout());
    // MAIN PANEL
    Border margin = new EmptyBorder(10, 10, 5, 10);
    this.setBorder(margin);
    // NAME
    this.add(makeTitlePanel(), BorderLayout.NORTH);
    // TEXTAREA LABEL
    JLabel textAreaLabel = // $NON-NLS-1$
    new JLabel(JMeterUtils.getResString("assertion_textarea_label"));
    textAreaLabel.setLabelFor(textArea);
    Box mainPanel = Box.createVerticalBox();
    mainPanel.add(textAreaLabel);
    // TEXTAREA
    textArea = new JTextArea();
    textArea.setEditable(false);
    textArea.setLineWrap(false);
    JScrollPane areaScrollPane = new JScrollPane(textArea);
    areaScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    areaScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    areaScrollPane.setPreferredSize(new Dimension(mainPanel.getWidth(), mainPanel.getHeight()));
    mainPanel.add(areaScrollPane);
    this.add(mainPanel, BorderLayout.CENTER);
}