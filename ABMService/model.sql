
CREATE TABLE simulation_state
(
	id                   INTEGER NOT NULL ,
	description          VARCHAR(20) NULL
);



CREATE UNIQUE INDEX XPKsimulation_state ON simulation_state
(id   ASC);



ALTER TABLE simulation_state
	ADD CONSTRAINT  XPKsimulation_statePK PRIMARY KEY (id);



CREATE TABLE simulation_type
(
	id                   INTEGER NOT NULL ,
	description          VARCHAR(60) NULL
);



CREATE UNIQUE INDEX XPKsimulation_type ON simulation_type
(id   ASC);



ALTER TABLE simulation_type
	ADD CONSTRAINT  XPKsimulation_typePK PRIMARY KEY (id);



CREATE TABLE simulations
(
	id                   CHAR(36) NOT NULL ,
	description          VARCHAR(200) NOT NULL ,
	user_id              VARCHAR(20) NOT NULL ,
	timestamp            CHAR(23) NOT NULL ,
	id_simulation_type   INTEGER NULL ,
	id_simulation_state  INTEGER NULL ,
	content              bytea NULL
);



CREATE UNIQUE INDEX XPKsimulations ON simulations
(id   ASC);



ALTER TABLE simulations
	ADD CONSTRAINT  XPKsimulationsPK PRIMARY KEY (id);


CREATE TABLE simulation_details
(
	id_simulation        CHAR(36) NOT NULL ,
	id                   INTEGER NOT NULL ,
	content              bytea NOT NULL
);



CREATE UNIQUE INDEX XPKsimulation_details ON simulation_details
(id_simulation   ASC,id   ASC);



ALTER TABLE simulation_details
	ADD CONSTRAINT  XPKsimulation_detailsPK PRIMARY KEY (id_simulation,id);



ALTER TABLE simulations
	ADD CONSTRAINT simulation_type_sim FOREIGN KEY (id_simulation_type) REFERENCES simulation_type (id);



ALTER TABLE simulations
	ADD CONSTRAINT simulation_state_sim FOREIGN KEY (id_simulation_state) REFERENCES simulation_state (id);

ALTER TABLE simulation_details
	ADD CONSTRAINT simulations_simdetails FOREIGN KEY (id_simulation) REFERENCES simulations (id);

CREATE or replace FUNCTION simulations_del() RETURNS trigger AS $simulations_del$
BEGIN
	delete from simulation_details where id_simulation = old.id;
	return OLD;
END;
$simulations_del$ LANGUAGE plpgsql;

create  trigger tg_simulations_del_before before delete on simulations FOR EACH ROW
	execute FUNCTION simulations_del();

SELECT sum(pg_column_size(t.*)) as filesize, count(*) as filerow FROM simulation_details as t;

alter table usuario add usr_configuration text;
