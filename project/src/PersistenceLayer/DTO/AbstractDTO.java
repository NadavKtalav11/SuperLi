package PersistenceLayer.DTO;

import PersistenceLayer.DAO.AbstractMapper;

public class AbstractDTO {

    protected AbstractMapper dao;
    public AbstractDTO( AbstractMapper dao){
        this.dao = dao;
    }

    public AbstractMapper getDao(){
        return dao;
    }
}