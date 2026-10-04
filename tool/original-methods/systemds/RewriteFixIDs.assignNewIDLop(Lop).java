private void assignNewIDLop(Lop lop) {
    if (lop.isVisited())
        return;
    if (lop.getInputs().isEmpty()) {
        //leaf node
        lop.setNewID();
        lop.setVisited();
        return;
    }
    for (Lop input : lop.getInputs()) assignNewIDLop(input);
    lop.setNewID();
    lop.setVisited();
}