@Override
public void read(IRequestParameters parameters) {
    super.read(parameters);
    String url = parameters.getParameterValue("codebase").toString(OpenmeetingsVariables.getBaseUrl());
    StringBuilder sb = cleanUrl(url);
    if (sb.charAt(sb.length() - 1) != '/') {
        sb.append('/');
    }
    baseUrl = sb.toString();
    codebase = sb.append("screenshare").toString();
    settings = parameters.getParameterValue("settings").toString("{}");
}