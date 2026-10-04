public PrintStatement(PRINTTYPE type, List<Expression> expressions) {
    _type = type;
    if (expressions == null) {
        this.expressions = new ArrayList<>();
    } else {
        this.expressions = expressions;
    }
}