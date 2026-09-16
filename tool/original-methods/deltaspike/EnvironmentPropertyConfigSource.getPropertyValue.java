@Override
public String getPropertyValue(String key) {
    String val = super.getPropertyValue(key);
    if (val == null || val.isEmpty()) {
        val = super.getPropertyValue(key.replace('.', '_'));
    }
    return val;
}