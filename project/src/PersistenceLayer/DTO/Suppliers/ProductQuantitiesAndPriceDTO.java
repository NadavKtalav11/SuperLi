package PersistenceLayer.DTO.Suppliers;

import PersistenceLayer.DTO.AbstractDTO;
import PersistenceLayer.DAO.Suppliers.ProductQuantitiesAndPriceMapper;

public class ProductQuantitiesAndPriceDTO extends AbstractDTO{
    private String productId;
    private int supplierId;
    private int amountCanSupply;
    private double price;
    private int catalogNumber;


    public ProductQuantitiesAndPriceDTO(int supplierId,String productId, int amountCanSupply,  double price,int catalogNumber ) {
        super(new ProductQuantitiesAndPriceMapper());
        this.productId = productId;
        this.price = price;
        this.supplierId = supplierId;
        this.amountCanSupply = amountCanSupply;
        this.catalogNumber = catalogNumber;

    }

    public int getSupplierId() {
        return supplierId;
    }

    public String getProductId() {
        return productId;
    }

    @Override
    public ProductQuantitiesAndPriceMapper getDao() {
        return (ProductQuantitiesAndPriceMapper) dao;
    }

    public double getPrice() {
        return price;
    }

    public int getAmountCanSupply() {
        return amountCanSupply;
    }

    public int getCatalogNumber() {
        return catalogNumber;
    }
}
