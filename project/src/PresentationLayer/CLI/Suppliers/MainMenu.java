package PresentationLayer.CLI.Suppliers;

import BusinessLayer.Suppliers.PaymentOptions;
import ServiceLayer.Suppliers.SupplierControllerService;

import java.util.Scanner;

public class MainMenu {
        private SupplierControllerService suppliersController;

    public MainMenu() {
        this.suppliersController = new SupplierControllerService();
    }

    public void startMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("hey, welcome to the Suppliers System \n" +
                "please select your next action: \n" +
                "1 - add new Supplier \n" +
                "2 - remove supplier from the system \n" +
                "3 - print all orders \n" +
                "4 - edit existing supplier \n" +
                "5 - load system data \n" +
                "6 - add products to supplier \n" +
                "7 - add new discount to supplier \n" +
                "8 - add new contact to supplier \n" +
                "9 - remove contact from supplier \n" +
                "10 - remove product from supplier \n" +
                "0 - exit from the system"
        );
        int option = scanner.nextInt();
        while (option < 0|| option >10 ){
            System.out.println("please select an option from the List - \n" +
                            "please select your next action: \n" +
                            "1 - add new Supplier \n" +
                            "2 - remove supplier from the system \n" +
                            "3 - print all orders \n" +
                            "4 - edit existing supplier \n" +
                            "5 - load system data \n" +
                            "6 - add products to supplier \n" +
                            "7 - add new discount to supplier \n" +
                            "8 - add new contact to supplier \n" +
                            "9 - remove contact from supplier \n" +
                            "10 - remove product from supplier \n" +
                            "0 - exit from the system");

                    option  = scanner.nextInt();
        }
        if (option == 0){
            return;
        }
        if (option == 1) {
            addNewSupplier();
            System.out.println();
            startMenu();
        } else if (option == 2) {
            removeSupplier();
            System.out.println();
            startMenu();

        } else if (option == 3) {
            printOrders();
            System.out.println();
            startMenu();
        } else if (option == 4) {
            editSupplier();
            System.out.println();
            startMenu();
        } else if (option == 5) {
            setUP();
            System.out.println();
            startMenu();
        } else if (option == 6) {
            addProductsToSupplier();
            System.out.println();
            startMenu();
        }
        else if (option == 7){
            addDiscountToSupplier();
            System.out.println();
            startMenu();
        }
        else if (option == 8){
            addContact();
            System.out.println();
            startMenu();
        }
        else if (option == 9){
            removeContact();
            System.out.println();
            startMenu();
        }
        else if (option == 10){
            removeProductFromSupplier();
            System.out.println();
            startMenu();
        }


    }

    public void alertPeriodOrders(){
        suppliersController.alertPeriodOrders();
    }

    public void printOrders(){
        System.out.println("you choose to print all suppliers Orders \n" +
                "please enter the supplierId : \n");
        suppliersController.printSuppliers();
        Scanner scanner = new Scanner(System.in);
        int supplierId = scanner.nextInt();
        suppliersController.printAllOrders(supplierId);
    }


    public void addDiscountToSupplier(){
        System.out.println("you choose to add new discount to supplier \n" +
                "please enter the supplierId : \n");
        suppliersController.printSuppliers();
        Scanner scanner = new Scanner(System.in);
        int supplierId = scanner.nextInt();
        System.out.println("please select your discount type: \n" +
                "1- add discount for specific product \n" +
                "2- add discount fot total price order");
        int product_Order = 3;
        String productId= "---";
        boolean discountType1=true;
       // Discount.DiscountType discountTypeDis = Discount.DiscountType.buy_amount_get_discount_Per_product;
        while (product_Order!= 1 && product_Order!=2) {
            product_Order = scanner.nextInt();
            if (product_Order == 1) {
                System.out.println("please enter the product Id \n");
                scanner.nextLine();
                productId = scanner.nextLine();
                int discountType = 3;
                while (discountType!= 1 &&discountType!=2 ) {
                    System.out.println("please select your discount type: \n" +
                            "1- buy x amount get y% Discount\n" +
                            "2- buy x amount get y Discount");
                    discountType =scanner.nextInt();
                    if (discountType == 1){
                       discountType1 = true;
                    }
                    if (discountType==2){
                       discountType1= false;
                    }
                };

            }
            if (product_Order == 2) {
                int discountType = 3;
                while (discountType!= 1 && discountType!=2 ) {
                    System.out.println("please select your discount type: \n" +
                            "1- buy in total price x get y% Discount\n" +
                            "2- buy in total price x get y Discount");
                    discountType =scanner.nextInt();
                    if (discountType == 1){
                        discountType1 = true;
                    }
                    if (discountType==2){
                        discountType1= false;
                    }
                };
            }

        }
        boolean success = true;
        System.out.println("please enter the minimum amount for the discount (x)");
        double amount =scanner.nextDouble();
        System.out.println("please enter the discount number (y)");
        double discount =scanner.nextDouble();
        if (product_Order== 2 ){
            success = suppliersController.addDiscountToOrder(supplierId, amount,discount , discountType1 );
        }
        else {
            success =suppliersController.addDiscountToProduct(supplierId, productId, amount ,discount ,discountType1 );
        }
        if (success){
            System.out.println("the discount was added successfully" );
        }
    }

    public void removeAllData() {
        suppliersController.removeAllData();
    }

    public void setUP() {
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

        this.suppliersController = new SupplierControllerService();
        if (success) {
            System.out.println("the information was loaded successfully");
        }
    }

    public void editSupplier() {
        boolean success = false;
        System.out.println("you choose to edit a supplier from the system \n" +
                "please select your supplier Id: \n");
        suppliersController.printSuppliers();
        Scanner scanner = new Scanner(System.in);
        int supplierId = scanner.nextInt();
        System.out.println("please select what info you want to edit:\n" +
                "1- delivery term \n" +
                "2- BN number \n" +
                "3- bank account number \n" +
                "4- payment terms \n" +
                "5- supplier's name \n" +
                "6 - supplier's address"
        );
        int optionToEdit = scanner.nextInt();
        while (optionToEdit< 1 || optionToEdit> 6){
            System.out.println("please select what info you want to edit:\n" +
                    "1- delivery term \n" +
                    "2- BN number \n" +
                    "3- bank account number \n" +
                    "4- payment terms \n" +
                    "5- supplier name \n" +
                    "6 - supplier's address"
            );
            optionToEdit = scanner.nextInt();
        }
        if (optionToEdit == 1) {
            System.out.println("please select supplier Delivery terms \n" +
                    "1 - no_delivery,\n" +
                    "2 - delivery By Order,\n" +
                    "3 - delivery on regular days;  \n"
            );
            int deliveryTermsScanner = scanner.nextInt();
            while (deliveryTermsScanner<1 || deliveryTermsScanner>3 ){
                System.out.println("please select supplier Delivery terms \n" +
                        "1 - no_delivery,\n" +
                        "2 - delivery By Order,\n" +
                        "3 - delivery on regular days;  \n"
                );
                deliveryTermsScanner = scanner.nextInt();
            }
            boolean canDeliver = false;
            String days = "";
            //DeliveryTerms deliveryTerms = DeliveryTerms.no_delivery;
            if (deliveryTermsScanner == 1) {
                canDeliver = false;
                days = "";

            } else if (deliveryTermsScanner == 2) {
                canDeliver = true;
                days="";
            } else if (deliveryTermsScanner == 3) {
                canDeliver = true;
                int dayOfOrder=-1;
                System.out.println("Please choose the days the supplier can deliver");
                System.out.println("for each day, press:\n" +
                        "1 - can deliver on this day \n" +
                        "2 - can't deliver on this day \n");

                System.out.println("Sunday");
                 dayOfOrder = scanner.nextInt();
                while (dayOfOrder<1 || dayOfOrder>2 ){
                    System.out.println("for each day, press:\n" +
                            "1 - can deliver on this day \n" +
                            "2 - can't deliver on this day \n");
                    System.out.println("Sunday");
                    dayOfOrder = scanner.nextInt();}

                if(dayOfOrder==1){
                    days = days + 1;
                }


                System.out.println("Monday");
                dayOfOrder = scanner.nextInt();
                while (dayOfOrder<1 || dayOfOrder>2 ){
                    System.out.println("for each day, press:\n" +
                            "1 - can deliver on this day \n" +
                            "2 - can't deliver on this day \n");
                    System.out.println("Monday");
                    dayOfOrder = scanner.nextInt();}

                if(dayOfOrder==1){
                    days = days + 2;
                }

                System.out.println("Tuesday");
                dayOfOrder = scanner.nextInt();
                while (dayOfOrder<1 || dayOfOrder>2 ){
                    System.out.println("for each day, press:\n" +
                            "1 - can deliver on this day \n" +
                            "2 - can't deliver on this day \n");
                    System.out.println("Tuesday");
                    dayOfOrder = scanner.nextInt();}

                if(dayOfOrder==1){
                    days = days + 3;
                }

                System.out.println("Wednesday");
                dayOfOrder = scanner.nextInt();
                while (dayOfOrder<1 || dayOfOrder>2 ){
                    System.out.println("for each day, press:\n" +
                            "1 - can deliver on this day \n" +
                            "2 - can't deliver on this day \n");
                    System.out.println("Wednesday");
                    dayOfOrder = scanner.nextInt();}

                if(dayOfOrder==1){
                    days = days + 4;
                }

                System.out.println("Thursday");
                dayOfOrder = scanner.nextInt();
                while (dayOfOrder<1 || dayOfOrder>2 ){
                    System.out.println("for each day, press:\n" +
                            "1 - can deliver on this day \n" +
                            "2 - can't deliver on this day \n");
                    System.out.println("Thursday");
                    dayOfOrder = scanner.nextInt();}

                if(dayOfOrder==1){
                    days = days + 5;
                }

                System.out.println("Friday");
                dayOfOrder = scanner.nextInt();
                while (dayOfOrder<1 || dayOfOrder>2 ){
                    System.out.println("for each day, press:\n" +
                            "1 - can deliver on this day \n" +
                            "2 - can't deliver on this day \n");
                    System.out.println("Friday");
                    dayOfOrder = scanner.nextInt();}

                if(dayOfOrder==1){
                    days = days + 6;
                }

                System.out.println("Saturday");
                dayOfOrder = scanner.nextInt();
                while (dayOfOrder<1 || dayOfOrder>2 ){
                    System.out.println("for each day, press:\n" +
                            "1 - can deliver on this day \n" +
                            "2 - can't deliver on this day \n");
                    System.out.println("Saturday");
                    dayOfOrder = scanner.nextInt();}

                if(dayOfOrder==1){
                    days = days + 7;
                }

            }
            success = suppliersController.editSupplierCanDeliver(supplierId, canDeliver, days);
        } else if (optionToEdit == 2) {
            System.out.println("please enter the supplier BN number: \n"
            );
            int bnNum = scanner.nextInt();
            success = suppliersController.editSupplierBnNumber(supplierId, bnNum);
        } else if (optionToEdit == 3) {
            System.out.println("please enter the supplier bank account number: \n"
            );
            int bankAccount = scanner.nextInt();
            success = suppliersController.editSupplierBankAccount(supplierId, bankAccount);
        } else if (optionToEdit == 4) {
            System.out.println("please select supplier Payment terms \n" +
                    "1 - regular,\n" +
                    "2 - shotef plus 30,\n" +
                    "3 - shotef plus 60;  \n"
            );
            int paymentOptionScanner = scanner.nextInt();
            while (paymentOptionScanner<1 || paymentOptionScanner>3){
                System.out.println("please select supplier Payment terms \n" +
                        "1 - regular,\n" +
                        "2 - shotef plus 30,\n" +
                        "3 - shotef plus 60;  \n"
                );
                paymentOptionScanner = scanner.nextInt();
            }
            PaymentOptions payOption = PaymentOptions.regular;
            if (paymentOptionScanner == 1) {
                payOption = PaymentOptions.regular;
            } else if (paymentOptionScanner == 2) {
                payOption = PaymentOptions.plus_30;
            } else if (paymentOptionScanner == 3) {
                payOption = PaymentOptions.plus_60;
            }
            success = suppliersController.editSupplierPaymentTerms(supplierId, payOption);
        } else if (optionToEdit == 5) {
            System.out.println("please enter new supplier name and then press enter: \n");
            scanner.nextLine();
            String supplierName = scanner.nextLine();
            success = suppliersController.editSupplierName(supplierId, supplierName);

        }
        else{
            System.out.println("please enter supplier new address and then press enter: \n");
            scanner.nextLine();
            String supplierAddress = scanner.nextLine();
            success = suppliersController.editSupplierAddress(supplierId, supplierAddress);
        }
        if(success){
        System.out.println("the change was made successfully");}

    }





    public void removeSupplier(){

        System.out.println("you choose to remove a supplier from the system \n" +
                "please select your supplier Id: \n");
        Scanner scanner = new Scanner(System.in);
        suppliersController.printSuppliers();
        int supplierId = scanner.nextInt();

        boolean success = suppliersController.RemoveSupplier(supplierId);
        if (success){
            System.out.println("the supplier has removed successfully");
        }

    }

    public void addContact () {
        Scanner scanner = new Scanner(System.in);
        System.out.println("you choose to add new contact to supplier \n") ;
        suppliersController.printSuppliers();
        System.out.println("please enter the supplier Id: \n");
        int supplierId = scanner.nextInt();
        System.out.println("please enter the contact name: \n");
        scanner.nextLine();
        String contactName = scanner.nextLine();
        System.out.println("please enter the contact phone number: \n");
        String phoneNumber = scanner.nextLine();
        boolean success = suppliersController.addContactToSupplier(supplierId ,contactName ,phoneNumber);
        if (success){
            System.out.println("the contact was added successfully");
        }
    }
    public void removeContact () {
        Scanner scanner = new Scanner(System.in);
        System.out.println("you choose to remove contact from supplier \n") ;
        suppliersController.printSuppliers();
        System.out.println("please enter the supplier Id: \n");
        int supplierId = scanner.nextInt();
        boolean success1 = suppliersController.printSupplierContacts(supplierId);
        if (!success1){
            return;
        }
        System.out.println("please enter the contact Id: \n");
        int contactId = scanner.nextInt();
        boolean success2 =suppliersController.removeContactFromSupplier(supplierId , contactId);
        if (success2){
            System.out.println("contact removed successfully");
        }

    }




    public void addNewSupplier (){
        Scanner scanner = new Scanner(System.in);
        System.out.println("you choose to add new supplier to the system \n" +
                "please enter the supplier Id: \n");
        int supplierId = scanner.nextInt();


        System.out.println("please select supplier Delivery terms \n" +
                "1 - no_delivery,\n" +
                "2 - delivery By Order,\n" +
                "3 - delivery on regular days;  \n"
        );
        int deliveryTermsScanner = scanner.nextInt();
        while (deliveryTermsScanner<1 || deliveryTermsScanner>3 ){
            System.out.println("please select supplier Delivery terms \n" +
                    "1 - no_delivery,\n" +
                    "2 - delivery By Order,\n" +
                    "3 - delivery on regular days;  \n"
            );
            deliveryTermsScanner = scanner.nextInt();
        }
        boolean canDeliver = false;
        String days = "";
        //DeliveryTerms deliveryTerms = DeliveryTerms.no_delivery;
        if (deliveryTermsScanner == 1) {
            canDeliver = false;
            days = "";

        } else if (deliveryTermsScanner == 2) {
            canDeliver = true;
            days="";
        } else {
            canDeliver = true;
            int dayOfOrder = -1;
            System.out.println("Please choose the days the supplier can deliver");
            System.out.println("for each day, press:\n" +
                    "1 - can deliver on this day \n" +
                    "2 - can't deliver on this day \n");

            System.out.println("Sunday");
            dayOfOrder = scanner.nextInt();
            while (dayOfOrder < 1 || dayOfOrder > 2) {
                System.out.println("for each day, press:\n" +
                        "1 - can deliver on this day \n" +
                        "2 - can't deliver on this day \n");
                System.out.println("Sunday");
                dayOfOrder = scanner.nextInt();
            }

            if (dayOfOrder == 1) {
                days = days + 0;
            }


            System.out.println("Monday");
            dayOfOrder = scanner.nextInt();
            while (dayOfOrder < 1 || dayOfOrder > 2) {
                System.out.println("for each day, press:\n" +
                        "1 - can deliver on this day \n" +
                        "2 - can't deliver on this day \n");
                System.out.println("Monday");
                dayOfOrder = scanner.nextInt();
            }

            if (dayOfOrder == 1) {
                days = days + 1;
            }

            System.out.println("Tuesday");
            dayOfOrder = scanner.nextInt();
            while (dayOfOrder < 1 || dayOfOrder > 2) {
                System.out.println("for each day, press:\n" +
                        "1 - can deliver on this day \n" +
                        "2 - can't deliver on this day \n");
                System.out.println("Tuesday");
                dayOfOrder = scanner.nextInt();
            }

            if (dayOfOrder == 1) {
                days = days + 2;
            }

            System.out.println("Wednesday");
            dayOfOrder = scanner.nextInt();
            while (dayOfOrder < 1 || dayOfOrder > 2) {
                System.out.println("for each day, press:\n" +
                        "1 - can deliver on this day \n" +
                        "2 - can't deliver on this day \n");
                System.out.println("Wednesday");
                dayOfOrder = scanner.nextInt();
            }

            if (dayOfOrder == 1) {
                days = days + 3;
            }

            System.out.println("Thursday");
            dayOfOrder = scanner.nextInt();
            while (dayOfOrder < 1 || dayOfOrder > 2) {
                System.out.println("for each day, press:\n" +
                        "1 - can deliver on this day \n" +
                        "2 - can't deliver on this day \n");
                System.out.println("Thursday");
                dayOfOrder = scanner.nextInt();
            }

            if (dayOfOrder == 1) {
                days = days + 4;
            }

            System.out.println("Friday");
            dayOfOrder = scanner.nextInt();
            while (dayOfOrder < 1 || dayOfOrder > 2) {
                System.out.println("for each day, press:\n" +
                        "1 - can deliver on this day \n" +
                        "2 - can't deliver on this day \n");
                System.out.println("Friday");
                dayOfOrder = scanner.nextInt();
            }

            if (dayOfOrder == 1) {
                days = days + 5;
            }

            System.out.println("Saturday");
            dayOfOrder = scanner.nextInt();
            while (dayOfOrder < 1 || dayOfOrder > 2) {
                System.out.println("for each day, press:\n" +
                        "1 - can deliver on this day \n" +
                        "2 - can't deliver on this day \n");
                System.out.println("Saturday");
                dayOfOrder = scanner.nextInt();
            }

            if (dayOfOrder == 1) {
                days = days + 6;
            }
        }
        System.out.println("please enter the supplier BN number: \n");

        int bnNum = scanner.nextInt();
        System.out.println("please enter the supplier bank account number: \n"
        );
        int bankAccount = scanner.nextInt();
        System.out.println("please select supplier payment terms \n" +
                "1 - regular,\n" +
                "2 - shotef plus 30,\n" +
                "3 - shotef plus 60;  \n"
        );
        int paymentOptionScanner = scanner.nextInt();
        while (paymentOptionScanner<1 || paymentOptionScanner>3){
            System.out.println("please select supplier payment terms \n" +
                    "1 - regular,\n" +
                    "2 - shotef plus 30,\n" +
                    "3 - shotef plus 60;  \n"
            );
            paymentOptionScanner = scanner.nextInt();
        }
        PaymentOptions payOption = PaymentOptions.regular ;
        if (paymentOptionScanner == 1){
            payOption = PaymentOptions.regular;
        }
        else if (paymentOptionScanner == 2){
            payOption =PaymentOptions.plus_30 ;
        }
        else if (paymentOptionScanner == 3){
            payOption = PaymentOptions.plus_60;
        }
        System.out.println("please enter the contact name: \n");
        scanner.nextLine();
        String contactName = scanner.nextLine();
        System.out.println("please enter the contact phone number: \n");
        String phoneNumber = scanner.nextLine();


        System.out.print("please enter supplier's name and then press enter:\n");
        String supplierName = scanner.nextLine();

        System.out.print("please enter supplier's address and then press enter:\n");
        String supplierAddress = scanner.nextLine();



        boolean success =  suppliersController.addNewSupplier(supplierId ,canDeliver,days ,bnNum ,bankAccount ,payOption , supplierName , contactName ,phoneNumber, supplierAddress);
         if(success){
            System.out.println("the new supplier was added successfully\n");
        }
        else {
            System.out.println("an error occurred please check your details and try again");
        }
    }

    public void addProductsToSupplier(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("you choose to add products to suppliers \n" +
                "please enter the supplier Id: \n");
        int supplierId = scanner.nextInt();
        System.out.println("please enter the amount of this product that the supplier can supply:\n");
        int amount = scanner.nextInt();
        System.out.println("please enter price of the product:\n");
        double price = scanner.nextDouble();
        System.out.println("please enter the supplier catalog number of the product:\n");
        int catalogNumber = scanner.nextInt();
        System.out.println("please enter the productId:\n");
        scanner.nextLine();
        String productId = scanner.nextLine();
        boolean success = suppliersController.addProductToSupplier(supplierId,productId,catalogNumber,  amount , price);
        if (success){
            System.out.println("the product was added successfully to the supplier");
        }
    }

    public void removeProductFromSupplier(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("you choose to remove products from suppliers \n" +
                "please enter the supplier Id: \n");
        suppliersController.printSuppliers();
        int supplierId = scanner.nextInt();
        System.out.println("please enter the product serial number (product Id) you want to remove:\n");
        scanner.nextLine();
        String productId = scanner.nextLine();
        boolean success = suppliersController.removeProductFromSupplier(supplierId,productId);
        if (success){
            System.out.println("the product was removed successfully from the supplier");
        }
    }



}







