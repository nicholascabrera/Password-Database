# Password-Database
*THIS IS A WORK-IN-PROGRESS*

The Password Database aims to generate and store passwords for later use securely.

To access and generate passwords, users must sign in. The usernames and master passwords are stored in an SQL Database hosted locally, and everything is securely locked behind a login feature. I utilized the Argon2 hash algorithm, a state-of-the-art (at least for the next year) hashing algorithm that is currently the only dedicated password hashing algorithm. Once users are beyond the log-in window, they are presented with three choices: to print all their generated passwords, print a certain generated password, or generate a new password.

To clarify, generated passwords are paired with the website or application they allow access to and a username or display name. There are no duplicate usernames and website pairs. Should the user choose to print their generated password database, their usernames, generated passwords, and websites are printed for them to view in the GUI. Similarly, should the user choose to search for generated passwords for a single website, the username, and generated password will be displayed.

Should the user choose to generate a new password, they will be prompted to enter a username and website or application to pair with the generated password. Then, the password generation sequence will begin, in which the user is queried on the specifics of the password they want generated. The generated password will be graded and presented to the user at the end of the process. If the user accepts the password, it will be encrypted with a randomly salted AES cipher and stored in their database. No passwords are stored in the database without being encrypted, as in the case with generated passwords or hashed, as is standard for user login information. The user may also choose not to accept the generated password, in which case a new one will be generated and presented.

Users may exit the application upon completing their business with the Password Database or at any time simply by clicking the red 'X' or with the 'ESC' key.

## Features
The Password Database sports several interesting features, the foremost listed here:
1. Secure Username and Password Verification via a randomly salted Argon2 Password Hash offered by the Bouncy Castle JCA provider
2. Clean and easy-to-navigate GUI via Java Swing
3. AES Encrypted and Securely Random Password Generation via the JCA
4. MySQL Database Storage via JDBC
5. Secure Anti-SQL Injection techniques such as Input Verification with regex, Limited Views and Permissions, and Parameterized Queries
6. Custom Exception Handling
7. Maven Build in VS Code, which allows the addition of dependencies for Bouncy Castle and JDBC

## User Installation
Assuming an exe or jar file has not yet been uploaded, you'll need to download a few things and run a few scripts to run this code yourself. Namely, you'll need to download MySQL and run my SQL script to set up the databases, readers, and writers. Once the databases are set up, you'll need to actually open up the database for business using the ```mysqld --console``` command after navigating to the bin folder of your MySQL installation in the terminal.

### Scripts
After navigating to the bin folder of your installed MySQL directory in the terminal and signing in to a non-root user with ```CREATE``` and ```INSERT``` permissions, you will be able to run the ```db_setup.sql``` script. You can do this by running the code below:

```
SOURCE C:\[directory_script_is_stored_in]\db_setup.sql
```

Once you have completed this step, you must set up some users. This means you must be logged into a user that can ```GRANT``` permissions to other users, such as ```root```. Be extremely careful when using root, as you can inadvertently cause damage to your system. While in the root, run the following commands:

```
CREATE USER IF NOT EXISTS 'reader_userDB'@'localhost' IDENTIFIED BY '9XE6g^#^5VB';
CREATE USER IF NOT EXISTS 'reader_passDB'@'localhost' IDENTIFIED BY 'Gy8KS7^7%a!';
CREATE USER IF NOT EXISTS 'writer_passDB'@'localhost' IDENTIFIED BY '56zj95!34u3$VB$t';

GRANT SELECT ON user_db.users TO 'reader_userDB'@'localhost';
GRANT SELECT ON pass_db.app_pass TO 'reader_passDB'@'localhost';
GRANT INSERT ON pass_db.app_pass TO 'writer_passDB'@'localhost';
```

These commands are not housed in a file for user reassurance.

After running those commands and assuming you use VS Code, you are now ready to use this program.
