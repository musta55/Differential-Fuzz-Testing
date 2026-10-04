@Test
public void testGetAndIncrement() {
    AtomicRangeInteger atomicI = new AtomicRangeInteger(0, 10);
    assertGetAndIncrementSequence(atomicI);
    assertGetAndIncrementWrapAround(atomicI);
    assertGetValueMethods(atomicI);
}
// ---- helper method(s) introduced by the refactoring ----
private void assertGetAndIncrementSequence(AtomicRangeInteger atomicI) {
    for (int i = 0; i < 10; i++) {
        Assertions.assertEquals(i, atomicI.getAndIncrement());
    }
}

private void assertGetAndIncrementWrapAround(AtomicRangeInteger atomicI) {
    Assertions.assertEquals(0, atomicI.getAndIncrement());
}

private void assertGetValueMethods(AtomicRangeInteger atomicI) {
    Assertions.assertEquals(1, atomicI.get());
    Assertions.assertEquals(1, atomicI.intValue());
    Assertions.assertEquals(1, atomicI.longValue());
    Assertions.assertEquals(1, (int) atomicI.floatValue());
    Assertions.assertEquals(1, (int) atomicI.doubleValue());
}

