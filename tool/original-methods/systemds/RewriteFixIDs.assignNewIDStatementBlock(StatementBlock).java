private void assignNewIDStatementBlock(StatementBlock sb) {
    // Reset the IDs in a depth-first manner
    if (sb.getLops() != null && !sb.getLops().isEmpty()) {
        for (Lop root : sb.getLops()) assignNewIDLop(root);
        sb.getLops().forEach(Lop::resetVisitStatus);
    }
}