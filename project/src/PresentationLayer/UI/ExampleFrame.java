package PresentationLayer.UI;

import ServiceLayer.Stock.StockService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExampleFrame extends JFrame implements ActionListener {

    StockService ss;
    JComboBox comboBox;
    JButton button;
    GridLayout additionalLayout;
    JPanel additionalPanel;

    ExampleFrame(StockService ss){
        this.ss = ss;

        JLabel titleL = new JLabel("Manage Discounts");
        titleL.setHorizontalAlignment(JLabel.CENTER);
        titleL.setVerticalAlignment(JLabel.TOP);
        titleL.setFont(new Font("Gisha", Font.BOLD, 20));
        titleL.setBounds(100, 0, 200, 80);

        ////////////////////////////////////////////////////////


        String[] toDisplay = {" ", " "};
        comboBox = new JComboBox(toDisplay);
        comboBox.setFocusable(false);
        comboBox.addActionListener(this);

        button = new JButton("Create new branch");
        button.setFocusable(false);
        button.addActionListener(this);
        button.setVisible(false);

        //*****************************************************//


        /////////////////////////////////////////////////////////////////

        additionalLayout = new GridLayout(2, 2, 5, 5);

        additionalPanel = new JPanel();
        additionalPanel.setLayout(additionalLayout);
        additionalPanel.setBackground(Color.gray);
        additionalPanel.add(button);
        additionalPanel.setVisible(false);

        //*****************************************************//

        JPanel titleP = new JPanel();
        titleP.setBackground(Color.PINK);
        titleP.add(titleL);

        JPanel bodyP = new JPanel();
        bodyP.setLayout(new FlowLayout(FlowLayout.CENTER));
        bodyP.setBackground(Color.gray);

        ////////////////////////////////////////////////////////////////////

        //*****************************************************//


        this.setTitle("Manage Discounts");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300, 300);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(139, 187, 161));
        this.setLayout(new BorderLayout());
        this.add(titleP, BorderLayout.NORTH);
        this.add(bodyP, BorderLayout.CENTER);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {

        }
    }



    public boolean checkProductExists(String productID) {
        try  {
            return ss.checkProductExist(productID);
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
        }
        return true;
    }

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

}
