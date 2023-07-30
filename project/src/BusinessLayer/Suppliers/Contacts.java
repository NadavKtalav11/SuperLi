package BusinessLayer.Suppliers;

import PersistenceLayer.DAO.Suppliers.SupplierContactMapper;
import PersistenceLayer.DTO.Suppliers.SupplierContactDTO;

public class Contacts {
    private int Id;
    private String name;
    private String phoneNumber;
    SupplierContactDTO supplierContactDTO ;

    public Contacts (int Id,String name , String pN, int supplierId , SupplierContactMapper supplierContactMapper){
        this.Id = Id;
        this.name= name;
        this.phoneNumber = pN;
        supplierContactDTO = new SupplierContactDTO(Id , pN, name  , supplierId , supplierContactMapper);
        //new Contacts(supplierContactDTO);
        supplierContactMapper.addSupplierContacts(supplierContactDTO);
    }


    public Contacts (SupplierContactDTO supplierContactDTO){
        this.Id = supplierContactDTO.getId();
        this.name= supplierContactDTO.getName();
        this.phoneNumber = supplierContactDTO.getPhoneNumber();
        this.supplierContactDTO =supplierContactDTO;
    }


    public int getId() { return Id;}
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
