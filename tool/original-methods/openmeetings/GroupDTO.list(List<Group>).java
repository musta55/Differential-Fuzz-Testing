public static List<GroupDTO> list(List<Group> l) {
    List<GroupDTO> gList = new ArrayList<>();
    if (l != null) {
        for (Group g : l) {
            gList.add(new GroupDTO(g));
        }
    }
    return gList;
}