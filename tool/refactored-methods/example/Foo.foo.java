public void foo() {
    Error wrapped = new Error(new NullPointerException());
    throw wrapped;
}