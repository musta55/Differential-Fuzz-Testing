@Override
public int hashCode() {
    return Arrays.hashCode(actualTypeArguments) ^ (ownerType == null ? 0 : ownerType.hashCode()) ^ (rawType == null ? 0 : rawType.hashCode());
}