package PresentationLayer.UI.Stock;

import ServiceLayer.Stock.StockService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class ChooseCategoryMenuFrame extends JFrame implements ActionListener {

    private JComboBox<String> categoryComboBox;
    private JLabel categoryLabel;
    private List<JComboBox<String>> subCategoryComboBoxes;
    private JButton endButton;
    StockService ss;
    List<String> IDs = new ArrayList<>();
    boolean finished;
    JFrameCategories waitingFrame;
    Map<String, String> mapID;

    ChooseCategoryMenuFrame(StockService ss, JFrameCategories waitingFrame) {
        finished = false;
        this.waitingFrame = waitingFrame;
        this.ss = ss;
        JLabel titleL = new JLabel("Choose Category");
        titleL.setHorizontalAlignment(JLabel.CENTER);
        titleL.setVerticalAlignment(JLabel.TOP);
        titleL.setFont(new Font("Gisha", Font.BOLD, 20));
        titleL.setBounds(100, 0, 200, 80);

        categoryLabel = new JLabel("Choose the desired category:");
        categoryLabel.setBounds(50, 50, 200, 30);

        categoryComboBox = new JComboBox<>();
        categoryComboBox.setBounds(50, 100, 200, 30);

        JButton selectButton = new JButton("Select");
        selectButton.setBounds(120, 200, 80, 30);
        selectButton.addActionListener(this);

        endButton = new JButton("End Choice");
        endButton.addActionListener(this);
        endButton.setHorizontalAlignment(JButton.CENTER);

        JPanel titleP = new JPanel();
        titleP.setBackground(Color.PINK);
        titleP.add(titleL);

        JPanel bodyP = new JPanel();
        bodyP.setBackground(Color.GRAY);
        bodyP.setLayout(null);
        bodyP.add(categoryLabel);
        bodyP.add(categoryComboBox);
        bodyP.add(selectButton);

        JPanel endP = new JPanel();
        endP.setBackground(Color.GRAY);
        endP.add(endButton);

        // Populate the category combo box
        Map<String, String> categoryMap;
        try {
            categoryMap = ss.getMainCategories();
            mapID = new HashMap<>();
            Integer counter = 1;
            for (String id : categoryMap.keySet()) {
                categoryComboBox.addItem(counter + ". " + categoryMap.get(id));
                mapID.put(counter.toString(), id);
                counter++;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        subCategoryComboBoxes = new ArrayList<>();

        this.setTitle("Choose Category");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300, 350);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(139, 187, 161));
        this.setLayout(new BorderLayout());
        this.add(titleP, BorderLayout.NORTH);
        this.add(bodyP, BorderLayout.CENTER);
        this.add(endP, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Select")) {
            String selectedCounter = (String) categoryComboBox.getSelectedItem();
            if (selectedCounter != null) {
                String selectedCategory = mapID.get(selectedCounter.split(". ")[0]);
                IDs.add(selectedCategory);
                categoryComboBox.removeAllItems();
                Map<String, String> subCategoryMap;
                try {
                    // Get the sub-categories for the selected category
                    subCategoryMap = ss.getSub(new ArrayList<>(IDs));
                    mapID = new HashMap<>();
                    Integer counter = 1;
                    for (String id : subCategoryMap.keySet()) {
                        categoryComboBox.addItem(counter + ". " + subCategoryMap.get(id));
                        mapID.put(counter.toString(),id);
                        counter++;
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                this.revalidate();
                this.repaint();
            }
            else{
                this.dispose();
                waitingFrame.categoriesDone(IDs);
            }
        }
        if(e.getSource() == endButton){
            this.dispose();
            waitingFrame.categoriesDone(IDs);
        }
    }
}
