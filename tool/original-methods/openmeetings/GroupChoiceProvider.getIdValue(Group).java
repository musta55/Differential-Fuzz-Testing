@Override
public String getIdValue(Group choice) {
    Long id = choice.getId();
    return id == null ? null : "" + id;
}