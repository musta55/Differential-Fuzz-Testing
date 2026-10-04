public static Tuple2<Boolean, Integer> checkRowAlignment(JavaPairRDD<Long, FrameBlock> in, int blen) {
    JavaRDD<Tuple5<Boolean, Long, Integer, Integer, Boolean>> row_rdd = in.map((Function<Tuple2<Long, FrameBlock>, Tuple5<Boolean, Long, Integer, Integer, Boolean>>) in1 -> {
        long key = in1._1();
        FrameBlock blk = in1._2();
        return new Tuple5<>(true, key, blen == -1 ? blk.getNumRows() : blen, blk.getNumRows(), true);
    });
    Tuple5<Boolean, Long, Integer, Integer, Boolean> result = row_rdd.fold(null, (Function2<Tuple5<Boolean, Long, Integer, Integer, Boolean>, Tuple5<Boolean, Long, Integer, Integer, Boolean>, Tuple5<Boolean, Long, Integer, Integer, Boolean>>) (in1, in2) -> {
        //easy evaluation
        if (in1 == null)
            return in2;
        if (in2 == null)
            return in1;
        if (!in1._1())
            return in1;
        if (!in2._1())
            return in2;
        //default evaluation
        int in1_max = in1._3();
        int in1_min = in1._4();
        //Index of Block with min nr rows --> Block with largest index ( --> last block index)
        long in1_min_index = in1._2();
        int in2_max = in2._3();
        int in2_min = in2._4();
        long in2_min_index = in2._2();
        boolean in1_isSingleBlock = in1._5();
        boolean in2_isSingleBlock = in2._5();
        boolean min_index_comp = in1_min_index > in2_min_index;
        if (in1_max == in2_max) {
            if (in1_min == in1_max) {
                if (in2_min == in2_max)
                    return new Tuple5<>(true, min_index_comp ? in1_min_index : in2_min_index, in1_max, in1_max, false);
                else if (!min_index_comp)
                    return new Tuple5<>(true, in2_min_index, in1_max, in2_min, false);
                //else: in1_min_index > in2_min_index -->  in2 is not aligned
            } else {
                if (in2_min == in2_max)
                    if (min_index_comp)
                        return new Tuple5<>(true, in1_min_index, in1_max, in1_min, false);
                //else: in1_min_index < in2_min_index -->  in1 is not aligned
                //else: both contain blocks with less blocks than max
            }
        } else {
            if (in1_max > in2_max && in1_min == in1_max && in2_isSingleBlock && in1_min_index < in2_min_index)
                return new Tuple5<>(true, in2_min_index, in1_max, in2_min, false);
            /* else:
				in1_min != in1_max -> both contain blocks with less blocks than max
				!in2_isSingleBlock -> in2 contains at least 2 blocks with less blocks than in1's max
				in1_min_index > in2_min_index -> in2's min block != lst block
				 */
            if (in1_max < in2_max && in2_min == in2_max && in1_isSingleBlock && in2_min_index < in1_min_index)
                return new Tuple5<>(true, in1_min_index, in2_max, in1_min, false);
        }
        return new Tuple5<>(false, null, null, null, null);
    });
    return new Tuple2<>(result._1(), result._3());
}