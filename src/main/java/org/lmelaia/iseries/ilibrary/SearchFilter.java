package org.lmelaia.iseries.ilibrary;

/**
 * A search query implementation of a {@link TableEntryFilter}
 * that filters entries based on a search query String. Any
 * entries with information that matches the search query will
 * be accepted.
 * <p>
 * <p/>All text information within the entry is compared
 * to the filters search query. If the search query is
 * found anywhere within the entries text information
 * (not case sensitive), the entry match the search
 * query and filter will allow the entry though. Otherwise
 * the entry will be allowed through.
 */
public class SearchFilter implements TableEntryFilter {

    /**
     * Used to get a list of strings from a given entry,
     * all of which contain searchable information, each
     * of which can be matched against the search query.
     *
     * @param entry the entry to obtain the searchable
     *              information from.
     * @return a list of string, each of which contains
     * information that can be matched against the search
     * query.
     */
    private static String[] getSearchableStrings(ITableEntry entry) {
        return new String[]{
                entry.getName(), entry.getComments(), entry.getRating(),
                entry.getSynopsis(), entry.getType(), entry.getUUID()
        };
    }

    /**
     * The given search query as a String
     * converted to lowercase.
     */
    private final String searchQuery;

    /**
     * Creates a new SearchFilter that searches for enties
     * with any information matching the given search query.
     *
     * @param searchQuery the search query.
     */
    public SearchFilter(String searchQuery) {
        this.searchQuery = searchQuery.toLowerCase();
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p/>Matches all searchable information within
     * the entry against the filters search query. If
     * a match is found, the entry will be accepted,
     * otherwise it will be rejected.
     *
     * @param entry the ITableEntry to check.
     * @return {@code true} if the has some information
     * within that matches the provided {@link #searchQuery},
     * accepting the entry as a match, {@code false} otherwise.
     */
    @Override
    public boolean accept(ITableEntry entry) {
        return hasMatch(entry);
    }

    /**
     * Checks if the given entry has any searchable
     * information within that matches the search query.
     *
     * @param entry the entry to check for a match.
     * @return {@code true} if the entry is a match.
     */
    private boolean hasMatch(ITableEntry entry) {
        for (String searchString : getSearchableStrings(entry)) {
            if (searchString.toLowerCase().contains(searchQuery))
                return true;
        }
        return false;
    }

}
