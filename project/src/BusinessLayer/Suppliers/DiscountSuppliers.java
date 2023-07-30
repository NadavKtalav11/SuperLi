package BusinessLayer.Suppliers;

public abstract class DiscountSuppliers {
    // private DiscountType discountType;
    protected double amount;
    protected double discount;
    protected int discountId;

    public DiscountSuppliers(double amount , double discount , int Id ){
        this.amount = amount;
        this.discount = discount;
        this.discountId = Id;
    }

    public int getDiscountId() {
        return discountId;
    }

    public double getAmount() {
        return amount;
    }

    //public DiscountType getDiscountType() {
    //    return discountType;
    //}

    public double getDiscount() {
        return discount;
    }

    public void setAmount( int amount){
        this.amount = amount;
    }

    public void setDiscount(double discount){
        this.discount = discount;
    }


    public abstract double getPriceAfterDiscount(String productId, double price);
    public abstract boolean getIsPercentage();

    public abstract String toPrintStyle();





     /*public enum DiscountType {
        buy_amount_get_discount_Per_product,
        buy_amount_get_PercentDiscount_perProduct,
         buy_amount_get_discount_Per_Order,
         buy_amount_get_PercentDiscount_per_Order;

    }*/
}

