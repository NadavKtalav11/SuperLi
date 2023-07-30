package BusinessLayer.Suppliers;

import BusinessLayer.Branch;
import BusinessLayer.Stock.Product;
import BusinessLayer.Tools.DayConverter;
import BusinessLayer.Tools.Pair;
import PersistenceLayer.DAO.Stock.BranchDAO;
import PersistenceLayer.DAO.Suppliers.*;
import PersistenceLayer.DTO.Stock.BranchDTO;
import PersistenceLayer.DTO.Suppliers.OrderConstDTO;
import PersistenceLayer.DTO.Suppliers.ProductInOrderDTO;
import PersistenceLayer.DTO.Suppliers.SupplierDTO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;


public class SuppliersController {
    private Map<Integer, Supplier> supplierMapMap;
    private Map<Integer, Branch> branchMap;
    //private List<Order> lastOrder;
    private HashMap<Integer , HashMap<Integer ,Supplier>> constOrdersBydays_byOrserId;
    private int orderIdCounter;
    private int discountCounter;
    private SupplierMapper supplierMapper;
    private ProductInOrderMapper productInOrderMapper;

    //private HashMap<Integer, Order> constOrders;
    //private int periodOrderCounter;
    private OrderConstMapper orderConstMapper;
    private OrderMapper orderMapper;
    private DiscountSupplierMapper discountSupplierMapper;
    private SupplierProductDiscountsMapper supplierProductDiscountsMapper;
    private BranchDAO branchDAO = new BranchDAO();



    public SuppliersController() {

        this.supplierMapper = new SupplierMapper();
        this.orderConstMapper = new OrderConstMapper();
        this.orderMapper =new OrderMapper();
        productInOrderMapper = new ProductInOrderMapper();
        this.discountSupplierMapper =new DiscountSupplierMapper();
        this.supplierProductDiscountsMapper= new SupplierProductDiscountsMapper();
        this.supplierMapMap = new HashMap<>();
        //lastOrder = new ArrayList<>();
        this.branchMap = new HashMap<>();



        this.discountCounter = Math.max(discountSupplierMapper.getMaxDiscountId(),
                supplierProductDiscountsMapper.getMaxDiscountId())+1;

        this.supplierMapper = new SupplierMapper();

        int maxOrderId = orderMapper.getMaxDiscountId();
        int maxOrderConstId = orderConstMapper.getMaxDiscountId();
        this.orderIdCounter = Math.max(maxOrderId,maxOrderConstId )+1;  ;
        int maxDiscountSupplierMapper = discountSupplierMapper.getMaxDiscountId();
        int maxDiscountProductSupplierMapper = supplierProductDiscountsMapper.getMaxDiscountId();
        this.discountCounter = Math.max(maxDiscountProductSupplierMapper, maxDiscountSupplierMapper)+1;
        loadALLUnordered();


    }


    public void addDiscountToProduct(int supplierId, String productId, double amount, double discount, boolean percentageDiscount) {
        if (!containSupplier(supplierId)) {
            throw new IllegalArgumentException("this supplierId doesn't exist in the system");
        }
        //if (!containProduct(productID)) {
        //    throw new IllegalArgumentException("this product ID doesn't exist in the system");
        //}
        if (!supplierContainSProduct(supplierId, productId)) {
            throw new IllegalArgumentException("this supplier does not supply this product");
        }

        getSupplier(supplierId).addDiscountToProduct(productId,percentageDiscount, amount, discount, discountCounter++);
    }

    public void addDiscountToOrder(int supplierId, double amount, double discount, boolean percentageDiscount) {
        if (!containSupplier(supplierId)) {
            throw new IllegalArgumentException("this supplierId doesn't exist in the system");
        }
        int percentageDiscountInt =0;
        if (percentageDiscount){
            percentageDiscountInt=1;
        }
        //discountSupplierMapper.addSupplierDiscount(new DiscountSupplierDTO( amount ,discount, supplierId, percentageDiscountInt))
        getSupplier(supplierId).addDiscountToSupplier(percentageDiscount, amount, discount, discountCounter++);
    }



    public void addSupplier(int supplierId, boolean canDeliver, String workDays, int BNNumber, int BankAccount, PaymentOptions pOpt, String name, String contactName, String phoneNumber, String address) {
        if (containSupplier(supplierId)) {
            throw new IllegalArgumentException("this supplier is already in the system");
        }
        Supplier supplier = new Supplier(supplierId, BNNumber, BankAccount, pOpt, 1, contactName, phoneNumber, canDeliver, workDays, name, address,supplierMapper);
        supplierMapMap.put(supplierId,supplier);
    }

    public void removeSupplier(int supplierId) throws Exception{
        //orderConstMapper.getAllUnOrdered();
        loadAllUnOrderedConst();
        if (!containSupplier(supplierId)) {
            throw new IllegalArgumentException("this supplier number that you have enter is not exist in the system"
                    + " please check the number and try again");
        } else {
            Supplier supplier = getSupplier(supplierId);
            HashMap<Pair<Integer,Integer>, List<Integer>> constOrderReservoir = supplier.getConstOrderReservoir();
            HashMap<Pair<Integer,Integer>, HashMap<Pair<String,String>, Integer>> constOrderDetails = supplier.getConstOrderDetails();
            supplier.removeSupplier();
            supplierMapMap.remove(supplierId);
            supplierMapper.removeSupplier(supplierId);
            for (Pair<Integer,Integer> key: constOrderReservoir.keySet()){
                Supplier supplier1 = findCurrentBestSupplierForPeriodOrder(constOrderReservoir.get(key),constOrderDetails.get(key));
                if (supplier1!=null){
                    supplier1.saveConstOrderForSupplier(key.getFirst(),constOrderReservoir.get(key),constOrderDetails.get(key),key.getSecond(), getBranch(key.getSecond()).getAddress(), false);
                }
            }

        }
    }

