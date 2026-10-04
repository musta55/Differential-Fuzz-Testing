@Test
public void testGetAndIncrement() {
    AtomicRangeInteger atomicI = new AtomicRangeInteger(0, 10);
    for (int i = 0; i < 10; i++) {
        Assertions.assertEquals(i, atomicI.getAndIncrement());
    }
    Assertions.assertEquals(0, atomicI.getAndIncrement());
    Assertions.assertEquals(1, atomicI.get());
    Assertions.assertEquals(1, atomicI.intValue());
    Assertions.assertEquals(1, atomicI.longValue());
    Assertions.assertEquals(1, (int) atomicI.floatValue());
    Assertions.assertEquals(1, (int) atomicI.doubleValue());
}