// Body changed but semantically equivalent (integer add is commutative/associative,
// overflow wraps identically) -> EQUIVALENT.
public int combine(int a, int b) {
    return a + b + base;
}