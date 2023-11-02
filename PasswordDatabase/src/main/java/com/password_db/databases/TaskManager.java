package com.password_db.databases;

/**
 * Provides the choice options for DatabaseTaskManager.
 * <pre></pre>
 * There are five choices, user verification, adding a user,
 * pulling all passwords, storing a specific password, and no task selection.
 * They are quantified as such:
 * <html>
 *   <ul>
 *     <li>VERIFY_USER</li>
 *     <li>ADD_USER</li>
 *     <li>PULL_PASSWORDS</li>
 *     <li>STORE_PASSWORD</li>
 *     <li>NO_CHOICE</li>
 *   </ul>
 * </html>
 * To use these values, simply invoke the enum name "TaskManager", add a dot, then the correct choice. For example:
 * 
 * {@code TaskManager.VERIFY_USER}
 * 
 * will invoke the user verification choice.
 * 
 * @author Nicholas Cabrera
 * @see com.password_db.databases.DatabaseTaskManager
 * @see com.password_db.databases.Database
 */
public enum TaskManager {
    VERIFY_USER, ADD_USER, PULL_PASSWORDS, STORE_PASSWORD, NO_CHOICE
}