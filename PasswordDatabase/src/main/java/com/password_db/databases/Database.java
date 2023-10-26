package com.password_db.databases;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import javax.swing.JOptionPane;

import com.password_db.cryptography.Password;
import com.password_db.cryptography.SecureObject;

import com.password_db.gui.Record;

import com.password_db.exceptions.IncorrectPasswordException;
import com.password_db.exceptions.IncorrectUsernameException;
import com.password_db.exceptions.NoPasswordException;

public class Database {
   private String username;
   private Password masterPassword;

   public static final int LOGIN_GOOD = 0, LOGIN_BAD = 1, REGISTER = 2;

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

   public boolean idExists(int ID){
      try (
         Connection conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/user_db?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "reader_userDB", "9XE6g^#^5VB")
      ){
         String query = "SELECT username FROM user_db.users WHERE ID = ?";
         PreparedStatement parameterizedQuery = conn.prepareStatement(query);
         parameterizedQuery.setInt(1, ID);

         ResultSet rs = parameterizedQuery.executeQuery();
         
         return rs.next();

      } catch(SQLException e){
         e.printStackTrace();
      }

      return false;
   }

   public boolean addCredentials(int ID, String username, Password masterPassword){
      boolean usernameAdded = false;
      boolean passwordAdded = false;

      try (
         Connection conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/user_db?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "writer_passDB", "56zj95!34u3$VB$t")
      ){
         String query = "INSERT INTO user_db.users VALUES (?, ?)";
         PreparedStatement parameterizedQueries = conn.prepareStatement(query);
         parameterizedQueries.setInt(1, ID);
         parameterizedQueries.setString(2, username);

         int rowsAffected = parameterizedQueries.executeUpdate();

         if(rowsAffected > 0){
            usernameAdded = true;
         }
      } catch(SQLException e){
         e.printStackTrace();
      }

      if(usernameAdded){
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
               passwordAdded =  true;
            }

         } catch (SQLException exception){
            exception.printStackTrace();
         }
      }

      return passwordAdded;
   }

   public int verifyCredentials(String username, Password masterPassword){
      int credentialsVerified = LOGIN_BAD;
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
         int entry = JOptionPane.showConfirmDialog(null, exception.getMessage() + "\nWould you like to register with this username?", "Invalid Username", JOptionPane.YES_NO_OPTION);
         if (entry == JOptionPane.YES_OPTION){
            credentialsVerified = REGISTER;
         }
      }

      if(usernameVerified && identifier != -1){
         //System.out.println("ID: " + identifier);

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
                  credentialsVerified = LOGIN_GOOD;
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
               addCredentials(identifier, username, masterPassword);
            }
         }
      }

      return credentialsVerified;
   }

   public boolean addUser(String username, Password password){
      //ID is going to be the value of the all the characters in the username multiplied by some securely random integer.
      char userletters[] = username.toCharArray();
      int userValue = 0;
      for(char c: userletters){
         userValue += Character.getNumericValue(c);
      }

      SecureRandom sr = new SecureRandom();
      int ID = userValue * sr.nextInt(128);

      if(!this.idExists(ID)){
         return addCredentials(ID, username, password);
      }
      
      return false;
   }

   public boolean storeGeneratedPassword(String ID, String website, String username, String password, String salt, String key){
      try (
         Connection conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/generated_db?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
            "writer_generatedDB", "67&7tP4f@PYcx") // this is the database reader. It has a limited view (pass_db)
            // and read-only access.
      ) {

         String query = "INSERT INTO generated_db.website_users VALUES (?, ?, ?)"; // insert the id, website, and username

         PreparedStatement prepped = conn.prepareStatement(query); // use parameterized queries to prevent SQL injection.
         prepped.setString(1, ID); // substitute the input ID for the 1st '?'
         prepped.setString(2, website); // sub the website
         prepped.setString(3, username); // sub the username

         int rowsAffected = prepped.executeUpdate(); // execute the SQL insertion

         query = "INSERT INTO generated_db.e_passwords VALUES (?, ?)"; // insert the ID and generated password

         prepped = conn.prepareStatement(query);
         prepped.setString(1, ID); // substitute the input ID for the first parameter
         prepped.setString(2, password); // sub out the generated password for the second parameter.

         rowsAffected = prepped.executeUpdate(); // execute insertion

         query = "INSERT INTO generated_db.e_keys VALUES (?, ?, ?)"; // insert the ID, key, and salt.

         prepped = conn.prepareStatement(query);
         prepped.setString(1, ID); // insert the ID
         prepped.setString(2, key); // insert the key
         prepped.setString(3, salt); // insert the salt

         rowsAffected = prepped.executeUpdate(); // execute

         if (rowsAffected > 0) {
            return true;
         }

      } catch (SQLException e) {
         e.printStackTrace();
      }
      return false;
   }

   public ResultSet pullKey(String ID) throws SQLException{
      Connection conn = DriverManager.getConnection(
         "jdbc:mysql://localhost:3306/generated_db?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
         "reader_senes", "#js7qBD5#Y8xG"); // this is the database reader. It has a limited view (e_keys)
         // and read-only access.

      String query = "SELECT e_key, salt FROM generated_db.e_keys WHERE ID = ?";
      PreparedStatement paramQuery = conn.prepareStatement(query);
      paramQuery.setString(1, ID);

      ResultSet keysAndSalts = paramQuery.executeQuery();
      return keysAndSalts;
   }

   public Record[] pullAllPasswords() throws Exception{
      // in order to pull the passwords, the reader will read the recorded username and website from website_users
      // concatenate them with the master username and master password the user provides, then checks to see if it matches the 
      // ID recorded in the table. Then and only then, will they reader pull from e_passwords and e_keys.
      String ID = "", website = "", pulledUser = "", pulledPassword = "", salt = "", key = "";
      byte[] hashSalt = Base64.getDecoder().decode("ABCDEFGHIJKLMNOP");
      SecureObject s = new SecureObject();
      s.init();

      Record records[] = new Record[0];

      try (
         Connection conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/generated_db?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
            "reader_uspa", "d7*jNc5dZz9z@") // this is the database reader. It has a limited view (website_users
            // and e_passwords) and read-only access.
      ) {
         
         // first, we have to pull all the records from our databases.
         // pull from website_users
         String query = "SELECT * FROM generated_db.website_users"; // read all
         PreparedStatement prepped = conn.prepareStatement(query); // use parameterized queries to prevent SQL injection.
         ResultSet website_usersResultSet = prepped.executeQuery(); // execute the read

         //System.out.println("Read from usernames and websites.");

         // pull from e_passwords
         query = "SELECT * FROM generated_db.e_passwords;"; // read all
         prepped = conn.prepareStatement(query);
         ResultSet e_passwordsResultSet = prepped.executeQuery(); // execute insertion
         
         //System.out.println("Read from passwords.");

         while(website_usersResultSet.next() && e_passwordsResultSet.next()){
            ID = website_usersResultSet.getString("ID");
            website = website_usersResultSet.getString("website");
            pulledUser = website_usersResultSet.getString("username");

            String identifier = ((pulledUser.concat(website)).concat(this.getUsername())).concat(this.getPassword().getPassword());
            identifier = s.argonHash(identifier, hashSalt);

            if(identifier.equals(ID)){     // the ID is correct, and the stored password belongs to us.
               pulledPassword = e_passwordsResultSet.getString("password");   // pull the password
               ResultSet keyResultSet = pullKey(ID);     // pull the key and salt at that ID
               if(keyResultSet.next()){
                  key = keyResultSet.getString("e_key");
                  salt = keyResultSet.getString("salt");

                  String decryptedKey = s.decrypt(Base64.getDecoder().decode(key), this.getPassword());

                  String decryptedString = s.decrypt(Base64.getDecoder().decode(pulledPassword), Base64.getDecoder().decode(decryptedKey), Base64.getDecoder().decode(salt));
                  
                  records = Record.append(records, new Record(website, pulledUser, decryptedString));
                  //System.out.println("Website: " + website);
                  //System.out.println("Username: " + pulledUser);
                  //System.out.println("Password: " + decryptedString);
               } else {
                  System.out.println("Couldn't pull the key.");
               }
            }
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return records;
   }
}