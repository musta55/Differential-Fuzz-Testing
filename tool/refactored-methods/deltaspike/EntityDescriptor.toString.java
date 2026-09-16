@Override
public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("EntityDescriptor [");
    builder.append("entityClass=").append(getEntityClass().getName()).append(", ");
    builder.append("name=").append(getName()).append(", ");
    builder.append("idClass=").append(getIdClass().getName()).append(", ");
    builder.append("id=").append(getId()).append(", ");
    builder.append("superClass=").append(getParent()).append(", ");
    builder.append("tableName=").append(tableName).append("]");
    return builder.toString();
}