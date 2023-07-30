package BusinessLayer;
import BusinessLayer.Suppliers.SuppliersController;
import BusinessLayer.Tools.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockToSupplier {
    private SuppliersController supController;

    public StockToSupplier() {
        this.supController = new SuppliersController();
    }

    public boolean sendDeficiencyOrderToSuppliers(int branchID, Map<Pair<String, String>, Integer> purchaseDeficiencyList) throws Exception{
        return supController.addShortageOrder(purchaseDeficiencyList, branchID);
        //return true;
    }

    //Integer = orderID
    //String = supply days (X, Y, Z...)
    public Map<Integer, String> getAutoOrdersList(int branchID) {
        Map<Integer, String> toReturn = supController.getAutoOrdersList(branchID);
        if ((toReturn != null) && (toReturn.size() > 0)) {
            for (int id : toReturn.keySet()) {
                toReturn.put(-id, toReturn.get(id));
                toReturn.remove(id);
            }
        }
        return toReturn;
        //return null;

    }

    public void deleteAutomaticOrder(int branchID, int orderID) {
        supController.deleteAutomaticOrder(branchID, -orderID);
    }

    public void addAutomaticOrder(int branchID, int orderID, List<Integer> supplyDays, Map<Pair<String, String>, Integer> productsToOrder) throws Exception {
        supController.addAutomaticOrderConst(branchID, -orderID, supplyDays, (HashMap)productsToOrder, true);
    }

    public boolean addExpressOrder(int branchID, Map<Pair<String, String>, Integer> productsToOrder)  throws Exception{
        return supController.addShortageOrder(productsToOrder, branchID);
        //return true;
    }

    public boolean updateOrderSupplyDays(int branchID, int orderID, List<Integer> newSupplyDays) {
        return supController.updateOrderSupplyDays(branchID, -orderID, newSupplyDays);
        //return true;
    }

    public Map<Pair<String, String>, Integer> getOrderProductsMap(int branchID, int orderID) {
        return supController.getOrderProductsMap(branchID, -orderID);
        //return null;
    }

    public boolean updateOrderProductsAndAmounts(int branchID, int orderID, Map<Pair<String, String>, Integer> productsAndAmountsMap) throws Exception{
        return supController.updateOrderProductsAndAmounts(branchID, -orderID, productsAndAmountsMap);
        //return true;
    }
}
