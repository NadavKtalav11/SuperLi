package BusinessLayer.Stock;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import BusinessLayer.Stock.Discount.DiscountType;
import PersistenceLayer.DAO.Stock.CategoryDAO;
import PersistenceLayer.DAO.Stock.DiscountInStoreDAO;
import PersistenceLayer.DTO.Stock.CategoryDTO;
import PersistenceLayer.DTO.Stock.DiscountInStoreDTO;
import PersistenceLayer.DTO.Stock.ItemDTO;

public class Category {
    private final String CATEGORY_ID;
    private String name;
    private Map<String,Category> subCategoryList;
    private List<Product> productList;
    private List<Discount> discountList;
    private int subCounter = 1;
    private CategoryDAO categoryDAO;
    private DiscountInStoreDAO discountDAO = new DiscountInStoreDAO();

    public Category(String categoryID, String name, CategoryDAO categoryDAO) {
        this.CATEGORY_ID = categoryID;
        this.name = name;
        this.subCategoryList = new HashMap<>();
        this.productList = new ArrayList<>();
        this.discountList = new ArrayList<>();
        this.categoryDAO = categoryDAO;
    }

    public Category(CategoryDTO cDTO, CategoryDAO categoryDAO, Map<String, List<Product>> products) {
        this.CATEGORY_ID = cDTO.getCATEGORY_ID();
        this.name = cDTO.getName();
        this.categoryDAO = categoryDAO;
        this.subCategoryList = new HashMap<>();
        this.productList = products.get(cDTO.getCATEGORY_ID());
        this.discountList = new ArrayList<>();
        try {
            for (CategoryDTO cdto : categoryDAO.getSubCategories(CATEGORY_ID)) {
                this.subCategoryList.put(cdto.getCATEGORY_ID(), new Category(cdto, categoryDAO, products));
            }
            this.subCounter = cDTO.getSubCounter();
        } catch (Exception ex){

        }
    }

    public String getCATEGORY_ID() {
        return CATEGORY_ID;
    }

    public String getName(){
        return name;
    }

