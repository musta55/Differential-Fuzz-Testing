public OmOption(String group, int order, String opt, String longOpt, boolean hasArg, String description) {
    super(opt, longOpt, hasArg, description);
    this.group = group;
    this.order = order;
}