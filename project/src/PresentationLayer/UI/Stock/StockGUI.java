package PresentationLayer.UI.Stock;

import ServiceLayer.Stock.BranchService;

public class StockGUI {
    BranchService bs;

    public StockGUI () {
    }

    public static void removeAllData() {
        BranchService bs = new BranchService();
        while (true) {
            try {
                bs.removeAllData();
                break;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void setUp() {
        BranchService bs = new BranchService();
        while (true) {
            try {
                bs.setUp();
                break;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void startStock(boolean shouldLoad) {
        bs = new BranchService();
        new BranchMenuFrame(bs, shouldLoad);
    }
}
