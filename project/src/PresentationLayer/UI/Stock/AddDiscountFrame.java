package PresentationLayer.UI.Stock;

import PresentationLayer.UI.InputTools;
import ServiceLayer.Stock.StockService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

public class AddDiscountFrame extends JFrame implements ActionListener {

    StockService ss;
    int discountType; //1=Category, 2=Product, 3=Damaged
    List<String> categoryChain;
    String discountedObjectID;
    String damageType;

    JPanel discountAttributesP;
    JLabel startDateL;
    JLabel endDateL;
    JLabel discountMeasurementL;
    JLabel discountValueL;

    JTextField startDateText;
    JTextField endDateText;
    JComboBox discountMeasurementSelect;
    JTextField discountValueText;

    JButton submit;

    public AddDiscountFrame(StockService ss, int discountType, List<String> categoryChain, String discountedObjectID, String damageType) {
        this.ss = ss;
        this.discountType = discountType;
        this.categoryChain = categoryChain;
        this.discountedObjectID = discountedObjectID;
        this.damageType = damageType;

        JLabel titleL = new JLabel("Manage Discounts");
        titleL.setHorizontalAlignment(JLabel.CENTER);
        titleL.setVerticalAlignment(JLabel.TOP);
        titleL.setFont(new Font("Gisha", Font.BOLD, 20));
        titleL.setBounds(100, 0, 200, 80);

        startDateL = new JLabel("Discount start date (dd-mm-yyyy):");
        startDateL.setFont(new Font("Gisha", Font.BOLD, 14));
        startDateL.setForeground(Color.white);

        endDateL = new JLabel("Discount end date (dd-mm-yyyy):");
        endDateL.setFont(new Font("Gisha", Font.BOLD, 14));
        endDateL.setForeground(Color.white);

        discountMeasurementL = new JLabel("Discount rate measurements:");
        discountMeasurementL.setFont(new Font("Gisha", Font.BOLD, 14));
        discountMeasurementL.setForeground(Color.white);

        discountValueL = new JLabel("Discount value (rate):");
        discountValueL.setFont(new Font("Gisha", Font.BOLD, 14));
        discountValueL.setForeground(Color.white);


        startDateText = new JTextField(12);
        startDateText.setPreferredSize(new Dimension(0, 25));
        startDateText.addActionListener(this);

        endDateText = new JTextField(12);
        endDateText.setPreferredSize(new Dimension(0, 25));
        endDateText.addActionListener(this);

        String[] PercentOrNIS = {"Percent", "NIS"};
        discountMeasurementSelect = new JComboBox(PercentOrNIS);
        discountMeasurementSelect.setFocusable(false);
        discountMeasurementSelect.addActionListener(this);

        discountValueText = new JTextField(12);
        discountValueText.setPreferredSize(new Dimension(0, 25));
        discountValueText.addActionListener(this);

        submit = new JButton("Submit");
        submit.setFocusable(false);
        submit.addActionListener(this);


        discountAttributesP = new JPanel(new GridLayout(4,2,2,10));
        discountAttributesP.setBackground(Color.gray);
        discountAttributesP.add(startDateL);
        discountAttributesP.add(startDateText);
        discountAttributesP.add(endDateL);
        discountAttributesP.add(endDateText);
        discountAttributesP.add(discountMeasurementL);
        discountAttributesP.add(discountMeasurementSelect);
        discountAttributesP.add(discountValueL);
        discountAttributesP.add(discountValueText);
        discountAttributesP.setVisible(true);

        JPanel titleP = new JPanel();
        titleP.setBackground(Color.PINK);
        titleP.add(titleL);

        JPanel bodyP = new JPanel();
        bodyP.setLayout(new FlowLayout(FlowLayout.CENTER));
        bodyP.setBackground(Color.gray);
        bodyP.add(discountAttributesP);
        bodyP.add(submit);

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
        boolean isSuccess = false;
        if (e.getSource() == submit) {
            Date dateStartDate = InputTools.insureDateInput(this, startDateText.getText());
            Date dateEndDate = InputTools.insureDateInput(this, endDateText.getText());
            Double doubleDiscountValue = InputTools.insureDoubleInput(this, discountValueText.getText());
            if (discountMeasurementSelect.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Choose Percent / NIS");
            }
            else {
                boolean isPercent = discountMeasurementSelect.getSelectedItem().equals("Percent");
                if ((dateStartDate != null) && (dateEndDate != null) && (doubleDiscountValue != null)) {
                    isSuccess = addNewDiscount(dateStartDate, dateEndDate, isPercent, doubleDiscountValue);
                }
                else {
                    JOptionPane.showMessageDialog(this, "One of the fields is not valid, try again");
                    isSuccess = false;
                }
            }
        }
        if (isSuccess) {
            //TODO - "success / fail" -> return to menu
            this.dispose();
        }
    }

    private boolean addNewDiscount(Date dateStartDate, Date dateEndDate, boolean isPercent, Double doubleDiscountValue) {
        try {
            if (discountType == 1) {
                ss.addCategoryDiscount(categoryChain,dateStartDate, dateEndDate, doubleDiscountValue, isPercent);
            }
            else if (discountType == 2) {
                ss.addProductDiscount(discountedObjectID, dateStartDate, dateEndDate, doubleDiscountValue, isPercent);
            }
            else if (discountType == 3) {
                ss.addDamagedDiscount(discountedObjectID, damageType, dateStartDate, dateEndDate, doubleDiscountValue, isPercent);
            }
        }
        catch (Exception ex){
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
            return false;
        }
        return true;
    }

}
