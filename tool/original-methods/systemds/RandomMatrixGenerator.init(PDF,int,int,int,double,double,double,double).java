/**
 * Instantiates a Random number generator with a specific poisson mean
 * @param pdf    probability density function
 * @param r      number of rows
 * @param c      number of columns
 * @param blen   rows/cols per block
 * @param sp     sparsity (0 = completely sparse, 1 = completely dense)
 * @param min    minimum of range of random numbers
 * @param max    maximum of range of random numbers
 * @param mean   the poisson mean
 */
public void init(PDF pdf, int r, int c, int blen, double sp, double min, double max, double mean) {
    _pdf = pdf;
    _rows = r;
    _cols = c;
    _blocksize = blen;
    _sparsity = sp;
    _min = min;
    _max = max;
    _mean = mean;
    setupValuePRNG();
}