package BusinessLayer.Stock;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import BusinessLayer.Tools.Pair;
import BusinessLayer.Stock.Discount.DiscountType;
import BusinessLayer.Stock.Item.DamageType;
import BusinessLayer.StockToSupplier;
import BusinessLayer.Tools.Pair;
import PersistenceLayer.DAO.Stock.CategoryDAO;
import PersistenceLayer.DAO.Stock.ControllerDAO;
import PersistenceLayer.DAO.Stock.ProductDAO;
import PersistenceLayer.DAO.Stock.PurchaseDAO;
import PersistenceLayer.DAO.Stock.ReportDAO;
import PersistenceLayer.DTO.Stock.CategoryDTO;
import PersistenceLayer.DTO.Stock.ControllerDTO;
import PersistenceLayer.DTO.Stock.ProductDTO;

import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Date;
import java.util.*;


public class StockController {

    private StockToSupplier sTS;
    private Map<String, Product> productList;
    //private Map<Integer, Discount> discountList;
    private Map<String, Category> mainCategoryList;
    private Map<String, Purchase> purchaseList;
    private Map<String, List<Report>> reportList;
    private final int BRANCH_ID;
    private int discountCounter = 1;
    private int mainCategoryCounter = 1;
    private int purchaseCounter = 1;
    private int reportCounter = 1;
    private int orderCounter = 1;
    private ProductDAO productDAO = new ProductDAO();
    private CategoryDAO categoryDAO = new CategoryDAO(productDAO);
    private ReportDAO reportDAO = new ReportDAO();
    private PurchaseDAO purchaseDAO = new PurchaseDAO();
    private ControllerDAO controllerDAO;

    public StockController(int branchID, ControllerDAO controllerDAO) {
        this.sTS = new StockToSupplier();
        this.productList = new HashMap<>();
        //this.discountList = new HashMap<>();
        this.mainCategoryList = new HashMap<>();
        this.purchaseList = new HashMap<>();
        this.BRANCH_ID = branchID;
        this.discountCounter = 1;
        this.mainCategoryCounter = 1;
        this.purchaseCounter = 1;
        this.reportCounter = 1;
        this.orderCounter = 1;
        this.reportList = new HashMap<>();
        reportList.put("Deficiency Report", new ArrayList<>());
        reportList.put("Damaged Report", new ArrayList<>());
        reportList.put("Stock Report", new ArrayList<>());
        this.controllerDAO = controllerDAO;
    }

    public StockController(int branchID, ControllerDAO cDAO, ControllerDTO cDTO) {
        this.sTS = new StockToSupplier();
        this.controllerDAO = cDAO;
        this.productList = new HashMap<>();
        this.mainCategoryList = new HashMap<>();
        this.purchaseList = new HashMap<>();
        this.BRANCH_ID = branchID;
        this.discountCounter = cDTO.getDiscountCounter();
        this.mainCategoryCounter = cDTO.getMainCategoryCounter();
        this.purchaseCounter = cDTO.getPurchaseCounter();
        this.reportCounter = cDTO.getReportCounter();
        this.orderCounter = cDTO.getOrderCounter();
        this.reportList = new HashMap<>();
        reportList.put("Deficiency Report", new ArrayList<>());
        reportList.put("Damaged Report", new ArrayList<>());
        reportList.put("Stock Report", new ArrayList<>());
    }

    public StockController(int id) {
        this.sTS = new StockToSupplier();
        this.productList = new HashMap<>();
        //this.discountList = new HashMap<>();
        this.mainCategoryList = new HashMap<>();
        this.purchaseList = new HashMap<>();
        this.BRANCH_ID = id;
        this.discountCounter = 1;
        this.mainCategoryCounter = 1;
        this.purchaseCounter = 1;
        this.reportCounter = 1;
        this.orderCounter = 1;
        this.reportList = new HashMap<>();
        reportList.put("Deficiency Report", new ArrayList<>());
        reportList.put("Damaged Report", new ArrayList<>());
        reportList.put("Stock Report", new ArrayList<>());
        this.controllerDAO = new ControllerDAO();
    }


    /**
     * 
     * @return the branchID
     */
    public int getBranchID(){
        return BRANCH_ID;
    }


    /**
     * 
     * @return a list of all the products that are below minimum amount and need to be restocked
     */
    public List<String> getBelowMinimumAmountList() {
        List<String> belowMinimumAmount = new LinkedList<>();
        for (Product product: productList.values()) {
            if (product.isBelowMinimumAmount()) {
                belowMinimumAmount.add(product.getName());
            }
        }
        return belowMinimumAmount;
    }


    /**
     * 
     * @return a supply report for the suppliers model
     */
    public Map<String, Integer> supplyReportForSuppliers() {
        Map<String, Integer> ans = new HashMap<>();
        for (Product product: productList.values()) {
            if (product.isBelowMinimumAmount()) {
                ans.put(product.getProductID(), product.getNotificationAmount() - product.getTotalAmount());
            }
        }
        return ans;
    }


    /**
     * @return next valid category id
     */
    private String generateCategoryID(){
        Integer id = mainCategoryCounter;
        mainCategoryCounter += 1;
        controllerDAO.updateCategoryID(mainCategoryCounter, getBranchID());
        return getBranchID() + "#" + id;
    }


    /**
     * @return next valid purchase id
     */
    private String generatePurchaseID(){
        Integer id = purchaseCounter;
        purchaseCounter += 1;
        return getBranchID() + "#" + id;
    }


    /**
     * @return next valid purchase id
     */
    private String generateReportID(){
        int id = reportCounter;
        reportCounter += 1;
        controllerDAO.updateReportID(reportCounter, getBranchID());
        return getBranchID() + "#" + id;
    }


