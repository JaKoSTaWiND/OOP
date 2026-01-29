package factories;

import java.math.BigDecimal;
import java.time.LocalDate;

import models.employeeModels.BaseCashier;
import models.employeeModels.BaseManager;
import models.employeeModels.Cashier;
import models.employeeModels.Employee;
import models.employeeModels.Manager;

public class EmployeeFactory {

    /**
     * Factory method used by the Repository to reconstruct a {@link Manager} 
     * from database records, including their specific team size.
     * <p>
     * This method leverages the {@code BaseManager.Builder} to instantiate an 
     * immutable manager object with data persisted in the {@code EMPLOYEES} table.
     * </p>
     *
     * @param employeeId the unique database ID.
     * @param name       the full name of the manager.
     * @param hourlyRate the hourly pay rate.
     * @param isFullTime the employment type flag.
     * @param startedAt  the date when the manager started working.
     * @param teamSize   the number of employees managed.
     * @return a fully initialized {@link Manager} instance.
     */
    public static Employee createManagerEmployee( // for Repository
            int employeeId,
            String name, 
            BigDecimal hourlyRate, 
            boolean isFullTime,
            LocalDate startedAt,
            int teamSize
        ) {
        return new BaseManager.Builder()
                .employeeId(employeeId)
                .fullName(name)
                .hourlyRate(hourlyRate)
                .position("Manager")
                .isFullTime(isFullTime)
                .startedAt(startedAt)
                .teamSize(teamSize)
                .build();
    }

    /**
     * Overloaded factory method for creating a new {@link Manager} (e.g., via Service).
     * <p>
     * Sets the start date to the current system date by default. 
     * Typically used for hiring new managers.
     * </p>
     *
     * @param name       the full name.
     * @param hourlyRate the pay rate.
     * @param isFullTime full-time status.
     * @param teamSize   initial team size.
     * @return a {@link Manager} with {@code startedAt} set to today.
     */
    public static Employee createManagerEmployee( // for Service
            String name, 
            BigDecimal hourlyRate, 
            boolean isFullTime,
            int teamSize
        ) {
        return createManagerEmployee(0, name, hourlyRate, isFullTime, LocalDate.now(), teamSize);
    }

    /**
     * Factory method used by the Repository to reconstruct a {@link Cashier} 
     * from database records, including register data and shift history.
     * <p>
     * Utilizes the {@code BaseCashier.Builder} to create an immutable instance 
     * reflecting the current state of a cashier in the system.
     * </p>
     *
     * @param employeeId     the unique database ID.
     * @param name           the full name of the cashier.
     * @param hourlyRate     the hourly pay rate.
     * @param isFullTime     the employment type flag.
     * @param startedAt      the hiring date.
     * @param registerNumber the assigned cash register identifier.
     * @param shiftCount     the total number of shifts completed.
     * @return a fully initialized {@link Cashier} instance.
     */
    public static Employee createCashierEmployee( // for Repository
            int employeeId, 
            String name, 
            BigDecimal hourlyRate, 
            boolean isFullTime,
            LocalDate startedAt,
            int registerNumber,
            int shiftCount
        ) {
        return new BaseCashier.Builder()
                .employeeId(employeeId)
                .fullName(name)
                .hourlyRate(hourlyRate)
                .position("Cashier")
                .isFullTime(isFullTime)
                .startedAt(startedAt)
                .registerNumber(registerNumber)
                .shiftCount(shiftCount)
                .build();
    }

    /**
     * Overloaded factory method for creating a new {@link Cashier} (e.g., via Service).
     * <p>
     * Initializes the cashier with the current date and zero completed shifts.
     * </p>
     *
     * @param name           the full name.
     * @param hourlyRate     the pay rate.
     * @param isFullTime     full-time status.
     * @param registerNumberassigned register number.
     * @return a {@link Cashier} with {@code startedAt} set to today and {@code shiftCount} set to 0.
     */
    public static Employee createCashierEmployee( // for Service
            String name, 
            BigDecimal hourlyRate, 
            boolean isFullTime,
            int registerNumber
        ) {
        return createCashierEmployee(0, name, hourlyRate, isFullTime, LocalDate.now(), registerNumber, 0);
    }
}