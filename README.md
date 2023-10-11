# Password-Database
The purpose of the Password Database is, in short, to generate secure passwords and store them for later use. In order to access and generate passwords, you must sign it. The usernames and passwords are stored in an SQL Database hosted locally, and everything is securely locked behind a login feature. I utilized the Argon2 hash algorithm, a state of the art (at least for the next year) hashing algorithm which is currently the only dedicated password hashing algorithm.

## Features
The Password Databse sports several interesting features, the foremost listed here:
1. Secure Username and Password Verification via a randomly salted Argon2 Password Hash
2. Clean GUI via Java Swing
3. AES Encrypted Password Generation
4. MySQL Database Storage
5. Secure Anti-SQL Injection features such as Input Verification, Limited Views, and Parameterized Queries

