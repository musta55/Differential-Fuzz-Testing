@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(_name).append(" = function (");
    for (int i = 0; i < _inputParams.size(); i++) {
        sb.append(_inputParams.get(i).getName());
        if (i < _inputParams.size() - 1) {
            sb.append(", ");
        }
    }
    sb.append(") return (");
    for (int i = 0; i < _outputParams.size(); i++) {
        sb.append(_outputParams.get(i).getName());
        if (i < _outputParams.size() - 1) {
            sb.append(", ");
        }
    }
    sb.append(") {\n");
    for (StatementBlock block : _body) {
        sb.append(block.toString());
    }
    sb.append("}\n");
    return sb.toString();
}