@Override
public void validate(IValidatable<String> pass) {
    if (badLength(pass.getValue())) {
        error(pass, "bad.password.short", Map.of("0", getMinPasswdLength()));
    }
    if (noLowerCase(pass.getValue())) {
        error(pass, "bad.password.lower");
    }
    if (noUpperCase(pass.getValue())) {
        error(pass, "bad.password.upper");
    }
    if (noDigit(pass.getValue())) {
        error(pass, "bad.password.digit");
    }
    if (noSymbol(pass.getValue())) {
        error(pass, "bad.password.special");
    }
    if (hasStopWords(pass.getValue())) {
        error(pass, "bad.password.stop");
    }
}