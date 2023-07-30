package Tests.Stock;

import static org.junit.jupiter.api.Assertions.*;


import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import PersistenceLayer.DAO.Stock.CategoryDAO;
import PersistenceLayer.DAO.Stock.ProductDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import BusinessLayer.Stock.Category;
import BusinessLayer.Stock.Discount.DiscountType;
import BusinessLayer.Stock.Product;
import java.lang.NullPointerException;


class CategoryTest {
    private Category category;
    private Product product1;
    private Product product2;

    @BeforeEach
    public void setUp() {
        ProductDAO pdao = new ProductDAO();
        CategoryDAO cdao = new CategoryDAO(pdao);
        cdao.removeAll();
        pdao.removeAll();
        category = new Category("0#2", "Vegetables and Fruits", cdao);
        try {
            cdao.addCategory("0#2", "Vegetables and Fruits", null);
        }
        catch (Exception ex){}
        List<String> chain = new ArrayList<>();
        chain.add("0#2");
        product1 = new Product("0#111", "Apple", 4.5, 8, "Amit", 20, 3, 30, new ArrayList<>(chain), pdao);
        product2 = new Product("0#222", "Banana", 4.5, 8, "Amit", 10, 3, 40, new ArrayList<>(chain), pdao);
        try {
            pdao.addProduct("0#111", "Apple", 4.5, 8, "Amit", 20, 3, 30, "0#2");
            pdao.addProduct("0#222", "Banana", 4.5, 8, "Amit", 10, 3, 40, "0#2");
        }
        catch (Exception ex){}
        category.addProduct(product1);
        category.addProduct(product2);
    }

    @Test
    public void testAddSubCategory() {
        List<String> categoryIds = new ArrayList<>();
        try {
            String subCategoryId1 = category.addSubCategory(new ArrayList<>(categoryIds), "Fruits");
            assertEquals("0#2-1", subCategoryId1);
            String subCategoryId2 = category.addSubCategory(new ArrayList<>(categoryIds), "Vegetables");
            assertEquals("0#2-2", subCategoryId2);
            categoryIds.add("0#2-1");
            String subCategoryId3 = category.addSubCategory(new ArrayList<>(categoryIds), "Apple");
            assertEquals("0#2-1-1", subCategoryId3);
            String subCategoryId4 = category.addSubCategory(new ArrayList<>(categoryIds), "Banana");
            assertEquals("0#2-1-2", subCategoryId4);
        }
        catch (Exception ex){
            fail();
        }
        categoryIds.remove(0);
        categoryIds.add("0#2-2");
        try {
            String subCategoryId5 = category.addSubCategory(new ArrayList<>(categoryIds), "Tomato");
            assertEquals("0#2-2-1", subCategoryId5);
        }
        catch (Exception ex){
            fail();
        }
    }

    @Test
    public void testGetCategoryChainBySubIDListWithInvalidId() {
        List<String> categoryIds = new ArrayList<>();
        categoryIds.add("1");
        categoryIds.add("invalid_id");
        assertThrows(NullPointerException.class, () -> {
            category.getCategoryChainBySubIDList(categoryIds);
        });
    }

    @Test
    public void testGetProducts() {
        List<String> categoryIds = new ArrayList<>();
        Map<String, String> expectedProducts = new HashMap<>();
        expectedProducts.put("0#111", "Apple");
        expectedProducts.put("0#222", "Banana");
        Map<String, String> actualProducts = category.getProducts(new ArrayList<>(categoryIds));
        assertEquals(expectedProducts, actualProducts);
        categoryIds.add("0#1-1");
        actualProducts = category.getProducts(new ArrayList<>(categoryIds));
        assertTrue(actualProducts.isEmpty());
    }

    @Test
    public void testGetSub() {
        List<String> categoryIds = new ArrayList<>();
        Map<String, String> expectedSubcategories = new HashMap<>();
        try {
            String subCategoryId1 = category.addSubCategory(new ArrayList<>(categoryIds), "Fruits");
            String subCategoryId2 = category.addSubCategory(new ArrayList<>(categoryIds), "Vegetables");
            expectedSubcategories.put(subCategoryId1, "Fruits");
            expectedSubcategories.put(subCategoryId2, "Vegetables");
            Map<String, String> actualSubcategories = category.getSub(new ArrayList<>(categoryIds));
            assertEquals(expectedSubcategories, actualSubcategories);
            categoryIds.add("0#2-1");
            category.addSubCategory(new ArrayList<>(categoryIds), "Apple");
            categoryIds.remove(0);
            actualSubcategories = category.getSub(categoryIds);
            assertEquals(expectedSubcategories, actualSubcategories);
        }
        catch (Exception ex){
            fail();
        }
    }

}