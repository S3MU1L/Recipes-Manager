package cz.fi.muni.pv168.easyfood.bussiness.service.export.batch;


import cz.fi.muni.pv168.easyfood.bussiness.service.export.DataManipulationException;
import cz.fi.muni.pv168.easyfood.bussiness.service.export.format.FileFormat;

/**
 * Generic mechanism, allowing to export a {@link Batch} of entities to a file.
 *
 * <p>Both the <i>format</i> and <i>encoding</i> of the file are to be defined and
 * documented by implementations of this interface.
 */
public interface BatchExporter extends FileFormat {

    /**
     * Exports the data from the {@link Batch} to a file.
     *
     * @param batch    entities to be exported (in the collection iteration order)
     * @param filePath absolute path of the export file (to be created or overwritten)
     * @throws DataManipulationException if the export file cannot be opened or written
     */
    void exportBatch(Batch batch, String filePath);
}
