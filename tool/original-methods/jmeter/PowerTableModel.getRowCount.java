/**
 * Required by table model interface.
 *
 * @return the RowCount value
 */
@Override
public int getRowCount() {
    if (model == null) {
        return 0;
    }
    return model.size();
}