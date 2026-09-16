package org.apache.deltaspike.jpa.spi.descriptor.xml;

import java.io.Serializable;

public class EntityDescriptor extends AbstractEntityDescriptor
{
    private String tableName;

    public EntityDescriptor()
    {
    }

    public EntityDescriptor(String[] id, String version, String name, Class<?> entityClass,
            Class<? extends Serializable> idClass, AbstractEntityDescriptor parent,
            String tableName)
    {
        super(id, version, name, entityClass, idClass, parent);
        this.tableName = tableName;
    }
    
    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }
    
    @Override
    public String toString()
    {
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
}