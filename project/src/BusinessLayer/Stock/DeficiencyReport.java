package BusinessLayer.Stock;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeficiencyReport extends Report {
    
    private Map<Category, Map<Product, Integer>> categoryProducts;

    public DeficiencyReport(String reportID, String name, List<Category> categories, Date date){
        super(reportID, name, categories, date);
        this.categoryProducts = new HashMap<>();
        for(Category category : categories){
            categoryProducts.put(category, category.getShortageInCategory());
        }
    }

    public String toString(){
        String toReturn = "\n";
        toReturn  = toReturn + "Date: " + reportDate.toString() + "\n";
        toReturn  = toReturn + name + "\n";
        for (Category category : categoryProducts.keySet()) {
            Map<Product, Integer> products = categoryProducts.get(category);
            if(products.keySet().size() != 0){
                toReturn = toReturn + "# Category: " + category.getName() + "\n";
                for (Product product : products.keySet()) {
                    toReturn = toReturn + "\t" + "* ID: " + product.getProductID() + "\n";
                    toReturn = toReturn + "\t" + "* Name: " + product.getName() + "\n";
                    toReturn = toReturn + "\t" + "* Minimum amount needed: " + products.get(product) + "\n";
                }
            }
            else{
                toReturn = toReturn + "\t" + "There are no missing products in the category " + category.getName() + "\n";
            }
        }
        toReturn = toReturn + "\n";
        return toReturn;
    }

}