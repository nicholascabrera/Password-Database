# Password-Database
The Password Database aims to generate secure passwords and store them for later use.

To access and generate passwords, users must sign in. The usernames and master passwords are stored in an SQL Database hosted locally, and everything is securely locked behind a login feature. I utilized the Argon2 hash algorithm, a state-of-the-art (at least for the next year) hashing algorithm that is currently the only dedicated password hashing algorithm. Once users are beyond the log-in window, they are presented with three choices: to print all their generated passwords, print a certain generated password, or generate a new password.

To clarify, generated passwords are paired with the website or application they allow access to and a username or display name. There are no duplicate usernames and website pairs. Should the user choose to print their generated password database, their usernames, generated passwords, and websites are printed for them to view in the GUI. Similarly, should the user choose to search for generated passwords for a single website, the username, and generated password will be displayed.

Should the user choose to generate a new password, they will be prompted to enter a username and website or application for pairing the generated password. Then, the password generation sequence will begin, in which the user is queried on the specifics of the password they want generated. At the end of the process, the generated password will be graded and presented to the user. Should the user choose to accept the password, it will be encrypted and stored in their database. The user may also choose not to accept the generated password, in which case a new one will be generated and presented.

## Features
The Password Database sports several interesting features, the foremost listed here:
1. Secure Username and Password Verification via a randomly salted Argon2 Password Hash
2. Clean GUI via Java Swing
3. AES Encrypted Password Generation
4. MySQL Database Storage
5. Secure Anti-SQL Injection features such as Input Verification, Limited Views, and Parameterized Queries

