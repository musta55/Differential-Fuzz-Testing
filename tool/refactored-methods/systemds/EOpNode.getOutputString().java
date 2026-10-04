public String getOutputString() {
    return Optional.ofNullable(c1).map(c -> Optional.ofNullable(c2).map(c2 -> c + c2.toString()).orElse(c.toString())).orElse("''");
}