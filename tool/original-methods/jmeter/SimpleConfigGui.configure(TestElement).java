/**
 * A newly created component can be initialized with the contents of a Test
 * Element object by calling this method. The component is responsible for
 * querying the Test Element object for the relevant information to display
 * in its GUI.
 * <p>
 * This implementation retrieves all key/value pairs from the TestElement
 * object and sets these values in the GUI.
 *
 * @param el
 *            the TestElement to configure
 */
@Override
public void configure(TestElement el) {
    super.configure(el);
    tableModel.clearData();
    PropertyIterator iter = el.propertyIterator();
    while (iter.hasNext()) {
        JMeterProperty prop = iter.next();
        tableModel.addRow(new Object[] { prop.getName(), prop.getStringValue() });
    }
    checkDeleteStatus();
}