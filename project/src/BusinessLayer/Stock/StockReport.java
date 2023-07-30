package BusinessLayer.Stock;

import java.util.Date;
import java.util.List;

public class StockReport extends Report{

    public StockReport(String reportID, String name, List<Category> categories, Date date){
        super(reportID, name, categories, date);
    }


    @Override
    public String toString() {
        String toReturn = "\n";
        toReturn = toReturn + "Date: " + reportDate.toString() + "\n";
        toReturn = toReturn + name + "\n";
        for (Category category : categories) {
            toReturn = stringAllCategoryProductsStock(toReturn, category);
        }
        toReturn = toReturn + "\n";
        return toReturn;
    }


    public String stringAllCategoryProductsStock(String s, Category category) {
        List<Product> products = category.getProductList();
        s = s + "# Category: " + category.getName() + "\n";
        if(products.size() != 0){
            for (Product product : products) {
                s = s + "\t" + "* Product: " + product.getProductID() + " (" + product.getName() + ")" + "\n";
                s = s + "\t" + "    In store: " + product.getStoreAmount() + "\n";
                s = s + "\t" + "    In warehouse: " + product.getWarehouseAmount() + "\n";
                s = s + "\t" + "    Total of " + product.getTotalAmount() + " products." + "\n";
            }
        }
        else{
            s = s + "\t" + "There are no products in the Category '" + category.getName() + "'." + "\n";
        }
        List <Category> subCategories = category.getSubCategoryList();
        if (subCategories.size() != 0) {
            s = s + "\t" + "Products in Category '" + category.getName() + "''s sub-categories:" + "\n";
            for (Category subCat : subCategories) {
                s = stringAllCategoryProductsStock (s, subCat);
            }
        }
        return s;
    }
}
