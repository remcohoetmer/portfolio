DROP TABLE IF EXISTS kenmerk;
DROP TABLE IF EXISTS lidmaatschap;
DROP TABLE IF EXISTS groep;
DROP TABLE IF EXISTS scopes;


CREATE TABLE  groep (
  id int(18) unsigned NOT NULL auto_increment,
  laatstgewijzigd TIMESTAMP not null,
  status varchar(1) NOT NULL,
  naam varchar(100) NOT NULL,
  beschrijving varchar(255) NULL,
  aangemaakt_door varchar(92) not NULL,
  groeps_mutatie_type ENUM('M', 'S')  NOT NULL, 
  hoofdgroep_id int(18) unsigned NULL,
  groepscode varchar(100) NULL,
  product varchar(100) NULL,
  scope_id int(18) unsigned NULL,
  organisatie_id varchar(25) NULL,
  geplande_periode varchar(100) NULL,
  PRIMARY KEY  (id),
  FOREIGN KEY (hoofdgroep_id) REFERENCES groep(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TRIGGER groep_laatstgewijzigd_insert BEFORE INSERT ON groep 
FOR EACH ROW
SET NEW.laatstgewijzigd = NOW();

CREATE TRIGGER groep_laatstgewijzigd_update BEFORE UPDATE ON groep 
FOR EACH ROW
SET NEW.laatstgewijzigd = NOW();


CREATE TABLE kenmerk (
  id int(18) unsigned NOT NULL auto_increment,
  groep_id int(18) unsigned NOT NULL,
  kenmerk varchar(30) NOT NULL,
  PRIMARY KEY  (id),
  FOREIGN KEY (groep_id) REFERENCES groep (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lidmaatschap(
  id int(18) unsigned NOT NULL auto_increment,
  laatstgewijzigd TIMESTAMP not null,
  status varchar(1) NOT NULL,
  groep_id int(18) unsigned NOT NULL,
  klant_id varchar(92) NOT NULL,
  rol int(1) NOT NULL,
  PRIMARY KEY  (id),
  FOREIGN KEY (groep_id) REFERENCES groep(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TRIGGER lidmaatschap_laatstgewijzigd_insert BEFORE INSERT ON lidmaatschap 
FOR EACH ROW
SET NEW.laatstgewijzigd = NOW();

CREATE TRIGGER lidmaatschap_laatstgewijzigd_update BEFORE UPDATE ON lidmaatschap 
FOR EACH ROW
SET NEW.laatstgewijzigd = NOW();

CREATE TABLE scopes (
  id int(18) unsigned NOT NULL auto_increment,
  status varchar(1) NOT NULL,
  aangemaakt_door int(18) unsigned NULL,
  laatstgewijzigd TIMESTAMP not null,
  naam varchar(100) NOT NULL, 
  PRIMARY KEY  (id)
);


CREATE TRIGGER scope_laatstgewijzigd_insert BEFORE INSERT ON scopes 
FOR EACH ROW
SET NEW.laatstgewijzigd = NOW();

CREATE TRIGGER scope_laatstgewijzigd_update BEFORE UPDATE ON scopes 
FOR EACH ROW
SET NEW.laatstgewijzigd = NOW();




