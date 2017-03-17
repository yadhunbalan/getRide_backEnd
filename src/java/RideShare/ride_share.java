/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RideShare;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.ArrayUtils;

/**
 * REST Web Service
 *
 * @author yadhubalan
 */
@Path("rideshare")
public class ride_share {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public ride_share() {
    }

    /**
     * Retrieves representation of an instance of RideShare.GenericResource
     * @return an instance of java.lang.String
     */
    @Path("login/{ID}&{password}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String login( @PathParam("ID") int S_id, @PathParam("password") String password ) throws ClassNotFoundException,SQLException {
        //TODO return proper representation object
        Connection myCon = null;
        
        Class.forName("oracle.jdbc.OracleDriver");
        myCon=DriverManager.getConnection("jdbc:derby://localhost:1527/ride_share","yadhu","yadhu");
        
        Statement st=myCon.createStatement();
        ResultSet rs=st.executeQuery("select STUDENT_ID, PASSWORD from USERS");
         JSONObject allusers= new JSONObject();
      
            
        JSONArray loginarray=new JSONArray();
        //users.accumulate("ID",S_id);
        //users.accumulate("Password", password);
        while(rs.next()){
            //ALL DATA WILL STORE on DR JAVA OBJECT       
                JSONObject users=new JSONObject();
                int id = rs.getInt("STUDENT_ID");
                users.accumulate("id", id);
                String pWord=rs.getString("PASSWORD");
                users.accumulate("password", pWord);
                loginarray.add(users);
                
        }  
        
        allusers.accumulate("UserLogin", loginarray);
        String userLogin=""+allusers;
        String str=null;
        JSONObject loginStatus=new JSONObject();
        loginStatus.accumulate("Status", "OK");
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");
        String strDate = sdf.format(cal.getTime());
        loginStatus.accumulate("TimeStamp", strDate);
        for(int i=0;i<loginarray.size();i++){
            JSONObject user=new JSONObject();
            JSONObject getuser=loginarray.getJSONObject(i);
            if(S_id==Integer.parseInt(getuser.getString("id")) && password.equals(getuser.getString("password"))){
                
                loginStatus.accumulate("Login", "success");
                str=""+user;
            }
            
        }
        
        
           return ""+loginStatus;
        
    }
    @Path("{destination}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String serchResults(@PathParam("destination") String destination) throws ClassNotFoundException,SQLException{
       Connection myCon = null;
        
        Class.forName("oracle.jdbc.OracleDriver");
         myCon=DriverManager.getConnection("jdbc:derby://localhost:1527/ride_share","yadhu","yadhu");
        
        Statement st=myCon.createStatement();
        ResultSet rs=st.executeQuery("select U.STUDENT_ID,R.RIDE_ID,U.FIRST_NAME, U.LAST_NAME, R.PRICE, R.SEATS  from USERS U join RIDES R on U.STUDENT_ID=R.STUDENT_ID where R.DESTINATION like '%"+destination+"%'");
         JSONObject allrides= new JSONObject();
       allrides.accumulate("Status", "OK");
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");
            String strDate = sdf.format(cal.getTime());
            allrides.accumulate("TimeStamp", strDate);
            JSONArray Results=new JSONArray();
            while(rs.next()){
                JSONObject drives=new JSONObject();
                int s_id=rs.getInt("STUDENT_ID");
                drives.accumulate("Student_id", s_id);
                int r_id=rs.getInt("RIDE_ID");
                drives.accumulate("Ride_id",r_id);
                String fname=rs.getString("FIRST_NAME");
                drives.accumulate("First_name", fname);
                String lname=rs.getString("LAST_NAME");
                drives.accumulate("Last_name", lname);
                int price=rs.getInt("PRICE");
                drives.accumulate("Price", price);
                int seats=rs.getInt("SEATS");
                drives.accumulate("Seats",seats);
                Results.add(drives);
                
        }
            allrides.accumulate("AllRidesAvailable", Results);
            return ""+allrides;
            
        
    }
    @Path("account/{studentID}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String UserProfile(@PathParam ("studentID") int sID) throws ClassNotFoundException,SQLException{
        Connection myCon = null;
        Class.forName("oracle.jdbc.OracleDriver");
        myCon=DriverManager.getConnection("jdbc:derby://localhost:1527/ride_share","yadhu","yadhu");
        Statement st=myCon.createStatement();
        ResultSet rs=st.executeQuery("select * from USERS where STUDENT_ID="+sID );
        JSONObject user= new JSONObject();
        user.accumulate("Status", "OK");
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");
            String strDate = sdf.format(cal.getTime());
            user.accumulate("TimeStamp", strDate);
            JSONArray Results=new JSONArray();
            while(rs.next()){
                JSONObject userdetails=new JSONObject();
                int id=rs.getInt("STUDENT_ID");
                userdetails.accumulate("Student_id", id);
                String fname=rs.getString("FIRST_NAME");
                userdetails.accumulate("First_name", fname);
                String lname=rs.getString("LAST_NAME");
                userdetails.accumulate("Last_name", lname);
                String email=rs.getString("EMAIL");
                userdetails.accumulate("Email", email);
                String phone=rs.getString("MOBILE");
                userdetails.accumulate("Mobile", phone);
                String password=rs.getString("PASSWORD");
                userdetails.accumulate("Password", password);
                Results.add(userdetails);
            }
            user.accumulate("UserProfile", Results);
            return ""+user;
    }
    @Path("bookseat/{student_id}&{ride_id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void seatBooked(@PathParam ("student_id") int S_id, @PathParam ("ride_id") int r_id) throws ClassNotFoundException,SQLException{
        Connection myCon = null;
        Class.forName("oracle.jdbc.OracleDriver");
         myCon=DriverManager.getConnection("jdbc:derby://localhost:1527/ride_share","yadhu","yadhu");
        Statement st=myCon.createStatement();
        st.executeUpdate("update RIDES set SEATS=SEATS-1 where RIDE_ID="+r_id);
        //st.executeUpdate("INSERT INTO PASSENGERS VALUES (BOOKED_ID_SEQUENCE.NEXTVAL"+r_id+","+S_id+")");
        st.executeUpdate("INSERT INTO PASSENGERS (STUDENT_ID,RIDE_ID) VALUES ("+S_id+","+r_id+")");
        
        
    }
    @Path("addride/{student_id}&{vehicle}&{seats}&{destination}&{price}&{description}&{datetime}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void addRide(@PathParam ("student_id") int S_id, @PathParam ("vehicle") String vehicle, @PathParam("seats") int seats, @PathParam("destination") String destination, @PathParam("price") int price, @PathParam("description") String description,@PathParam("datetime") String datetime) throws ClassNotFoundException,SQLException{
        Connection myCon = null;
        Class.forName("oracle.jdbc.OracleDriver");
         myCon=DriverManager.getConnection("jdbc:derby://localhost:1527/ride_share","yadhu","yadhu");
        Statement st=myCon.createStatement();
        
        st.executeUpdate("INSERT INTO RIDES (STUDENT_ID,VEHICLE,SEATS,DESTINATION,PRICE,DESCRIPTION,DATETIME) VALUES ("+S_id+",'"+vehicle+"',"+seats+",'"+destination+"',"+price+",'"+description+","+datetime+"')");
        
    }
    @Path("cancelreservation/{student_id}&{ride_id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void cancelReservation(@PathParam ("student_id") int S_id, @PathParam ("ride_id") int r_id) throws ClassNotFoundException,SQLException{
        Connection myCon = null;
        Class.forName("oracle.jdbc.OracleDriver");
         myCon=DriverManager.getConnection("jdbc:derby://localhost:1527/ride_share","yadhu","yadhu");
        Statement st=myCon.createStatement();
        st.executeUpdate("update RIDES set SEATS=SEATS+1 where RIDE_ID="+r_id);
        st.executeUpdate("delete from passengers where ride_id="+r_id+" and student_id="+S_id);
        
    }
    @Path("driverdetails/{s_id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String driverDetails(@PathParam ("s_id") int s_id) throws ClassNotFoundException,SQLException{
        Connection myCon = null;
        Class.forName("oracle.jdbc.OracleDriver");
         myCon=DriverManager.getConnection("jdbc:derby://localhost:1527/ride_share","yadhu","yadhu");
        Statement st=myCon.createStatement();
        ResultSet rs=st.executeQuery("select U.STUDENT_ID, U.FIRST_NAME, U.LAST_NAME, R.RIDE_ID, R.VEHICLE, R.SEATS, R.DESTINATION, R.PRICE, R.DESCRIPTION from USERS U join RIDES R on U.STUDENT_ID=R.STUDENT_ID where U.STUDENT_ID="+s_id);
        JSONObject driverDetails=new JSONObject();
        JSONArray details=new JSONArray();
        while(rs.next()){
            JSONObject data=new JSONObject();
            
                int id=rs.getInt("STUDENT_ID");
                data.accumulate("Student_id", id);
                String fname=rs.getString("FIRST_NAME");
                data.accumulate("First_name", fname);
                String lname=rs.getString("LAST_NAME");
                data.accumulate("Last_name", lname); 
                int rid=rs.getInt("RIDE_ID");
                data.accumulate("Ride_id", rid);
                String vehicle=rs.getString("VEHICLE");
                data.accumulate("Vehicle", vehicle);
                int seats=rs.getInt("SEATS");
                data.accumulate("Seats", seats);
                String dest=rs.getString("DESTINATION");
                data.accumulate("Destination", dest);
                int price=rs.getInt("PRICE");
                data.accumulate("Price", price);
                String desc=rs.getString("DESCRIPTION");
                data.accumulate("Description", desc);
                details.add(data);
        }
        driverDetails.accumulate("Driver_Details", details);
        return ""+driverDetails;
    
    }
    @Path("reserveddetails/{r_id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String whoReserved(@PathParam ("r_id") int r_id) throws ClassNotFoundException,SQLException{
        Connection myCon = null;
        Class.forName("oracle.jdbc.OracleDriver");
         myCon=DriverManager.getConnection("jdbc:derby://localhost:1527/ride_share","yadhu","yadhu");
        Statement st=myCon.createStatement();
        ResultSet rs=st.executeQuery("select P.STUDENT_ID, U.FIRST_NAME, U.LAST_NAME from USERS U join PASSENGERS P on U.STUDENT_ID=P.STUDENT_ID join RIDES R on R.RIDE_ID=P.RIDE_ID where R.RIDE_ID="+r_id);
        JSONObject Passengers=new JSONObject();
        JSONArray passenger=new JSONArray();
        while(rs.next()){
            JSONObject data=new JSONObject();
            
                int id=rs.getInt("STUDENT_ID");
                data.accumulate("Student_id", id);
                String fname=rs.getString("FIRST_NAME");
                data.accumulate("First_name", fname);
                String lname=rs.getString("LAST_NAME");
                data.accumulate("Last_name", lname);
                passenger.add(data);
    
        }
        Passengers.accumulate("Reserved_by", passenger);
        return ""+Passengers;
    }
    @Path("cancelride/{ride_id}")
    @GET
    public void cancelRide(@PathParam ("ride_id") int r_id) throws ClassNotFoundException,SQLException{
        Connection myCon = null;
        Class.forName("oracle.jdbc.OracleDriver");
         myCon=DriverManager.getConnection("jdbc:derby://localhost:1527/ride_share","yadhu","yadhu");
        Statement st=myCon.createStatement();
        st.executeUpdate("delete from ride where ride_id="+r_id);
        st.executeUpdate("delete from passengers where ride_id="+r_id);
    }
    @Path("passwordChange/{student_id}&{new_password}")
    @GET
    
