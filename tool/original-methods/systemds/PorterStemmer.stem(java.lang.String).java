public static String stem(String word) {
    if (word.length() >= 3) {
        word = step1(word);
        word = step2(word);
        word = step3(word);
        word = step4(word);
        if (word.length() > 0)
            word = step5(word);
    }
    return word;
}