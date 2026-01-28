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
                     ? BigDecimal.valueOf(f.storageTemp()) : null)
                .execute();
    }

    @Override
    public void update(Product product) {
        dsl.update(PRODUCTS)
                .set(PRODUCTS.UNITPRICE, product.unitPrice())
                .set(PRODUCTS.QUANTITY, BigDecimal.valueOf(product.quantity()))
                .set(PRODUCTS.ISDISCONTINUED, product.isDiscounted())
                .where(PRODUCTS.ID.eq(product.productId()))
                .execute();
    }

    @Override
    public void delete(int id) {
        dsl.deleteFrom(PRODUCTS)
                .where(PRODUCTS.ID.eq(id))
                .execute();
    }

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