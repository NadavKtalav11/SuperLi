package Tests.Suppliers;


import BusinessLayer.Stock.BranchController;
import BusinessLayer.Stock.StockController;
import BusinessLayer.Suppliers.DiscountSuppliersPerProduct;
import BusinessLayer.Suppliers.PaymentOptions;
import BusinessLayer.Suppliers.Supplier;
import BusinessLayer.Suppliers.SuppliersController;
import BusinessLayer.Tools.Pair;
import PersistenceLayer.DAO.Stock.BranchDAO;
import PersistenceLayer.DAO.Stock.ControllerDAO;
import PersistenceLayer.DAO.Suppliers.*;
import PersistenceLayer.DTO.Suppliers.OrderDTO;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SuppliersControllerTest {

    private SuppliersController suppliersController1;
    private SupplierMapper supplierMapper;
    private SupplierContactMapper supplierContactMapper;
    private OrderMapper orderMapper;
    private StockController stockController;


    // private SupplierDTO supplierDTO;
    private int supplier1Id;
    private int BNNumber1;
    private int BankAccount1;
    private PaymentOptions paymentOptions1;
    private String supplierName1;
    private String days;


    private int supplier2Id;
    private int BNNumber2;
    private int BankAccount2;
    private PaymentOptions paymentOptions2;
    private String supplierName2;

    private int supplier3Id;

    private int BNNumber3;
    private int BankAccount3;
    private PaymentOptions paymentOptions3;
    private String supplierName3;


    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        suppliersController1 = new SuppliersController();
        suppliersController1.removeAllData();
        supplierMapper = new SupplierMapper();
        supplierContactMapper = new SupplierContactMapper();
        BranchDAO branchDAO= new BranchDAO();
        branchDAO.removeAll();
        ControllerDAO controllerDAO = new ControllerDAO();
        controllerDAO.removeAll();
//        supplierDTO = new SupplierDTO();
        BranchController branchController= new BranchController();
        try {
            stockController = branchController.addBranch("Branch1", "Ashdod Brahms");
        }
        catch (Exception ex){
            fail();
        }

        supplier1Id = 001;
        BNNumber1 = 100;
        BankAccount1 = 100100100;
        paymentOptions1 = PaymentOptions.plus_60;
        supplierName1 = "David david";

        supplier2Id = 002;
        BNNumber2 = 200;
        BankAccount2 = 200200200;
        paymentOptions2 = PaymentOptions.plus_60;
        supplierName2 = "Nadav nadav";

        supplier3Id = 003;
        BNNumber3 = 230;
        BankAccount3 = 300200200;
        paymentOptions3 = PaymentOptions.plus_60;
        supplierName3 = "David nadav";
        days = "23456";

        suppliersController1.addSupplier(2, false, "", 100,
                100100100, PaymentOptions.plus_60, "david_david", "nadv", "0434324", "asdod");

    }



    @Test
    void testAddAndRemoveSupplier() throws Exception{

        int supplier1Id = 123234239;
        BNNumber1 = 100;
        BankAccount1 = 100100100;
        //paymentOptions1 = PaymentOptions.plus_60;
        supplierName1 = "David david";


        assertEquals(null, suppliersController1.getSupplierMapMap().get(123234239));
        suppliersController1.addSupplier(123234239, true, "1", 100,
                100100100, PaymentOptions.plus_60, "david_david", "nadv", "0434324", "asdod");
        //suppliersController1.removeSupplier(123234239);
        Supplier supplier = suppliersController1.getSupplier(123234239);
        assertEquals(supplier.getSupplierId(), 123234239);


        suppliersController1.removeSupplier(123234239);
        assertEquals(null, suppliersController1.getSupplierMapMap().get(123234239));

    }


    @Test
    /*
    here we test that we can add a contact to supplier and can
    remove a contact from supplier
     */


    void TestAddAndRemoveContactToSupplier(){


     suppliersController1.addSupplier(1232342980, true, "1",100,
             100100100, PaymentOptions.plus_60, "david david", "nadv", "0434324", "asdod");
      Supplier supplier = suppliersController1.getSupplier(1232342980);
       int counter = supplier.getsCard().getSupplierContactMapper().getMaxContactId();
        //Contacts contact = supplier.getsCard().getSpecificContactOne(counter);


        assertEquals(1,supplier.getsCard().getContacts().size());

          supplier.addContact("nadav_david","345678");

        assertEquals(2,supplier.getsCard().getContacts().size());

        supplier.removeContact(counter);

        assertEquals(1,supplier.getsCard().getContacts().size());


    }


    @Test
    /*
    here we test that an order is being added correctly, by one supplier only.
    we test that the one supplier is the one with the lowest price

     */



    void TestAddOrderAllByOneSupplier() throws Exception {

        suppliersController1.addSupplier(supplier1Id,true, days ,BNNumber1,BankAccount1,paymentOptions1, supplierName1, "nadav" , "045345353", "telAviv");
        //suppliersController1.addSupplier(supplier2Id,false, "",BNNumber2,BankAccount2,paymentOptions2, supplierName2, "nadav" , "045345353", "bass");

        Supplier sup1 = suppliersController1.getSupplier(001);
        Supplier sup2 = suppliersController1.getSupplier(002);
        //suppliersController1.addBranch(1,"Branch_1","Ashdod Hertzel blv");

        String productId1 = "1";
        String productId2 = "2";
        String productId3 = "3";
        String productId4 = "4";
        //String productId1 = "bamba";


        sup1.addProductToSupply("1",37,500, 4.9);
        sup1.addProductToSupply(productId2,36,2000, 5.9);
        sup1.addProductToSupply(productId3,35,500, 5.9);
        sup1.addProductToSupply(productId4,34,500, 5.9);

        sup2.addProductToSupply(productId1,37,500, 5.9);
        sup2.addProductToSupply(productId2,36,2000, 5.9);
        sup2.addProductToSupply(productId3,35,500, 5.9);
        sup2.addProductToSupply(productId4,34,500, 5.9);

        DiscountSuppliersPerProduct discount1 = new DiscountSuppliersPerProduct(true  , 200, 33,436,3, "1", new SupplierProductDiscountsMapper() );
        sup1.addDiscountToProduct(productId3,true  , 200, 33,436);
        sup1.addDiscountToProduct(productId4,true  , 200, 33,416);

        sup1.addDiscountToSupplier(true  , 200, 33,43324);
        sup1.addDiscountToSupplier(true  , 200, 33,4363225);


        HashMap<Pair<String, String>,Integer> productsByBranch = new HashMap<>();
        //HashMap<Pair <String,String> productsByIdAndAmount = new HashMap<>();;
        productsByBranch.put(new Pair<String,String>(productId1, "bamaba"),200);
        productsByBranch.put(new Pair<String,String>(productId2, "sprite"),2000);
        productsByBranch.put(new Pair<String,String>(productId3, "cola"),200);
        productsByBranch.put(new Pair<String,String>(productId4, "bamabaNugat"),200);


        assertEquals(0,suppliersController1.totalNumOfOrders());
        //assertEquals(0,suppliersController1.getLastOrder().size());


        suppliersController1.addShortageOrder(productsByBranch , 1);
        assertEquals(1 , suppliersController1.getSupplier(1).numOfOrders());
        assertEquals(0 , suppliersController1.getSupplier(2).numOfOrders());
        assertEquals(1,suppliersController1.totalNumOfOrders());


    }

    @Test

    //here we test that an order is being added correctly, by 3 different suppliers.
    //we test that the one supplier is the one with the lowest price



    void TestAddOrderThatNeedMoreThanOneSupplier() throws Exception{

        suppliersController1 = new SuppliersController();
        suppliersController1.addSupplier(supplier1Id, true, days, BNNumber1, BankAccount1, paymentOptions1, supplierName1, "nadav", "045345353", "telAviv");

        suppliersController1.addSupplier(supplier3Id, false, "", BNNumber3, BankAccount3, paymentOptions3, supplierName3, "nadav", "045345353", "bass");


        Supplier sup1 = suppliersController1.getSupplier(001);
        Supplier sup2 = suppliersController1.getSupplier(002);
        Supplier sup3 = suppliersController1.getSupplier(003);

       // suppliersController1.addBranch(1, "Branch_1", "Ashdod Hertzel blv");



        String pr1 = "9800";
        String pr2 = "9900";
        String pr3 = "250";
        String pr4 = "260";


        sup2.addProductToSupply(pr1, 37, 500, 5.9);
        sup2.addProductToSupply(pr2, 36, 2000, 5.9);
        sup2.addProductToSupply(pr3, 35, 500, 5.9);
        sup3.addProductToSupply(pr4, 34, 500, 7.5);
        sup3.addProductToSupply(pr3, 36, 500, 7.5);


        sup2.addDiscountToProduct(pr3, false, 200, 33, 1);
        sup2.addDiscountToSupplier(true, 2000, 7, 55);
        sup1.addDiscountToSupplier(false, 2000, 100, 546);


        HashMap<Pair<String, String>, Integer> productsByBranch = new HashMap<>();

        productsByBranch.put(new Pair<String, String>(pr1, "bamaba"), 200);
        productsByBranch.put(new Pair<String, String>(pr2, "sprite"), 2000);
        productsByBranch.put(new Pair<String, String>(pr3, "cola"), 600);
        productsByBranch.put(new Pair<String, String>(pr4, "bamabaNugat"), 200);


        assertEquals(0, suppliersController1.totalNumOfOrders());

        suppliersController1.addShortageOrder(productsByBranch, 1);
        assertEquals(2, suppliersController1.totalNumOfOrders());
    }

    @Test
    void SuppliersNextSupplyDayArray(){
        int today = LocalDate.now().getDayOfWeek().getValue();
        Integer in_five_days = (today+5)%7;
        Integer in_three_days = (today+3)%7;
        String supplier1days = in_five_days.toString();
        String supplier5days = in_three_days.toString();

        suppliersController1.addSupplier(supplier1Id,true, supplier1days ,BNNumber1,BankAccount1,paymentOptions1, supplierName1, "nadav" , "045345353", "telAviv");
        suppliersController1.addSupplier(5,true, supplier5days ,BNNumber2,BankAccount2,paymentOptions2, supplierName2, "nadav1" , "04534534453", "telAviv");

        ArrayList<Supplier>[] arraySupplier = suppliersController1.SuppliersNextSupplyDayArray();
        assertEquals(1,arraySupplier[4].get(0).getSupplierId());
        assertEquals(5,arraySupplier[2].get(0).getSupplierId());

    }


    @Test
    void TestSaveConstOrder() throws Exception{

        suppliersController1.addSupplier(supplier1Id,true, days ,BNNumber1,BankAccount1,paymentOptions1, supplierName1, "nadav" , "045345353", "telAviv");
        //suppliersController1.addSupplier(supplier2Id,false, "",BNNumber2,BankAccount2,paymentOptions2, supplierName2, "nadav" , "045345353", "bass");

        Supplier sup1 = suppliersController1.getSupplier(001);
        Supplier sup2 = suppliersController1.getSupplier(002);
       // suppliersController1.addBranch(1,"Branch_1","Ashdod Hertzel blv");

        String productId1 = "1";
        String productId2 = "2";
        String productId3 = "3";
        String productId4 = "4";
        //String productId1 = "bamba";


        sup1.addProductToSupply(productId1,37,500, 4.9);
        sup1.addProductToSupply(productId2,36,2000, 5.9);
        sup1.addProductToSupply(productId3,35,500, 5.9);
        sup1.addProductToSupply(productId4,34,500, 5.9);

        sup2.addProductToSupply(productId1,37,500, 5.9);
        sup2.addProductToSupply(productId2,36,2000, 5.9);
        sup2.addProductToSupply(productId3,35,500, 5.9);
        sup2.addProductToSupply(productId4,34,500, 5.9);

        DiscountSuppliersPerProduct discount1 = new DiscountSuppliersPerProduct(true  , 200, 33,436,3, "1", new SupplierProductDiscountsMapper() );
        sup1.addDiscountToProduct(productId3,true  , 200, 33,436);
        sup1.addDiscountToProduct(productId4,true  , 200, 33,416);

        sup1.addDiscountToSupplier(true  , 200, 33,43324);
        sup1.addDiscountToSupplier(true  , 200, 33,4363225);


        HashMap<Pair<String, String> ,Integer> productsByBranch = new HashMap<>();
        //HashMap<Pair <String,String> productsByIdAndAmount = new HashMap<>();;
        productsByBranch.put(new Pair<String,String>(productId1, "bamaba"),200);
        productsByBranch.put(new Pair<String,String>(productId2, "sprite"),2000);
        productsByBranch.put(new Pair<String,String>(productId3, "cola"),200);
        productsByBranch.put(new Pair<String,String>(productId4, "bamabaNugat"),200);
        assertEquals(0, suppliersController1.totalNumOfOrders());
        List<Integer> days = new ArrayList<>() ;
        days.add(2);
        days.add(4);


        assertEquals(0, suppliersController1.getSupplier(supplier1Id).getConstOrderDetails().size());
        assertEquals(0, suppliersController1.getSupplier(supplier1Id).getConstOrderReservoir().size());
        suppliersController1.addAutomaticOrderConst(1 ,5,days , productsByBranch , false );
        assertEquals(0, suppliersController1.totalNumOfOrders());
        assertEquals(1, suppliersController1.getSupplier(supplier1Id).getConstOrderDetails().size());
        assertEquals(1, suppliersController1.getSupplier(supplier1Id).getConstOrderReservoir().size());
    }

    @Test
    void TestAddConstOrder() throws Exception{

        suppliersController1.addSupplier(supplier1Id,true, days ,BNNumber1,BankAccount1,paymentOptions1, supplierName1, "nadav" , "045345353", "telAviv");
        //suppliersController1.addSupplier(supplier2Id,false, "",BNNumber2,BankAccount2,paymentOptions2, supplierName2, "nadav" , "045345353", "bass");

        Supplier sup1 = suppliersController1.getSupplier(001);
        Supplier sup2 = suppliersController1.getSupplier(002);
        //suppliersController1.addBranch(1,"Branch_1","Ashdod Hertzel blv");

        String productId1 = "1";
        String productId2 = "2";
        String productId3 = "3";
        String productId4 = "4";
        //String productId1 = "bamba";


        sup1.addProductToSupply(productId1,37,500, 4.9);
        sup1.addProductToSupply(productId2,36,2000, 5.9);
        sup1.addProductToSupply(productId3,35,500, 5.9);
        sup1.addProductToSupply(productId4,34,500, 5.9);

        sup2.addProductToSupply(productId1,37,500, 5.9);
        sup2.addProductToSupply(productId2,36,2000, 5.9);
        sup2.addProductToSupply(productId3,35,500, 5.9);
        sup2.addProductToSupply(productId4,34,500, 5.9);

        DiscountSuppliersPerProduct discount1 = new DiscountSuppliersPerProduct(true  , 200, 33,436,3, "1", new SupplierProductDiscountsMapper() );
        sup1.addDiscountToProduct(productId3,true  , 200, 33,436);
        sup1.addDiscountToProduct(productId4,true  , 200, 33,416);

        sup1.addDiscountToSupplier(true  , 200, 33,43324);
        sup1.addDiscountToSupplier(true  , 200, 33,4363225);


        HashMap<Pair<String, String> ,Integer> productsByBranch = new HashMap<>();
        //HashMap<Pair <String,String> productsByIdAndAmount = new HashMap<>();;
        productsByBranch.put(new Pair<String,String>(productId1, "bamaba"),200);
        productsByBranch.put(new Pair<String,String>(productId2, "sprite"),2000);
        productsByBranch.put(new Pair<String,String>(productId3, "cola"),200);
        productsByBranch.put(new Pair<String,String>(productId4, "bamabaNugat"),200);
        assertEquals(0, suppliersController1.totalNumOfOrders());
        List<Integer> days = new ArrayList<>() ;
        int today = LocalDate.now().getDayOfWeek().getValue();
        int tomorrow = ((today+1)%7);

        days.add(tomorrow);


        assertEquals(0, suppliersController1.totalNumOfOrders());
        assertEquals(0, suppliersController1.getSupplier(supplier1Id).getConstOrderDetails().size());
        assertEquals(0, suppliersController1.getSupplier(supplier1Id).getConstOrderReservoir().size());
        suppliersController1.addAutomaticOrderConst(1 ,5,days , productsByBranch, false );
        suppliersController1.alertPeriodOrders1();

        assertEquals(1, suppliersController1.totalNumOfOrders());
        assertEquals(1, suppliersController1.getSupplier(supplier1Id).getConstOrderDetails().size());
        assertEquals(1, suppliersController1.getSupplier(supplier1Id).getConstOrderReservoir().size());
    }











}






