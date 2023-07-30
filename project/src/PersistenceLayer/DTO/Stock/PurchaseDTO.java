package PersistenceLayer.DTO.Stock;

import PersistenceLayer.DAO.Stock.DiscountInStoreDAO;
import PersistenceLayer.DAO.Stock.PurchaseDAO;
import PersistenceLayer.DTO.AbstractDTO;

import java.util.Date;

public class PurchaseDTO extends AbstractDTO {

    private final String PURCHASE_ID;
    private String date;
    private double totalPrice;

    public PurchaseDTO(PurchaseDAO dao, String purchaseID, String date, Double totalPrice) {
        super(dao);
        this.PURCHASE_ID = purchaseID;
        this.date = date;
        this.totalPrice = totalPrice;
    }

    public String getPURCHASE_ID() {
        return PURCHASE_ID;
    }

    public String getDate() {
        return date;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

}
