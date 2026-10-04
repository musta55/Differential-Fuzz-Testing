@Override
public String getTemplateInfo() {
    StringBuilder sb = new StringBuilder();
    sb.append("SPOOF MULTIAGG [aggOps=");
    sb.append(Arrays.toString(_aggOps.toArray(new AggOp[0])));
    sb.append("]");
    return sb.toString();
}