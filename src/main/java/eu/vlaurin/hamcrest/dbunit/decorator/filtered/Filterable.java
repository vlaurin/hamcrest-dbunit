package eu.vlaurin.hamcrest.dbunit.decorator.filtered;

/**
 * A matcher that supports the {@link Filtered} decorator.
 *
 * @see Filtered
 * @since 0.1.0
 */
public interface Filterable {
    /**
     * Creates a {@link Filtered} decorator of the current matcher that only includes in the comparison the <strong>columns of the expected table</strong>.
     *
     * @return current matcher decorated with {@link Filtered}
     */
    Filtered filtered();

    /**
     * Creates a {@link Filtered} decorator of the current matcher that only includes in the comparison the <strong>columns specified</strong>.
     *
     * @param columns
     *         Names of the columns to include in the comparison
     * @return current matcher decorated with {@link Filtered}
     */
    Filtered filtered(String... columns);
}
