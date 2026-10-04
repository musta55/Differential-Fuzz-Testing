private static PRINTTYPE getPrintType(String type, List<Expression> expressions) {
    if (type.equalsIgnoreCase(Opcodes.PRINT.toString())) {
        if ((expressions == null) || (expressions.size() == 1)) {
            return PRINTTYPE.PRINT;
        } else {
            return PRINTTYPE.PRINTF;
        }
    } else if (type.equalsIgnoreCase(Opcodes.ASSERT.toString())) {
        return PRINTTYPE.ASSERT;
    } else if (type.equalsIgnoreCase(Opcodes.STOP.toString())) {
        return PRINTTYPE.STOP;
    } else
        throw new LanguageException("Unknown statement type: " + type);
}