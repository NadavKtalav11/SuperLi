package PersistenceLayer.DAO;

import PersistenceLayer.DTO.AbstractDTO;

import java.nio.file.Paths;
import java.sql.*;


public abstract class AbstractMapper {

    protected String table_name ;
    abstract protected AbstractDTO convertReaderToObject(ResultSet reader);
    //Connection connect;


    public AbstractMapper(String tableName){
        this.table_name = tableName;
        //connect = connect();
    }
    protected Connection connect() {
        // SQLite connection string
        String userDirectory = Paths.get("").toAbsolutePath().toString();
        //String url = "jdbc:sqlite:DataBase/superli.db";
        String url = "jdbc:sqlite:DataBase/superli.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    protected void runDeleteQuery(String query) throws SQLException {
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("failed to delete sale from data base");
        }
    }
    public void remove(String columnName, int columnValue) throws Exception {
        String sql = "DELETE FROM " + table_name + " WHERE " + columnName + "= ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, columnValue);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void remove(String columnName, String columnValue) throws Exception {
        String sql = "DELETE FROM " +table_name+ " WHERE " +columnName+ "= ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, columnValue);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("fail to delete" + e.getMessage());
        }
    }


    public void remove(String columnName1, String columnName2, String columnName3, int columnValue1, int columnValue2, int columnValue3) throws Exception {
        String sql = "DELETE FROM " +table_name+ " WHERE " +columnName1+ "= ? AND "+columnName2 +"=? AND "+columnName3+"=?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, columnValue1);
            pstmt.setInt(2, columnValue2);
            pstmt.setInt(3, columnValue3);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void remove(String columnName1, String columnName2, int columnValue1, int columnValue2 ) throws Exception {
        String sql = "DELETE FROM " +table_name+ " WHERE " +columnName1+ "= ? AND "+columnName2 +"=?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, columnValue1);
            pstmt.setInt(2, columnValue2);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void remove(int columnValue, String columnName) {
        String sql = "DELETE FROM " + table_name + " WHERE " + columnName + "= ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, columnValue);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void remove(int columnValue1, int columnValue2, String columnName1, String columnName2) {
        String sql = "DELETE FROM " + table_name + " WHERE " + columnName1 + "= ? AND " + columnName2 + "=?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, columnValue1);
            pstmt.setInt(2, columnValue2);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void remove(int value1, String value2, String columnName1, String columnName2) {
        String sql = "DELETE FROM " + table_name + " WHERE " + columnName1 + "= ? AND " + columnName2 + "=?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, value1);
            pstmt.setString(2, value2);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(  String attributeName, String attributeValue, String columnName1,String columnName2,int value1, int value2) {
        String sql = "UPDATE "+table_name+" SET "+attributeName+" = ? "
                + " WHERE "+columnName1+" = ? AND "+columnName2+" = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the corresponding param
            pstmt.setString(1, attributeValue);
            pstmt.setInt(2, value1);
            pstmt.setInt(3, value2);
            // update
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(String attributeName, double attributeValue, String columnName1,String columnName2,int value1, int value2) {
        String sql = "UPDATE "+table_name+" SET "+attributeName+" = ? "
                + " WHERE "+columnName1+" = ? AND "+columnName2+" = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the corresponding param
            pstmt.setDouble(1, attributeValue);
            pstmt.setInt(2, value1);
            pstmt.setInt(3, value2);
            // update
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(  String attributeName, int attributeValue, String columnName1,String columnName2,int value1, int value2) {
        String sql = "UPDATE "+table_name+" SET "+attributeName+" = ? "
                + " WHERE "+columnName1+" = ? AND "+columnName2+" = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the corresponding param
            pstmt.setDouble(1, attributeValue);
            pstmt.setInt(2, value1);
            pstmt.setInt(3, value2);
            // update
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(  String attributeName, double attributeValue, String columnName1,int value1) {
        String sql = "UPDATE "+table_name+" SET "+attributeName+" = ? "
                + " WHERE "+columnName1+" = ? ";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the corresponding param
            pstmt.setDouble(1, attributeValue);
            pstmt.setInt(2, value1);
            // update
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(  String attributeName, String attributeValue, String columnName1,int value1) {
        String sql = "UPDATE "+table_name+" SET "+attributeName+" = ? "
                + " WHERE "+columnName1+" = ? ";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the corresponding param
            pstmt.setString(1, attributeValue);
            pstmt.setInt(2, value1);
            // update
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(  String attributeName, double attributeValue, String columnName1,String value1) {
        String sql = "UPDATE "+table_name+" SET "+attributeName+" = ? "
                + " WHERE "+columnName1+" = ? ";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the corresponding param
            pstmt.setDouble(1, attributeValue);
            pstmt.setString(2, value1);
            // update
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(String attributeName, int attributeValue, String columnName1,String value1) {
        String sql = "UPDATE "+table_name+" SET "+attributeName+" = ? "
                + " WHERE "+columnName1+" = ? ";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the corresponding param
            pstmt.setInt(1, attributeValue);
            pstmt.setString(2, value1);
            // update
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(  String attributeName, String attributeValue, String columnName1,String value1) {
        String sql = "UPDATE "+table_name+" SET "+attributeName+" = ? "
                + " WHERE "+columnName1+" = ? ";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the corresponding param
            pstmt.setString(1, attributeValue);
            pstmt.setString(2, value1);
            // update
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void update(  String attributeName1, String attributeName2, String attributeName3, String attributeValue1, int attributeValue2,String attributeValue3, String columnName1,int value1) {
        String sql = "UPDATE "+table_name+" SET "+attributeName1+" = ? ," + attributeName2+" = ?, " + attributeName3+" = ?, "
                + " WHERE "+columnName1+" = ? ";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the corresponding param
            pstmt.setString(1, attributeValue1);
            pstmt.setInt(2, attributeValue2);
            pstmt.setString(3, attributeValue3);
            pstmt.setInt(4, value1);
            // update
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void update(  String attributeName1, String attributeName2, int attributeValue1, double attributeValue2, String columnName1, String columnName2, int value1, int value3 ) {
        String sql = "UPDATE "+table_name+" SET "+attributeName1+" = ? ," + attributeName2+" = ? " +
                " WHERE "+columnName1+" = ? AND "+columnName2 +" = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the corresponding param
            pstmt.setInt(1, attributeValue1);
            pstmt.setDouble(2, attributeValue2);
            pstmt.setInt(3, value1);
            pstmt.setInt(4, value3);
            // update
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void update(  String attributeName1, String attributeName3, String attributeValue1,String attributeValue3, String columnName1,int value1) {
        String sql = "UPDATE "+table_name+" SET "+attributeName1+" = ? ,"  + attributeName3+" = ? "
                + " WHERE "+columnName1+" = ? ";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the corresponding param
            pstmt.setString(1, attributeValue1);
            pstmt.setString(2, attributeValue3);
            pstmt.setInt(3, value1);
            // update
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    protected boolean checkIfNotEmpty(String query) throws SQLException {
        try (Connection conn = this.connect();
             Statement statement = conn.createStatement()){
            ResultSet res = statement.executeQuery(query);
            return res.next();
        } catch (SQLException e) {
            throw new SQLException("fail to connect with database");
        }
    }

    public void update(String attributeName, double attributeValue, String columnName1, String columnName2, int value1, String value2) {
        String sql = "UPDATE " + table_name + " SET " + attributeName + " = ? "
                + " WHERE " + columnName1 + " = ? AND " + columnName2 + " = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the corresponding param
            pstmt.setDouble(1, attributeValue);
            pstmt.setInt(2, value1);
            pstmt.setString(3, value2);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update2Values(String attributeName1, int attributeValue1,String attributeName2, String attributeValue2 , String columnName1, int value1) {
        String sql = "UPDATE " + table_name + " SET " + attributeName1 + " = ? ,"+
                " "+ attributeName2 + " = ? "
                + " WHERE " + columnName1 + " = ? ";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the corresponding param
            pstmt.setInt(1, attributeValue1);
            pstmt.setString(2, attributeValue2);
            pstmt.setInt(3, value1);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void remove(int columnValue1,String columnValue2,int columnValue3, String columnName1, String columnName2, String columnName3) {
        String sql = "DELETE FROM " + table_name + " WHERE " + columnName1 + "= ? AND " + columnName2 + "=? AND " + columnName3 + "=?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, columnValue1);
            pstmt.setString(2, columnValue2);
            pstmt.setInt(3, columnValue3);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeAll(){
        String sql = "DELETE FROM " + table_name + "" ;
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
