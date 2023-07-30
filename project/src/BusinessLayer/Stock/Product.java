package BusinessLayer.Stock;
//import jdk.jshell.spi.ExecutionControl;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import BusinessLayer.Stock.Discount.DiscountType;
import BusinessLayer.Stock.Item.DamageType;
import PersistenceLayer.DAO.Stock.DiscountInStoreDAO;
import PersistenceLayer.DAO.Stock.ItemDAO;
import PersistenceLayer.DAO.Stock.ProductDAO;
import PersistenceLayer.DTO.Stock.DiscountInStoreDTO;
import PersistenceLayer.DTO.Stock.ItemDTO;
import PersistenceLayer.DTO.Stock.ProductDTO;

public class Product {

    private final String PRODUCT_ID;
    private String name;
    private int storeAmount;
    private int warehouseAmount;
    private int totalAmount;
    private int damagedAmount;
    private int notificationAmount;
    private double purchasePrice;
    private double sellingPrice;
    private String manufacture;
    private int demand;
    private int supplyTime;
    private boolean hasDefInProcess;
    private List<String> categoryIDChain;
    private int itemCounter;
    private Map<String, Item> itemList;
    private Map<Item.DamageType, List<String>> damagedList;
    private Map<Date, List<String>> expirationList;
    private Map<Item.DamageType, List<Discount>> discountList;
    private ProductDAO productDAO;
    private ItemDAO itemDAO = new ItemDAO();
    private DiscountInStoreDAO discountDAO = new DiscountInStoreDAO();

    public Product(String productID, String name, double purchasePrice, double sellingPrice, String manufacture, int demand, int supplyTime, int initialNotificationAmount, List<String> categoryIDs, ProductDAO pdao){
        this.PRODUCT_ID = productID;
        this.name= name;
        this.storeAmount = 0;
        this.warehouseAmount = 0;
        this.totalAmount = 0;
        this.damagedAmount = 0;
        this.purchasePrice = purchasePrice;
        this.sellingPrice =  sellingPrice;
        this.manufacture = manufacture;
        this.demand = demand; //???
        this.supplyTime = supplyTime; //???
        this.notificationAmount = initialNotificationAmount;
        this.hasDefInProcess = false;
        this.categoryIDChain = categoryIDs;
        this.itemList = new HashMap<>();
        this.damagedList = new HashMap<>();
        this.expirationList = new HashMap<>();
        this.discountList = new HashMap<>();
        this.productDAO = pdao;
        this.itemCounter = 1;
    }

