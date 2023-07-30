package BusinessLayer.Stock;
import PersistenceLayer.DAO.Stock.DiscountInStoreDAO;
import PersistenceLayer.DAO.Stock.ItemDAO;
import PersistenceLayer.DTO.Stock.DiscountInStoreDTO;
import PersistenceLayer.DTO.Stock.ItemDTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Discount {

    private final String DISCOUNT_ID;
    private Date startDate;
    private Date endDate;
    private double discountValue;
    private boolean isPercent;
    private DiscountType discountType;
    private DiscountInStoreDAO discountDAO;

    public Discount(String discountID, Date startDate, Date endDate, Double discountValue, boolean isPercent, DiscountType type, DiscountInStoreDAO dDAO) {
        this.DISCOUNT_ID = discountID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountValue = discountValue;
        this.isPercent = isPercent;
        this.discountType = type;
        this.discountDAO = dDAO;
    }

    public Discount(DiscountInStoreDTO dDTO, DiscountInStoreDAO dDAO) {
        this.DISCOUNT_ID = dDTO.getDiscountID();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            this.startDate = format.parse(dDTO.getStartDate());
            this.endDate = format.parse(dDTO.getEndDate());
        }
        catch (Exception ex){}
        this.discountValue = dDTO.getDiscountValue();
        this.isPercent = convertIntToBoolean(dDTO.getIsPercent());
        this.discountType = DiscountType.valueOf(dDTO.getDiscountType());
        this.discountDAO = dDAO;
    }


    public String getDiscountID() {
        return DISCOUNT_ID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public boolean getIsPercent() {
        return isPercent;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setStartDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        discountDAO.updateStartDate(dateFormat.format(date), getDiscountID());
        this.startDate = date;
    }

    public void setEndDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        discountDAO.updateEndDate(dateFormat.format(date), getDiscountID());
        this.endDate = date;
    }

    public void setDiscountValue(Double value) {
        discountDAO.updateDiscountValue(value, getDiscountID());
        this.discountValue = value;
    }

    public void setPercent(boolean ans) {
        discountDAO.updateIsPercent(convertBooleanToInt(ans), getDiscountID());
        this.isPercent = ans;
    }

    public void setDiscountType(DiscountType type) {
        this.discountType = type;
    }

    private boolean convertIntToBoolean (int val) {
        if (val == 0) {
            return false;
        }
       return true;
    }

    private int convertBooleanToInt(boolean bool) {
        if (bool) {
            return 1;
        }
        return 0;
    }

    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String toReturn = "";
        toReturn = toReturn + "Discount " + getDiscountID() + " - " + getDiscountValue();
        if (isPercent) {
            toReturn = toReturn + "%, ";
        }
        else {
            toReturn = toReturn + "₪, ";
        }
        toReturn = toReturn + "active from " + dateFormat.format(getStartDate()) + " to " +  dateFormat.format(getEndDate()) + ".";
        return toReturn;
    }

    /**
     * 
     * @return if this discount should be active now due to its dates
     */
    public boolean isActiveDiscount() {
        Date nowDate = new Date();
        return (startDate.before(nowDate)) && (endDate.after(nowDate));
    }

    /**
     * 
     * @return if the end date of this discount has passedand it is nor relevant anymore
     */
    public boolean isOver() {
        Date nowDate = new Date();
        return endDate.before(nowDate);
    }

    /**
     * 
     * @param sellingPrice
     * @return the calcuted price in case of activating this discount on the given selling Price
     */
    public double calculateDiscountedPrice(double sellingPrice) {
        if (getIsPercent()) {
            return ((100 - getDiscountValue()) * 0.01 * sellingPrice);
        }
        else {
            return (sellingPrice - getDiscountValue());
        }
    }


    /*public void setDamagedOnly(boolean ans) {
        this.isDamagedOnly = ans;
    } */

    /**
     * Called from discountController as a part of the creation of a new discount
     * checks if this discount should be active now due to its dates and calls activate if so
     */
    /*
	public void updateNewDiscount() {
        Date today = new Date();
        if ((startDate.before(today)) && (endDate.after(today))) {
            activateDiscount();
        }
	} 
    */

    public enum DiscountType {
        CATEGORY,
        PRODUCT,
        DAMAGED
    }

}