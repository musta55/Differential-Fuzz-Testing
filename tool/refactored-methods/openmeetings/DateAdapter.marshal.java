@Override
public String marshal(Date v) throws Exception {
    return Long.toString(v.getTime());
}