    public Product(ProductDTO pdto, List<String> categories, ProductDAO productDAO) throws SQLException{
        this.PRODUCT_ID = pdto.getProductID();
        this.name= pdto.getName();
        this.storeAmount = pdto.getStoreAmount();
        this.warehouseAmount = pdto.getWarehouseAmount();
        this.totalAmount = pdto.getTotalAmount();
        this.damagedAmount = pdto.getDamagedAmount();
        this.purchasePrice = pdto.getPurchasePrice();
        this.sellingPrice =  pdto.getSellingPrice();
        this.manufacture = pdto.getManufacturer();
        this.demand = pdto.getDemand();
        this.supplyTime = pdto.getSupplyTime();
        this.notificationAmount = pdto.getNotificationAmount();
        this.categoryIDChain = categories;
        this.itemList = new HashMap<>();
        this.damagedList = new HashMap<>();
        this.expirationList = new HashMap<>();
        this.discountList = new HashMap<>();
        this.productDAO = productDAO;
        if(pdto.getHasDefInProcess() == 0){
            this.hasDefInProcess = false;
        }
        else{
            this.hasDefInProcess = true;
        }
        this.itemCounter = pdto.getItemCounter();
        try {
            for (ItemDTO idto : itemDAO.getItems(getProductID())) {
                Item i = new Item(idto, itemDAO);
                itemList.put(idto.getITEM_ID(), i);
                if(!damagedList.containsKey(i.getDamageType()))
                    damagedList.put(i.getDamageType(), new ArrayList<>());
                damagedList.get(i.getDamageType()).add(i.getItemID());
                if(i.getExpirationDate().before(new Date())){
                    i.setDamageType(DamageType.EXPIRED);
                    if(!damagedList.containsKey(DamageType.EXPIRED)){
                        damagedList.put(DamageType.EXPIRED, new ArrayList<>());
                    }
                    damagedList.get(DamageType.EXPIRED).add(i.getItemID());
                }
                if(expirationList.containsKey(i.getExpirationDate())){
                    expirationList.get(i.getExpirationDate()).add(i.getItemID());
                }
                else{
                    expirationList.put(i.getExpirationDate(), new ArrayList<>());
                    expirationList.get(i.getExpirationDate()).add(i.getItemID());
                }
            }

        }
        catch (SQLException ex){
            throw new SQLException("failed to set product lists");
        }
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

    public String getManufacture() {
        return manufacture;
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

    public List<String> getCategoryIDChain() {
        return categoryIDChain;
    }

    public Map<String, Item> getItemList() {
        return itemList;
    }

    public Map<Item.DamageType, List<String>> getDamagedList() {
        return damagedList;
    }

    public Map<Date, List<String>> getExpirationList() {
        return expirationList;
    }

    public Map<Item.DamageType, List<Discount>> getDiscountList() {
        return discountList;
    }

    public boolean isHasDefInProcess() { return hasDefInProcess; }


    public List<Discount> getDiscountListByDamage(Item.DamageType damageType) {
        //Item.DamageType demType = convertStringToEnumDamageType(damageType);
        if (!discountList.containsKey(damageType)) {
            discountList.put(damageType, new ArrayList<>());
        }
        return discountList.get(damageType);
    }

    public void setStoreAmount(int amount) {
        productDAO.updateStoreAmount(amount, getProductID());
        this.storeAmount = amount;
        setTotalAmount();
    }

    public void setWarehouseAmount(int amount) {
        this.productDAO.updateWarehouseAmount(amount, getProductID());
        this.warehouseAmount = amount;
        setTotalAmount();
    }

    //when adding/deleting/doing something with items amount - call this function
    private void setTotalAmount() {
        this.totalAmount = this.storeAmount + this.warehouseAmount;

    }

    //when adding/deleting/doing something with damages - call this function
    private void setDamagedAmount() {
        int amountToSet = 0;
        for (List<String> damageList : damagedList.values()) {
            amountToSet += damageList.size();
        }
        //its green because it was here twice, is there a reason?
        /*for (List<String> damageList : damagedList.values()) {
            amountToSet += damageList.size();
        }*/
        productDAO.updateDamagedAmount(amountToSet, PRODUCT_ID);
        this.damagedAmount = amountToSet;
    }

    public void setPurchasePrice(double price) {
        productDAO.updatePurchasePrice(price, getProductID());
        this.purchasePrice = price;
    }

    public void setSellingPrice(double price) {
        productDAO.updateSellingPrice(price, getProductID());
        this.sellingPrice = price;
    }

    public void setManufacture(String newManufacture) {
        this.manufacture = newManufacture;
    }

    public void setHasDefInProcess(boolean isInProcess) {
        if (isInProcess){
            productDAO.updateIsOrdered(1, getProductID());
        }
        else{
            productDAO.updateIsOrdered(0, getProductID());
        }
        this.hasDefInProcess = isInProcess; }


    public void setDemand(int newDemand) {
        productDAO.updateDemand(newDemand, getProductID());
        this.demand = newDemand;
        //setNotificationAmount();
    }

    public void setSupplyTime(int time) {
        productDAO.updateSupplyTime(time, getProductID());
        this.supplyTime = time;
        setNotificationAmount();
    }

    public void setNotificationAmount() {
        productDAO.updateNotifyAmount(getDemand() * (getSupplyTime() + 1), getProductID());
        this.notificationAmount = getDemand() * (getSupplyTime() + 1);
    }

    private int booleanToInt(boolean bool) {
        if (bool) {
            return 1;
        }
        return 0;
    }

    /**
     * @return A valid itemID to use next
     */
    public String generateItemID(){
        int nextID = itemCounter;
        itemCounter += 1;
        productDAO.updateCounter(itemCounter, getProductID());
        return PRODUCT_ID + "-" + nextID;
    }


    /**
     *
     * @return a list of item objects of this product
     */
    public List<Item> getProductItems() {
        List<Item> toReturn = new ArrayList<>();
        for (Item item : itemList.values()) {
            toReturn.add(item);
        }
        return toReturn;
    }

    /**
     *
     * @param itemID
     * @return Item object by the given ID
     */
    public Item getItemByID(String itemID) {
        return itemList.get(itemID);
    }

    /**
     *
     * @param itemIDs
     * @return a List of Item objects by the given IDs
     */
    public List<Item> getItemListByIDList(List<String> itemIDs) {
        List<Item> toReturn = new ArrayList<>();
        for (String itemID : itemIDs) {
            toReturn.add(getItemByID(itemID));
        }
        return toReturn;
    }

    public Boolean isItemIDExists(String itemID){
        return itemList.containsKey(itemID);
    }


    /**
     *
     * @param damage
     * @return a list of Item objects that has the given damage
     */
    public List<Item> getDamagedItemsByDamage(Item.DamageType damage) {
        if(!damagedList.containsKey(damage)){
            damagedList.put(damage, new ArrayList<>());
        }
        return getItemListByIDList(damagedList.get(damage));
    }

    /**
     *
     * @param date
     * @return a list of Item objects that has the expires on the given date
     */
    public List<Item> getExpiredItemsByDate(Date date) {
        return getItemListByIDList(expirationList.get(date));
    }

    /**
     *
     * @return true if number of items is less then minimun amount
     */
    public boolean isBelowMinimumAmount() {
        if (totalAmount <= notificationAmount) {
            return true;
        }
        return false;
    }

    /**
     * @param storeAmount- the amount of supply going to the store
     * @param warehouseAmount- the amount of supply going to the warehouse
     * @param expirationDate- the expiration date of the current supply
     * updates the amount of items for this product, and creates items (generates id)
     */
    public void receiveSupply(int storeAmount, int warehouseAmount, Date expirationDate)throws SQLException {
        setStoreAmount(this.storeAmount + storeAmount);
        setWarehouseAmount(this.warehouseAmount + warehouseAmount);
        List<String> currentItems = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(expirationDate);
        for(int i = 0; i < storeAmount; i++){
            String id = generateItemID();
            itemDAO.addItem(id, strDate, "STORE", "", purchasePrice, sellingPrice, getProductID());
            Item newItem = new Item(id, expirationDate, Item.Location.STORE, purchasePrice, sellingPrice, itemDAO);
            currentItems.add(id);
            itemList.put(id, newItem);
            if(expirationDate.before(new Date())){
                newItem.setDamageType(DamageType.EXPIRED);
                if(!damagedList.containsKey(DamageType.EXPIRED)){
                    damagedList.put(DamageType.EXPIRED, new ArrayList<>());
                }
                damagedList.get(DamageType.EXPIRED).add(id);
                setDamagedAmount();
            }
        }

        for(int i = 0; i < warehouseAmount; i++){
            String id = generateItemID();
            itemDAO.addItem(id, strDate, "WAREHOUSE", "", purchasePrice, sellingPrice, getProductID());
            Item newItem = new Item(id, expirationDate, Item.Location.WAREHOUSE, purchasePrice, sellingPrice, itemDAO);
            currentItems.add(id);
            itemList.put(id, newItem);
            if(expirationDate.before(new Date())){
                newItem.setDamageType(DamageType.EXPIRED);
                if(!damagedList.containsKey(DamageType.EXPIRED)){
                    damagedList.put(DamageType.EXPIRED, new ArrayList<>());
                }
                damagedList.get(DamageType.EXPIRED).add(id);
                setDamagedAmount();
            }
        }

        if(expirationList.containsKey(expirationDate)){
            expirationList.get(expirationDate).addAll(currentItems);
        }
        else{
            expirationList.put(expirationDate, currentItems);
        }
        setHasDefInProcess(false);
    }


    /**
     * @param itemID
     * move item from warehouse to store, and opposite
     */
    public void moveItem(String itemID){
        if(!itemList.containsKey(itemID))
            throw new NullPointerException("Item doesn't exist in Product list.");
        itemList.get(itemID).moveLocation();
    }


    /**
     * @param itemID
     * @return List with 2 cells: cell 0- 1 if the item is demaged, 0 otherwise. cell 1- the price of the item
     * remove item from store
     */
    public Item removeItem(String itemID) throws Exception{
        if(!itemList.containsKey(itemID)) {
            throw new NoSuchElementException("Item not found");
        }
        itemDAO.deleteItem(itemID);
        //itemDAO.deleteItemFromProdDamTable(itemID);
        Item removedItem = itemList.remove(itemID);
        //decrease amounts
        switch (removedItem.getLocation()){
            case STORE :
                setStoreAmount(getStoreAmount() - 1);
                break;
            case WAREHOUSE:
                setWarehouseAmount(getWarehouseAmount() - 1);
        }
        //increase demand (updates the notification amount)
        setDemand(getDemand() + 1);
        //preparing the list to return
        if(removedItem.getIsDamaged()) {
            damagedList.get(removedItem.getDamageType()).remove(itemID);
            //If we want to delete list completely - uncomment. else - erase
            /*
            if(damagedList.get(removedItem.getDamageType()).size() == 0)
                damagedList.remove(removedItem.getDamageType());
             */
            setDamagedAmount();
        }
        expirationList.get(removedItem.getExpirationDate()).remove(itemID);
        if(expirationList.get(removedItem.getExpirationDate()).size() == 0)
            expirationList.remove(removedItem.getExpirationDate());
        return removedItem;
    }


    /**
     * Adds a general product discount - DamageType = NONE
     * @param discountID
     * @param sDate
     * @param eDate
     * @param value
     * @param isPercent
     * @param disType
     */
    public void addProductDiscount(String discountID, Date sDate, Date eDate, double value, boolean isPercent, DiscountType disType) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        if (!discountList.containsKey(Item.DamageType.NONE)) {
            discountList.put(Item.DamageType.NONE, new ArrayList<>());
        }
        discountDAO.addDiscount(discountID, dateFormat.format(sDate), dateFormat.format(eDate), value, booleanToInt(isPercent), disType.toString());
        discountDAO.addDiscountToDisProdDamTable(discountID, PRODUCT_ID, "NONE");
        discountList.get(Item.DamageType.NONE).add(new Discount(discountID, sDate, eDate, value, isPercent, disType, discountDAO));
    }


    /**
     * Adds a discount for damaged items only by DamageType
     * @param discountID
     * @param sDate
     * @param eDate
     * @param value
     * @param isPercent
     * @param disType
     * @param damType
     */
    public void addDamagedDiscount(String discountID, Date sDate, Date eDate, double value, boolean isPercent, DiscountType disType, DamageType damType) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        if (!discountList.containsKey(damType)) {
            discountList.put(damType, new ArrayList<>());
        }
        discountDAO.addDiscount(discountID, dateFormat.format(sDate), dateFormat.format(eDate), value, booleanToInt(isPercent), disType.toString());
        discountDAO.addDiscountToDisProdDamTable (discountID, PRODUCT_ID, damType.toString());
        discountList.get(damType).add(new Discount(discountID, sDate, eDate, value, isPercent, disType, discountDAO));
    }

