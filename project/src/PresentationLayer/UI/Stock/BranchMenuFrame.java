package PresentationLayer.UI.Stock;

import ServiceLayer.Stock.BranchService;
import ServiceLayer.Stock.StockService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class BranchMenuFrame extends JFrame implements ActionListener {


    JButton newBranch;
    JButton chooseBranch;
    JComboBox comboBox;
    Map<Integer, Integer> indexToId;
    boolean shouldLoad;
    int branchId;
    BranchService bs;
    JTextField nameText;
    JTextField addressText;
    JLabel insertNameL;
    JLabel insertAddressL;
    GridLayout newBranchLayout;
    JPanel newBranchFormP;
    JPanel alertP;


    BranchMenuFrame(BranchService bs, boolean shouldLoad){
        this.bs = bs;
        this.shouldLoad = shouldLoad;

        JLabel titleL = new JLabel("Branch menu");
        titleL.setHorizontalAlignment(JLabel.CENTER);
        titleL.setVerticalAlignment(JLabel.TOP);
        titleL.setFont(new Font("Gisha", Font.BOLD, 20));
        titleL.setBounds(100,0,200,80);

        Map<Integer,String> branches = new HashMap<>();
        try{
            branches = bs.getBranches();
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
        }


        String [] namesToDisplay = new String[1];
        indexToId = new HashMap<>();
        if ((branches != null) && (branches.size()>0)) {
            int i = 0;
            namesToDisplay = new String[branches.size() + 1];
            for (int id : branches.keySet()) {
                namesToDisplay[i] = branches.get(id);
                indexToId.put(i, id);
                i++;
            }
        }
        namesToDisplay [namesToDisplay.length - 1] = "Add a new branch";


        comboBox = new JComboBox(namesToDisplay);
        comboBox.setFocusable(false);
        comboBox.addActionListener(this);

        newBranch = new JButton("Create new branch");
        newBranch.setFocusable(false);
        newBranch.addActionListener(this);
        newBranch.setVisible(false);


        chooseBranch = new JButton("Choose selected branch");
        chooseBranch.setFocusable(false);
        chooseBranch.addActionListener(this);
        chooseBranch.setVisible(false);

        insertNameL = new JLabel("Branch name:");
        insertNameL.setFont(new Font("Gisha", Font.BOLD, 14));
        insertNameL.setForeground(Color.white);

        nameText = new JTextField();
        nameText.setPreferredSize(new Dimension(0, 25));
        nameText.addActionListener(this);

        insertAddressL = new JLabel("Branch Address:");
        insertAddressL.setFont(new Font("Gisha", Font.BOLD, 14));
        insertAddressL.setForeground(Color.white);

        addressText = new JTextField(12);
        addressText.setPreferredSize(new Dimension(0, 25));
        addressText.addActionListener(this);

        newBranchLayout = new GridLayout(2, 2, 5, 5);

        JPanel titleP = new JPanel();
        titleP.setBackground(Color.PINK);
        titleP.add(titleL);

        newBranchFormP = new JPanel();
        newBranchFormP.setLayout(newBranchLayout);
        newBranchFormP.setBackground(Color.gray);
        newBranchFormP.add(insertNameL);
        newBranchFormP.add(nameText);
        newBranchFormP.add(insertAddressL);
        newBranchFormP.add(addressText);
        newBranchFormP.setVisible(false);

        JPanel bodyP = new JPanel();
        bodyP.setLayout(new FlowLayout(FlowLayout.CENTER));
        bodyP.setBackground(Color.gray);
        bodyP.add(comboBox);
        bodyP.add(chooseBranch);
        bodyP.add(newBranchFormP);
        bodyP.add(newBranch);

        alertP = new JPanel();

        this.setTitle("Branch Menu");
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
        if(e.getSource() == comboBox){
            if (comboBox.getSelectedItem().equals("Add a new branch")) {
                chooseBranch.setVisible(false);
                newBranchFormP.setVisible(true);
                newBranch.setVisible(true);
            }
            else {
                newBranchFormP.setVisible(false);
                newBranch.setVisible(false);
                chooseBranch.setVisible(true);
                branchId = indexToId.get(comboBox.getSelectedIndex());
            }
        }
        if(e.getSource() == newBranch){
            if ((nameText.getText() == null) || (nameText.getText().length() == 0)) {
                    JOptionPane.showMessageDialog(this, "Enter branch name");
            }
            else {
                if (addressText.getText() == null) {
                    addressText.setText("");
                }
                try {
                    StockService ss  = bs.addBranch(nameText.getText(), addressText.getText());
                    this.dispose();
                    new MainStockFrame(ss);
                } catch(Exception ex){
                    JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
                }
            }
        }
        if(e.getSource() == chooseBranch){
            try {
                StockService ss = bs.selectBranch(shouldLoad, branchId);
                this.dispose();
                new MainStockFrame(ss);
            } catch(Exception ex){
                JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
            }
        }
    }
}
