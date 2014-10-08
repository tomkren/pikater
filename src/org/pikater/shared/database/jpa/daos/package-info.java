/**
 * This package contains classes, that are implementing JPA entity retrieval and creation.
 * For each JPA entity class exists a data manipulation class called Data Access Object (DAO).
 * <p>
 * Each DAO implements typical use-cases for that particular entity, however, in some cases it's inevitable that
 *  one DAO must manipulate with other JPA-entities.
 */
package org.pikater.shared.database.jpa.daos;