    public void passwordChange(@PathParam ("student_id") int s_id, @PathParam ("new_password") String newPassword) throws ClassNotFoundException,SQLException{
        Connection myCon = null;
        Class.forName("oracle.jdbc.OracleDriver");
         myCon=DriverManager.getConnection("jdbc:derby://localhost:1527/ride_share","yadhu","yadhu");
        Statement st=myCon.createStatement();
        st.executeUpdate("update users set password= '"+newPassword+"' where student_id="+s_id);
    }
    @Path("register/{student_id}&{f_name}&{l_name}&{email}&{mobile}&{password}")
    @GET
    
    public void registerUser(@PathParam ("student_id") int S_id, @PathParam ("f_name") String f_name, @PathParam ("l_name") String l_name, @PathParam ("email") String email, @PathParam ("mobile") String mobile, @PathParam ("password") String password) throws ClassNotFoundException,SQLException{
        Connection myCon = null;
        Class.forName("oracle.jdbc.OracleDriver");
         myCon=DriverManager.getConnection("jdbc:derby://localhost:1527/ride_share","yadhu","yadhu");
        Statement st=myCon.createStatement();
        st.executeUpdate("INSERT INTO USERS (STUDENT_ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILE, PASSWORD) VALUES ("+S_id+",'"+ f_name+"', '"+l_name+"', '"+email+"', '"+mobile+"','"+password+"')");
        
    }
    @Path("ridehistory/{student_id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String rideHistory(@PathParam("student_id") int s_id) throws ClassNotFoundException,SQLException{
        Connection myCon = null;
        Class.forName("oracle.jdbc.OracleDriver");
         myCon=DriverManager.getConnection("jdbc:derby://localhost:1527/ride_share","yadhu","yadhu");
        Statement st=myCon.createStatement();
        ResultSet rs=st.executeQuery("SELECT RIDE_ID, PRICE, DESTINATION FROM RIDES WHERE STUDENT_ID= "+s_id);
        JSONObject Rides=new JSONObject();
        JSONArray rides=new JSONArray();
        while(rs.next()){
            JSONObject data=new JSONObject();
            
                int id=rs.getInt("RIDE_ID");
                data.accumulate("Ride_id", id);
                int price=rs.getInt("PRICE");
                data.accumulate("Price", price);
                String destination=rs.getString("DESTINATION");
                data.accumulate("Destination", destination);
                rides.add(data);
    
        }
        Rides.accumulate("Ride_History", rides);
        return ""+Rides;
    }
    @Path("validateuser/{student_id}&{email}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String rpasswordChangefromLogin1(@PathParam("student_id") int s_id,@PathParam("email") String email) throws ClassNotFoundException,SQLException{
        Connection myCon = null;
        Class.forName("oracle.jdbc.OracleDriver");
         myCon=DriverManager.getConnection("jdbc:derby://localhost:1527/ride_share","yadhu","yadhu");
        Statement st=myCon.createStatement();
        ResultSet rs=st.executeQuery("SELECT EMAIL FROM USER WHERE STUDENT_ID= "+s_id);
        JSONObject Rides=new JSONObject();
        
        if(!rs.next()){
            Rides.accumulate("Status","success");
        }
        else{
            Rides.accumulate("Status","fail");
        }
        
        return ""+Rides;
    }

    /**
     * PUT method for updating or creating an instance of GenericResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
