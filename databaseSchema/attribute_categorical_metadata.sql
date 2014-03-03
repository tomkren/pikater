CREATE TABLE attribute_categorical_metadata (
  id SERIAL PRIMARY KEY REFERENCES attribute_metadata (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);
