package PersistenceLayer.DTO.Stock;

import BusinessLayer.Stock.Item;
import PersistenceLayer.DAO.AbstractMapper;
import PersistenceLayer.DAO.Stock.ItemDAO;
import PersistenceLayer.DTO.AbstractDTO;

import java.awt.font.TextHitInfo;
import java.util.Date;

public class ItemDTO extends AbstractDTO {

    private final String ITEM_ID;
    private String expirationDate;
    private String location;
    private String inLocationPosition;
    private double discountPrice;
    private double supplierPrice;
    private int isDamaged;
    private String damageType;

    public ItemDTO(ItemDAO dao, String itemID, String date, String location, double purchasePrice, double sellingPrice, int isDamaged, String damageType, String inLocationPosition) {
        super(dao);
        this.ITEM_ID = itemID;
        this.expirationDate = date;
        this.location = location;
        this.discountPrice = sellingPrice;
        this.supplierPrice = purchasePrice;
        this.isDamaged = isDamaged;
        this.damageType = damageType;
        this.inLocationPosition = inLocationPosition;
    }

    public String getITEM_ID() {
        return ITEM_ID;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getLocation() {
        return location;
    }

    public String getInLocationPosition() {
        return inLocationPosition;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public double getSupplierPrice() {
        return supplierPrice;
    }

    public int getIsDamaged() {
        return isDamaged;
    }

    public String getDamageType() {
        return damageType;
    }

}
