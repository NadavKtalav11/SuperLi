package PresentationLayer.UI.StockAndSuppliers;

import PresentationLayer.UI.Stock.StockGUI;
import PresentationLayer.UI.Suppliers.SuppliersGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainManagerFrame extends JFrame implements ActionListener {

    JLabel chooseMenuL;
    JButton stock;
    JButton suppliers;
    JButton hR;
    JButton transports;

    boolean shouldLoad;
    StockGUI stockMain;
    SuppliersGUI suppliersMain;

    public MainManagerFrame(boolean shouldLoad, StockGUI stockMain ,SuppliersGUI suppliersMain){

        this.shouldLoad = shouldLoad;
        this.stockMain = stockMain;
        this.suppliersMain = suppliersMain;

        JLabel titleL = new JLabel("Manager Menu");
        titleL.setHorizontalAlignment(JLabel.CENTER);
        titleL.setVerticalAlignment(JLabel.TOP);
        titleL.setFont(new Font("Gisha", Font.BOLD, 20));
        titleL.setBounds(100, 0, 200, 80);

        chooseMenuL = new JLabel("Select Your Desired Action:");
        chooseMenuL.setFont(new Font("Gisha", Font.BOLD, 18));
        chooseMenuL.setHorizontalAlignment(JLabel.CENTER);
        chooseMenuL.setForeground(Color.white);
        chooseMenuL.setAlignmentX(CENTER_ALIGNMENT);

        stock = new JButton("Stock Management");
        stock.setFocusable(false);
        stock.addActionListener(this);
        stock.setAlignmentX(CENTER_ALIGNMENT);

        suppliers = new JButton("Suppliers Management");
        suppliers.setFocusable(false);
        suppliers.addActionListener(this);
        suppliers.setAlignmentX(CENTER_ALIGNMENT);

        hR = new JButton("HR Management");
        hR.setFocusable(false);
        hR.addActionListener(this);
        hR.setEnabled(false);
        hR.setAlignmentX(CENTER_ALIGNMENT);

        transports = new JButton("Transports Management");
        transports.setFocusable(false);
        transports.addActionListener(this);
        transports.setEnabled(false);
        transports.setAlignmentX(CENTER_ALIGNMENT);

        JPanel titleP = new JPanel();
        titleP.setBackground(Color.PINK);
        titleP.add(titleL);

        JPanel bodyP = new JPanel();
        bodyP.setLayout(new GridLayout(5,1, 5, 10));
        bodyP.setBackground(Color.gray);
        bodyP.add(chooseMenuL);
        bodyP.add(stock);
        bodyP.add(suppliers);
        bodyP.add(hR);
        bodyP.add(transports);


        this.setTitle("Manager System");
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
        if (e.getSource() == stock) {
            try {
                stockMain.startStock(shouldLoad);
//                this.dispose();
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(this, exception.getMessage());
            }
        }
        if (e.getSource() == suppliers) {
            try {
            suppliersMain.startSuppliers();
//                this.dispose();
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(this, exception.getMessage());
            }
        }
    }
}






