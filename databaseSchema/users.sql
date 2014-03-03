CREATE TABLE users (
  id SERIAL,
  login varchar(45) NOT NULL,
  password varchar(45) NOT NULL,
  PRIMARY KEY (id)
);
