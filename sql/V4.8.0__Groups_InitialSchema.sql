CREATE TABLE country (
	id bigserial PRIMARY KEY,
	name text NOT NULL,
	code text,
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC')	
);

CREATE TABLE state (
	id bigserial PRIMARY KEY,
	name text NOT NULL,
	code text,
	country_id bigint REFERENCES country(id),
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC')	
);

CREATE TABLE school (
	id bigserial PRIMARY KEY,
	name text NOT NULL,
	code text,
	tenant text NOT NULL,
	reference_id text,
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC')	
);

COMMENT ON COLUMN school.tenant IS 'Even though the type of the column is text, expected value is UUID';

CREATE TABLE groups (
	id bigserial PRIMARY KEY,
	name text NOT NULL,
	code text,
	description text,
	type varchar(128) CHECK (type::varchar = ANY(ARRAY['system'::varchar, 'custom'::varchar])),
	sub_type varchar(128) CHECK ( sub_type::varchar = ANY(ARRAY['school_district'::varchar, 'district'::varchar, 'block'::varchar, 'cluster'::varchar])),
	parent_id bigint,
	state_id bigint REFERENCES state(id),
	country_id bigint REFERENCES country(id),
	tenant text NOT NULL,
	tenant_root text,
	creator_id text NOT NULL,
	modifier_id text NOT NULL,
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC')	
);

CREATE TABLE class_level_data_reports (
	id bigserial PRIMARY KEY,
	class_id text NOT NULL,
	views int,
	attempts int,
	collection_timespent bigint,
	assessment_timespent bigint,
	assessment_performance numeric (5,2),
	school_id bigint REFERENCES school(id),
	state_id bigint REFERENCES state(id),
	country_id bigint REFERENCES country(id),
	month int,
	year int,
	tenant text NOT NULL,
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC')
);

COMMENT ON COLUMN class_level_data_reports.class_id IS 'Even though the type of the column is text, expected value is UUID';
COMMENT ON COLUMN class_level_data_reports.tenant IS 'Even though the type of the column is text, expected value is UUID';

CREATE TABLE group_level_data_reports (
	id bigserial PRIMARY KEY,
	class_id text NOT NULL,
	views int,
	attempts int,
	collection_timespent bigint,
	assessment_timespent bigint,
	assessment_performance numeric (5,2),
	group_id bigint REFERENCES groups(id),
	group_type varchar(128) CHECK (group_type::varchar = ANY(ARRAY['system'::varchar, 'custom'::varchar])),
	group_sub_type varchar(128) CHECK ( group_sub_type::varchar = ANY(ARRAY['school_district'::varchar, 'district'::varchar, 'block'::varchar, 'cluster'::varchar])),
	school_id bigint REFERENCES school(id),
	state_id bigint REFERENCES state(id),
	country_id bigint REFERENCES country(id),
	month int,
	year int,
	tenant text NOT NULL,
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC')
);

CREATE TABLE group_user_acl (
	user_id text NOT NULL,
	group_id bigint REFERENCES groups(id),
	acl_type varchar(128) CHECK (acl_type::varchar = ANY(ARRAY['read'::varchar, 'report'::varchar])),
	tenant text NOT NULL,
	UNIQUE (user_id, group_id)
);

COMMENT ON COLUMN group_user_acl.user_id IS 'Even though the type of the column is text, expected value is UUID';

CREATE TABLE group_school_mapping (
	group_id bigint REFERENCES groups(id),
	school_id bigint REFERENCES school(id),
	UNIQUE(group_id, school_id)
);

CREATE TABLE school_class_mapping (
	school_id bigint REFERENCES school(id),
	class_id text NOT NULL,
	UNIQUE(school_id, class_id)
);

COMMENT ON COLUMN school_class_mapping.class_id IS 'Even though the type of the column is text, expected value is UUID';
