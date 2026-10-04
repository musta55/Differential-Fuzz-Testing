protected static boolean equalInputReferences(CNode current1, CNode current2, ArrayList<CNode> input1, ArrayList<CNode> input2) {
    boolean ret = (current1.getInput().size() == current2.getInput().size());
    //process childs recursively
    for (int i = 0; ret && i < current1.getInput().size(); i++) ret &= equalInputReferences(current1.getInput().get(i), current2.getInput().get(i), input1, input2);
    if (ret && current1 instanceof CNodeData) {
        ret &= current2 instanceof CNodeData && indexOf(input1, (CNodeData) current1) == indexOf(input2, (CNodeData) current2);
    }
    return ret;
}