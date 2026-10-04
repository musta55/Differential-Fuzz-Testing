public void setOptional(String group, boolean val) {
    optional = Stream.of(group.split(",")).collect(Collectors.toMap(Function.identity(), s -> val));
}