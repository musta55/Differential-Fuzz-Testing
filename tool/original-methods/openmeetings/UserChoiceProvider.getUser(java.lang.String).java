public User getUser(String value) {
    User u = null;
    if (!Strings.isEmpty(value)) {
        String email = null;
        String fName = null;
        String lName = null;
        int idx = value.indexOf('<');
        if (idx > -1) {
            int idx1 = value.indexOf('>', idx);
            if (idx1 > -1) {
                email = value.substring(idx + 1, idx1);
                String name = value.substring(0, idx).replace("\"", "");
                int idx2 = name.indexOf(' ');
                if (idx2 > -1) {
                    fName = name.substring(0, idx2);
                    lName = name.substring(idx2 + 1);
                } else {
                    fName = "";
                    lName = name;
                }
            }
        } else {
            email = value;
        }
        if (!Strings.isEmpty(email)) {
            Validatable<String> valEmail = new Validatable<>(email);
            RfcCompliantEmailAddressValidator.getInstance().validate(valEmail);
            if (valEmail.isValid()) {
                u = userDao.getContact(email, fName, lName, getUserId());
            }
        }
    }
    return u;
}