package BusinessLayer.Suppliers;



import PersistenceLayer.DAO.Suppliers.SupplierCardMapper;
import PersistenceLayer.DAO.Suppliers.SupplierContactMapper;
import PersistenceLayer.DTO.Suppliers.SupplierCardDTO;

import java.util.HashMap;

;

public class SupplierCard {
    private int BNnumber;
    private int bankAccountNumber;
    private PaymentOptions paymentOptions;
    private HashMap <Integer,Contacts> contactsMap;
    private int contactIdCounter ;
    private SupplierCardDTO supplierCardDTO;
    private SupplierContactMapper supplierContactMapper;




    public SupplierCard(SupplierCardDTO supplierCardDTO){
        this.supplierCardDTO = supplierCardDTO;
        this.supplierContactMapper = new SupplierContactMapper();
      //  this.contactIdCounter= supplierContactMapper.getMaxContactId()+1;
        this.paymentOptions= supplierCardDTO.getPaymentOptions();
        this.bankAccountNumber= supplierCardDTO.getBankAccountNumber();
        this.BNnumber= supplierCardDTO.getBNNumber();
        this.contactsMap = new HashMap<>();
        contactIdCounter = 1;



    }


    public SupplierCard(int BNNumber , int BankAccount , PaymentOptions pOpt ,  String contactName, String phoneNumber , SupplierCardMapper supplierCardMapper, int supplierId){
        this.bankAccountNumber= BankAccount;
        this.BNnumber = BNNumber;
        this.paymentOptions = pOpt;
        this.supplierContactMapper = new SupplierContactMapper();
        SupplierContactMapper contactMapper = new SupplierContactMapper();
        contactIdCounter = contactMapper.getMaxContactId()+1;
        Contacts contacts = new Contacts(contactIdCounter , contactName, phoneNumber, supplierId , supplierContactMapper);
        contactIdCounter++;
        contactsMap = new HashMap<>();
        contactsMap.put(contactIdCounter,contacts);
       // contactIdCounter++;
        this.contactsMap = new HashMap<>() ;
        this.contactsMap.put(contacts.getId(),contacts);
      //  this.contactIdCounter= 2;
        this.supplierCardDTO = new SupplierCardDTO(bankAccountNumber, bankAccountNumber, paymentOptions, supplierId, supplierCardMapper );
        supplierCardMapper.addSupplierCard(supplierCardDTO);


    }

    public int getContactIdCounter(){
        contactIdCounter++;
        return contactIdCounter;
    }
    public int getContactIdCounterWithoutAdvancing() {
        return contactIdCounter;
    }

        public int getBNNumber() {
        return BNnumber;
    }
    public int getBankAccountNumber(){
        return bankAccountNumber;
    }

    public PaymentOptions getPaymentOptions() {
        return paymentOptions;
    }

    public HashMap<Integer,Contacts> getContacts() {
        return contactsMap;
    }

    public Contacts getSpecificContactOne(int contactId){
        supplierContactMapper.selectAllSupplierContacts();
        return contactsMap.get(contactId);
    }

    public void removeSupplier(int supplierId){
        supplierContactMapper.removeSupplierContacts(supplierId);
        contactsMap.clear();
        this.supplierCardDTO = null;
    }



    public void setBNNumber(int newBN){
        supplierCardDTO.getDao().updateBNNumber(newBN, supplierCardDTO.getSupplierId());
        supplierCardDTO.setbNNumber(newBN);
        BNnumber = newBN;}



    public void setBankAccountNumber(int newBAN){
        supplierCardDTO.getDao().updateBankNumber(newBAN, supplierCardDTO.getSupplierId());
        bankAccountNumber= newBAN;
        supplierCardDTO.setBankAccountNumber(newBAN);
    }
    public void setPaymentOptions(PaymentOptions newPayment){
        paymentOptions= newPayment;
        supplierCardDTO.getDao().updatePayment(newPayment, supplierCardDTO.getSupplierId());
        supplierCardDTO.setPaymentOptions(newPayment);
    }
    public void addContact ( String name , String phoneNumber, int supplierId){

        int id =getContactIdCounter();
        Contacts contacts = new Contacts(id, name, phoneNumber, supplierId, supplierContactMapper);
        contactsMap.put(id,contacts);
    }
    public void removeContact(int contactId, int supplierId){
        if (!containsContact(supplierId , contactId )){
            throw new IllegalArgumentException("this contact Id doesn't exist in this supplier");
        }
        supplierContactMapper.removeContact(contactId, supplierId);
        if (contactsMap.containsKey(contactId)) {
            contactsMap.remove(contactId);
        }
    }
    public boolean containsContact(int supplierId, int id){
        if (contactsMap.containsKey(id)){
            return true;
        }
        if (supplierContactMapper.getSpecificSupplierContact( supplierId, id)==null){
            return false;
        }
        else {
            return true;
        }
    }

    public void printContacts(int supId){
        supplierContactMapper.getSupplierContacts(supId);
        for (int id : contactsMap.keySet()){
            System.out.println("contact Id- " + id + " contact name - " + contactsMap.get(id).getName()+
                    " contact phone number - " + contactsMap.get(id).getPhoneNumber());
        }
    }

    public SupplierContactMapper getSupplierContactMapper(){
        return supplierContactMapper;
    }

}




