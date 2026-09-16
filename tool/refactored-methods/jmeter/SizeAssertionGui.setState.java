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
    switch(state) {
        case SizeAssertion.EQUAL:
            equalButton.setSelected(true);
            break;
        case SizeAssertion.NOTEQUAL:
            notequalButton.setSelected(true);
            break;
        case SizeAssertion.GREATERTHAN:
            greaterthanButton.setSelected(true);
            break;
        case SizeAssertion.LESSTHAN:
            lessthanButton.setSelected(true);
            break;
        case SizeAssertion.GREATERTHANEQUAL:
            greaterthanequalButton.setSelected(true);
            break;
        case SizeAssertion.LESSTHANEQUAL:
            lessthanequalButton.setSelected(true);
            break;
        default:
            throw new IllegalArgumentException("Invalid state: " + state);
    }
    execState = state;
}