    public double getAndUpdateItemCalculatedPrice(String itemID, double calculatedPrice) throws Exception {
        Item toUpdate = itemList.get(itemID);
        double bestPrice = calculatedPrice;
        if (toUpdate.getIsDamaged()) {
            Item.DamageType damage = toUpdate.getDamageType();
            bestPrice = getBestCalculatedByDamagePrice(damage, calculatedPrice); //to unite?
        }
        toUpdate.setDiscountPrice(bestPrice);
        return bestPrice;
    }


    /**
     * calls correctDiscountList and then calculates the price
     * @param damage
     * @param calculatedPrice
     * @return the best price for this product, after activating the best discout for this damage(or not) type
     */
    private double getBestCalculatedByDamagePrice(DamageType damage, double calculatedPrice) throws Exception{
        double bestPrice = calculatedPrice;
        correctDiscountList(damage);
        double tempPrice;
        if(discountList.containsKey(damage)){ // added by Amit
            for (Discount discount : discountList.get(damage)) {
                if (discount.isActiveDiscount()) {
                    tempPrice = discount.calculateDiscountedPrice(getSellingPrice());
                    bestPrice = Math.min(tempPrice, bestPrice);
                }
            }
        }
        return bestPrice;
    }

    /**
     * deletes all the invalid discounts (their endDate has passed) from the given damageType discountList
     */
    public void correctDiscountList(Item.DamageType damageType) throws Exception{
        if ((!discountList.containsKey(damageType)) || (discountList.containsKey(damageType) && discountList.get(damageType).size() == 0)) {
            loadProductDamageDiscounts(damageType);
        }
        if (discountList.get(damageType).size() != 0) {
            for (Discount discount : discountList.get(damageType)) {
                if (discount.isOver()) {
                    discountList.get(damageType).remove(discount);
                }
            }
        }
    }

