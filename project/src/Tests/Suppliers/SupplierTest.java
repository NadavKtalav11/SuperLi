package Tests.Suppliers;

import BusinessLayer.Suppliers.*;

import BusinessLayer.Tools.Pair;
import PersistenceLayer.DAO.Suppliers.ProductQuantitiesAndPriceMapper;
import PersistenceLayer.DTO.Suppliers.ProductQuantitiesAndPriceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SupplierTest {

    private SuppliersController suppliersController1;
    private int supplier1Id;
    //private DeliveryTerms deliveryTerms1;
    private int BNNumber1;
    private int BankAccount1;
    private PaymentOptions paymentOptions1;
    private String supplierName1;


    private int supplier2Id;
    //private DeliveryTerms deliveryTerms2;
    private int BNNumber2;
    private int BankAccount2;
    private PaymentOptions paymentOptions2;
    private String supplierName2;
    private ProductQuantitiesAndPriceMapper productQuantitiesAndPriceMapper;


    @BeforeEach
    void setUp() {
        suppliersController1 = new SuppliersController();
        suppliersController1.removeAllData();
        supplier1Id = 001;
        BNNumber1 = 100;
        BankAccount1 = 100100100;
        paymentOptions1 = PaymentOptions.plus_60;
        supplierName1 = "David david";
        productQuantitiesAndPriceMapper = new ProductQuantitiesAndPriceMapper();

        suppliersController1.addSupplier(supplier1Id,true , "236",BNNumber1,BankAccount1,paymentOptions1, supplierName1, "nadav" , "045345353", "dizingoff");


        //deliveryTerms2 = DeliveryTerms.delivery_By_Order;
        supplier2Id = 002;
        BNNumber2 = 200;
        BankAccount2 = 200200200;
        paymentOptions2 = PaymentOptions.plus_60;
        supplierName2 = "Nadav nadav";

        suppliersController1.addSupplier(supplier2Id,false, "",BNNumber2,BankAccount2,paymentOptions2, supplierName2, "nadav" , "045345353", "belgrad");

    }

    @Test
    // here we test adding product to supplier and removing it
    void TestAddAndRemoveProductToSupply() {

        Supplier sup1 = suppliersController1.getSupplier(001);

        assertEquals(0,sup1.getProductsByCatalogNum().size());
        assertEquals(0,sup1.getProductsQuantities().size());
        assertEquals(0,sup1.getProductsPrice().size());

        ProductQuantitiesAndPriceDTO productQuantitiesAndPriceDTO= productQuantitiesAndPriceMapper.selectProductBySupplier(1,"78");
        assertEquals(null, productQuantitiesAndPriceDTO);

        suppliersController1.addProductToSupplier(1, "78",77, 1977, 1);
        suppliersController1.addProductToSupplier(1, "12",3, 192, 6.7);
        suppliersController1.addProductToSupplier(1, "14",23, 197227, 16.3);

        ProductQuantitiesAndPriceDTO productQuantitiesAndPriceDTO1= productQuantitiesAndPriceMapper.selectProductBySupplier(1,"78");
        assertEquals("78",productQuantitiesAndPriceDTO1.getProductId());
        assertFalse(productQuantitiesAndPriceDTO1.getProductId()=="79");


        assertEquals(3,sup1.getProductsByCatalogNum().size());
        assertEquals(3,sup1.getProductsQuantities().size());
        assertEquals(3,sup1.getProductsPrice().size());

        sup1.removeProduct("78");
        assertEquals(2,sup1.getProductsByCatalogNum().size());
        assertEquals(2,sup1.getProductsQuantities().size());
        assertEquals(2,sup1.getProductsPrice().size());
    }

    @Test
        // here we test updating a price to a product that the supplier has
    void TestUpdateProductPrice() {
        Supplier sup1 = suppliersController1.getSupplier(001);
        //suppliersController1.a(9800,"Bamba", "Elit");

        //SupplierProduct pr1 = suppliersController1.getSystemProduct().get(9800);
        sup1.addProductToSupply("76",37,500, 5.9);

        assertEquals(5.9, sup1.getProductsPrice().get("76"));

        sup1.updateProductPrice("76",5.95);
        assertEquals(5.95, sup1.getProductsPrice().get("76"));

    }


    @Test
        // here we test adding a discount to a product that the supplier has
        // and removing it.
    void TestAddDiscountToProduct() {
        Supplier sup1 = suppliersController1.getSupplier(001);
        //suppliersController1.addProductToTheSystem(9800,"Bamba", "Elit");

        //SupplierProduct pr1 = suppliersController1.getSystemProduct().get(9800);
        sup1.addProductToSupply("76",37,500, 5.9);


        assertEquals(0,sup1.getProductsDiscount().get("76").size());

        //Discount discount11 = new Discount(Discount.DiscountType.buy_amount_get_discount_Per_product, 200, 10 , 1);

        sup1.addDiscountToProduct("76" , true ,200,10, 1);
        assertEquals(1,sup1.getProductsDiscount().get("76").size());

        sup1.removeDiscountToProduct("76", 1);
        assertEquals(0,sup1.getProductsDiscount().get("76").size());

    }

    @Test
        // here we test that the discounts to a product are being sorted from the smallest
        // to the biggest.
    void TestSortDiscounts() {

        Supplier sup1 = suppliersController1.getSupplier(001);
        //suppliersController1.addProductToTheSystem(9800,"Bamba", "Elit");

        //SupplierProduct pr1 = suppliersController1.getSystemProduct().get(9800);
        sup1.addProductToSupply("76",37,500, 5.9);


        assertEquals(0,sup1.getProductsDiscount().get("76").size());

        //Discount discount11 = new Discount(Discount.DiscountType.buy_amount_get_PercentDiscount_perProduct, 200, 20 ,1);
        //Discount discount12 = new Discount(Discount.DiscountType.buy_amount_get_discount_Per_product, 130, 39, 133);
        //Discount discount13 = new Discount(Discount.DiscountType.buy_amount_get_discount_Per_product, 199, 50, 314);

        sup1.addDiscountToProduct("76",false,200,20 ,2);
        sup1.addDiscountToProduct("76",true, 130 , 39 ,3);
        sup1.addDiscountToProduct("76",true, 199,50 ,314);

        assertEquals(130,sup1.getProductsDiscount().get("76").get(0).getAmount());
        assertEquals(199,sup1.getProductsDiscount().get("76").get(1).getAmount());
        assertEquals(200,sup1.getProductsDiscount().get("76").get(2).getAmount());
    }

    @Test
        // here we test that we check correctly which supplier can supply all by himself
        // an entire order.
    void TestIsSupplyAllTheProducts() {

        Supplier sup1 = suppliersController1.getSupplier(001);
        Supplier sup2 = suppliersController1.getSupplier(002);

        //suppliersController1.addProductToTheSystem(9800,"Bamba", "Elit");
        //suppliersController1.addProductToTheSystem(9900,"Doritos", "Elit");
        //suppliersController1.addProductToTheSystem(250,"Coca-Cola Zero", "Coca-Cola");
        //suppliersController1.addProductToTheSystem(260,"Sprite zero", "Coca-Cola");

        //SupplierProduct pr1 = suppliersController1.getSystemProduct().get(9800);
        //SupplierProduct pr2 = suppliersController1.getSystemProduct().get(9900);
        //SupplierProduct pr3 = suppliersController1.getSystemProduct().get(250);
        //SupplierProduct pr4 = suppliersController1.getSystemProduct().get(260);

        Map<String,Integer> productsToSupply = new HashMap<>();
        productsToSupply.put("9800",200);
        productsToSupply.put("9900",2000);
        productsToSupply.put("250",200);
        productsToSupply.put("260",200);

        sup1.addProductToSupply("9800",37,500, 5.9);
        sup1.addProductToSupply("9900",36,2000, 5.9);
        sup1.addProductToSupply("250",35,500, 5.9);
        sup1.addProductToSupply("260",34,500, 5.9);

        sup2.addProductToSupply("9800",37,500, 5.9);
        sup2.addProductToSupply("9900",36,500, 5.9);
        sup2.addProductToSupply("250",35,500, 5.9);
        sup2.addProductToSupply("260",34,500, 5.9);

        assertTrue(sup1.isSupplyAllTheProducts(productsToSupply));
        assertFalse(sup2.isSupplyAllTheProducts(productsToSupply));

    }

    @Test
        // here we test that get the right price for all the order from a supplier
        // we check that the discounts are taken into account.
    void getPriceForAllOrder() {
        Supplier sup1 = suppliersController1.getSupplier(001);
        Supplier sup2 = suppliersController1.getSupplier(002);

        String pr1 = "9800";
        String pr2 = "9900";
        String pr3 = "250";
        String pr4 = "260";
        Map<String ,Integer> productsToSupply = new HashMap<>();
        productsToSupply.put(pr1,200);
        productsToSupply.put(pr2,2000);
        productsToSupply.put(pr3,200);
        productsToSupply.put(pr4,200);

        sup1.addProductToSupply(pr1,37,500, 4.9);
        sup1.addProductToSupply(pr2,36,2000, 5.9);
        sup1.addProductToSupply(pr3,35,500, 5.9);
        sup1.addProductToSupply(pr4,34,500, 5.9);

        sup2.addProductToSupply(pr1,37,500, 5.9);
        sup2.addProductToSupply(pr2,36,2000, 5.9);
        sup2.addProductToSupply(pr3,35,500, 5.9);
        sup2.addProductToSupply(pr4,34,500, 5.9);

        //System.out.println(sup1.getPriceForAllOrder(productsToSupply));
       // System.out.println(sup2.getPriceForAllOrder(productsToSupply));
       assertEquals(15140,sup1.getPriceForAllOrder(productsToSupply) );
        assertEquals(15340,sup2.getPriceForAllOrder(productsToSupply) );

        sup1.addDiscountToProduct(pr3,false, 200, 3 , 1);
        sup1.addDiscountToProduct(pr4,false , 200 , 3 ,2);

        assertEquals(13940,sup1.getPriceForAllOrder(productsToSupply) );


        sup2.addDiscountToSupplier(true, 2000, 7, 3242);
        assertEquals(14266.2,sup2.getPriceForAllOrder(productsToSupply) );

        sup1.addDiscountToSupplier(false, 2000, 100 ,3242);
        assertEquals(13840,sup1.getPriceForAllOrder(productsToSupply) );

        sup2.addDiscountToSupplier(true, 3000, 11, 23332);
        assertEquals(13652.6,sup2.getPriceForAllOrder(productsToSupply) );

    }

    void getPriceForAllOrderNoDiscount() {
        Supplier sup1 = suppliersController1.getSupplier(001);
        Supplier sup2 = suppliersController1.getSupplier(002);

        String pr1 = "9800";
        String pr2 = "9900";
        String pr3 = "250";
        String pr4 = "260";
        Map<String ,Integer> productsToSupply = new HashMap<>();
        productsToSupply.put(pr1,200);
        productsToSupply.put(pr2,2000);
        productsToSupply.put(pr3,200);
        productsToSupply.put(pr4,200);

        sup1.addProductToSupply(pr1,37,500, 4.9);
        sup1.addProductToSupply(pr2,36,2000, 5.9);
        sup1.addProductToSupply(pr3,35,500, 5.9);
        sup1.addProductToSupply(pr4,34,500, 5.9);

        sup2.addProductToSupply(pr1,37,500, 5.9);
        sup2.addProductToSupply(pr2,36,2000, 5.9);
        sup2.addProductToSupply(pr3,35,500, 5.9);
        sup2.addProductToSupply(pr4,34,500, 5.9);

        //System.out.println(sup1.getPriceForAllOrder(productsToSupply));
        //System.out.println(sup2.getPriceForAllOrder(productsToSupply));
        assertEquals(15140,sup1.getPriceForAllOrder(productsToSupply) );
        assertEquals(15340,sup2.getPriceForAllOrder(productsToSupply) );

        sup1.addDiscountToProduct(pr3,false, 212300, 3 , 1);
        sup1.addDiscountToProduct(pr4,false , 201110 , 3 ,2);

        assertEquals(15140,sup1.getPriceForAllOrder(productsToSupply) );


        sup2.addDiscountToSupplier(true, 2333000, 7, 3242);
        assertEquals(15340,sup2.getPriceForAllOrder(productsToSupply) );

        sup1.addDiscountToSupplier(false, 2003230, 100 ,3242);
        assertEquals(15140,sup1.getPriceForAllOrder(productsToSupply) );

        sup2.addDiscountToSupplier(true, 3023200, 11, 23332);
        assertEquals(15340,sup2.getPriceForAllOrder(productsToSupply) );

    }
    void TestSaveConstOrder() {

        Supplier sup1 = suppliersController1.getSupplier(001);
        //suppliersController1.addProductToTheSystem(9800,"Bamba", "Elit");

        //SupplierProduct pr1 = suppliersController1.getSystemProduct().get(9800);
        sup1.addProductToSupply("76",37,500, 5.9);


        assertEquals(0,sup1.getProductsDiscount().get("76").size());

        sup1.addDiscountToProduct("76",false,200,20 ,2);
        sup1.addDiscountToProduct("76",true, 130 , 39 ,3);
        sup1.addDiscountToProduct("76",true, 199,50 ,314);


        List<Integer> days = new ArrayList<>();
        days.add(1);
        days.add(4);
        days.add(7);

        String pr1 = "9800";
        String pr2 = "9900";
        String pr3 = "250";
        String pr4 = "260";


        HashMap<Pair<String,String> ,Integer> productsToSupply = new HashMap<>();
        productsToSupply.put(new Pair<String,String>(pr1,"bamba"), 200);
        productsToSupply.put(new Pair<String,String>(pr2,"Bisli"),2000);
        productsToSupply.put(new Pair<String,String>(pr3,"cola"),200);
        productsToSupply.put(new Pair<String,String>(pr4,"sprite"),200);

        assertEquals(0, sup1.getConstOrderDetails().size());
        sup1.saveConstOrderForSupplier(3,days ,productsToSupply, 1,"petachTikva", true);
        //assertEquals(130,sup1().get("76").get(0).getAmount());
        assertEquals(1, sup1.getConstOrderDetails().size());



    }





}