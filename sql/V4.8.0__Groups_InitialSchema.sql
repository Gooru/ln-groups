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

CREATE TABLE groups (
	id bigserial PRIMARY KEY,
	name text NOT NULL,
	code text,
	description text,
	type varchar(128) CHECK (type::varchar = ANY(ARRAY['system'::varchar, 'custom'::varchar])),
	sub_type varchar(128) CHECK (sub_type::varchar = ANY(ARRAY['school_district'::varchar, 'district'::varchar, 'block'::varchar, 'cluster'::varchar, 'custom'::varchar])),
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

CREATE TABLE group_user_acl (
	user_id text NOT NULL,
	group_id bigint REFERENCES groups(id),
	acl_type varchar(128) CHECK (acl_type::varchar = ANY(ARRAY['read'::varchar, 'report'::varchar])),
	tenant text NOT NULL,
	UNIQUE (user_id, group_id)
);

COMMENT ON COLUMN group_user_acl.user_id IS 'Even though the type of the column is text, expected value is UUID';




---- QUEUE Tables ----

CREATE TABLE perf_ts_data_reports_queue (
	id bigserial PRIMARY KEY,
	class_id text NOT NULL,
	course_id text NOT NULL,
	kpi varchar(128) CHECK (kpi::varchar = ANY(ARRAY['performance'::varchar, 'timespent'::varchar])),
	tenant text NOT NULL,
	status varchar(128) CHECK (status::varchar = ANY(ARRAY['pending'::varchar, 'completed'::varchar])),
	content_source varchar(128) NOT NULL,
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	UNIQUE (class_id, kpi, content_source)
);

CREATE TABLE competency_data_reports_queue (
	id bigserial PRIMARY KEY,
	class_id text NOT NULL,
	tenant text NOT NULL,
	status varchar(128) CHECK (status::varchar = ANY(ARRAY['pending'::varchar, 'completed'::varchar])),
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	UNIQUE (class_id)
);


--- DATA Report Tables ----

CREATE TABLE class_performance_data_reports (
	id bigserial PRIMARY KEY,
	class_id text NOT NULL,
	collection_timespent bigint,
	assessment_timespent bigint,
	assessment_performance numeric (5,2),
	school_id bigint REFERENCES school(id),
	state_id bigint REFERENCES state(id),
	country_id bigint REFERENCES country(id),
	month int,
	year int,
	content_source varchar(128) NOT NULL,
	tenant text NOT NULL,
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	UNIQUE(class_id, content_source, month, year)
);

COMMENT ON COLUMN class_performance_data_reports.class_id IS 'Even though the type of the column is text, expected value is UUID';
COMMENT ON COLUMN class_performance_data_reports.tenant IS 'Even though the type of the column is text, expected value is UUID';

CREATE TABLE group_performance_data_reports (
	id bigserial PRIMARY KEY,
	collection_timespent bigint,
	assessment_timespent bigint,
	assessment_performance numeric (5,2),
	group_id bigint REFERENCES groups(id),
	school_id bigint REFERENCES school(id),
	state_id bigint REFERENCES state(id),
	country_id bigint REFERENCES country(id),
	month int,
	year int,
	content_source varchar(128) NOT NULL,
	tenant text NOT NULL,
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	UNIQUE(group_id, content_source, month, year)
);

CREATE TABLE class_competency_data_reports (
	id bigserial PRIMARY KEY,
	class_id text NOT NULL,
	completed_count bigint,
	inprogress_count bigint,
	cumulative_completed_count bigint,
	school_id bigint REFERENCES school(id),
	state_id bigint REFERENCES state(id),
	country_id bigint REFERENCES country(id),
	month int,
	year int,
	tenant text NOT NULL,
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	UNIQUE(class_id, month, year)
);

CREATE TABLE group_competency_data_reports (
	id bigserial PRIMARY KEY,
	completed_count bigint,
	inprogress_count bigint,
	cumulative_completed_count bigint,
	group_id bigint REFERENCES groups(id),
	school_id bigint REFERENCES school(id),
	state_id bigint REFERENCES state(id),
	country_id bigint REFERENCES country(id),
	month int,
	year int,
	tenant text NOT NULL,
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	UNIQUE(group_id, month, year)
);


CREATE TABLE group_client_data_reports (
	id bigserial PRIMARY KEY,
	total_students bigint,
	total_teachers bigint,
	total_others bigint,
	total_classes bigint,
	total_competencies_gained bigint,
	total_timespent bigint,
	total_activities_conducted bigint,
	total_navigator_courses bigint,
	country_id bigint REFERENCES country(id),
	month int,
	year int,
	tenant text,
	partner text,
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC')
);

 CREATE INDEX group_client_data_reports_country_idx ON group_client_data_reports USING btree (country_id);
 
 ALTER TABLE class_performance_data_reports ALTER COLUMN tenant DROP NOT NULL;
 ALTER TABLE class_competency_data_reports ALTER COLUMN tenant DROP NOT NULL;
