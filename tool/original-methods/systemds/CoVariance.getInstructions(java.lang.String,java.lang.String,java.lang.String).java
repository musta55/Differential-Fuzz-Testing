/**
 * Function two generate CP instruction to compute unweighted covariance.
 * input1 -&gt; input column 1
 * input2 -&gt; input column 2
 */
@Override
public String getInstructions(String input1, String input2, String output) {
    return getInstructions(input1, input2, null, output);
}