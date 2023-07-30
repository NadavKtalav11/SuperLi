package PersistenceLayer.DTO.Suppliers;


import PersistenceLayer.DAO.Suppliers.SupplierMapper;
import PersistenceLayer.DTO.AbstractDTO;

public class SupplierDTO extends AbstractDTO {
    private int Id;
    private String name;
    private int canDeliver;
    private String days;
    private String address;

    public SupplierDTO( String name ,int idNumber , int canDeliver, String days,String address){
        super(new SupplierMapper());
        this.Id= idNumber;
        this.name = name;
        this.canDeliver= canDeliver;

        this.days= days;
        this.address = address;
    }
    public SupplierDTO( String name ,int idNumber , int canDeliver, String days,String address , SupplierMapper supplierMapper){
        super(supplierMapper);
        this.Id= idNumber;
        this.name = name;
        this.canDeliver= canDeliver;
        this.days= days;
        this.address = address;
    }


    public String getName() {
        return name;
    }

    public int getId() {
        return Id;
    }
    public int getCanDeliver(){
        return canDeliver;
    }
    public String getDays(){
        return days;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public SupplierMapper getDao() {
        return (SupplierMapper) dao;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCanDeliver(int canDeliver) {
        this.canDeliver = canDeliver;
    }

    public void setDays(String days) {
        this.days = days;
    }
}