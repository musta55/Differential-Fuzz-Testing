private List<String> performOperation(List<Integer> numbers) {
    return numbers.stream().map(number -> String.valueOf(number * 2)).collect(Collectors.toList());
}