    /**
     * 
     * @param calculatedPrice
     * @return the product's calculated price before activating DAMAGED discounts
     */
    public double getProductCalculatedPrice(double calculatedPrice) throws Exception{
        return getBestCalculatedByDamagePrice(Item.DamageType.NONE,  calculatedPrice);
    }


    public void setItemAsDamaged(String itemID, Item.DamageType damageType){
        if(!itemList.keySet().contains(itemID)){
            throw new IllegalArgumentException("Item id doesn't exist in the system");
        }
        Item item = itemList.get(itemID);
        item.setIsDamaged(true);
        item.setDamageType(damageType);
        if(!damagedList.containsKey(damageType)){
            damagedList.put(damageType, new ArrayList<>());
        }
        damagedList.get(damageType).add(itemID);
        setDamagedAmount();
        //Replaced all following lines in the above using the "convertToEnum..." method
        //delete if works properly:
        /* 
        if(damage.equals("BROKEN")){
            item.setIsDamaged(true);
            item.setDamageType(Item.DamageType.BROKEN);
            if(!damagedList.containsKey(Item.DamageType.BROKEN)){
                damagedList.put(Item.DamageType.BROKEN, new ArrayList<>());
            }
            damagedList.get(Item.DamageType.BROKEN).add(itemID);
            setDamagedAmount();
        }
        else{
            if(damage.equals("EXPIRED")){
                item.setIsDamaged(true);
                item.setDamageType(Item.DamageType.EXPIRED);
                if(!damagedList.containsKey(Item.DamageType.EXPIRED)){
                    damagedList.put(Item.DamageType.EXPIRED, new ArrayList<>());
                }
                damagedList.get(Item.DamageType.EXPIRED).add(itemID);
                setDamagedAmount();
            }
            else{
                if(damage.equals("SCRATCH")){
                    item.setIsDamaged(true);
                    item.setDamageType(Item.DamageType.SCRATCH);
                    if(!damagedList.containsKey(Item.DamageType.SCRATCH)){
                        damagedList.put(Item.DamageType.SCRATCH, new ArrayList<>());
                    }
                    damagedList.get(Item.DamageType.SCRATCH).add(itemID);
                    setDamagedAmount();
                }
                else{
                    if(damage.equals("OTHER")){
                        item.setIsDamaged(true);
                        item.setDamageType(Item.DamageType.OTHER);
                        if(!damagedList.containsKey(Item.DamageType.OTHER)){
                            damagedList.put(Item.DamageType.OTHER, new ArrayList<>());
                        }
                        damagedList.get(Item.DamageType.OTHER).add(itemID);
                        setDamagedAmount();
                    }
                }
            }
        }
        */
    }

