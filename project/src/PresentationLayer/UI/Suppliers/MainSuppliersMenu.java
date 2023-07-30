package PresentationLayer.UI.Suppliers;

import PresentationLayer.CLI.Suppliers.MainMenu;
import PresentationLayer.UI.Suppliers.View.MenuView;

import javax.swing.*;
import java.awt.*;

public class MainSuppliersMenu {
    public MainSuppliersMenu() {
        JFrame jFrame = new JFrame();
        jFrame.setLayout(null);
        jFrame.setSize(800,665);
        JLabel jLabel =new JLabel("welcome to the suppliers system",SwingConstants.CENTER);
        jLabel.setForeground(Color.BLUE);
        jLabel.setBounds(0,0,jFrame.getWidth(),100);
        //jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setFont(new Font("Arial", Font.BOLD, 30));
        jFrame.setVisible(true);
        jFrame.add(jLabel);
        jLabel.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setTitle("welcome to the suppliers system");
        MenuView menuView = new MenuView();
        menuView.panel1.setSize(800 ,530);
        menuView.panel1.setBounds(jFrame.getX() ,jFrame.getY()+100 , jFrame.getWidth(),jFrame.getHeight()-100);
        menuView.panel1.setLocation(0, 100);
        jFrame.add(menuView.panel1);
        jFrame.setVisible(true);
        jFrame.setResizable(false);


    }
}




