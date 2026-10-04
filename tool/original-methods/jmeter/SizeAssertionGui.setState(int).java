/**
 * Set the state of the radio Button.
 * <p>
 * Allowed states are
 * <ul>
 * <li>{@link SizeAssertion#EQUAL}</li>
 * <li>{@link SizeAssertion#NOTEQUAL}</li>
 * <li>{@link SizeAssertion#GREATERTHAN}</li>
 * <li>{@link SizeAssertion#LESSTHAN}</li>
 * <li>{@link SizeAssertion#GREATERTHANEQUAL}</li>
 * <li>{@link SizeAssertion#LESSTHANEQUAL}</li>
 * </ul>
 * @param state One of the allowed states
 */
public void setState(int state) {
    if (state == SizeAssertion.EQUAL) {
        equalButton.setSelected(true);
        execState = state;
    } else if (state == SizeAssertion.NOTEQUAL) {
        notequalButton.setSelected(true);
        execState = state;
    } else if (state == SizeAssertion.GREATERTHAN) {
        greaterthanButton.setSelected(true);
        execState = state;
    } else if (state == SizeAssertion.LESSTHAN) {
        lessthanButton.setSelected(true);
        execState = state;
    } else if (state == SizeAssertion.GREATERTHANEQUAL) {
        greaterthanequalButton.setSelected(true);
        execState = state;
    } else if (state == SizeAssertion.LESSTHANEQUAL) {
        lessthanequalButton.setSelected(true);
        execState = state;
    }
}