    public Map<String, String> getDamagedDiscountIDMap(Item.DamageType damageType) throws Exception{
        Map<String,String> toReturn = new HashMap<>();
        if (getDiscountListByDamage(damageType).size() == 0) {
            loadProductDamageDiscounts(damageType);
        }
        if (getDiscountListByDamage(damageType).size() != 0) {
            for (Discount discount : getDiscountListByDamage(damageType)) {
                toReturn.put(discount.getDiscountID(), discount.toString());
            }
        }
        return toReturn;
    }

    private void loadProductDamageDiscounts(DamageType damageType) throws Exception{
        try {
            if (!discountList.containsKey(damageType)) {
                discountList.put(damageType, new ArrayList<>());
            }
            for (DiscountInStoreDTO discountDTO : discountDAO.getProductDamageDiscounts(getProductID(), damageType.toString())) {
                Discount d = new Discount(discountDTO, discountDAO);
                discountList.get(damageType).add(d);
            }
        }
        catch (Exception ex){
            throw new Exception("failed to set discounts list for damageType:" + damageType.toString());
        }
    }


    public void deleteDiscount(String discountID, Item.DamageType damageType) throws Exception{
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            discountDAO.deleteDiscount(discountID);
            discountDAO.deleteDiscountFromDisProdDamTable(discountID, getProductID(), damageType.toString());
            if (getDiscountListByDamage(damageType).size() > 0) {
                for (Discount discount : getDiscountListByDamage(damageType)) {
                    if (discount.getDiscountID().equals(discountID)) {
                        discountList.remove(discount);
                    }
                }
            }
        }
        catch (Exception ex){
            throw new Exception("failed to delete discount from product");
        }
    }

    public Discount getDiscount(Item.DamageType damageType, String discountID) {
        for (Discount discount: getDiscountListByDamage(damageType)) {
            if (discount.getDiscountID().equals(discountID)) {
                return discount;
            }
        }
        throw new IllegalArgumentException("Discount ID doesnt exist.");
    }

    public boolean shouldAddToDeficiencyOrder() {
        return (!isHasDefInProcess() && isBelowMinimumAmount());
    }

    public int calculateAmountToOrder() {
        return notificationAmount;
        // To change according to desired calculation formula
    }

    public int addToDeficiencyOrderAndGetAmount() {
        setHasDefInProcess(true);
        return calculateAmountToOrder();
    }
}