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
    setupValuePRNG();
}