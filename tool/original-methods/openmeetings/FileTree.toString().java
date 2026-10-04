@Override
public String toString() {
    String val = "FileTree[type ='" + (item == null ? "root" : item.getType()) + "'";
    if (item != null) {
        val += ", name='" + item.getName() + "'";
    }
    if (!children.isEmpty()) {
        val += ", children='" + children + "'";
    }
    return val + "]";
}