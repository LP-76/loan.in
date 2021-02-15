package odu.edu.loadin.webapi;

import odu.edu.loadin.common.BoxSize;
import odu.edu.loadin.common.User;
import odu.edu.loadin.common.UserLoginRequest;
import odu.edu.loadin.common.UserService;
import odu.edu.loadin.helpers.StatementHelper;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserServiceImpl implements UserService {


    @Override
    public User getUser(int id) {

        //query the database for a user but do not return the password information


        //we get a connection here

        try(Connection conn = DatabaseConnectionProvider.getLoadInSqlConnection()){ //this is called a try with resources and with java 1.8
            //this will auto-close the connection
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM USER where ID = ?");
            statement.setInt(1, id);

            //this is more of a transparent method.  person who is performing the query can decide how it gets mapped back to
            //individual objects
            User results = StatementHelper.getResults(statement, (ResultSet rs) -> {
                return mapStandardUserResult(rs);
            }).stream().findFirst().orElse(null);
            return results;
        }
        catch (SQLException ex){
            //TODO: exception logging
            System.out.println(ex);
        }
        return null;
    }

    @Override
    public Response login(UserLoginRequest userLoginRequest) {

        try(Connection conn = DatabaseConnectionProvider.getLoadInSqlConnection()){ //this is called a try with resources and with java 1.8
            //this will auto-close the connection
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM USER where EMAIL = ?");
            statement.setString(1, userLoginRequest.getEmail());

            //this is more of a transparent method.  person who is performing the query can decide how it gets mapped back to
            //individual objects
            User results = StatementHelper.getResults(statement, (ResultSet rs) -> {
               User u =  mapStandardUserResult(rs);
               u.setPassword(rs.getString("PASSWORD"));
               return u;
            }).stream().findFirst().orElse(null);

            //confirm that the password is correct

            //TODO: implement hashing and salting here!

            if(results != null && results.getPassword() != null && results.getPassword().equals(userLoginRequest.getPassword())){
                //we have an authenticated user
                results.setPassword(null);  //clear password
                return Response.ok(results).build();

            }else{
                return Response.status(Response.Status.FORBIDDEN).build();
            }


        }
        catch (SQLException ex){
            //TODO: exception logging
            System.out.println(ex);
        }
        return Response.status(Response.Status.NOT_FOUND).build();




    }

    private User mapStandardUserResult(ResultSet rs) throws SQLException {
        User r = new User();
        r.setId(rs.getInt("ID"));
        r.setFirstName(rs.getString("FIRST_NAME"));
        r.setLastName(rs.getString("LAST_NAME"));
        r.setCreatedAt(rs.getDate("CREATED_AT"));
        r.setUpdatedAt(rs.getDate("UPDATED_AT"));
        r.setEmail(rs.getString("EMAIL"));
        r.setPhoneNumber(rs.getString("PHONE_NUMBER"));
        return r;
    }


}
