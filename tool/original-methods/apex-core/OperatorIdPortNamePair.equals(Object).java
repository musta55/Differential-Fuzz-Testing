@Override
public boolean equals(Object obj) {
    if (obj == null) {
        return false;
    }
    if (getClass() != obj.getClass()) {
        return false;
    }
    final OperatorIdPortNamePair other = (OperatorIdPortNamePair) obj;
    if (this.operatorId != other.operatorId) {
        return false;
    }
    if ((this.portName == null) ? (other.portName != null) : !this.portName.equals(other.portName)) {
        return false;
    }
    return true;
}