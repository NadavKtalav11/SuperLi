package Tests.Stock;

import BusinessLayer.Stock.BranchController;
import PersistenceLayer.DAO.Stock.BranchDAO;
import PersistenceLayer.DAO.Stock.ControllerDAO;
import PersistenceLayer.DAO.Stock.ItemDAO;
import PersistenceLayer.DAO.Stock.ProductDAO;
import PersistenceLayer.DTO.Stock.BranchDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

class BranchControllerTest {
    BranchController branchController ;
    BranchDAO bdao = new BranchDAO();
    ControllerDAO cdao = new ControllerDAO();

    @BeforeEach
    void setUp() {
        bdao.removeAll();
        cdao.removeAll();
        branchController = new BranchController();
    }

    @Test
    void addBranch(){
        try {
            BranchDTO branchDTO = bdao.getBranch(1);
            assertNull(branchDTO);
            assertEquals(0, branchController.getBranches().size());
            branchController.addBranch("Branch_1", "Ashdod Hertzel blv");
            branchDTO = bdao.getBranch(1);
            assertEquals(1, branchDTO.getBranchID());
            assertEquals(1, branchController.getBranches().size());
        }
        catch (Exception ex){
            ex.printStackTrace();
            fail();
        }

    }
}