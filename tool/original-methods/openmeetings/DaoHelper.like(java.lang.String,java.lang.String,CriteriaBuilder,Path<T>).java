public static <T> Predicate like(String col, String searchItem, CriteriaBuilder builder, Path<T> root) {
    Path<String> colPath = null;
    String[] cols = col.split("[.]");
    for (String s : cols) {
        colPath = colPath == null ? root.get(s) : colPath.get(s);
    }
    return builder.like(builder.lower(colPath), "%" + searchItem + "%");
}