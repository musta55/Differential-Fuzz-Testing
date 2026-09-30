@Override
public void setup(OperatorContext context) {
    OutputPort<?> unifierOutputPort = getOutputPort();
    unifierOutputPort.setSink(new Sink<Object>() {

        @Override
        public void put(Object tuple) {
            outputPort.emit(tuple);
        }

        @Override
        public int getCount(boolean reset) {
            return 0;
        }
    });
    unifier.setup(context);
    spinMillis = context.getValue(OperatorContext.SPIN_MILLIS);
}