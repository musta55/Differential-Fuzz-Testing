public static String stem(String word) {
    if (word.length() >= 3) {
        word = applyStep1(word);
        word = applyStep2(word);
        word = applyStep3(word);
        word = applyStep4(word);
        if (!word.isEmpty()) {
            word = applyStep5(word);
        }
    }
    return word;
}
// ---- helper method(s) introduced by the refactoring ----
/* doublec(j) is true <=> j,(j-1) contain a double consonant. */
private static boolean hasDoubleConsonant(String word) {
    int length = word.length() - 1;
    if (length < 1)
        return false;
    if (word.charAt(length) != word.charAt(length - 1))
        return false;
    return isConsonant(word, length);
}

/*
	 * cvc(i) is true <=> i-2,i-1,i has the form consonant - vowel - consonant and also if the second c is not w,x or y.
	 * this is used when trying to restore an e at the end of a short word. e.g.
	 * 
	 * cav(e), lov(e), hop(e), crim(e), but snow, box, tray.
	 */
private static boolean endsWithCVC(String word) {
    int length = word.length();
    if (length < 3)
        return false;
    if (!isConsonant(word, length - 1) || isConsonant(word, length - 2) || !isConsonant(word, length - 3))
        return false;
    char lastChar = word.charAt(length - 1);
    return lastChar != 'w' && lastChar != 'x' && lastChar != 'y';
}

/* vowelinstem() is true <=> 0,...j contains a vowel */
private static boolean containsVowel(String word, String suffix) {
    int length = word.length() - suffix.length();
    for (int i = 0; i < length; i++) {
        if (!isConsonant(word, i)) {
            return true;
        }
    }
    return false;
}

/* cons(i) is true <=> b[i] is a consonant. */
private static boolean isConsonant(String word, int index) {
    char ch = word.charAt(index);
    if ("aeiou".indexOf(ch) != -1) {
        return false;
    }
    if (ch == 'y') {
        return index == 0 || !isConsonant(word, index - 1);
    }
    return true;
}

// process the collection of tuples to find which prefix matches the case.
private static String processMatches(String word, HashMap<String, String> suffixAndFix, int mCount) {
    String stemmed = null;
    Iterator<Entry<String, String>> iterator = suffixAndFix.entrySet().iterator();
    while (iterator.hasNext() && stemmed == null) {
        Entry<String, String> entry = iterator.next();
        stemmed = replaceSuffix(word, entry.getKey(), entry.getValue(), mCount);
        iterator.remove();
    }
    return stemmed;
}

// replace the suffix with suggestion
private static String replaceSuffix(String word, String suffix, String replacement, int mCount) {
    if (word.endsWith(suffix)) {
        String stem = word.substring(0, word.length() - suffix.length());
        if (calcM(stem) > mCount) {
            return stem + replacement;
        }
    }
    return word;
}

/* step1() gets rid of plurals and -ed or -ing. e.g.
	i.e., condition & suffix -> replacement
		SSES -> SS
		IES  -> I
		SS -> SS
		S -> ""
		(m > 0) EED -> EE
		vowelSequence(ED) -> ""
		vowelsequence(ING) -> ""
		any("at, bl, iz")  -> add(e)
		doubleconsonant and not("l", "s", "z") -> remove single letter from end
		(m == 1 and cvc) -> add(e)
		turns terminal y to i when there is another vowel in the stem.
   */
private static String applyStep1(String word) {
    if (word.endsWith("sses")) {
        word = StringUtils.removeEnd(word, "es");
    } else if (word.endsWith("ies")) {
        word = StringUtils.removeEnd(word, "ies") + "i";
    } else if (word.endsWith("s") && !word.endsWith("ss")) {
        word = StringUtils.removeEnd(word, "s");
    }
    if (word.endsWith("eed")) {
        if (calcM(word) > 1) {
            word = StringUtils.removeEnd(word, "d");
        }
    } else if (word.endsWith("ed") && containsVowel(word, "ed")) {
        word = StringUtils.removeEnd(word, "ed");
        word = applyStep1Adjustments(word);
    } else if (word.endsWith("ing") && containsVowel(word, "ing")) {
        word = StringUtils.removeEnd(word, "ing");
        word = applyStep1Adjustments(word);
    }
    if (word.endsWith("y") && containsVowel(word, "y")) {
        word = StringUtils.removeEnd(word, "y") + "i";
    }
    return word;
}

