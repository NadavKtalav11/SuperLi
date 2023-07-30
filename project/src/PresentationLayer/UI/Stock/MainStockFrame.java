package PresentationLayer.UI.Stock;

import ServiceLayer.Stock.StockService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainStockFrame extends JFrame implements ActionListener {

    StockService ss;
    JLabel chooseMenuL;
    JButton stockButton;
    JButton itemButton;
    JButton cashierButton;
    JButton discountButton;
    JButton orderButton;
    JButton reportButton;

    MainStockFrame(StockService ss){
        this.ss = ss;
        JLabel titleL = new JLabel("Stock System");
        titleL.setHorizontalAlignment(JLabel.CENTER);
        titleL.setVerticalAlignment(JLabel.TOP);
        titleL.setFont(new Font("Gisha", Font.BOLD, 20));
        titleL.setBounds(100,0,200,80);

        chooseMenuL = new JLabel("Select Your Desired Action:");
        chooseMenuL.setFont(new Font("Gisha", Font.BOLD, 16));
        chooseMenuL.setHorizontalAlignment(JLabel.CENTER);
        chooseMenuL.setForeground(Color.white);
        chooseMenuL.setAlignmentX(CENTER_ALIGNMENT);

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

        reportButton = new JButton("Print Reports");
        reportButton.setFocusable(false);
        reportButton.addActionListener(this);

        JPanel titleP = new JPanel();
        //titleP.setBounds(0,0, 420, 50);
        //titleP.setLayout(null);
        //titleP.setOpaque(false);
        titleP.setBackground(Color.PINK);
        titleP.add(titleL);

        JPanel bodyP = new JPanel(new GridLayout(7, 1, 0, 5));
        //bodyP.setBounds(0,50, 420, 370);
        //bodyP.setLayout(null);
        //bodyP.setOpaque(false);
        bodyP.setBackground(Color.gray);
        bodyP.add(chooseMenuL);
        bodyP.add(stockButton);
        bodyP.add(itemButton);
        bodyP.add(cashierButton);
        bodyP.add(discountButton);
        bodyP.add(orderButton);
        bodyP.add(reportButton);



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
            this.setVisible(false);
            new ReceiveSupplyFrame(ss, this);
            //ChooseCategoryMenuFrame.chooseCategoryMenu(ss);
        }
        if(e.getSource() == itemButton){
            this.setVisible(false);
            new UpdateDataFrame(ss);
        }
        if(e.getSource() == cashierButton){
            new CashierFrame(ss);
        }
        if(e.getSource() == discountButton){
            new ManageDiscountsFrame(ss);
        }
        if(e.getSource() == orderButton){
            new ManageOrdersFrame(ss);
        }
        if (e.getSource() == reportButton) {
            new ReportsMenuFrame(ss);
        }
    }
}
