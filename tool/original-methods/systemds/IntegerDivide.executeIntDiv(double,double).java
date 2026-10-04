/**
 * NOTE: The R semantics of integer divide a%/%b are to compute the
 * double division and subsequently cast to int. In case of a NaN
 * or +-INFINITY division result, the overall output is NOT cast to
 * int in order to prevent the special double values.
 *
 * @param in1 double input 1
 * @param in2 double input 2
 * @return result
 */
private static double executeIntDiv(double in1, double in2) {
    //compute normal double division
    double ret = in1 / in2;
    //check for NaN/+-INF intermediate (cast to int would eliminate it)
    if (Double.isNaN(ret) || Double.isInfinite(ret))
        return ret;
    //safe cast to int
    return UtilFunctions.toLong(ret);
}