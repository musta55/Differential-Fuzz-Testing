public OmOption(String group, String opt, String longOpt, boolean hasArg, String description, boolean optional) {
    this(group, 0, opt, longOpt, hasArg, description);
    setOptional(optional);
}