@Override
protected StringBuffer renderOptions(StringBuffer sb, int width, Options options, int leftPad, int descPad) {
    final String dpad = createPadding(descPad);
    final String optional = "(optional) ";
    Map<String, List<OmOption>> optList = getOptions(options, leftPad);
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