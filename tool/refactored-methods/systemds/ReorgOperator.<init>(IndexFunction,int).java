public ReorgOperator(IndexFunction p, int numThreads) {
    super(true);
    fn = p;
    setNumThreads(numThreads);
}