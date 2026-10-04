@Override
public boolean isSupported(GeneratorAPI api) {
    boolean is_supported = (api == GeneratorAPI.JAVA);
    int i = 0;
    while (is_supported && i < _inputs.size()) {
        CNode in = _inputs.get(i++);
        is_supported = in.isSupported(api);
    }
    return is_supported;
}