    public List<Discount> getDiscountList(){
        return discountList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public List<String> getDiscountIDList(){
        List<String> IDList = new ArrayList<>();
        for (Discount discount : getDiscountList()) {
            IDList.add(discount.getDiscountID());
        }
        return IDList;
    }

    public Map<String, String> getDiscountIDMap() throws Exception{
        Map<String, String> toReturn = new HashMap<>();
        if (getDiscountList().size() == 0) {
            loadCategoryDiscounts();
        }
        if (getDiscountList().size() != 0) {
            for (Discount discount : getDiscountList()) {
                toReturn.put(discount.getDiscountID(), discount.toString());
            }
        }
        return toReturn;
    }


    private void loadCategoryDiscounts() throws Exception {
        try {
            for (DiscountInStoreDTO discountDTO : discountDAO.getCategoryDiscounts(getCATEGORY_ID())) {
                Discount d = new Discount(discountDTO, discountDAO);
                discountList.add(d);
            }
        }
        catch (Exception ex){
            throw new Exception("failed to set discounts list");
        }
    }


    private int convertBooleanToInt(boolean bool) {
        if (bool) {
            return 1;
        }
        return 0;
    }

    /**
     * @return next valid sub-category id
     */
    private String generateSubCategoryID(){
        Integer id = subCounter;
        subCounter++;
        categoryDAO.updateSubCategoryID(subCounter, getCATEGORY_ID());
        return getCATEGORY_ID() + "-" + id.toString();
    }

    /**
     * @param name
     * adds new subCategory
     */
    public String addSubCategory(List<String> categories, String name) throws Exception{
        //Category newSub = new Category(CATEGORY_ID + "/" + generateSubCategoryID(), name);
        if(categories.size() == 0){
            String id = generateSubCategoryID();
            categoryDAO.addCategory(id, name, getCATEGORY_ID());
            Category newSub = new Category(id, name, categoryDAO);
            subCategoryList.put(newSub.getCATEGORY_ID(), newSub);
            return newSub.getCATEGORY_ID();
        }
        else{
            Category cat = subCategoryList.get(categories.get(0));
            categories.remove(0);
             return cat.addSubCategory(categories, name);
        }
    }

    public List<Category> getSubCategoryList() {
        List<Category> toReturn = new ArrayList<>();
        if (subCategoryList.size() != 0) {
            for (String catID : subCategoryList.keySet()) {
                toReturn.add(subCategoryList.get(catID));
            }
        }
        return toReturn;
    }

    /**
     * @param product
     * adds new Product
     */
    public void addProduct(Product product){
        productList.add(product);
    }

    public void addCategoryDiscount(String discountID, Date sDate, Date eDate, double value, boolean isPercent, DiscountType disType) throws Exception {
        if (getDiscountList().size() == 0) {
            loadCategoryDiscounts();
        }
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        discountDAO.addDiscount(discountID, dateFormat.format(sDate), dateFormat.format(eDate), value, convertBooleanToInt(isPercent), disType.toString());
        discountDAO.addDiscountToDisCatTable (discountID, CATEGORY_ID);
        discountList.add(new Discount(discountID, sDate, eDate, value, isPercent, disType, discountDAO));
    }

    /**
     * 
     * @param categoryIDs
     * @return
     */
    public List<Category> getCategoryChainBySubIDList(List<String> categoryIDs) {
        if (categoryIDs.size() == 0) {
            return new ArrayList<>();
        }
        if (!subCategoryList.containsKey(categoryIDs.get(0))) {
            throw new NullPointerException("A sub category with the given ID doesn't exist");
        }
        List<Category> categoryChain;
        Category thisCategory = subCategoryList.get(categoryIDs.remove(0));
        if (categoryIDs.isEmpty()) {
            categoryChain = new ArrayList<>();          
        }
        else {
            categoryChain = thisCategory.getCategoryChainBySubIDList(categoryIDs);
        }
        categoryChain.add(0,thisCategory);
        return categoryChain;
    }

    public Map<String, String> getProducts(List<String> categoriesIDS) {
        if(categoriesIDS.size() == 0){
            Map<String, String> ans = new HashMap<>();
            for(Product p: productList){
                ans.put(p.getProductID(), p.getName());
            }
            return ans;
        }
        if(subCategoryList.size() ==0){
            return new HashMap<>();
        }
        Category cat = subCategoryList.get(categoriesIDS.get(0));
        categoriesIDS.remove(0);
        return cat.getProducts(new ArrayList<>(categoriesIDS));
    }

    public Map<String, String> getSub(List<String> categories) {
        if(categories.size() == 0){
            Map<String, String> ans = new HashMap<>();
            for(String id: subCategoryList.keySet()){
                ans.put(id, subCategoryList.get(id).getName());
            }
            return ans;
        }
        Category cat = subCategoryList.get(categories.get(0));
        categories.remove(0);
        return cat.getSub(categories);
    }

    public Map<Product, Integer> getShortageInCategory(){
        Map<Product, Integer> ans = new HashMap<>();
        for (Product product : productList) {
            if(product.isBelowMinimumAmount()){
                ans.put(product, product.getNotificationAmount() - product.getTotalAmount());
            }
        }
        for(Category category: subCategoryList.values()){
            ans.putAll(category.getShortageInCategory());
        }
        return ans;
    }

    public Map<Item.DamageType, List<Item>> getDamagedInCategoryByType(List<Item.DamageType> damages){
        Map<Item.DamageType, List<Item>> ans = new HashMap<>();
        for (Item.DamageType type : damages) {
            ans.put(type, new ArrayList<>());
            for (Product product : productList) {
                ans.get(type).addAll(product.getDamagedItemsByDamage(type));
            }
        }
        Map<Item.DamageType, List<Item>> other;
        for(Category category: subCategoryList.values()){
            other = category.getDamagedInCategoryByType(damages);
            for (Item.DamageType damageType : damages) {
                if(other.containsKey(damageType)){
                    ans.get(damageType).addAll(other.get(damageType));
                }
            }
        }
        return ans;
    }

    /**
     * deletes all the invalid discounts (their endDate has passed) from all discountList
     */
    public void correctDiscountList() {
        if (discountList.size() > 0) {
            for (Discount discount : discountList) {
                if (discount.isOver()) {
                    discountList.remove(discount);
                }
            }
        }
    }


    /**
     * calls correctDiscountList and then calculates the price
     * @param sellingPrice
     * @param calculatedPrice
     * @return the best price for this product, after activating the best discout for this category(or not)
     */
    public double getPriceAfterDiscount(double sellingPrice, double calculatedPrice) throws Exception{
        double bestPrice = calculatedPrice;
        if (discountList.size() == 0) {
            loadCategoryDiscounts();
            correctDiscountList();
        }
        double tempPrice;
        if (discountList.size() > 0) {
            for (Discount discount : discountList) {
                if (discount.isActiveDiscount()) {
                    tempPrice = discount.calculateDiscountedPrice(sellingPrice);
                    bestPrice = Math.min(tempPrice, bestPrice);
                }
            }
        }
        return bestPrice;
    }

    public void deleteDiscount(String discountID) throws Exception{
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        discountDAO.deleteDiscount(discountID);
        discountDAO.deleteDiscountFromDisCatTable (discountID, CATEGORY_ID);
        if (getDiscountList().size() > 0) {
            for (Discount discount : getDiscountList()) {
                if (discount.getDiscountID().equals(discountID)) {
                    discountList.remove(discount);
                    break;
                }
            }
        }
    }

    public Discount getDiscount(String discountID) {
        if (getDiscountList().size() > 0) {
            for (Discount discount : getDiscountList()) {
                if (discount.getDiscountID().equals(discountID)) {
                    return discount;
                }
            }
        }
        throw new IllegalArgumentException("Discount ID doesnt exist.");
    }

}
