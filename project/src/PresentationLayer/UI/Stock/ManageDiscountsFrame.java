package PresentationLayer.UI.Stock;

import PresentationLayer.UI.InputTools;
import ServiceLayer.Stock.StockService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ManageDiscountsFrame extends JFrameCategories implements ActionListener {

    JOptionPane alertP;
    StockService ss;
    boolean isNewDiscount;
    int discountType; //1=Category, 2=Product, 3=Damaged
    String discountedObjectID;
    String discountID;
    String damageType;
    List<String> categoryChain;
    Map<Integer, String> damageTypesList;
    Map<Integer, String> discountsMapper;

    JPanel selectDiscountTypeP;
    JPanel productDiscountP;
    JPanel damagedDiscountP;
    JPanel selectDiscountP;
    JPanel updateDiscountP;

    JButton newDiscount;
    JButton updateOrDeleteDiscount;

    JLabel selectDiscountTypeL;
    JComboBox discountTypes;
    JButton selectType;

    JLabel productIDL;
    JTextField productIDText;
    JButton submitID;

    JLabel selectDamageTypeL;
    JComboBox damageTypes;
    JButton selectDamageType;

    JLabel selectDiscountL;
    JComboBox discountsList;
    JButton selectDiscount;
    JButton updateDiscount;
    JButton deleteDiscount;

    JLabel attToUpdateL;
    JComboBox discountAttributes;
    JLabel attTextL;
    JTextField updatedAttText;
    JButton updateDiscountAtt;


    ManageDiscountsFrame() {}

    ManageDiscountsFrame(StockService ss){
        this.ss = ss;

        JLabel titleL = new JLabel("Manage Discounts");
        titleL.setHorizontalAlignment(JLabel.CENTER);
        titleL.setVerticalAlignment(JLabel.TOP);
        titleL.setFont(new Font("Gisha", Font.BOLD, 20));
        titleL.setBounds(100, 0, 200, 80);

        ////////////////////////////////////////////////////////


        //*****************************************************//

        newDiscount = new JButton("Create a new discount");
        newDiscount.setFocusable(false);
        newDiscount.addActionListener(this);
        newDiscount.setVisible(true);

        updateOrDeleteDiscount = new JButton("Update / Delete an existing discount");
        updateOrDeleteDiscount.setFocusable(false);
        updateOrDeleteDiscount.addActionListener(this);
        updateOrDeleteDiscount.setVisible(true);


        selectDiscountTypeL = new JLabel("Select discount type: ");
        selectDiscountTypeL.setFont(new Font("Gisha", Font.BOLD, 14));
        selectDiscountTypeL.setForeground(Color.white);

        String[] discountTypesToDisplay = {"Category Discount", "Product Discount", "Damaged Items Discount"};
        discountTypes = new JComboBox(discountTypesToDisplay);
        discountTypes.setFocusable(false);
        discountTypes.addActionListener(this);

        selectType = new JButton("Select");
        selectType.setFocusable(false);
        selectType.addActionListener(this);
        selectType.setVisible(false);



        productIDL = new JLabel("Enter ProductID:");
        productIDL.setFont(new Font("Gisha", Font.BOLD, 14));
        productIDL.setForeground(Color.white);

        productIDText = new JTextField(12);
        productIDText.setPreferredSize(new Dimension(0, 25));
        productIDText.addActionListener(this);

        submitID = new JButton("Submit");
        submitID.setFocusable(false);
        submitID.addActionListener(this);



        selectDamageTypeL = new JLabel("Select damage type:");
        selectDamageTypeL.setFont(new Font("Gisha", Font.BOLD, 14));
        selectDamageTypeL.setForeground(Color.white);
        selectDamageTypeL.setVisible(false);

        String[] damageTypesToDisplay = {"Temp"};
        damageTypes = new JComboBox(damageTypesToDisplay);
        damageTypes.setFocusable(false);
        damageTypes.addActionListener(this);
        damageTypes.setVisible(false);

        selectDamageType = new JButton("Select");
        selectDamageType.setFocusable(false);
        selectDamageType.addActionListener(this);
        selectDamageType.setVisible(false);



        selectDiscountL = new JLabel("Select discount: ");
        selectDiscountL.setFont(new Font("Gisha", Font.BOLD, 14));
        selectDiscountL.setForeground(Color.white);

        String[] discountsToDisplay = {"DiscountA", "DiscountB", "DiscountC"};
        discountsList = new JComboBox(discountsToDisplay);
        discountsList.setFocusable(false);
        discountsList.addActionListener(this);

        selectDiscount = new JButton("Select");
        selectDiscount.setFocusable(false);
        selectDiscount.addActionListener(this);
        selectDiscount.setVisible(false);

        updateDiscount = new JButton("Update the discount");
        updateDiscount.setFocusable(false);
        updateDiscount.addActionListener(this);
        updateDiscount.setVisible(false);

        deleteDiscount = new JButton("Delete the discount");
        deleteDiscount.setFocusable(false);
        deleteDiscount.addActionListener(this);
        deleteDiscount.setVisible(false);



        attToUpdateL = new JLabel("Select the attribute you would like to update: ");
        attToUpdateL.setFont(new Font("Gisha", Font.BOLD, 14));
        attToUpdateL.setForeground(Color.white);

        String[] AttsToDisplay = {"Discount start date", "Discount end date", "Discount in Percent / NIS ?", "discount rate"};
        discountAttributes = new JComboBox(AttsToDisplay);
        discountAttributes.setFocusable(false);
        discountAttributes.addActionListener(this);

        attTextL = new JLabel("Enter the updated data: ");
        attTextL.setFont(new Font("Gisha", Font.BOLD, 14));
        attTextL.setForeground(Color.white);
        attTextL.setVisible(false);

        updatedAttText = new JTextField(12);
        updatedAttText.setPreferredSize(new Dimension(0, 25));
        updatedAttText.addActionListener(this);
        updatedAttText.setVisible(false);

        updateDiscountAtt = new JButton("Update");
        updateDiscountAtt.setFocusable(false);
        updateDiscountAtt.addActionListener(this);
        updateDiscountAtt.setVisible(false);

        /////////////////////////////////////////////////////////////////


        //*****************************************************//
        selectDiscountTypeP = new JPanel(new FlowLayout(FlowLayout.CENTER));
        selectDiscountTypeP.setBackground(Color.gray);
        selectDiscountTypeP.add(selectDiscountTypeL);
        selectDiscountTypeP.add(discountTypes);
        selectDiscountTypeP.add(selectType);
        selectDiscountTypeP.setVisible(false);

        productDiscountP = new JPanel(new FlowLayout(FlowLayout.CENTER));
        productDiscountP.setBackground(Color.gray);
        productDiscountP.add(productIDL);
        productDiscountP.add(productIDText);
        productDiscountP.add(submitID);
        productDiscountP.setVisible(false);

        damagedDiscountP = new JPanel(new FlowLayout(FlowLayout.CENTER));
        damagedDiscountP.setBackground(Color.gray);
        damagedDiscountP.add(productDiscountP);
        damagedDiscountP.add(selectDamageTypeL);
        damagedDiscountP.add(damageTypes);
        damagedDiscountP.add(selectDamageType);
        damagedDiscountP.setVisible(false);

        selectDiscountP = new JPanel(new FlowLayout(FlowLayout.CENTER));
        selectDiscountP.setBackground(Color.gray);
        selectDiscountP.add(selectDiscountL);
        selectDiscountP.add(discountsList);
        selectDiscountP.add(selectDiscount);
        selectDiscountP.add(updateDiscount);
        selectDiscountP.add(deleteDiscount);
        selectDiscountP.setVisible(false);

        updateDiscountP = new JPanel(new FlowLayout(FlowLayout.CENTER));
        updateDiscountP.setBackground(Color.gray);
        updateDiscountP.add(attToUpdateL);
        updateDiscountP.add(discountAttributes);
        updateDiscountP.add(attTextL);
        updateDiscountP.add(updatedAttText);
        updateDiscountP.add(updateDiscountAtt);
        updateDiscountP.setVisible(false);

        JPanel titleP = new JPanel();
        titleP.setBackground(Color.PINK);
        titleP.add(titleL);

        JPanel bodyP = new JPanel();
        bodyP.setLayout(new FlowLayout(FlowLayout.CENTER));
        bodyP.setBackground(Color.gray);
        bodyP.add(newDiscount);
        bodyP.add(updateOrDeleteDiscount);
        bodyP.add(selectDiscountTypeP);
        bodyP.add(productDiscountP);
        bodyP.add(damagedDiscountP);
        bodyP.add(selectDiscountP);
        bodyP.add(updateDiscountP);

        alertP = new JOptionPane();

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
        if (e.getSource() == newDiscount) {
            isNewDiscount = true;
            updateOrDeleteDiscount.setVisible(false);
            newDiscount.setEnabled(false);
            selectDiscountTypeP.setVisible(true);
        }
        if (e.getSource() == updateOrDeleteDiscount) {
            isNewDiscount = false;
            newDiscount.setVisible(false);
            updateOrDeleteDiscount.setEnabled(false);
            selectDiscountTypeP.setVisible(true);
        }

        if (e.getSource() == discountTypes) {
            selectType.setVisible(true);

        }
        if (e.getSource() == selectType) {
            if (discountTypes.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Choose discount type");
            } else {
                selectDiscountTypeP.setVisible(false);
                if (discountTypes.getSelectedItem().equals("Category Discount")) {
                    discountType = 1;
                    this.setVisible(false);
                    ChooseCategoryMenuFrame c = new ChooseCategoryMenuFrame(ss, this);
                }
                if (discountTypes.getSelectedItem().equals("Product Discount")) {
                    discountType = 2;
                    productDiscountP.setVisible(true);
                }
                if (discountTypes.getSelectedItem().equals("Damaged Items Discount")) {
                    discountType = 3;
                    damagedDiscountP.setVisible(true);
                    productDiscountP.setVisible(true);
                }
            }
        }
        if (e.getSource() == submitID) {
            if (productIDText.getText().length() == 0) {
                JOptionPane.showMessageDialog(this, "Enter product ID");
            } else {
               if(InputTools.checkProductExists(this, ss, productIDText.getText())) {
                   if (discountType == 3) {
                       discountedObjectID = productIDText.getText();
                       updateDamageTypes();
                       selectDamageTypeL.setVisible(true);
                       damageTypes.setVisible(true);
                   }
                   else {
                       if (isNewDiscount) {
                           this.dispose();
                           new AddDiscountFrame(ss, discountType, null, productIDText.getText(), "NONE");
                       } else {
                           discountedObjectID = productIDText.getText();
                           productDiscountP.setVisible(false);
                           createDiscountsList();
                           selectDiscountP.setVisible(true);
                       }
                   }
                }
               else {
                   JOptionPane.showMessageDialog(this, "Product ID doesn't exist, try again");
               }
            }

            //TODO - ERRs
        }

        if (e.getSource() == damageTypes) {
            selectDamageType.setVisible(true);
        }
        if (e.getSource() == selectDamageType) {
            damageType = damageTypesList.get(damageTypes.getSelectedIndex()+1);
            if (isNewDiscount) {
                this.dispose();
                new AddDiscountFrame(ss, discountType, null, discountedObjectID, damageType);
            } else {
                productDiscountP.setVisible(false);
                damagedDiscountP.setVisible(false);
                createDiscountsList();
                selectDiscountP.setVisible(true);
            }
        }
        if (e.getSource() == discountsList) {
            selectDiscount.setVisible(true);
        }
        if (e.getSource() == selectDiscount) {
            discountID = discountsMapper.get(discountsList.getSelectedIndex());
            discountsList.setEnabled(false);
            selectDiscount.setEnabled(false);
            updateDiscount.setVisible(true);
            deleteDiscount.setVisible(true);
        }
        if (e.getSource() == updateDiscount) {
            selectDiscountP.setVisible(false);
            updateDiscountP.setVisible(true);
        }
        if (e.getSource() == deleteDiscount) {
            boolean isSuccess = deleteSelectedDiscount();
            //TODO - "success / fail" -> return to menu
            this.dispose();
        }
        if (e.getSource() == discountAttributes) {
            attTextL.setVisible(true);
            updatedAttText.setVisible(true);
            updateDiscountAtt.setVisible(true);
        }
        if (e.getSource() == updateDiscountAtt) {
            boolean isSuccess = false;
            if (discountAttributes.getSelectedItem().equals("Discount start date")) {
                isSuccess = updateDiscountStartDate(InputTools.insureDateInput(this, updatedAttText.getText()));
            }
            if (discountAttributes.getSelectedItem().equals("Discount end date")) {
                isSuccess = updateDiscountEndDate(InputTools.insureDateInput(this, updatedAttText.getText()));
            }
            if (discountAttributes.getSelectedItem().equals("Discount in Percent / NIS ?")) {
                isSuccess = updateDiscountMeasurement(updatedAttText.getText());
            }
            if (discountAttributes.getSelectedItem().equals("Discount rate")) {
                isSuccess = updateDiscountRate(InputTools.insureDoubleInput(this, updatedAttText.getText()));
            }
            if (isSuccess) {
                //TODO - "success / fail" -> return to menu
                this.dispose();
            }

        }
    }




    public String[] getDamageTypes() {
        damageTypesList = ss.getDamages();
        String[] listToDisplay = new String[damageTypesList.size()];
        for (int i = 0; i < damageTypesList.size(); i++) {
            listToDisplay[i] = damageTypesList.get(i+1);
        }
        return listToDisplay;
    }

    public void updateDamageTypes() {
        damageTypes.setModel(new javax.swing.DefaultComboBoxModel<>(getDamageTypes()));
    }

    public void createDiscountsList() {
        Map<String, String> discountsListMap = null;
        discountsMapper = new HashMap<>();
        try {
            if (discountType == 1) {
                discountsListMap = ss.getCategoryDiscouts(categoryChain);
            }
            if (discountType == 2) {
                discountsListMap = ss.getProductGeneralDiscouts(discountedObjectID);
            }
            if (discountType == 3) {
                discountsListMap = ss.getProductDamagedDiscounts(discountedObjectID, damageType);
            }
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
        }
        if ((discountsListMap == null) || (discountsListMap.size() == 0)) {
            JOptionPane.showMessageDialog(this, "No discounts to show!");
        }
        else {
            updateDiscountsList(discountsListMap);
        }
    }

    public void updateDiscountsList(Map<String, String> discountsListMap) {
        String[] listToDisplay = new String[discountsListMap.size()];
        int counter = 0;
        for (String id : discountsListMap.keySet()) {
            listToDisplay[counter] = discountsListMap.get(id);
            discountsMapper.put(counter, id);
            counter++;
        }
        discountsList.setModel(new javax.swing.DefaultComboBoxModel<>(listToDisplay));
    }


    private boolean updateDiscountStartDate(Date startDate) {
        if (startDate != null) {
            try {
                if (discountType == 1) {
                    ss.updateCategoryDiscountStartDate(categoryChain, discountID, startDate);
                }
                else if (discountType == 2) {
                    ss.updateProductDiscountStartDate(discountedObjectID, "NONE", discountID, startDate);
                }
                else if (discountType == 3) {
                    ss.updateProductDiscountStartDate(discountedObjectID, damageType, discountID, startDate);
                }
            }
            catch (Exception ex){
                JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean updateDiscountEndDate(Date endDate) {
        if (endDate != null) {
            try {
                if (discountType == 1) {
                    ss.updateCategoryDiscountEndDate(categoryChain, discountID, endDate);
                }
                else if (discountType == 2) {
                    ss.updateProductDiscountEndDate(discountedObjectID, "NONE", discountID, endDate);
                }
                else if (discountType == 3) {
                    ss.updateProductDiscountEndDate(discountedObjectID, damageType, discountID, endDate);
                }
            }
            catch (Exception ex){
                JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean updateDiscountMeasurement(String percentOrNIS) {
        percentOrNIS = percentOrNIS.toUpperCase();
        if (!(percentOrNIS.equals("PERCENT") | percentOrNIS.equals("NIS"))) {
            JOptionPane.showMessageDialog(this, "Please enter discount measurement in the format 'Percent'/'NIS'");
            return false;
        }
        else {
            boolean isPercent = percentOrNIS.equals("PERCENT");
            try {
                if (discountType == 1) {
                    ss.updateCategoryDiscountIsPercent(categoryChain, discountID, isPercent);
                }
                else if (discountType == 2) {
                    ss.updateProductDiscountIsPercent(discountedObjectID, "NONE", discountID, isPercent);
                }
                else if (discountType == 3) {
                    ss.updateProductDiscountIsPercent(discountedObjectID, damageType, discountID, isPercent);
                }
            }
            catch (Exception ex){
                JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
                return false;
            }
            return true;
        }
    }

    private boolean updateDiscountRate(Double discountRate) {
        if (discountRate != null) {
            try {
                if (discountType == 1) {
                    ss.updateCategoryDiscountValue(categoryChain, discountID, discountRate);
                } else if (discountType == 2) {
                    ss.updateProductDiscountValue(discountedObjectID, "NONE", discountID, discountRate);
                } else if (discountType == 3) {
                    ss.updateProductDiscountValue(discountedObjectID, damageType, discountID, discountRate);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean deleteSelectedDiscount() {
        try {
            if (discountType == 1) {
                ss.deleteCategoryDiscount(categoryChain, discountID);
            } else if (discountType == 2) {
                ss.deleteProductDiscount(discountedObjectID, discountID, "NONE");
            } else if (discountType == 3) {
                ss.deleteProductDiscount(discountedObjectID, discountID, damageType);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void categoriesDone(List<String> categories) {
        this.setVisible(true);
        this.categoryChain = categories;
        if (isNewDiscount) {
            this.dispose();
            new AddDiscountFrame(ss, discountType, categories, "", "");
        } else {
            createDiscountsList();
            selectDiscountP.setVisible(true);
        }
    }
}

