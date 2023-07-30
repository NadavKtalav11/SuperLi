package Tests.Stock;
import BusinessLayer.Stock.Item;
import BusinessLayer.Stock.Product;
import PersistenceLayer.DAO.Stock.ItemDAO;
import PersistenceLayer.DAO.Stock.ProductDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.text.SimpleDateFormat;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;
    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        ProductDAO pdao = new ProductDAO();
        ItemDAO idao = new ItemDAO();
        pdao.removeAll();
        idao.removeAll();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try{
            date = format.parse("06-06-2025");
        }
        catch (Exception ex){
            fail();
        }
        product = new Product("0#111", "Milk", 10.0, 20.0, "TNUVA", 100, 5, 50, new ArrayList<>(),pdao);
        try {
            pdao.addProduct("0#111", "Milk", 10.0, 20.0, "TNUVA", 100, 5, 50,"");
            product.receiveSupply(1, 1, date);
        }
        catch (Exception ex){
            fail();
        }

    }


    @Test
    public void testGettersAndSetters() {
        assertEquals("0#111", product.getProductID());
        assertEquals("Milk", product.getName());
        assertEquals(1, product.getStoreAmount());
        assertEquals(1, product.getWarehouseAmount());
        assertEquals(2, product.getTotalAmount());
        assertEquals(0, product.getDamagedAmount());
        assertEquals(10.0, product.getPurchasePrice());
        assertEquals(20.0, product.getSellingPrice());
        assertEquals("TNUVA", product.getManufacture());
        assertEquals(100, product.getDemand());
        assertEquals(5, product.getSupplyTime());
        assertEquals(50, product.getNotificationAmount());
        assertEquals(Arrays.asList(), product.getCategoryIDChain());

        product.setStoreAmount(100);
        assertEquals(100, product.getStoreAmount());
        assertEquals(101, product.getTotalAmount());

        product.setWarehouseAmount(50);
        assertEquals(50, product.getWarehouseAmount());
        assertEquals(150, product.getTotalAmount());

        product.setPurchasePrice(12.0);
        assertEquals(12.0, product.getPurchasePrice());

        product.setSellingPrice(25.0);
        assertEquals(25.0, product.getSellingPrice());

        product.setManufacture("Manufacturer 2");
        assertEquals("Manufacturer 2", product.getManufacture());

        product.setDemand(200);
        assertEquals(200, product.getDemand());
        assertEquals(50, product.getNotificationAmount());

        product.setSupplyTime(10);
        assertEquals(10, product.getSupplyTime());
        assertEquals(2200, product.getNotificationAmount());
    }

    @Test
    public void testGenerateItemID() {
        // Test generateItemID() method
        assertEquals("0#111-3", product.generateItemID());
        assertEquals("0#111-4", product.generateItemID());
        assertEquals("0#111-5", product.generateItemID());
    }

    @Test
    void testRemoveItem() {
        // Check that both Items are in the Product's itemList
        item1 = product.getItemByID("0#111-1");
        item2 = product.getItemByID("0#111-2");
        assertTrue(product.getItemList().containsKey(item1.getItemID()));
        assertTrue(product.getItemList().containsKey(item2.getItemID()));

        // Remove one of the Items
        try{
            product.removeItem(item1.getItemID());
        }
        catch (Exception ex){
            fail();
        }

        // Check that the removed Item is no longer in the itemList
        assertFalse(product.getItemList().containsKey(item1.getItemID()));
        assertTrue(product.getItemList().containsKey(item2.getItemID()));

        // Try to remove an Item that doesn't exist
        assertThrows(NoSuchElementException.class, () -> {
            product.removeItem("0#111-3");
        });
    }

    @Test
    void testSetItemAsDamaged() {
        // Check that neither Item is damaged
        item1 = product.getItemByID("0#111-1");
        assertFalse(product.getDamagedList().containsKey(Item.DamageType.BROKEN));
        assertFalse(product.getDamagedList().containsKey(Item.DamageType.EXPIRED));

        // Set one of the Items as damaged
        product.setItemAsDamaged(item1.getItemID(), Item.DamageType.BROKEN);

        // Check that the damaged Item is in the damagedList for the correct damage type
        assertTrue(product.getDamagedList().containsKey(Item.DamageType.BROKEN));
        assertTrue(product.getDamagedList().get(Item.DamageType.BROKEN).contains(item1.getItemID()));
        assertFalse(product.getDamagedList().containsKey(Item.DamageType.EXPIRED));

        // Try to set an Item as damaged that doesn't exist
        assertThrows(IllegalArgumentException.class, () -> {
            product.setItemAsDamaged("0#111-3", Item.DamageType.BROKEN);
        });
    }
}