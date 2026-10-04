public COVOperator(COV op, int numThreads) {
    super(true);
    fn = op;
    _numThreads = numThreads;
}