        /**
     * @return next valid discount id
     */
    private String generateDiscountID(){
        Integer id = discountCounter;
        discountCounter += 1;
        controllerDAO.updateDiscountID(discountCounter, getBranchID());
        return getBranchID() + "#" + id;
    }

    /**
     * @return next valid discount id
     */
    private int generateOrderID(){
        Integer id = orderCounter;
        orderCounter += 1;
        return id;
    }


    /**
     * @param itemID
     * @return list that contains the productID in cell 0, and item serial in cell 1
     */
    private List<String> splitItemID(String itemID){
        String[] ids = itemID.split("-");
        List<String> idsList = new ArrayList<>();
        idsList.add(ids[0]);
        idsList.add(ids[1]); //???
        return idsList;
    }


    /**
     * @param productID
     * @param name
     * @param purchasePrice
     * @param sellingPrice
     * @param manufacture
     * @param demand
     * @param supplyTime
     * @param initialNotificationAmount
     * @param categoryIDs
     * Adding a new product to the list. If the product already exists, throws an exception.
     */
    public void addNewProduct(String productID, String name, double purchasePrice, double sellingPrice, String manufacture, int demand, int supplyTime, int initialNotificationAmount, List<String> categoryIDs) throws Exception{
        productID = getBranchID() + "#" + productID;
        if(productList.containsKey(productID)) {
            throw new IllegalArgumentException("Product id already exist");
        }
        else{
            try{
                if(productDAO.checkIfProductExists(productID)) {
                    productList.put(productID, new Product(productDAO.getProduct(productID), categoryIDs, productDAO));
                    throw new IllegalArgumentException("Product id already exist");
                }
            } catch(Exception ex){
                throw new Exception("DB ERROR: " + ex.getMessage());
            }
        }
        productDAO.addProduct(productID, name, purchasePrice, sellingPrice, manufacture, demand, supplyTime, initialNotificationAmount, categoryIDs.get(categoryIDs.size() - 1));
        Product newProduct = new Product(productID, name, purchasePrice, sellingPrice, manufacture, demand, supplyTime, initialNotificationAmount, categoryIDs, productDAO);
        productList.put(productID, newProduct);
        int index = categoryIDs.size() - 1;
        getCategoryChainByIDList(categoryIDs).get(index).addProduct(newProduct);
    }


    /**
     * @param productID
     * @param newDemand
     * Edit the demand value for a product
     */
    public void editDemand(String productID, int newDemand) throws SQLException{
        if(!isProductExist(productID))
            throw new IllegalArgumentException("Product is not found.");
        productID = addHashtagToIDIfNeeded(productID);
        productList.get(productID).setDemand(newDemand);
        productList.get(productID).setNotificationAmount(); //
    }


    /**
     * @param productID
     * @param newSupplyTime
     * Edit the supply time value for a product
     */
    public void editSupplyTime(String productID, int newSupplyTime) throws SQLException{
        if(!isProductExist(productID))
            throw new IllegalArgumentException("Product is not found.");
        productID = addHashtagToIDIfNeeded(productID);
        productList.get(productID).setSupplyTime(newSupplyTime);
    }


    /**
     * @param productID
     * @param storeAmount
     * @param warehouseAmount
     * @param expirationDate
     * activates the function in Product class
     */
    public void receiveSupply(String productID, int storeAmount, int warehouseAmount, Date expirationDate) throws SQLException{
        if(!isProductExist(productID))
            throw new NoSuchElementException("Product " + productID + " doesn't exist in the system. ");
        productID = addHashtagToIDIfNeeded(productID);
        productList.get(productID).receiveSupply(storeAmount, warehouseAmount, expirationDate);
    }


    /**
     * @param name
     * adding a new category
     */
    public String addCategory(List<String> categories, String name) throws Exception{
        if(categories.size() == 0){
            String id = generateCategoryID();
            categoryDAO.addCategory(id, name, null);
            mainCategoryList.put(id, new Category(id, name, categoryDAO));
            return id;
        }
        else {
            Category cat = mainCategoryList.get(categories.get(0));
            categories.remove(0);
            return cat.addSubCategory(categories, name);
        }
    }

    /**
     * 
     * @param damageType - String of a damage type
     * @return a matching DamageType enum object
     */
    private DamageType convertStringToEnumDamageType(String damageType) {
        if(damageType.equals("BROKEN")){
            return Item.DamageType.BROKEN;
        }
        else if(damageType.equals("EXPIRED")){
            return Item.DamageType.EXPIRED;
        }
        else if(damageType.equals("SCRATCH")){
            return Item.DamageType.SCRATCH;
        }
        else if(damageType.equals("NONE")) {
            return Item.DamageType.NONE;
        }
        else if(damageType.equals("OTHER")){
            return Item.DamageType.OTHER;
        }
        else {
            throw new IllegalArgumentException("DamageType " + "'" + damageType + "'" + " does'nt exist");
        }
    }

    /**
     * @param itemID
     * move item from warehouse to store, and opposite
     */
    public void moveItem(String itemID) throws SQLException{
        List<String> ids = splitItemID(itemID);
        if(!isProductExist(ids.get(0)))
            throw new NoSuchElementException("Product doesn't exist.");
        productList.get(ids.get(0)).moveItem(itemID);
    }

    /**
     * creating a new purchase
     * @return The purchaseID
     */
    public String addPurchase() throws Exception{
        String id = generatePurchaseID();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        purchaseDAO.addNewPurchase(id, dateFormat.format(date));
        purchaseList.put(id, new Purchase(id, date, purchaseDAO));
        return id;
    }

