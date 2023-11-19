package cz.fi.muni.pv168.easyfood.bussiness.service.export.format;

import java.util.Collection;
import java.util.Collections;

/**
 * Base class representing a format.
 */
public record Format(String name, Collection<String> extensions) {

    /**
     * Gets a {@link Collection} of file extensions of the format.
     */
    @Override
    public Collection<String> extensions() {
        return Collections.unmodifiableCollection(extensions);
    }
}