    public Branch getBranch(int branchId) throws Exception{
        Branch branch = null;
        if (branchMap.containsKey(branchId)) {
            return branchMap.get(branchId);
        }
        else {
            BranchDTO branchDTO=  branchDAO.getBranch(branchId);
            if (branchDTO!= null) {
                branch = new Branch(branchDTO);
                branchMap.put(branchId, branch);
            }
        }
        return branch;
    }



    /*public void printLastOrder() {
        if (lastOrder.size() == 0) {
            throw new IllegalArgumentException("no orders in the system");
        }
        for (Order order : lastOrder) {
            order.printOrder();
        }
    }*/

    public String  getAllOrders(){
        String toReturn = "";
        loadAllSuppliers();
        for (int supplierId: supplierMapMap.keySet()){
            toReturn = toReturn + getSupplier(supplierId).getAllOrder();
        }
        if (toReturn.length()==0){
            toReturn = "no orders in the system";
        }
        return toReturn;
    }

    public String printSuppliersForGUI() {
        loadAllSuppliers();

        String suppliers = "";

        for (int supplierId : supplierMapMap.keySet()) {
            Supplier supplier = getSupplier(supplierId);
            suppliers = suppliers + "supplier Id : " + supplier.getSupplierId() + " " + "|" + " supplierName : " + supplier.getName();
            suppliers = suppliers + "\n";
        }
        return suppliers;
    }

    public void removeDiscountFromProduct(int supplierId ,String productId,  int discountId) {
        getSupplier(supplierId).removeDiscountToProduct(productId, discountId);
    }

    public void removeDiscountFromSupplier(int supplierId ,  int discountId) throws Exception {
        getSupplier(supplierId).removeDiscountToSupplier(discountId);
    }

    public String getDiscounts(int supplierId) {
        String toReturn=getSupplier(supplierId).getDiscounts();
        if (toReturn.length()==0){
            return "no discounts for this supplier";
        }
        return toReturn;
    }
    public String getProductDiscounts(int supplierId, String productId) {
        String toReturn=  getSupplier(supplierId).getDiscountsProduct(productId);
        if (toReturn.length()==0){
            return "no discounts for this product and supplier";
        }
        return toReturn;
    }




    public void printAllOrders(){
        loadAllSuppliers();
        for (int supplierId: supplierMapMap.keySet()){
            getSupplier(supplierId).printOrders();
        }
    }

    public void editSupplierName(int supplierId, String name) {
        if (!containSupplier(supplierId)) {
            throw new IllegalArgumentException("this supplier Id does not exist");
        }
        getSupplier(supplierId).setName(name);
    }

    public void editSupplierAddress(int supplierId, String address) {
        if (!containSupplier(supplierId)) {
            throw new IllegalArgumentException("this supplier Id does not exist");
        }
        getSupplier(supplierId).setAddress(address);
    }

    public void editSupplierCanDeliver(int supplierId, boolean canDeliver, String days) {
        if (!containSupplier(supplierId)) {
            throw new IllegalArgumentException("this supplier Id does not exist");
        }
        getSupplier(supplierId).setCanDeliver(canDeliver, days);
    }

    public void editSupplierPaymentTerms(int supplierId, PaymentOptions paymentOptions) {
        if (!containSupplier(supplierId)) {
            throw new IllegalArgumentException("this supplier Id does not exist");
        }
        getSupplier(supplierId).setPaymentTerms(paymentOptions);
    }

    public void editBNNumber(int supplierId, int bnNumber) {
        if (!containSupplier(supplierId)) {
            throw new IllegalArgumentException("this supplier Id does not exist");
        }
        getSupplier(supplierId).setBNNumber(bnNumber);
    }

    public void editBankAccount(int supplierId, int bankAccount) {
        if (!containSupplier(supplierId)) {
            throw new IllegalArgumentException("this supplier Id does not exist");
        }
        getSupplier(supplierId).setBankAccount(bankAccount);
    }
    public void removeAllData(){
        supplierMapper.clearData();
        //for(Integer supplier :supplierMapMap.keySet()){
        //    Supplier supplier1 = supplierMapMap.get(supplier);
        //    supplier1.removeSupplier();
       // }
        supplierMapMap.clear();
        ProductInOrderMapper productInOrderMapper = new ProductInOrderMapper();
        productInOrderMapper.clearData();
        orderConstMapper.clearData();
        orderMapper.clearData();
        discountSupplierMapper.clearData();
        SupplierContactMapper supplierContactMapper = new SupplierContactMapper();
        supplierContactMapper.clearData();
        SupplierCardMapper supplierCardMapper = new SupplierCardMapper();
        supplierCardMapper.removeAll();
        supplierProductDiscountsMapper.clearData();
        DiscountSupplierMapper dsmapper= new DiscountSupplierMapper();
        dsmapper.clearData();
        ProductInOrderMapper piomapper= new ProductInOrderMapper();
        piomapper.clearData();
        ProductQuantitiesAndPriceMapper mapper1= new ProductQuantitiesAndPriceMapper();
        mapper1.removeAll();
        supplierContactMapper.clearData();
        SupplierProductDiscountsMapper supplierProductDiscountsMapper = new SupplierProductDiscountsMapper();
        supplierProductDiscountsMapper.clearData();
        orderIdCounter =1;
        discountCounter =1;
    }

