package com.password_db.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import javax.swing.JOptionPane;

import com.password_db.cryptography.Password;
import com.password_db.cryptography.SecureObject;
import com.password_db.exceptions.IncorrectPasswordException;
import com.password_db.exceptions.IncorrectUsernameException;
import com.password_db.exceptions.NoPasswordException;

public class Database {
   private String username;
   private Password masterPassword;

   /**
    * In order to prevent SQL Injection, I will need several things:
    * - Parameterized Queries,
    * - "Least Privilege", and
    * - Allow-list Input Validation.
    * First is "Least Privilege", which can be accomplished by the admin. I need to create
    *    a dedicated database user account for each instance of its use. For example,
    *    I will have the three databases for username, password, and salt. From the login
    *    window a username and password are taken and passed to one "Database" instance,
    *    one which has read-only access for the username table. This instance passes the ID associated
    *    with the username to the two other database accounts: one that has read-only access to
    *    the passwords, and one with read-only access to the salts. Using these three accounts, I will
    *    link them together to form a complete username, password, salt combination.
    * Second is the Input Validation. By definition, anything not on the Allow-list
    *    prohibitted. I need to learn how to develop "regular expressions."
    * Lastly is Parameterized Queries. This ensures that the validated input that I receive is, in fact,
    *    exactly what I expect it to be, and not an injected SQL script.
    */
   public Database(){}

   public Database(String username, Password masterPassword){
      this.username = username;
      this.masterPassword = masterPassword;
   }

   public void setUsername(String username){
      this.username = username;
   }
   
   public void setPassword(Password masterPassword){
      this.masterPassword = masterPassword;
   }

   public String getUsername(){
      return this.username;
   }

   public Password getPassword(){
      return this.masterPassword;
   }

   public boolean addCredentials(int ID, Password masterPassword){
      try (
         Connection conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/pass_db?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "writer_passDB", "56zj95!34u3$VB$t")
      ){
         SecureObject s = new SecureObject();

         byte[] salt = SecureObject.generateSalt(128);
         String passwordHash = s.argonHash(masterPassword, salt);

         String query = "INSERT INTO pass_db.app_pass VALUES (?, ?, ?)";       // insert the id, hashed password, and its salt.

         PreparedStatement prepped = conn.prepareStatement(query);      // use parameterized queries to prevent SQL injection.
         prepped.setInt(1, ID);                                            // substitute the input ID for the 1st '?'
         prepped.setString(2, passwordHash);                               // sub the hashed password 
         prepped.setString(3, Base64.getEncoder().encodeToString(salt));   // sub the new created salt

         int rowsAffected = prepped.executeUpdate();       // execute the SQL insertion

         if(rowsAffected > 0){
            return true;
         }

      } catch (SQLException exception){
         exception.printStackTrace();
      }

      return false;
   }

   public boolean verifyCredentials(String username, Password masterPassword){
      boolean credentialsVerified = false;
      boolean usernameVerified = false;
      int identifier = -1;
      SecureObject s = new SecureObject();

      // first connection to the username database
      try (
         Connection conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/user_db?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "reader_userDB", "9XE6g^#^5VB")     // this is the database reader. It has a limited view (user_dba) and read-only access.
      ){
         String query = "SELECT ID FROM user_db.users WHERE username = ?";       // find the ID for the corresponding username.

         PreparedStatement prepped = conn.prepareStatement(query);      // use parameterized queries to prevent SQL injection.
         prepped.setString(1, username);                 // substitute the input username for the 1st '?'

         ResultSet rs = prepped.executeQuery();       // execute the SQL query

         // check if the database contains matching data
         if(rs.next()){
            identifier = rs.getInt("ID");    // get the ID so we can look up the hashed password
            usernameVerified = true;
         } else {
            throw new IncorrectUsernameException("This username doesn't exist.");   //if the username doesn't exist, then throw exception
         }

      } catch (SQLException exception){
         exception.printStackTrace();
      } catch (IncorrectUsernameException exception){
         JOptionPane.showMessageDialog(null, exception.getMessage(), "Invalid Username", 0);
      }

      if(usernameVerified && identifier != -1){
         System.out.println("ID: " + identifier);

         // second connection to the password database
         try(
            Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:3306/pass_db?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                  "reader_passDB", "Gy8KS7^7%a!")     // this is the database reader. It has a limited view (pass_db) and read-only access.
         ){
            String query = "SELECT * FROM pass_db.app_pass WHERE ID = ?";       // find the password for the corresponding ID.

            PreparedStatement prepped = conn.prepareStatement(query);      // use parameterized queries to prevent SQL injection.
            prepped.setInt(1, identifier);                 // substitute the input ID for the 1st '?'

            ResultSet rs = prepped.executeQuery();       // execute the SQL query

            if(rs.next()){
               String password = rs.getString("password");
               String salt = rs.getString("salt");
               if(s.verifier(masterPassword, password, salt)){
                  credentialsVerified = true;
               } else {
                  throw new IncorrectPasswordException("Incorrect Password.");
               }
            } else {
               throw new NoPasswordException("No password exists for this username.");
            }

         } catch (SQLException exception){
            exception.printStackTrace();
         } catch (IncorrectPasswordException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Invalid Password", 0);
         } catch (NoPasswordException exception){
            byte entry = (byte) JOptionPane.showConfirmDialog(null, exception.getMessage() + 
               "\nDo you want to add your currently inputted password?", "No Password.", 0);
            if (entry == JOptionPane.YES_OPTION) {
               addCredentials(identifier, masterPassword);
            }
         }
      }

      return credentialsVerified;
   }
}