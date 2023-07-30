package PresentationLayer.UI.Stock;

import ServiceLayer.Stock.StockService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChooseOrAddBranchFrame extends JFrame implements ActionListener {

    JButton stockButton;
    JButton itemButton;
    JButton cashierButton;
    JButton discountButton;
    JButton orderButton;
    StockService ss;
    //JcomboBox comboBox;

    ChooseOrAddBranchFrame(StockService ss){
        this.ss = ss;
        JLabel titleL = new JLabel("Branch menu");
        titleL.setHorizontalAlignment(JLabel.CENTER);
        titleL.setVerticalAlignment(JLabel.TOP);
        titleL.setFont(new Font("Gisha", Font.BOLD, 20));
        titleL.setBounds(100,0,200,80);

        String[] branches = {"to", "add"};
        //comboBox = new JComboBox(branches);
        stockButton = new JButton("Receive Stock");
        stockButton.setFocusable(false);
        stockButton.addActionListener(this);

        itemButton = new JButton("Update Item Data");
        itemButton.setFocusable(false);
        itemButton.addActionListener(this);

        cashierButton = new JButton("Cashier");
        cashierButton.setFocusable(false);
        cashierButton.addActionListener(this);

        discountButton = new JButton("Manage Discounts");
        discountButton.setFocusable(false);
        discountButton.addActionListener(this);

        orderButton = new JButton("Manage Orders");
        orderButton.setFocusable(false);
        orderButton.addActionListener(this);

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
        bodyP.add(stockButton);
        bodyP.add(itemButton);
        bodyP.add(cashierButton);
        bodyP.add(discountButton);
        bodyP.add(orderButton);


        JFrame frame = new JFrame();
        frame.setTitle("Stock System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setResizable(false);
        frame.setSize(300,300);
        frame.setVisible(true);
        frame.getContentPane().setBackground(new Color(139, 187, 161));
        frame.setLayout(new BorderLayout());
        frame.add(titleP, BorderLayout.NORTH);
        frame.add(bodyP, BorderLayout.CENTER);
        //frame.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == stockButton){
            //this.dispose();
            this.setVisible(false);
            new ReceiveSupplyFrame(ss, this);
        }
        if(e.getSource() == itemButton){

        }
        if(e.getSource() == cashierButton){

        }
        if(e.getSource() == discountButton){

        }
        if(e.getSource() == orderButton){

        }
    }
}
