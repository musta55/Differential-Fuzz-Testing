public File getOutputFile(int channel) {
    String ext = null;
    String fName = getOutputFile().getName();
    int idx = fName.lastIndexOf('.');
    if (idx >= 0 && idx < fName.length() - 1) {
        String backedName = fName;
        fName = fName.substring(0, idx);
        ext = backedName.substring(idx + 1);
    } else {
        ext = "";
    }
    if (channel > 0) {
        fName += "-" + channel + "." + ext;
    } else {
        fName += "." + ext;
    }
    return new File(getOutputFile().getParentFile(), fName);
}