    /**
     * @param purchaseID
     * @param itemID
     * @return Map with the keys: isDemaged - true if the purchased item is damaged, false otherwise. shortage - true if the purchased item is short in store, false otherwise.
     */
    public Map<String, Boolean> purchaseItem(String purchaseID, String itemID) throws Exception{
        if(!purchaseList.containsKey(purchaseID))
            throw new IllegalArgumentException("Purchase is not found.");
        List<String> ids = splitItemID(itemID);
        String productID = ids.get(0);
        //if(!productList.containsKey(productID))
        if(!isProductExist(productID)) {
            throw new IllegalArgumentException("Product is not found." + productID);
        }
        if(!productList.get(productID).isItemIDExists(itemID)){
            throw new IllegalArgumentException("Item is not found.");
        }
        if (!isCategoriesExist(productList.get(productID).getCategoryIDChain())) {
            throw new IllegalArgumentException("Categories are not found.");
        }
        double price = getItemCalculatedPrice(itemID);
        Item removed = productList.get(productID).removeItem(itemID);
        boolean isDamaged = removed.getIsDamaged();
        //purchaseList.get(purchaseID).increaseTotalPrice(price);
        purchaseList.get(purchaseID).addItem(productID, itemID, removed.getSupplierPrice() ,price);
        Map<String, Boolean> notify = new HashMap<>();
        if(isDamaged)
            notify.put("isDamaged", true);
        else notify.put("isDamaged", false);
        if(productList.get(productID).getTotalAmount() <= productList.get(productID).getNotificationAmount())
            notify.put("shortage", true);
        else notify.put("shortage", false);
        return notify;
    }

    public Map<Pair<String, String>, Integer> getPurchaseDeficiencyList(String purchaseID) {
        List<String> purchaseProducts = purchaseList.get(purchaseID).getProductsList();
        Map<Pair<String,String>, Integer> ProductsDeficiencyList = new HashMap<>();
        for (String productID : purchaseProducts) {
            if (productList.get(productID).shouldAddToDeficiencyOrder()) {
                ProductsDeficiencyList.put(new Pair<>(productID.split("#")[1], productList.get(productID).getName()), productList.get(productID).addToDeficiencyOrderAndGetAmount());
            }
        }
        return ProductsDeficiencyList;
    }

    /**
     * 
     * @param IDsList
     * @return a list of Category Objects that has the given IDs
     */
    public List<Category> getMainCategoryListByIDs(List<String> IDsList) {
        List<Category> toReturn = new ArrayList<>();
        for (String ID: IDsList) {
            toReturn.add(mainCategoryList.get(ID));
        }
        return toReturn;
    }

    /**
     * 
     * @param categoryIDs
     * @return Category objects List (chain of existing subs)
     */
    private List<Category> getCategoryChainByIDList(List<String> categoryIDs) throws Exception{
        try {
            if (!mainCategoryList.containsKey(categoryIDs.get(0))) {
                if (categoryDAO.checkIfCategoryExists(categoryIDs.get(0))) {
                    mainCategoryList.put(categoryIDs.get(0), new Category(categoryDAO.getCategory(categoryIDs.get(0)), categoryDAO, allProductsByCategories()));
                } else {
                    throw new NullPointerException("a main category with the given ID doesn't exists");
                }
            }

        }
        catch(Exception ex){
            throw new Exception(ex.getMessage());
        }
        List<String> categoryIDsCopy = new ArrayList<>(categoryIDs);
        Category mainCategory = mainCategoryList.get(categoryIDsCopy.remove(0));
        List<Category> categoryChain = mainCategory.getCategoryChainBySubIDList(categoryIDsCopy);
        categoryChain.add(0,mainCategory);
        return categoryChain;
    }


    /**
     * 
     * @param IDsList
     * @return a list of Product Objects that has the given IDs
     */
    public List<Product> getProductListByIDs(List<String> IDsList) {
        List<Product> toReturn = new ArrayList<>();
        for (String ID: IDsList) {
            toReturn.add(productList.get(ID));
        }
        return toReturn;
    }

    /**
     * Creates and adds a new discount due to the given details
     * @param sDate
     * @param eDate
     * @param value
     * @param isPercent
     * @param typeString
     * @param damString
     * @param IDsList
     */
    public void addDiscount(Date sDate, Date eDate, double value, boolean isPercent, String typeString, String damString, List<String> IDsList) throws Exception{
        Discount.DiscountType disType;
        Item.DamageType damType = Item.DamageType.OTHER;
        if (typeString.equals("CATEGORY")) {
            disType = DiscountType.CATEGORY;
        }
        else if (typeString.equals("PRODUCT")) {
            disType = DiscountType.PRODUCT;
        }
        else {
            disType = DiscountType.DAMAGED;
            damType = convertStringToEnumDamageType(damString);
        }
        switch (disType) {
            case CATEGORY:
            //in case category list is used to get to the desired category. not possible to add a discount for several categories at the same time
            //in oroduct and damaged - possible to add to several
                /*for (String catID: IDsList) {
                    mainCategoryList.get(catID).addDiscount(generateDiscountID(), sDate, eDate, value, isPercent, disType);
                }*/
                getLastCategoryInIDChain(IDsList).addCategoryDiscount(generateDiscountID(), sDate, eDate, value, isPercent, disType);
                break;
            case PRODUCT:
                for (String prodID: IDsList) {
                    prodID = addHashtagToIDIfNeeded(prodID);
                    productList.get(prodID).addProductDiscount(generateDiscountID(), sDate, eDate, value, isPercent, disType);
                }
                break;
            case DAMAGED:
                for (String prodID: IDsList) {
                    prodID = addHashtagToIDIfNeeded(prodID);
                    productList.get(prodID).addDamagedDiscount(generateDiscountID(), sDate, eDate, value, isPercent, disType, damType);
                }
                break;
        }
    }


