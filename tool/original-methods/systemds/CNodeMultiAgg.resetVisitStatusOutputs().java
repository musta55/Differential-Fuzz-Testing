@Override
public void resetVisitStatusOutputs() {
    for (CNode output : _outputs) output.resetVisitStatus();
}