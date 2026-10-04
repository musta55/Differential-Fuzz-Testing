public CountDistinctSketch(Operator op) {
    if (!(op instanceof CountDistinctOperator)) {
        throw new DMLRuntimeException(String.format("Cannot create %s with given operator", CountDistinctSketch.class.getSimpleName()));
    }
    this.op = (CountDistinctOperator) op;
    if (this.op.getDirection() == null) {
        throw new DMLRuntimeException("No direction was set for the operator");
    }
    if (!this.op.getDirection().isRow() && !this.op.getDirection().isCol() && !this.op.getDirection().isRowCol()) {
        throw new DMLRuntimeException(String.format("Unexpected direction: %s", this.op.getDirection()));
    }
}