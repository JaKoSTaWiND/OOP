package repositories;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import factories.EmployeeFactory;
import interfaces.employee.IEmployeeRepository;
import static jooq.generated.Tables.EMPLOYEES;
import jooq.generated.tables.records.EmployeesRecord;
import models.employeeModels.Cashier;
import models.employeeModels.Employee;
import models.employeeModels.Manager;

@Repository
public class JooqEmployeeRepository implements IEmployeeRepository {

    private final DSLContext dsl;

    public JooqEmployeeRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return dsl.selectFrom(EMPLOYEES)
                .fetch()
                .map(this::mapRecordToEmployee);
    }

    /**
     * Finds an employee by their unique database identifier.
     * <p>
     * This method retrieves a record from the {@code EMPLOYEES} table and uses 
     * {@link #mapRecordToEmployee(EmployeesRecord)} to reconstruct the appropriate 
     * domain object (e.g., {@link Manager} or {@link Cashier}).
     * </p>
     *
     * @param employeeId the primary key of the employee.
     * @return an {@link Optional} containing the employee if found, or empty if not.
     */
    @Override
    public Optional<Employee> findById(int employeeId) {
        return dsl.selectFrom(EMPLOYEES)
                .where(EMPLOYEES.ID.eq(employeeId))
                .fetchOptional()
                .map(this::mapRecordToEmployee);
    }

    /**
     * Searches for an employee by their full name, ignoring case sensitivity.
     * <p>
     * This is useful for search features where the user might not know the exact 
     * capitalization stored in the database. Internally, it maps the record 
     * to the specific subclass based on the {@code position} column.
     * </p>
     *
     * @param name the full name of the employee to search for.
     * @return an {@link Optional} with the found employee, or empty if no match exists.
     */
    @Override
    public Optional<Employee> findByName(String name) {
        return dsl.selectFrom(EMPLOYEES)
                .where(EMPLOYEES.FULL_NAME.equalIgnoreCase(name))
                .fetchOptional()
                .map(this::mapRecordToEmployee);
    }

    /**
     * Persists a new employee record into the database.
     * <p>
     * Uses Java 21+ switch pattern matching to extract role-specific data 
     * (register number, shift count for Cashiers, or team size for Managers) 
     * and maps them to the corresponding table columns.
     * </p>
     *
     * @param employee the employee instance to save (must not be null).
     */
    @Override
    public void save(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }

        var insertStep = dsl.insertInto(EMPLOYEES)
                .set(EMPLOYEES.FULL_NAME, employee.fullName())
                .set(EMPLOYEES.HOURLY_RATE, employee.hourlyRate())
                .set(EMPLOYEES.ISFULLTIME, employee.isFullTime())
                .set(EMPLOYEES.STARTED_AT, employee.startedAt());

        switch (employee) {
            case Cashier cashier -> {
                insertStep = insertStep.set(EMPLOYEES.POSITION, 1)
                          .set(EMPLOYEES.REGISTER_NUMBER, cashier.registerNumber())
                          .set(EMPLOYEES.SHIFT_COUNT, cashier.shiftCount())
                          .set(EMPLOYEES.TEAM_SIZE, (Integer) null);
            }
            case Manager manager -> {
                insertStep = insertStep.set(EMPLOYEES.POSITION, 2)
                          .set(EMPLOYEES.REGISTER_NUMBER, (Integer) null)
                          .set(EMPLOYEES.SHIFT_COUNT, (Integer) null)
                          .set(EMPLOYEES.TEAM_SIZE, manager.teamSize());
            }
            default -> throw new IllegalArgumentException("Unsupported employee type: " + employee.getClass());
        }

        insertStep.execute();
    }

    /**
     * Updates an existing employee record identified by its ID.
     * <p>
     * Re-evaluates the employee type during update to ensure that if a role 
     * has changed, the specific columns (like team size or register number) 
     * are correctly updated or reset to null.
     * </p>
     *
     * @param employee the employee instance with updated values.
     */
    @Override
    public void update(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }

        var updateStep = dsl.update(EMPLOYEES)
                .set(EMPLOYEES.FULL_NAME, employee.fullName())
                .set(EMPLOYEES.HOURLY_RATE, employee.hourlyRate())
                .set(EMPLOYEES.ISFULLTIME, employee.isFullTime())
                .set(EMPLOYEES.STARTED_AT, employee.startedAt());

        switch (employee) {
            case Cashier cashier -> {
                updateStep = updateStep.set(EMPLOYEES.POSITION, 1)
                          .set(EMPLOYEES.REGISTER_NUMBER, cashier.registerNumber())
                          .set(EMPLOYEES.SHIFT_COUNT, cashier.shiftCount())
                          .set(EMPLOYEES.TEAM_SIZE, (Integer) null);
            }
            case Manager manager -> {
                updateStep = updateStep.set(EMPLOYEES.POSITION, 2)
                          .set(EMPLOYEES.REGISTER_NUMBER, (Integer) null)
                          .set(EMPLOYEES.SHIFT_COUNT, (Integer) null)
                          .set(EMPLOYEES.TEAM_SIZE, manager.teamSize());
            }
            default -> throw new IllegalArgumentException("Unsupported employee type: " + employee.getClass());
        }

        int rows = updateStep.where(EMPLOYEES.ID.eq(employee.employeeId()))
                .execute();

        if (rows == 0) {
            throw new RuntimeException("Update failed: Employee with ID " + employee.employeeId() + " not found.");
        }
    }

    /**
     * Removes a employee record from the {@code EMPLOYEES} table by its unique ID.
     * <p>
     * This operation is permanent. If no record exists with the provided ID, 
     * the method completes successfully without making any changes to the database.
     * </p>
     *
     * @param id the primary key of the product to be deleted.
     */
    @Override
    public void delete(int id) {
        dsl.deleteFrom(EMPLOYEES)
                .where(EMPLOYEES.ID.eq(id))
                .execute();
    }

    /**
     * Internal mapper that transforms a database record into a concrete domain object.
     * <p>
     * Uses the {@code POSITION} column (1 for Cashier, 2 for Manager) to decide 
     * which factory method to call. Utilizes {@code Objects.requireNonNullElse} 
     * to safely handle nullable numeric columns from the DB.
     * </p>
     *
     * @param record the jOOQ record containing employee data.
     * @return a concrete instance of {@link Cashier} or {@link Manager}.
     */
    private Employee mapRecordToEmployee(EmployeesRecord record) {
        return switch (record.getPosition()) {
            case 1 -> EmployeeFactory.createCashierEmployee(
                        record.getId(),
                        record.getFullName(),
                        record.getHourlyRate(),
                        record.getIsfulltime(),
                        record.getStartedAt(),
                        Objects.requireNonNullElse(record.getRegisterNumber(), 0),
                        Objects.requireNonNullElse(record.getShiftCount(), 0)
                    );

            case 2 -> EmployeeFactory.createManagerEmployee(
                        record.getId(),
                        record.getFullName(),
                        record.getHourlyRate(),
                        record.getIsfulltime(),
                        record.getStartedAt(),
                        Objects.requireNonNullElse(record.getTeamSize(), 0)
                    );

            default -> throw new IllegalArgumentException("Unknown position: " + record.getPosition());
        };
    }
}
