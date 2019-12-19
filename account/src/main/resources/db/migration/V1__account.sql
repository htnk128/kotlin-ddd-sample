CREATE TABLE account (
  account_id            varchar(64)     PRIMARY KEY
 ,name                  varchar(100)    NOT NULL
 ,name_pronunciation    varchar(100)    NOT NULL
 ,email                 varchar(100)    NOT NULL
 ,password              varchar(64)     NOT NULL
 ,created_at            DATETIME(3)     NOT NULL
 ,deleted_at            DATETIME(3)     NULL
 ,updated_at            DATETIME(3)     NOT NULL
);
