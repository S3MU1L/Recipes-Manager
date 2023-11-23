package cz.muni.fi.pv168.easyfood.export.csv;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.business.model.Employee;
import cz.muni.fi.pv168.easyfood.business.service.export.DataManipulationException;
import cz.muni.fi.pv168.easyfood.business.service.export.batch.Batch;
import cz.muni.fi.pv168.easyfood.business.service.export.batch.BatchExporter;
import cz.muni.fi.pv168.easyfood.business.service.export.format.Format;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * CSV-specific implementation of the {@link BatchExporter}.
 *
 * <p>The exported CSV file is UTF-8 file without any header and each line is of the
 * following format:<pre>{@code
 * guid,firstName,lastName,gender,birthDate,departmentGuid:departmentNumber:departmentName
 * }</pre>
 * for instance:<pre>{@code
 * c501536d-a3a5-4145-a996-b8f9cc86f0ff,John,Doe,MALE,1980-02-29,c501536d-a3a5-4145-a996-b8f9cc86f0ff:007:IT
 * }</pre>
 */
public class BatchCsvExporter implements BatchExporter {

    private static final String SEPARATOR = ",";
    private static final String DEPARTMENT_SEPARATOR = ":";
    private static final Format FORMAT = new Format("CSV", List.of("csv"));

    @Override
    public Format getFormat() {
        return FORMAT;
    }

    @Override
    public void exportBatch(Batch batch, String filePath) {

        try (var writer = Files.newBufferedWriter(Path.of(filePath), StandardCharsets.UTF_8)) {
            for (var employee : batch.employees()) {
                String line = createCsvLine(employee);
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException exception) {
            throw new DataManipulationException("Unable to write to file", exception);
        }
    }

    private String createCsvLine(Employee employee) {
        return serializeEmployee(employee) + SEPARATOR + serializeDepartment(employee.getDepartment());
    }

    private String serializeDepartment(Department department) {
        return String.join(DEPARTMENT_SEPARATOR,
                department.getGuid(),
                department.getNumber(),
                department.getName());
    }

    private String serializeEmployee(Employee employee) {
        return String.join(SEPARATOR,
                employee.getGuid(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getGender().name(),
                employee.getBirthDate().toString());
    }
}
