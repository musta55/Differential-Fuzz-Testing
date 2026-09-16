//
// Constructors
//
/**
 * Constructs a <code>TableCellEditor</code> that uses a text field.
 */
public TextAreaTableCellEditor() {
    editorComponent = new JTextArea();
    editorComponent.setRows(3);
    this.clickCountToStart = 2;
    delegate = new EditorDelegate();
    editorComponent.addFocusListener(delegate);
}