    public boolean containSupplier(int supplierID) {
        boolean exist = supplierMapMap.containsKey(supplierID);
        if (exist) {
            return true;
        }
        SupplierDTO supplierDTO = supplierMapper.selectSupplier(supplierID);
        if (supplierDTO == null) {
            return false;
        }
        Supplier supplier = new Supplier(supplierDTO);
        supplierMapMap.put(supplierID, supplier);
        return true;
    }


    private boolean supplierContainSProduct(int supplierId, Product product) {
        Supplier supplier = getSupplier(supplierId);
        boolean exist = supplier.isSupplyTheProduct(product);
        if (exist) {
            return true;
        }
        //getSupplier(supplierId).addProductToSupply(product, productQuantitiesAndPriceDTO.getCatalogNumber(), productQuantitiesAndPriceDTO.getAmountCanSupply(), productQuantitiesAndPriceDTO.getPrice());
        return false;
    }

    private boolean supplierContainSProduct(int supplierId, String productId) {
        Supplier supplier = getSupplier(supplierId);
        boolean exist = supplier.isSupplyTheProduct(productId);
        if (exist) {
            return true;
        }
        //getSupplier(supplierId).addProductToSupply(product, productQuantitiesAndPriceDTO.getCatalogNumber(), productQuantitiesAndPriceDTO.getAmountCanSupply(), productQuantitiesAndPriceDTO.getPrice());
        return false;
    }




    public Supplier getSupplier(int supplierId) {
        if (!containSupplier(supplierId)) {
            throw new IllegalArgumentException("this supplier Id does not exist");
        }
        return supplierMapMap.get(supplierId);
    }


    /*public void editOrder(Order order, ProductInOrder product, int newAmount) {
        order.editProductInOrderAmount(product, newAmount);
    }*/


    /*public void addProductToExistingOrder(Order order, Product product, int amount) {
        if(order.getCurrOrderStatus()!= Order.OrderStatus.not_order_yet) {
            throw new IllegalArgumentException("order has already sent");
        }
        Supplier supplier1 = order.getSupplier();
        int catalogNum = supplier1.getProductsByCatalogNum().get(product);
        order.AddProductToOrder(product, amount,catalogNum );
    }*/

    public void printSuppliers() {
        loadAllSuppliers();
        for (int supplierId : supplierMapMap.keySet()) {
            Supplier supplier = getSupplier(supplierId);
            System.out.println("supplier Id : " + supplier.getSupplierId() + " supplierName : " + supplier.getName());
        }
    }

    public void removeProductFromSupplier(int supplierId, String product) {
        if (!containSupplier(supplierId)) {
            throw new IllegalArgumentException("this supplier Id does not exist in the system");
        }
        if (product == null) {
            System.out.println("this product Id does not exist in the system , please add it to the system ant then try again");
        }
        getSupplier(supplierId).removeProduct(product);
    }


    public void addProductToSupplier(int supplierId, String productId, int catalogNum, int amountToSupply, double price) {
        if (!containSupplier(supplierId)) {
            throw new IllegalArgumentException("this supplier Id does not exist in the system");
        }

        getSupplier(supplierId).addProductToSupply(productId, catalogNum, amountToSupply, price);
    }

    /*private SupplierProduct getProductByProductId(int productId) {
        if (!containProduct(productId)) {
            System.out.println("this product Id doesn't exist in the system");
        }
        return systemProducts.get(productId);
    }*/


    public Map<Supplier, Integer> getBestPriceSuppliers(String supplierProduct, int amount, List<Supplier> supplierList) {
        List<Supplier> relevantSuppliers = new ArrayList<>();
        List<Double> relevantSuppliersPrices = new ArrayList<>();
        for (Supplier supplier : supplierList) {
            if (supplier.isSupplyTheProduct(supplierProduct)) {
                int amount1 = supplier.getProductsQuantities().get(supplierProduct);
                relevantSuppliers.add(supplier);
                relevantSuppliersPrices.add(supplier.getPriceForProductAndAmount(supplierProduct, amount1));
            }
        }
        int totalAmountCanSupply = 0;
        for (Supplier supplier : relevantSuppliers) {
            totalAmountCanSupply = totalAmountCanSupply + supplier.getMaxAmountPerProduct(supplierProduct);
        }
        if (totalAmountCanSupply < amount) {
            return new HashMap<>();
        }
        //    throw new IllegalArgumentException("this product amount cannot supply by the suppliers");
        //}
        Map<Supplier, Integer> toOrderSuppliers = new HashMap<>();
        if (relevantSuppliers.size() > 0){
            while (amount > 0) {
                Supplier minSupplier = getMultiBestPriceSupplier(relevantSuppliers, relevantSuppliersPrices);
                toOrderSuppliers.put(minSupplier, Math.min(minSupplier.getMaxAmountPerProduct(supplierProduct), amount));
                amount = amount - minSupplier.getMaxAmountPerProduct(supplierProduct);
            }
        }
        return toOrderSuppliers;
    }

    private Supplier getMultiBestPriceSupplier(List<Supplier> relevantSuppliers, List<Double> relevantSuppliersPrices) {
        double minPrice = relevantSuppliersPrices.get(0);
        Supplier minSupplier = relevantSuppliers.get(0);
        int minIndex = 0;
        for (int i = 1; i < relevantSuppliersPrices.size(); i++) {
            if (relevantSuppliersPrices.get(i) < minPrice) {
                minIndex = i;
                minSupplier = relevantSuppliers.get(i);
            }
        }
        relevantSuppliers.remove(minSupplier);
        relevantSuppliersPrices.remove(minPrice);
        return minSupplier;
    }


    // add new Order to the System

    private void addOrderFromSpecificSupplier(int branchId, int supplierId, Map<Pair<String,String>, Integer> products)throws Exception {
        Supplier currSupplier = getSupplier(supplierId);

        currSupplier.addShortageOrder(branchId, getBranch(branchId).getAddress(), getOrderIdCounter(), products);
    }



