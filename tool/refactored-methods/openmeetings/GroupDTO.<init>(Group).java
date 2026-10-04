public GroupDTO(Group g) {
    Objects.requireNonNull(g, "Group must not be null");
    this.id = g.getId();
    this.name = g.getName();
    this.tag = g.getTag();
}