-- Table: add_patent_record

-- DROP TABLE add_patent_record;

CREATE TABLE add_patent_record
(
  id serial NOT NULL,
  public_date timestamp with time zone,
  patent_type smallint,
  total_page bigint,
  per_page_num bigint,
  current_page bigint,
  load_status smallint,
  add_date timestamp with time zone,
  CONSTRAINT pk_add_patent_record PRIMARY KEY (id),
  CONSTRAINT one_add_patent_record UNIQUE (public_date, patent_type, current_page)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE add_patent_record
  OWNER TO changsure;

-- Index: index_add_patent_record_public_date

-- DROP INDEX index_add_patent_record_public_date;

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

-- Table: patent_fee

-- DROP TABLE patent_fee;

CREATE TABLE patent_fee
(
  patent_no character varying(16) NOT NULL,
  fee_amount numeric(10,2),
  fee_type character varying(64),
  register_no character varying(64),
  receipt_no character varying(64),
  fee_person character varying(64),
  status character varying(64),
  fee_date timestamp with time zone,
  add_date timestamp with time zone,
  CONSTRAINT pk_patent_fee PRIMARY KEY (patent_no)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE patent_fee
  OWNER TO changsure;

  -- Table: patent_info

-- DROP TABLE patent_info;

CREATE TABLE patent_info
(
  patent_no character varying(16) NOT NULL,
  apply_date timestamp with time zone,
  patent_name character varying(128),
  public_no character varying(32),
  public_date timestamp with time zone,
  main_category_no character varying(32),
  pre_apply_no character varying(64),
  sec_category_no character varying(256),
  certificate_date timestamp with time zone,
  pre_right character varying(256),
  applier character varying(64),
  address character varying(256),
  inventor character varying(128),
  global_patent character varying(64),
  global_public character varying(64),
  entry_country_date timestamp with time zone,
  agency character varying(256),
  agent character varying(64),
  summary text,
  add_date timestamp with time zone,
  CONSTRAINT pk_patent_info PRIMARY KEY (patent_no)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE patent_info
  OWNER TO changsure;

  -- Table: patent_main

-- DROP TABLE patent_main;

CREATE TABLE patent_main
(
  patent_no character varying(16) NOT NULL,
  patent_name character varying(256),
  public_date timestamp with time zone,
  patent_status character varying(16),
  add_date timestamp with time zone,
  patent_type smallint,
  CONSTRAINT pk_patent_main PRIMARY KEY (patent_no)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE patent_main
  OWNER TO changsure;

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

-- Table: patent_notice_fawen

-- DROP TABLE patent_notice_fawen;

CREATE TABLE patent_notice_fawen
(
  patent_no character varying(16) NOT NULL,
  notice_date timestamp with time zone,
  register_no character varying(64),
  notice_code character varying(16),
  receiver character varying(32),
  address character varying(256),
  add_date timestamp with time zone,
  CONSTRAINT pk_patent_notice_fawen PRIMARY KEY (patent_no)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE patent_notice_fawen
  OWNER TO changsure;
