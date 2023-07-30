package BusinessLayer.Suppliers;

import BusinessLayer.Stock.Discount;
import BusinessLayer.Stock.Product;
import BusinessLayer.Tools.Pair;
import PersistenceLayer.DAO.Suppliers.*;
import PersistenceLayer.DTO.Suppliers.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Supplier {

    private String name;
    private int supplierId;
    private SupplierCard sCard;
    private SupplierDTO supplierDTO;
    private boolean canDeliver;
    //private boolean workOnSpecificDays;
    private boolean[] daysCanSupply;
    private String address;


    // each product of the supplier by his serial number
    private HashMap<String, Integer> productsByCatalogNum;

    // hashmap from product to his original price before discounts.
    private HashMap<String, Double> productsPrice;

    // the key is Product , the value is array of the product's discounts ,
    private HashMap<String, List<DiscountSuppliers>> productsDiscount;

    // the key is product , the value is the maximum quantity of this product
    private HashMap<String, Integer> productsQuantities;

    private List<DiscountSuppliers> totalDiscountSuppliers;

    private HashMap<Integer, Order> orders;

    // pair<orderId, branchId> , list day of week
    private HashMap<Pair<Integer, Integer>, List<Integer>> constOrderReservoir;

    // pair<orderId, branchId> , Hashmap <productId , productName > , amount>
    private HashMap<Pair<Integer, Integer>, HashMap<Pair<String, String>, Integer>> constOrderDetails;


    private SupplierCardMapper supplierCardMapper;
    private ProductQuantitiesAndPriceMapper productQuantitiesAndPriceMapper;
    private DiscountSupplierMapper discountSupplierMapper;
    private SupplierProductDiscountsMapper supplierProductDiscountsMapper;
    private OrderMapper orderMapper;
    private OrderConstMapper orderConstMapper;
    private ProductInOrderMapper productInOrderMapper;
    //private SupplierContactMapper supplierContactMapper;


    public Supplier(int supplierId, SupplierCard sc, boolean canDeliver, String daysCanSupplyString, String name, String address, SupplierMapper supplierMapper) {
        this.name = name;
        this.supplierId = supplierId;
        this.sCard = sc;
        this.canDeliver = canDeliver;
        this.address = address;
        this.daysCanSupply = daysToArray(daysCanSupplyString);
        //this.workOnSpecificDays = daysCanSupplyString.length() > 0;
        this.supplierProductDiscountsMapper = new SupplierProductDiscountsMapper();
        this.orders = new HashMap<>();
        this.productsByCatalogNum = new HashMap<>();
        this.productsPrice = new HashMap<>();
        this.productsDiscount = new HashMap<>();
        this.productsQuantities = new HashMap<>();
        this.totalDiscountSuppliers = new ArrayList<>();
        this.supplierCardMapper = new SupplierCardMapper();
        this.orderMapper = new OrderMapper();
        this.productQuantitiesAndPriceMapper = new ProductQuantitiesAndPriceMapper();
        this.productInOrderMapper = new ProductInOrderMapper();
        //HashMap<Product, Integer> hashMap = new HashMap<Product, Integer>();
        this.constOrderDetails = new HashMap<>();
        this.constOrderReservoir = new HashMap<>();


        int canDeliverInt = 0;
        if (canDeliver) {
            canDeliverInt = 1;
        }
        this.supplierDTO = new SupplierDTO(name, supplierId, canDeliverInt, daysCanSupplyString, address, supplierMapper);
        supplierMapper.addSupplier(supplierDTO);

    }

    public Supplier(int supplierId, int bnNumber, int bankAccount, PaymentOptions paymentOptions, int contactId, String contactName, String phoneNumber, boolean canDeliver, String daysCanSupply, String name, String address, SupplierMapper supplierMapper) {
        this.name = name;
        this.supplierId = supplierId;
        this.supplierCardMapper = new SupplierCardMapper();
        this.productInOrderMapper = new ProductInOrderMapper();
        this.sCard = new SupplierCard(bnNumber, bankAccount, paymentOptions, contactName, phoneNumber, supplierCardMapper, supplierId);
        this.canDeliver = canDeliver;
        this.address = address;
        this.daysCanSupply = daysToArray(daysCanSupply);
        //this.workOnSpecificDays = daysCanSupply.length() > 0;
        this.orders = new HashMap<>();
        this.productsByCatalogNum = new HashMap<>();
        this.discountSupplierMapper = new DiscountSupplierMapper();
        this.productsPrice = new HashMap<>();
        this.productsDiscount = new HashMap<>();
        this.productsQuantities = new HashMap<>();
        this.totalDiscountSuppliers = new ArrayList<>();
        this.supplierCardMapper = new SupplierCardMapper();
        this.orderMapper = new OrderMapper();
        this.orderConstMapper = new OrderConstMapper();
        this.productQuantitiesAndPriceMapper = new ProductQuantitiesAndPriceMapper();
        this.productInOrderMapper = new ProductInOrderMapper();
        //this.supplierContactMapper = new SupplierContactMapper();
        this.constOrderReservoir = new HashMap<>();
        this.constOrderDetails = new HashMap<>();
        this.supplierProductDiscountsMapper = new SupplierProductDiscountsMapper();

        int canDeliverInt = 0;
        if (canDeliver) {
            canDeliverInt = 1;
        }
        this.supplierDTO = new SupplierDTO(name, supplierId, canDeliverInt, daysCanSupply, address, supplierMapper);
        supplierMapper.addSupplier(supplierDTO);


    }



    public Supplier(SupplierDTO supplierDTO) {
        this.supplierId = supplierDTO.getId();
        this.supplierCardMapper = new SupplierCardMapper();
        this.name = supplierDTO.getName();
        this.daysCanSupply = daysToArray(supplierDTO.getDays());
        this.address = supplierDTO.getAddress();
        boolean canDeliverBool = true;
        if (supplierDTO.getCanDeliver() == 0) {
            canDeliverBool = false;
        }
        this.supplierCardMapper = new SupplierCardMapper();
        this.canDeliver = canDeliverBool;
        this.sCard = new SupplierCard(supplierCardMapper.selectSupplierCard(supplierId));
        this.productsByCatalogNum = new HashMap<>();
        this.productsPrice = new HashMap<>();
        this.productsDiscount = new HashMap<>();
        this.productsQuantities = new HashMap<>();
        this.totalDiscountSuppliers = new ArrayList<>();
        this.discountSupplierMapper = new DiscountSupplierMapper();
        this.productQuantitiesAndPriceMapper = new ProductQuantitiesAndPriceMapper();
        this.supplierProductDiscountsMapper = new SupplierProductDiscountsMapper();
        //this.supplierContactMapper = new SupplierContactMapper();
        this.productInOrderMapper = new ProductInOrderMapper();
        this.orderConstMapper = new OrderConstMapper();
        this.constOrderReservoir = new HashMap<>();
        this.constOrderDetails = new HashMap<>();
        loadSupplierFromDAL();
        this.supplierDTO = supplierDTO;
        this.orderMapper = new OrderMapper();
        this.orders = new HashMap<>();

    }

    private void loadSupplierFromDAL() {

        List<ProductQuantitiesAndPriceDTO> productQuantitiesAndPriceDTOSList = productQuantitiesAndPriceMapper.getProductQuantitiesAndPriceDTO(supplierId);
        for (ProductQuantitiesAndPriceDTO productQuantitiesAndPriceDTO : productQuantitiesAndPriceDTOSList) {
            productsPrice.put(productQuantitiesAndPriceDTO.getProductId(), productQuantitiesAndPriceDTO.getPrice());
            productsQuantities.put(productQuantitiesAndPriceDTO.getProductId(), productQuantitiesAndPriceDTO.getAmountCanSupply());
            productsByCatalogNum.put(productQuantitiesAndPriceDTO.getProductId(), productQuantitiesAndPriceDTO.getCatalogNumber());
        }

        List<DiscountSupplierDTO> ordersDiscountList = discountSupplierMapper.getDiscountsListDTO(supplierId);
        if (ordersDiscountList != null) {
            for (DiscountSupplierDTO discountSupplierDTO : ordersDiscountList) {
                if (discountSupplierDTO != null) {
                    totalDiscountSuppliers.add(new DiscountSuppliersPerOrder(discountSupplierDTO));
                }

                    //ArrayList<DiscountSuppliers> discountSuppliersPerProductArrayList = new ArrayList<>();



            }
        }
        List<SupplierProductDiscountsDTO> productDiscountList = supplierProductDiscountsMapper.getSupplierDiscountsDTOs(supplierId);
        if (productDiscountList != null) {
            for (SupplierProductDiscountsDTO discountProductSupplierDTO : productDiscountList) {
                if (discountProductSupplierDTO != null) {
                    if (productsDiscount.containsKey(discountProductSupplierDTO.getProductId())) {
                        productsDiscount.get(discountProductSupplierDTO.getProductId()).add(new DiscountSuppliersPerProduct(discountProductSupplierDTO));
                    }
                    else {
                        ArrayList<DiscountSuppliers> discountSuppliersPerProductArrayList = new ArrayList<>();
                        discountSuppliersPerProductArrayList.add((new DiscountSuppliersPerProduct(discountProductSupplierDTO)));
                        productsDiscount.put(discountProductSupplierDTO.getProductId(),  discountSuppliersPerProductArrayList);

                    }
                }
            }
        }

    }


    public boolean supplyThisDays(List<Integer> days) {
        for (Integer dayInt : days) {
            if (!daysCanSupply[dayInt]) {
                return false;
            }
        }
        return true;
    }

    public String daysToString(List<Integer> days) {
        String toReturn = "";
        for (int day : days) {
            toReturn = toReturn + day;
        }
        return toReturn;
    }

    public boolean[] daysToArray(String daysString) {
        boolean[] daysArray = new boolean[7];
        for (int i = 0; i < 7; i++) {
            daysArray[i] = false;
        }
        for (int i = 0; i < daysString.length(); i++) {

            daysArray[Integer.parseInt(String.valueOf(daysString.charAt(i))) ] = true;
        }
        return daysArray;
    }

    public boolean isWorkOnSpecificDays() {
        return daysToString().length() > 0;
    }

    public String daysToString() {
        String toReturn = "";
        for (int i = 0; i < 7; i++) {
            if (daysCanSupply[i]) {
                toReturn = toReturn + i + 1;
            }
        }
        return toReturn;
    }


    public boolean isCanDeliver() {
        return canDeliver;
    }

    public void addOrder(Order order) {
        orders.put(order.getOrderId(), order);
    }

    public Order addShortageOrder(int branchId, String address, int orderId, Map<Pair<String, String>, Integer> products) {
        ArrayList<ProductInOrder> productInOrderArrayList = new ArrayList<>();
        for (Pair<String, String> productIdName : products.keySet()) {
            String productId = productIdName.getFirst();
            int amount = products.get(productIdName);
            productInOrderArrayList.add(new ProductInOrder(productIdName.getFirst(), branchId, orderId, productsByCatalogNum.get(productId), productIdName.getSecond(), amount, getPriceForProductAndAmount(productId, amount), getPriceBeforeDiscount(productId, amount), productInOrderMapper));
        }
        Order order = new Order(this.getSupplierId(), this.getName(), branchId, address, orderId, orderMapper, productInOrderArrayList);
        orders.put(order.orderId, order);
        return order;
    }

    public OrderConst addConstOrder(int branchId, String address, int orderId, Map<Pair<String, String>, Integer> products, List<Integer> days) {
        ArrayList<ProductInOrder> productInOrderArrayList = new ArrayList<>();

        for (Pair<String, String> product1 : products.keySet()) {
            int amount = products.get(product1);
            productInOrderArrayList.add(new ProductInOrder(product1.getFirst(), branchId, orderId, productsByCatalogNum.get(product1.getFirst()), product1.getSecond(), amount, getPriceForProductAndAmount(product1.getFirst(), amount), getPriceBeforeDiscount(product1.getFirst(), amount), this.productInOrderMapper));
        }
        OrderConst orderConst = new OrderConst(this.getSupplierId(), this.getName(), branchId, address, orderId, productInOrderArrayList, orderConstMapper, days);
        orders.put(orderConst.getOrderId(), orderConst);
        return orderConst;
    }


    public void addProductToSupply(String supplierProduct, int catalogNum, int amountToSupply, double price) {
        if (containProduct(supplierProduct)) {
            throw new IllegalArgumentException("this product is already in the supplier stocks");
        }
        for (String productId : productsByCatalogNum.keySet()) {
            if (productsByCatalogNum.get(productId) == catalogNum) {
                throw new IllegalArgumentException("catalogNum is already assigned to another product");
            }
        }
        if (amountToSupply <= 0) {
            throw new IllegalArgumentException("the supplier must supply positive amount");
        }
        if (price < 0) {
            throw new IllegalArgumentException("price must be positive");
        }
        ProductQuantitiesAndPriceDTO productQuantitiesAndPriceDTO1 = new ProductQuantitiesAndPriceDTO(supplierId, supplierProduct, amountToSupply, price, catalogNum);
        productQuantitiesAndPriceMapper.addProductToSupply(productQuantitiesAndPriceDTO1);
        productsByCatalogNum.put(supplierProduct, catalogNum);
        productsQuantities.put(supplierProduct, amountToSupply);
        productsPrice.put(supplierProduct, price);
        productsDiscount.put(supplierProduct, new ArrayList<DiscountSuppliers>());
    }


    public void addContact(String name, String phoneNumber) {
        sCard.addContact( name, phoneNumber, supplierId);
    }


   /* public void addContact(String name , String phoneNumber){
        //int id = sCard.getContactIdCounter();
        sCard.addContact(id, name ,phoneNumber,supplierId);
*/

    public void printContacts() {
        sCard.printContacts(supplierId);
    }



    public void removeContact(int contactId) {
        sCard.removeContact(contactId,supplierId);
    }



    public void setName(String name) {
        supplierDTO.getDao().updateName(name, supplierId);
        supplierDTO.setName(name);
        this.name = name;
    }

    public void setPaymentTerms(PaymentOptions paymentOptions) {
        this.sCard.setPaymentOptions(paymentOptions);
    }

    public void setBNNumber(int bNnumber) {
        sCard.setBNNumber(bNnumber);
    }

    public void setBankAccount(int bankAccount) {
        sCard.setBankAccountNumber(bankAccount);
    }

    public int getSupplierId() {
        return supplierId;
    }

    public SupplierCard getsCard() {
        return sCard;
    }

    public String getName() {
        return name;
    }

    //public DeliveryTerms getDeliveryTerms() {
    //    return deliveryTerms;
    //}


    public String getAddress() {
        return address;
    }

    public boolean[] getDaysCanSupply() {
        return daysCanSupply;
    }

    public List<DiscountSuppliers> getTotalDiscounts() {
        return totalDiscountSuppliers;
    }

    public int findNearestDayCanSupply(int day) {
        if (!isWorkOnSpecificDays()) {
            if (canDeliver) {
                return 1;
            } else
                return 3;
        }
        for (int i = 1; i < 8; i++) {
            if (daysCanSupply[(i + day) % 7]) {
                return (i + day) % 7;
            }
        }
        return -1;
    }

    public void addDayToDeliver(int day) {
        if (!isWorkOnSpecificDays()) {
            throw new IllegalArgumentException("only suppliers that works on specific days can add days to deliver");
        }
        if (day < 0 || day > 7) {
            throw new IllegalArgumentException("not a valid day;");
        }
        daysCanSupply[day] = true;
    }


    public boolean isSupplyAllTheProducts(Map<String, Integer> products) {
        boolean ans = true;
        for (String supplierProduct : products.keySet()) {
            ans = ans & hasEnoughOfTheProduct(supplierProduct, products.get(supplierProduct));
        }
        return ans;
    }

    public double getPriceForAllOrder(Map<String, Integer> productsAndAmount) {
        double totalOrder = 0;
        for (String supplierProduct : productsAndAmount.keySet()) {
            int amount = productsAndAmount.get(supplierProduct);
            totalOrder = totalOrder + (getPriceForProductAndAmount(supplierProduct, amount) * amount);
        }
        return totalOrder - getHighestOrderDiscount(totalOrder);
    }

    ;

    private double getBiggestDiscount(double price, DiscountSuppliers discountSuppliers) {

        if (!discountSuppliers.getIsPercentage()) {
            return discountSuppliers.getDiscount();
        } else {
            return price * (discountSuppliers.getDiscount() / 100);
        }
    }

    public List<String> getNumOfProductsCanSupply(Map<String, Integer> productsToSupply) {
        List<String> canSupply = new ArrayList<>();
        for (String supplierProduct : productsToSupply.keySet()) {
            if (hasEnoughOfTheProduct(supplierProduct, productsToSupply.get(supplierProduct))) {
                canSupply.add(supplierProduct);
            }
        }
        return canSupply;
    }

    public double getHighestOrderDiscount(double totalPrice) {
        DiscountSuppliers discountSuppliers = null;
        boolean found = false;
        for (int i = 0; i < totalDiscountSuppliers.size() && !found; i++) {
            if (totalDiscountSuppliers.get(i).getAmount() < totalPrice) {
                discountSuppliers = totalDiscountSuppliers.get(i);
                found =false;
            }
        }
        if (discountSuppliers == null) {
            return 0.0;
        } else {
            return getBiggestDiscount(totalPrice, discountSuppliers);
        }
    }

    public void removeSupplier() {
        sCard.removeSupplier(supplierId);
        supplierCardMapper.removeSupplierCard(supplierId);
        supplierProductDiscountsMapper.removeSupplierDetails(supplierId);
        discountSupplierMapper.removeSupplierDetails(supplierId);
        productQuantitiesAndPriceMapper.removeSupplierDetails(supplierId);
        sCard = null;


    }


    public void removeProduct(String productId) {

        if (!containsProduct(productId)) {
            throw new IllegalArgumentException("can't remove a product that the supplier doesn't has");
        }
        productQuantitiesAndPriceMapper.removeProduct(productId, supplierId);
        supplierProductDiscountsMapper.removeProductDiscounts(productId, supplierId);
        productsByCatalogNum.remove(productId);
        productsPrice.remove(productId);
        productsDiscount.remove(productId);
        productsQuantities.remove(productId);
    }

    public boolean containsProduct(String productId) {
        if (productsQuantities.containsKey(productId)) {
            return true;
        }
        if (productQuantitiesAndPriceMapper.selectProductBySupplier(supplierId, productId) == null) {
            return false;
        }
        return true;
    }


    public void updateProductPrice(String ProductId, double newPrice) {
        if (!productsByCatalogNum.containsKey(ProductId)) {
            throw new IllegalArgumentException("can't update a price to a product that the supplier doesn't has");
        }
        productQuantitiesAndPriceMapper.updateProductPrice(supplierId, ProductId, newPrice);
        productsPrice.remove(ProductId);
        productsPrice.put(ProductId, newPrice);
    }

    public void addDiscountToProduct(String productId, boolean percentageDiscount, double amount, double discount, int discountId) {
        if (!productsByCatalogNum.containsKey(productId)) {
            throw new IllegalArgumentException("can't add a discount to a product that the supplier doesn't has");
        }
        productsDiscount.get(productId).add(new DiscountSuppliersPerProduct(percentageDiscount, amount, discount, discountId, supplierId, productId, supplierProductDiscountsMapper));
        productsDiscount.get(productId).sort(new DiscountComparator());
    }

    public void removeDiscountToProduct(String productId, int discountId) {
        if (!containProduct(productId)) {
            throw new IllegalArgumentException("can't remove a discount from a product that the supplier doesn't has");
        }
        supplierProductDiscountsMapper.removeProductDiscount(productId, supplierId, discountId);
        DiscountSuppliers toRemove = null;
        List<DiscountSuppliers> discountList = productsDiscount.get(productId);
        for (DiscountSuppliers discountSuppliers : discountList) {
            if (discountSuppliers.getDiscountId() == discountId) {
                toRemove = discountSuppliers;
            }
        }
        if (toRemove != null) {
            discountList.remove(toRemove);
        }
    }

    public void removeDiscountToSupplier(int discountId) throws Exception {

        discountSupplierMapper.removeDiscount(discountId);
        DiscountSuppliers toRemove = null;
        for (DiscountSuppliers discountSuppliers : totalDiscountSuppliers) {
            if (discountSuppliers.getDiscountId() == discountId) {
                toRemove = discountSuppliers;
            }
        }
        if (toRemove != null) {
            totalDiscountSuppliers.remove(toRemove);
        }
    }

    public boolean containProduct(String productId) {
        if (productsByCatalogNum.containsKey(productId)) {
            return true;
        }
        if (productQuantitiesAndPriceMapper.selectProductBySupplier(supplierId, productId) == null) {
            return false;
        }
        return true;
    }


    public Contacts getOneContacts() {
        if (sCard.getContacts().size() > 0) {
            return sCard.getContacts().get(0);
        }
        throw new IllegalArgumentException("no contacts for this supplier");
    }


    private double getPricePerProductAfterDiscount(DiscountSuppliers discountSuppliers, String supplierProduct, int amount) {
        return discountSuppliers.getPriceAfterDiscount(supplierProduct, productsPrice.get(supplierProduct));
    }


    public double getPriceForProductAndAmount(String supplierProduct, int amount) {
        List<DiscountSuppliers> discountSuppliers = productsDiscount.get(supplierProduct);
        if (discountSuppliers==null){
            discountSuppliers = new ArrayList<>();
        }
        int biggestDiscount = -1;
        for (int i = 0; i < discountSuppliers.size(); i++) {
            if (discountSuppliers.get(i) != null) {
                if (amount >= discountSuppliers.get(i).getAmount()) {
                    biggestDiscount = i;
                }
            }
        }
        if (biggestDiscount == -1) {
            return productsPrice.get(supplierProduct);
        }
        return getPricePerProductAfterDiscount(discountSuppliers.get(biggestDiscount), supplierProduct, amount);

    }

    public void constOrderDetailsRemove(Pair<Integer, Integer> orderIdBranchId) {
        constOrderDetails.remove(orderIdBranchId);
    }

    public boolean isSupplyTheProduct(Product supplierProduct) {
        if (productsQuantities.containsKey(supplierProduct.getProductID())) {
            return true;
        }
        return false;
    }


    public boolean isSupplyTheProduct(String productId) {
        if (productsQuantities.containsKey(productId)) {
            return true;
        }
        return false;
    }

    public String getDiscounts(){
        String toReturn = "";
        for (DiscountSuppliers discount: totalDiscountSuppliers){
            toReturn = toReturn + discount.toPrintStyle();
        }
        return toReturn;
    }

    public String getDiscountsProduct(String productID) {
        String toReturn = "";
        List<DiscountSuppliers> discountSuppliers = productsDiscount.get(productID);
        for (DiscountSuppliers discountSuppliers1 : discountSuppliers) {
            toReturn = toReturn + discountSuppliers1.toPrintStyle();
        }
        return toReturn;
    }


    public int numOfOrders() {
        selectAllSupplierOrders();
        return orders.keySet().size();
    }

    public double getPriceBeforeDiscount(String supplierProduct, int amount) {
        return productsPrice.get(supplierProduct) * amount;
    }

    public boolean hasEnoughOfTheProduct(String supplierProduct, int amount) {
        if (!productsQuantities.containsKey(supplierProduct)) {
            return false;
        }
        return productsQuantities.get(supplierProduct) >= amount;
    }

    public int getMaxAmountPerProduct(String supplierProduct) {
        int maxAmount = productsQuantities.get(supplierProduct);
        return maxAmount;
    }

    public HashMap<String, Integer> getProductsByCatalogNum() {
        return productsByCatalogNum;
    }

    public HashMap<String, Double> getProductsPrice() {
        return productsPrice;
    }

    public HashMap<String, List<DiscountSuppliers>> getProductsDiscount() {
        return productsDiscount;
    }

    public HashMap<String, Integer> getProductsQuantities() {
        return productsQuantities;
    }


    public HashMap<Pair<Integer, Integer>, List<Integer>> getConstOrderReservoir() {
        loadDataConstOrderData();
        return constOrderReservoir;
    }

    public void loadDataConstOrderData() {

        List<OrderConstDTO> orders = orderConstMapper.selectAllSupplierUnOrdered(supplierId);
        for (OrderConstDTO orderConstDTO : orders) {
            Pair<Integer, Integer> pair1 = new Pair<>(orderConstDTO.getOrderId(), orderConstDTO.getBranchId());
            boolean detailsexist = false;
            if (constOrderDetails != null) {
                for (Pair<Integer, Integer> currPair : constOrderDetails.keySet()) {
                    if (currPair.getFirst() == pair1.getFirst() && pair1.getSecond() == currPair.getSecond()) {
                        detailsexist = true;
                    }
                }
            }
            if (!detailsexist) {
                List<ProductInOrderDTO> productInOrderDTOS = productInOrderMapper.selectProductInOrder(orderConstDTO.getOrderId(), orderConstDTO.getBranchId());
                for (ProductInOrderDTO productInOrderDTO : productInOrderDTOS) {
                    HashMap<Pair<String, String>, Integer> hashMap = new HashMap<>();
                    Pair<String, String> pairIdName = new Pair(productInOrderDTO.getProductId(), productInOrderDTO.getProductName());
                    hashMap.put(pairIdName, productInOrderDTO.getAmount());
                    constOrderDetails.put(pair1, hashMap);

                }
            }
            boolean exist = false;
            if (constOrderReservoir != null) {
                if (constOrderReservoir != null) {
                    for (Pair<Integer, Integer> currpair : constOrderReservoir.keySet()) {
                        if (pair1.getFirst() == currpair.getFirst() && pair1.getSecond() == currpair.getSecond()) {
                            exist = true;
                        }
                    }
                }

                if (!exist) {
                    if (constOrderReservoir.get(pair1) != null) {
                        constOrderReservoir.put(pair1, daysToList(orderConstDTO.getDays()));
                    }
                }

            }
        }

    }

    private List<Integer> daysToList (String daysString) {
        List<Integer> toReturn = new ArrayList<>();
        for (int i = 0; i < daysString.length(); i++) {
            toReturn.add(Character.getNumericValue(daysString.charAt(i)));
        }
        return toReturn;

    }

    public HashMap<Pair<String, String>, Integer> getSpecificConstOrderDetails(Pair<Integer,Integer> pair1) {
        //productInOrderMapper.selectProductsInOrder(supplierId);
        loadDataConstOrderData();
        for (Pair<Integer, Integer> findPair : constOrderDetails.keySet()){
            if  (findPair.getFirst() == pair1.getFirst() && findPair.getSecond() == pair1.getSecond()){
                return constOrderDetails.get(findPair);
            }
        }
        return new HashMap<Pair<String,String>,Integer>();



    }


    ///// check how to load DATA.
    public HashMap<Pair<Integer, Integer>, HashMap<Pair<String, String>, Integer>> getConstOrderDetails() {
        //productInOrderMapper.selectProductsInOrder(supplierId);
        loadDataConstOrderData();
        return constOrderDetails;
    }
    //}

    public void setConstOrderDetails(Pair<Integer, Integer> orderIdBranchId, HashMap<Pair<String, String>, Integer> productsAndAmount) throws Exception {
        productInOrderMapper.removeProd(orderIdBranchId.getFirst(), orderIdBranchId.getSecond());
        for (Pair<String , String> products: productsAndAmount.keySet()) {
            ProductInOrder productInOrder = new ProductInOrder(products.getFirst(), orderIdBranchId.getSecond(), orderIdBranchId.getFirst(),
                    1, products.getSecond(), productsAndAmount.get(products), 0, 0, productInOrderMapper);
            //productInOrderMapper.addProductInOrder(productInOrder.getProductInOrderDTO());
        }
        constOrderDetails.put(orderIdBranchId, productsAndAmount);
    }

    public Map<Pair<String, String>, Integer> getConstOrderDetailsByOrderAndBranch(Pair<Integer, Integer> orderIdBranchId) {
        productInOrderMapper.selectProductInOrder(orderIdBranchId.getFirst(), orderIdBranchId.getSecond());
        return constOrderDetails.get(orderIdBranchId);
    }


    public void addDiscountToSupplier(boolean percentageDiscount, double amount, double discount, int discountId) {
        DiscountSuppliersPerOrder discountSuppliers = new DiscountSuppliersPerOrder(percentageDiscount, amount, discount, discountId, supplierId, discountSupplierMapper);
        totalDiscountSuppliers.add(discountSuppliers);
        totalDiscountSuppliers.sort(new DiscountComparator());


    }

    public String getAllOrder() {
        String toReturn = "";
        selectAllSupplierOrders();
        for (Integer order : orders.keySet()) {
            toReturn = toReturn + orders.get(order).getStringOrder();
        }
        return toReturn;
    }




    public void saveConstOrderForSupplier(int orderId, List<Integer> daysOfWeek, HashMap<Pair<String, String>, Integer>
            productsAndAmount, int branchId, String branchAddress, boolean newOrder) {

        Pair<Integer, Integer> pairPair = new Pair<Integer, Integer>(0, 0);
        pairPair.setFirst(orderId);
        pairPair.setSecond(branchId);
        boolean exist = false;
        if (constOrderReservoir!= null) {
            for (Pair<Integer, Integer> pair1 : constOrderReservoir.keySet()) {
                if (pair1.getFirst() == orderId && pair1.getSecond() == branchId) {
                    exist = true;
                }
            }
        }
        if (!exist) {
            constOrderReservoir.put(pairPair, daysOfWeek);
            constOrderDetails.put(pairPair, productsAndAmount);
        }


        if (newOrder) {
            OrderConstDTO orderConstDTO = new OrderConstDTO(productsAndAmount.size(), branchAddress, branchId, supplierId,
                    LocalDate.of(1, 1, 1), orderId, 0, daysToString(daysOfWeek), orderConstMapper);
            orderConstMapper.addConstOrder(orderConstDTO);
            // ArrayList<ProductInOrder> productInOrderArrayList = new ArrayList<>();
            for (Pair<String, String> product1 : productsAndAmount.keySet()) {
                int amount = productsAndAmount.get(product1);
                String productId =product1.getFirst() ;
                int catalogNum = productsByCatalogNum.get(product1.getFirst());
                String productName = product1.getSecond();
                double priceAfter = amount * getPriceForProductAndAmount(product1.getFirst(), amount);
                double priceBefore = getPriceBeforeDiscount(product1.getFirst(),amount);
                new ProductInOrder(productId, branchId, orderId, catalogNum, productName, amount,  priceBefore, priceAfter, this.productInOrderMapper);
            }
        }
        if (!newOrder) {
            orderConstMapper.updateSupplier(supplierId, orderId, branchId);
        }
    }

    public void setCanDeliver(boolean canDeliver, String days) {
        int canDeliverInt = 0;
        if (canDeliver) {
            canDeliverInt = 1;
        }
        supplierDTO.getDao().updateCanDeliver(canDeliverInt, days, supplierId);
        this.canDeliver = canDeliver;
        supplierDTO.setCanDeliver(canDeliverInt);
        supplierDTO.setDays(days);
        this.daysCanSupply = daysToArray(days);

    }

    public void selectAllSupplierOrders() {
        List<OrderConstDTO> orderConstDTOS = orderConstMapper.selectAllOrders(supplierId);
        List<OrderDTO> orderDTOS = orderMapper.selectAllOrders(supplierId);
        for (OrderConstDTO orderConstDTO : orderConstDTOS) {
            if (!orders.containsKey(orderConstDTO.getOrderId())) {
                if (orderConstDTO.getOrderId() > 0) {
                    List<ProductInOrderDTO> productInOrderDTOS = productInOrderMapper.selectProductInOrder(orderConstDTO.getOrderId(), orderConstDTO.getBranchId());
                    List<ProductInOrder> productInOrderList = new ArrayList<>();
                    for (ProductInOrderDTO productInOrderDTO : productInOrderDTOS) {
                        productInOrderList.add(new ProductInOrder(productInOrderDTO));
                    }
                    orders.put(orderConstDTO.getSupplierId(), new OrderConst(orderConstDTO, name, productInOrderList));
                }
            }
        }
        for (OrderDTO orderDTO : orderDTOS) {
            if (!orders.containsKey(orderDTO.getOrderId())) {
                List<ProductInOrderDTO> productInOrderDTOS = productInOrderMapper.selectProductInOrder(orderDTO.getOrderId(), orderDTO.getBranchId());
                List<ProductInOrder> productInOrderList = new ArrayList<>();
                for (ProductInOrderDTO productInOrderDTO : productInOrderDTOS) {
                    productInOrderList.add(new ProductInOrder(productInOrderDTO));
                }
                orders.put(orderDTO.getOrderId(), new Order(orderDTO, name, productInOrderList));
            }
        }
    }

    public void printOrders() {
        selectAllSupplierOrders();
        for (Integer order : orders.keySet()) {
            orders.get(order).printOrder();
        }
    }

    public void setAddress(String address) {
        this.address = address;
        supplierDTO.getDao().updateAddress(address, supplierId);
    }


}