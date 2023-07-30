package PersistenceLayer.DTO.Stock;

import PersistenceLayer.DAO.Stock.BranchDAO;
import PersistenceLayer.DTO.AbstractDTO;

public class BranchDTO extends AbstractDTO {

    private final int BRANCH_ID;
    private String name;
    private String address;

    public BranchDTO(int branchID, String name, String city, BranchDAO bdao) {
        super(bdao);
        this.BRANCH_ID = branchID;
        this.address = city;
        this.name = name;
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

}