    /**
     * 
     * @param itemID
     * @return the calculated price of the given item
     */
    public double getItemCalculatedPrice(String itemID) throws Exception {
        List<String> splitItemID = splitItemID(itemID);
        Product product = productList.get(splitItemID.get(0));
        double CalculatedPrice = getProductCalculatedPrice(product);
        CalculatedPrice = product.getAndUpdateItemCalculatedPrice(itemID, CalculatedPrice);       
        return CalculatedPrice;
    }


    /**
     * 
     * @param product
     * @return product's price after implementing the best discount (out of its categories and product discounts)
     */
    public double getProductCalculatedPrice(Product product) throws Exception{
        double sellingPrice = product.getSellingPrice();
        double calculatedPrice = sellingPrice;
        List<String> categoryIDList = product.getCategoryIDChain();
        List<Category> categoryList = getCategoryChainByIDList(categoryIDList);
        for (Category category : categoryList) {
            calculatedPrice = category.getPriceAfterDiscount(sellingPrice, calculatedPrice);
        }
        calculatedPrice = product.getProductCalculatedPrice(calculatedPrice);
        return calculatedPrice;
    }


    /**
     * setting the item of the given itemID as damaged (due to the given damagetype)
     * @param itemID
     * @param damageType
     */
    public void setAnItemAsDamaged(String itemID, String damageType) throws SQLException{
        //or set a list of items as damaged??
        List<String> ids = splitItemID(itemID);
        if(!isProductExist(ids.get(0))){
            throw new NullPointerException("Product" + itemID + " doesn't exist in the system. ");
        }
        productList.get(ids.get(0)).setItemAsDamaged(itemID, convertStringToEnumDamageType(damageType));
    }


    /**
     * 
     * @return A map of mainCategory details - <categroty ID, Category Name>
     */
    public Map<String, String> getMainCategoryNameMapByIDs() throws Exception {
        List<CategoryDTO> categoriesList;
        try {
            categoriesList = categoryDAO.getMainCategories(BRANCH_ID);
        }
        catch (SQLException ex){
            throw new Exception("Database error: " + ex.getMessage());
        }
        Map<String, String> ans = new HashMap<>();
        Map<String, List<Product>> products = allProductsByCategories();
        for(CategoryDTO cDTO: categoriesList){
            ans.put(cDTO.getCATEGORY_ID(), cDTO.getName());
            if(!mainCategoryList.containsKey(cDTO.getCATEGORY_ID())){
                mainCategoryList.put(cDTO.getCATEGORY_ID(), new Category(cDTO, categoryDAO, products));
            }
        }
        return ans;
    }


    /**
     * 
     * @param categoriesIDS
     * @return
     */
    public Map<String, String> getProducts(List<String> categoriesIDS){
        if(categoriesIDS.size() == 0){
            return new HashMap<>();
        }
        Category cat = mainCategoryList.get(categoriesIDS.get(0));
        categoriesIDS.remove(0);
        return cat.getProducts(categoriesIDS);
    }


    /**
     * 
     * @param categories
     * @return  A map of subCategory details - <categroty ID, Category Name>
     */
    public Map<String, String> getSubCategories(List<String> categories) {
        if(categories.size() == 0)
            return new HashMap<>();
        Category cat = mainCategoryList.get(categories.get(0));
        categories.remove(0);
        return cat.getSub(categories);
    }

    public boolean isProductExist(String id) throws SQLException{
        id = addHashtagToIDIfNeeded(id);
        if(!productList.containsKey(id) && productDAO.checkIfProductExists(id)){
            ProductDTO pdto = productDAO.getProduct(id);
            productList.put(id, new Product(pdto, getCategoryIDChainByID(pdto.getCategory()), productDAO));
        }
        return productList.containsKey(id);
    }

    private boolean isCategoriesExist(List<String> categoryIDChain) throws Exception{
        try {
            if (!mainCategoryList.containsKey(categoryIDChain.get(0))) {
                if (categoryDAO.checkIfCategoryExists(categoryIDChain.get(0))) {
                    mainCategoryList.put(categoryIDChain.get(0), new Category(categoryDAO.getCategory(categoryIDChain.get(0)), categoryDAO, allProductsByCategories()));
                } else {
                    throw new NullPointerException("a main category with the given ID doesn't exists");
                }
            }
        }
        catch(Exception ex){
            throw new Exception(ex.getMessage());
        }
        return categoryDAO.checkIfCategoryExists(categoryIDChain.get(0));
    }

    private String addHashtagToIDIfNeeded (String id) {
        if (!id.contains("#")) {
            id = getBranchID() + "#" + id;
        }
        return id;
    }


    public String getProductName(String id) throws Exception{
        if (isProductExist(id)) {
            id = addHashtagToIDIfNeeded(id);
            return productList.get(id).getName();
        }
        else {
            return "-";
        }
    }

