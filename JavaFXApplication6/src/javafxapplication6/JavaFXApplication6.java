
package javafxapplication6;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.fxml.Initializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import java.util.UUID;
import javafx.scene.layout.Pane;
public class JavaFXApplication6 extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setResizable(true);
        stage.setTitle("Appartment Management");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

 class person{
    private String user_acess,name,uname,email,psw;
    String Duser = "root";
    String Dpass = "Raizen092103";
    String Durl = "jdbc:mysql://127.0.0.1:3306/cc105";
    String systempsw = "patApartment2121";
    String Client_key;
    boolean has_house;
    person( String user_acess,String name,String uname,String email,String psw,boolean has_house){
        this.user_acess = user_acess;
        this.name = name;
        this.uname = uname;
        this.email = email;
        this.psw = psw;
        this.has_house = has_house;
    } 
    public void savedata(){
        if(user_acess.equalsIgnoreCase("client")){
            try (Connection connection = DriverManager.getConnection(Durl, Duser, Dpass)) {
                
    
                String insertQuery = "INSERT INTO Client (Fname, Username, Email, Password,Req_key,has_house) VALUES (?, ?, ?, ?,?,?)";
    
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, uname);
                    preparedStatement.setString(3, email);
                    preparedStatement.setString(4, psw);
                    Client_key = UUID.randomUUID().toString().substring(0, 8);
                    preparedStatement.setString(5, Client_key);
                    preparedStatement.setBoolean(6,has_house);

                    preparedStatement.executeUpdate();
                    System.out.println("sucess");
                    
                    JOptionPane.showMessageDialog(null,"Save or Take a photo of your Recover-key!\n\n\n" + "                        " + Client_key);
                    
                 
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.err.println("SQL State: " + e.getSQLState());
                    System.err.println("Error Code: " + e.getErrorCode());
                    System.err.println("Message: " + e.getMessage());
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Failed to connect to the database!");
            }
        }else{
             String guestpsw = JOptionPane.showInputDialog("Enter System Key");
             if(guestpsw.equals(systempsw)){
                try (Connection connection = DriverManager.getConnection(Durl, Duser, Dpass)) {
                
    
                    String insertQuery = "INSERT INTO Admin(Fname, Username, Email, Password,System_key) VALUES (?,?,?,?,?)";
        
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                        preparedStatement.setString(1, name);
                        preparedStatement.setString(2, uname);
                        preparedStatement.setString(3, email);
                        preparedStatement.setString(4, psw);
                        preparedStatement.setString(5, systempsw);
                        
                     preparedStatement.executeUpdate();
                        System.out.println("sucess");
                     
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.err.println("SQL State: " + e.getSQLState());
                        System.err.println("Error Code: " + e.getErrorCode());
                        System.err.println("Message: " + e.getMessage());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.err.println("Failed to connect to the database!");
                }
             }else{
                JOptionPane.showMessageDialog(null, "Please input corret System Key");
             }
           
        }
    } 

   String getUname(){
        return uname;
    }
    public void setUname(String uname){
        this.uname = uname;
    }
   
}
 class User {
    private String username,Fname,email,password,rec_key,usertype,Add_ress,contact;
    boolean has_house;
    int Id;
    LocalDate applyDate;
     User(String username,String Fname,String Email, String password,boolean has_house,int ID,LocalDate applydate,String Add_ress, String contact) {  //Client
        this.username = username;
        this.Fname = Fname;
        this.email = Email;
        this.password = password;
        this.has_house = has_house;
        this.Id = ID;
        this.applyDate = applydate;
        this.Add_ress = Add_ress;
        this.contact = contact;
    }
    User(String username,String Fname,String Email, String password,boolean has_house,int ID,String Add_ress, String contact) {  //Client
        this.username = username;
        this.Fname = Fname;
        this.email = Email;
        this.password = password;
        this.has_house = has_house;
        this.Id = ID;
        this.Add_ress = Add_ress;
        this.contact = contact;
    }
    User(int id, String username,String Fname,String Email, String password,boolean has_house, String contact) { //Admin
        this.username = username;
        this.Fname = Fname;
        this.email = Email;
        this.password = password;
        this.has_house = has_house;
        this.contact = contact;
        this.Id = id;
    }
    User(String username,String Fname,String Email, String password,boolean has_house, String contact) { //Admin
        this.username = username;
        this.Fname = Fname;
        this.email = Email;
        this.password = password;
        this.has_house = has_house;
        this.contact = contact;
        
    }
    User(String username,String Fname,String Email, String password,String rec_key,String usertype) { //forgot pass
        this.username = username;
        this.Fname = Fname;
        this.email = Email;
        this.password = password;
        this.rec_key = rec_key;
        this.usertype = usertype;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    public String getFname() {
        return Fname;
    }
    public String getRec_key() {
        return rec_key;
    }
    public String getUsertype() {
        return usertype;
    }
    public boolean getHas_house(){
        return has_house;
    }
    public int getID(){
        return Id;
    }
    public LocalDate getApplyDate() {
        return applyDate;
    }
    public String getAdd_ress() {
        return Add_ress;
    }
    public String getContact() {
        return contact;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setFname(String fname) {
        this.Fname = fname;
    }
    
    public void setRec_key(String recKey) {
        this.rec_key = recKey;
    }
    
    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
    
    public void setHas_house(boolean hasHouse) {
        this.has_house = hasHouse;
    }
    
    public void setID(int id) {
        this.Id = id;
    }
    
    public void setApplyDate(LocalDate applyDate) {
        this.applyDate = applyDate;
    }
    
    public void setAdd_ress(String address) {
        this.Add_ress = address;
    }
    
    public void setContact(String contact) {
        this.contact = contact;
    }
    

    public void LogCredential(){
        System.out.println("Name: " + Fname);
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        System.out.println("Has house: " + has_house);
    }

}
class House {
    private int house_num,clientID;
    double price;
    private boolean has_client;

    House(int x,boolean has_client,int cliendID,double price){
        this.house_num = x;
        this.has_client = has_client;
        this.clientID = cliendID;
        this.price = price;
    }
    House(int x){
        this.house_num = x;
    }
    House(int x, Double price){
        this.house_num = x;
        this.price = price;
    }
   public int Gethouse_num(){
        return house_num;
    }

    public double getprice(){
        return price;
    }
}
 class Tenant {
    public int id;
    public String name;
    public int houseNumber;
    public double monthlyRate;
    public Date lastPayment;
    Date applyDate;
    // Constructor
    public Tenant(int id, String name, int houseNumber, double monthlyRate, Date lastPayment,Date appdate) {
        this.id = id;
        this.name = name;
        this.houseNumber = houseNumber;
        this.monthlyRate = monthlyRate;
        this.lastPayment = lastPayment;
        this.applyDate = appdate;
    }

    // Getters and setters
     // Getters and setters
     public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public double getMonthlyRate() {
        return monthlyRate;
    }

    public void setMonthlyRate(double monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public Date getLastPayment() {
        return lastPayment;
    }

    public void setLastPayment(Date lastPayment) {
        this.lastPayment = lastPayment;
    }
    public Date getApplyDate() {
        return applyDate;
    }

}
  
    class Payment {
        private int id;
        private int houseid;
        private String name;
        private String stats;
        private double monthly_rate;
        private Date date;
    
        // Constructor
        public Payment(int id, int houseid, String name, String stats, double monthly_rate, Date date) {
            this.id = id;
            this.houseid = houseid;
            this.name = name;
            this.stats = stats;
            this.monthly_rate = monthly_rate;
            this.date = date;
        }
        public Payment(String name, int house_num,double price, Date date){
            this.name = name;
            this.houseid = house_num;
            this.monthly_rate = price;
            this.date = date;
        }
    
        // Getter and Setter methods
        public int getId() {
            return id;
        }
    
        public void setId(int id) {
            this.id = id;
        }
    
        public int getHouseid() {
            return houseid;
        }
    
        public void setHouseid(int houseid) {
            this.houseid = houseid;
        }
    
        public String getName() {
            return name;
        }
    
        public void setName(String name) {
            this.name = name;
        }
    
        public String getStats() {
            return stats;
        }
    
        public void setStats(String stats) {
            this.stats = stats;
        }
    
        public double getMonthly_rate() {
            return monthly_rate;
        }
    
        public void setMonthly_rate(double monthly_rate) {
            this.monthly_rate = monthly_rate;
        }
    
        public Date getDate() {
            return date;
        }
    
        public void setDate(Date date) {
            this.date = date;
        }
    }
    


