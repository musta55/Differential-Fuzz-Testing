@Override
public boolean controlStatement() {
    // Keep stop() statement in a separate statement block
    return (getType() == PRINTTYPE.STOP);
}