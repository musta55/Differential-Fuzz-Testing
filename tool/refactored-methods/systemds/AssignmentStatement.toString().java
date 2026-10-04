@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    appendTargets(sb);
    appendOperator(sb);
    appendSource(sb);
    sb.append(";");
    return sb.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private void appendTargets(StringBuilder sb) {
    for (int i = 0; i < _targetList.size(); i++) {
        sb.append(_targetList.get(i));
    }
}

private void appendOperator(StringBuilder sb) {
    sb.append(_isAccum ? " += " : " = ");
}

private void appendSource(StringBuilder sb) {
    if (_source instanceof StringIdentifier) {
        sb.append("\"").append(_source.toString()).append("\"");
    } else {
        sb.append(_source.toString());
    }
}

