package Tests.Stock;

import static org.junit.jupiter.api.Assertions.*;


import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import BusinessLayer.Stock.BranchController;
import BusinessLayer.Tools.Pair;
import BusinessLayer.Stock.StockController;
import BusinessLayer.Suppliers.PaymentOptions;
import BusinessLayer.Suppliers.Supplier;
import BusinessLayer.Suppliers.SuppliersController;
import PersistenceLayer.DAO.Stock.*;
import PersistenceLayer.DAO.Suppliers.OrderConstMapper;
import PersistenceLayer.DAO.Suppliers.OrderMapper;
import PersistenceLayer.DTO.Suppliers.OrderConstDTO;
import PersistenceLayer.DTO.Suppliers.OrderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import BusinessLayer.Stock.Category;
import BusinessLayer.Stock.Discount.DiscountType;
import BusinessLayer.Stock.Product;
import java.lang.NullPointerException;

    class OrderTests {

        private OrderMapper orderMapper;
        private OrderConstMapper orderConstMapper;
        private Product product1;
        private Product product2;
        private StockController sc;
        private BranchController branchController;

        private SuppliersController suppliersController1;

        @BeforeEach
        public void setUp() {

            suppliersController1 =new SuppliersController();
            suppliersController1.removeAllData();
            ProductDAO pdao = new ProductDAO();
            CategoryDAO cdao = new CategoryDAO(pdao);
            ControllerDAO controllerDAO = new ControllerDAO();
            PurchaseDAO purchaseDAO = new PurchaseDAO();
            BranchDAO branchDAO = new BranchDAO();
            ItemDAO itemDAO = new ItemDAO();
            cdao.removeAll();
            pdao.removeAll();
            controllerDAO.removeAll();
            controllerDAO.removeAll();
            purchaseDAO.removeAll();
            itemDAO.removeAll();
            branchDAO.removeAll();
            branchController = new BranchController();
            orderMapper = new OrderMapper();
            orderConstMapper = new OrderConstMapper();
            try {
                sc = branchController.addBranch("branch_1", "Ashdod");
                sc.addCategory(new ArrayList<>(), "Fruit");
                List<String> chain = new ArrayList<>();
                chain.add("1#1");
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                Date date = format.parse("06-06-2025");
                sc.addNewProduct("111", "Apple", 4.5, 8, "Amit", 5, 3, 30, new ArrayList<>(chain));
                sc.addNewProduct("222", "Banana", 4.5, 8, "Amit", 2, 3, 40, new ArrayList<>(chain));
                sc.receiveSupply("111", 3, 3, date);
            }
            catch (Exception ex){
                ex.printStackTrace();
                fail();
            }
        }

        @Test
        public void testExpressOrder() throws Exception {
            Map<Pair<String, String>, Integer> productsToOrder = new HashMap<>();
            Pair<String, String> pair1 = new Pair<>("111","Apple");
            Pair<String, String> pair2 = new Pair<>("222","Banana");

            productsToOrder.put(pair1,100);
            productsToOrder.put(pair2,200);

            List<OrderDTO> orderDTO = orderMapper.selectAllOrders();
            assertEquals(0,orderDTO.size());


            int supplier1Id = 001;
            int BNNumber1 = 100;
            int BankAccount1 = 100100100;
            PaymentOptions paymentOptions1 = PaymentOptions.plus_60;
            String supplierName1 = "David david";

            int supplier2Id = 002;
            int BNNumber2 = 200;
            int BankAccount2 = 200200200;
            PaymentOptions paymentOptions2 = PaymentOptions.plus_60;
            String supplierName2 = "Nadav nadav";

            suppliersController1 = new SuppliersController();
            suppliersController1.addSupplier(supplier1Id,true, "" ,BNNumber1,BankAccount1,paymentOptions1, supplierName1, "nadav" , "045345353", "telAviv");
            suppliersController1.addSupplier(supplier2Id,true, "" ,BNNumber2,BankAccount2,paymentOptions2, supplierName2, "nadav1" , "04534534453", "telAviv");

            Supplier supplier1 = suppliersController1.getSupplier(1);
            Supplier supplier2 = suppliersController1.getSupplier(2);

            supplier1.addProductToSupply("111", 37, 500, 222223.9);
            supplier1.addProductToSupply("222",38,333,3.4);
            supplier2.addProductToSupply("111", 34, 654334,223.3);
            supplier2.addProductToSupply("222",38,333,3.4);


            assertEquals(true,sc.addExpressOrder(productsToOrder));

            List<OrderDTO> orderDTO1 = orderMapper.selectAllOrders();
            assertEquals(1,orderDTO1.size());


        }

        @Test
        public void testExpressOrderAfterBuy() throws Exception {
            //Map<Pair<String, String>, Integer> productsToOrder = new HashMap<>();
            //Pair<String, String> pair1 = new Pair<>("111","Apple");
            //Pair<String, String> pair2 = new Pair<>("222","Banana");

            String purchaseID = sc.addPurchase();
            sc.purchaseItem(purchaseID, "1#111-1");


            List<OrderDTO> orderDTO = orderMapper.selectAllOrders();
            assertEquals(0,orderDTO.size());


            int supplier1Id = 001;
            int BNNumber1 = 100;
            int BankAccount1 = 100100100;
            PaymentOptions paymentOptions1 = PaymentOptions.plus_60;
            String supplierName1 = "David david";

            int supplier2Id = 002;
            int BNNumber2 = 200;
            int BankAccount2 = 200200200;
            PaymentOptions paymentOptions2 = PaymentOptions.plus_60;
            String supplierName2 = "Nadav nadav";

            suppliersController1 = new SuppliersController();
            suppliersController1.addSupplier(supplier1Id,true, "" ,BNNumber1,BankAccount1,paymentOptions1, supplierName1, "nadav" , "045345353", "telAviv");
            suppliersController1.addSupplier(supplier2Id,true, "" ,BNNumber2,BankAccount2,paymentOptions2, supplierName2, "nadav1" , "04534534453", "telAviv");

            Supplier supplier1 = suppliersController1.getSupplier(1);
            Supplier supplier2 = suppliersController1.getSupplier(2);

            supplier1.addProductToSupply("111", 37, 500, 222223.9);
            supplier1.addProductToSupply("222",38,333,3.4);
            supplier2.addProductToSupply("111", 34, 654334,223.3);
            supplier2.addProductToSupply("222",38,333,3.4);

            int numOfOrders1 = suppliersController1.getSupplier(1).numOfOrders();
            int numOfOrders2 = suppliersController1.getSupplier(2).numOfOrders();
            assertEquals(0, numOfOrders1);
            assertEquals(0, numOfOrders2);
            sc.sendDeficiencyOrderToSuppliers(purchaseID);
            numOfOrders1 = suppliersController1.getSupplier(1).numOfOrders();
            numOfOrders2 = suppliersController1.getSupplier(2).numOfOrders();
            assertEquals(0, numOfOrders1);
            assertEquals(1, numOfOrders2);
            List<OrderDTO> orderDTO1 = orderMapper.selectAllOrders();
            assertEquals(1,orderDTO1.size());

        }


        @Test
        public void testAddConstOrderStock() throws Exception {
            //Map<Pair<String, String>, Integer> productsToOrder = new HashMap<>();
            //Pair<String, String> pair1 = new Pair<>("111","Apple");
            //Pair<String, String> pair2 = new Pair<>("222","Banana");

            List<Integer> supplyDays = new ArrayList<>();
            int today = LocalDate.now().getDayOfWeek().getValue();

            supplyDays.add((today + 1) % 7);
            supplyDays.add((today + 3) % 7);
            String supplierDays = "";
            for (Integer int1: supplyDays){
                supplierDays = supplierDays + int1;
            }
            Pair<String, String> pair = new Pair<>("111", "Apple");
            Map<Pair<String, String>, Integer> productsToOrder = new HashMap<>();
            productsToOrder.put(pair, 30);


            List<OrderDTO> orderDTO = orderMapper.selectAllOrders();
            assertEquals(0,orderDTO.size());


            int supplier1Id = 001;
            int BNNumber1 = 100;
            int BankAccount1 = 100100100;
            PaymentOptions paymentOptions1 = PaymentOptions.plus_60;
            String supplierName1 = "David david";

            int supplier2Id = 002;
            int BNNumber2 = 200;
            int BankAccount2 = 200200200;
            PaymentOptions paymentOptions2 = PaymentOptions.plus_60;
            String supplierName2 = "Nadav nadav";

            suppliersController1 = new SuppliersController();
            suppliersController1.addSupplier(supplier1Id,true, "0123456" ,BNNumber1,BankAccount1,paymentOptions1, supplierName1, "nadav" , "045345353", "telAviv");
            suppliersController1.addSupplier(supplier2Id,true, supplierDays ,BNNumber2,BankAccount2,paymentOptions2, supplierName2, "nadav1" , "04534534453", "telAviv");

            Supplier supplier1 = suppliersController1.getSupplier(1);
            Supplier supplier2 = suppliersController1.getSupplier(2);

            supplier1.addProductToSupply("111", 37, 500, 222223.9);
            supplier1.addProductToSupply("222",38,333,3.4);
            supplier2.addProductToSupply("111", 34, 654334,223.3);
            supplier2.addProductToSupply("222",38,333,3.4);

            int numOfOrders1 = suppliersController1.getSupplier(1).numOfOrders();
            int numOfOrders2 = suppliersController1.getSupplier(2).numOfOrders();
            assertEquals(0, numOfOrders1);
            assertEquals(0, numOfOrders2);
            assertEquals(0,suppliersController1.getSupplier(1).getConstOrderReservoir().size());
            //sc.sendDeficiencyOrderToSuppliers(purchaseID);
            sc.addAutomaticOrder(supplyDays, productsToOrder);
            numOfOrders1 = suppliersController1.getSupplier(1).numOfOrders();
            numOfOrders2 = suppliersController1.getSupplier(2).numOfOrders();
            assertEquals(0, numOfOrders1);
            assertEquals(0, numOfOrders2);

            List<OrderDTO> orderDTO1 = orderMapper.selectAllOrders();
            assertEquals(0,orderDTO1.size());


        }
        @Test
        public void updateAutomaticOrderProductsAndAmount() throws Exception{
            Map<Pair<String, String>, Integer> productsToOrder = new HashMap<>();
            Pair<String, String> pair1 = new Pair<>("111","Apple");
            Pair<String, String> pair2 = new Pair<>("222","Banana");

            List<Integer> supplyDays = new ArrayList<>();
            int today = LocalDate.now().getDayOfWeek().getValue();
            Integer day1;
            if((today+2)%7==0){
                day1 =6;
            }
            else{
                day1 = (today+2)%7;
            }
            supplyDays.add(day1);
            Integer day7;
            if((today+3)%7==0){
                day7 =6;
            }
            else{
                day7 = (today+3)%7;
            }
            supplyDays.add(day7);
            String supplierDays = "";
            for (Integer int1: supplyDays){
                supplierDays = supplierDays + int1;
            }
            productsToOrder.put(pair1, 30);
            productsToOrder.put(pair2, 50);

            List<OrderConstDTO> orderConstDTOS = orderConstMapper.selectAllOrders();
            assertEquals(0,orderConstDTOS.size());


            int supplier1Id = 001;
            int BNNumber1 = 100;
            int BankAccount1 = 100100100;
            PaymentOptions paymentOptions1 = PaymentOptions.plus_60;
            String supplierName1 = "David david";


            suppliersController1 = new SuppliersController();
            suppliersController1.addSupplier(supplier1Id,true, "0123456" ,BNNumber1,BankAccount1,paymentOptions1, supplierName1, "nadav" , "045345353", "telAviv");

            Supplier supplier1 = suppliersController1.getSupplier(1);

            supplier1.addProductToSupply("111", 37, 500, 222223.9);
            supplier1.addProductToSupply("222",38,333,3.4);


            int numOfOrders1 = suppliersController1.getSupplier(1).numOfOrders();
            assertEquals(0, numOfOrders1);
            //sc.sendDeficiencyOrderToSuppliers(purchaseID);
            sc.addAutomaticOrder(supplyDays, productsToOrder);
            numOfOrders1 = suppliersController1.getSupplier(1).numOfOrders();
            assertEquals(0, numOfOrders1);

            List<OrderConstDTO> orderConstDTOS1 = orderConstMapper.selectAllOrders();
            int orderId =orderConstDTOS1.get(0).getOrderId() *-1;
            Map<Pair<String, String>, Integer> productsAmount = sc.getOrderProductsMap(orderId);

            assertEquals(2,productsAmount.size());
            assertEquals(30,productsAmount.get(pair1));
            assertEquals(50,productsAmount.get(pair2));

            Map<Pair<String, String>, Integer> productsToOrder1 = new HashMap<>();
            Pair<String, String> pair3 = new Pair<>("111","Apple");
            Pair<String, String> pair4 = new Pair<>("222","Banana");
            productsToOrder1.put(pair3,100);
            productsToOrder1.put(pair4,100);
             sc.updateOrderProductsAndAmounts(orderId,productsToOrder1);

             List<OrderConstDTO> orderConstDTOS2 = orderConstMapper.selectAllOrders();
            assertEquals(1,orderConstDTOS2.size());

            Map<Pair<String, String>, Integer> productsAmount1 = sc.getOrderProductsMap(orderId);

            assertEquals(100,productsAmount1.get(pair3));
            assertEquals(100,productsAmount1.get(pair4));
        }
        @Test
        public void updateAutomaticOrderDays() throws Exception{
                Map<Pair<String, String>, Integer> productsToOrder = new HashMap<>();
                Pair<String, String> pair1 = new Pair<>("111","Apple");
                Pair<String, String> pair2 = new Pair<>("222","Banana");

                List<Integer> supplyDays = new ArrayList<>();
                int today = LocalDate.now().getDayOfWeek().getValue();
                Integer day1;
                if((today+2)%7==0){
                    day1 =6;
                }
                else{
                    day1 = (today+2)%7;
                }
                supplyDays.add(day1);
                Integer day7;
                if((today+3)%7==0){
                    day7 =6;
                }
                else{
                    day7 = (today+3)%7;
                }
                supplyDays.add(day7);
                String supplierDays = "";
                for (Integer int1: supplyDays){
                    supplierDays = supplierDays + int1;
                }
                productsToOrder.put(pair1, 30);
                productsToOrder.put(pair2, 50);

                List<OrderConstDTO> orderConstDTOS = orderConstMapper.selectAllOrders();
                assertEquals(0,orderConstDTOS.size());


                int supplier1Id = 001;
                int BNNumber1 = 100;
                int BankAccount1 = 100100100;
                PaymentOptions paymentOptions1 = PaymentOptions.plus_60;
                String supplierName1 = "David david";


                suppliersController1 = new SuppliersController();
                suppliersController1.addSupplier(supplier1Id,true, "0123456" ,BNNumber1,BankAccount1,paymentOptions1, supplierName1, "nadav" , "045345353", "telAviv");

                Supplier supplier1 = suppliersController1.getSupplier(1);

                supplier1.addProductToSupply("111", 37, 500, 222223.9);
                supplier1.addProductToSupply("222",38,333,3.4);


                int numOfOrders1 = suppliersController1.getSupplier(1).numOfOrders();
                assertEquals(0, numOfOrders1);
                sc.addAutomaticOrder(supplyDays, productsToOrder);
                numOfOrders1 = suppliersController1.getSupplier(1).numOfOrders();
                assertEquals(0, numOfOrders1);

            List<OrderConstDTO> orderConstDTOS1 = orderConstMapper.selectAllOrders();
            int orderId =orderConstDTOS1.get(0).getOrderId() *-1;

            List<Integer> days = new ArrayList<>();
            String daysOfWeek = orderConstDTOS1.get(0).getDays();

            for (int i = 0; i < daysOfWeek.length(); i++) {
                int digit = Character.getNumericValue(daysOfWeek.charAt(i));
                days.add(digit);
            }

            Integer day3;
            if((today+4)%7==0){
                day3 =7;
            }
            else{
                day3 = (today+4)%7;
            }
            assertTrue(days.contains(day1));
            assertTrue(days.contains(day7));
            assertTrue(!days.contains(day3));

            List<Integer> newDays = new ArrayList<>();
            newDays.add(day3);

            sc.updateOrderSupplyDays(orderId, newDays);

            List<OrderConstDTO> orderConstDTOS2 = orderConstMapper.selectAllOrders();
            String daysOfWeek1 = orderConstDTOS2.get(0).getDays();

            List<Integer> newDaysOfWeek = new ArrayList<>();

            for (int i = 0; i < daysOfWeek1.length(); i++) {
                int digit = Character.getNumericValue(daysOfWeek1.charAt(i));
                newDaysOfWeek.add(digit);
            }

            assertTrue(newDaysOfWeek.contains(day3));
           assertTrue(!newDaysOfWeek.contains(day1));
            assertTrue(!newDaysOfWeek.contains(day7));


        }



    }