    public ArrayList<Supplier>[] SuppliersNextSupplyDayArray() {
        ArrayList<Supplier>[] arrayToReturn = new ArrayList[7];
        for (int i=0; i<7 ;i++){
            arrayToReturn[i] = new ArrayList<>();
        }
        //ArrayList<Supplier> arrayToReturn =new ArrayList<Supplier>();
        int today = LocalDate.now().getDayOfWeek().getValue();
        for (int supplierId : supplierMapMap.keySet()) {
            Supplier supplier = getSupplier(supplierId);
            int nearestDay = supplier.findNearestDayCanSupply(today);
            if (arrayToReturn[(nearestDay+today)%7]==null){
                arrayToReturn[(nearestDay+today)%7]= new ArrayList<>();
            }
            arrayToReturn[(nearestDay+today)%7].add(supplier);
        }

        return arrayToReturn;
    }
    public void loadAllSuppliers(){
        List<SupplierDTO> allSuppliers = supplierMapper.selectAllSuppliers();
        for (SupplierDTO supplierDTO: allSuppliers){
            if (!supplierMapMap.containsKey(supplierDTO.getId())){
                Supplier supplier = new Supplier(supplierDTO);
                supplierMapMap.put(supplierDTO.getId(),supplier);
            }
        }
    }

    public void loadAllUnOrderedConst(){
        List<OrderConstDTO> allConstOrders = orderConstMapper.selectAllOrders();
        List<ProductInOrderDTO> productInOrders;
        for (OrderConstDTO orderConstDTO: allConstOrders){
            Supplier supplier1= getSupplier(orderConstDTO.getSupplierId());
            int orderId = orderConstDTO.getOrderId();
            int branchId = orderConstDTO.getBranchId();
            productInOrders = productInOrderMapper.selectProductInOrder(orderId,branchId);
            Pair<String,String> IdAndNameP ;
            HashMap<Pair<String,String>,Integer> productsAndAmount = new HashMap<>();
            Pair<Integer, Integer> orderBranchId = new Pair<>(orderId,branchId);
            List<Integer> days = new ArrayList<>();
            boolean exist = false;
            for (Pair<Integer,Integer> pair1 : supplier1.getConstOrderReservoir().keySet()){
                if (pair1.getFirst()== orderBranchId.getFirst() && branchId == pair1.getSecond()){
                    exist =true;
                }
            }
            if(!exist){
                String days1 = orderConstDTO.getDays();
                for (int i = 0; i < days1.length(); i++) {
                    int digit = Character.getNumericValue(days1.charAt(i));
                    days.add(digit);
                }
                supplier1.getConstOrderReservoir().put(orderBranchId,days);
                for(ProductInOrderDTO productInOrderDTO:productInOrders){
                    String Id = productInOrderDTO.getProductId();
                    String name = productInOrderDTO.getProductName();
                    IdAndNameP = new Pair<>(Id,name);
                    Integer amount = productInOrderDTO.getAmount();
                    productsAndAmount.put(IdAndNameP,amount);
                    supplier1.getConstOrderDetails().put(orderBranchId,productsAndAmount);

                }



            }

        }
    }



