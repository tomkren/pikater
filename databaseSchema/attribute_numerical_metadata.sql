CREATE TABLE attribute_numerical_metadata (
  id INTEGER PRIMARY KEY NOT NULL REFERENCES attribute_metadata (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);
