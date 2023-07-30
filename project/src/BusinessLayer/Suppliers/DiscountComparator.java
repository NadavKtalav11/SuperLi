package BusinessLayer.Suppliers;

import java.util.Comparator;

public class DiscountComparator implements Comparator<DiscountSuppliers> {
    public int compare (DiscountSuppliers discountSuppliers1, DiscountSuppliers discountSuppliers2){
        if (discountSuppliers1.getAmount()> discountSuppliers2.getAmount())
            return 1;
        else return -1;
    }
}