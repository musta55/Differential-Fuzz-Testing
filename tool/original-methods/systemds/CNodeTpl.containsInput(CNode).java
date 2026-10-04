/**
 * Checks for duplicates (object ref or varname).
 *
 * @param input new input node
 * @return true if duplicate, false otherwise
 */
private boolean containsInput(CNode input) {
    if (!(input instanceof CNodeData))
        return false;
    CNodeData input2 = (CNodeData) input;
    for (CNode cnode : _inputs) {
        if (!(cnode instanceof CNodeData))
            continue;
        CNodeData cnode2 = (CNodeData) cnode;
        if (cnode2._name.equals(input2._name) && cnode2._hopID == input2._hopID)
            return true;
    }
    return false;
}