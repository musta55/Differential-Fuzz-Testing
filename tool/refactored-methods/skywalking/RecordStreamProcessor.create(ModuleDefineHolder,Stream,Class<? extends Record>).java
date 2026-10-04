public void create(ModuleDefineHolder moduleDefineHolder, Stream stream, Class<? extends Record> recordClass) throws StorageException {
    final StorageBuilderFactory storageBuilderFactory = moduleDefineHolder.find(StorageModule.NAME).provider().getService(StorageBuilderFactory.class);
    final Class<? extends StorageBuilder> builder = storageBuilderFactory.builderOf(recordClass, stream.builder());
    StorageDAO storageDAO = moduleDefineHolder.find(StorageModule.NAME).provider().getService(StorageDAO.class);
    IRecordDAO recordDAO = createRecordDAO(storageDAO, builder);
    ModelCreator modelSetter = moduleDefineHolder.find(CoreModule.NAME).provider().getService(ModelCreator.class);
    // Record stream doesn't read data from database during the persistent process. Keep the timeRelativeID == false always.
    Model model = modelSetter.add(recordClass, stream.scopeId(), new Storage(stream.name(), false, DownSampling.Second));
    ExportRecordWorker exportWorker = new ExportRecordWorker(moduleDefineHolder);
    RecordPersistentWorker persistentWorker = new RecordPersistentWorker(moduleDefineHolder, model, recordDAO, exportWorker);
    workers.put(recordClass, persistentWorker);
}
// ---- helper method(s) introduced by the refactoring ----
private IRecordDAO createRecordDAO(StorageDAO storageDAO, Class<? extends StorageBuilder> builder) throws StorageException {
    try {
        return storageDAO.newRecordDao(builder.getDeclaredConstructor().newInstance());
    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
        throw new UnexpectedException("Create " + builder.getSimpleName() + " record DAO failure.", e);
    }
}

