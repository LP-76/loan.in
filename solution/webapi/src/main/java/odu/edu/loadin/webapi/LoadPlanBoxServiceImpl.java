package odu.edu.loadin.webapi;

import odu.edu.loadin.common.LoadPlanBox;
import odu.edu.loadin.common.LoadPlanBoxService;

import java.sql.SQLException;
import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.ArrayList;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import odu.edu.loadin.common.*;
import odu.edu.loadin.helpers.*;

public class LoadPlanBoxServiceImpl implements LoadPlanBoxService
{
    @Override
    public ArrayList<LoadPlanBox> getLoadPlan(int userId) throws SQLException
    {
        System.out.println("--invoking getInventory");
        try (Connection conn = DatabaseConnectionProvider.getLoadInSqlConnection()) { //this is called a try with resources and with java 1.8
            //this will auto-close the connection
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM USER_INVENTORY_ITEM i\n" +
                    "    JOIN LOAD_PLAN_BOX LPB on i.ID = LPB.ID\n" +
                    "    WHERE USER_ID = ?");
            statement.setInt(1, userId);

            //this is more of a transparent method.  person who is performing the query can decide how it gets mapped back to
            //individual objects
            ArrayList<LoadPlanBox> results = StatementHelper.getResults(statement, (ResultSet rs) -> {
                LoadPlanBox s = new LoadPlanBox();
                s.setId(rs.getInt("ID"));
                s.setUserID(rs.getInt("USER_ID"));
                s.setBoxID(rs.getInt("BOX_ID"));
                s.setWidth(rs.getFloat("BOX_WIDTH"));
                s.setHeight(rs.getFloat("BOX_HEIGHT"));
                s.setLength(rs.getFloat("BOX_LENGTH"));
                s.setDescription(rs.getString("ITEM_DESCRIPTION"));
                s.setFragility(rs.getInt("FRAGILITY"));
                s.setWeight(rs.getDouble("WEIGHT"));
                s.setCreatedAt(rs.getDate("CREATED_AT"));
                s.setUpdatedAt(rs.getDate("UPDATED_AT"));

                //additional properties
                s.setxOffset(rs.getFloat("X_OFFSET"));
                s.setyOffset(rs.getFloat("Y_OFFSET"));
                s.setzOffset(rs.getFloat("Z_OFFSET"));

                s.setStepNumber(rs.getInt("BOX_STEP"));
                s.setLoadNumber(rs.getInt("LOAD_NUMBER"));


                return s;
            });
            return results;
        } catch (SQLException ex) {
            //TODO: exception logging
            System.out.println(ex);
        }

        return new ArrayList<LoadPlanBox>();


    }

    @Override
    public ArrayList<LoadPlanBox> addLoadPlan(int userId, ArrayList<LoadPlanBox> boxes) throws SQLException
    {

        System.out.println("----invoking addLoadPlan");

        try (Connection conn = DatabaseConnectionProvider.getLoadInSqlConnection()) {
//
            clearLoadPlan(conn, userId);
            saveLoadPlan(conn, boxes);


        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return boxes;
    }

    private void clearLoadPlan(Connection conn, int userId) throws SQLException {
        String query = "\n" +
                "DELETE LPB FROM\n" +
                "              USER_INVENTORY_ITEM i\n" +
                "    JOIN LOAD_PLAN_BOX LPB on i.ID = LPB.ID\n" +
                "    WHERE USER_ID = ?";
        PreparedStatement deleteStatement = conn.prepareStatement(query);
        deleteStatement.setInt(1, userId);
        deleteStatement.execute();
    }
    private void saveLoadPlan(Connection con, ArrayList<LoadPlanBox> boxes) throws SQLException {
        for(LoadPlanBox box: boxes){
            saveLoadPlanBox(con, box);
        }
    }
    private void saveLoadPlanBox(Connection conn, LoadPlanBox box) throws SQLException {
        String query = "INSERT INTO LOAD_PLAN_BOX(ID, X_OFFSET, Y_OFFSET, Z_OFFSET, BOX_STEP, LOAD_NUMBER)\n" +
                "VALUES (?, ?, ?, ?, ?, ?);";

        PreparedStatement insertStatement = conn.prepareStatement(query);
        insertStatement.setInt(1, box.getId());
        insertStatement.setFloat(2, box.getxOffset());
        insertStatement.setFloat(3, box.getyOffset());
        insertStatement.setFloat(4, box.getzOffset());
        insertStatement.setInt(5, box.getStepNumber());
        insertStatement.setInt(6, box.getLoadNumber());
        System.out.println(insertStatement);
        insertStatement.executeUpdate();
    }
}