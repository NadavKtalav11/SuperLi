package PersistenceLayer.DTO.Stock;

import BusinessLayer.Stock.Discount;
import PersistenceLayer.DAO.Stock.DiscountInStoreDAO;
import PersistenceLayer.DAO.Stock.ItemDAO;
import PersistenceLayer.DTO.AbstractDTO;

import java.util.Date;

public class DiscountInStoreDTO extends AbstractDTO {

    private final String DISCOUNT_ID;
    private String startDate;
    private String endDate;
    private double discountValue;
    private int isPercent;
    private String discountType;

    public DiscountInStoreDTO(DiscountInStoreDAO dao, String discountID, String startDate, String endDate, Double discountValue, int isPercent, String type) {
        super(dao);
        this.DISCOUNT_ID = discountID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountValue = discountValue;
        this.isPercent = isPercent;
        this.discountType = type;
    }

    public String getDiscountID() {
        return DISCOUNT_ID;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public int getIsPercent() {
        return isPercent;
    }

    public String getDiscountType() {
        return discountType;
    }

}
