@Override
public void resetVisitStatusOutputs() {
    _outputs.forEach(CNode::resetVisitStatus);
}