    /**
     * creates and prints a supply report
     * @param categoriesString
     */
    public String createDeficiencyReport(List<List<String>> categoriesString) throws Exception {
        List<Category> finalCategories = new ArrayList<>();
        for (List<String> list : categoriesString) {
            List<Category> catList = getCategoryChainByIDList(list);
            if(!finalCategories.contains(catList.get(catList.size() - 1)))
                finalCategories.add(catList.get(catList.size() - 1));
        }
        List<String> catIDS = new ArrayList<>();
        for (Category c: finalCategories) {
            catIDS.add(c.getCATEGORY_ID());
        }
        String id = generateReportID();
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(date);
        reportDAO.addReport(id, "Deficiency Report", strDate, catIDS);
        Report report = new DeficiencyReport(id, "Deficiency Report", finalCategories, date);
        reportList.get("Deficiency Report").add(report);
        return report.toString();
    }


    /**
     * creates and prints a damaged report
     * @param categoriesString
     * @param damageList
     */
    public String createDamageReport(List<List<String>> categoriesString, List<String> damageList) throws Exception{
        List<Item.DamageType> damages = new ArrayList<>();
        for (String damageType : damageList) {
            if(damageType.equals("BROKEN")){
                damages.add(Item.DamageType.BROKEN);
            }
            else{
                if(damageType.equals("EXPIRED")){
                    damages.add(Item.DamageType.EXPIRED);
                }
                else{
                    if(damageType.equals("SCRATCH")){
                        damages.add(Item.DamageType.SCRATCH);
                    }
                    else{
                        if(damageType.equals("OTHER")){
                            damages.add(Item.DamageType.OTHER);
                        }
                    }
                }
            }
        }
        List<Category> finalCategories = new ArrayList<>();
        for (List<String> list : categoriesString) {
            List<Category> catList = getCategoryChainByIDList(list);
            if(!finalCategories.contains(catList.get(catList.size() - 1)))
                finalCategories.add(catList.get(catList.size() - 1));
        }
        List<String> catIDS = new ArrayList<>();
        for (Category c: finalCategories) {
            catIDS.add(c.getCATEGORY_ID());
        }
        String id = generateReportID();
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(date);
        reportDAO.addReport(id, "Damaged Report", strDate, catIDS);
        reportDAO.addDamages(id, damageList);
        Report report = new DamagedReport(id, "Damaged Report", finalCategories, damages, date);
        reportList.get("Damaged Report").add(report);
        return report.toString();
    }


    /**
     * creates and prints a stock report
     * @param categoriesString
     */
    public String createStockReport(List<List<String>> categoriesString) throws Exception{
        List<Category> finalCategories = new ArrayList<>();
        for (List<String> list : categoriesString) {
            List<Category> catList = getCategoryChainByIDList(list);
            if(!finalCategories.contains(catList.get(catList.size() - 1)))
                finalCategories.add(catList.get(catList.size() - 1));
        }
        List<String> catIDS = new ArrayList<>();
        for (Category c: finalCategories) {
            catIDS.add(c.getCATEGORY_ID());
        }
        String id = generateReportID();
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(date);
        reportDAO.addReport(id, "Stock Report", strDate, catIDS);
        Report report = new StockReport(id, "Stock Report", finalCategories, date);
        reportList.get("Stock Report").add(report);
        return report.toString();
    }



    //return all damage Types not including NONE!
    /**
     * 
     * @return A map of serial numbers and damageType enums converted to string
     */
    public Map<Integer, String> getDamageEnumStrings(){
        Map<Integer, String> damages = new HashMap<>();
        int counter = 1;
        for(Item.DamageType type: Item.DamageType.values()){
            if (type != Item.DamageType.NONE) {
                damages.put(counter, type.toString());
                counter++;
            }
        }
        return damages;
    }


    /**
     * Loads data for initialization of branch0
     */
    public void setUpStockController1() throws Exception{
        List<String> chain1 = new ArrayList<>();
        chain1.add(addCategory(new ArrayList<>(chain1), "Dairy"));
        chain1.add(addCategory(new ArrayList<>(chain1), "Milk"));
        chain1.add(addCategory(new ArrayList<>(chain1), "1 liter"));
        addNewProduct("111", "Milk 3%", 4.5, 8, "Tnuva", 20, 3, 60, new ArrayList<>(chain1));
        addNewProduct("222", "Milk 1% (Yotvata)", 4.5, 8, "Yotvata", 10, 3, 50, new ArrayList<>(chain1));
        chain1.remove(2);
        chain1.add(addCategory(new ArrayList<>(chain1), "2 liter"));
        addNewProduct("112", "Milk 1% (Tnuva)", 9, 16, "Tnuva", 20, 3, 60, new ArrayList<>(chain1));
        chain1.remove(2);
        chain1.remove(1);
        chain1.add(addCategory(new ArrayList<>(chain1), "Cheese"));
        chain1.add(addCategory(new ArrayList<>(chain1), "400 gr"));
        addNewProduct("333", "Emek cheese", 8.5, 20, "Emek", 10, 3, 30, new ArrayList<>(chain1));

        List<String> chain2 = new ArrayList<>();
        chain2.add(addCategory(new ArrayList<>(chain2), "Meat"));
        chain2.add(addCategory(new ArrayList<>(chain2), "Steak"));
        chain2.add(addCategory(new ArrayList<>(chain2), "300 gr"));
        addNewProduct("444", "Steak Adom", 50, 100, "Adom Adom", 7, 5, 35, new ArrayList<>(chain2));
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date expire = null;
        try{
            expire = format.parse("30-04-2023");
        } 
        catch(Exception ex){}
        receiveSupply("111", 4, 0, expire);
        try{
            expire = format.parse("05-04-2023");
        } 
        catch(Exception ex){}
        receiveSupply("222", 10, 65, expire);
        try{
            expire = format.parse("20-06-2023");
        } 
        catch(Exception ex){}
        receiveSupply("112", 10, 30, expire);
        try{
            expire = format.parse("20-06-2023");
        } 
        catch(Exception ex){}
        receiveSupply("333", 20, 15, expire);
        try{
            expire = format.parse("30-04-2023");
        } 
        catch(Exception ex){}
        receiveSupply("444", 10, 20, expire);

        setAnItemAsDamaged("1#222-2", "OTHER");
        setAnItemAsDamaged("1#444-2", "OTHER");
        setAnItemAsDamaged("1#222-3", "OTHER");
        setAnItemAsDamaged("1#111-1", "EXPIRED");
        setAnItemAsDamaged("1#111-1", "EXPIRED");
        setAnItemAsDamaged("1#111-2", "EXPIRED");
        setAnItemAsDamaged("1#111-3", "EXPIRED");

        List<String> discountIDList = new ArrayList<>();
        discountIDList.add("1#444");
        try {
            addDiscount(format.parse("01-04-2023"), format.parse("01-07-2023"), 20, true, "PRODUCT", null, discountIDList);
        }
        catch(Exception ex){}
        discountIDList.clear();
        discountIDList.add("1#1");
        discountIDList.add("1#1-1");
        discountIDList.add("1#1-1-1");
        try {
            addDiscount(format.parse("01-04-2023"), format.parse("01-07-2023"), 2, false, "CATEGORY", null, discountIDList);
        }
        catch(Exception ex){}
        discountIDList.clear();
        discountIDList.add("1#222");
        try {
            addDiscount(format.parse("01-04-2023"), format.parse("01-07-2023"), 3, false, "DAMAGED", "OTHER", discountIDList);
        }
        catch(Exception ex){}
    }


