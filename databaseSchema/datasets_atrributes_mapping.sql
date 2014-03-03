CREATE TABLE datasets_atrributes_mapping (
  id SERIAL PRIMARY KEY,
  datasetId INTEGER NOT NULL REFERENCES datasets (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  attributeMetadataId INTEGER NOT NULL REFERENCES attribute_metadata (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE INDEX datasetFK_idx ON datasets_atrributes_mapping (datasetId);
CREATE INDEX attributeMetadataFK_idx ON datasets_atrributes_mapping (attributeMetadataId);
