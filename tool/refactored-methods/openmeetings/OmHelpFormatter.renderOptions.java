@Override
protected StringBuffer renderOptions(StringBuffer sb, int width, Options options, int leftPad, int descPad) {
    final String dpad = createPadding(descPad);
    final String optional = "(optional) ";
    Map<String, List<OmOption>> optList = categorizeOptions(options, leftPad);
    char[] delimiter = new char[width - 2];
    Arrays.fill(delimiter, '-');
    for (Entry<String, List<OmOption>> me : optList.entrySet()) {
        if (GENERAL_OPTION_GROUP.equals(me.getKey())) {
            sb.append("General options:").append(getNewLine());
        }
        for (OmOption option : me.getValue()) {
            StringBuilder optBuf = new StringBuilder(option.getHelpPrefix());
            if (optBuf.length() < maxPrefixLength) {
                optBuf.append(createPadding(maxPrefixLength - optBuf.length()));
            }
            optBuf.append(dpad);
            int nextLineTabStop = maxPrefixLength + descPad;
            if (option.isOptional(me.getKey())) {
                optBuf.append(optional);
            }
            if (option.getDescription() != null) {
                optBuf.append(option.getDescription());
            }
            renderWrappedText(sb, width, nextLineTabStop, optBuf.toString());
            sb.append(getNewLine());
        }
        sb.append(delimiter).append(getNewLine());
    }
    return sb;
}
// ---- helper method(s) introduced by the refactoring ----
@SuppressWarnings("unchecked")
private static List<OmOption> getRequiredOptions(Options opts) {
    OptionGroup group = ((List<OptionGroup>) opts.getRequiredOptions()).get(0);
    List<OmOption> result = new ArrayList<>();
    for (Option o : group.getOptions()) {
        result.add((OmOption) o);
    }
    Collections.sort(result, (o1, o2) -> o1.getOrder() - o2.getOrder());
    return result;
}

private Map<String, List<OmOption>> categorizeOptions(Options opts, int leftPad) {
    List<OmOption> reqOptions = getRequiredOptions(opts);
    Map<String, List<OmOption>> map = initializeOptionMap(reqOptions);
    populateOptionMap(opts, map, leftPad);
    sortAndOrganizeOptions(map, opts);
    return map;
}

private Map<String, List<OmOption>> initializeOptionMap(List<OmOption> reqOptions) {
    Map<String, List<OmOption>> map = new LinkedHashMap<>(reqOptions.size());
    map.put(GENERAL_OPTION_GROUP, new ArrayList<>());
    for (OmOption o : reqOptions) {
        map.put(o.getOpt(), new ArrayList<>());
    }
    return map;
}

private void populateOptionMap(Options opts, Map<String, List<OmOption>> map, int leftPad) {
    final String longOptSeparator = " ";
    final String lpad = createPadding(leftPad);
    final String lpadParam = createPadding(leftPad + 2);
    for (Option option : opts.getOptions()) {
        OmOption o = (OmOption) option;
        boolean skipOption = map.containsKey(o.getOpt());
        boolean mainOption = skipOption || o.getGroup() == null;
        StringBuilder optBuf = buildOptionPrefix(o, mainOption, lpad, lpadParam, longOptSeparator);
        setHelpPrefixAndMaxPrefixLength(o, optBuf);
        if (!skipOption) {
            addOptionToGroups(o, map);
        }
    }
}

private StringBuilder buildOptionPrefix(OmOption o, boolean mainOption, String lpad, String lpadParam, String longOptSeparator) {
    StringBuilder optBuf = new StringBuilder();
    if (o.getOpt() == null) {
        optBuf.append(mainOption ? lpad : lpadParam).append("   ").append(getLongOptPrefix()).append(o.getLongOpt());
    } else {
        optBuf.append(mainOption ? lpad : lpadParam).append(getOptPrefix()).append(o.getOpt());
        if (o.hasLongOpt()) {
            optBuf.append(',').append(getLongOptPrefix()).append(o.getLongOpt());
        }
    }
    if (o.hasArg()) {
        String argName = o.getArgName();
        if (argName != null && argName.length() == 0) {
            optBuf.append(' ');
        } else {
            optBuf.append(o.hasLongOpt() ? longOptSeparator : " ").append("<").append(argName != null ? o.getArgName() : getArgName()).append(">");
        }
    }
    return optBuf;
}

private void setHelpPrefixAndMaxPrefixLength(OmOption o, StringBuilder optBuf) {
    o.setHelpPrefix(optBuf);
    maxPrefixLength = Math.max(optBuf.length(), maxPrefixLength);
}

private void addOptionToGroups(OmOption o, Map<String, List<OmOption>> map) {
    String grp = o.getGroup() == null ? GENERAL_OPTION_GROUP : o.getGroup();
    String[] grps = grp.split(",");
    for (String g : grps) {
        map.get(g).add(o);
    }
}

private void sortAndOrganizeOptions(Map<String, List<OmOption>> map, Options opts) {
    for (Entry<String, List<OmOption>> me : map.entrySet()) {
        List<OmOption> options = me.getValue();
        Collections.sort(options, (o1, o2) -> {
            boolean o1mandatory = !o1.isOptional(me.getKey());
            boolean o2mandatory = !o2.isOptional(me.getKey());
            int shortNameVal = o1.getOpt() == null ? 1 : -1;
            int mandatoryVal = o1mandatory ? -1 : 1;
            return (o1mandatory && o2mandatory || !o1mandatory && !o2mandatory) ? shortNameVal : mandatoryVal;
        });
        if (opts.hasOption(me.getKey())) {
            options.add(0, (OmOption) opts.getOption(me.getKey()));
        }
    }
}

private static StringBuilder getRequiredOptionsString(Options opts) {
    String delim = "";
    StringBuilder result = new StringBuilder();
    for (Option o : getRequiredOptions(opts)) {
        result.append(delim).append("-").append(o.getOpt());
        delim = "|";
    }
    return result;
}

