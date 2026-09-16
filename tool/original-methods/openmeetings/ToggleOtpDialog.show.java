@Override
public Modal<User> show(IPartialPageRequestHandler target) {
    secret = otpManager.generateSecret();
    codes = otpManager.getRecoveryCodes();
    final User u = getModelObject();
    current.setModelObject(null);
    qr.add(AttributeModifier.replace(PARAM_SRC, otpManager.getQr(u.getAddress().getEmail(), secret)));
    codesArea.setModelObject(String.join("\n", codes));
    target.add(current, qr, codesArea);
    return super.show(target);
}