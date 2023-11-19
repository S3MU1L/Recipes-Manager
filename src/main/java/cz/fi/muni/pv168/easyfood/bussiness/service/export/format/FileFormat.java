package cz.fi.muni.pv168.easyfood.bussiness.service.export.format;

/**
 * The interface for providing an observer of the {@link Format}
 */
public interface FileFormat {

    /**
     * Gets the {@link Format} of the implementation.
     */
    Format getFormat();
}
