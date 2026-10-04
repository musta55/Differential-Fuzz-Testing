@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < _targetList.size(); i++) sb.append(_targetList.get(i));
    sb.append(_isAccum ? " += " : " = ");
    if (_source instanceof StringIdentifier) {
        sb.append("\"");
        sb.append(_source.toString());
        sb.append("\"");
    } else {
        sb.append(_source.toString());
    }
    sb.append(";");
    return sb.toString();
}