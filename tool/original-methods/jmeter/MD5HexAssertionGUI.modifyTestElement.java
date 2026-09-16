/*
     * @param element
     */
@Override
public void modifyTestElement(TestElement element) {
    configureTestElement(element);
    String md5HexString = this.md5HexInput.getText();
    // initialize to empty string, this will fail the assertion
    if (md5HexString == null || md5HexString.length() == 0) {
        md5HexString = "";
    }
    ((MD5HexAssertion) element).setAllowedMD5Hex(md5HexString);
}