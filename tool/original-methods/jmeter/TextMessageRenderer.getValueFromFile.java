@Override
public String getValueFromFile(String filename, String encoding, boolean hasVariable, Cache<Object, Object> cache) {
    String text = (String) cache.get(new FileKey(filename, encoding), key -> getContent((FileKey) key));
    if (hasVariable) {
        text = new CompoundVariable(text).execute();
    }
    return text;
}