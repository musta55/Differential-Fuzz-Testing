/**
 * Constructor.
 *
 * @param name        the name/long option
 * @param flags       the flags
 * @param id          the id/character option
 * @param description description of option usage
 */
public CLOptionDescriptor(final String name, final int flags, final int id, final String description) {
    checkFlags(flags);
    this.id = id;
    this.name = name;
    this.flags = flags;
    this.description = description;
    this.incompatible = ((flags & DUPLICATES_ALLOWED) != 0) ? new int[0] : new int[] { id };
}