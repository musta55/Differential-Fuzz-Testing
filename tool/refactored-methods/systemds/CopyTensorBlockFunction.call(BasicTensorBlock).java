@Override
public BasicTensorBlock call(BasicTensorBlock tensorBlock) throws Exception {
    if (_deepCopy)
        return new BasicTensorBlock(tensorBlock);
    else
        return tensorBlock;
}