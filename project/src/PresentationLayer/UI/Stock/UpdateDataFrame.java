package PresentationLayer.UI.Stock;

import PresentationLayer.UI.InputTools;
import ServiceLayer.Stock.StockService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class UpdateDataFrame extends JFrame implements ActionListener {
    StockService ss;
    JLabel itemIDL;
    JLabel productIDL;
    JLabel valueL;
    JTextField IDT;
    JTextField valueT;
    JComboBox comboBox;
    JButton button;
    JButton changeButton;
    JButton damageButton;
    GridLayout additionalLayout;
    JPanel additionalPanel;
    JPanel bodyP;
    JPanel endP;
    int type;
    String damageType;

    UpdateDataFrame(StockService ss){
        this.ss = ss;

        JLabel titleL = new JLabel("Update data");
        titleL.setHorizontalAlignment(JLabel.CENTER);
        titleL.setVerticalAlignment(JLabel.TOP);
        titleL.setFont(new Font("Gisha", Font.BOLD, 20));
        titleL.setBounds(100, 0, 200, 80);

        ////////////////////////////////////////////////////////


        String[] toDisplay = {"Report on a damaged item", "Update product demand", "Update product supply time", "Change item's location", "Update the purchase price from suppliers"};
        comboBox = new JComboBox(toDisplay);
        comboBox.setFocusable(false);
        comboBox.addActionListener(this);

        button = new JButton("Choose action");
        button.setFocusable(false);
        button.addActionListener(this);
        //button.setVisible(false);

        changeButton = new JButton("Make change");
        changeButton.setFocusable(false);
        changeButton.addActionListener(this);
        changeButton.setEnabled(false);

        damageButton = new JButton("Choose damage");
        damageButton.setFocusable(false);
        damageButton.addActionListener(this);
        damageButton.setEnabled(true);

        itemIDL = new JLabel("Item ID: ");
        itemIDL.setHorizontalAlignment(JLabel.LEFT);
        itemIDL.setVerticalAlignment(JLabel.TOP);
        itemIDL.setFont(new Font("Gisha", Font.PLAIN, 14));
        //titleL.setBounds(100, 0, 200, 80);

        valueL = new JLabel("");
        valueL.setHorizontalAlignment(JLabel.LEFT);
        valueL.setVerticalAlignment(JLabel.TOP);
        valueL.setFont(new Font("Gisha", Font.PLAIN, 14));

        productIDL = new JLabel("Product ID: ");
        productIDL.setHorizontalAlignment(JLabel.LEFT);
        productIDL.setVerticalAlignment(JLabel.TOP);
        productIDL.setFont(new Font("Gisha", Font.PLAIN, 14));

        IDT = new JTextField();
        IDT.setFont(new Font("Gisha", Font.PLAIN, 14));

        valueT = new JTextField();
        valueT.setFont(new Font("Gisha", Font.PLAIN, 14));


        //*****************************************************//


        /////////////////////////////////////////////////////////////////

        additionalLayout = new GridLayout(2, 2, 5, 5);

        additionalPanel = new JPanel();
        additionalPanel.setLayout(additionalLayout);
        additionalPanel.setBackground(Color.gray);
        //additionalPanel.add(button);
        additionalPanel.setVisible(true);

        //*****************************************************//

        JPanel titleP = new JPanel();
        titleP.setBackground(Color.PINK);
        titleP.add(titleL);

        bodyP = new JPanel();
        bodyP.setLayout(new FlowLayout(FlowLayout.CENTER));
        bodyP.setBackground(Color.gray);
        bodyP.add(comboBox);

        endP = new JPanel();
        endP.setLayout(new FlowLayout(FlowLayout.CENTER));
        endP.setBackground(Color.gray);
        endP.add(button);

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
        this.add(endP, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            button.setEnabled(false);
            comboBox.setEnabled(false);

            if(comboBox.getSelectedItem().equals("Report on a damaged item")){
                this.type = 1;
                damageType = "";
                changeButton.setEnabled(false);
                IDT.setText("");
                additionalPanel.setVisible(true);
                additionalPanel.add(itemIDL);
                additionalPanel.add(IDT);
                additionalPanel.add(damageButton);
                additionalPanel.add(changeButton);
                bodyP.setVisible(false);
                this.add(additionalPanel, BorderLayout.CENTER);
            }
            if(comboBox.getSelectedItem().equals("Update product demand")){
                this.type = 2;
                IDT.setText("");
                valueL.setText("Demand: ");
                changeButton.setEnabled(true);
                additionalPanel.setVisible(true);
                additionalPanel.add(productIDL);
                additionalPanel.add(IDT);
                additionalPanel.add(valueL);
                additionalPanel.add(valueT);
                endP.add(changeButton);
                bodyP.setVisible(false);
                this.add(additionalPanel, BorderLayout.CENTER);
            }
            if(comboBox.getSelectedItem().equals("Update product supply time")){
                this.type = 3;
                IDT.setText("");
                valueL.setText("Supply time: ");
                changeButton.setEnabled(true);
                additionalPanel.setVisible(true);
                additionalPanel.add(productIDL);
                additionalPanel.add(IDT);
                additionalPanel.add(valueL);
                additionalPanel.add(valueT);
                endP.add(changeButton);
                bodyP.setVisible(false);
                this.add(additionalPanel, BorderLayout.CENTER);
            }
            if(comboBox.getSelectedItem().equals("Change item's location")){
                this.type = 4;
                changeButton.setEnabled(true);
                additionalPanel.setVisible(true);
                additionalPanel.setLayout(new GridLayout(1,1,5,5));
                additionalPanel.add(itemIDL);
                additionalPanel.add(IDT);
                endP.add(changeButton);
                bodyP.setVisible(false);
                this.add(additionalPanel, BorderLayout.CENTER);
            }
            if(comboBox.getSelectedItem().equals("Update the purchase price from suppliers")) {
                this.type = 5;
                IDT.setText("");
                valueL.setText("Purchase price: ");
                changeButton.setEnabled(true);
                additionalPanel.setVisible(true);
                additionalPanel.add(productIDL);
                additionalPanel.add(IDT);
                additionalPanel.add(valueL);
                additionalPanel.add(valueT);
                endP.add(changeButton);
                bodyP.setVisible(false);
                this.add(additionalPanel, BorderLayout.CENTER);
            }
                this.revalidate();
            this.repaint();
        }
        if(e.getSource() == damageButton){
            Map<Integer, String> damages = ss.getDamages();
            String out = "Choose damage type for item: \n";
            for (Integer key: damages.keySet()) {
                out = out + key + ": " + damages.get(key) + "\n";
            }
            Integer damages_choice = InputTools.insureIntInput(this,JOptionPane.showInputDialog(null, out, "Damages Choice"));
            while(damages_choice == null || !damages.containsKey(damages_choice)){
                damages_choice = InputTools.insureIntInput(this, JOptionPane.showInputDialog(null, "Invalid input given. Try again! \n" + out, "Damages Choice"));
            }
            damageType = damages.get(damages_choice);
            changeButton.setEnabled(true);
        }
        if(e.getSource() == changeButton){
            if(type == 1){
                try{
                    ss.addDamagedItem(IDT.getText(), damageType);
                    additionalPanel.removeAll();
                    additionalPanel.setVisible(false);
                    bodyP.setVisible(true);
                    button.setEnabled(true);
                    comboBox.setEnabled(true);
                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage() + " Try again!", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
            if(type == 2){
                Integer demand = InputTools.insureIntInput(this, valueT.getText());
                if(demand!= null) {
                    try {
                        ss.SetDemand(IDT.getText(), demand);
                        endP.remove(changeButton);
                        additionalPanel.removeAll();
                        additionalPanel.setVisible(false);
                        bodyP.setVisible(true);
                        button.setEnabled(true);
                        comboBox.setEnabled(true);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage() + " Try again!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            if(type == 3){
                Integer supplyTime = InputTools.insureIntInput(this, valueT.getText());
                if(supplyTime!= null) {
                    try {
                        ss.SetSupplyTime(IDT.getText(), supplyTime);
                        endP.remove(changeButton);
                        additionalPanel.removeAll();
                        additionalPanel.setVisible(false);
                        bodyP.setVisible(true);
                        button.setEnabled(true);
                        comboBox.setEnabled(true);

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage() + " Try again!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            if(type == 4){
                try{
                    ss.moveItem(IDT.getText());
                    endP.remove(changeButton);
                    additionalPanel.setLayout(additionalLayout);
                    additionalPanel.removeAll();
                    additionalPanel.setVisible(false);
                    bodyP.setVisible(true);
                    button.setEnabled(true);
                    comboBox.setEnabled(true);
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage() + " Try again!", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
            if(type == 5){
                Double price = InputTools.insureDoubleInput(this, valueT.getText());
                if(price != null){
                    try{
                        ss.SetPurchasePrice(IDT.getText(), price);
                        endP.remove(changeButton);
                        additionalPanel.removeAll();
                        additionalPanel.setVisible(false);
                        bodyP.setVisible(true);
                        button.setEnabled(true);
                        comboBox.setEnabled(true);
                    }
                    catch (Exception ex){
                        JOptionPane.showMessageDialog(null, ex.getMessage() + " Try again!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
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
}
