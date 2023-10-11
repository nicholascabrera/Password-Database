# Password-Database
*THIS IS A WORK-IN-PROGRESS*

The Password Database aims to generate and store passwords for later use securely.

To access and generate passwords, users must sign in. The usernames and master passwords are stored in an SQL Database hosted locally, and everything is securely locked behind a login feature. I utilized the Argon2 hash algorithm, a state-of-the-art (at least for the next year) hashing algorithm that is currently the only dedicated password hashing algorithm. Once users are beyond the log-in window, they are presented with three choices: to print all their generated passwords, print a certain generated password, or generate a new password.

To clarify, generated passwords are paired with the website or application they allow access to and a username or display name. There are no duplicate usernames and website pairs. Should the user choose to print their generated password database, their usernames, generated passwords, and websites are printed for them to view in the GUI. Similarly, should the user choose to search for generated passwords for a single website, the username, and generated password will be displayed.

Should the user choose to generate a new password, they will be prompted to enter a username and website or application for pairing the generated password. Then, the password generation sequence will begin, in which the user is queried on the specifics of the password they want generated. The generated password will be graded and presented to the user at the end of the process. If the user accepts the password, it will be encrypted and stored in their database. No passwords are stored in the database without being encrypted or hashed. The user may also choose not to accept the generated password, in which case a new one will be generated and presented.

Users may exit the application upon completing their business with the Password Database simply by clicking the red 'X.'

## Features
The Password Database sports several interesting features, the foremost listed here:
1. Secure Username and Password Verification via a randomly salted Argon2 Password Hash offered by the Bouncy Castle JCA provider
2. Clean and easy-to-navigate GUI via Java Swing
3. AES Encrypted and Securely Random Password Generation via the JCA
4. MySQL Database Storage via JDBC
5. Secure Anti-SQL Injection techniques such as Input Verification with regex, Limited Views and Permissions, and Parameterized Queries
6. Custom Exception Handling
7. Maven Build which allows the addition of dependencies for Bouncy Castle and JDBC

## User Installation
Assuming an exe or jar file has not yet been uploaded, you'll need to download a few things and run a few scripts to run this code yourself. Namely, you'll need to download MySQL and run my SQL script to set up the databases, readers, and writers. Once the databases are set up, you'll need to actually open up the database for business using the ```mysqld --console``` command after navigating to the bin folder of your MySQL installation in the terminal.

Assuming you use VS Code, you are now ready to use this program.