    /**
     * Loads data for initialization of branch1
     */
    public void setUpStockController2() throws Exception{
        List<String> chain1 = new ArrayList<>();
        chain1.add(addCategory(new ArrayList<>(chain1), "Vegetables and Fruits"));
        chain1.add(addCategory(new ArrayList<>(chain1), "Vegetables"));
        chain1.add(addCategory(new ArrayList<>(chain1), "Tomato"));
        addNewProduct("555", "Chery", 4.5, 8, "Amit", 20, 3, 30, new ArrayList<>(chain1));
        addNewProduct("666", "Tamar", 4.5, 8, "Amit", 10, 3, 40, new ArrayList<>(chain1));
        chain1.remove(2);
        chain1.add(addCategory(new ArrayList<>(chain1), "Cucumber"));
        addNewProduct("552", "Mini", 4, 10, "Gili", 20, 3, 45, new ArrayList<>(chain1));
        chain1.remove(2);
        chain1.remove(1);
        chain1.add(addCategory(new ArrayList<>(chain1), "Fruits"));
        chain1.add(addCategory(new ArrayList<>(chain1), "Apple"));
        addNewProduct("777", "Pink lady", 8.5, 20, "Tali", 10, 3, 30, new ArrayList<>(chain1));
        List<String> chain2 = new ArrayList<>();
        chain2.add(addCategory(new ArrayList<>(chain2), "Drinks"));
        chain2.add(addCategory(new ArrayList<>(chain2), "Cola"));
        chain2.add(addCategory(new ArrayList<>(chain2), "1.5 liter"));
        addNewProduct("888", "Coca Cola", 10, 12, "Coca Cola", 7, 5, 35, new ArrayList<>(chain2));
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date expire = null;
        try{
            expire = format.parse("30-06-2025");
        }
        catch(Exception ex){}
        receiveSupply("555", 10, 15, expire);
        try{
            expire = format.parse("05-04-2025");
        }
        catch(Exception ex){}
        receiveSupply("666", 10, 65, expire);
        try{
            expire = format.parse("20-06-2025");
        }
        catch(Exception ex){}
        receiveSupply("552", 10, 30, expire);
        try{
            expire = format.parse("20-06-2025");
        }
        catch(Exception ex){}
        receiveSupply("777", 20, 15, expire);
        try{
            expire = format.parse("30-07-2025");
        }
        catch(Exception ex){}
        receiveSupply("888", 10, 20, expire);

        setAnItemAsDamaged("2#666-2", "OTHER");
        setAnItemAsDamaged("2#888-2", "BROKEN");
        setAnItemAsDamaged("2#666-3", "OTHER");

        List<String> discountIDList = new ArrayList<>();
        discountIDList.add("2#888");
        try {
            addDiscount(format.parse("01-04-2023"), format.parse("01-07-2023"), 20, true, "PRODUCT", null, discountIDList);
        }
        catch(Exception ex){}
        discountIDList.clear();
        discountIDList.add("2#1");
        discountIDList.add("2#1-1");
        discountIDList.add("2#1-1-1");
        try {
            addDiscount(format.parse("01-04-2023"), format.parse("01-07-2023"), 2, false, "CATEGORY", null, discountIDList);
        }
        catch(Exception ex){}
        discountIDList.clear();
        discountIDList.add("2#888");
        try {
            addDiscount(format.parse("01-04-2023"), format.parse("01-07-2023"), 3, false, "DAMAGED", "BROKEN", discountIDList);
        }
        catch(Exception ex){}
    }


    /**
     * Changes the purchase price for the product (use in case the supplier changed it)
     * @param productID
     * @param price
     */
    public void SetPurchasePriceForProduct(String productID, Double price) throws Exception{
        if (!isProductExist(productID)) {
            throw new IllegalArgumentException("Product is not found.");
        }
        productID = addHashtagToIDIfNeeded(productID);
        productList.get(productID).setPurchasePrice(price);
    }


         //Green lines fron now on - trying the "get last...."" method - all can be deleted if it works

