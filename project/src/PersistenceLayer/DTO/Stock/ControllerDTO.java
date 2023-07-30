package PersistenceLayer.DTO.Stock;

import PersistenceLayer.DAO.AbstractMapper;
import PersistenceLayer.DAO.Stock.ControllerDAO;
import PersistenceLayer.DTO.AbstractDTO;

public class ControllerDTO  extends AbstractDTO {

    private final int discountCounter;
    private final int mainCategoryCounter;
    private final int purchaseCounter;
    private final int reportCounter;
    private final int orderCounter;
    private final int branchID;

    public ControllerDTO(ControllerDAO dao, int discountCounter, int mainCategoryCounter, int purchaseCounter, int reportCounter, int orderCounter, int branchID) {
        super(dao);
        this.discountCounter = discountCounter;
        this.purchaseCounter = purchaseCounter;
        this.reportCounter = reportCounter;
        this.mainCategoryCounter = mainCategoryCounter;
        this.orderCounter = orderCounter;
        this.branchID = branchID;
    }

    public int getDiscountCounter() {
        return discountCounter;
    }

    public int getMainCategoryCounter() {
        return mainCategoryCounter;
    }

    public int getPurchaseCounter() {
        return purchaseCounter;
    }

    public int getReportCounter() {
        return reportCounter;
    }

    public int getBranchID() {
        return branchID;
    }

    public int getOrderCounter() {
        return orderCounter;
    }
}
