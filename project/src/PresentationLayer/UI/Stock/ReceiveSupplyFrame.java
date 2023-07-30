package PresentationLayer.UI.Stock;

import ServiceLayer.Stock.StockService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReceiveSupplyFrame extends JFrameCategories implements ActionListener {

    JLabel idL;
    JTextField idT;
    JLabel storeL;
    JTextField storeT;
    JLabel warehouseL;
    JTextField warehouseT;
    JLabel expirationL;
    JTextField expirationT;
    JButton checkButton;
    JButton addButton;
    JButton backButton;
    StockService ss;
    String name;
    Double purchaseDouble;
    Double sellingDouble;
    String manufacture;
    Integer demandInt;
    Integer supplyTimeInt;
    Integer initialNotificationAmountInt;
    JFrame prev;

    ReceiveSupplyFrame(StockService ss, JFrame prev){
        this.prev = prev;
        this.ss = ss;
        JLabel titleL = new JLabel("Receive Stock");
        titleL.setHorizontalAlignment(JLabel.CENTER);
        titleL.setVerticalAlignment(JLabel.TOP);
        titleL.setFont(new Font("Gisha", Font.BOLD, 20));
        titleL.setBounds(100,0,200,80);

        idL = new JLabel("Product ID: ");
        idL.setHorizontalAlignment(JLabel.LEFT);
        idL.setVerticalAlignment(JLabel.TOP);
        idL.setFont(new Font("Gisha", Font.PLAIN, 14));
        idL.setBounds(100,0,200,80);

        storeL = new JLabel("Store amount: ");
        storeL.setHorizontalAlignment(JLabel.LEFT);
        storeL.setVerticalAlignment(JLabel.TOP);
        storeL.setFont(new Font("Gisha", Font.PLAIN, 14));
        storeL.setBounds(100,0,200,80);
        storeL.setVisible(false);

        warehouseL = new JLabel("Warehouse amount: ");
        warehouseL.setHorizontalAlignment(JLabel.LEFT);
        warehouseL.setVerticalAlignment(JLabel.TOP);
        warehouseL.setFont(new Font("Gisha", Font.PLAIN, 14));
        warehouseL.setBounds(100,0,200,80);
        warehouseL.setVisible(false);

        expirationL = new JLabel("Expiration date: ");
        expirationL.setHorizontalAlignment(JLabel.LEFT);
        expirationL.setVerticalAlignment(JLabel.TOP);
        expirationL.setFont(new Font("Gisha", Font.PLAIN, 14));
        expirationL.setBounds(100,0,200,80);
        expirationL.setVisible(false);

        storeT = new JTextField();
        storeT.setFont(new Font("Gisha", Font.ITALIC, 16));
        storeT.setHorizontalAlignment(JTextField.LEFT);
        storeT.setPreferredSize(new Dimension(100,40));
        storeT.setVisible(false);

        warehouseT = new JTextField();
        warehouseT.setFont(new Font("Gisha", Font.ITALIC, 16));
        warehouseT.setHorizontalAlignment(JTextField.LEFT);
        warehouseT.setPreferredSize(new Dimension(100,40));
        warehouseT.setVisible(false);

        expirationT = new JTextField();
        expirationT.setFont(new Font("Gisha", Font.ITALIC, 16));
        expirationT.setHorizontalAlignment(JTextField.LEFT);
        expirationT.setPreferredSize(new Dimension(100,40));
        expirationT.setVisible(false);

        idT = new JTextField();
        idT.setFont(new Font("Gisha", Font.ITALIC, 16));
        idT.setHorizontalAlignment(JTextField.LEFT);
        idT.setPreferredSize(new Dimension(100,20));
        idT.setVisible(true);

        checkButton = new JButton("Check if product exists");
        //checkButton.setHorizontalAlignment(JButton.);
        checkButton.setFocusable(false);
        checkButton.addActionListener(this);

        addButton = new JButton("Add to stock");
        addButton.setFocusable(false);
        addButton.addActionListener(this);
        addButton.setEnabled(false);

        backButton = new JButton("Back");
        backButton.setFocusable(false);
        backButton.addActionListener(this);
        backButton.setEnabled(true);
        backButton.setHorizontalAlignment(JButton.CENTER);

        JPanel titleP = new JPanel();
        titleP.setOpaque(false);
        titleP.add(titleL);

        JPanel bodyP = new JPanel();
        bodyP.setOpaque(false);
        bodyP.setLayout(new GridLayout(5,2,10,10));
        bodyP.add(idL);
        bodyP.add(idT);
        bodyP.add(storeL);
        bodyP.add(storeT);
        bodyP.add(warehouseL);
        bodyP.add(warehouseT);
        bodyP.add(expirationL);
        bodyP.add(expirationT);
        bodyP.add(checkButton);
        bodyP.add(addButton);

        JPanel endP = new JPanel();
        endP.setOpaque(false);
        endP.add(backButton);

        JFrame frame = new JFrame();
        frame.setTitle("Stock System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setResizable(false);
        frame.setSize(400,350);
        frame.setVisible(true);
        frame.getContentPane().setBackground(new Color(139, 187, 161));
        frame.setLayout(new BorderLayout());
        frame.add(titleP, BorderLayout.NORTH);
        frame.add(bodyP, BorderLayout.CENTER);
        frame.add(endP, BorderLayout.SOUTH);
        //frame.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == checkButton){
            if(!idT.getText().equals("")) {
                idT.setEnabled(false);
                checkButton.setEnabled(false);
                boolean b = true;
                try {
                    b = ss.checkProductExist(idT.getText());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    idT.setEnabled(true);
                    checkButton.setEnabled(true);
                }
                if (!b) {
                    JOptionPane.showMessageDialog(null, "Product doesn't exist in the system. Let's add it", "Add Notice", JOptionPane.INFORMATION_MESSAGE);
                    name = JOptionPane.showInputDialog("Enter name of the product");
                    String purchaseString = JOptionPane.showInputDialog("Enter the product purchase price");
                    purchaseDouble = insureDoubleInput(purchaseString);
                    while (purchaseDouble == -1.0) {
                        purchaseString = JOptionPane.showInputDialog("Illegal Press. Double type needed. Try again:");
                        purchaseDouble = insureDoubleInput(purchaseString);
                    }
                    String sellingString = JOptionPane.showInputDialog("Enter the product selling price");
                    sellingDouble = insureDoubleInput(purchaseString);
                    while (sellingDouble == -1.0) {
                        sellingString = JOptionPane.showInputDialog("Illegal Press. Double type needed. Try again:");
                        sellingDouble = insureDoubleInput(sellingString);
                    }
                    manufacture = JOptionPane.showInputDialog("Enter name of the manufacture");
                    String demandString = JOptionPane.showInputDialog("Enter the product demand");
                    demandInt = insureIntegerInput(demandString);
                    while (demandInt == -1) {
                        demandString = JOptionPane.showInputDialog("Illegal Press. Positive integer type needed. Try again:");
                        demandInt = insureIntegerInput(demandString);
                    }
                    String supplyTimeString = JOptionPane.showInputDialog("Enter the product supply time in days");
                    supplyTimeInt = insureIntegerInput(demandString);
                    while (supplyTimeInt == -1) {
                        supplyTimeString = JOptionPane.showInputDialog("Illegal Press. Positive integer type needed. Try again:");
                        supplyTimeInt = insureIntegerInput(supplyTimeString);
                    }
                    String initialNotificationAmountString = JOptionPane.showInputDialog("Enter the product minimum amount to get notification");
                    initialNotificationAmountInt = insureIntegerInput(demandString);
                    while (initialNotificationAmountInt == -1) {
                        initialNotificationAmountString = JOptionPane.showInputDialog("Enter the product minimum amount to get notification");
                        initialNotificationAmountInt = insureIntegerInput(initialNotificationAmountString);
                    }
                    this.setVisible(false);
                    ChooseOrAddCategoryMenuFrame c = new ChooseOrAddCategoryMenuFrame(ss, this);
                    //List<String> categories = c.IDs;
                    //List<String> categories = ChooseCategoryMenuFrame.chooseCategoryMenu(ss, this); // change to add!!!!!
                } else {
                    JOptionPane.showMessageDialog(null, "The product exists. Add items to stock.", "Notify", JOptionPane.INFORMATION_MESSAGE);
                    addButton.setEnabled(true);
                    storeL.setVisible(true);
                    storeT.setVisible(true);
                    warehouseL.setVisible(true);
                    warehouseT.setVisible(true);
                    expirationL.setVisible(true);
                    expirationT.setVisible(true);
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "Product id can't be empty", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
        if(e.getSource() == addButton){
            storeT.setEnabled(false);
            warehouseT.setEnabled(false);
            expirationT.setEnabled(false);
            addButton.setEnabled(false);
            Integer store = insureIntegerInput(storeT.getText());
            if(store == -1){
                JOptionPane.showMessageDialog(null, "Store amount must be a positive integer. Try again!", "Warning", JOptionPane.WARNING_MESSAGE);
                storeT.setEnabled(true);
                warehouseT.setEnabled(true);
                expirationT.setEnabled(true);
                addButton.setEnabled(true);
            }
            Integer warehouse = insureIntegerInput(warehouseT.getText());
            if(warehouse == -1){
                JOptionPane.showMessageDialog(null, "Warehouse amount must be a positive integer. Try again!", "Warning", JOptionPane.WARNING_MESSAGE);
                storeT.setEnabled(true);
                warehouseT.setEnabled(true);
                expirationT.setEnabled(true);
                addButton.setEnabled(true);
            }
            Date expDate = insureDateInput(expirationT.getText());
            if(expDate == null){
                JOptionPane.showMessageDialog(null, "Expiration date must be in format dd-mm-yyyy. Try again!", "Warning", JOptionPane.WARNING_MESSAGE);
                storeT.setEnabled(true);
                warehouseT.setEnabled(true);
                expirationT.setEnabled(true);
                addButton.setEnabled(true);
            }
            try{
                ss.receiveSupply(idT.getText(), store, warehouse, expDate);
                JOptionPane.showMessageDialog(null, "Stock Added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                storeT.setEnabled(true);
                warehouseT.setEnabled(true);
                expirationT.setEnabled(true);
                addButton.setEnabled(false);
                idT.setEnabled(true);
                checkButton.setEnabled(true);
                storeL.setVisible(false);
                storeT.setVisible(false);
                warehouseL.setVisible(false);
                warehouseT.setVisible(false);
                expirationL.setVisible(false);
                expirationT.setVisible(false);
            }
            catch(Exception ex){
                System.out.println(ex.getMessage() + " Try again.");
            }
        }
        if(e.getSource() == backButton){
            this.dispose();
            this.setVisible(false);
            prev.setVisible(true);
        }
    }

    private static Double insureDoubleInput(String input){
        Double readDouble;
        while(true){
            try{
                readDouble = Double.parseDouble(input);
                return readDouble;
            }
            catch(Exception e){
                return -1.0;
            }
        }
    }

    private static Integer insureIntegerInput(String input){
        Integer readInteger;
        while(true){
            try{
                readInteger = Integer.parseInt(input);
                return readInteger;
            }
            catch(Exception e){
                return -1;
            }
        }
    }

    private static Date insureDateInput(String input){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        while(true){
            try {
                Date date = format.parse(input);
                //scanner.close();
                return date;
            } catch (java.text.ParseException e) {
                return null;
            }
        }
    }

    @Override
    public void categoriesDone(List<String> categories) {
        this.setVisible(true);
        try{
            ss.addProduct(idT.getText(), name, purchaseDouble, sellingDouble, manufacture, demandInt, supplyTimeInt, initialNotificationAmountInt, categories);
            addButton.setEnabled(true);
            storeL.setVisible(true);
            storeT.setVisible(true);
            warehouseL.setVisible(true);
            warehouseT.setVisible(true);
            expirationL.setVisible(true);
            expirationT.setVisible(true);
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage() + " Try Again!", "Error", JOptionPane.ERROR_MESSAGE);
            idT.setEnabled(true);
            checkButton.setEnabled(true);
        }
    }
}
