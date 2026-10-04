@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.getClass().getSimpleName());
    sb.append(" ID: ");
    sb.append(ID);
    sb.append(" Level: ");
    sb.append(level);
    sb.append(" ExecType: ");
    sb.append(execType);
    sb.append(" Intermediate: ");
    sb.append(producesIntermediateOutput);
    return sb.toString();
}