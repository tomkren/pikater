/**
 * <p>
 * Package containing classes implementing native database access. Currently it has one
 * implemented class for connections to PostgreSQL database that is implementing the {@link org.pikater.shared.database.connection.IConnectionProvider}
 * interface.
 * </p>
 * <p>
 * In future these classes should be extended by new classes if new database managemenet
 * systems will be supported, or it can be removed if all database calls will be using
 * the JPA interface.
 * </p>
 * @see org.pikater.shared.database.connection.PostgreSQLConnectionProvider
 */
package org.pikater.shared.database.connection;