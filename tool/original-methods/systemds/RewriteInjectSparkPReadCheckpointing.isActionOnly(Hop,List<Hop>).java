private boolean isActionOnly(Hop hop, List<Hop> parents) {
    // if the number of consumers of this hop is equal to 1 and no more
    // then do not cache block unless that one operation is transient write
    if (parents.size() == 1) {
        return !(//
        parents.get(0) instanceof DataOp && ((DataOp) parents.get(0)).getOp() == OpOpData.TRANSIENTWRITE);
    } else
        return false;
}