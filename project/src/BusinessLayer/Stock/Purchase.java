package BusinessLayer.Stock;
import PersistenceLayer.DAO.Stock.PurchaseDAO;

import java.util.Map;
import java.util.HashMap;
import java.util.*;

public class Purchase {
    private final String PURCHASE_ID;
    private double totalPrice;
    private Date date;
    private Map<String, List<PurchasedItemDocumentation>> productItemsList;
    private PurchaseDAO purchaseDAO;

    public Purchase(String purchaseID, Date date, PurchaseDAO pDAO) {
        this.PURCHASE_ID = purchaseID;
        this.productItemsList = new HashMap<>();
        this.date = date;
        this.totalPrice = 0;
        this.purchaseDAO = pDAO;
    }

    public String getPurchaseID() {
        return PURCHASE_ID;
    }

    public Date getDate() {
        return date;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Map<String, List<PurchasedItemDocumentation>> getProductsItemsList() {
        return productItemsList;
    }

    public void setTotalPrice(double price) {
        this.totalPrice = price;
    }

    public void increaseTotalPrice(double price) { this.totalPrice = this.totalPrice + price; }

    /**
     * @param productID
     * @param itemID
     * @param supplierPrice
     * @param purchasePrice
     * add item to the purchase
     */
    public void addItem(String productID, String itemID, double supplierPrice, double purchasePrice) {
        totalPrice += purchasePrice;
        if(productItemsList.containsKey(productID))
            productItemsList.get(productID).add(new PurchasedItemDocumentation(itemID, supplierPrice, purchasePrice));
        else {
            List<PurchasedItemDocumentation> itemsList = new ArrayList<>();
            itemsList.add(new PurchasedItemDocumentation(itemID, supplierPrice, purchasePrice));
            productItemsList.put(productID, itemsList);
        }
    }

    
    public Map<String, Double> getItemsAndPricesMap() {
        Map<String, Double> result = new HashMap<>();
        for (String productID : productItemsList.keySet()) {
            for (PurchasedItemDocumentation itemObj : productItemsList.get(productID)) {
                result.put(itemObj.getItemID(), itemObj.getSellingPrice());
            }
        }
        return result;
    }

    public List<String> getProductsList() {
        List<String> productsList = new ArrayList<>();
        if ((productItemsList != null) && (productItemsList.size() > 0)) {
            for (String productID : productItemsList.keySet()) {
                productsList.add(productID);
            }
        }
        return productsList;
    }

    public Map<String, Double> finishPurchase() throws Exception{
        addPurchaseToItemPriceTable();
        purchaseDAO.updateTotalPriceAfterFinish(getPurchaseID(), getTotalPrice());
        return getItemsAndPricesMap();
    }

    private void addPurchaseToItemPriceTable() throws Exception{
        if (productItemsList.size() > 0) {
            for (String productID : productItemsList.keySet()) {
                if (productItemsList.get(productID).size() > 0) {
                    for (PurchasedItemDocumentation purItemDoc : productItemsList.get(productID)) {
                        purchaseDAO.addToItemPriceTable(getPurchaseID(), purItemDoc.getItemID(), purItemDoc.getSupplierPrice(), purItemDoc.getSellingPrice());
                    }
                }
            }
        }
    }
}