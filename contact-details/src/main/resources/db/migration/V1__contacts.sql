CREATE TABLE contact_details (
  contact_details_id    varchar(64) PRIMARY KEY
 ,customer_id           varchar(64) NOT NULL
 ,telephone_number      varchar(50) NOT NULL
);
CREATE INDEX contact_details_index01 ON contact_details (`customer_id`);
