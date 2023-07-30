package BusinessLayer.Stock;
//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import BusinessLayer.Branch;
import PersistenceLayer.DAO.Stock.*;
import PersistenceLayer.DTO.Stock.BranchDTO;

public class BranchController {
    private Map<Integer, Branch> branchList;
    private Integer branchCounter;
    private BranchDAO branchDAO = new BranchDAO();

    public BranchController() {
        this.branchList = new HashMap<>();
        try {
            this.branchCounter = branchDAO.maxIDBranch() + 1;
        }
        catch (Exception ex){}
    }

    
    /**
     * 
     * @return a Map of all the existing branches details: <branchID, name>
     */
    public Map<Integer, String> getBranches() throws Exception{
        Map<Integer, String> ans = new HashMap<>();
        try{
            List<BranchDTO> branchList = branchDAO.getAll();
            for (BranchDTO bDTO : branchList) {
                ans.put(bDTO.getBranchID(), bDTO.getName());
                if(bDTO.getBranchID() >= branchCounter)
                    branchCounter = bDTO.getBranchID() + 1;
            }
            return ans;
        }
        catch(Exception ex){
            throw new Exception(ex.getMessage());
        }
    }


    /**
     * 
     * @return the next branchID
     */
    private int generateBranchID(){
        int id = branchCounter;
        branchCounter++;
        return id;
    }


    /**
     * Creates a new branch and returns its new StockController
     * @param name
     * @param address
     * @return the branche's StockCoontroller
     */
    public StockController addBranch(String name, String address) throws Exception{
        int id = generateBranchID();
        try{
            branchDAO.addBranch(id, name, address);
            Branch newBranch = new Branch(id, name, address);
            branchList.put(id, newBranch);
            return newBranch.getStockController();
        }
        catch(Exception ex){
            throw new Exception(ex.getMessage());
        }
        
    }


    /**
     * 
     * @param id
     * @return the StockController of the branch of the given ID
     */
    public StockController getStockControllerByID(int id){
        if(!branchList.containsKey(id)) {
            try {
                BranchDTO bdto = branchDAO.getBranch(id);
                branchList.put(id, new Branch(bdto));
            }
            catch(Exception ex){
                throw new IllegalArgumentException("The given branch id doesn't exist.");
            }
        }
        return branchList.get(id).getStockController();
    }


    /**
     * 
     * @return a supply report for the suppliers model
     */
    public Map<Integer, Map<String, Integer>> supplyReportForSuppliers() {
        Map<Integer, Map<String, Integer>> ans = new HashMap<>();
        for (Branch branch: branchList.values()) {
            ans.put(branch.getBranchID(), branch.supplyReportForSuppliers());
        }
        return ans;
    }


    /**
     * Creates 2 new branches and loads example data defined in StockController class to each
     */
    public void setUp() throws Exception{
        StockController sc1 = addBranch("Branch 1", "Tel Aviv");
        StockController sc2 = addBranch("Branch 2", "Ashdod");
    }

    public void removeAllData() throws Exception{
        try {
            ProductDAO productDAO = new ProductDAO();
            CategoryDAO categoryDAO = new CategoryDAO(productDAO);
            ControllerDAO controllerDAO = new ControllerDAO();
            DiscountInStoreDAO discountInStoreDAO = new DiscountInStoreDAO();
            ItemDAO itemDAO = new ItemDAO();
            PurchaseDAO purchaseDAO = new PurchaseDAO();
            ReportDAO reportDAO = new ReportDAO();

            branchDAO.removeAll();
            productDAO.removeAll();
            categoryDAO.removeAll();
            controllerDAO.removeAll();
            discountInStoreDAO.removeAll();
            discountInStoreDAO.removeAllFromSubTables();
            itemDAO.removeAll();
            purchaseDAO.removeAll();
            purchaseDAO.removeAllFromSubTables();
            reportDAO.removeAll();
        }
        catch(Exception ex){
            throw new Exception(ex.getMessage());
        }
    }
}