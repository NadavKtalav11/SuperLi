package PresentationLayer.UI.Stock;

import PresentationLayer.UI.InputTools;
import ServiceLayer.Stock.StockService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportsMenuFrame extends JFrameCategories implements ActionListener {

    JButton stockButton;
    JButton defButton;
    JButton damButton;
    StockService ss;
    List<List<String>> chosenCategories;
    List<String> chosenDamages;
    int type;

    ReportsMenuFrame(StockService ss){
        this.ss = ss;
        JLabel titleL = new JLabel("Stock System");
        titleL.setHorizontalAlignment(JLabel.CENTER);
        titleL.setVerticalAlignment(JLabel.TOP);
        titleL.setFont(new Font("Gisha", Font.BOLD, 20));
        titleL.setBounds(100,0,200,80);

        stockButton = new JButton("Stock Report");
        stockButton.setFocusable(false);
        stockButton.addActionListener(this);

        defButton = new JButton("Deficiency Report");
        defButton.setFocusable(false);
        defButton.addActionListener(this);

        damButton = new JButton("Damaged Report");
        damButton.setFocusable(false);
        damButton.addActionListener(this);

        JPanel titleP = new JPanel();
        //titleP.setBounds(0,0, 420, 50);
        //titleP.setLayout(null);
        titleP.setOpaque(false);
        //titleP.setBackground(Color.PINK);
        titleP.add(titleL);

        JPanel bodyP = new JPanel();
        //bodyP.setBounds(0,50, 420, 370);
        //bodyP.setLayout(null);
        bodyP.setOpaque(false);
        //bodyP.setBackground(Color.gray);
        bodyP.add(stockButton);
        bodyP.add(defButton);
        bodyP.add(damButton);

        this.setTitle("Stock System");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setResizable(false);
        this.setSize(420,200);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(139, 187, 161));
        this.setLayout(new BorderLayout());
        this.add(titleP, BorderLayout.NORTH);
        this.add(bodyP, BorderLayout.CENTER);
        //this.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == stockButton){
            this.chosenCategories = new ArrayList<>();
            this.type = 1;
            this.setVisible(false);
            new ChooseCategoryMenuFrame(ss, this);
        }
        if(e.getSource() == defButton){
            this.chosenCategories = new ArrayList<>();
            this.type = 2;
            this.setVisible(false);
            new ChooseCategoryMenuFrame(ss, this);
        }
        if(e.getSource() == damButton){
            this.chosenCategories = new ArrayList<>();
            this.chosenDamages = new ArrayList<>();
            this.type = 3;
            this.setVisible(false);
            new ChooseCategoryMenuFrame(ss, this);
        }
    }

    @Override
    public void categoriesDone(List<String> categories) {
        this.setVisible(true);
        chosenCategories.add(categories);
        int ans = JOptionPane.showConfirmDialog(null, "Would you like to add categories to the report?", "To continue?", JOptionPane.YES_NO_OPTION);
        if(ans == 0){
            this.setVisible(false);
            new ChooseCategoryMenuFrame(ss, this);
        }
        else{
            try {
                if(type == 1) {
                    JOptionPane.showMessageDialog(null, ss.printStockReport(chosenCategories), "Stock Report", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                } if(type == 2) {
                    JOptionPane.showMessageDialog(null, ss.printDeficiencyReport(chosenCategories), "Deficiency Report", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                } if(type == 3){
                    Map<Integer, String> damages = ss.getDamages();
                    String out = "Choose damage type for report: \n";
                    for (Integer key: damages.keySet()) {
                        out = out + key + ": " + damages.get(key) + "\n";
                    }
                    Integer damages_choice = InputTools.insureIntInput(this,JOptionPane.showInputDialog(null, out, "Damages Choice"));
                    while(damages_choice == null || !damages.containsKey(damages_choice)){
                        damages_choice = InputTools.insureIntInput(this, JOptionPane.showInputDialog(null, "Invalid input given. Try again! \n" + out, "Damages Choice"));
                    }
                    chosenDamages.add(damages.get(damages_choice));
                    Integer toContinue = JOptionPane.showConfirmDialog(null, "Would you like to add another damage type?", "To continue?", JOptionPane.YES_NO_OPTION);
                    while (toContinue == 0){
                        damages_choice = InputTools.insureIntInput(this,JOptionPane.showInputDialog(null, out, "Damages Choice"));
                        while(damages_choice == null || !damages.containsKey(damages_choice)){
                            damages_choice = InputTools.insureIntInput(this, JOptionPane.showInputDialog(null, "Invalid input given. Try again! \n" + out, "Damages Choice"));
                        }
                        chosenDamages.add(damages.get(damages_choice));
                        toContinue = JOptionPane.showConfirmDialog(null, "Would you like to add another damage type?", "To continue?", JOptionPane.YES_NO_OPTION);
                    }
                    JOptionPane.showMessageDialog(null, ss.printDamagedReport(chosenCategories, chosenDamages), "Damaged Report", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                }
                } catch(Exception ex){
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);

            }
        }
    }
}
