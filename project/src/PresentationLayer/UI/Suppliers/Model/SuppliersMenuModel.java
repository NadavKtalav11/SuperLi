package PresentationLayer.UI.Suppliers.Model;

import BusinessLayer.Suppliers.PaymentOptions;
import PresentationLayer.UI.Suppliers.Controller.Controller;
import ServiceLayer.Suppliers.SupplierControllerService;

import java.awt.*;

public class SuppliersMenuModel {

    private SupplierControllerService service;
    public SuppliersMenuModel(){
        service = new SupplierControllerService();
    }

    public boolean removeContactFromSupplier(int supplierId, int contactId){
        return service.removeContactFromSupplier(supplierId , contactId);
    }

    public boolean addSupplier(int supplierId , boolean canDeliver , String workDays , int BNNumber , int BankAccount
            , PaymentOptions paymentOptions, String name , String contactName, String phoneNumber , String address) {
        return service.addNewSupplier(supplierId, canDeliver, workDays, BNNumber, BankAccount , paymentOptions,
                name, contactName, phoneNumber, address);
    }
    public boolean removeSupplier(int supplierId)
    {
        return service.removeSupplier(supplierId);
    }
    public String showAllOrders(){
        return service.showAllOrderUI();
    }
    public boolean loadSystemData(){
        return service.loadSystemData();
    }

    public boolean removeProductFromSupplier(int supplierId, String productId){
        return service.removeProductFromSupplier(supplierId, productId);
    }
    public boolean addProductToSupplier(int supplierId, String productId , int catalogNumber, int amount , double price){
        return service.addProductToSupplier(supplierId, productId, catalogNumber, amount, price);
    }
    public boolean addDiscountToSupplier(int supplierId  , double amount, double price , boolean precentageDiscount ){
        return service.addDiscountToOrder(supplierId, amount, price, precentageDiscount);
    }
    public boolean addContactToSupplier(int supplierId , String contactName, String phoneNumber) {
        return service.addContactToSupplier(supplierId, contactName, phoneNumber);
    }
    public boolean addDiscountToProduct(int supplierId  ,String productId , double amount, double price , boolean precentageDiscount ){
        return service.addDiscountToProduct(supplierId,productId,  amount, price, precentageDiscount);
    }

    public boolean removeDiscountFromSupplier(int supplierId, int discountId){
        return service.removeDiscountFromSupplier(supplierId, discountId);
    }

    public boolean removeDiscountFromProduct(int supplierId,String productId,  int discountId){
        return service.removeDiscountFromProduct(supplierId,  productId , discountId);
    }

    public String getDiscountToPrint(int supplierId, boolean isProduct , String productId){
        return service.getDiscount(supplierId, isProduct, productId);
    }
    public  boolean editSupplierBnNumber(int supplierId, int supplierBnNumber){
        return service.editSupplierBnNumber(supplierId, supplierBnNumber);
    }
    public  boolean editSupplierBankAccount(int supplierId, int accountNumber){
        return service.editSupplierBankAccount(supplierId, accountNumber);
    }

    public boolean editSupplierPaymentTerms(int supplierId,PaymentOptions paymentOptions){
        return service.editSupplierPaymentTerms(supplierId, paymentOptions);
    }

    public boolean editSupplierName(int supplierId, String supplierName){
        return service.editSupplierName(supplierId, supplierName);
    }
    public boolean editSupplierAddress(int supplierId, String supplierAddress){
        return service.editSupplierAddress(supplierId, supplierAddress);
    }
    public String printSupplierForGUI(){ return service.printSuppliersForGUI();}

    public boolean editSupplierCanDeliver(int supplierId,boolean canDeliver, String days){
        return service.editSupplierCanDeliver(supplierId,canDeliver,days);
    }
}