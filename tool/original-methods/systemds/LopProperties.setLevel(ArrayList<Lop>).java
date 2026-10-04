/*
	 * Function to compute the node level in the entire Lops DAG. 
	 *   level(v) = max( levels(v.inputs) ) + 1
	 */
public void setLevel(ArrayList<Lop> inputs) {
    int tmplevel = -1;
    if (inputs == null || inputs.isEmpty())
        tmplevel = 0;
    else {
        // find the max level among all inputs
        for (Lop in : inputs) {
            if (tmplevel < in.getLevel()) {
                tmplevel = in.getLevel();
            }
        }
        // this.level should be one more than the max
        tmplevel = tmplevel + 1;
    }
    setLevel(tmplevel);
}