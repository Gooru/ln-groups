CREATE TABLE group_hierarchy (
	id bigserial PRIMARY KEY,
	name character varying(512),
	description text,
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC')
);

INSERT INTO group_hierarchy(name, description) VALUES ('India', 'Group hierarchy for India'), ('US', 'Group hierarchy for US'), ('SAP', 'Group hierarchy for SAP');

CREATE TABLE group_hierarchy_details (
	id bigserial PRIMARY KEY,
	name text NOT NULL,
	type text NOT NULL CHECK (type::varchar = ANY(ARRAY['school_district'::varchar, 'district'::varchar, 'block'::varchar, 'cluster'::varchar, 'school'::varchar, 'country'::varchar, 'state'::varchar])),
	hierarchy_id bigint NOT NULL REFERENCES group_hierarchy(id),
	sequence int NOT NULL,
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC')	
);

INSERT INTO group_hierarchy_details(name, type, hierarchy_id, sequence) VALUES ('Country', 'country', 1, 1);
INSERT INTO group_hierarchy_details(name, type, hierarchy_id, sequence) VALUES ('State', 'state', 1, 2);
INSERT INTO group_hierarchy_details(name, type, hierarchy_id, sequence) VALUES ('District', 'district', 1, 3);
INSERT INTO group_hierarchy_details(name, type, hierarchy_id, sequence) VALUES ('Block', 'block', 1, 4);
INSERT INTO group_hierarchy_details(name, type, hierarchy_id, sequence) VALUES ('Cluster', 'cluster', 1, 5);
INSERT INTO group_hierarchy_details(name, type, hierarchy_id, sequence) VALUES ('School', 'school', 1, 6);

INSERT INTO group_hierarchy_details(name, type, hierarchy_id, sequence) VALUES ('Country', 'country', 2, 1);
INSERT INTO group_hierarchy_details(name, type, hierarchy_id, sequence) VALUES ('State', 'state', 2, 2);
INSERT INTO group_hierarchy_details(name, type, hierarchy_id, sequence) VALUES ('School District', 'school_district', 2, 3);
INSERT INTO group_hierarchy_details(name, type, hierarchy_id, sequence) VALUES ('School', 'school', 2, 4);

INSERT INTO group_hierarchy_details(name, type, hierarchy_id, sequence) VALUES ('Country', 'country', 3, 1);
INSERT INTO group_hierarchy_details(name, type, hierarchy_id, sequence) VALUES ('School', 'school', 3, 2);

CREATE TABLE country_school_mapping (
	country_id bigint NOT NULL REFERENCES country_ds(id),
	school_id bigint NOT NULL REFERENCES school_ds(id),
	tenant text NOT NULL
);