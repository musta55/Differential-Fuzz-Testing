public COVOperator(COV op, int numThreads) {
    super(true);
    this.fn = op;
    this._numThreads = numThreads;
}