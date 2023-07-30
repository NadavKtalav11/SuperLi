package PresentationLayer.UI.Suppliers.View;

import BusinessLayer.Suppliers.PaymentOptions;
import PresentationLayer.UI.Suppliers.Controller.Controller;
import PresentationLayer.UI.Suppliers.Model.SuppliersMenuModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuView  {
    public JPanel panel1;
    public JButton addNewSupplierButton;
    public JButton removeSupplierButton;
    public JButton lookAtLastOrderButton;
    public JButton removeProductFromSupplierButton;
    //  public JButton loadSystemDataButton;
    public JButton addProductToSupplierButton;
    public JButton addDiscountToSupplierButton;
    public JButton addContactToSupplierButton;
    public JButton removeContactFromSupplierButton;
    public JButton removeDiscountFromSupplierButton;
    public JButton exitButton;
    public JButton editSupplierButton;
    public JButton addDiscountToProductButton;


    //private SuppliersMenuModel model;

    public MenuView() {

        Controller  controller= new Controller(this);
        panel1.setVisible(true );

        ;
        // model = new SuppliersMenuModel();
    }


}