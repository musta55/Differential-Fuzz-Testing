public OmMenuItem(String title, String desc) {
    this(title, desc, null, List.of(), true);
}
// ---- helper method(s) introduced by the refactoring ----
private OmMenuItem(String title, String desc, IconType icon, List<INavbarComponent> items, boolean visible) {
    this.title = title;
    this.desc = desc;
    this.icon = icon;
    this.items.addAll(items);
    this.visible = visible;
}

