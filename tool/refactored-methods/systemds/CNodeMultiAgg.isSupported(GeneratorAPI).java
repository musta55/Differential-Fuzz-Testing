@Override
public boolean isSupported(GeneratorAPI api) {
    return api == GeneratorAPI.JAVA && _inputs.stream().allMatch(in -> in.isSupported(api));
}