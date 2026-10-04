@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("VarStats: [");
    sb.append("rlen = ");
    sb.append(_dc.getRows());
    sb.append(", clen = ");
    sb.append(_dc.getCols());
    sb.append(", nnz = ");
    sb.append(_dc.getNonZeros());
    sb.append(", inmem = ");
    sb.append(_inmem);
    sb.append("]");
    return sb.toString();
}