    public boolean addShortageOrder(Map<Pair<String, String>, Integer> productsAndAmount, int branchId) throws Exception {
        loadAllSuppliers();
        ArrayList<Supplier>[] nearestDAysArray = SuppliersNextSupplyDayArray();
        ArrayList<Supplier> canSupplyAlone = new ArrayList<>();
        int dayToAllOrder=7;
        //lastOrder = new ArrayList<>();
        boolean found= false;
        /// check if there is a supplier who can supply all order alone ,
        // if exist it save the nearest day;
        HashMap<String, Integer> productsAndAmount1 = new HashMap<>();
        for(Pair<String,String> productIdAndName : productsAndAmount.keySet() ){
            String productId = productIdAndName.getFirst();
            productsAndAmount1.put(productId,productsAndAmount.get(productIdAndName));
        }

        for (int i = 0; i < 7 & !found; i++) {
            //List<Integer> canSupplyAllOrder = new ArrayList<>();
            for (Supplier supplier : nearestDAysArray[i]) {
                if (supplier.isSupplyAllTheProducts(productsAndAmount1)) {
                    canSupplyAlone.add(supplier);
                    found = true;
                    dayToAllOrder =i;
                }
            }
        }
        Map<Supplier, HashMap<String, Integer>> productsBySuppliers= new HashMap<>();
        ArrayList<Supplier> relevantSuppliers ;
        ArrayList<Supplier> relevantSuppliersUnited= new ArrayList<>() ;
        HashMap<String, Integer> productsAndAmountCopy= new HashMap<>(productsAndAmount1);
        int counterCanOrderToday =0;
        for (int i= 0 ; i< dayToAllOrder  ; i++ ) {
            relevantSuppliers = nearestDAysArray[i];
            relevantSuppliersUnited.addAll(relevantSuppliers);
            int supplierId = getWhoSupplyTheMost(productsAndAmountCopy, relevantSuppliers);
            while (productsAndAmountCopy.keySet().size() > 0 && supplierId != -1) {
                List<String> canSupplyList = getSupplier(supplierId).getNumOfProductsCanSupply(productsAndAmountCopy);
                counterCanOrderToday = counterCanOrderToday + canSupplyList.size();
                HashMap<String, Integer> productIntegerMap = shareTheOrder(productsAndAmountCopy, canSupplyList);
                if (!productsBySuppliers.containsKey(getSupplier(supplierId))) {
                    productsBySuppliers.put(getSupplier(supplierId), productIntegerMap);
                } else {
                    for (String supplierProduct : productIntegerMap.keySet()) {
                        productsBySuppliers.get(getSupplier(supplierId)).put(supplierProduct, productIntegerMap.get(supplierProduct));
                    }
                }
                supplierId = getWhoSupplyTheMost(productsAndAmountCopy, relevantSuppliers);
            }
            if (i== dayToAllOrder-1 ){
                List<String> supplierProductsToRemove = new ArrayList<>();
                for (String productId : productsAndAmountCopy.keySet()){
                    Map<Supplier, Integer> suppliersProducts =shareTheProductOrder(productId, productsAndAmountCopy.get(productId), relevantSuppliersUnited);
                    if (!suppliersProducts.isEmpty()) {
                        for (Supplier supplier : suppliersProducts.keySet()) {
                            if (productsBySuppliers.containsKey(supplier)) {
                                productsBySuppliers.get(supplier).put(productId, suppliersProducts.get(supplier));
                            } else {
                                HashMap<String, Integer> currSupplierHashMap = new HashMap<String, Integer>();
                                currSupplierHashMap.put(productId, suppliersProducts.get(supplier));
                                productsBySuppliers.put(supplier, currSupplierHashMap);
                            }
                        }
                        //productsAndAmountCopy.remove(supplierProduct);
                        supplierProductsToRemove.add(productId);
                    }
                }
                for (String supplierProduct : supplierProductsToRemove) {
                    productsAndAmountCopy.remove(supplierProduct);
                }
            }
        }
        if (productsAndAmountCopy.size()>0){
            int minSupplierId = -1;
            double minOrder = Integer.MAX_VALUE;
            for (Supplier supplier: canSupplyAlone) {
                double currPrice = supplier.getPriceForAllOrder(productsAndAmount1);
                if (minOrder > currPrice) {
                    minOrder = currPrice;
                    minSupplierId = supplier.getSupplierId();
                }
            }
            if (minSupplierId!=-1){
                addOrderFromSpecificSupplier(branchId,minSupplierId, productsAndAmount);
                return true;
            }
            else {
                return false;
            }
        }
        else{
            for (Supplier supplier: productsBySuppliers.keySet()) {
                HashMap<String, Integer> productsAmount = productsBySuppliers.get(supplier);
                Map<Pair<String, String>, Integer> allInfo = new HashMap<>();
                for(String productId : productsAmount.keySet()){
                    for(Pair<String, String> nameId: productsAndAmount.keySet()){
                        if(productId.equals(nameId.getFirst())){
                            Pair<String, String> namePid = new Pair<String, String>("", "");
                            namePid.setFirst(productId);
                            namePid.setSecond(nameId.getSecond());
                            allInfo.put(namePid,productsAmount.get(productId));

                        }
                    }
                }
                addOrderFromSpecificSupplier(branchId,supplier.getSupplierId(), allInfo );
            }
            return true;
        }

    }




    public void addAutomaticOrderConst(int branchId, int orderId, List<Integer> constantDays, HashMap<Pair<String, String>, Integer> items, boolean newOrder  ) throws Exception{
        loadAllSuppliers();
        Supplier supplier1 = findCurrentBestSupplierForPeriodOrder(constantDays, items);
        String branchAddress = getBranch(branchId).getAddress();
        supplier1.saveConstOrderForSupplier(orderId, constantDays, items, branchId, branchAddress, newOrder);

    }


    public Supplier findCurrentBestSupplierForPeriodOrder(List<Integer> constantDays, Map<Pair<String, String>, Integer> items){
        List<Supplier> constSuppliers = getConstSuppliers();
        List<Supplier> constSuppliersMatchingDays = new ArrayList<>();
        for (Supplier supplier: constSuppliers){
            if (supplier.supplyThisDays(constantDays)){
                constSuppliersMatchingDays.add(supplier);
            }
        }

        Map<String, Integer> currProducts = new HashMap();
        for(Pair<String,String> orderIdBranchId: items.keySet()){
            currProducts.put(orderIdBranchId.getFirst(),items.get(orderIdBranchId));
        }
        List<Integer> canSupplyAllOrder = new ArrayList<>();
        for (Supplier supplier1 : constSuppliersMatchingDays) {
            if (supplier1.isSupplyAllTheProducts(currProducts)) {
                canSupplyAllOrder.add(supplier1.getSupplierId());
            }
        }
        int minSupplierId = -1;
        double minOrder = Integer.MAX_VALUE;
        for (int supplierId : canSupplyAllOrder) {
            double currPrice = getSupplier(supplierId).getPriceForAllOrder(currProducts);
            if (minOrder > currPrice) {
                minOrder = currPrice;
                minSupplierId = supplierId;
            }
        }
        if (minSupplierId == -1) {
            throw new IllegalArgumentException("no supplier can supply");
        }
        return getSupplier(minSupplierId);
    }


