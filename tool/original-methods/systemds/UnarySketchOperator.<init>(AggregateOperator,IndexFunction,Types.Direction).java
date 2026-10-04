public UnarySketchOperator(AggregateOperator aop, IndexFunction indexFunction, Types.Direction direction) {
    super(aop, indexFunction);
    this.direction = direction;
}