package services.productServices;

import java.util.List;

import models.productModels.FrozenProduct;
import storage.DataStorage;

public class FrozenProductService {
    private final DataStorage storage;

    public FrozenProductService(DataStorage storage) {
        this.storage = storage;
    }

    private boolean isIdTaken(int id) {
        return storage.getProducts().stream().anyMatch(p -> p.getId() == id);
    }

    public void addFrozenProduct(int id, String name, java.math.BigDecimal price, int storageTemp, String cat) {
        if (isIdTaken(id)) {
            System.out.println("Product with ID " + id + " already exists.");
        } else {
            FrozenProduct fp = new FrozenProduct(id, name, price, storageTemp, cat);
            storage.addProduct(fp);
        }
    }

    public List<FrozenProduct> getAllFrozenProducts() {
        return storage.getProducts().stream()
                .filter(p -> p instanceof FrozenProduct)
                .map(p -> (FrozenProduct) p)
                .toList();
    }
}