CREATE TABLE address (
  address_id            varchar(64)     PRIMARY KEY
 ,owner_id              varchar(64)     NOT NULL
 ,full_name             varchar(100)    NOT NULL
 ,zip_code              varchar(50)     NOT NULL
 ,state_or_region       varchar(100)    NOT NULL
 ,line1                 varchar(100)    NOT NULL
 ,line2                 varchar(100)    NULL
 ,phone_number          varchar(50)     NOT NULL
 ,created_at            DATETIME(3)     NOT NULL
 ,deleted_at            DATETIME(3)     NULL
 ,updated_at            DATETIME(3)     NOT NULL
);
