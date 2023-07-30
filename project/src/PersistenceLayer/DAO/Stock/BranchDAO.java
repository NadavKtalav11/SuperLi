package PersistenceLayer.DAO.Stock;

import PersistenceLayer.DAO.AbstractMapper;
import PersistenceLayer.DTO.Stock.BranchDTO;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class BranchDAO extends AbstractMapper{
    
    private static final String BRANCH_ID_COLUMN = "BRANCH_ID";
    private static final String NAME_COLUMN = "name";
    private static final String ADDRESS_COLUMN = "address";


    public BranchDAO(){
        super("Branch");
    }

    public BranchDTO convertReaderToObject(ResultSet reader){
        BranchDTO result = null;
        try {
            result = new BranchDTO(reader.getInt(BRANCH_ID_COLUMN), reader.getString(NAME_COLUMN), reader.getString(ADDRESS_COLUMN), this);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public boolean checkIfBranchExists(int branchId) throws Exception {
        String sqlQuery = String.format("SELECT * FROM %s WHERE %s = '%s'",
        table_name, BRANCH_ID_COLUMN, branchId);
        return checkIfNotEmpty(sqlQuery);
    }

    public void addBranch(int branchId, String name, String city) throws Exception {
        String sqlQuery = String.format("INSERT INTO %s Values (?,?,?)", table_name);
        try(Connection conn = this.connect();
            PreparedStatement statement = conn.prepareStatement(sqlQuery)){
            statement.setInt(1, branchId);
            statement.setString(2, name);
            statement.setString(3, city);
            statement.executeUpdate();
        } catch (Exception e){
            throw new Exception("fail to insert new branch because: "+ e.getMessage());
        }
    }

    public BranchDTO getBranch(int branchID)throws Exception{
        BranchDTO objects;
        String sql = "SELECT * FROM " + table_name + " WHERE " + BRANCH_ID_COLUMN + "=" + branchID;
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            if (resultSet.next()) {
                objects = convertReaderToObject(resultSet);
                return objects;
            }
        } catch (SQLException e) {
            throw new Exception("fail to select branch from branches table: "+e.getMessage());
        }
        return null;
    }

    public List<BranchDTO> getAll() throws Exception {
        List<BranchDTO> results = new LinkedList<>();
        String sqlQuery = String.format("SELECT * FROM %s",table_name);
        try (Connection conn = this.connect();
             PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
            ResultSet rsData = statement.executeQuery();
            while (rsData.next())
                results.add(convertReaderToObject(rsData));
        }
        catch (SQLException e) {
            throw new Exception("fail to connect to branches table: "+e.getMessage());
        }
        return results;
    }

    public Integer maxIDBranch()throws Exception{
        Integer ans;
        String sql = "SELECT max( " + BRANCH_ID_COLUMN + ") as maxBranch FROM " + table_name ;
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            if (resultSet.next()) {
                ans = resultSet.getInt("maxBranch");
                return ans;
            }
        } catch (SQLException e) {
            throw new Exception("fail to select branch from branches table: "+e.getMessage());
        }
        return 0;
    }
}
