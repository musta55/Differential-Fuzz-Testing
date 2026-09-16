/**
 * Constructor.
 *
 * @param name         the name/long option
 * @param flags        the flags
 * @param id           the id/character option
 * @param description  description of option usage
 * @param incompatible descriptors for incompatible options
 */
public CLOptionDescriptor(final String name, final int flags, final int id, final String description, final CLOptionDescriptor[] incompatible) {
    checkFlags(flags);
    this.id = id;
    this.name = name;
    this.flags = flags;
    this.description = description;
    this.incompatible = new int[incompatible.length];
    for (int i = 0; i < incompatible.length; i++) {
        this.incompatible[i] = incompatible[i].getId();
    }
}