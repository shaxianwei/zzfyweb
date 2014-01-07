CREATE INDEX index_add_patent_record_public_date
  ON add_patent_record
  USING btree
  (public_date);

-- Index: index_add_patent_record_public_patent_type

-- DROP INDEX index_add_patent_record_public_patent_type;

CREATE INDEX index_add_patent_record_public_patent_type
  ON add_patent_record
  USING btree
  (patent_type);
-- Index: index_patent_main_patent_type

-- DROP INDEX index_patent_main_patent_type;

CREATE INDEX index_patent_main_patent_type
  ON patent_main
  USING btree
  (patent_type);

-- Index: index_patent_main_public_date

-- DROP INDEX index_patent_main_public_date;

CREATE INDEX index_patent_main_public_date
  ON patent_main
  USING btree
  (public_date);

-- Index: index_patent_main_status

-- DROP INDEX index_patent_main_status;

CREATE INDEX index_patent_main_status
  ON patent_main
  USING btree
  (patent_status COLLATE pg_catalog."default");