    /*
        public void alertPeriodOrders () throws Exception{
          //  System.out.println("workinggg");
            loadAllSuppliers();
           // loadAllUnOrderedConst();
            supplierMapper.selectAllSuppliers();
            orderConstMapper.getAllUnOrdered();
            for(Integer supplierId: supplierMapMap.keySet()){
                Supplier supplier1 = getSupplier(supplierId);
                supplier1.loadDataConstOrderData();
                HashMap<Pair<String,String>, Integer> hash1= new HashMap<>();
                if(supplier1.getConstOrderReservoir().size()>0){

                for (Pair<Integer,Integer> orderIdBranchId: supplier1.getConstOrderReservoir().keySet()) {
                    System.out.println(supplier1.getConstOrderReservoir().size());
                    LocalDate today = LocalDate.now();
                    DayOfWeek dayOfWeek = today.getDayOfWeek();
                    int dayOfWeekNum = dayOfWeek.getValue();
                    System.out.println(dayOfWeekNum);
                    if (supplier1.getConstOrderReservoir().containsKey(orderIdBranchId)) {
                        if (supplier1.getConstOrderReservoir().get(orderIdBranchId).contains((dayOfWeekNum + 1) % 7)) {
                            System.out.println("in");
                            System.out.println(supplier1.getConstOrderDetails().size());
                            if (supplier1.getConstOrderDetails().containsKey(orderIdBranchId)) {

                                System.out.println("blaa");
                                HashMap<Pair<String, String>, Integer> hash3 = supplier1.getConstOrderDetails().get(orderIdBranchId);
                                for (Pair<String, String> productIdPName : hash3.keySet()) {
                                    System.out.println("in111");
                                    int amount = supplier1.getConstOrderDetails().get(orderIdBranchId).get(productIdPName);
                                    System.out.println("in111");

                                    hash1.put(productIdPName, amount);
                                    System.out.println("in111");

                                }
                                //  hash1 = supplier1.getConstOrderDetails().get(orderIdBranchId);
                                Supplier supplier2 = findCurrentBestSupplierForPeriodOrder(supplier1.getConstOrderReservoir().get(orderIdBranchId), hash1);
                                System.out.println("in14444411");

                                int branchId = orderIdBranchId.getSecond();
                                addConstOrderFromSpecificSupplier(supplier2, supplier1.getConstOrderReservoir().get(orderIdBranchId), hash1, branchId, orderIdCounter);
                                System.out.println("done");

                            }

                        }
                    }
                }
                    for (Pair<Integer, Integer> orderIdBranchId : ordersToRemove) {
                        supplier1.getConstOrderReservoir().remove(orderIdBranchId);
                        supplier1.getConstOrderDetails().remove(orderIdBranchId);
                    }
                }
            }

        }
    */
    public void alertPeriodOrders1() throws Exception {
        loadAllSuppliers();
        //supplierMapper.selectAllSuppliers();
        //orderConstMapper.getAllUnOrdered();
        loadAllUnOrderedConst();

        for (Iterator<Integer> iterator = supplierMapMap.keySet().iterator(); iterator.hasNext();) {
            Integer supplierId = iterator.next();
            Supplier supplier1 = getSupplier(supplierId);
            supplier1.loadDataConstOrderData();
            HashMap<Pair<String, String>, Integer> hash1 = new HashMap<>();

            if (supplier1.getConstOrderReservoir().size() > 0) {
                List<Pair<Integer, Integer>> ordersToRemove = new ArrayList<>();
                List<Integer> days = new ArrayList<>();

                for (Pair<Integer, Integer> orderIdBranchId : supplier1.getConstOrderReservoir().keySet()) {
                    LocalDate today = LocalDate.now();
                    DayOfWeek dayOfWeek = today.getDayOfWeek();
                    int dayOfWeekNum = dayOfWeek.getValue();

                    if (supplier1.getConstOrderReservoir().containsKey(orderIdBranchId)) {
                        if (supplier1.getConstOrderReservoir().get(orderIdBranchId).contains((dayOfWeekNum + 1) % 7)) {
                            if (supplier1.getConstOrderDetails().containsKey(orderIdBranchId)) {
                                HashMap<Pair<String, String>, Integer> hash3 = new HashMap<>(supplier1.getConstOrderDetails().get(orderIdBranchId));
                                days = supplier1.getConstOrderReservoir().get(orderIdBranchId);

                                for (Pair<String, String> productIdPName : hash3.keySet()) {
                                    int amount = supplier1.getConstOrderDetails().get(orderIdBranchId).get(productIdPName);
                                    hash1.put(productIdPName, amount);
                                }

                                Supplier supplier2 = findCurrentBestSupplierForPeriodOrder(supplier1.getConstOrderReservoir().get(orderIdBranchId), hash1);
                                int branchId = orderIdBranchId.getSecond();
                                addConstOrderFromSpecificSupplier(supplier2, supplier1.getConstOrderReservoir().get(orderIdBranchId), hash1, branchId, orderIdCounter);
                                if(supplier1.getSupplierId()!= supplier2.getSupplierId()){
                                    ordersToRemove.add(orderIdBranchId);}
                                // supplier2.getConstOrderDetails().put(orderIdBranchId, hash1);
                                // supplier2.getConstOrderReservoir().put(orderIdBranchId,days);
                            }
                        }
                    }
                }

                // Remove the processed orders
                for (Pair<Integer, Integer> orderIdBranchId : ordersToRemove) {
                    supplier1.getConstOrderReservoir().remove(orderIdBranchId);
                    supplier1.getConstOrderDetails().remove(orderIdBranchId);
                }
            }
        }
    }



    private void addConstOrderFromSpecificSupplier(Supplier supplier, List<Integer> days,HashMap<Pair<String,String>,Integer> productsAmount, int branchId, int orderId) throws Exception{
        Branch branch = getBranch(branchId);
        supplier.addConstOrder(branchId,branch.getAddress(), getOrderIdCounter(), productsAmount, days );
    }

    public int getOrderIdCounter(){
        return orderIdCounter++;
    }



/*
    public Order addOrderConst(Supplier supplier,List<Integer> supplyConstantDays,HashMap<String, Integer> productsAndAmount, int branchId,int orderID) throws Exception {
        HashMap<Product, Integer> itemAndAmount = new HashMap<>();
        HashMap<Integer,HashMap<Integer,Integer>> itemToAmountAndDiscount = new HashMap<>();
        Order orderConst = supplier.addConstOrder(  branchId, getBranchMap().get(branchId).getAddress(),orderID,productsAndAmount,  supplyConstantDays );
        return orderConst;
    }

 */

