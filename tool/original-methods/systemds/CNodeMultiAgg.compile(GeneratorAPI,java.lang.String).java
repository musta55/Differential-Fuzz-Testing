public int compile(GeneratorAPI api, String src) {
    if (api == GeneratorAPI.CUDA)
        // ToDo: compile MA
        return compile_nvrtc(SpoofCompiler.native_contexts.get(api), _genVar, src, _sparseSafe);
    return -1;
}