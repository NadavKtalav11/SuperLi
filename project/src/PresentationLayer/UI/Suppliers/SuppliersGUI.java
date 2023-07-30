package PresentationLayer.UI.Suppliers;

import BusinessLayer.Suppliers.PaymentOptions;
import ServiceLayer.Stock.BranchService;
import ServiceLayer.Suppliers.SupplierControllerService;

public class SuppliersGUI {
    private SupplierControllerService suppliersController;

    public SuppliersGUI () {
        suppliersController = new SupplierControllerService();
    }

    public void removeAllData() {
        BranchService bs = new BranchService();
        while (true) {
            try {
                suppliersController.removeAllData();
                break;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }


    public void setUp() {
        BranchService bs = new BranchService();
        while (true) {
            try {
                suppliersController.removeAllData();
                boolean success = suppliersController.addNewSupplier(1, true,"0123456" ,353, 3454, PaymentOptions.plus_30, "Tnuva","nadav" , "045345353","Ashdod");
                success = success & suppliersController.addNewSupplier(2, false,"", 334, 3434, PaymentOptions.plus_60, "Osem","nadav" , "045345353", "bas");
                success = success & suppliersController.addNewSupplier(3, true,"56", 334, 3434, PaymentOptions.plus_60, "Telma","nadav" , "045345353", "bash1");
                success = success & suppliersController.addNewSupplier(4, true,"", 334, 3434, PaymentOptions.plus_60, "Coca-Cola","nadav" , "045345353", "bash2");
                success =success & suppliersController.editSupplierName(1 , " moshe");
                success =success & suppliersController.addContactToSupplier(1 , " moshe" , "35535");
                success =success & suppliersController.addContactToSupplier(1 , " moshe1" , "3511535");
                success = success & suppliersController.addProductToSupplier(1 ,"111" ,1 ,100 ,7.9);
                success = success & suppliersController.addProductToSupplier(3 ,"111" ,1 ,100 ,7.8);
                success = success & suppliersController.addProductToSupplier(1 ,"222" ,2 ,200 ,1.9);
                success = success & suppliersController.addProductToSupplier(2 ,"333" ,1 ,600 ,146.9);
                success = success & suppliersController.addProductToSupplier(2 ,"111",5 ,50 ,14.5);
                success = success & suppliersController.addProductToSupplier(4 ,"112" ,1 ,200 ,5.0);
                success = success & suppliersController.addProductToSupplier(1 ,"444",12 ,2400 ,5.3);
                success = success & suppliersController.addDiscountToProduct(1 , "222",5,18.3,true );
               success = success & suppliersController.addDiscountToProduct(1 , "111",10,5,true);
                success = success & suppliersController.addDiscountToProduct(1 , "444",5,18.3,true );
                success = success & suppliersController.addDiscountToOrder(1,10 , 13 , true);

                this.suppliersController = new SupplierControllerService();                break;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void startSuppliers() {
        new MainSuppliersMenu();
    }
}

