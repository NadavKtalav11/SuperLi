package BusinessLayer;

import BusinessLayer.Stock.StockController;
import PersistenceLayer.DAO.Stock.ControllerDAO;
import PersistenceLayer.DTO.Stock.BranchDTO;

import java.sql.SQLException;
import java.util.Map;

public class Branch {

    private final int BRANCH_ID;
    private String name;
    private String address;
    private StockController stockController;
    private ControllerDAO controllerDAO = new ControllerDAO();

    public Branch(int branchID, String name, String address) throws SQLException{
        this.BRANCH_ID = branchID;
        this.address = address;
        this.name = name;
        controllerDAO.addController(1, 1, 1, 1, 1, BRANCH_ID);
        this.stockController = new StockController(branchID, controllerDAO);
    }

    public Branch(BranchDTO branchDTO) throws SQLException {
        this.BRANCH_ID = branchDTO.getBranchID();
        this.address = branchDTO.getAddress();
        this.name = branchDTO.getName();
        this.stockController = new StockController(branchDTO.getBranchID(), controllerDAO, controllerDAO.getController(branchDTO.getBranchID()));
    }

    public int getBranchID() {
        return BRANCH_ID;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public StockController getStockController(){
        return stockController;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setAddress(String newCity) {
        this.address = newCity;
    }

    /**
     * 
     * @return a supply report for the suppliers model
     */
    public Map<String, Integer> supplyReportForSuppliers() {
        return stockController.supplyReportForSuppliers();
    }
}