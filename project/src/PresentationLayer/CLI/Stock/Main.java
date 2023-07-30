package PresentationLayer.CLI.Stock;

import BusinessLayer.Stock.StockController;
import BusinessLayer.Tools.Pair;
import ServiceLayer.Stock.BranchService;
import ServiceLayer.Stock.StockService;

import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    public Main() { }

    public void startMenu(boolean shouldLoad) {
        BranchService bs = new BranchService();
        while(true){
            //Scanner scanner = new Scanner(System.in);
            //first choose restart or example data
            System.out.println("Welcome to Super-Li supply system!");
            /*System.out.println ("Please choose your desired initialization state:");
            System.out.println(" 1 : Restart");
            System.out.println(" 2 : Use example data");
            String state = scanner.nextLine();
            while(!state.equals("1") && !state.equals("2")){
                System.out.println("Illegal press. Please press again.");
                state = scanner.nextLine();
            }
            if(state.equals("1")){
                //restart case
            }
            else{
                if(state.equals("2")){
                //example data case
                    bs.loadData();
                }

            }*/
            startMenu(bs, shouldLoad);
            break;
            //scanner.close();
        }
        return;
    }



    /**
     * Converts String to Integer
     * @param read
     * @return
     */
    private static Integer insureIntInput(String read){
        Scanner scanner = new Scanner(System.in);
        Integer readInt;
        while(true){
            try{
                readInt = Integer.parseInt(read);
                //scanner.close();
                return readInt;
            }
            catch(Exception e){
                System.out.println("Illegal press. Please press again.");
                read = scanner.nextLine();
            }
        }
    }


    /**
     * Converts String to Double
     * @param read
     * @return
     */
    private static Double insureDoubleInput(String read){
        Scanner scanner = new Scanner(System.in);
        Double readDouble;
        while(true){
            try{
                readDouble = Double.parseDouble(read);
                //scanner.close();
                return readDouble;
            }
            catch(Exception e){
                System.out.println("Illegal press. Please press again.");
                read = scanner.nextLine();
            }
        }
    }


    /**
     * Converts String to Date, asks to re-enter string if not in a valid Date format
     * @param read
     * @return
     */
    private static Date insureDateInput(String read){
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        while(true){
            try {
                Date date = format.parse(read);
                //scanner.close();
                return date;
            } catch (java.text.ParseException e) {
                System.out.println("Invalid date: " + read + ". Please enter the date in the format dd-mm-yyyy.");
                read = scanner.nextLine();
            }
        }
    }

    /**
     *
     */
    public static void removeAllData() {
        BranchService bs = new BranchService();
        while (true) {
            try {
                bs.removeAllData();
                break;
            } catch (Exception ex) {
                System.out.println(ex.getMessage() + "Couldn't remove all data");
            }
        }
    }

    /**
     *
     */
    public static void setUp() {
        BranchService bs = new BranchService();
        while (true) {
            try {
                bs.setUp();
                break;
            } catch (Exception ex) {
                System.out.println(ex.getMessage() + "Couldn't set up data");
            }
        }
    }

    /**
     * Choose branch menu
     * @param bs
     * @return
     */
    private static StockService branchMenu(BranchService bs, boolean shouldLoad){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please choose: ");
        System.out.println(" 1 : Add new branch");
        System.out.println(" 2 : Choose existing branch");
        System.out.println(" 3 : Back");
        String read = scanner.nextLine();
        while(!read.equals("1") && !read.equals("2") && !read.equals("3")){
            System.out.println("Illegal press. Please press again.");
            read = scanner.nextLine();
        }
        if(read.equals("1")){
            return addBranch(bs);
        }
        else if(read.equals("2")){
            return selectBranch(bs, shouldLoad);
        }
        else{
            return null;
        }
    }


     /**
     * Runs secondaryMenu for the given branch
     * @param bs
     */
     private static void startMenu(BranchService bs, boolean shouldLoad) {
         while (true) {
             StockService ss = branchMenu(bs, shouldLoad);
             if (ss != null) {
                 boolean toContinue = secondaryMenu(ss);
                 while (toContinue) {
                     toContinue = secondaryMenu(ss);
                 }
             }
             else {
                 break;
             }
         }
         return;
     }


    /**
     * Choose desired action to preform menu
     * @param ss
     * @return
     */
    private static boolean secondaryMenu(StockService ss){
        System.out.println("Choose the wanted action:");
        System.out.println(" 1 : Receiving stock");
        System.out.println(" 2 : Print reports");
        System.out.println(" 3 : Update item data");
        System.out.println(" 4 : Cashier");
        System.out.println(" 5 : Manage discounts");
        System.out.println(" 6 : Manage Orders");
        System.out.println(" 7 : Back");
        Scanner scanner = new Scanner(System.in);
        String read = scanner.nextLine();
        while(!read.equals("1") && !read.equals("2") && !read.equals("3") && !read.equals("4") && !read.equals("5") && !read.equals("6") && !read.equals("7")){
            System.out.println("Illegal press. Please press again.");
            read = scanner.nextLine();
        }
        if(read.equals("1")){
            addSupplyMenu(ss);
            return true;
        }
        else{
            if(read.equals("2")){
                reportsMenu(ss);
                return true;
            }
            else{
                if(read.equals("3")){
                    updateDataMenu(ss);
                    return true;
                }
                else{
                    if(read.equals("4")){
                        cashierMenu(ss);
                        return true;
                    }
                    else{
                        if(read.equals("5")){
                            manageDiscountsMenu(ss);
                            return true;
                        }
                        else{
                            if(read.equals("6")){
                                manageOrdersMenu(ss);
                                return true;
                            }
                            else {
                                if (read.equals("7")) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }


    /**
     * Calls the BranchService's required methods for adding a new branch
     * @return
     */
    private static StockService addBranch(BranchService bs){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the new branch");
        String name = scanner.nextLine();
        System.out.println("Enter the address of the new branch");
        String address = scanner.nextLine();
        //scanner.close();
        try{
            return bs.addBranch(name, address);
        }
        catch(Exception ex){
            System.out.println("An error accured: " + ex.getMessage());
            return null;
        }
    }

    /**
     * Calls the BranchController's required methods for selecting an existing branch
     * @return
     */
    private static StockService selectBranch(BranchService bs, boolean shouldLoad){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press the number of the branch:");
        Map<Integer,String> branches = new HashMap<>();
        try{
            branches = bs.getBranches();
        }
        catch(Exception ex){
            System.out.println("An error accured: " + ex.getMessage());
            return null;
        }
        if(branches.keySet().size() == 0){
            System.out.println("There are no existing branches. Please return to the previous menu and add a new one.");
            //scanner.close();
            return null;
        }
        for(Integer id: branches.keySet()){
            System.out.println(id + " : " + branches.get(id));
        }
        String read = scanner.nextLine();
        //scanner.close();
        int selectedBranch = insureIntInput(read);
        try{
            return bs.selectBranch(shouldLoad, selectedBranch);
        }
        catch(Exception ex){
            System.out.println("An error accured: " + ex.getMessage());
            return null;
        }
    }


    /**
     * Access to starting a purchase
     * @param ss
     */
    private static void cashierMenu(StockService ss) {
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("Would you like to start purchase?");
            System.out.println(" 1 : Yes");
            System.out.println(" 2 : No");
            String read = scanner.nextLine();
            while(!read.equals("1") && !read.equals("2")){
                System.out.println("Illegal press. Please press again.");
                read = scanner.nextLine();
            }
            //scanner.close();
            if(read.equals("2")) 
                break;
            startPurchase(ss);
        }
    }

    
    /**
     * Purchase process menu - typing itemID to purchase or asking for check 
     * @param ss
     */
    public static void startPurchase(StockService ss) {
        Scanner scanner = new Scanner(System.in);
        String purchaseID = "";
        try {
            purchaseID = ss.addPurchase();
        } catch(Exception e) {
            System.out.println("Could'nt Add purchase.");
        }
        while(true){
            System.out.println("Enter item id");
            String read = scanner.nextLine();
            Map<String, Boolean> result;
            while(true){
                try{
                    result = ss.purchaseItem(purchaseID, read);
                    break;
                }
                catch(Exception ex){
                    System.out.println(ex.getMessage() + " Try again.");
                    read = scanner.nextLine();
                }
            }
            if(result.get("isDamaged")){
                System.out.println("Pay attention: the product " + read.split("-")[0] + " is damaged");
            }
            if(result.get("shortage")){
                System.out.println("Pay attention: the product " + read.split("-")[0] + " is low in stock");
            }
            System.out.println("Would you like to add additional items or get the check?");
            System.out.println(" 1 : Add additional items");
            System.out.println(" 2 : I'm done. Check please!");
            read = scanner.nextLine();
            while(!read.equals("1") && !read.equals("2")){
                System.out.println("Illegal press. Please press again.");
                read = scanner.nextLine();
            }
            if(read.equals("2")) {
                finishPurchase(ss, purchaseID);
                break;
            }
        }
    }


    /**
     * Prints the purchase's total price and reciept
     * @param ss
     * @param purchaseID
     */
    public static void finishPurchase(StockService ss, String purchaseID) {
        Map<String,Double> products;
        double total;
        while(true){
            try{
                products = ss.finishPurchase(purchaseID);
                total = ss.getPurchaseTotalPrice(purchaseID);
                break;
            }
            catch(Exception ex){
                System.out.println(ex.getMessage() + " Try again.");
            }
        }
        System.out.println("Thank you for choosing us for your purchase!");
        System.out.println("Here is your bill:");
        System.out.println("");
        int itemsCounter = 1;
        for (String name : products.keySet()) {
            System.out.println(itemsCounter + ". " + name + " - " + products.get(name) + " NIS");
            itemsCounter++;
        }
        System.out.println("");
        System.out.println("Total to pay: " + total + " NIS");
        System.out.println("");
        System.out.println("Hope to see you soon :)");
    }


    /**
     * Adding supply menu
     * @param ss
     */
    private static void addSupplyMenu(StockService ss){
        Scanner scanner = new Scanner(System.in);

        System.out.println("How many product types you need to enter?");
        String read = scanner.nextLine();
        Integer readInt = insureIntInput(read);
        for(int i = 0; i < readInt ; i++){
            addProductMenu(ss);
        }
        System.out.println("Supply was added successfully!");
    }

    private static void addProductMenu(StockService ss) {
        Scanner scanner = new Scanner(System.in);
        String id;
        while(true){
            System.out.println("Enter the product id");
            id = scanner.nextLine();
            boolean b = true;
            try{
                b = ss.checkProductExist(id);
            }
            catch (Exception ex){
                System.out.println("Error: " + ex.getMessage());
            }
            if(!b){
                System.out.println("Product doesn't exist in the system. Let's add it");
                List<String> categories = chooseOrAddCategoryMenu(ss);
                System.out.println("Enter name of the product");
                String name = scanner.nextLine();
                System.out.println("Enter the product purchase price");
                Double purchasePrice = insureDoubleInput(scanner.nextLine());
                System.out.println("Enter the product selling price");
                Double sellingPrice = insureDoubleInput(scanner.nextLine());
                System.out.println("Enter name of the manufacture");
                String manufacture = scanner.nextLine();
                System.out.println("Enter the product demand");
                Integer demand = insureIntInput(scanner.nextLine());
                System.out.println("Enter the product supply time in days");
                Integer supplyTime = insureIntInput(scanner.nextLine());
                System.out.println("Enter the product minimum amount to get notification");
                Integer initialNotificationAmount = insureIntInput(scanner.nextLine());
                try{
                    ss.addProduct(id, name, purchasePrice, sellingPrice, manufacture, demand, supplyTime, initialNotificationAmount, categories);
                    break;
                }
                catch(Exception ex){
                    System.out.println(ex.getMessage() + " Try again.");
                }
            }
            else{
                try{
                    System.out.println("Adding new items to an existing product - " + ss.getProductName(id));
                    break;
                }
                catch(Exception ex){
                    System.out.println(ex.getMessage() + " Try again.");
                }
            }
        }
        while(true){
            System.out.println("Enter the number of recieved items that will go to the store");
            int storeAmount = insureIntInput(scanner.nextLine());
            System.out.println("Enter the number of recieved items that will go to the warehouse");
            int warehouseAmount = insureIntInput(scanner.nextLine());
            System.out.println("Enter the expiration date in the format dd-mm-yyyy");
            Date expiration = insureDateInput(scanner.nextLine());
            try{
                ss.receiveSupply(id, storeAmount, warehouseAmount, expiration);
                break;
            }
            catch(Exception ex){
                System.out.println(ex.getMessage() + " Try again.");
            }
        }
        System.out.println("Product was added successfully");
        //scanner.close();
    }


    /**
     * Printing reports Menu
     * @param ss
     */
    private static void reportsMenu(StockService ss) {
        System.out.println("Which report would you like to print?");
        System.out.println(" 1 : Deficiency report");
        System.out.println(" 2 : Damaged Items report");
        System.out.println(" 3 : Stock report");
        Scanner scanner = new Scanner(System.in);
        String read = scanner.nextLine();
        while(!read.equals("1") && !read.equals("2") && !read.equals("3")) {
            System.out.println("Illegal press. Please press again.");
            read = scanner.nextLine();
        }
        List<String> categoryChain = chooseCategoryMenu(ss);
        List<List<String>> chosenCategories = new ArrayList<>();
        String toContinue;
        while(categoryChain.size() != 0){
            chosenCategories.add(categoryChain);
            System.out.println("Would you like to add another category to the report?");
            System.out.println(" 1 : Yes");
            System.out.println(" 2 : No");
            toContinue = scanner.nextLine();
            while(!toContinue.equals("1") && !toContinue.equals("2")){
                System.out.println("Illegal press. Please press again.");
                toContinue = scanner.nextLine();
            }
            if(toContinue.equals("1"))
                categoryChain = chooseCategoryMenu(ss);
            else break;
        }
        if(read.equals("1")){
            try {
                System.out.println(ss.printDeficiencyReport(chosenCategories));
            }
            catch (Exception ex){

            }
        }
        else if(read.equals("2")){
            List<String> damageList = new ArrayList<>();
            String damageString = chooseDamageTypeMenu(ss);
            while(true){
                damageList.add(damageString);
                System.out.println("Would you like to add another DamageType to the report?");
                System.out.println(" 1 : Yes");
                System.out.println(" 2 : No");
                toContinue = scanner.nextLine();;
                while(!toContinue.equals("1") && !toContinue.equals("2")){
                    System.out.println("Illegal press. Please press again.");
                    toContinue = scanner.nextLine();
                }
                if(toContinue.equals("1"))
                    damageString = chooseDamageTypeMenu(ss);
                else break;
            }
            try {
                System.out.println(ss.printDamagedReport(chosenCategories, damageList));
            }catch (Exception ex){

            }
        }
        else {
            try {
                System.out.println(ss.printStockReport(chosenCategories));
            }
            catch (Exception ex){}
        }
    }
    //scanner.close();



    /**
     * Choosing a category from main categories list or sub categories
     * In each iteration choosing between the current category(0) or one of its subs
     * @param ss
     * @return
     */
    private static List<String> chooseCategoryMenu(StockService ss) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the desired category:");
        Map<String, String> categoryMap;
        try {
            categoryMap = ss.getMainCategories();
        } catch (Exception ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
        System.out.println(" 0 : End choice");
        int counter = 1;
        Map<Integer, String> mapper = new HashMap<>();
        for(String id: categoryMap.keySet()){
            System.out.println(" " + counter + " : " + categoryMap.get(id));
            mapper.put(counter, id);
            counter++;
        }
        List<String> categoryList = new ArrayList<>();
        String read = scanner.nextLine();
        while(!mapper.containsKey(Integer.parseInt(read)) && !read.equals("0")){
            System.out.println("Illegal press. Please press again.");
            read = scanner.nextLine();
        }
        if(!read.equals("0")){
            read = mapper.get(Integer.parseInt(read));
            categoryList.add(read);
            categoryMap = ss.getSub(new ArrayList<>(categoryList));
            while(categoryMap.keySet().size() != 0){
                System.out.println("Choose a sub-category or end choice:");
                System.out.println(" 0 : End choice");
                counter = 1;
                mapper = new HashMap<>();
                for(String id: categoryMap.keySet()){
                    System.out.println(" " + counter + " : " + categoryMap.get(id));
                    mapper.put(counter, id);
                    counter++;
                }
                read = scanner.nextLine();
                while(!mapper.containsKey(Integer.parseInt(read)) && !read.equals("0")){
                    System.out.println("Illegal press. Please press again.");
                    read = scanner.nextLine();
                }
                if(read.equals("0")) break;
                read = mapper.get(Integer.parseInt(read));
                categoryList.add(read);
                categoryMap = ss.getSub(new ArrayList<>(categoryList));
            }
        }
        //scanner.close();
        return categoryList;
    }


    /**
     * Choose between choosing an existing category or adding a new one
     * enables the navigation through main and sub categorys in order to add a new sub category
     * @param ss
     * @return
     */
    private static List<String> chooseOrAddCategoryMenu(StockService ss) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the fit category or add a new one:");
        Map<String, String> categoryMap;
        try {
            categoryMap = ss.getMainCategories();
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
        System.out.println(" " + "0" + " : " + "Add new category");
        int counter = 1;
        Map<Integer, String> mapper = new HashMap<>();
        for(String id: categoryMap.keySet()){
            System.out.println(" " + counter + " : " + categoryMap.get(id));
            mapper.put(counter, id);
            counter++;
        }
        List<String> categoryList = new ArrayList<>();
        String read = scanner.nextLine();
        while(!mapper.containsKey(Integer.parseInt(read)) && !read.equals("0")){
            System.out.println("Illegal press. Please press again.");
            read = scanner.nextLine();
        }
        if(read.equals("0")){
            addCategoryMenu(ss, categoryList);
            System.out.println("Category Added!");
        }
        else{
            read = mapper.get(Integer.parseInt(read));
            categoryList.add(read);
            categoryMap = ss.getSub(new ArrayList<>(categoryList));
            while(categoryMap.keySet().size() != 0 && !read.equals("0")){
                System.out.println(" " + "0" + " : " + "Add new category");
                counter = 1;
                mapper = new HashMap<>();
                for(String id: categoryMap.keySet()){
                    System.out.println(" " + counter + " : " + categoryMap.get(id));
                    mapper.put(counter, id);
                    counter++;
                }
                read = scanner.nextLine();
                while(!mapper.containsKey(Integer.parseInt(read)) && !read.equals("0")){
                    System.out.println("Illegal press. Please press again.");
                    read = scanner.nextLine();
                }
                if(read.equals("0")){
                    addCategoryMenu(ss, categoryList);
                    System.out.println("Category Added!");
                }
                else{
                    read = mapper.get(Integer.parseInt(read));
                    categoryList.add(read);
                    categoryMap = ss.getSub(new ArrayList<>(categoryList));
                }
            }
        }
        //scanner.close();
        return categoryList;
    }


    /**
     * Adding a new category
     * @param ss
     * @param categories
     */
    private static void addCategoryMenu(StockService ss, List<String> categories) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What is the name of the new category?");
        String read = scanner.nextLine();
        String category;
        try{
            category = ss.addCategory(new ArrayList<>(categories), read);
        } catch(Exception ex){
            System.out.println(ex.getMessage());
            return;
        }
        categories.add(category);
        System.out.println("Would you like to add sub category?");
        System.out.println(" 1 : Yes");
        System.out.println(" 2 : No");
        read = scanner.nextLine();
        while(!read.equals("1") && !read.equals("2")){
            System.out.println("Illegal press. Please press again.");
            read = scanner.nextLine();
        }
        if(read.equals("1")){
            addCategoryMenu(ss, categories);
        }
    }


    /**
     * Choosing a damageType out of the existing damageTypes in the system
     * @param ss
     * @return
     */
    private static String chooseDamageTypeMenu(StockService ss) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a damage type: ");
        Map<Integer, String> damages = ss.getDamages();
        for(Integer key: damages.keySet()){
            System.out.println(" " + key + " : " + damages.get(key));
        }
        String read = scanner.nextLine();
        Integer readInt = insureIntInput(read);
        if(!damages.keySet().contains(readInt)){
            System.out.println("Illegal press. Please press again.");
            read = scanner.nextLine();
            readInt = insureIntInput(read);
        }
        //scanner.close();
        return damages.get(readInt);
    }


    /**
     * Choosing what kind of data we desire to update
     * @param ss
     */
    private static void updateDataMenu(StockService ss) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose the wanted action:");
        System.out.println(" 1 : Report on a damaged item");
        System.out.println(" 2 : Update product demand");
        System.out.println(" 3 : Update product supply time");
        System.out.println(" 4 : Change item's location");
        System.out.println(" 5 : Update the purchase price from suppliers");
        String read = scanner.nextLine();
        while(!read.equals("1") && !read.equals("2") && !read.equals("3") && !read.equals("4") && !read.equals("5")){
            System.out.println("Illegal press. Please press again.");
            read = scanner.nextLine();
        }
        
        if(read.equals("1")){
            System.out.println("Enter the item id");
            read = scanner.nextLine();
            String demage = chooseDamageTypeMenu(ss);
            while(true){
                try{
                    ss.addDamagedItem(read, demage);
                    break;
                }
                catch(Exception ex){
                    System.out.println(ex.getMessage() + " Try again.");
                    read = scanner.nextLine();
                }
            }
        }
        else{
            if(read.equals("2")){
                System.out.println("Enter the product id");
                read = scanner.nextLine();
                System.out.println("Enter the new demand value");
                Integer demand = insureIntInput(scanner.nextLine());
                while(true){
                    try{
                        ss.SetDemand(read, demand);
                        break;
                    }
                    catch(Exception ex){
                        System.out.println(ex.getMessage() + " Try again.");
                        System.out.println("Enter the product id");
                        read = scanner.nextLine();
                    }
                }
            }
            else{
                if(read.equals("3")){
                    System.out.println("Enter the product id");
                    read = scanner.nextLine();
                    System.out.println("Enter the new supply time value");
                    Integer readInt = insureIntInput(scanner.nextLine());
                    while(true){
                        try{
                            ss.SetSupplyTime(read, readInt);
                            break;
                        }
                        catch(Exception ex){
                            System.out.println(ex.getMessage() + " Try again.");
                            System.out.println("Enter the product id");
                            read = scanner.nextLine();
                        }
                    }
                }
                else{
                    if(read.equals("4")){
                        System.out.println("Enter the item id");
                        read = scanner.nextLine();
                        while(true){
                            try{
                                ss.moveItem(read);
                                break;
                            }
                            catch(Exception ex){
                                System.out.println(ex.getMessage() + " Try again.");
                                System.out.println("Enter the item id");
                                read = scanner.nextLine();
                            }
                        }
                    }
                    else{
                        if(read.equals("5")){
                            System.out.println("Enter the product id");
                            read = scanner.nextLine();
                            System.out.println("Enter the new purchase price from suppliers");
                            Double price = insureDoubleInput(scanner.nextLine());
                            while(true){
                                try{
                                    ss.SetPurchasePrice(read, price);
                                    break;
                                }
                                catch(Exception ex){
                                    System.out.println(ex.getMessage() + " Try again.");
                                    System.out.println("Enter the product id");
                                    read = scanner.nextLine();
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    
    /**
     * Managing all discounts menu - add new/ update/ delete etc. and all these actions require
     * @param ss
     */
    private static void manageDiscountsMenu(StockService ss) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("What would you like to do?");
        System.out.println(" 1 : Add a new discount");
        System.out.println(" 2 : Update/Delete an existing discount");
        String read = scanner.nextLine();
        while (!read.equals("1") && !read.equals("2")) {
            System.out.println("Illegal press. Please press again.");
            read = scanner.nextLine();
        }

        //new discount!:
        if (read.equals("1")) {
            int type = chooseDiscountTypeMenu();
            if (type == 1) {
                //CATEGORY Discount!
                List<String> categoryChain = chooseCategoryMenu(ss);
                addNewDiscount(ss, categoryChain, 1);
            }
            else if (type == 2) {
                //PRODUCT Discount!
                System.out.println("Enter the product id");
                read = scanner.nextLine();
                while (true) {
                    boolean b = true;
                    try{
                        b = ss.checkProductExist(read);
                    }
                    catch (Exception ex){
                        System.out.println("DB ERROR:" + ex.getMessage());
                    }
                    while (!b) {
                        System.out.println("Try again. Product ID doesnt exist");
                        System.out.println("Enter the product id");
                        read = scanner.nextLine();
                    }
                    List<String> IDList = new ArrayList<>();
                    IDList.add(read);
                    addNewDiscount(ss, IDList, 2);
                    break;
                }
            }
            else { //(type == 3)
                //DAMAGED Discount!
                System.out.println("Enter the product id");
                read = scanner.nextLine();
                while (true) {
                    boolean b = true;
                    try{
                        b = ss.checkProductExist(read);
                    }
                    catch (Exception ex){
                        System.out.println("DB ERROR:" + ex.getMessage());
                    }
                    while (!b) {
                        System.out.println("Try again. Product ID doesnt exist");
                        System.out.println("Enter the product id");
                        read = scanner.nextLine();
                    }
                    List <String> IDList = new ArrayList<>();
                    IDList.add(0, read); //productID
                    IDList.add(1,chooseDamageTypeMenu(ss)); //damageType
                    addNewDiscount(ss, IDList, 3);
                    break;
                    }
                }
            }

        //Update/Delete an existing discount!:
        else {
            int type = chooseDiscountTypeMenu();
            if (type == 1) {
                //CATEGORY DISCOUNT!
                List<String> categoryChain = chooseCategoryMenu(ss);
                Map<String, String> discountsList;
                while (true) {
                    try {
                        discountsList = ss.getCategoryDiscouts(categoryChain);
                        break;
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage() + " Try again.");
                    }
                }
                String discountID = chooseDiscountMenu(discountsList);
                if(discountID.equals("-1")) {
                    return;
                }
                boolean toDelete = chooseUpdateOrDeleteMenu();
                if (toDelete) {
                    while (true) {
                        try {
                            ss.deleteCategoryDiscount(categoryChain, discountID);
                            break;
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage() + " Try again.");
                        }
                    }
                }
                else {
                    while (true) {
                        try {
                            updateDiscount(ss, categoryChain, 1, discountID);
                            break;
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage() + " Try again.");
                        }
                    }
                }
            }
            else if (type == 2) {
                //PRODUCT Discount!
                System.out.println("Enter the product id");
                read = scanner.nextLine();
                while (true) {
                    boolean b = true;
                    try{
                        b = ss.checkProductExist(read);
                    }
                    catch (Exception ex){
                        System.out.println("DB ERROR:" + ex.getMessage());
                    }
                    while (!b) {
                        System.out.println("Try again. Product ID doesnt exist");
                        System.out.println("Enter the product id");
                        read = scanner.nextLine();
                    }
                    Map<String, String> discountsList;
                    List<String> IDList = new ArrayList<>();
                    IDList.add(read); //productID
                    while (true) {
                        try {
                            discountsList = ss.getProductGeneralDiscouts(IDList.get(0));
                            break;
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage() + " Try again.");
                        }
                    }
                    String discountID = chooseDiscountMenu(discountsList);
                    if (discountID.equals("-1")) {
                        return;
                    }
                    boolean toDelete = chooseUpdateOrDeleteMenu();
                    if (toDelete) {
                        while (true) {
                            try {
                                ss.deleteProductDiscount(IDList.get(0), discountID, "NONE");
                                break;
                            } catch (Exception ex) {
                                System.out.println(ex.getMessage() + " Try again.");
                            }
                        }
                    } else {
                        while (true) {
                            try {
                                updateDiscount(ss, IDList, 2, discountID);
                                break;
                            } catch (Exception ex) {
                                System.out.println(ex.getMessage() + " Try again.");
                            }
                        }
                    }
                    break;
                }
            }
            else { //(type == 3)
                //DAMAGED Discount!
                System.out.println("Enter the product id");
                read = scanner.nextLine();
                while (true) {
                    boolean b = true;
                    try{
                        b = ss.checkProductExist(read);
                    }
                    catch (Exception ex){
                        System.out.println("DB ERROR:" + ex.getMessage());
                    }
                    while (!b) {
                        System.out.println("Try again. Product ID doesnt exist");
                        System.out.println("Enter the product id");
                        read = scanner.nextLine();
                    }
                    List<String> IDList = new ArrayList<>();
                    IDList.add(0, read); //productID
                    IDList.add(1, chooseDamageTypeMenu(ss));
                    Map<String, String> discountsList;
                    while (true) {
                        try {
                            discountsList = ss.getProductDamagedDiscounts(IDList.get(0), IDList.get(1));
                            break;
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage() + " Try again.");
                        }
                    }
                    String discountID = chooseDiscountMenu(discountsList);
                    if (discountID.equals("-1")) {
                        return;
                    }
                    boolean toDelete = chooseUpdateOrDeleteMenu();
                    if (toDelete) {
                        while (true) {
                            try {
                                ss.deleteProductDiscount(IDList.get(0), discountID, IDList.get(1));
                                break;
                            } catch (Exception ex) {
                                System.out.println(ex.getMessage() + " Try again.");
                            }
                        }
                    }
                    else {
                        while (true) {
                            try {
                                updateDiscount(ss, IDList, 3, discountID);
                                break;
                            } catch (Exception ex) {
                                System.out.println(ex.getMessage() + " Try again.");
                            }
                        }
                    }
                    break;
                }
            }
        }
    }


    /**
     * Creates a new discount and adds it to the proper Category/Product/Damage
     * @param ss
     * @param IDList - Depends on Discount Type:
     *              if CATEGORY - CategoryIDChain,
     *              if PRODUCT - productID ([0]),
     *              if DAMAGED - productID([0]), damageType([1])
     * @param discountType
     */
    private static void addNewDiscount(StockService ss, List<String> IDList, int discountType) {
        Scanner scanner = new Scanner(System.in);
        String read;
        System.out.println("Enter discount start date");
        Date startDate = insureDateInput(scanner.nextLine());
        System.out.println("Enter discount end date");
        Date endDate = insureDateInput(scanner.nextLine());
        System.out.println("Choose discount rate mesurments:");
        System.out.println(" 1 : Percente");
        System.out.println(" 2 : NIS");
        read = scanner.nextLine();
        boolean isPercent;
        while(!read.equals("1") && !read.equals("2")){
            System.out.println("Illegal press. Please press again.");
            read = scanner.nextLine();
        }
        if(read.equals("1")){
            isPercent = true;
        }
        else {
            isPercent = false;
        }
        System.out.println("Enter value (rate) of the discount");
        double discoutValue = insureDoubleInput(scanner.nextLine());
        while (true) {
            try{
                if (discountType == 1) { //Category Discount
                    ss.addCategoryDiscount(IDList, startDate, endDate, discoutValue, isPercent);
                    break;
                }
                else if (discountType == 2) {
                    ss.addProductDiscount(IDList.get(0), startDate, endDate, discoutValue, isPercent);
                    break;
                }
                else if (discountType == 3) {
                    ss.addDamagedDiscount(IDList.get(0), IDList.get(1), startDate, endDate, discoutValue, isPercent);
                    break;
                }
            }
            catch(Exception ex){
                System.out.println(ex.getMessage() + " Try again.");
            }
        }
    }


    /**
     *
     * @param discountsList
     * @return the required discount ID out of the category/ objec discounts list
     */
    private static String chooseDiscountMenu (Map<String, String> discountsList) {
        Scanner scanner = new Scanner(System.in);
        String read;
        if (discountsList.size() == 0) {
            System.out.println("There are no discounts to show, please try again!");
            System.out.println("");
            return "-1";
        }
        else {
            System.out.println("Choose the discount you wish to update/delete:");
            int counter = 1;
            Map<Integer, String> mapper = new HashMap<>();
            for(String id: discountsList.keySet()){
                System.out.println(" " + counter + " : " + discountsList.get(id));
                mapper.put(counter, id);
                counter++;
            }
            String discountID;
            read = scanner.nextLine();
            while(!mapper.containsKey(Integer.parseInt(read))){
                System.out.println("Illegal press. Please press again.");
                read = scanner.nextLine();
            }
            read = mapper.get(Integer.parseInt(read));
            discountID = read;
            return discountID;
        }
    }


    /**
     * Asks the user to choose a discount attribute to update and the new valid and updates it
     * @param ss
     * @param IDList
     * @param discountType
     * @param discountID
     */
    private static void updateDiscount(StockService ss, List<String> IDList, int discountType, String discountID) {
        Scanner scanner = new Scanner(System.in);
        String read;
        System.out.println("Choose the discount information you wish to update:");
        System.out.println(" 1 : startDate");
        System.out.println(" 2 : endDate");
        System.out.println(" 3 : rate mesurments");
        System.out.println(" 4 : discount value (rate)");
        read = scanner.nextLine();
        while (!read.equals("1") && !read.equals("2") && !read.equals("3") && !read.equals("4")) {
            System.out.println("Illegal press. Please press again.");
            read = scanner.nextLine();
        }
        while (true) {
            if (read.equals("1")) {
                //update startDate
                System.out.println("Enter new discount start date");
                Date startDate = insureDateInput(scanner.nextLine());
                try {
                    if (discountType == 1) {
                        ss.updateCategoryDiscountStartDate(IDList, discountID, startDate);
                    }
                    else if (discountType == 2) {
                        ss.updateProductDiscountStartDate(IDList.get(0), "NONE", discountID, startDate);
                    }
                    else if (discountType == 3) {
                        ss.updateProductDiscountStartDate(IDList.get(0), IDList.get(1), discountID, startDate);
                    }
                    break;
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage() + " Try again.");
                }
            }
            else if (read.equals("2")) {
                //update endDate
                System.out.println("Enter new discount end date");
                Date endDate = insureDateInput(scanner.nextLine());
                try {
                    if (discountType == 1) {
                        ss.updateCategoryDiscountEndDate(IDList, discountID, endDate);
                    }
                    else if (discountType == 2) {
                        ss.updateProductDiscountEndDate(IDList.get(0), "NONE", discountID, endDate);
                    }
                    else if (discountType == 3) {
                        ss.updateProductDiscountEndDate(IDList.get(0), IDList.get(1), discountID, endDate);
                    }
                    break;
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage() + " Try again.");
                }
            }
            else if (read.equals("3")) {
                //update isPercent
                System.out.println("Choose new discount rate mesurments:");
                System.out.println(" 1 : Percente");
                System.out.println(" 2 : NIS");
                read = scanner.nextLine();
                while (!read.equals("1") && !read.equals("2")) {
                    System.out.println("Illegal press. Please press again.");
                    read = scanner.nextLine();
                }
                boolean isPercent = read.equals("1");
                try {
                    if (discountType == 1) {
                        ss.updateCategoryDiscountIsPercent(IDList, discountID, isPercent);
                    }
                    else if (discountType == 2) {
                        ss.updateProductDiscountIsPercent(IDList.get(0), "NONE", discountID, isPercent);
                    }
                    else if (discountType == 3) {
                        ss.updateProductDiscountIsPercent(IDList.get(0), IDList.get(1), discountID, isPercent);
                    }
                    break;
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage() + " Try again.");
                }
            }
            else {
                //update discountValue
                System.out.println("Enter new discount value (rate)");
                double discountValue = insureDoubleInput(scanner.nextLine());
                try {
                    if (discountType == 1) {
                        ss.updateCategoryDiscountValue(IDList, discountID, discountValue);
                    }
                    else if (discountType == 2) {
                        ss.updateProductDiscountValue(IDList.get(0), "NONE", discountID, discountValue);
                    }
                    else if (discountType == 3) {
                        ss.updateProductDiscountValue(IDList.get(0), IDList.get(1), discountID, discountValue);
                    }
                    break;
                }
                    catch (Exception ex) {
                    System.out.println(ex.getMessage() + " Try again.");
                }
            }
        }
    }


    /**
     * A discount / order managment sub menu for choosing the desired action out of update and delete
     * @return
     */
    private static boolean chooseUpdateOrDeleteMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose your desired action:");
        System.out.println(" 1 : Update");
        System.out.println(" 2 : Delete");
        String read = scanner.nextLine();
        while(!read.equals("1") && !read.equals("2")){
            System.out.println("Illegal press. Please press again.");
            read = scanner.nextLine();
        }
        return (read.equals("2"));
    }


    /**
     * A discount managment sub menu for choosing a discount type:
     * Category/ Product/ Damaged
     * @return
     */
    private static int chooseDiscountTypeMenu() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose your desired discount type:");
        System.out.println(" 1 : Category discount");
        System.out.println(" 2 : Product discount");
        System.out.println(" 3 : Damaged items discount");
        String read = scanner.nextLine();
        while(!read.equals("1") && !read.equals("2") && !read.equals("3")){
            System.out.println("Illegal press. Please press again.");
            read = scanner.nextLine();
        }
        return insureIntInput(read);
    }

    /**
     * Managing all orders menu - add new/ update/ delete etc. and all these actions require
     * @param ss
     */
    private static void manageOrdersMenu(StockService ss) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("What would you like to do?");
        System.out.println(" 1 : Add a new order");
        System.out.println(" 2 : Update/Delete an existing order");
        String read = scanner.nextLine();
        while (!read.equals("1") && !read.equals("2")) {
            System.out.println("Illegal press. Please press again.");
            read = scanner.nextLine();
        }
        if (read.equals("1")) {
            //add a new order
            System.out.println("What kind of order would you like to add?");
            System.out.println(" 1 : A new Automatic Order");
            System.out.println(" 2 : A new one-time Express Order");
            read = scanner.nextLine();
            while (!read.equals("1") && !read.equals("2")) {
                System.out.println("Illegal press. Please press again.");
                read = scanner.nextLine();
            }
            if (read.equals("1")) {
                addNewAutomaticOrder(ss);
            } else if (read.equals("2")) {
                addNewExpressOrder(ss);
            }
        }

        else if (read.equals("2")) {
            // Update an existing order
            Map<Integer, String> ordersList;
            while (true) {
                try {
                    ordersList = ss.getAutoOrdersList();
                    break;
                } catch (Exception ex) {
                    System.out.println(ex.getMessage() + " Try again.");
                }
            }
            int orderID = chooseOrderMenu(ordersList);
            if(orderID == -1) {
                return;
            }
            boolean toDelete = chooseUpdateOrDeleteMenu();
            if (toDelete) {
                while (true) {
                    try {
                        ss.deleteAutomaticOrder(orderID);
                        break;
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage() + " Try again.");
                    }
                }
            }
            else {
                updateOrder(ss, orderID);
            }
        }
    }

    private static int chooseOrderMenu (Map<Integer, String> ordersList) {
        Scanner scanner = new Scanner(System.in);
        String read;
        if (ordersList.size() == 0) {
            System.out.println("There are no orders to show, please try again!");
            System.out.println("");
            return -1;
        }
        else {
            System.out.println("Choose the order you wish to update/delete:");
            System.out.println(" 0 : End choice");
            for (int id : ordersList.keySet()) {
                System.out.println(" " + id + " : supplied on days " + ordersList.get(id)); //????
            }
            int orderID;
            read = scanner.nextLine();
            while (!ordersList.keySet().contains(read)) {
                System.out.println("Illegal press. Please press again.");
                read = scanner.nextLine();
            }
            orderID = insureIntInput(read);
            return orderID;
        }
    }



    private static void addNewAutomaticOrder(StockService ss) {
        Scanner scanner = new Scanner(System.in);
        String read;
        System.out.println("Choose desired supply days:");
        List<Integer> supplyDays = chooseDaysMenu(ss);
        System.out.println("Add Products to your Automatic Order:");
        Map<Pair<String, String>, Integer> productsToOrder = new HashMap<>();
        int orderID = -1;
        while (true) {
            try {
                orderID = ss.addAutomaticOrder(supplyDays, addProductsToOrder(ss, productsToOrder));
                break;
            } catch (Exception ex) {
                System.out.println(ex.getMessage() + " Try again.");
            }
        }

            if (orderID == -1) {
                System.out.println("Order insertion failed");
            } else {
                System.out.println("An automatic Order was successfully added to the System!");
                System.out.println("Order ID: " + orderID);
                System.out.println("Make sure to remember the order ID, it is mandatory in order to enable to update / delete the order.");
            }

    }


    private static void addNewExpressOrder(StockService ss) {
        System.out.println("Add Products to your Express Order:");
        Map<Pair<String, String>, Integer> productsToOrder = new HashMap<>();
        boolean orderAdded = false;
        while (true) {
            try {
                orderAdded = ss.addExpressOrder(addProductsToOrder(ss, productsToOrder));
                break;
            } catch (Exception ex) {
                System.out.println(ex.getMessage() + " Try again.");
            }
        }
            if (!orderAdded) {
                System.out.println("Order insertion failed");
            } else {
                System.out.println("An Express Order was successfully added to the System!");
                System.out.println("Products will arrive as soon as possible");
            }

    }


    private static Map<Pair<String, String>, Integer> addProductsToOrder (StockService ss, Map<Pair<String, String>, Integer> productsToOrder) throws Exception{////throws exception - delete
        Scanner scanner = new Scanner(System.in);
        String read;
        boolean doneEnteringProducts = false;
        String productID;
        String productName;
        int amount;
        while (!doneEnteringProducts) {
            System.out.println("Enter Product ID");
            productID = scanner.nextLine();
            if(!ss.checkProductExist(productID)) {//throws exception
                System.out.println("Product doesn't exist in the system. Please enter product's name:");
                productName = scanner.nextLine();
            }
            else {
                productName = ss.getProductName(productID);//throws exception
            }
            System.out.println("How many items of product '" + productName + "' would you like to order?");
            amount = insureIntInput(scanner.nextLine());
            productsToOrder.put(new Pair<>(productID,productName), amount);
            System.out.println("Would you like to add an additional product to your order?");
            System.out.println(" 0 : No, I'm done adding products");
            System.out.println(" 1 : Yes");
            read = scanner.nextLine();
            while (!read.equals("0") && !read.equals("1")) {
                System.out.println("Illegal option. Your answer should be '0' or '1'. Please press again.");
                read = scanner.nextLine();
            }
            if (read.equals("0")) {
                doneEnteringProducts = true;
            }
        }
        return productsToOrder;
    }

    private static List<Integer> chooseDaysMenu(StockService ss) {
        List<Integer> daysList = new ArrayList<>();
        boolean done = false;
        while (!done) {
            Scanner scanner = new Scanner(System.in);
            System.out.println(" 0 : Sunday");
            System.out.println(" 1 : Monday");
            System.out.println(" 2 : Tuesday");
            System.out.println(" 3 : Wednesday");
            System.out.println(" 4 : Thursday");
            System.out.println(" 5 : Friday");
            System.out.println(" 6 : Saturday");
            System.out.println(" 8 : End choice");
            String read = scanner.nextLine();
            while (!read.equals("8") && !read.equals("0") && !read.equals("1") && !read.equals("2") && !read.equals("3") && !read.equals("4") && !read.equals("5") && !read.equals("6")) {
                System.out.println("Illegal day was chosen. Please press again.");
                read = scanner.nextLine();
            }
            if (!read.equals("8")) {
                daysList.add(insureIntInput(read));
            }
            if (read.equals("8")) {
                done = true;
            }
        }
        //scanner.close();
        return daysList;
    }


    private static void updateOrder(StockService ss, int orderID) {
        Scanner scanner = new Scanner(System.in);
        String read;
        System.out.println("What kind of information would you like to update?");
        System.out.println(" 1 : Supply arrival days");
        System.out.println(" 2 : Products and amounts");
        read = scanner.nextLine();
        while (!read.equals("1") && !read.equals("2")) {
            System.out.println("Illegal press. Please press again.");
            read = scanner.nextLine();
        }
        if (read.equals("1")) {
            System.out.println("Choose new desired supply days:");
            List<Integer> newSupplyDays = chooseDaysMenu(ss);
            while (true) {
                try{
                    boolean updated = ss.updateOrderSupplyDays(orderID, newSupplyDays);
                    if (updated) {
                        System.out.println("Order was updated successfully.");
                    }
                }
                catch(Exception ex){
                    System.out.println(ex.getMessage() + " Try again.");
                }
            }
        }
        else if (read.equals("2")) {
            boolean doneUpdatingProducts = false;
            Map<Pair<String, String>, Integer> productsAndAmountsMap = new HashMap<>();
            while (true) {
                try {
                    productsAndAmountsMap = ss.getOrderProductsMap(orderID);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage() + " Try again.");
                }
                while (!doneUpdatingProducts) {
                    System.out.println("Choose your desired action:");
                    System.out.println(" 1 : Add a new product to the order");
                    System.out.println(" 2 : Edit / delete an existing product in order");
                    read = scanner.nextLine();
                    while (!read.equals("1") && !read.equals("2")) {
                        System.out.println("Illegal option. Your answer should be '1' or '2'. Please press again.");
                        read = scanner.nextLine();
                    }
                    if (read.equals("1")) {
                        try {
                            productsAndAmountsMap = addProductsToOrder(ss, productsAndAmountsMap); //throws exception
                        }catch (Exception ex) {
                            System.out.println(ex.getMessage() + " Try again.");
                        }
                    }
                    else if (read.equals("2")) {
                        System.out.println("Choose a product:");
                        Map<Integer, Pair<String, String>> selectionDisplay = new HashMap<>();
                        int iDCount = 1;
                        for (Pair<String, String> product: productsAndAmountsMap.keySet()) {
                            selectionDisplay.put(iDCount, product);
                            iDCount++;
                        }
                        for (int id : selectionDisplay.keySet()) {
                            System.out.println(" " + id + " : " + selectionDisplay.get(id).getSecond() + " (" + selectionDisplay.get(id).getFirst() + ") - " + productsAndAmountsMap.get(selectionDisplay.get(id)) + " items");
                        }
                        read = scanner.nextLine();
                        while (!selectionDisplay.keySet().contains(insureIntInput(read))) {
                            System.out.println("Illegal press. Please press again.");
                            read = scanner.nextLine();
                        }
                        Pair<String, String> productToUpdate = selectionDisplay.get(insureIntInput(read));
                        System.out.println("Insert the new amount of items you would like to order, to delete product - insert '0'");
                        read = scanner.nextLine();
                        if (insureIntInput(read).equals(0)) {
                            productsAndAmountsMap.remove(productToUpdate);
                        }
                        else {
                            productsAndAmountsMap.replace(productToUpdate, insureIntInput(read));
                        }
                    }
                    System.out.println("Would you like to update more products in this order?");
                    System.out.println(" 0 : No, I'm done updating products");
                    System.out.println(" 1 : Yes");
                    read = scanner.nextLine();
                    while (!read.equals("1") && !read.equals("2")) {
                        System.out.println("Illegal option. Your answer should be '0' or '1'. Please press again.");
                        read = scanner.nextLine();
                    }
                    if (read.equals("0")) {
                        doneUpdatingProducts = true;
                    }
                }
                try {
                    boolean updated = ss.updateOrderProductsAndAmounts(orderID, productsAndAmountsMap);
                    if (updated) {
                        System.out.println("Order was updated successfully.");
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage() + " Try again.");
                }
            }
        }
    }
}

