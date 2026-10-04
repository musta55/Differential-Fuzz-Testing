@Entrance
public final void combine(@SourceFrom double count) {
    if (count > this.value) {
        this.value = count;
    }
}