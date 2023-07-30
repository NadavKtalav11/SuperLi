package ServiceLayer.Stock;
import BusinessLayer.Tools.Pair;
import BusinessLayer.Stock.StockController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class StockService {

    private StockController sc;

    public StockService(StockController sc){
        this.sc = sc;
    }


    /**
     * Calls the stockController's required methods for printing the Deficiency Report
     * @param chosenCategories
     */
    public String printDeficiencyReport(List<List<String>> chosenCategories) throws Exception{
        return sc.createDeficiencyReport(chosenCategories);
    }


    /**
     * Calls the stockController's required methods for printing the Damaged items Report
     * @param chosenCategories
     */
    public String printDamagedReport(List<List<String>> chosenCategories, List<String> damageList) throws Exception{
        return sc.createDamageReport(chosenCategories, damageList);
    }

    /**
     * Calls the stockController's required methods for printing the Stock Report
     * @param chosenCategories
     */
    public String printStockReport(List<List<String>> chosenCategories) throws Exception{
        return sc.createStockReport(chosenCategories);
    }


    /**
     * Calls the stockController's required methods for setting the given item as damaged
     * @param itemID
     * @param damageType
     */
    public void addDamagedItem(String itemID, String damageType) throws SQLException{
        sc.setAnItemAsDamaged(itemID, damageType);
    }


    /**
     * Calls the stockController's required methods for setting the given product's demand value
     * @param productID
     * @param demand
     */
    public void SetDemand(String productID, int demand) throws SQLException{
        sc.editDemand(productID, demand);
    }


    /**
     * Calls the stockController's required methods for setting the given product's supply time value
     * @param productID
     * @param supplyTime
     */
    public void SetSupplyTime(String productID, int supplyTime) throws SQLException{
        sc.editSupplyTime(productID, supplyTime);
    }


    /**
     * Calls the stockController's required methods for changing the given item's location
     * @param itemID
     */
    public void moveItem(String itemID) throws SQLException{
        sc.moveItem(itemID);
    }

    
    /**
     * Calls the stockController's required methods for adding the given item to the given purchase
     * @param purchaseID
     * @param itemID
     * @return
     */
    public Map<String, Boolean> purchaseItem(String purchaseID, String itemID) throws Exception{
        return sc.purchaseItem(purchaseID, itemID);
    }


    /**
     * Calls the stockController's required methods for getting a Map of main categories' IDs and names
     * @return
     */
    public Map<String, String> getMainCategories() throws Exception{
        return sc.getMainCategoryNameMapByIDs();
    }


    /**
     * Calls the stockController's required methods for getting a Map of sub categories' IDs and names
     * @param categories
     * @return
     */
    public Map<String, String> getSub(List<String> categories) {
        return sc.getSubCategories(categories);
    }


    /**
     * Calls the stockController's required methods for adding a new category in the end of the given category chain
     * @param categories
     * @param name
     * @return
     */
    public String addCategory(List<String> categories, String name)throws Exception {
        return sc.addCategory(categories, name);
    }


    /**
     * Calls the stockController's required methods for checking if the given id is ascribed to an existing product
     * @param id
     * @return
     */
    public boolean checkProductExist(String id) throws Exception{
        return sc.isProductExist(id);
    }


    /**
     *
     * @param id
     * @return The product's name
     */
    public String getProductName(String id)throws Exception {
        return sc.getProductName(id);
    }


    /**
     * Calls the stockController's required methods for adding a new product
     * @param id
     * @param name
     * @param purchasePrice
     * @param sellingPrice
     * @param manufacture
     * @param demand
     * @param supplyTime
     * @param initialNotificationAmount
     * @param categories
     */
    public void addProduct(String id, String name, double purchasePrice, double sellingPrice, String manufacture,
            Integer demand, Integer supplyTime, Integer initialNotificationAmount, List<String> categories) throws Exception{
        sc.addNewProduct(id, name, purchasePrice, sellingPrice, manufacture, demand, supplyTime, initialNotificationAmount, categories);
    }

    /**
     * Calls the stockController's required methods for recieving a new supply batch
     * @param id
     * @param storeAmount
     * @param warehouseAmount
     * @param expiration
     */
    public void receiveSupply(String id, int storeAmount, int warehouseAmount, Date expiration) throws SQLException {
        sc.receiveSupply(id, storeAmount, warehouseAmount, expiration);
    }


    /**
     * Calls the stockController's required methods for recieving Strings of the existing damageType enums in the system
     * @return
     */
    public Map<Integer, String> getDamages() {
        return sc.getDamageEnumStrings();
    }


    /**
     * Calls the stockController's required methods for adding a new purchase object
     * @return the new purchase's ID
     */
    public String addPurchase() throws Exception {
        return sc.addPurchase();
    }


    /**
     * Calls the stockController's required methods for setting a new purchase (from supplier) price to the given product
     * @param productID
     * @param price
     */
    public void SetPurchasePrice(String productID, Double price) throws Exception{
        sc.SetPurchasePriceForProduct(productID, price);
    }


    /**
     * Calls the method that sends the deficiency list to the suppliers
     * Calls the stockController's required methods for getting the list of items and their prices that were purchased in the given purchase
     * @param purchaseID
     * @return
     */
    public Map<String, Double> finishPurchase(String purchaseID) throws Exception{
        sc.sendDeficiencyOrderToSuppliers(purchaseID);
        return sc.finishPurchase(purchaseID);
    }

    
    /**
     * Calls the stockController's required methods for getting the given purchase's total price
     * @param purchaseID
     * @return
     */
    public double getPurchaseTotalPrice(String purchaseID) {
        return sc.getPurchaseTotalPrice(purchaseID);
    }


    /**
     * Calls the stockController's required methods for adding a new discount of type CATEGORY
     * @param categoryChain
     * @param startDate
     * @param endDate
     * @param discoutValue
     * @param isPercent
     */
    public void addCategoryDiscount(List<String> categoryChain, Date startDate, Date endDate, double discoutValue,
            boolean isPercent) throws Exception{
                sc.addDiscount(startDate, endDate, discoutValue, isPercent, "CATEGORY", "", categoryChain);
    }


    /**
     * Calls the stockController's required methods for adding a new discount of type PRODUCT
     * @param productID
     * @param startDate
     * @param endDate
     * @param discoutValue
     * @param isPercent
     */
    public void addProductDiscount(String productID, Date startDate, Date endDate, double discoutValue,
            boolean isPercent) throws Exception{
                List<String> productList = new ArrayList<>();
                productList.add(productID);
                sc.addDiscount(startDate, endDate, discoutValue, isPercent, "PRODUCT", "", productList);
    }


    /**
     * Calls the stockController's required methods for adding a new discount of type DAMAGED
     * @param productID
     * @param damageType
     * @param startDate
     * @param endDate
     * @param discoutValue
     * @param isPercent
     */
    public void addDamagedDiscount(String productID, String damageType, Date startDate, Date endDate,
            double discoutValue, boolean isPercent) throws Exception{
                List<String> productList = new ArrayList<>();
                productList.add(productID);
                sc.addDiscount(startDate, endDate, discoutValue, isPercent, "DAMAGED", damageType, productList);
    }


    /**
     * Calls the stockController's required methods for  getting the given category's discounts
     * @param categoryChain
     * @return //!!!
     */
    public Map<String, String> getCategoryDiscouts(List<String> categoryChain) throws Exception{
        return sc.getCategotyDiscountList(categoryChain);
    }


    /**
     * Calls the stockController's required methods for deleting the given discount from the last category in the category chain
     * @param categoryChain
     * @param discountID
     */
    public void deleteCategoryDiscount(List<String> categoryChain, String discountID) throws Exception{
        sc.deleteCategoryDiscount(categoryChain, discountID);
    }


    /**
     * Calls the stockController's required methods for re-setting the start date of the given discount from the last category in the category chain
     * @param categoryChain
     * @param discountID
     * @param startDate
     */
    public void updateCategoryDiscountStartDate(List<String> categoryChain, String discountID, Date startDate) throws Exception{
        sc.updateCategoryDiscountStartDate(categoryChain, discountID, startDate);
    }


    /**
     * Calls the stockController's required methods for re-setting the end date of the given discount from the last category in the category chain
     * @param categoryChain
     * @param discountID
     * @param endDate
     */
    public void updateCategoryDiscountEndDate(List<String> categoryChain, String discountID, Date endDate) throws Exception{
        sc.updateCategoryDiscountEndDate(categoryChain, discountID, endDate);
    }


    /**
     * Calls the stockController's required methods for re-setting the isPercent value of the given discount from the last category in the category chain
     * @param categoryChain
     * @param discountID
     * @param isPercent
     */
    public void updateCategoryDiscountIsPercent(List<String> categoryChain, String discountID, boolean isPercent) throws Exception{
        sc.updateCategoryDiscountIsPercent(categoryChain, discountID, isPercent);
    }


    /**
     * Calls the stockController's required methods for re-setting the discount value of the given discount from the last category in the category chain
     * @param categoryChain
     * @param discountID
     * @param discountValue
     */
    public void updateCategoryDiscountValue(List<String> categoryChain, String discountID, double discountValue) throws Exception{
        sc.updateCategoryDiscountValue(categoryChain, discountID, discountValue);
    }


    /**
     * Calls the stockController's required methods for getting the given product's general discounts (not for damaged items only)
     * @param productID
     * @return
     */
    public Map<String, String> getProductGeneralDiscouts(String productID) throws Exception{
        return sc.getProductDiscountsByDamage(productID, "NONE");
    }


    /**
     * Calls the stockController's required methods for deleting the given discount from the given product
     * @param productID
     * @param discountID
     * @param damageType - "NONE" if a general discount, other damageType otherwise
     */
    public void deleteProductDiscount(String productID, String discountID, String damageType) throws Exception{
        sc.deleteProductDiscount(productID, discountID, damageType);
    }


    /**
     * Calls the stockController's required methods for re-setting the given product's discount's start date 
     * @param productID
     * @param damageType - "NONE" if a general discount, other damageType otherwise
     * @param discountID
     * @param startDate 
     */
    public void updateProductDiscountStartDate(String productID, String damageType, String discountID, Date startDate) {
        sc.updateProductDiscountStartDate(productID, damageType, discountID, startDate);
    }


    /**
     * Calls the stockController's required methods for re-setting the given product's discount's end date 
     * @param productID
     * @param damageType - "NONE" if a general discount, other damageType otherwise
     * @param discountID
     * @param endDate
     */
    public void updateProductDiscountEndDate(String productID, String damageType, String discountID, Date endDate) {
        sc.updateProductDiscountEndDate(productID, damageType, discountID, endDate);
    }


    /**
     * Calls the stockController's required methods for re-setting the given product's discount's isPercent value
     * @param productID
     * @param damageType - "NONE" if a general discount, other damageType otherwise
     * @param discountID
     * @param isPercent
     */
    public void updateProductDiscountIsPercent(String productID, String damageType, String discountID, boolean isPercent) {
        sc.updateProductDiscountIsPercent(productID, damageType, discountID, isPercent);
    }


    /**
     * Calls the stockController's required methods for re-setting the given product's discount's discount value
     * @param productID
     * @param damageType - "NONE" if a general discount, other damageType otherwise
     * @param discountID
     * @param discountValue
     */
    public void updateProductDiscountValue(String productID, String damageType, String discountID, double discountValue) {
        sc.updateProductDiscountValue(productID, damageType, discountID, discountValue);
    }


    /**
     * Calls the stockController's required methods for getting a map of the prouct's discount for the given damageType
     * @param productID
     * @param damageType
     * @return
     */
    public Map<String, String> getProductDamagedDiscounts(String productID, String damageType) throws Exception{
        return sc.getProductDiscountsByDamage(productID, damageType);
    }

    public Map<Integer, String> getAutoOrdersList() {
        return sc.getAutoOrdersList();
    }

    public void deleteAutomaticOrder(int orderID) {
        sc.deleteAutomaticOrder(orderID);
    }

    public int addAutomaticOrder(List<Integer> supplyDays, Map<Pair<String, String>, Integer> productsToOrder) throws Exception{
        return sc.addAutomaticOrder(supplyDays, productsToOrder);
    }

    public boolean addExpressOrder(Map<Pair<String, String>, Integer> productsToOrder) throws Exception{
        return sc.addExpressOrder(productsToOrder);
    }

    public boolean updateOrderSupplyDays(int orderID, List<Integer> newSupplyDays) {
        return sc.updateOrderSupplyDays(orderID, newSupplyDays);
    }

    public Map<Pair<String, String>, Integer> getOrderProductsMap(int orderID) {
        return sc.getOrderProductsMap(orderID);
    }

    public boolean updateOrderProductsAndAmounts(int orderID, Map<Pair<String, String>, Integer> productsAndAmountsMap) throws Exception{
        return sc.updateOrderProductsAndAmounts(orderID, productsAndAmountsMap);
    }
}