private static String applyStep1Adjustments(String word) {
    if (word.endsWith("at") || word.endsWith("bl") || word.endsWith("iz")) {
        return word + "e";
    }
    int m = calcM(word);
    char lastChar = word.charAt(word.length() - 1);
    if (hasDoubleConsonant(word) && !"lsz".contains(String.valueOf(lastChar))) {
        return word.substring(0, word.length() - 1);
    } else if (m == 1 && endsWithCVC(word)) {
        return word + "e";
    }
    return word;
}

// step2() maps double suffices to single ones
private static String applyStep2(String word) {
    if (word.isEmpty())
        return word;
    HashMap<String, String> suffixAndFix = new HashMap<>();
    suffixAndFix.put("ational", "ate");
    suffixAndFix.put("tional", "tion");
    suffixAndFix.put("enci", "ence");
    suffixAndFix.put("anci", "ance");
    suffixAndFix.put("izer", "ize");
    suffixAndFix.put("bli", "ble");
    suffixAndFix.put("alli", "al");
    suffixAndFix.put("entli", "ent");
    suffixAndFix.put("eli", "e");
    suffixAndFix.put("ousli", "ous");
    suffixAndFix.put("ization", "ize");
    suffixAndFix.put("ation", "ate");
    suffixAndFix.put("ator", "ate");
    suffixAndFix.put("alism", "al");
    suffixAndFix.put("iveness", "ive");
    suffixAndFix.put("fulness", "ful");
    suffixAndFix.put("ousness", "ous");
    suffixAndFix.put("aliti", "al");
    suffixAndFix.put("iviti", "ive");
    suffixAndFix.put("biliti", "ble");
    suffixAndFix.put("log", "logi");
    suffixAndFix.put("icate", "ic");
    suffixAndFix.put("ative", "");
    suffixAndFix.put("alize", "al");
    suffixAndFix.put("iciti", "ic");
    suffixAndFix.put("ical", "ic");
    return processMatches(word, suffixAndFix, 0);
}

// handles -ic-, -full, -ness etc.
private static String applyStep3(String word) {
    if (word.isEmpty())
        return word;
    HashMap<String, String> suffixAndFix = new HashMap<>();
    suffixAndFix.put("icate", "ic");
    suffixAndFix.put("ative", "");
    suffixAndFix.put("alize", "al");
    suffixAndFix.put("iciti", "ic");
    suffixAndFix.put("ical", "ic");
    suffixAndFix.put("ful", "");
    suffixAndFix.put("ness", "");
    return processMatches(word, suffixAndFix, 0);
}

// takes off -ant, -ence etc., in context <c>vcvc<v>
private static String applyStep4(String word) {
    String[] suffixes = { "al", "ance", "ence", "er", "ic", "able", "ible", "ant", "ement", "ment", "ent" };
    String stemmed = replaceSuffixes(word, suffixes, 1);
    if (stemmed == null && word.length() > 4) {
        char fourthLastChar = word.charAt(word.length() - 4);
        if (fourthLastChar == 's' || fourthLastChar == 't') {
            stemmed = replaceSuffix(word, "ion", "", 1);
        }
    }
    if (stemmed == null) {
        suffixes = new String[] { "ou", "ism", "ate", "iti", "ous", "ive", "ize" };
        stemmed = replaceSuffixes(word, suffixes, 1);
    }
    return stemmed != null ? stemmed : word;
}

private static String replaceSuffixes(String word, String[] suffixes, int mCount) {
    for (String suffix : suffixes) {
        String stemmed = replaceSuffix(word, suffix, "", mCount);
        if (stemmed != word) {
            return stemmed;
        }
    }
    return null;
}

// handle the last e and l
private static String applyStep5(String word) {
    if (word.endsWith("e") && calcM(word) > 1) {
        word = StringUtils.removeEnd(word, "e");
    }
    if (word.endsWith("e") && calcM(word) == 1 && !endsWithCVC(StringUtils.removeEnd(word, "e"))) {
        word = StringUtils.removeEnd(word, "e");
    }
    if (word.endsWith("l") && hasDoubleConsonant(word) && calcM(word) > 1) {
        word = word.substring(0, word.length() - 1);
    }
    return word;
}

