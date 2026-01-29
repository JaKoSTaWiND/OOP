package repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import factories.ProductFactory;
import interfaces.product.IProductRepository;
import static jooq.generated.Tables.PRODUCTS;
import jooq.generated.tables.records.ProductsRecord;
import models.productModels.FreshProduct;
import models.productModels.FrozenProduct;
import models.productModels.Product;

@Repository
public class JooqProductRepository implements IProductRepository {

    private final DSLContext dsl;

    public JooqProductRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public List<Product> getAllProducts() {
        return dsl.selectFrom(PRODUCTS)
                .fetch()
                .map(this::mapRecordToProduct);
    }

    /**
     * Searches a product from the database by its primary key.
     * <p>
     * This method executes a {@code SELECT} query using jOOQ's DSL context 
     * and attempts to map the resulting record to a concrete {@link Product} 
     * implementation using {@link #mapRecordToProduct(Record)}.
     * </p>
     *
     * @param id the unique identifier of the product in the database.
     * @return an {@link Optional} containing the mapped product if found, 
     * or {@link Optional#empty()} if no record matches the ID.
     */
    @Override
    public Optional<Product> findById(int id) {
        return dsl.selectFrom(PRODUCTS)
                .where(PRODUCTS.ID.eq(id))
                .fetchOptional()
                .map(this::mapRecordToProduct);
    }

    /**
     * Searches for a product by its name using a case-insensitive comparison.
     * <p>
     * Uses the SQL {@code LOWER} or {@code UPPER} function (via {@code equalIgnoreCase}) 
     * to ensure that searches like "Apple", "apple", and "APPLE" all return the same result.
     * </p>
     *
     * @param name the name of the product to search for.
     * @return an {@link Optional} containing the found product, or empty if the name 
     * does not exist in the database.
     */
    @Override
    public Optional<Product> findByName(String name) {
        return dsl.selectFrom(PRODUCTS)
                .where(PRODUCTS.NAME.equalIgnoreCase(name)) // Xyyy = xyyy = XYYY
                .fetchOptional()
                .map(this::mapRecordToProduct);
    }

    /**
     * Persists a new product record into the {@code PRODUCTS} table.
     * <p>
     * This method maps common {@link Product} attributes to database columns. 
     * It uses a simple type-mapping strategy where {@code FreshProduct} is assigned 
     * type ID {@code 1} and {@code FrozenProduct} is assigned type ID {@code 2}.
     * Special fields like {@code storageTemp} are handled conditionally based on 
     * the concrete instance type.
     * </p>
     *
     * @param product the product instance to be saved.
     */
    @Override
    public void save(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        dsl.insertInto(PRODUCTS)
                .set(PRODUCTS.PRODUCT_TYPE_ID, product instanceof FreshProduct ? 1 : 2)
                .set(PRODUCTS.NAME, product.name())
                .set(PRODUCTS.UNITPRICE, product.unitPrice())
                .set(PRODUCTS.QUANTITY, BigDecimal.valueOf(product.quantity()))
                .set(PRODUCTS.CATEGORY, product.category())
                .set(PRODUCTS.ISDISCONTINUED, product.isDiscounted())
                .set(PRODUCTS.STORAGETEMP, product instanceof FrozenProduct f 
                     ? BigDecimal.valueOf(f.storageTemp()) : null) // if FrozenProduct set storageTemp else null
                .execute();
    }


    /**
     * Updates an existing product record in the {@code PRODUCTS} table.
     * <p>
     * This method builds a dynamic SQL {@code UPDATE} statement. It always updates 
     * common fields (name, price, quantity, category, and discount status). 
     * If the product is an instance of {@link FrozenProduct}, it also updates 
     * the {@code storageTemp}; otherwise, it ensures the temperature field is set to {@code null}.
     * </p>
     *
     * @param product the product instance containing the updated data.
     */
    @Override
    public void update(Product product) {
        var updateStep = dsl.update(PRODUCTS)
                .set(PRODUCTS.NAME, product.name())
                .set(PRODUCTS.UNITPRICE, product.unitPrice())
                .set(PRODUCTS.QUANTITY, BigDecimal.valueOf(product.quantity()))
                .set(PRODUCTS.CATEGORY, product.category())
                .set(PRODUCTS.ISDISCONTINUED, product.isDiscounted());

        if (product instanceof FrozenProduct frozen) { // check for FrozenProduct
            updateStep = updateStep.set(PRODUCTS.STORAGETEMP, BigDecimal.valueOf(frozen.storageTemp()));
        } else {
            updateStep = updateStep.set(PRODUCTS.STORAGETEMP, (BigDecimal) null);
        }

        int rows = updateStep.where(PRODUCTS.ID.eq(product.productId())) // update value by found productId
                .execute();

        if (rows == 0) {
            throw new RuntimeException("Update failed: Product with ID " + product.productId() + " not found.");
        }
    }

    /**
     * Removes a product record from the {@code PRODUCTS} table by its unique ID.
     * <p>
     * This operation is permanent. If no record exists with the provided ID, 
     * the method completes successfully without making any changes to the database.
     * </p>
     *
     * @param id the primary key of the product to be deleted.
     */
    @Override
    public void delete(int id) {
        dsl.deleteFrom(PRODUCTS)
                .where(PRODUCTS.ID.eq(id))
                .execute();
    }

    /**
     * Maps a jOOQ {@link ProductsRecord} to a concrete {@link Product} implementation.
     * <p>
     * This internal helper method interprets the {@code PRODUCT_TYPE_ID} column to 
     * determine which factory method to invoke. It performs necessary type conversions, 
     * such as converting {@link BigDecimal} quantities to {@code double} and 
     * handling nullable {@code storageTemp} values for frozen goods.
     * </p>
     *
     * @param record the database record containing product data.
     * @return a concrete instance of {@link FreshProduct} (Type 1) or {@link FrozenProduct} (Type 2).
     * @see ProductFactory
     */
    private Product mapRecordToProduct(ProductsRecord record) {
        return switch (record.getProductTypeId()) {
            case 1 -> ProductFactory.createFreshProduct(
                        record.getId(),
                        record.getName(),
                        record.getUnitprice(),
                        record.getQuantity().doubleValue(),
                        record.getCategory(),
                        record.getIsdiscontinued()
                    );

            case 2 -> ProductFactory.createFrozenProduct(
                        record.getId(),
                        record.getName(),
                        record.getUnitprice(),
                        record.getQuantity().doubleValue(),
                        record.getStoragetemp() != null ? record.getStoragetemp().intValue() : 0,
                        record.getCategory(),
                        record.getIsdiscontinued()
                    );

            default -> throw new IllegalArgumentException("Unknown product type: " + record.getProductTypeId());
        };
    }
}