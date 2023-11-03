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
2. Clean, easy-to-navigate, and concurrent GUI via Java Swing and SwingWorker(Multithreading)
3. AES Encrypted and Securely Random Password Generation via the JCA
4. MySQL Database Storage via JDBC
5. Secure Anti-SQL Injection techniques such as Input Verification with regex, Limited Views and Permissions, and Parameterized Queries
6. Custom Exception Handling
7. Maven Build in VS Code, which allows the addition of dependencies for Bouncy Castle and JDBC

## User Installation
There are two ways to use this program. One requires running a single script from your ```root``` MySQL sccount, and the other involves some manual code. Neither are hard.

Assuming an exe or jar file has not yet been uploaded, you'll need to download a few things and run a few scripts to run this code yourself. Namely, you'll need to download MySQL and run my SQL script to set up the databases, readers, and writers. Learn how to download and set up MySQL [here](https://www3.ntu.edu.sg/home/ehchua/programming/sql/MySQL_HowTo.html#intro).

### Automatic Installation
After navigating to the bin folder of your installed MySQL directory in the terminal and signing into a root user with ```CREATE``` and ```GRANT``` permissions, you will be able to run the ```setup.sql``` script. Be careful when using root, as you can inadvertently cause damage to your system. You can do this by running the code below:

```
SOURCE C:\[directory_script_is_stored_in]\setup.sql
```
Before running the script, ensure that the code within the script matches the code here. For a quick check, there should only be 34 lines.

After running the script, you are ready to use this program.

### Manual Installation
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

CREATE USER IF NOT EXISTS 'reader_uspa'@'localhost' IDENTIFIED BY 'd7*jNc5dZz9z@';
CREATE USER IF NOT EXISTS 'reader_senes'@'localhost' IDENTIFIED BY '#js7qBD5#Y8xG';
CREATE USER IF NOT EXISTS 'writer_generatedDB'@'localhost' IDENTIFIED BY '67&7tP4f@PYcx';

GRANT SELECT ON generated_db.website_users TO 'reader_uspa'@'localhost';
GRANT SELECT ON generated_db.e_passwords TO 'reader_uspa'@'localhost';
GRANT SELECT ON generated_db.e_keys TO 'reader_senes'@'localhost';
GRANT INSERT ON generated_db.website_users TO 'writer_generatedDB'@'localhost';
GRANT INSERT ON generated_db.e_passwords TO 'writer_generatedDB'@'localhost';
GRANT INSERT ON generated_db.e_keys TO 'writer_generatedDB'@'localhost';
```
These commands are not housed in a file for user reassurance, but if it looks to be too much, just use the "Automatic Installation" Section, which does this for you.

After running these commands, you are now ready to use this program.

## What's Next?
Despite all my efforts, there are still major security flaws within the program. One massive issue is the fact that the MySQL users themselves (the readers and writers) store their login information in plain text in the program. Unfortunately, this isn't possible to rectify this without creating an external web service which holds the passwords, and only the usernames are stored in the java program. This way, decompiling the code would not directly compromise the entire project. This is currently beyond the scope of this project, but could be worth looking into for a future project. This could also mean migrating the MySQL server from command prompt to somewhere online, most likely SQLite if that is the case.

Ultimately, designing a webservice for this program requires more knowledge than I currently possess. Thankfully, I'll be working on rectifying that situation in the future.

Another potential avenue for improvement is utilizing the JavaMail API to send emails to users that have forgotten their passwords, which would allow them to securely change their password and maintain access to their passwords even if they forgot their password. This could also be done with security questions. Both are somewhat vulnerable, however in different ways. Should the security question be too surface level, an attacker could very easily use social engineering to comprimise a users data. With the email, if the users email is compromised then the attacker could easily gain access to all a users passwords. However, these two scenarios are different in one crucial respect: upon whom the blame must fall.

In the case of the security questions, the blame could easily fall on the program and myself for choosing poor questions whose answers are easily sniffed out. In this social world, it's unreasonable to believe that every user is going to be careful with surface level information about themself. On the otherhand, a compromised email is not the fault of the password manager, but the email provider. Should an attacker compromise a users data through another compromised resource, that's hardly the programs fault or my own. All in all, the JavaMail API would be the way to go.
