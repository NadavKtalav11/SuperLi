package ServiceLayer.Suppliers;


import BusinessLayer.Suppliers.PaymentOptions;
import BusinessLayer.Suppliers.SuppliersController;

import java.util.HashMap;
import java.util.Map;

public class SupplierControllerService {


    private SuppliersController suppliersController;

    public SupplierControllerService(){
        suppliersController = new SuppliersController();
    }

    public String showAllOrderUI(){
        try{
            return suppliersController.getAllOrders();
        }
        catch (Exception ex){
            return "no last Order to Show";
        }
    }

    public boolean addNewSupplier (int supplierId , boolean canDeliver, String workDays, int bNNumber , int bankAccount , PaymentOptions pOpt , String name , String contactsName , String phoneNumber, String address ){
        try {
            suppliersController.addSupplier(supplierId, canDeliver,workDays, bNNumber, bankAccount, pOpt, name, contactsName ,phoneNumber, address);
            return true;
        }
        catch (Exception ex){
            System.out.println(ex);
            return false;
        }
    }

    public boolean alertPeriodOrders(){
        try {
            suppliersController.alertPeriodOrders1();
            return true;
        }
        catch (Exception ex){
            System.out.println(ex);
            return false;
        }
    }


    public boolean addDiscountToOrder (int supplierId , double amount , double price ,boolean percentageDiscount){
        try {
            suppliersController.addDiscountToOrder(supplierId, amount , price, percentageDiscount);
            return true;
        }
        catch (Exception ex){
            System.out.println(ex);
            return false;
        }
    }

    public boolean removeDiscountFromProduct (int supplierId , String productId,int  discountId){
        try {
            suppliersController.removeDiscountFromProduct(supplierId, productId , discountId);
            return true;
        }
        catch (Exception ex){
            System.out.println(ex);
            return false;
        }
    }

    public String getDiscount (int supplierId, boolean isProduct , String productId){
        if (isProduct){
            try {
                return suppliersController.getProductDiscounts(supplierId, productId );
            }
            catch (Exception ex){
                System.out.println(ex);
                return ex.toString();
            }
        }
        else {
            try {
                return suppliersController.getDiscounts(supplierId);

            }
            catch (Exception ex){
                System.out.println(ex);
                return ex.toString();
            }
        }
    }


    public boolean removeDiscountFromSupplier (int supplierId , int  discountId){
        try {
            suppliersController.removeDiscountFromSupplier(supplierId, discountId);
            return true;
        }
        catch (Exception ex){
            System.out.println(ex);
            return false;
        }
    }

    public String printSuppliersForGUI() {
        try {
            return suppliersController.printSuppliersForGUI();
        } catch (Exception e) {
            return e.toString();
        }
    }

    public boolean addDiscountToProduct (int supplierId ,String productId, double amount , double price ,boolean percentageDiscount){
        try {
            suppliersController.addDiscountToProduct(supplierId,productId, amount , price, percentageDiscount);
            return true;
        }
        catch (Exception ex){
            System.out.println(ex);
            return false;
        }
    }






    public boolean addContactToSupplier (int supplierId , String name , String phoneNumber){
        try {
            suppliersController.addContact(supplierId, name ,  phoneNumber);
            return true;
        }
        catch (Exception ex){
            System.out.println(ex);
            return false;
        }
    }

    public boolean printSupplierContacts (int supplierId ){
        try {
            suppliersController.printSupplierContacts(supplierId);
            return true;
        }
        catch (Exception ex){
            System.out.println(ex);
            return false;
        }
    }

    public boolean removeProductFromSupplier (int supplierId ,String productId){
        try {
            suppliersController.removeProductFromSupplier(supplierId, productId);
            return true;
        }
        catch (Exception ex){
            System.out.println(ex);
            return false;
        }
    }

    public boolean removeContactFromSupplier (int supplierId , int contactId){
        try {
            suppliersController.removeContact(supplierId, contactId);
            return true;
        }
        catch (Exception ex){
            System.out.println(ex);
            return false;
        }
    }


