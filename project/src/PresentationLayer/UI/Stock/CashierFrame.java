package PresentationLayer.UI.Stock;

import ServiceLayer.Stock.StockService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class CashierFrame extends JFrame implements ActionListener {

    JButton addItemButton;
    JTextField itemIdT;
    String purchaseId;
    JButton startButton;
    JButton endButton;
    JLabel itemIdL;
    StockService ss;

    CashierFrame(StockService ss){
        this.ss = ss;
        JLabel titleL = new JLabel("Cashier");
        titleL.setHorizontalAlignment(JLabel.CENTER);
        titleL.setVerticalAlignment(JLabel.TOP);
        titleL.setFont(new Font("Gisha", Font.BOLD, 20));
        titleL.setBounds(100,0,200,80);

        itemIdL = new JLabel("Enter item id");
        itemIdL.setHorizontalAlignment(JLabel.LEFT);
        itemIdL.setVerticalAlignment(JLabel.TOP);
        itemIdL.setFont(new Font("Gisha", Font.BOLD, 16));
        itemIdL.setBounds(100,0,200,80);
        itemIdL.setVisible(false);

        itemIdT = new JTextField();
        itemIdT.setFont(new Font("Gisha", Font.ITALIC, 16));
        itemIdT.setVisible(false);
        //itemIdT.setBounds(100,0,200,80);
        itemIdT.setHorizontalAlignment(JTextField.LEFT);
        itemIdT.setPreferredSize(new Dimension(100,40));

        addItemButton = new JButton("Add item");
        addItemButton.setFocusable(false);
        addItemButton.addActionListener(this);
        addItemButton.setVisible(false);

        startButton = new JButton("Start Purchase");
        startButton.setFocusable(false);
        startButton.addActionListener(this);

        endButton = new JButton("End purchase");
        endButton.setFocusable(false);
        endButton.addActionListener(this);
        endButton.setVisible(false);

        JPanel titleP = new JPanel();
        //titleP.setBounds(0,0, 420, 50);
        //titleP.setLayout(null);
        //titleP.setOpaque(false);
        titleP.setBackground(Color.PINK);
        titleP.add(titleL);

        JPanel bodyP = new JPanel();
        //bodyP.setBounds(0,50, 420, 370);
        //bodyP.setLayout(null);
        //bodyP.setOpaque(false);
        bodyP.setBackground(Color.gray);
        bodyP.add(itemIdL);
        bodyP.add(itemIdT);

        JPanel endP = new JPanel();
        endP.setOpaque(false);
        endP.add(startButton);
        endP.add(addItemButton);
        endP.add(endButton);

        JFrame frame = new JFrame();
        frame.setTitle("Cashier");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setResizable(false);
        frame.setSize(300,300);
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
        if(e.getSource() == startButton){
            try {
                purchaseId = ss.addPurchase();
                addItemButton.setVisible(true);
                itemIdT.setVisible(true);
                itemIdL.setVisible(true);
                startButton.setVisible(false);
            }
            catch (Exception ex){
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if(e.getSource() == addItemButton){
            try{
                Map< String, Boolean> result = ss.purchaseItem(purchaseId, itemIdT.getText());
                endButton.setVisible(true);
                if(result.get("isDamaged")){
                    JOptionPane.showMessageDialog(null, "Pay attention: the product " + itemIdT.getText().split("-")[0]+ " is damaged", "Pay Attention", JOptionPane.INFORMATION_MESSAGE);
                }
                if(result.get("shortage")){
                    JOptionPane.showMessageDialog(null, "Pay attention: the product " + itemIdT.getText().split("-")[0]+ " is low in stock", "Pay Attention", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            catch (Exception ex){
                JOptionPane.showMessageDialog(null, ex.getMessage() + " Try again!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if(e.getSource() == endButton){
            Map<String,Double> products;
            double total;
            while(true){
                try{
                    products = ss.finishPurchase(purchaseId);
                    total = ss.getPurchaseTotalPrice(purchaseId);
                    break;
                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage() + " Try again!", "Error", JOptionPane.ERROR_MESSAGE);

                }
            }
            String receipt = "";
            receipt = receipt + "Thank you for choosing us for your purchase! \n";
            receipt = receipt +"Here is your bill:\n\n";
            int itemsCounter = 1;
            for (String name : products.keySet()) {
                receipt = receipt + itemsCounter + ". " + name + " - " + products.get(name) + " NIS\n";
                itemsCounter++;
            }
            receipt = receipt + "\n";
            receipt = receipt + "Total to pay: " + total + " NIS\n\n";
            receipt = receipt +"Hope to see you soon :)";
            JOptionPane.showMessageDialog(this, receipt, "Receipt", JOptionPane.INFORMATION_MESSAGE);
            itemIdT.setVisible(false);
            itemIdL.setVisible(false);
            addItemButton.setVisible(false);
            endButton.setVisible(false);
            startButton.setVisible(true);
            this.dispose();
        }
    }
}
