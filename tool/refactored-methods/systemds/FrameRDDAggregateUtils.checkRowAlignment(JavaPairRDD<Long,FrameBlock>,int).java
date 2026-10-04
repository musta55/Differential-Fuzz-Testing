public static Tuple2<Boolean, Integer> checkRowAlignment(JavaPairRDD<Long, FrameBlock> in, int blen) {
    JavaRDD<Tuple5<Boolean, Long, Integer, Integer, Boolean>> rowRdd = mapToRowInfo(in, blen);
    Tuple5<Boolean, Long, Integer, Integer, Boolean> result = foldRowInfo(rowRdd);
    return new Tuple2<>(result._1(), result._3());
}
// ---- helper method(s) introduced by the refactoring ----
private static JavaRDD<Tuple5<Boolean, Long, Integer, Integer, Boolean>> mapToRowInfo(JavaPairRDD<Long, FrameBlock> in, int blen) {
    return in.map(tuple -> {
        long key = tuple._1();
        FrameBlock blk = tuple._2();
        return new Tuple5<>(true, key, blen == -1 ? blk.getNumRows() : blen, blk.getNumRows(), true);
    });
}

private static Tuple5<Boolean, Long, Integer, Integer, Boolean> foldRowInfo(JavaRDD<Tuple5<Boolean, Long, Integer, Integer, Boolean>> rowRdd) {
    return rowRdd.fold(null, (tuple1, tuple2) -> {
        if (tuple1 == null)
            return tuple2;
        if (tuple2 == null)
            return tuple1;
        if (!tuple1._1())
            return tuple1;
        if (!tuple2._1())
            return tuple2;
        int max1 = tuple1._3();
        int min1 = tuple1._4();
        long minIndex1 = tuple1._2();
        int max2 = tuple2._3();
        int min2 = tuple2._4();
        long minIndex2 = tuple2._2();
        boolean isSingleBlock1 = tuple1._5();
        boolean isSingleBlock2 = tuple2._5();
        boolean minIndexComp = minIndex1 > minIndex2;
        if (max1 == max2) {
            if (min1 == max1) {
                if (min2 == max2)
                    return new Tuple5<>(true, minIndexComp ? minIndex1 : minIndex2, max1, max1, false);
                else if (!minIndexComp)
                    return new Tuple5<>(true, minIndex2, max1, min2, false);
            } else {
                if (min2 == max2 && minIndexComp)
                    return new Tuple5<>(true, minIndex1, max1, min1, false);
            }
        } else {
            if (max1 > max2 && min1 == max1 && isSingleBlock2 && minIndex1 < minIndex2)
                return new Tuple5<>(true, minIndex2, max1, min2, false);
            if (max1 < max2 && min2 == max2 && isSingleBlock1 && minIndex2 < minIndex1)
                return new Tuple5<>(true, minIndex1, max2, min1, false);
        }
        return new Tuple5<>(false, null, null, null, null);
    });
}

