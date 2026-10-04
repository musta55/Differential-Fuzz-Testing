@Override
public String marshal(Recording.Status v) throws Exception {
    return "" + (v == null ? Recording.Status.NONE.name() : v.name());
}