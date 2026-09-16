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
    delegate = new EditorDelegate() {

        private static final long serialVersionUID = 240L;

        @Override
        public void setValue(Object value) {
            editorComponent.setText((value != null) ? value.toString() : "");
        }

        @Override
        public Object getCellEditorValue() {
            return editorComponent.getText();
        }
    };
    editorComponent.addFocusListener(delegate);
}