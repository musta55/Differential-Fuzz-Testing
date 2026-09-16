@Override
public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    } else if (obj instanceof ParameterizedType) {
        ParameterizedType that = (ParameterizedType) obj;
        Type thatOwnerType = that.getOwnerType();
        Type thatRawType = that.getRawType();
        return (ownerType == null ? thatOwnerType == null : ownerType.equals(thatOwnerType)) && (rawType == null ? thatRawType == null : rawType.equals(thatRawType)) && Arrays.equals(actualTypeArguments, that.getActualTypeArguments());
    } else {
        return false;
    }
}