    public List<Supplier> getConstSuppliers() {
        List<Supplier> supplierList = new ArrayList<>();
        for(Integer supplierId: supplierMapMap.keySet()){
            supplierList.add(getSupplier(supplierId));
        }
        List<Supplier> suppliersConstList = new LinkedList<>();
        for(Supplier s : supplierList)
            if(s.isWorkOnSpecificDays())
                suppliersConstList.add(s);
        return suppliersConstList;
    }


    public boolean checkCanEditConstOrder(int orderId, int branchId){
        boolean possibleToEdit = true;
        Supplier supplier1 =findIsTheCurrentHolder(orderId, branchId);
        LocalDate localDate = LocalDate.now();
        int currentDayOfWeek = localDate.getDayOfWeek().getValue();

        for(Pair<Integer, Integer> orderIdBranchId : supplier1.getConstOrderReservoir().keySet()) {
            if(orderIdBranchId.getFirst()==orderId&& orderIdBranchId.getSecond()==branchId) {
                for (Integer dw : supplier1.getConstOrderReservoir().get(orderIdBranchId)) {
                    if ((currentDayOfWeek + 1) % 7 == dw % 7 || currentDayOfWeek == dw)
                        return false;
                }
                return true;
            }
        }
        return possibleToEdit;
    }

    public Supplier findIsTheCurrentHolder(int orderId, int branchId){
        Supplier supplier = null;
        //  Pair<Integer, Integer> orderIdBranchId = new Pair<Integer,Integer>(orderId,branchId);
        for (Integer supplierId: supplierMapMap.keySet()){
            for(Pair<Integer, Integer> key: getSupplier(supplierId).getConstOrderReservoir().keySet()) {
                if (key.getFirst() == orderId && key.getSecond() == branchId) {
                    supplier = getSupplier(supplierId);
                }
            }
        }
        return supplier;
    }



    private int getWhoSupplyTheMost(Map<String, Integer> productsToOrder, ArrayList<Supplier> relevantSuppliers) {
        int maxProducts = 0;
        int minSupplierId = -1;
        double price = Integer.MAX_VALUE;
        for (Supplier supplier : relevantSuppliers) {
            List<String> canSupply = supplier.getNumOfProductsCanSupply(productsToOrder);
            int currNum = canSupply.size();
            HashMap<String, Integer> canSupplyMap = new HashMap<>();
            for (int i=0; i<canSupply.size();i++){
                canSupplyMap.put(canSupply.get(i), productsToOrder.get(canSupply.get(i)));
            }
            if (currNum > maxProducts) {
                maxProducts = currNum;
                minSupplierId = supplier.getSupplierId();
                price =supplier.getPriceForAllOrder(canSupplyMap);
            }
            else if (currNum == maxProducts &currNum!=0) {
                double currPrice = supplier.getPriceForAllOrder(canSupplyMap);
                if (price > currPrice ) {
                    maxProducts = currNum;
                    minSupplierId = supplier.getSupplierId();
                    price = currPrice;
                }
            }
        }
        return minSupplierId;
    }


    private HashMap<String, Integer> shareTheOrder(HashMap<String, Integer> productsToOrder, List<String> canSupplyList) {
        HashMap<String, Integer> biggestOrder = new HashMap<>();
        List<String> toRemove = new ArrayList<>();
        for (String supplierProduct : productsToOrder.keySet()) {
            if (canSupplyList.contains(supplierProduct)) {
                biggestOrder.put(supplierProduct, productsToOrder.get(supplierProduct));
                toRemove.add(supplierProduct);
            }

        }
        for(int i=0; i< toRemove.size();i++){
            productsToOrder.remove(toRemove.get(i));
        }
        return biggestOrder;
    }
    private Map<Supplier , Integer> shareTheProductOrder(String supplierProduct, int amount , List<Supplier> supplierList) {
        Map<Supplier, Integer> currSuppliers = getBestPriceSuppliers(supplierProduct, amount ,supplierList);
        return currSuppliers;
    }



    //////////////////////////////////////////////
    // Supplier's edit functions
    public void addContact (int supplierId, String contactName , String phoneNumber ){
        if (!containSupplier(supplierId)){
            throw new IllegalArgumentException("can't add a contact member to a supplier that isn't in the system");
        }
        getSupplier(supplierId).addContact(contactName , phoneNumber);
    }

    public void removeContact(int supplierId, int contactId){
        if (!containSupplier(supplierId)){
            throw new IllegalArgumentException("can't remove a contact member to a supplier that isn't in the system");
        }
        getSupplier(supplierId).removeContact(contactId);
    }

    public void printSupplierContacts(int supplierId){
        getSupplier(supplierId).printContacts();
    }



    public  Map<Integer, Supplier> getSupplierMapMap(){
        return supplierMapMap;
    }




    public Map<Integer, Branch> getBranchMap(){
        return branchMap;
    }

    //public List<Order> getAllOrders(){
    //    return allOrders;
    //}



    public void deleteAutomaticOrder(int branchId, int orderId){
        //orderConstMapper.getAllUnOrdered();
        loadAllUnOrderedConst();
        loadAllSuppliers();
        //supplierMapper.selectAllSuppliers();

        for(Integer supplierId: supplierMapMap.keySet()) {
            Pair<Integer, Integer> toRemove = null;
            Supplier supplier1 = getSupplier(supplierId);
            HashMap<Pair<Integer, Integer>, List<Integer>> map = supplier1.getConstOrderReservoir();
            for (Pair<Integer, Integer> orderIdBranchId : map.keySet()) {
                if (orderIdBranchId.getFirst() == orderId && orderIdBranchId.getSecond() == branchId) {
                    orderConstMapper.removeUnordered(branchId, orderId);
                    //supplier1.getConstOrderReservoir().remove(orderIdBranchId);
                    toRemove = orderIdBranchId;
                    //supplier1.constOrderDetailsRemove(orderIdBranchId);
                }
            }
            if (toRemove != null){
                supplier1.getConstOrderReservoir().remove(toRemove);
                supplier1.getConstOrderDetails().remove(toRemove);
                return;
            }
        }
    }



