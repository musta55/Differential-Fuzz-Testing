@Override
public FrameBlock call(FrameBlock arg0) throws Exception {
    if (_deepCopy)
        return new FrameBlock(arg0);
    else
        return arg0;
}