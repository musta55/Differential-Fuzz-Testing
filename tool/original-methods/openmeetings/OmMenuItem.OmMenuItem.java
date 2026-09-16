public OmMenuItem(String title, String desc, IconType icon, List<INavbarComponent> items) {
    this.title = title;
    this.desc = desc;
    this.icon = icon;
    this.items.addAll(items);
}