package ServiceLayer.Stock;
import BusinessLayer.Stock.BranchController;
import BusinessLayer.Stock.StockController;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class BranchService {

    private BranchController bc;
    private int selectedBranch; // needed???

    public BranchService(){
        this.bc = new BranchController();
        this.selectedBranch = 0;
    }


    /**
     * Calls the BranchController's required methods for adding a new branch
     * @return
     */
    public StockService addBranch(String name, String address){
        //scanner.close();
        try{
            StockController sc = bc.addBranch(name, address);
            selectedBranch = sc.getBranchID();
            return new StockService(sc);
        }
        catch(Exception ex){
            System.out.println("An error accured: " + ex.getMessage());
            return null;
        }
    }


    /**
     * Calls the BranchController's required methods for displaying all existing branches
     * @return
     */
    public Map<Integer,String> getBranches(){
        Map<Integer,String> branches = new HashMap<>();
        try{
            branches = bc.getBranches();
            return branches;
        }
        catch(Exception ex){
            System.out.println("An error accured: " + ex.getMessage());
            return null;
        }
    }

    /**
     * Calls the BranchController's required methods for selecting a branch and returning its stockController
     * @return
     */
    public StockService selectBranch(boolean shouldLoad, int selectedBranch){
        this.selectedBranch = selectedBranch;
        if (shouldLoad) {
            loadBranch();
        }
        try{
            StockController sc = bc.getStockControllerByID(selectedBranch);
            return new StockService(sc);
        }
        catch(Exception ex){
            System.out.println("An error accured: " + ex.getMessage());
            return null;
        }
    }

    /**
     * Calls the BranchController's required methods for loading the example data
     */
    public void setUp() throws Exception{
        bc.setUp();
    }

    public void removeAllData() throws Exception{
        bc.removeAllData();
    }

    public void loadBranch() {
        StockController scForLoad = new StockController(selectedBranch);
        try {
            if (selectedBranch == 1) {
                scForLoad.setUpStockController1();
            } else if (selectedBranch == 2) {
                scForLoad.setUpStockController2();
            }
        }
        catch(Exception ex) {
            System.out.println("An error accured: " + ex.getMessage());
        }
    }

    public BranchController getBC(){
        return bc;
    }
}