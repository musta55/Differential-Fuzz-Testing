@Override
public FrameObject executeSerialMerge() {
    //always create new matrix object (required for nested parallelism)
    FrameObject foNew = null;
    if (LOG.isTraceEnabled())
        LOG.trace("ResultMerge (local, in-memory): Execute serial merge for output " + _output.hashCode() + " (fname=" + _output.getFileName() + ")");
    try {
        //get old and new output frame blocks
        FrameBlock outFB = _output.acquireRead();
        FrameBlock outFBNew = new FrameBlock(outFB);
        //create compare matrix if required (existing data in result)
        FrameBlock compare = outFB;
        int rlen = compare.getNumRows();
        int clen = compare.getNumColumns();
        //serial merge all inputs
        boolean flagMerged = false;
        for (FrameObject in : _inputs) {
            //check for empty inputs (no iterations executed)
            if (in != null && in != _output) {
                if (LOG.isTraceEnabled())
                    LOG.trace("ResultMergeFrame (local, in-memory): Merge input " + in.hashCode() + " (fname=" + in.getFileName() + ")");
                //read/pin input_i
                FrameBlock inMB = in.acquireRead();
                //core merge
                for (int j = 0; j < clen; j++) {
                    ValueType vt = compare.getSchema()[j];
                    for (int i = 0; i < rlen; i++) {
                        Object val1 = compare.get(i, j);
                        Object val2 = inMB.get(i, j);
                        if (UtilFunctions.compareTo(vt, val1, val2) != 0)
                            outFBNew.set(i, j, val2);
                    }
                }
                //unpin and clear in-memory input_i
                in.release();
                in.clearData();
                flagMerged = true;
            }
        }
        //create output and release old output
        foNew = flagMerged ? createNewFrameObject(_output, outFBNew) : _output;
        _output.release();
    } catch (Exception ex) {
        throw new DMLRuntimeException(ex);
    }
    //LOG.trace("ResultMerge (local, in-memory): Executed serial merge for output "+_output.getVarName()+" (fname="+_output.getFileName()+") in "+time.stop()+"ms");
    return foNew;
}