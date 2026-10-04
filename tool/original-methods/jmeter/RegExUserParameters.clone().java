/**
 * A new instance is created for each thread group, and the
 * clone() method is then called to create copies for each thread in a
 * thread group.
 */
@Override
public Object clone() {
    RegExUserParameters up = (RegExUserParameters) super.clone();
    return up;
}