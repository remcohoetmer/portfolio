delete from scopes;



INSERT INTO `scopes`
	(`id`,`status`,`aangemaakt_door`,`naam`) 
VALUES
(90,'A',100000,'Corporate'),
(91,'A',9000000,'SME'),
(92,'A',9000000,'Privates'),
(93,'A',9000000,'Sole Proprietorships');


delete from groep;
INSERT INTO groep (`id`,`status`,`naam`,`beschrijving`,`aangemaakt_door`,`groeps_mutatie_type`,
`hoofdgroep_id`,`groepscode`,`product`,geplande_periode, organisatie_id,scope_id)
VALUES
 (151,'A','Groep 1','Beschrijving 1',100000,'M',NULL,'AAA1','Soep','2013', 124,null),
 (152,'A','Groep 2','Beschrijving 2',100000,'M',151,'AAA2',NULL,'mei 2013',124,92),
 (153,'A','Groep 3','Edu',100000,'S',null,'AAA3',NULL,null,125,93),
 (154,'A','Club 4','club',100000,'M',null,'AAA4',NULL,null,126,91),
 (155,'P','Club 5','club',100000,'M',null,'AAA5',NULL,null,126,91);
 
 

INSERT INTO kenmerk (`groep_id`,`kenmerk`) VALUES
(151,'Kenmerk1'),
(151,'KNMK 2');


INSERT INTO lidmaatschap (status,groep_id,klant_id,rol)
VALUES
('A',153,"testklant1", 0),
('P',154,"testklant2", 0);


commit;

