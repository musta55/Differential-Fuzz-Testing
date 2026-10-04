@Override
public BasicTensorBlock call(BasicTensorBlock arg0) throws Exception {
    if (_deepCopy)
        return new BasicTensorBlock(arg0);
    else
        return arg0;
}