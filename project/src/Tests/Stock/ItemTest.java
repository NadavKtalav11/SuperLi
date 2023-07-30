package Tests.Stock;

import BusinessLayer.Stock.Item;
import PersistenceLayer.DAO.Stock.ItemDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;

class ItemTest {
    Item item;

    @BeforeEach
    void setUp() {
        ItemDAO idao = new ItemDAO();
        idao.removeAll();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try{
            date = format.parse("06-06-2025");
        }
        catch (Exception ex){
            fail();
        }
        item = new Item("0#2-123", date, Item.Location.WAREHOUSE, 10.0, 20.0, idao);
        try {
            idao.addItem("0#2-123", "06-06-2025", "WAREHOUSE", "", 10.0, 20.0, "0#2");
        }
        catch (Exception ex){
            fail();
        }
    }

    @Test
    public void testSetItemAsDamaged() {
        // Create an undamaged item
        assertFalse(item.getIsDamaged());

        // Set the item as damaged
        item.setDamageType(Item.DamageType.SCRATCH);

        // Verify that the item is now marked as damaged
        assertTrue(item.getIsDamaged());
        assertEquals(Item.DamageType.SCRATCH, item.getDamageType());
    }

    @Test
    public void testMoveLocation() {
        item.moveLocation();
        assertEquals(Item.Location.STORE, item.getLocation());
        item.moveLocation();
        assertEquals(Item.Location.WAREHOUSE, item.getLocation());
    }
}