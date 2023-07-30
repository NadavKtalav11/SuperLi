package Tests.Stock;


import BusinessLayer.Stock.Product;
import BusinessLayer.Stock.Purchase;
import BusinessLayer.Stock.StockController;
import PersistenceLayer.DAO.Stock.*;
import PersistenceLayer.DTO.Stock.ItemDTO;
import PersistenceLayer.DTO.Stock.PurchaseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseTest {
    private StockController sc;
    private String purchase;
    private PurchaseDAO purchaseDAO;
    private ItemDAO itemDAO;

    @BeforeEach
    void setUp() {
        ControllerDAO cdao = new ControllerDAO();
        itemDAO = new ItemDAO();
        ProductDAO productDAO = new ProductDAO();
        CategoryDAO catdao = new CategoryDAO(productDAO);
        purchaseDAO = new PurchaseDAO();
        purchaseDAO.removeAll();
        purchaseDAO.removeAllPurchaseItem();
        cdao.removeAll();
        itemDAO.removeAll();
        productDAO.removeAll();
        catdao.removeAll();
        sc = new StockController(1, cdao);
        try {
            sc.addCategory(new ArrayList<>(), "Dairy");
            List<String> chain = new ArrayList<>();
            chain.add("1#1");
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date date = format.parse("06-06-2025");
            sc.addNewProduct("111", "Milk", 10.0, 20.0, "TNUVA", 10, 5, 50, new ArrayList<>(chain));
            sc.addNewProduct("222", "Cheese", 8.5, 20.0, "EMEK", 10, 5, 50, new ArrayList<>(chain));
            sc.receiveSupply("111", 5, 5, date);
            sc.receiveSupply("222", 10, 20, date);
            purchase = sc.addPurchase();
        }
        catch (Exception ex){
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    void purchaseItemTest() {
        try {
            sc.purchaseItem(purchase, "1#111-1");
            sc.purchaseItem(purchase, "1#111-6");
            for (ItemDTO idto: itemDAO.getItems("1#111")) {
                assertNotEquals(idto.getITEM_ID(), "1#111-1");
                assertNotEquals(idto.getITEM_ID(), "1#111-6");
            }
            assertThrows(IllegalArgumentException.class, () -> sc.purchaseItem(purchase, "1#111-16"));
            assertThrows(IllegalArgumentException.class, () -> sc.purchaseItem("1#222", "1#111-5"));
        }
        catch (Exception ex){
            ex.printStackTrace();
            fail();
        }
    }

}
