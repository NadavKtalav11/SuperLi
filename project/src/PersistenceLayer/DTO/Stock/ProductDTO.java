package PersistenceLayer.DTO.Stock;

import PersistenceLayer.DAO.Stock.ProductDAO;
import PersistenceLayer.DTO.AbstractDTO;

public class ProductDTO extends AbstractDTO {
    
    private final String PRODUCT_ID;
    private final String name;
    private final int storeAmount;
    private final int warehouseAmount;
    private final int totalAmount;
    private final int damagedAmount;
    private final int notificationAmount;
    private final double purchasePrice;
    private final double sellingPrice;
    private final String manufacturer;
    private final int demand;
    private final int supplyTime;
    private final String category;
    private final int counter;
    private final int hasDefInProcess;

    public ProductDTO(String productID, String name, int initialNotificationAmount, double purchasePrice, double sellingPrice, String manufacturer, int demand, int supplyTime, String category, int counter, int store, int warehouse, int damagedAmount, int isOrdered, ProductDAO pdao){
        super(pdao);
        this.PRODUCT_ID = productID;
        this.name= name;
        this.storeAmount = store;
        this.warehouseAmount = warehouse;
        this.totalAmount = store + warehouse;
        this.damagedAmount = damagedAmount;
        this.purchasePrice = purchasePrice;
        this.sellingPrice =  sellingPrice;
        this.manufacturer = manufacturer;
        this.demand = demand;
        this.supplyTime = supplyTime;
        this.notificationAmount = initialNotificationAmount;
        this.category = category;
        this.counter = counter;
        this.hasDefInProcess = isOrdered;
    }

    public String getProductID() {
        return PRODUCT_ID;
    }

    public String getName() {
        return name;
    }

    public int getStoreAmount () {
        return storeAmount;
    }

    public int getWarehouseAmount() {
        return warehouseAmount;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public int getDamagedAmount() {
        return damagedAmount;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public int getDemand() {
        return demand;
    }

    public int getSupplyTime() {
        return supplyTime;
    }

    public int getNotificationAmount() {
        return notificationAmount;
    }

    public String getCategory() {
        return category;
    }

    public int getItemCounter(){ return counter; }

    public int getHasDefInProcess() {
        return hasDefInProcess;
    }
}
