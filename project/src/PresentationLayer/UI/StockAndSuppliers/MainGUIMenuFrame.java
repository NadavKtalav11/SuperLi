package PresentationLayer.UI.StockAndSuppliers;

import PresentationLayer.UI.Stock.StockGUI;
import PresentationLayer.UI.StockAndSuppliers.MainManagerFrame;
import PresentationLayer.UI.Suppliers.SuppliersGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUIMenuFrame extends JFrame implements ActionListener {

    JButton resetData;
    JButton loadCurrentData;
    int roleID;
    StockGUI stockMain;
    SuppliersGUI suppliersMain;

    public MainGUIMenuFrame(int roleID){
        this.roleID = roleID; //1=Manager, 2=StockKeeper, 3=SuppliersManager
        stockMain = new StockGUI();
        suppliersMain = new SuppliersGUI();


        JLabel titleL = new JLabel("Welcome to Super-Li System!");
        titleL.setHorizontalAlignment(JLabel.CENTER);
        titleL.setVerticalAlignment(JLabel.TOP);
        titleL.setFont(new Font("Gisha", Font.BOLD, 20));
        titleL.setBounds(100,0,200,80);

        resetData = new JButton("Reset DB and re-load the predefined data");
        resetData.setFocusable(false);
        resetData.addActionListener(this);

        loadCurrentData = new JButton("Load the data that is currently in the DB");
        loadCurrentData.setFocusable(false);
        loadCurrentData.addActionListener(this);


        JPanel titleP = new JPanel();
        titleP.setBackground(Color.PINK);
        titleP.add(titleL);

        JPanel bodyP = new JPanel();
        bodyP.setLayout(new GridLayout(2,1,10,2));
        bodyP.setBackground(Color.gray);
        bodyP.add(resetData);
        bodyP.add(loadCurrentData);

        this.setTitle("Stock System");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300,300);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(139, 187, 161));
        this.setLayout(new BorderLayout());
        this.add(titleP, BorderLayout.NORTH);
        this.add(bodyP, BorderLayout.CENTER);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == resetData){
            try {
                stockMain.removeAllData();
                suppliersMain.removeAllData();
                stockMain.setUp();
                suppliersMain.setUp();
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(this, exception.getMessage());
            }
            runMainByRole(true);
        }
        if(e.getSource() == loadCurrentData){
            runMainByRole(false);
        }
    }


    public void runMainByRole(boolean shouldLoad) {
        if (roleID == 1) {
            try {
                new MainManagerFrame(shouldLoad, stockMain, suppliersMain);
                this.dispose();
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(this, exception.getMessage());
            }
        }
        if (roleID == 2) {
            try {
                stockMain.startStock(shouldLoad);
                this.dispose();
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(this, exception.getMessage());
            }
        }
        if (roleID == 3) {
            try {
                suppliersMain.startSuppliers();
                this.dispose();
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(this, exception.getMessage());
            }
        }
    }
}

