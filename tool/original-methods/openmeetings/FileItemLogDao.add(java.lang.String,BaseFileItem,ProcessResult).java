public FileItemLog add(String name, BaseFileItem f, ProcessResult r) {
    log.trace("Adding log: {}, {}, {}", name, f, r);
    FileItemLog l = new FileItemLog().setInserted(new Date()).setExitCode(r.getExitCode()).setFileId(f.getId()).setMessage(r.buildLogMessage()).setName(name).setOptional(r.isOptional());
    em.persist(l);
    return l;
}