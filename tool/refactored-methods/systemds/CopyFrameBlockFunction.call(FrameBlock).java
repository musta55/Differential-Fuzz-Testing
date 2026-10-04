@Override
public FrameBlock call(FrameBlock frameBlock) throws Exception {
    return _deepCopy ? new FrameBlock(frameBlock) : frameBlock;
}