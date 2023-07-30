package PersistenceLayer.DTO.Suppliers;


import BusinessLayer.Suppliers.PaymentOptions;
import PersistenceLayer.DAO.Suppliers.SupplierCardMapper;
import PersistenceLayer.DTO.AbstractDTO;

public class SupplierCardDTO extends AbstractDTO {
    private int bankAccountNumber;
    private int bNNumber;
    private PaymentOptions paymentOptions;
    private int supplierId;
   // private int contactIdCounter;

    public SupplierCardDTO(int bNNumber, int bankAccountNumber , PaymentOptions paymentOptions, int supplierId , SupplierCardMapper supplierCardMapper) {
        super(supplierCardMapper);
        this.bankAccountNumber = bankAccountNumber;
        this.bNNumber = bNNumber;
        this.paymentOptions = paymentOptions;
        this.supplierId = supplierId;
        //this.contactIdCounter = ContactIdCounter;
    }

    public SupplierCardDTO( int bNNumber,int bankAccountNumber ,  PaymentOptions paymentOptions, int supplierId ) {
        super(new SupplierCardMapper());
        this.bankAccountNumber = bankAccountNumber;
        this.bNNumber = bNNumber;
        this.paymentOptions = paymentOptions;
        this.supplierId = supplierId;
    }

    public void setBankAccountNumber(int bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public void setbNNumber(int bNNumber) {
        this.bNNumber = bNNumber;
    }

    public void setPaymentOptions(PaymentOptions paymentOptions) {
        this.paymentOptions = paymentOptions;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public int getBankAccountNumber() {
        return bankAccountNumber;
    }

    @Override
    public SupplierCardMapper getDao() {
        return (SupplierCardMapper) dao;
    }

    public int getBNNumber() {
        return bNNumber;
    }

    public PaymentOptions getPaymentOptions() {
        return paymentOptions;
    }
}
