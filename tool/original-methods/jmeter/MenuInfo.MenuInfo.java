public MenuInfo(String label, JMeterGUIComponent item, String classFullName) {
    this.label = label;
    guiComp = item;
    className = classFullName;
    sortOrder = getSortOrderFromName(classFullName);
}