    public boolean RemoveSupplier (int supplierId){
        try {
            suppliersController.removeSupplier(supplierId);
            return true;
        }
        catch (Exception ex){
            System.out.println(ex);
            return false;
        }
    }
    public boolean addProductToSupplier (int supplierId , String productId , int catalogNum, int amountToSupply , double price){
        try {
            suppliersController.addProductToSupplier(supplierId ,productId, catalogNum,amountToSupply ,price );
            return true;
        }
        catch (Exception ex){
            System.out.println(ex);
            return false;
        }
    }


    public void printSuppliers(){
        try {
            suppliersController.printSuppliers();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public boolean loadSystemData(){
        removeAllData();
        boolean success = addNewSupplier(1, true,"0123456" ,353, 3454, PaymentOptions.plus_30, "Tnuva","nadav" , "045345353","Ashdod");
        success = success & addNewSupplier(2, false,"", 334, 3434, PaymentOptions.plus_60, "Osem","nadav" , "045345353", "bas");
        success = success & addNewSupplier(3, true,"56", 334, 3434, PaymentOptions.plus_60, "Telma","nadav" , "045345353", "bash1");
        success = success & addNewSupplier(4, true,"", 334, 3434, PaymentOptions.plus_60, "Coca-Cola","nadav" , "045345353", "bash2");
        success =success & editSupplierName(1 , " moshe");
        success =success & addContactToSupplier(1 , " moshe" , "35535");
        success =success & addContactToSupplier(1 , " moshe1" , "3511535");
        success = success & addProductToSupplier(1 ,"111" ,1 ,100 ,7.9);
        success = success & addProductToSupplier(1 ,"222" ,2 ,200 ,1.9);
        success = success & addProductToSupplier(2 ,"333" ,1 ,600 ,146.9);
        success = success & addProductToSupplier(2 ,"111",5 ,50 ,14.5);
        success = success & addProductToSupplier(4 ,"112" ,1 ,200 ,5.0);
        success = success & addProductToSupplier(1 ,"444",12 ,2400 ,5.3);
        success = success & addDiscountToProduct(1 , "222",5,18.3,true );
        success = success & addDiscountToProduct(1 , "444",5,18.3,true );
        success = success & addDiscountToOrder(1,10 , 13 , true);
        return success;
    }

    public void printAllOrders(int supplierId){
        try {
            suppliersController.printSupplierOrders(supplierId);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void removeAllData(){
        try {
            suppliersController.removeAllData();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }


    public boolean editSupplierName(int supplierId , String name){
        try {
            suppliersController.editSupplierName(supplierId, name);
            return true;
        }
        catch (Exception ex){
            System.out.println(ex);
            return false;
        }
    }

    public boolean editSupplierAddress(int supplierId , String address){
        try {
            suppliersController.editSupplierAddress(supplierId, address);
            return true;
        }
        catch (Exception ex){
            System.out.println(ex);
            return false;
        }
    }



    public boolean editSupplierBnNumber(int supplierId , int bnnumber){
        try {
            suppliersController.editBNNumber(supplierId, bnnumber);
            return true;
        }
        catch (Exception ex){
            System.out.println(ex);
            return false;
        }
    }
    public boolean editSupplierBankAccount(int supplierId , int bankAccount){
        try {
            suppliersController.editBankAccount(supplierId, bankAccount);
            return true;
        }
        catch (Exception ex){
            System.out.println(ex);
            return false;
        }
    }
    public boolean editSupplierPaymentTerms(int supplierId , PaymentOptions paymentOptions){
        try {
            suppliersController.editSupplierPaymentTerms(supplierId, paymentOptions);
            return true;
        }
        catch (Exception ex){
            System.out.println(ex);
            return false;
        }
    }
    public boolean editSupplierCanDeliver(int supplierId , boolean canDeliver, String days){
        try {
            suppliersController.editSupplierCanDeliver(supplierId, canDeliver, days);
            return true;
        }
        catch (Exception ex){
            System.out.println(ex);
            return false;
        }
    }
    public boolean removeSupplier(int supplierId ) {
        try {
            suppliersController.removeSupplier(supplierId);
            return true;
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
    }



/*
    public boolean alretPeriodOrder(int supplierId , DeliveryTerms deliveryTerms){
        try {
            suppliersController.editSupplierDeliveryTerms(supplierId, deliveryTerms);
            return true;
        }
        catch (Exception ex){
            System.out.println(ex);
            return false;
        }
    }
*/






}