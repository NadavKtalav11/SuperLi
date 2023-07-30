package PresentationLayer.UI.Stock;

import ServiceLayer.Stock.StockService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class ChooseOrAddCategoryMenuFrame extends JFrame implements ActionListener {

    private JComboBox<String> categoryComboBox;
    private JLabel categoryLabel;
    private JButton addCatButton;
    private JButton doneButton;
    private List<JComboBox<String>> subCategoryComboBoxes;
    StockService ss;
    List<String> IDs = new ArrayList<>();
    boolean finished;
    JFrameCategories waitingFrame;
    Map<String, String> mapID;

    ChooseOrAddCategoryMenuFrame(StockService ss, JFrameCategories waitingFrame) {
        finished = false;
        this.waitingFrame = waitingFrame;
        this.ss = ss;
        JLabel titleL = new JLabel("Choose or Add Category");
        titleL.setHorizontalAlignment(JLabel.CENTER);
        titleL.setVerticalAlignment(JLabel.TOP);
        titleL.setFont(new Font("Gisha", Font.BOLD, 20));
        titleL.setBounds(100, 0, 200, 80);

        categoryLabel = new JLabel("Choose the desired category:");
        categoryLabel.setBounds(50, 50, 200, 30);

        categoryComboBox = new JComboBox<>();
        categoryComboBox.setBounds(50, 100, 200, 30);

        JButton selectButton = new JButton("Select");
        selectButton.setBounds(40, 200, 80, 30);
        selectButton.addActionListener(this);

        addCatButton = new JButton("Add Category");
        addCatButton.setBounds(140, 200, 120, 30);
        addCatButton.addActionListener(this);
        addCatButton.setVisible(true);

        doneButton = new JButton("End category adding");
        doneButton.setBounds(100, 250, 150, 30);
        doneButton.addActionListener(this);
        doneButton.setEnabled(false);
        doneButton.setVisible(true);

        JPanel titleP = new JPanel();
        titleP.setBackground(Color.PINK);
        titleP.add(titleL);

        JPanel bodyP = new JPanel();
        bodyP.setBackground(Color.GRAY);
        bodyP.setLayout(null);
        bodyP.add(categoryLabel);
        bodyP.add(categoryComboBox);
        bodyP.add(selectButton);
        bodyP.add(addCatButton);
        bodyP.add(doneButton);

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

        this.setTitle("Choose or Add Category");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 500);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(139, 187, 161));
        this.setLayout(new BorderLayout());
        this.add(titleP, BorderLayout.NORTH);
        this.add(bodyP, BorderLayout.CENTER);
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
                        mapID.put(counter.toString(), id);
                        counter++;
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                this.revalidate();
                this.repaint();
            } else {
                this.dispose();
                waitingFrame.categoriesDone(IDs);
            }
        }
        if (e.getSource() == addCatButton) {
            categoryComboBox.setEnabled(false);
            String name = JOptionPane.showInputDialog(null, "What is the name of the new category?");
            String category;
            try{
                category = ss.addCategory(new ArrayList<>(IDs), name);
                IDs.add(category);
                doneButton.setEnabled(true);
            } catch(Exception ex){
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getSource() == doneButton) {
            this.dispose();
            waitingFrame.categoriesDone(IDs);
        }
    }
}
