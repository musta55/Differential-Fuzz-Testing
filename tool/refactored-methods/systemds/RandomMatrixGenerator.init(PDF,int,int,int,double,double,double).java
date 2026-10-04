/**
 * Initializes internal data structures. Called by Constructor
 * @param pdf    probability density function
 * @param r      number of rows
 * @param c      number of columns
 * @param blen   rows/cols per block
 * @param sp     sparsity (0 = completely sparse, 1 = completely dense)
 * @param min    minimum of range of random numbers
 * @param max    maximum of range of random numbers
 */
public void init(PDF pdf, int r, int c, int blen, double sp, double min, double max) {
    _pdf = pdf;
    _rows = r;
    _cols = c;
    _blocksize = blen;
    _sparsity = sp;
    _min = min;
    _max = max;
    setupValuePRNGForNonPoisson();
}
// ---- helper method(s) introduced by the refactoring ----
private void setupValuePRNGForNonPoisson() {
    switch(_pdf) {
        case NORMAL:
            _valuePRNG = new NormalPRNGenerator();
            break;
        case UNIFORM:
            _valuePRNG = new UniformPRNGenerator();
            break;
        case CB_UNIFORM:
            _valuePRNG = new PhiloxUniformCBPRNGenerator();
            break;
        case CB_NORMAL:
            _valuePRNG = new PhiloxNormalCBPRNGenerator();
            break;
        default:
            throw new DMLRuntimeException("Unsupported probability density function");
    }
}

private void setupValuePRNGForPoisson() {
    if (_pdf == PDF.POISSON) {
        if (_mean <= 0)
            throw new DMLRuntimeException("Invalid parameter (" + _mean + ") for Poisson distribution.");
        _valuePRNG = new PoissonPRNGenerator(_mean);
    } else {
        setupValuePRNGForNonPoisson();
    }
}

