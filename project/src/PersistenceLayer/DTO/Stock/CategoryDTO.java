package PersistenceLayer.DTO.Stock;

import PersistenceLayer.DAO.Stock.CategoryDAO;
import PersistenceLayer.DTO.AbstractDTO;

import java.sql.SQLException;
import java.util.List;

public class CategoryDTO extends AbstractDTO {
    
    private final String CATEGORY_ID;
    private String name;
    private int subCounter;

    public CategoryDTO(String categoryID, String name, int counter, CategoryDAO cdao) throws SQLException{
        super(cdao);
        this.CATEGORY_ID = categoryID;
        this.name = name;
        this.subCounter = counter;
    }

    public String getCATEGORY_ID() {
        return CATEGORY_ID;
    }

    public String getName(){
        return name;
    }

    public int getSubCounter() {
        return subCounter;
    }
}
