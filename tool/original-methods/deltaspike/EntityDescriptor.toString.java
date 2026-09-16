@Override
public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("EntityDescriptor ").append("[entityClass=").append(getEntityClass().getName()).append(", name=").append(getName()).append(", idClass=").append(getIdClass().getName()).append(", id=").append(getId()).append(", superClass=").append(getParent()).append(", tableName=").append(tableName).append("]");
    return builder.toString();
}