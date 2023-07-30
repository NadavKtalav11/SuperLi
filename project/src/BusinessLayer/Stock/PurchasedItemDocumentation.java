package BusinessLayer.Stock;

public class PurchasedItemDocumentation {

    private String itemID;
    private double supplierPrice;
    private double sellingPrice;

    public PurchasedItemDocumentation(String itemID, double supplierPrice, double sellingPrice){
        this.itemID = itemID;
        this.supplierPrice = supplierPrice;
        this.sellingPrice = sellingPrice;
    }

    public String getItemID() {
        return itemID;
    }

    public double getSupplierPrice() {
        return supplierPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }  
}
