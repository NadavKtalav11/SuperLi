package PresentationLayer.UI.Stock;

import BusinessLayer.Tools.Pair;
import PresentationLayer.UI.InputTools;
import ServiceLayer.Stock.StockService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageOrdersFrame extends JFrame implements ActionListener {

    StockService ss;
    int orderType;
    boolean isNewOrder;
    List<Integer> supplyDays;
    Color backgroundColor = Color.gray;
    Map<Integer, Integer> ordersMapper;
    int orderID;
    Pair<String, String> prodToUpdate;
    Map<Pair<String, String>, Integer> productsAndAmountsMapper;
    Map<Integer, Pair<String, String>> indexToProductMapper;


    JPanel daysMenuP;
    JPanel newOrderP;
    JPanel updateOrderP;
    JPanel selectOrderP;
    JPanel selectProdInOrderP;


    JButton newOrder;
    JButton updateOrDeleteOrder;

    JButton automaticOrder;
    JButton expressOrder;

    JLabel selectDaysL;
    JCheckBox checkSunday;
    JCheckBox checkMonday;
    JCheckBox checkTuesday;
    JCheckBox checkWednesday;
    JCheckBox checkThursday;
    JCheckBox checkFriday;
    JCheckBox checkSaturday;
    JButton submitDays;

    JLabel productIDL;
    JButton checkProductID;
    JTextField productIDText;
    String StrProductID;
    String strProductName;
    JLabel enterProductNameL;
    JTextField productNameText;
    JButton submitName;
    JLabel amountL;
    JTextField amountText;
    JButton submitAmount;


    JLabel isDoneL;
    JButton doneInsertProducts;
    JButton addAnotherProduct;


    JLabel selectOrderL;
    JComboBox ordersList;
    JButton selectOrder;
    JButton updateOrder;
    JButton deleteOrder;


    JLabel attToUpdateL;

    JButton updateSupDays;
    JButton addProdToOrder;
    JButton updateOrDeleteProdInOrder;


    JLabel selectProdL;
    JComboBox prodsInOrderList;
    JButton selectProduct;
    JButton updateProdInOrder;
    JButton deleteProdInOrder;

    JLabel updateAmountL;
    JTextField updateAmountText;
    JButton submitUpdatedAmount;



    ManageOrdersFrame(StockService ss) {
        this.ss = ss;

        JLabel titleL = new JLabel("Manage Orders");
        titleL.setHorizontalAlignment(JLabel.CENTER);
        titleL.setVerticalAlignment(JLabel.TOP);
        titleL.setFont(new Font("Gisha", Font.BOLD, 20));
        titleL.setBounds(100, 0, 200, 80);

        ////////////////////////////////////////////////////////


        //*****************************************************//

        newOrder = new JButton("Create a new order");
        newOrder.setFocusable(false);
        newOrder.addActionListener(this);
        newOrder.setVisible(true);

        updateOrDeleteOrder = new JButton("Update / Delete an existing order");
        updateOrDeleteOrder.setFocusable(false);
        updateOrDeleteOrder.addActionListener(this);
        updateOrDeleteOrder.setVisible(true);


        automaticOrder = new JButton("Create a new Automatic Order");
        automaticOrder.setFocusable(false);
        automaticOrder.addActionListener(this);
        automaticOrder.setAlignmentX(CENTER_ALIGNMENT);
        automaticOrder.setVisible(false);

        expressOrder = new JButton("Create a new one-time Express Order");
        expressOrder.setFocusable(false);
        expressOrder.addActionListener(this);
        expressOrder.setAlignmentX(CENTER_ALIGNMENT);
        expressOrder.setVisible(false);


        selectDaysL = new JLabel("Select desired supply days: ");
        selectDaysL.setFont(new Font("Gisha", Font.BOLD, 14));
        selectDaysL.setForeground(Color.white);
        selectDaysL.setAlignmentX(CENTER_ALIGNMENT);


        checkSunday = new JCheckBox("Sunday");
        checkSunday.setFocusable(false);
        checkSunday.setBackground(backgroundColor);
        checkSunday.setFont(new Font("Gisha", Font.BOLD, 12));
        checkSunday.setForeground(Color.white);

        checkMonday = new JCheckBox("Monday");
        checkMonday.setFocusable(false);
        checkMonday.setBackground(backgroundColor);
        checkMonday.setFont(new Font("Gisha", Font.BOLD, 12));
        checkMonday.setForeground(Color.white);

        checkTuesday = new JCheckBox("Tuesday");
        checkTuesday.setFocusable(false);
        checkTuesday.setBackground(backgroundColor);
        checkTuesday.setFont(new Font("Gisha", Font.BOLD, 12));
        checkTuesday.setForeground(Color.white);

        checkWednesday = new JCheckBox("Wednesday");
        checkWednesday.setFocusable(false);
        checkWednesday.setBackground(backgroundColor);
        checkWednesday.setFont(new Font("Gisha", Font.BOLD, 12));
        checkWednesday.setForeground(Color.white);

        checkThursday = new JCheckBox("Thursday");
        checkThursday.setFocusable(false);
        checkThursday.setBackground(backgroundColor);
        checkThursday.setFont(new Font("Gisha", Font.BOLD, 12));
        checkThursday.setForeground(Color.white);

        checkFriday = new JCheckBox("Friday");
        checkFriday.setFocusable(false);
        checkFriday.setBackground(backgroundColor);
        checkFriday.setFont(new Font("Gisha", Font.BOLD, 12));
        checkFriday.setForeground(Color.white);

        checkSaturday = new JCheckBox("Saturday");
        checkSaturday.setFocusable(false);
        checkSaturday.setBackground(backgroundColor);
        checkSaturday.setFont(new Font("Gisha", Font.BOLD, 12));
        checkSaturday.setForeground(Color.white);

        submitDays = new JButton("Submit Days");
        submitDays.setFocusable(false);
        submitDays.addActionListener(this);

        productIDL = new JLabel("Enter Product ID:");
        productIDL.setFont(new Font("Gisha", Font.BOLD, 14));
        productIDL.setForeground(Color.white);
        productIDL.setFocusable(false);
        productIDL.setVisible(false);
        productIDL.setAlignmentX(CENTER_ALIGNMENT);

        productIDText = new JTextField();
        productIDText.setPreferredSize(new Dimension(0, 25));
        productIDText.addActionListener(this);
        productIDText.setVisible(false);
        productIDText.setAlignmentX(CENTER_ALIGNMENT);

        checkProductID = new JButton("Submit");
        checkProductID.setFocusable(false);
        checkProductID.addActionListener(this);
        checkProductID.setVisible(false);
        checkProductID.setAlignmentX(CENTER_ALIGNMENT);

        enterProductNameL = new JLabel("<html>Product doesn't exist. Enter its name:</html>");
        enterProductNameL.setFont(new Font("Gisha", Font.BOLD, 14));
        enterProductNameL.setForeground(Color.white);
        enterProductNameL.setFocusable(false);
        enterProductNameL.setVisible(false);
        enterProductNameL.setAlignmentX(CENTER_ALIGNMENT);

        productNameText = new JTextField();
        productNameText.setPreferredSize(new Dimension(0, 25));
        productNameText.addActionListener(this);
        productNameText.setVisible(false);
        productNameText.setAlignmentX(CENTER_ALIGNMENT);

        submitName = new JButton("Submit");
        submitName.setFocusable(false);
        submitName.addActionListener(this);
        submitName.setVisible(false);
        submitName.setAlignmentX(CENTER_ALIGNMENT);

        amountL = new JLabel();
        amountL.setFont(new Font("Gisha", Font.BOLD, 14));
        amountL.setForeground(Color.white);
        amountL.setFocusable(false);
        amountL.setVisible(false);
        amountL.setAlignmentX(CENTER_ALIGNMENT);

        amountText = new JTextField();
        amountText.setPreferredSize(new Dimension(0, 25));
        amountText.addActionListener(this);
        amountText.setVisible(false);
        amountText.setAlignmentX(CENTER_ALIGNMENT);

        submitAmount = new JButton("Submit");
        submitAmount.setFocusable(false);
        submitAmount.addActionListener(this);
        submitAmount.setVisible(false);
        submitAmount.setAlignmentX(CENTER_ALIGNMENT);

        isDoneL = new JLabel("Done entering products?");
        isDoneL.setFont(new Font("Gisha", Font.BOLD, 14));
        isDoneL.setForeground(Color.white);
        isDoneL.setFocusable(false);
        isDoneL.setVisible(false);
        isDoneL.setAlignmentX(CENTER_ALIGNMENT);

        doneInsertProducts = new JButton("YES - Submit order");
        doneInsertProducts.setFocusable(false);
        doneInsertProducts.addActionListener(this);
        doneInsertProducts.setVisible(false);
        doneInsertProducts.setAlignmentX(CENTER_ALIGNMENT);

        addAnotherProduct = new JButton("NO - Add another product");
        addAnotherProduct.setFocusable(false);
        addAnotherProduct.addActionListener(this);
        addAnotherProduct.setVisible(false);
        addAnotherProduct.setAlignmentX(CENTER_ALIGNMENT);

        ////////////////////

        selectOrderL = new JLabel("Select Order: ");
        selectOrderL.setFont(new Font("Gisha", Font.BOLD, 14));
        selectOrderL.setForeground(Color.white);

        String[] ordersToDisplay = {"OrderA", "OrderB", "OrderC"};
        ordersList = new JComboBox(ordersToDisplay);
        ordersList.setFocusable(false);
        ordersList.addActionListener(this);

        selectOrder = new JButton("Select");
        selectOrder.setFocusable(false);
        selectOrder.addActionListener(this);

        updateOrder = new JButton("Update the Order");
        updateOrder.setFocusable(false);
        updateOrder.addActionListener(this);
        updateOrder.setVisible(false);

        deleteOrder = new JButton("Delete the Order");
        deleteOrder.setFocusable(false);
        deleteOrder.addActionListener(this);
        deleteOrder.setVisible(false);

        attToUpdateL = new JLabel("Select your desired action: ");
        attToUpdateL.setFont(new Font("Gisha", Font.BOLD, 14));
        attToUpdateL.setForeground(Color.white);
        //attToUpdateL.setVisible(false);


        updateSupDays = new JButton("select new supply days");
        updateSupDays.setFocusable(false);
        updateSupDays.addActionListener(this);
        //updateSupDays.setVisible(false);

        addProdToOrder = new JButton("Add a product to order");
        addProdToOrder.setFocusable(false);
        addProdToOrder.addActionListener(this);
        //addProdToOrder.setVisible(false);

        updateOrDeleteProdInOrder = new JButton("Update / Delete a product in order");
        updateOrDeleteProdInOrder.setFocusable(false);
        updateOrDeleteProdInOrder.addActionListener(this);
        //updateOrDeleteProdInOrder.setVisible(false);

        selectProdL = new JLabel("Select Product: ");
        selectProdL.setFont(new Font("Gisha", Font.BOLD, 14));
        selectProdL.setForeground(Color.white);

        String[] productsToDisplay = {"ProductA", "ProductB", "ProductC"};
        prodsInOrderList = new JComboBox(productsToDisplay);
        prodsInOrderList.setFocusable(false);
        prodsInOrderList.addActionListener(this);

        selectProduct = new JButton("Select");
        selectProduct.setFocusable(false);
        selectProduct.addActionListener(this);
        //selectProduct.setVisible(false);

        updateProdInOrder = new JButton("Update product's amount");
        updateProdInOrder.setFocusable(false);
        updateProdInOrder.addActionListener(this);
        updateProdInOrder.setVisible(false);

        deleteProdInOrder = new JButton("Delete the product from order");
        deleteProdInOrder.setFocusable(false);
        deleteProdInOrder.addActionListener(this);
        deleteProdInOrder.setVisible(false);

        updateProdInOrder = new JButton("Update a product in order");
        updateProdInOrder.setFocusable(false);
        updateProdInOrder.addActionListener(this);
        updateProdInOrder.setVisible(false);

        deleteProdInOrder = new JButton("Delete a product from order");
        deleteProdInOrder.setFocusable(false);
        deleteProdInOrder.addActionListener(this);
        deleteProdInOrder.setVisible(false);


        updateAmountL = new JLabel("Enter the new amount: ");
        updateAmountL.setFont(new Font("Gisha", Font.BOLD, 14));
        updateAmountL.setForeground(Color.white);
        updateAmountL.setVisible(false);

        updateAmountText = new JTextField(12);
        updateAmountText.setPreferredSize(new Dimension(0, 25));
        updateAmountText.addActionListener(this);
        updateAmountText.setVisible(false);

        submitUpdatedAmount = new JButton("Update");
        submitUpdatedAmount.setFocusable(false);
        submitUpdatedAmount.addActionListener(this);
        submitUpdatedAmount.setVisible(false);

        /////////////////////////////////////////////////////////////////

        //*****************************************************//

        daysMenuP = new JPanel();
        daysMenuP.setLayout(new GridLayout(9,1, 1, 8));
        daysMenuP.setBackground(Color.gray);
        daysMenuP.add(selectDaysL);
        daysMenuP.add(checkSunday);
        daysMenuP.add(checkMonday);
        daysMenuP.add(checkTuesday);
        daysMenuP.add(checkWednesday);
        daysMenuP.add(checkThursday);
        daysMenuP.add(checkFriday);
        daysMenuP.add(checkSaturday);
        daysMenuP.add(submitDays);
        daysMenuP.setVisible(false);


        newOrderP = new JPanel();
        newOrderP.setLayout(new BoxLayout(newOrderP, BoxLayout.Y_AXIS));
        newOrderP.setBackground(Color.gray);
        newOrderP.add(productIDL);
        newOrderP.add(productIDText);
        newOrderP.add(checkProductID);
        newOrderP.add(enterProductNameL);
        newOrderP.add(productNameText);
        newOrderP.add(submitName);
        newOrderP.add(amountL);
        newOrderP.add(amountText);
        newOrderP.add(submitAmount);
        newOrderP.add(isDoneL);
        newOrderP.add(doneInsertProducts);
        newOrderP.add(addAnotherProduct);
        newOrderP.setVisible(false);


        selectOrderP = new JPanel(new FlowLayout(FlowLayout.CENTER));
        selectOrderP.setBackground(Color.gray);
        selectOrderP.add(selectOrderL);
        selectOrderP.add(ordersList);
        selectOrderP.add(selectOrder);
        selectOrderP.add(updateOrder);
        selectOrderP.add(deleteOrder);
        selectOrderP.setVisible(false);

        selectProdInOrderP = new JPanel(new FlowLayout(FlowLayout.CENTER));
        selectProdInOrderP.setBackground(Color.gray);
        selectProdInOrderP.add(selectProdL);
        selectProdInOrderP.add(prodsInOrderList);
        selectProdInOrderP.add(selectProduct);
        selectProdInOrderP.add(updateProdInOrder);
        selectProdInOrderP.add(deleteProdInOrder);
        selectProdInOrderP.setVisible(false);

        updateOrderP = new JPanel(new FlowLayout(FlowLayout.CENTER));
        updateOrderP.setBackground(Color.gray);
        updateOrderP.add(attToUpdateL);
        updateOrderP.add(updateSupDays);
        updateOrderP.add(addProdToOrder);
        updateOrderP.add(updateOrDeleteProdInOrder);
        updateOrderP.add(selectProdInOrderP);
        updateOrderP.add(updateAmountL);
        updateOrderP.add(updateAmountText);
        updateOrderP.add(submitUpdatedAmount);
        updateOrderP.setVisible(false);


        JPanel titleP = new JPanel();
        titleP.setBackground(Color.PINK);
        titleP.add(titleL);

        JPanel bodyP = new JPanel();
        //bodyP.setLayout(new FlowLayout(FlowLayout.CENTER));
        bodyP.setBackground(Color.gray);
        bodyP.add(newOrder);
        bodyP.add(updateOrDeleteOrder);
        bodyP.add(automaticOrder);
        bodyP.add(expressOrder);
        bodyP.add(daysMenuP);
        bodyP.add(newOrderP);
        bodyP.add(selectOrderP);
        bodyP.add(updateOrderP);

        ////////////////////////////////////////////////////////////////////

        //*****************************************************//


        this.setTitle("Manage Orders");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(300,300));
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(139, 187, 161));
        this.setLayout(new BorderLayout());
        this.add(titleP, BorderLayout.NORTH);
        this.add(bodyP, BorderLayout.CENTER);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newOrder) {
            isNewOrder = true;
            productsAndAmountsMapper = new HashMap<>();
            updateOrDeleteOrder.setVisible(false);
            newOrder.setEnabled(false);
            automaticOrder.setVisible(true);
            expressOrder.setVisible(true);
        }
        if (e.getSource() == updateOrDeleteOrder) {
            //updateOrderP.setVisible(true);
            isNewOrder = false;
            newOrder.setVisible(false);
            updateOrDeleteOrder.setEnabled(false);
            if (createOrdersList()) {
                selectOrderP.setVisible(true);
            }
        }
        if (e.getSource() == automaticOrder) {
            orderType = 1;
            daysMenuP.setVisible(true);
            this.setSize(new Dimension(300, 450));
            automaticOrder.setEnabled(false);
            expressOrder.setVisible(false);
        }
        if (e.getSource() == expressOrder) {
            orderType = 2;
            automaticOrder.setVisible(false);
            expressOrder.setEnabled(false);
            newOrderP.setVisible(true);
            productIDL.setVisible(true);
            productIDText.setVisible(true);
            checkProductID.setVisible(true);
        }
        if (e.getSource() == submitDays) {
            this.setSize(new Dimension(300, 300));
            supplyDays = new ArrayList<>();
            if (checkSunday.isSelected()) {
                supplyDays.add(0);
            }
            if (checkMonday.isSelected()) {
                supplyDays.add(1);
            }
            if (checkTuesday.isSelected()) {
                supplyDays.add(2);
            }
            if (checkWednesday.isSelected()) {
                supplyDays.add(3);
            }
            if (checkThursday.isSelected()) {
                supplyDays.add(4);
            }
            if (checkFriday.isSelected()) {
                supplyDays.add(5);
            }
            if (checkSaturday.isSelected()) {
                supplyDays.add(6);
            }
            if (supplyDays.isEmpty()) {
                JOptionPane.showMessageDialog(this, "You must select at least one day");
                this.setSize(new Dimension(300, 450));
            } else {
                if (isNewOrder) {
                    daysMenuP.setVisible(false);
                    newOrderP.setVisible(true);
                    productIDL.setVisible(true);
                    productIDText.setVisible(true);
                    checkProductID.setVisible(true);
                } else {
                    boolean isSuccess = updateSupplyDays();
                    //TODO - "success / fail" -> return to menu
                    this.dispose();
                }
            }
        }
        if (e.getSource() == checkProductID) {
            if (productIDText.getText().length() == 0) {
                JOptionPane.showMessageDialog(this, "Enter product ID");
            } else {
                StrProductID = productIDText.getText();
                if (InputTools.checkProductExists(this, ss, StrProductID)) {
                    try {
                        strProductName = ss.getProductName(StrProductID);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
                    }
                    productIDText.setEnabled(false);
                    checkProductID.setVisible(false);
                    amountL.setText("Amount of '" + strProductName + "' to order:");
                    amountL.setVisible(true);
                    amountText.setVisible(true);
                    submitAmount.setVisible(true);
                } else {
                    productIDText.setEnabled(false);
                    checkProductID.setVisible(false);
                    enterProductNameL.setText("Product doesn't exist. Enter its name:");
                    enterProductNameL.setVisible(true);
                    productNameText.setVisible(true);
                    productNameText.setEnabled(true);
                    submitName.setVisible(true);
                }
            }
        }
        if (e.getSource() == submitName) {
            submitName.setVisible(false);
            productNameText.setEnabled(false);
            if (productNameText.getText().length() == 0) {
                JOptionPane.showMessageDialog(this, "Enter product name");
            } else {
                strProductName = productNameText.getText();
                productNameText.setEnabled(false);
                submitName.setVisible(false);
                amountL.setText("Amount of '" + strProductName + "' to order:");
                amountL.setVisible(true);
                amountText.setVisible(true);
                amountText.setEnabled(true);
                submitAmount.setVisible(true);
            }
        }
        if (e.getSource() == submitAmount) {
            this.setSize(new Dimension(300, 350));
            productsAndAmountsMapper.put(new Pair<>(StrProductID, strProductName), InputTools.insureIntInput(this, amountText.getText()));
            amountText.setEnabled(false);
            submitAmount.setVisible(false);
            isDoneL.setVisible(true);
            doneInsertProducts.setVisible(true);
            addAnotherProduct.setVisible(true);
        }
        if (e.getSource() == doneInsertProducts) {
            if(isNewOrder) {
                if (orderType == 1) {
                    addAutoOrder();
                }
                if (orderType == 2) {
                    addExpressOrder();
                }
            }
            else {
                boolean isSuccess = updateOrder();
                //TODO - "success / fail" -> return to menu
                this.dispose();
            }
        }
        if (e.getSource() == addAnotherProduct) {
            this.setSize(new Dimension(300, 300));
            productIDL.setVisible(true);
            productIDText.setText("");
            productIDText.setVisible(true);
            productIDText.setEnabled(true);
            checkProductID.setEnabled(true);
            checkProductID.setVisible(true);
            StrProductID = "";
            strProductName = "";
            enterProductNameL.setVisible(false);
            productNameText.setVisible(false);
            productNameText.setEnabled(true);
            productNameText.setText("");
            amountL.setVisible(false);
            amountText.setVisible(false);
            amountText.setEnabled(true);
            amountText.setText("");
            isDoneL.setVisible(false);
            doneInsertProducts.setVisible(false);
            addAnotherProduct.setVisible(false);
        }

        if (e.getSource() == selectOrder) {
            orderID = ordersMapper.get(ordersList.getSelectedIndex());
            ordersList.setEnabled(false);
            selectOrder.setEnabled(false);
            updateOrder.setVisible(true);
            deleteOrder.setVisible(true);
        }
        if (e.getSource() == updateOrder) {
            selectOrderP.setVisible(false);
            updateOrderP.setVisible(true);
            updateSupDays.setVisible(true);
            addProdToOrder.setVisible(true);
            updateOrDeleteProdInOrder.setVisible(true);
        }
        if (e.getSource() == deleteOrder) {
            boolean isSuccess = deleteSelectedOrder();
            //TODO - "success / fail" -> return to menu
            this.dispose();
        }
        if (e.getSource() == updateSupDays) {
            updateSupDays.setEnabled(false);
            addProdToOrder.setVisible(false);
            updateOrDeleteProdInOrder.setVisible(false);
            daysMenuP.setVisible(true);
            this.setSize(new Dimension(300, 450));
        }
        if (e.getSource() == addProdToOrder) {
            createProdsInOrderList();
            updateSupDays.setVisible(false);
            addProdToOrder.setEnabled(false);
            updateOrDeleteProdInOrder.setVisible(false);
            newOrderP.setVisible(true);
            productIDL.setVisible(true);
            productIDText.setVisible(true);
            checkProductID.setVisible(true);
            this.setSize(new Dimension(300, 450));
        }
        if (e.getSource() == updateOrDeleteProdInOrder) {
            this.setSize(new Dimension(300, 450));
            updateSupDays.setVisible(false);
            addProdToOrder.setVisible(false);
            updateOrDeleteProdInOrder.setEnabled(false);
            createProdsInOrderList();
            selectProdInOrderP.setVisible(true);
            this.setSize(new Dimension(300, 450));
        }
        if (e.getSource() == selectProduct) {
            prodToUpdate = indexToProductMapper.get(prodsInOrderList.getSelectedIndex());
            prodsInOrderList.setEnabled(false);
            selectProduct.setEnabled(false);
            updateProdInOrder.setVisible(true);
            deleteProdInOrder.setVisible(true);
            this.setSize(new Dimension(300, 300));
        }
        if (e.getSource() == deleteProdInOrder) {
            daysMenuP.setVisible(true);
            productsAndAmountsMapper.replace(prodToUpdate, 0);
            boolean isSuccess = updateOrder();
            //TODO - "success / fail" -> return to menu
            this.dispose();
        }
        if (e.getSource() == updateProdInOrder) {
            updateProdInOrder.setEnabled(false);
            deleteProdInOrder.setVisible(false);
            updateAmountText.setVisible(true);
            updateAmountL.setVisible(true);
            submitUpdatedAmount.setVisible(true);
        }
        if (e.getSource() == submitUpdatedAmount) {
            productsAndAmountsMapper.replace(prodToUpdate, InputTools.insureIntInput(this, updateAmountText.getText()));
            boolean isSuccess = updateOrder();
            //TODO - "success / fail" -> return to menu
            this.dispose();
            }
        }


    private void addAutoOrder() {
        int orderID = -1;
        try {
            orderID = ss.addAutomaticOrder(supplyDays, productsAndAmountsMapper);
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "An error occurred, order insertion failed: " + ex.getMessage());
        }
        if (orderID != -1) {
            JOptionPane.showMessageDialog(this, "An automatic Order was successfully added to the System!\n Order ID: " + orderID +  "\n Make sure to remember the order ID, it is mandatory.");
        }
        this.dispose();
    }

    private void addExpressOrder() {
        boolean orderAdded = false;
        try {
            orderAdded = ss.addExpressOrder(productsAndAmountsMapper);
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "An error occurred, order insertion failed: " + ex.getMessage());
        }
        if (orderAdded) {
            JOptionPane.showMessageDialog(this, "An Express Order was successfully added to the System!\n Products will arrive as soon as possible.");
        }
        this.dispose();
    }

    public boolean createOrdersList() {
        Map<Integer, String> OrdersListMap = null;
        ordersMapper = new HashMap<>();
        try {
            OrdersListMap = ss.getAutoOrdersList();
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
        }
        if ((OrdersListMap == null) || (OrdersListMap.size() == 0)) {
            JOptionPane.showMessageDialog(this, "No Orders to show!");
            return false;
        }
        else {
            return updateOrdersList(OrdersListMap);
        }
    }

    public boolean updateOrdersList(Map<Integer, String> ordersListMap) {
        String[] listToDisplay = new String[ordersListMap.size()];
        int counter = 0;
        for (int id : ordersListMap.keySet()) {
            listToDisplay[counter] = ordersListMap.get(id);
            ordersMapper.put(counter, id);
            counter++;
        }
        ordersList.setModel(new javax.swing.DefaultComboBoxModel<>(listToDisplay));
        return true;
    }

    private boolean deleteSelectedOrder() {
        try {
            ss.deleteAutomaticOrder(orderID);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
            return false;
        }
        return true;
    }

    private boolean updateSupplyDays (){
        boolean updated;
        try{
            updated = ss.updateOrderSupplyDays(orderID, supplyDays);
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
            updated =  false;
        }
        return updated;
    }

    public void createProdsInOrderList() {
        productsAndAmountsMapper = new HashMap<>();
        try {
            productsAndAmountsMapper = ss.getOrderProductsMap(orderID);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
        }
        if ((productsAndAmountsMapper == null) || (productsAndAmountsMapper.size() == 0)) {
            JOptionPane.showMessageDialog(this, "No products in order to show!");
        } else {
            updateProdsList();
        }
    }

    public void updateProdsList() {
        indexToProductMapper = new HashMap<>();
        String[] listToDisplay = new String[productsAndAmountsMapper.size()];
        int counter = 0;
        for (Pair<String, String> product : productsAndAmountsMapper.keySet()) {
            listToDisplay[counter] = product.getSecond() + " (" + product.getFirst() + ") - " + productsAndAmountsMapper.get(product) + " items";
            indexToProductMapper.put(counter, product);
                counter++;
        }
        prodsInOrderList.setModel(new javax.swing.DefaultComboBoxModel<>(listToDisplay));
    }

    private boolean updateOrder() {
        boolean updated;
        try{
            updated = ss.updateOrderProductsAndAmounts(orderID, productsAndAmountsMapper);
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
            updated =  false;
        }
        return updated;
    }
}