    public int totalNumOfOrders(){
        int total= 0;
        for (Integer supplierId : supplierMapMap.keySet()){
            total = total +supplierMapMap.get(supplierId).numOfOrders();
        }
        return total;
    }


    public Map<Pair<String, String>, Integer> getOrderProductsMap(int branchID, int orderID) {
        loadALLUnordered();
        loadAllSuppliers();
        HashMap<Pair<String,String>, Integer> productsAndAmountNew = new HashMap<>();
        List<Pair<Integer, Integer>> matchingKeys = new ArrayList<>();
        for (Integer supplierId : supplierMapMap.keySet()) {
            Supplier supplier1 = getSupplier(supplierId);
            HashMap<Pair<Integer,Integer> ,List<Integer>> map = supplier1.getConstOrderReservoir();
            for (Pair<Integer, Integer> orderIdBranchId :map.keySet()) {
                if (orderIdBranchId.getFirst() == orderID && orderIdBranchId.getSecond() == branchID) {
                    matchingKeys.add(orderIdBranchId);
                    // productsAndAmountNew = supplier1.getSpecificConstOrderDetails(orderIdBranchId);
                }
            }
            for (Pair<Integer, Integer> key : matchingKeys) {
                productsAndAmountNew.putAll(supplier1.getSpecificConstOrderDetails(key));
            }
        }
        return productsAndAmountNew;
    }

    public Boolean updateOrderProductsAndAmounts(int branchID, int orderID, Map<Pair<String, String>, Integer> productsAndAmountsMap) throws Exception {
        //supplierMapper.selectAllSuppliers();
        HashMap<Pair<String,String> ,Integer> productsHashMap = new HashMap<>(productsAndAmountsMap);
        loadAllSuppliers();
        loadALLUnordered();
        orderConstMapper.selectConstOrder(branchID, orderID);
        boolean canEdit = checkCanEditConstOrder(orderID, branchID);
        if(canEdit) {
            for (Integer supplierId : supplierMapMap.keySet()) {
                Supplier supplier1 = getSupplier(supplierId);
                HashMap<Pair<Integer,Integer> ,List<Integer>> map = supplier1.getConstOrderReservoir();
                for (Pair<Integer,Integer> orderIdBranchId : map.keySet()) {
                    if (orderIdBranchId.getFirst() == orderID && orderIdBranchId.getSecond() == branchID) {
                        //supplier1.getConstOrderDetails().remove(orderIdBranchId);
                        supplier1.setConstOrderDetails(orderIdBranchId, productsHashMap);
                        return true;
                    }

                }
            }
        }
        return canEdit;
    }

    public boolean updateOrderSupplyDays(int branchID, int orderID, List<Integer> newSupplyDays) {
        //supplierMapper.selectAllSuppliers();
        loadAllSuppliers();
        //orderConstMapper.selectConstOrder(orderID, branchID);
        boolean canEdit = checkCanEditConstOrder(orderID, branchID);
        if(canEdit) {
            for (Integer supplierId : supplierMapMap.keySet()) {
                Supplier supplier1 = getSupplier(supplierId);
                HashMap<Pair<Integer,Integer> ,List<Integer>> map = supplier1.getConstOrderReservoir();
                //Pair<Integer,Integer> toAddPair = null;
                for (Pair<Integer,Integer> orderIdBranchId : map.keySet()) {
                    if (orderIdBranchId.getFirst() == orderID && orderIdBranchId.getSecond() == branchID) {
                        orderConstMapper.updateSupplyDate(branchID,orderID, supplier1.daysToString(newSupplyDays));
                        //supplier1.getConstOrderReservoir().remove(orderIdBranchId);

                        map.put(orderIdBranchId, newSupplyDays);
                        return true;
                    }
                }
            }
        }
        return canEdit;
    }



    public void printSupplierOrders(int supplierId){
        getSupplier(supplierId).printOrders();
    }


    public Map<Integer, String> getAutoOrdersList(int branchID){
        //orderConstMapper.getAllUnOrdered();
        loadAllUnOrderedConst();
        //getAllOrders();
        //supplierMapper.selectAllSuppliers();
        loadAllSuppliers();

        Map<Integer, String> orderIdDays = new HashMap<>();

        String days= "";
        for(Integer supplierId: supplierMapMap.keySet()){
            Supplier supplier1 = supplierMapMap.get(supplierId);
            HashMap<Pair<Integer,Integer> ,List<Integer>> map = supplier1.getConstOrderReservoir();
            for(Pair<Integer,Integer> orderIdBranchId: map.keySet()){
                if(orderIdBranchId.getSecond()==branchID){
                    List<Integer> days1 = map.get(orderIdBranchId);
                    days= DayConverter.convertDays(days1);
                    orderIdDays.put(orderIdBranchId.getFirst(), days);
                }
            }
        }
        return orderIdDays;
    }


    public void loadALLUnordered(){
        List<OrderConstDTO> unOrderedList = orderConstMapper.getAllUnOrdered();
        for (OrderConstDTO orderConstDTO: unOrderedList) {
            getSupplier(orderConstDTO.getSupplierId()).loadDataConstOrderData();
        }

    }







}