@Override
public void setSeed(long seed) {
    rnorm.setSeed(seed);
    pair = new RandNPair();
    flag = false;
    pair.compute(rnorm);
}