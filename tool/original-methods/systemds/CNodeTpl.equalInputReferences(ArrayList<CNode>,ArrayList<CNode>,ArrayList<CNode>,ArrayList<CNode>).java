protected static boolean equalInputReferences(ArrayList<CNode> current1, ArrayList<CNode> current2, ArrayList<CNode> input1, ArrayList<CNode> input2) {
    boolean ret = (current1.size() == current2.size());
    for (int i = 0; ret && i < current1.size(); i++) ret &= equalInputReferences(current1.get(i), current2.get(i), input1, input2);
    return ret;
}