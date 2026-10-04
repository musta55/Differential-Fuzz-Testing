public void addInput(CNode in) {
    // check for duplicate entries or literals
    // Note: this duplicate check is circumvented for tsmm based outer products (see CNodeOuterProduct constructor)
    if (containsInput(in) || in.isLiteral())
        return;
    _inputs.add(in);
}