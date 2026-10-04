@Override
public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("OmLanguage [id=");
    builder.append(id);
    builder.append(", locale=");
    builder.append(locale);
    builder.append(", rtl=");
    builder.append(rtl);
    builder.append(", rangeStart=");
    builder.append(rangeStart);
    builder.append(", rangeEnd=");
    builder.append(rangeEnd);
    builder.append("]");
    return builder.toString();
}