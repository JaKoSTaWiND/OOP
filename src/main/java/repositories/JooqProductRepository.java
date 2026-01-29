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

    @Override
    public Optional<Product> findById(int id) {
        return dsl.selectFrom(PRODUCTS)
                .where(PRODUCTS.ID.eq(id))
                .fetchOptional()
                .map(this::mapRecordToProduct);
    }

    @Override
    public Optional<Product> findByName(String name) {
        return dsl.selectFrom(PRODUCTS)
                .where(PRODUCTS.NAME.equalIgnoreCase(name)) // Xyyy = xyyy = XYYY
                .fetchOptional()
                .map(this::mapRecordToProduct);
    }

    /**
    * Use for saving products in the database by
    * checking product type by instanceof and setting
    * product_type_id = 1 for FreshProduct and 2 for FrozenProduct
    * then mapping values from object to columns by:
    * .set(PRODUCTS.COLUMN, product.getField());
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
     * Use for updating products in the database by
     * setting updateStep all columns and finding product by Id
     * then update value in chosen column by .PRODUCTS.COLUMN(value) = newValue
     * and for all unchanged columns -> PRODUCTS.COLUMN(value) = value
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

    @Override
    public void delete(int id) {
        dsl.deleteFrom(PRODUCTS)
                .where(PRODUCTS.ID.eq(id))
                .execute();
    }

    /**
     * Use for searching products by getting Optional<Product> from query
     * and creating objects by ProductFactory based on @code product_type_id,
     * where 1 = FreshProduct and 2 = FrozenProduct.
     * mapping values from record to object fields by:
     * .map(this::mapRecordToProduct);
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