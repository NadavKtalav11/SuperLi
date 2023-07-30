package PresentationLayer.UI;

import ServiceLayer.Stock.StockService;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class InputTools {

    String[] weekdays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    public static Date insureDateInput(JFrame parent, String dateInput) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = format.parse(dateInput);
            return date;
        } catch (java.text.ParseException e) {
            JOptionPane.showMessageDialog(parent, "Invalid date: " + dateInput + ". Please enter the date in the format dd-mm-yyyy.");
            return null;
        }
    }

    public static Double insureDoubleInput(JFrame parent, String doubleInput){
        Double inDouble;
        try{
            inDouble = Double.parseDouble(doubleInput);
            return inDouble;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Invalid input: " + doubleInput + ". Please enter a decimal number.");
            return null;
        }
    }

    public static Integer insureIntInput(JFrame parent, String intInput){
        Integer inInt;
        try{
            inInt = Integer.parseInt(intInput);
            return inInt;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Invalid input: " + intInput + ". Please enter a whole number.");
            return null;
        }
    }

    public static boolean checkProductExists(JFrame parent, StockService ss, String productID) {
        try  {
            return ss.checkProductExist(productID);
        } catch (Exception ex){
            JOptionPane.showMessageDialog(parent, "An error occurred: " + ex.getMessage());
        }
        return true;
    }


}
