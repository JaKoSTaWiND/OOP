package repositories;

import java.util.List;
import java.util.Optional;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import factories.CustomerFactory;
import interfaces.customer.ICustomerRepository;
import static jooq.generated.Tables.CUSTOMERS;
import jooq.generated.tables.records.CustomersRecord;
import models.customerModels.Customer;

@Repository
public class JooqCustomerRepository implements ICustomerRepository {

    private final DSLContext dsl;

    public JooqCustomerRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return dsl.selectFrom(CUSTOMERS)
                .fetch()
                .map(this::mapRecordToCustomer);
    }

    /**
     * Searches a customer from the database by its primary key.
     * <p>
     * This method executes a {@code SELECT} query using jOOQ's DSL context 
     * and attempts to map the resulting record to a concrete {@link Customer} 
     * implementation using {@link #mapRecordToCustomer(Record)}.
     * </p>
     *
     * @param customerId the unique identifier of the customer in the database.
     * @return an {@link Optional} containing the mapped customer if found, 
     * or {@link Optional#empty()} if no record matches the ID.
     */
    @Override
    public Optional<Customer> findById(int customerId) {
        return dsl.selectFrom(CUSTOMERS)
                .where(CUSTOMERS.ID.eq(customerId))
                .fetchOptional()
                .map(this::mapRecordToCustomer);
    }

    /**
     * Searches for a customer by its name using a case-insensitive comparison.
     * <p>
     * Uses the SQL {@code LOWER} or {@code UPPER} function (via {@code equalIgnoreCase}) 
     * to ensure that searches like "Apple", "apple", and "APPLE" all return the same result.
     * </p>
     *
     * @param fullName the name of the customer to search for.
     * @return an {@link Optional} containing the found customer, or empty if the name 
     * does not exist in the database.
     */
    @Override
    public Optional<Customer> findByName(String fullName) {
        return dsl.selectFrom(CUSTOMERS)
                .where(CUSTOMERS.FULL_NAME.equalIgnoreCase(fullName)) // Xyyy = xyyy = XYYY
                .fetchOptional()
                .map(this::mapRecordToCustomer);
    }

    /**
     * Persists a new customer record into the {@code CUSTOMERS} table.
     * <p>
     * This method maps common {@link Customer} attributes to database columns
     * </p>
     *
     * @param customer the customer instance to be saved.
     */
    @Override
    public void save(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }

        dsl.insertInto(CUSTOMERS)
                .set(CUSTOMERS.FULL_NAME, customer.fullName())
                .set(CUSTOMERS.PHONE, customer.phone())
                .set(CUSTOMERS.LOYALTY_POINTS, customer.loyaltyPoints())
                .set(CUSTOMERS.IS_VIP, customer.isVip())
                .execute();
    }

    /**
     * Updates an existing customer record in the {@code CUSTOMERS} table.
     * <p>
     * This method builds a dynamic SQL {@code UPDATE} statement. It always updates 
     * common fields (full_name, phone, loyalty_points, is_vip). 
     * </p>
     *
     * @param customer the custtomer instance containing the updated data.
     */
    @Override
    public void update(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }

        int updateStep = dsl.update(CUSTOMERS)
                .set(CUSTOMERS.FULL_NAME, customer.fullName())
                .set(CUSTOMERS.PHONE, customer.phone())
                .set(CUSTOMERS.LOYALTY_POINTS, customer.loyaltyPoints())
                .set(CUSTOMERS.IS_VIP, customer.isVip())
                .where(CUSTOMERS.ID.eq(customer.customerId()))
                .execute();
        
        if (updateStep == 0) {
            throw new RuntimeException("Update failed: Customer with ID " + customer.customerId() + " not found.");
        }
    }

    /**
     * Removes a customer record from the {@code CUSTOMERS} table by its unique ID.
     * <p>
     * This operation is permanent. If no record exists with the provided ID, 
     * the method completes successfully without making any changes to the database.
     * </p>
     *
     * @param customerId the primary key of the customer to be deleted.
     */
    @Override
    public void delete(int customerId) {
        dsl.deleteFrom(CUSTOMERS)
                .where(CUSTOMERS.ID.eq(customerId))
                .execute();
    }

    /**
     * Maps a jOOQ {@link CustomersRecord} to a {@link Customer} domain model.
     * <p>
     * This internal helper method acts as a bridge between the persistence layer and 
     * the domain layer. It extracts raw database values from the JOOQ record and 
     * delegates the instantiation to the {@link CustomerFactory}, ensuring that 
     * all customer attributes—including loyalty points and VIP status—are 
     * correctly transformed into a type-safe immutable object.
     * </p>
     * 
     * @param record the database record containing raw customer data from the "customers" table.
     * @return a fully initialized {@link Customer} instance.
     * @see CustomerFactory
     */
    private Customer mapRecordToCustomer(CustomersRecord record) {
        return CustomerFactory.createCustomer(
            record.getId(),
            record.getFullName(),
            record.getPhone(),
            record.getLoyaltyPoints(),
            record.getIsVip()
        );
    }
}
