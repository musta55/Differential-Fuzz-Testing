@Override
public boolean equals(Object obj) {
    if (obj == null) {
        return false;
    }
    if (getClass() != obj.getClass()) {
        return false;
    }
    final OperatorIdPortNamePair other = (OperatorIdPortNamePair) obj;
    return hasSameOperatorId(other) && hasSamePortName(other);
}
// ---- helper method(s) introduced by the refactoring ----
private boolean hasSameOperatorId(OperatorIdPortNamePair other) {
    return this.operatorId == other.operatorId;
}

private boolean hasSamePortName(OperatorIdPortNamePair other) {
    return (this.portName == null) ? (other.portName == null) : this.portName.equals(other.portName);
}

