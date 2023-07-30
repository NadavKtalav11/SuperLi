package BusinessLayer.Stock;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DamagedReport extends Report {

    private Map<Category, Map<Item.DamageType, List<Item>>> damagedItems;

    public DamagedReport(String reportID, String name, List<Category> categories, List<Item.DamageType> damages, Date date) {
        super(reportID, name, categories, date);
        this.damagedItems = new HashMap<>();
        for (Category category : categories) {
            damagedItems.put(category, category.getDamagedInCategoryByType(damages));
        }
    }

    public String toString(){
        String toReturn = "\n";
        toReturn = toReturn + "Date: " + reportDate.toString() + "\n";
        toReturn = toReturn + name + "\n";
        for (Category category : damagedItems.keySet()) {
            Map<Item.DamageType, List<Item>> items = damagedItems.get(category);
            toReturn = toReturn + "# Category: " + category.getName() + "\n";
            for (Item.DamageType type : items.keySet()) {
                if(items.get(type).size() != 0){
                    toReturn = toReturn + "\t" + "* Damage type " + type.toString() + "\n";
                    for (Item item : items.get(type)) {
                        toReturn = toReturn + "\t\t" + "ID: " + item.getItemID() + "\n";
                    }
                }
                else{
                    toReturn = toReturn + "\t" + "There are no damaged products in the category " + category.getName() + " from the damage type " + type.toString() + "\n";
                }
            }
        }
        toReturn = toReturn + "\n";
        return toReturn;
    }

}