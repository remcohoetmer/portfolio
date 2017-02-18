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
`hoofdgroep_id`,`groepscode`,`product`,geplande_periode, organisatie_id,locatie_id,scope_id)
VALUES
 (151,'A','Groep 1','Beschrijving 1',100000,'M',NULL,NULL,'Soep','2013', 124,null,null),
 (152,'A','Groep 2','Beschrijving 2',100000,'M',151,'AAA123',NULL,'mei 2013',124,null,92),
 (153,'A','Groep 3','Edu',100000,'S',null,null,NULL,null,125,null,93),
 (154,'A','Club 4','club',100000,'M',null,null,NULL,null,126,null,91),
 (155,'P','Club 5','club',100000,'M',null,null,NULL,null,126,null,91);
 
 

INSERT INTO kenmerk (`groep_id`,`kenmerk`) VALUES
(151,'Kenmerk1'),
(151,'KNMK 2');




-- Bulbasaur is lid van 4 groepen binnen 3 organisaties
INSERT INTO lidmaatschap (status,groep_id,klant_id,rol)
VALUES
('A',153,"testklant1", 0),
('P',154,"testklant2", 0);


commit;

