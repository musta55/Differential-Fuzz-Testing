public String getRandomName(String base) {
    Random r = new Random();
    int low = 0;
    int high = 100000000;
    int result = r.nextInt(high - low) + low;
    return base + "_" + result;
}