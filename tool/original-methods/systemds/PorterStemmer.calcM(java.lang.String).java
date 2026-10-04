/*
	 * m() measures the number of consonant sequences between 0 and j. if c is a consonant sequence and v a vowel
	 * sequence, and <..> indicates arbitrary presence,
	 * 
	 * <c><v> gives 0 <c>vc<v> gives 1 <c>vcvc<v> gives 2 <c>vcvcvc<v> gives 3 ....
	 */
private static int calcM(String word) {
    int l = word.length();
    int count = 0;
    boolean currentConst = false;
    for (int c = 0; c < l; c++) {
        if (cons(word, c)) {
            if (!currentConst && c != 0) {
                count += 1;
            }
            currentConst = true;
        } else
            currentConst = false;
    }
    return count;
}