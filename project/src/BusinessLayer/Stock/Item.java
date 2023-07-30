package BusinessLayer.Stock;

import PersistenceLayer.DAO.Stock.ItemDAO;
import PersistenceLayer.DTO.Stock.ItemDTO;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Item {

    private final String ITEM_ID;
    private Date expirationDate;
    private Location location;
    private String inLocationPosition;
    private double discountPrice;
    private double supplierPrice;
    private boolean isDamaged;
    private DamageType damageType;
    private ItemDAO itemDAO;

    public Item (String itemID, Date date, Location location, double purchasePrice, double sellingPrice, ItemDAO itemDAO) {
        this.ITEM_ID = itemID;
        this.expirationDate = date;
        this.location = location;
        this.inLocationPosition = ""; // ???
        this.discountPrice = sellingPrice;
        this.supplierPrice = purchasePrice;
        this.isDamaged = false;
        this.damageType = DamageType.NONE;
        this.itemDAO = itemDAO;
    }

    public Item (ItemDTO idto, ItemDAO itemDAO) {
        this.ITEM_ID = idto.getITEM_ID();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            this.expirationDate = format.parse(idto.getExpirationDate());
        }
        catch (Exception ex){}
        this.location = Location.valueOf(idto.getLocation());
        this.inLocationPosition = idto.getLocation();
        this.discountPrice = idto.getDiscountPrice();
        this.supplierPrice = idto.getSupplierPrice();
        if(idto.getIsDamaged() == 0)
            this.isDamaged = false;
        else
            this.isDamaged = true;
        this.damageType = DamageType.valueOf(idto.getDamageType());
        this.itemDAO = itemDAO;
    }

    public String getItemID() {
        return ITEM_ID;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public Location getLocation() {
        return location;
    }

    public String getInLocationPosition() {
        return inLocationPosition;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public boolean getIsDamaged() {
        return isDamaged;
    }

    public DamageType getDamageType() {
        return damageType;
    }

    public double getSupplierPrice() {
        return supplierPrice;
    }

    public void setSupplierPrice(double supplierPrice) {
        this.supplierPrice = supplierPrice;
    }
    
    public void setExpirationDate(Date date){
        this.expirationDate = date;
    }

    public void setLocation(Location newLocation) {
        itemDAO.updateLocation(newLocation.toString(), getItemID());
        this.location = newLocation;
    }

    public void setInLocationPosition(String position) {
        this.inLocationPosition = position;
    }

    public void setDiscountPrice(double price) {
        itemDAO.updateDiscountPrice(price, getItemID());
        this.discountPrice = price;
    }

    public void setIsDamaged(boolean damaged) {
        if(damaged){
            itemDAO.updateIsDamaged(1, getItemID());
        }
        else {
            itemDAO.updateIsDamaged(0, getItemID());
        }
        this.isDamaged = damaged;
    }

    public void setDamageType(DamageType damage) {
        itemDAO.updateDamageType(damage.toString(), getItemID());
        this.damageType = damage;
        if(!damage.equals(DamageType.NONE)) {
            setIsDamaged(true);
        }
        if(damage.equals(DamageType.NONE)) {
            setIsDamaged(false);
        }
    }

    /**
     * move item from warehouse to store, and opposite
     */
    public void moveLocation(){
        switch (location){
            case STORE : setLocation(Location.WAREHOUSE); break;
            case WAREHOUSE: setLocation(Location.STORE); break;
        }
    }

    public enum Location {
        STORE,
        WAREHOUSE
    }

    public enum DamageType {
        NONE,
        OTHER,
        SCRATCH,
        EXPIRED,
        BROKEN
    }
}