    /**
     * 
     * @param categoryChain
     * @return A Map of the last category in the chain's discounts = <discountID, !!!
     */
    public Map<String, String> getCategotyDiscountList(List<String> categoryChain) throws Exception{
        //List<Category> categoryList = getCategoryChainByIDList(categoryChain);
        //categoryList.get(categoryList.size()-1).getDiscountIDMap();
        return getLastCategoryInIDChain(categoryChain).getDiscountIDMap();
    } 


    /**
     * Deletes the discount from the last category in chain
     * @param categoryChain
     * @param discountID
     */
    public void deleteCategoryDiscount(List<String> categoryChain, String discountID) throws Exception{
        //List<Category> categoryList = getCategoryChainByIDList(categoryChain);
        //categoryList.get(categoryList.size()-1).deleteDiscount(discountID);
        getLastCategoryInIDChain(categoryChain).deleteDiscount(discountID);
    }


    /**
     * Update the category's discount's start date to the given date
     * @param categoryChain
     * @param discountID
     * @param startDate
     */
    public void updateCategoryDiscountStartDate(List<String> categoryChain, String discountID, Date startDate) throws Exception{
        //List<Category> categoryList = getCategoryChainByIDList(categoryChain);
        //categoryList.get(categoryList.size()-1).getDiscount(discountID).setStartDate(startDate);
        getLastCategoryInIDChain(categoryChain).getDiscount(discountID).setStartDate(startDate);
    }


    /**
     * Update the category's discount's end date to the given date
     * @param categoryChain
     * @param discountID
     * @param endDate
     */
    public void updateCategoryDiscountEndDate(List<String> categoryChain, String discountID, Date endDate) throws Exception{
        //List<Category> categoryList = getCategoryChainByIDList(categoryChain);
        //categoryList.get(categoryList.size()-1).getDiscount(discountID).setEndDate(endDate);
        getLastCategoryInIDChain(categoryChain).getDiscount(discountID).setEndDate(endDate);
    }


    /**
     * Update the category's discount's isPercent value to the given value
     * @param categoryChain
     * @param discountID
     * @param isPercent
     */
    public void updateCategoryDiscountIsPercent(List<String> categoryChain, String discountID, boolean isPercent) throws Exception{
        //List<Category> categoryList = getCategoryChainByIDList(categoryChain);
        //categoryList.get(categoryList.size()-1).getDiscount(discountID).setPercent(isPercent);
        getLastCategoryInIDChain(categoryChain).getDiscount(discountID).setPercent(isPercent);
    }


    /**
     * Update the category's discount's discount value to the given discountValue
     * @param categoryChain
     * @param discountID
     * @param discountValue
     */
    public void updateCategoryDiscountValue(List<String> categoryChain, String discountID, double discountValue) throws Exception{
        //List<Category> categoryList = getCategoryChainByIDList(categoryChain);
        //categoryList.get(categoryList.size()-1).getDiscount(discountID).setDiscountValue(discountValue);
        getLastCategoryInIDChain(categoryChain).getDiscount(discountID).setDiscountValue(discountValue);
    }


    /**
     * 
     * @param productID
     * @param damageString
     * @return A Map of the product's discounts for the given damageType string = <discountID, !!!
     */
    public Map<String, String> getProductDiscountsByDamage(String productID, String damageString) throws Exception{
        productID = addHashtagToIDIfNeeded(productID);
        return productList.get(productID).getDamagedDiscountIDMap(convertStringToEnumDamageType(damageString));
    }


    /**
     * Deletes the discount from the given damageType's discounts list
     * @param productID
     * @param discountID
     * @param damageType
     */
    public void deleteProductDiscount(String productID, String discountID, String damageType) throws Exception{
        productID = addHashtagToIDIfNeeded(productID);
        productList.get(productID).deleteDiscount(discountID, convertStringToEnumDamageType(damageType));
    }


    /**
     * Updates the discount's start date to the given date (in damageType's discounts list)
     * @param productID
     * @param damageType
     * @param discountID
     * @param startDate
     */
    public void updateProductDiscountStartDate(String productID, String damageType, String discountID, Date startDate) {
        productID = addHashtagToIDIfNeeded(productID);
        productList.get(productID).getDiscount(convertStringToEnumDamageType(damageType), discountID).setStartDate(startDate);
    }


    /**
     * Updates the discount's end date to the given date (in damageType's discounts list)
     * @param productID
     * @param damageType
     * @param discountID
     * @param endDate
     */
    public void updateProductDiscountEndDate(String productID, String damageType, String discountID, Date endDate) {
        productID = addHashtagToIDIfNeeded(productID);
        productList.get(productID).getDiscount(convertStringToEnumDamageType(damageType), discountID).setEndDate(endDate);

    }

    
    /**
     * Updates the discount's isPercent value to the given value (in damageType's discounts list)
     * @param productID
     * @param damageType
     * @param discountID
     * @param isPercent
     */
    public void updateProductDiscountIsPercent(String productID, String damageType, String discountID, boolean isPercent) {
        productID = addHashtagToIDIfNeeded(productID);
        productList.get(productID).getDiscount(convertStringToEnumDamageType(damageType), discountID).setPercent(isPercent);
    }

    
    /**
     * Updates the discount's value to the given discountValue (in damageType's discounts list)
     * @param productID
     * @param damageType
     * @param discountID
     * @param discountValue
     */
    public void updateProductDiscountValue(String productID, String damageType, String discountID, double discountValue) {
        productID = addHashtagToIDIfNeeded(productID);
        productList.get(productID).getDiscount(convertStringToEnumDamageType(damageType), discountID).setDiscountValue(discountValue);
    }


