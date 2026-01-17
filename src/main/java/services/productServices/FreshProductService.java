package services.productServices;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import exceptions.InvalidInputException;
import models.productModels.FreshProduct;
import storage.DataStorage;

public class FreshProductService {
    private final DataStorage storage;

    public FreshProductService(DataStorage storage) {
        this.storage = storage;
    }
    
    private boolean isIdTaken(int id) {
        return storage.getProducts().stream().anyMatch(p -> p.getId() == id);
    }

    public void addFreshProduct(int id, String name, BigDecimal price, String cat, double weight) throws InvalidInputException {
        if (isIdTaken(id)) {
            throw new InvalidInputException("ID " + id + " is already taken by another product!");
        }

        FreshProduct fp = new FreshProduct(id, name, price, cat, weight);
        storage.addProduct(fp);
    }

    public List<FreshProduct> getAllFreshProducts() {
        return storage.getProducts().stream()
                .filter(p -> p instanceof FreshProduct)
                .map(p -> (FreshProduct) p)
                .collect(Collectors.toList());
    }

    
}
