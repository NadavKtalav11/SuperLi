package PresentationLayer.UI.Suppliers.Controller;


import BusinessLayer.Suppliers.PaymentOptions;
import PresentationLayer.CLI.Suppliers.MainMenu;
import PresentationLayer.UI.Suppliers.Model.SuppliersMenuModel;
import PresentationLayer.UI.Suppliers.View.MenuView;
import ServiceLayer.Suppliers.SupplierControllerService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller extends JFrame implements ActionListener {
    private SuppliersMenuModel model ;
    private MenuView view;
    private JPanel panel1;


    public Controller(MenuView view1) {
        model =new SuppliersMenuModel();
        this.view = view1;
        panel1= view.panel1;
        view.addNewSupplierButton.addActionListener(this);
        view.removeSupplierButton.addActionListener(this);
        view.lookAtLastOrderButton.addActionListener(this);
  //      view.loadSystemDataButton.addActionListener(this);
        view.addProductToSupplierButton.addActionListener(this);
        view.removeProductFromSupplierButton.addActionListener(this);
        view.addDiscountToSupplierButton.addActionListener(this);
        view.addContactToSupplierButton.addActionListener(this);
        view.exitButton.addActionListener(this);
        view.removeDiscountFromSupplierButton.addActionListener(this);
        view.addDiscountToProductButton.addActionListener(this);
        view.removeContactFromSupplierButton.addActionListener(this);
        view.editSupplierButton.addActionListener(this);
        panel1.setVisible(true);
    }



    //private void createUIComponents() {// TODO: place custom component creation code here
    // }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.addNewSupplierButton) {
            addSupplier();
        }
        if (e.getSource() == view.removeSupplierButton) {
            removeSupplier();
        }
        if (e.getSource() == view.lookAtLastOrderButton) {
            lookAtLastOrder();
        }

        if (e.getSource() == view.removeProductFromSupplierButton) {
            removeProductFromSupplier();
        }
        if (e.getSource() == view.addProductToSupplierButton) {
            addProductToSupplier();
        }
        if (e.getSource() == view.addDiscountToSupplierButton) {
            addDiscountToSupplier();
        }
        if (e.getSource() == view.addContactToSupplierButton) {
            addContactToSupplier();
        }
        if (e.getSource() == view.exitButton) {
            System.exit(0);
        }
        if (e.getSource() == view.addDiscountToProductButton) {
            addDiscountToProduct();
        }
        if (e.getSource() == view.removeDiscountFromSupplierButton){
            removeDiscountFromSupplier();
        }
        if (e.getSource() == view.editSupplierButton){
            editSupplier();
        }
        if (e.getSource()== view.removeContactFromSupplierButton) {
            removeSupplierContact();
        }

    }

    public void editSupplier(){
        boolean parseSuccess = false;
        int supplierId =0;
        try {
                boolean success = false;
                String isItNull = JOptionPane.showInputDialog("you choose to edit a supplier from the system \n" +
                                "please select your supplier Id: \n"
                    + model.printSupplierForGUI()
                    );
            while (!parseSuccess) {
                if (isItNull == null) {
                    throw new Exception("");
                }
                try {
                    supplierId = Integer.parseInt(isItNull);
                    parseSuccess = true;
                } catch (Exception e) {
                    isItNull = JOptionPane.showInputDialog("please enter valid  \n"+
                            "you choose to edit a supplier from the system \n" +
                            "please select your supplier Id: \n"
                    );

                }
            }



            int infoToUpdate = Integer.parseInt(JOptionPane.showInputDialog("please select what info you want to edit:\n" +
                    "1- delivery term \n" +
                    "2- BN number \n" +
                    "3- bank account number \n" +
                    "4- payment terms \n" +
                    "5- supplier's name \n" +
                    "6 - supplier's address"
            ));

            while (infoToUpdate > 6 || infoToUpdate < 1) {
                infoToUpdate = Integer.parseInt(JOptionPane.showInputDialog("please select what info you want to edit:\n" +
                        "1- delivery term \n" +
                        "2- BN number \n" +
                        "3- bank account number \n" +
                        "4- payment terms \n" +
                        "5- supplier's name \n" +
                        "6 - supplier's address"
                ));

            }

            if (infoToUpdate == 1) {
                int deliveryTerm = Integer.parseInt(JOptionPane.showInputDialog("please select supplier Delivery terms \n" +
                        "1 - no_delivery,\n" +
                        "2 - delivery By Order,\n" +
                        "3 - delivery on regular days;  \n"
                ));
                while (deliveryTerm > 3 || deliveryTerm < 1) {
                    infoToUpdate = Integer.parseInt(JOptionPane.showInputDialog("please select supplier Delivery terms \n" +
                            "1 - no_delivery,\n" +
                            "2 - delivery By Order,\n" +
                            "3 - delivery on regular days;  \n"
                    ));
                }
                boolean canDeliver = false;
                String days = "";
                if (deliveryTerm == 1) {
                    canDeliver = false;
                    days = "";
                } else if (deliveryTerm == 2) {
                    canDeliver = true;
                    days = "";
                } else {
                    canDeliver = true;
                    JOptionPane.showMessageDialog(view.panel1, "please select the days the supplier supply");
                    int sunday = JOptionPane.showConfirmDialog(view.panel1, "Sunday");
                    //System.out.println(sunday);
                    if (sunday==-1){
                        throw new Exception("");
                    }
                    if (sunday == 0) {
                        days = days + 0;
                    }
                    int monday = JOptionPane.showConfirmDialog(view.panel1, "Monday");
                    if (monday==-1){
                        throw new Exception("");
                    }
                    if (monday == 0) {
                        days = days + 1;
                    }
                    int tuesday = JOptionPane.showConfirmDialog(view.panel1, "Tuesday");
                    if (tuesday==-1){
                        throw new Exception("");
                    }
                    if (tuesday == 0) {
                        days = days + 2;
                    }
                    int wednesday = JOptionPane.showConfirmDialog(view.panel1, "Wednesday");
                    if (wednesday==-1){
                        throw new Exception("");
                    }
                    if (wednesday == 0) {
                        days = days + 3;
                    }
                    int thursday = JOptionPane.showConfirmDialog(view.panel1, "Thursday");
                    if (thursday==-1){
                        throw new Exception("");
                    }
                    if (thursday == 0) {
                        days = days + 4;
                    }
                    int friday = JOptionPane.showConfirmDialog(view.panel1, "Friday");
                    if (friday==-1){
                        throw new Exception("");
                    }
                    if (friday == 0) {
                        days = days + 5;
                    }
                    int saturday = JOptionPane.showConfirmDialog(view.panel1, "Saturday");
                    if (saturday==-1){
                        throw new Exception("");
                    }
                    if (saturday == 0) {
                        days = days + 6;
                    }
                }

                success = model.editSupplierCanDeliver(supplierId, canDeliver, days);
            } else if (infoToUpdate == 2) {
                int supplierBnNumber = Integer.parseInt(JOptionPane.showInputDialog("please enter the supplier BN number: \n"
                ));
                success = model.editSupplierBnNumber(supplierId, supplierBnNumber);
            } else if (infoToUpdate == 3) {
                int accountNumber = Integer.parseInt(JOptionPane.showInputDialog("please enter the supplier bank account number: \n"
                ));
                success = model.editSupplierBankAccount(supplierId, accountNumber);
            } else if (infoToUpdate == 4) {
                PaymentOptions paymentOptions = PaymentOptions.plus_60;
                int payment = Integer.parseInt(JOptionPane.showInputDialog("please select supplier payment terms \n" +
                        "1 - regular,\n" +
                        "2 - shotef plus 30,\n" +
                        "3 - shotef plus 60;  \n"
                ));
                while (payment > 3 || payment < 1) {
                    payment = Integer.parseInt(JOptionPane.showInputDialog("please select supplier payment terms \n" +
                            "1 - regular,\n" +
                            "2 - shotef plus 30,\n" +
                            "3 - shotef plus 60;  \n"
                    ));
                }
                if (payment == 1) {
                    paymentOptions = PaymentOptions.regular;
                } else if (payment == 2) {
                    paymentOptions = PaymentOptions.plus_30;
                } else {
                    paymentOptions = PaymentOptions.plus_60;
                }

                success = model.editSupplierPaymentTerms(supplierId, paymentOptions);
            } else if (infoToUpdate == 5) {
                String supplierName = JOptionPane.showInputDialog("please enter the supplier's new name");
                if (supplierName == null){
                    throw new Exception("");
                }
                success = model.editSupplierName(supplierId, supplierName);
            } else {
                String supplierAddress = JOptionPane.showInputDialog("please enter supplier's new address");
                if (supplierAddress == null){
                    throw new Exception("");
                }
                success = model.editSupplierAddress(supplierId, supplierAddress);
            }

            if (success) {
                JOptionPane.showMessageDialog(view.panel1, "the supplier details have been edited successfully");
            } else {
                JOptionPane.showMessageDialog(view.panel1, "an error occurred, please check your details and try again");
            }
        }
        catch (Exception e){

        }


    }

    public void removeDiscountFromSupplier(){
        try {
            int supplierId = Integer.parseInt(JOptionPane.showInputDialog("please enter supplier Id + " +
                    model.printSupplierForGUI()));
            int isProductDiscount_int = JOptionPane.showConfirmDialog(view.panel1, "does the discount you want to remove is for specific product?");
            boolean isProduct = false;
            String productId = "";
            if (isProductDiscount_int == 0) {
                isProduct = true;
                productId = JOptionPane.showInputDialog("please enter productId");
            }
            String discounts = model.getDiscountToPrint(supplierId, isProduct, productId);
            if (discounts.charAt(0) != 'n') {
                int discountId = Integer.parseInt(JOptionPane.showInputDialog("please enter discount Id \n" + discounts));

                boolean success = false;
                if (!isProduct) {
                    success = model.removeDiscountFromSupplier(supplierId, discountId);
                } else {
                    success = model.removeDiscountFromProduct(supplierId, productId, discountId);
                }
                if (success) {
                    JOptionPane.showMessageDialog(panel1, "the discount was removed successfully");
                } else {
                    JOptionPane.showMessageDialog(panel1, "an error occurred please check your details and try again");
                }
            } else {
                JOptionPane.showMessageDialog(panel1, discounts);
            }
        }
        catch (Exception e){

        }




    }

    public void removeSupplierContact(){
        try {
            boolean success = false;
            int supplierId = Integer.parseInt(JOptionPane.showInputDialog("you choose to edit a supplier from the system \n" +
                    "please select your supplier Id: \n"
                    + model.printSupplierForGUI()
            ));
            int contactId = Integer.parseInt(JOptionPane.showInputDialog("please enter the contact Id: \n"
            ));
            success = model.removeContactFromSupplier(supplierId, contactId);
            if (success) {
                JOptionPane.showMessageDialog(panel1, "the contact was removed successfully");
            } else {
                JOptionPane.showMessageDialog(panel1, "an error occurred please check your details and try again");
            }
        }
        catch (Exception e){

        }
    }




    public void addDiscountToProduct() {
        try {
            int supplierId = 0;
            boolean parseSuccess = false;
            String supplierIdSTr = JOptionPane.showInputDialog("please enter supplier Id \n" +
                    model.printSupplierForGUI());
            while (!parseSuccess) {
                if (supplierIdSTr == null) {
                    throw new Exception("");
                }
                try {
                    supplierId = Integer.parseInt(supplierIdSTr);
                    parseSuccess = true;
                } catch (Exception e) {
                    supplierIdSTr = JOptionPane.showInputDialog("please enter valid number \n"+

                            "please select your supplier Id: \n"
                            + model.printSupplierForGUI()
                    );

                }
            }

            String productId = JOptionPane.showInputDialog("please enter productId");
            if (productId == null){
                throw new Exception("");
            }

            int isPercentage_int = JOptionPane.showConfirmDialog(panel1, "is it % Discount");
            boolean isPercentage = true;
            Double amount = Double.parseDouble(JOptionPane.showInputDialog("please enter the minimum amount for this discount"));
            Double price = 1.2;
            if (isPercentage_int == 0) {
                isPercentage = true;
                price = Double.parseDouble(JOptionPane.showInputDialog("please enter the discount amount"));
            } else {
                isPercentage = false;
                price = Double.parseDouble(JOptionPane.showInputDialog("please enter the price after the Discount"));
            }
            boolean success = model.addDiscountToProduct(supplierId, productId, amount, price, isPercentage);
            if (success) {
                JOptionPane.showMessageDialog(panel1, "the discount was added successfully");
            } else {
                JOptionPane.showMessageDialog(panel1, "an error occurred please check your details and try again");
            }
        }
        catch (Exception e){

        }
    }






    public void addContactToSupplier(){
        try {


            int supplierId = Integer.parseInt(JOptionPane.showInputDialog("please enter supplier Id : \n" +model.printSupplierForGUI() ));
            String contactName = JOptionPane.showInputDialog("please enter contact name");
            if (contactName == null){
                throw new Exception("");
            }
            String phoneNumber = JOptionPane.showInputDialog("please enter contact phone number");
            if (phoneNumber == null){
                throw new Exception("");
            }
            boolean success = model.addContactToSupplier(supplierId, contactName, phoneNumber);
            if (success) {
                JOptionPane.showMessageDialog(panel1, "the contact was added successfully");
            } else {
                JOptionPane.showMessageDialog(panel1, "an error occurred please check your details and try again");
            }
        }
        catch (Exception e){

        }
    }

    public void addDiscountToSupplier(){
        try {
            int supplierId = Integer.parseInt(JOptionPane.showInputDialog("please enter supplier Id \n" +
                    model.printSupplierForGUI()));
            int isPercentage_int = JOptionPane.showConfirmDialog(panel1, "is it % Discount");
            boolean isPercentage = true;
            Double amount = Double.parseDouble(JOptionPane.showInputDialog("please enter the minimum amount for this discount"));
            Double price = 1.2;
            if (isPercentage_int == 0) {
                isPercentage = true;
                price = Double.parseDouble(JOptionPane.showInputDialog("please enter the discount amount"));
            } else {
                isPercentage = false;
                price = Double.parseDouble(JOptionPane.showInputDialog("please enter the price after the Discount"));
            }
            boolean success = model.addDiscountToSupplier(supplierId, amount, price, isPercentage);
            if (success) {
                JOptionPane.showMessageDialog(panel1, "the discount was added successfully");
            } else {
                JOptionPane.showMessageDialog(panel1, "an error occurred please check your details and try again");
            }
        }
        catch (Exception e){

        }
    }


    public void addProductToSupplier(){
        try {
            int supplierId = Integer.parseInt(JOptionPane.showInputDialog("please enter supplier Id \n" +
                    model.printSupplierForGUI()));
            String productId = JOptionPane.showInputDialog("please enter the product Id you want to add");
            if (productId == null){
                throw new Exception("");
            }
            int catalogNumber = Integer.parseInt(JOptionPane.showInputDialog("please enter the product catalog number"));
            int amount = Integer.parseInt(JOptionPane.showInputDialog("please enter the amount you can supply"));
            double price = Double.parseDouble(JOptionPane.showInputDialog("please enter the price of the product"));
            boolean success = model.addProductToSupplier(supplierId, productId, catalogNumber, amount, price);
            if (success) {
                JOptionPane.showMessageDialog(panel1, "the product was added successfully");
            } else {
                JOptionPane.showMessageDialog(panel1, "an error occurred please check your details and try again");
            }
        }
        catch (Exception e){

        }
    }

    public void removeProductFromSupplier(){
        try {
            int supplierId = Integer.parseInt(JOptionPane.showInputDialog("please enter supplier Id \n" +
                    model.printSupplierForGUI()));
            String productId = JOptionPane.showInputDialog("please enter the product Id you want to remove");
            if (productId == null){
                throw new Exception("");
            }
            boolean success = model.removeProductFromSupplier(supplierId, productId);
            if (success) {
                JOptionPane.showMessageDialog(panel1, "the product was removed successfully");
            } else {
                JOptionPane.showMessageDialog(panel1, "an error occurred please check your details and try again");
            }
        }
        catch (Exception e){

        }
    }

    public void lookAtLastOrder(){
        String toPrint = model.showAllOrders();
        System.out.println(toPrint);
        JOptionPane.showMessageDialog(panel1, toPrint);
    }

    public void removeSupplier(){
        try {
            int supplierId = Integer.parseInt(JOptionPane.showInputDialog("please enter supplier Id \n" +
                    model.printSupplierForGUI()));
            boolean success = model.removeSupplier(supplierId);
            if (success) {
                JOptionPane.showMessageDialog(panel1, "the supplier was added successfully");
            } else {
                JOptionPane.showMessageDialog(panel1, "an error occurred please check your details and try again");
            }
        }
        catch (Exception e) {

        }

    }


    public  void addSupplier(){
        try {
            int supplierId = 0;
            boolean parseSuccess = false;
            String supplierIdStr= JOptionPane.showInputDialog(null,"please enter supplier Id",
                    "Supplier Id", JOptionPane.DEFAULT_OPTION
            );
            while (!parseSuccess) {
                if (supplierIdStr == null) {
                    throw new Exception("");
                }
                try {
                    supplierId = Integer.parseInt(supplierIdStr);
                    parseSuccess = true;
                } catch (Exception e) {
                    supplierIdStr = JOptionPane.showInputDialog("please enter valid number \n"+
                            "please select your supplier Id: \n"
                            + model.printSupplierForGUI()
                    );

                }
            }
            int delivery= 0 ;
            parseSuccess = false;
            String delivertParse = JOptionPane.showInputDialog(null,"please select supplier Delivery terms \n" +
                    "1 - no_delivery,\n" +
                    "2 - delivery By Order,\n" +
                    "3 - delivery on regular days;  \n", "Delivery terms", JOptionPane.DEFAULT_OPTION
            );
            while ((delivery > 3 || delivery < 1) & !parseSuccess) {
                if (delivertParse == null) {
                    throw new Exception("");
                }
                try {
                    delivery = Integer.parseInt(delivertParse);
                    if (delivery>0 && delivery <4) {
                        parseSuccess = true;
                    }
                    else {
                        delivertParse = JOptionPane.showInputDialog(null, "please enter valid number \n" +
                                "please select supplier Delivery terms  +\n" +
                                "1 - no_delivery,\n" +
                                "2 - delivery By Order,\n" +
                                "3 - delivery on regular days;" + "\n", "Delivery terms", JOptionPane.DEFAULT_OPTION
                        );
                    }
                } catch (Exception e) {
                    delivertParse = JOptionPane.showInputDialog(null, "please enter valid number \n" +
                            "please select supplier Delivery terms  +\n" +
                            "1 - no_delivery,\n" +
                            "2 - delivery By Order,\n" +
                            "3 - delivery on regular days;" + "\n", "Delivery terms", JOptionPane.DEFAULT_OPTION
                    );
                }
            }
            boolean canDeliver = false;
            String days = "";
            if (delivery == 1) {
                canDeliver = false;
                days = "";
            } else if (delivery == 2) {
                canDeliver = true;
                days = "";
            } else {
                canDeliver = true;
                JOptionPane.showMessageDialog(panel1, "please select the days the supplier supply");
                int sunday = JOptionPane.showConfirmDialog(panel1, "Sunday");
                if (sunday==-1){
                    throw new IllegalArgumentException("");
                }
                if (sunday == 0) {
                    days = days + 0;
                }
                int monday = JOptionPane.showConfirmDialog(panel1, "Monday");
                if (monday==-1){
                    throw new IllegalArgumentException("");
                }
                if (monday == 0) {
                    days = days + 1;
                }
                int tuesday = JOptionPane.showConfirmDialog(panel1, "Tuesday");
                if (tuesday==-1){
                    throw new Exception("");
                }
                if (tuesday == 0) {
                    days = days + 2;
                }
                int wednesday = JOptionPane.showConfirmDialog(panel1, "Wednesday");
                if (wednesday==-1){
                    throw new Exception("");
                }
                if (wednesday == 0) {
                    days = days + 3;
                }
                int thursday = JOptionPane.showConfirmDialog(panel1, "Thursday");
                if (thursday==-1){
                    throw new Exception("");
                }
                if (thursday == 0) {
                    days = days + 4;
                }

                int friday = JOptionPane.showConfirmDialog(panel1, "Friday");
                if (friday==-1){
                    throw new Exception("");
                }
                if (friday == 0) {
                    days = days + 5;
                }

                int saturday = JOptionPane.showConfirmDialog(panel1, "Saturday");
                if (saturday==-1){
                    throw new Exception("");
                }
                if (saturday == 0) {
                    days = days + 6;
                }
            }
            parseSuccess = false;
            int supplierBNNumber = 0;
            String supplierBNNumberString = JOptionPane.showInputDialog(null,"please enter supplier BN number", "BN number", JOptionPane.DEFAULT_OPTION);
            while (!parseSuccess) {
                if (supplierBNNumberString == null) {
                    throw new Exception("");
                }
                try {
                    supplierBNNumber = Integer.parseInt(supplierBNNumberString);
                    parseSuccess = true;
                } catch (Exception e) {
                    supplierBNNumberString = JOptionPane.showInputDialog("please enter valid number \n"+
                            "please enter supplier BN number"
                    );

                }
            }

            parseSuccess = false;
            int bankAccount = 0;
            String bankAccountStr = JOptionPane.showInputDialog(null,"please enter supplier bank account number", "Bank account number", JOptionPane.DEFAULT_OPTION);;
            while (!parseSuccess) {
                if (bankAccountStr == null) {
                    throw new Exception("");
                }
                try {
                    bankAccount = Integer.parseInt(bankAccountStr);
                    parseSuccess = true;
                } catch (Exception e) {
                    bankAccountStr = JOptionPane.showInputDialog("please enter valid number \n"+
                            "please enter supplier bank account number"
                    );

                }
            }

            //int bankAccount = Integer.parseInt(JOptionPane.showInputDialog(null,"please enter supplier bank account number", "Bank account number", JOptionPane.DEFAULT_OPTION));
            PaymentOptions paymentOptions = PaymentOptions.plus_60;
            int paymentOpt= 0 ;
            parseSuccess = false;
            String paymentOptSTR =JOptionPane.showInputDialog(null,"please select supplier payment terms \n" +
                            "1 - regular,\n" +
                            "2 - shotef plus 30,\n" +
                            "3 - shotef plus 60;  \n", "Payment terms", JOptionPane.DEFAULT_OPTION
                    );
            while ((paymentOpt > 3 || paymentOpt < 1) & !parseSuccess) {
                if (delivertParse == null) {
                    throw new Exception("");
                }
                try {
                    paymentOpt = Integer.parseInt(paymentOptSTR);
                    if (paymentOpt>0 && paymentOpt <4) {
                        parseSuccess = true;
                    }
                    else {
                        paymentOptSTR = JOptionPane.showInputDialog(null,
                                "please select supplier payment terms \n" +
                                "1 - regular,\n" +
                                "2 - shotef plus 30,\n" +
                                "3 - shotef plus 60;  \n" + "\n", "Delivery terms", JOptionPane.DEFAULT_OPTION
                        );
                    }
                } catch (Exception e) {
                    paymentOptSTR = JOptionPane.showInputDialog(null, "please enter valid number \n" +
                            "please select supplier payment terms \n" +
                            "1 - regular,\n" +
                            "2 - shotef plus 30,\n" +
                            "3 - shotef plus 60;  \n" + "\n", "Delivery terms", JOptionPane.DEFAULT_OPTION
                    );
                }
            }
            int payment = paymentOpt;
            if (payment == 1) {
                paymentOptions = PaymentOptions.regular;
            } else if (payment == 2) {
                paymentOptions = PaymentOptions.plus_30;
            } else {
                paymentOptions = PaymentOptions.plus_60;
            }
            String contactName = JOptionPane.showInputDialog(null,"please enter supplier's contact name",
                    "Contact name", JOptionPane.DEFAULT_OPTION);
            if (contactName == null){
                throw new Exception("");
            }
            String contactPhone = JOptionPane.showInputDialog(null,"please enter supplier's contact phone number",
                    "Contact's phone number", JOptionPane.DEFAULT_OPTION);
            if (contactPhone == null){
                throw new Exception("");
            }
            String supplierName = JOptionPane.showInputDialog(null,"please enter supplier's name",
                    "Supplier's name", JOptionPane.DEFAULT_OPTION);
            if (supplierName == null){
                throw new Exception("");
            }
            String supplierAddress = JOptionPane.showInputDialog(null,"please enter supplier's address",
                    "Supplier Address", JOptionPane.DEFAULT_OPTION);
            if (supplierAddress == null){
                throw new Exception("");
            }

            boolean success = model.addSupplier(supplierId, canDeliver, days, supplierBNNumber, bankAccount, paymentOptions,
                    supplierName, contactName, contactPhone, supplierAddress);
            if (success) {
                JOptionPane.showMessageDialog(panel1, "the supplier was added successfully");
            } else {
                JOptionPane.showMessageDialog(panel1, "an error occurred please check your details and try again");
            }
        }
        catch (Exception e){

        }
    }
}