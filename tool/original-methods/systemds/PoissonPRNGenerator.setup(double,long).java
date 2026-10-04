public void setup(double mean, long sd) {
    seed = sd;
    SynchronizedRandomGenerator srg = new SynchronizedRandomGenerator(new Well1024a());
    srg.setSeed(seed);
    _pdist = new PoissonDistribution(srg, _mean, PoissonDistribution.DEFAULT_EPSILON, PoissonDistribution.DEFAULT_MAX_ITERATIONS);
}