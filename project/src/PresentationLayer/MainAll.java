package PresentationLayer;


import PresentationLayer.CLI.Stock.Main;
import PresentationLayer.CLI.Suppliers.MainMenu;
import PresentationLayer.UI.StockAndSuppliers.MainGUIMenuFrame;

import java.util.Calendar;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainAll {


    public static void main(String[] args) {
        args = new String[]{"GUI", "StoreManager"};
        if (args.length != 2) {
            System.out.println("Invalid number of arguments.");
            System.out.println("Usage: java -jar adss2023_v03.jar <Mode> <Role>");
            return;
        }
        String mode = args[0];
        String role = args[1];
        if (mode.equalsIgnoreCase("GUI")) {
            if (role.equalsIgnoreCase("StoreManager")) {
                startGUI(1);
            } else if (role.equalsIgnoreCase("StockKeeper")) {
                startGUI(2);
            } else if (role.equalsIgnoreCase("SuppliersManager")) {
                startGUI(3);
            } else {
                System.out.println("Invalid role for GUI mode.");
            }
        } else if (mode.equalsIgnoreCase("CLI")) {
            if (role.equalsIgnoreCase("StoreManager")) {
                startCLI(1);
            } else if (role.equalsIgnoreCase("StockKeeper")) {
                startCLI(2);
            } else if (role.equalsIgnoreCase("SuppliersManager")) {
                startCLI(3);
            } else {
                System.out.println("Invalid role for CLI mode.");
            }
        } else {
            System.out.println("Invalid mode.");
        }
    }


    public static void startCLI(int roleID) {
        System.out.println("Starting CLI for role: " + roleID);
        Main mainMenuStock = new Main();
        MainMenu mainMenuSupplier = new MainMenu();
        Timer timer = setAlertCheck();
        mainLoop(mainMenuStock,mainMenuSupplier, roleID);
        timer.cancel();
    }


    public static void startGUI(int roleID) {
        System.out.println("Starting GUI for role: " + roleID);
        Timer timer = setAlertCheck();
        new MainGUIMenuFrame(roleID);
        timer.cancel();
    }


    public static void mainLoop(Main mainMenuStock, MainMenu mainMenuSupplier, int roleID){

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Welcome to Super-Li System! \n" +
                    "First of all, what data would you like to load? : \n" +
                    "1 - Reset DB and re-load the predefined data \n" +
                    "2 - Load the data that is currently in the DB \n" +
                    "3 - Log out from the system \n");

            int option = scanner.nextInt();
            boolean shouldLoad = false;
            while (option < 1 || option > 3) {
                System.out.println("Illegal press. Please press again.");
                option = scanner.nextInt();
            }
            if (option == 1) {
                mainMenuStock.removeAllData();
                mainMenuSupplier.removeAllData();
                mainMenuStock.setUp();
                mainMenuSupplier.setUP();
                shouldLoad = true;
            }
            if (option == 3) {
                System.out.println("log out");
                scanner.close();
                return;
            }
            if (roleID == 1) {
                System.out.println("Welcome Manager! \n Please select Your Desired Action:  \n" +
                        "1 - Stock management. \n" +
                        "2 - Suppliers management. \n" +
                        "3 - Log out from the system \n");
                option = scanner.nextInt();
                while (option < 1 || option > 3) {
                    System.out.println("Illegal press. Please press again.");
                    option = scanner.nextInt();
                }
                if (option == 1) {
                    mainMenuStock.startMenu(shouldLoad);
                } else if (option == 2) {
                    mainMenuSupplier.startMenu();
                } else {
                    return;
                }
            } else if (roleID == 2) {
                mainMenuStock.startMenu(shouldLoad);
            } else if (roleID == 3) {
                mainMenuSupplier.startMenu();
            }
        }
    }


    public static Timer  setAlertCheck(){
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 2);

        if (today.getTimeInMillis() < System.currentTimeMillis()) {
            // It's already past 00:02, move to next day
            today.add(Calendar.DAY_OF_MONTH, 1);
        }

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                synchronized (this) {
                    // Code that needs to be synchronized
                    System.out.println("I am the timer");
                    MainMenu mainMenuSupplier = new MainMenu();
                    try {
                        mainMenuSupplier.alertPeriodOrders();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("done1");
                }

            }
        };

        timer.schedule(task, today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); // period: 1 day
        // timer.schedule(task, today.getTime(), TimeUnit.MILLISECONDS.convert(15, TimeUnit.SECONDS)); // period: 5 seconds
        return timer;
    }


}