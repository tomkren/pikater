CREATE TABLE datasets (
  id SERIAL PRIMARY KEY,
  filehash varchar(45) UNIQUE NOT NULL,
  filename varchar(45) NOT NULL,
  ownerId integer NOT NULL REFERENCES users (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  globalMetadataId integer DEFAULT NULL REFERENCES global_metadata (id) ON DELETE NO ACTION ON UPDATE NO ACTION
); 
CREATE INDEX ownerFK_idx ON datasets (ownerId);
CREATE INDEX globalMetadataFK_idx ON datasets (globalMetadataId);