    /**
     * 
     * @param purchaseID
     * @return a Map of <itemID, itemPrice> contains of all the item purchased in the given purchase
     */
    public Map<String, Double> finishPurchase(String purchaseID) throws Exception{
        return purchaseList.get(purchaseID).finishPurchase();
    }


    /**
     * 
     * @param purchaseID
     * @return the purchase's total price
     */
    public double getPurchaseTotalPrice(String purchaseID) {
        return purchaseList.get(purchaseID).getTotalPrice();
    }

    public void sendDeficiencyOrderToSuppliers(String purchaseID) throws Exception{
        Map<Pair<String, String>, Integer> purchaseDeficiencyList = getPurchaseDeficiencyList(purchaseID);
        if ((purchaseDeficiencyList!= null) && (purchaseDeficiencyList.size() > 0)) {
            boolean defOrderWasSent = sTS.sendDeficiencyOrderToSuppliers(getBranchID(), purchaseDeficiencyList);
            if (!defOrderWasSent) {
                undoDefOrder(purchaseDeficiencyList.keySet().stream().toList());
            }
        }
    }

    public void undoDefOrder(List<Pair<String, String>> productsPair) {
        for (Pair<String, String> pair : productsPair) {
            productList.get(addHashtagToIDIfNeeded(pair.getFirst())).setHasDefInProcess(false);
        }
    }

    /**
     * 
     * @param categoryIDList
     * @return the Category object of the last ID in chain
     */
    private Category getLastCategoryInIDChain(List<String> categoryIDList) throws Exception{
        List<Category> categoryList = getCategoryChainByIDList(categoryIDList);
        return categoryList.get(categoryList.size()-1);
    }

    public Map<Integer, String> getAutoOrdersList() {
        return sTS.getAutoOrdersList(getBranchID());
    }

    public void deleteAutomaticOrder(int orderID) {
        sTS.deleteAutomaticOrder(getBranchID(), orderID);
    }

    public int addAutomaticOrder(List<Integer> supplyDays, Map<Pair<String, String>, Integer> productsToOrder) throws Exception{
        int orderID = generateOrderID();
        sTS.addAutomaticOrder(getBranchID(), orderID, supplyDays, productsToOrder);
        return orderID;
    }

    public boolean addExpressOrder(Map<Pair<String, String>, Integer> productsToOrder) throws Exception{
        return sTS.addExpressOrder(getBranchID(), productsToOrder);
    }

    public boolean updateOrderSupplyDays(int orderID, List<Integer> newSupplyDays) {
        boolean ans = sTS.updateOrderSupplyDays(getBranchID(), orderID, newSupplyDays);
        if (!ans) {
            throw new IllegalArgumentException("Order cannot be edited 24 hours before supply time!");
        }
        return ans;
    }

    public Map<Pair<String, String>, Integer> getOrderProductsMap(int orderID) {
        return sTS.getOrderProductsMap(getBranchID(), orderID);
    }

    public boolean updateOrderProductsAndAmounts(int orderID, Map<Pair<String, String>, Integer> productsAndAmountsMap)  throws  Exception{
        boolean ans = sTS.updateOrderProductsAndAmounts(getBranchID(), orderID, productsAndAmountsMap);
        if (!ans) {
            throw new IllegalArgumentException("Order cannot be edited 24 hours before supply time!");
        }
        return ans;
    }

    private Map<String, List<Product>> loadCategoriesProducts(List<String> categories) throws Exception{
        Map<String, List<Product>> productsMap = new HashMap<>();
        for (String id: categories){
            productsMap.put(id, new ArrayList<>());
            List<ProductDTO> pdtos = categoryDAO.getCategoriesProducts(id);
            for (ProductDTO pdto: pdtos) {
                if(!productList.containsKey(pdto.getProductID())){
                    Product p = new Product(pdto, getCategoryIDChainByID(pdto.getCategory()), productDAO);
                    productList.put(pdto.getProductID(), p);
                }
                productsMap.get(id).add(productList.get(pdto.getProductID()));
            }
        }
        return productsMap;
    }

    private List<String> getCategoryIDChainByID(String id){
        String[] split = id.split("-");
        List<String> ans = new ArrayList<>();
        String chain = new String();
        for(int i = 0; i < split.length; i++){
            chain = chain + split[i];
            ans.add(chain);
            if (i < split.length - 1) {
                chain = chain + "-";
            }
        }
        return ans;
    }

    private Map<String, List<Product>> loadAllProducts(List<String> categories) throws SQLException{
        Map<String, List<ProductDTO>> dtos = productDAO.loadAllCategoriesProducts(categories);
        Map<String, List<Product>> products = new HashMap<>();
        for (String category: dtos.keySet()) {
            products.put(category, new ArrayList<>());
            for (ProductDTO pdto: dtos.get(category)) {
                if(!productList.containsKey(pdto.getProductID())){
                    Product p = new Product(pdto, getCategoryIDChainByID(pdto.getCategory()), productDAO);
                    productList.put(pdto.getProductID(), p);
                    products.get(category).add(p);
                }
            }
        }
        return products;
    }

    private Map<String, List<Product>> allProductsByCategories() throws SQLException{
        List<CategoryDTO> categoryDTOS = categoryDAO.getCategories(getBranchID());
        List<String> cIds = new ArrayList<>();
        for (CategoryDTO cdto: categoryDTOS) {
            cIds.add(cdto.getCATEGORY_ID());
        }
        Map<String, List<Product>> productsByCategory = loadAllProducts(cIds);
        return productsByCategory;
    }

}










