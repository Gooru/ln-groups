INSERT INTO permission(code, display_name) values ('REPORT_GROUP_GET', 'Permission to fetch group report'), ('REPORT_GROUP_COMPETENCY_DRILLDOWN', 'Permission to fetch competency drill down report'), ('REPORT_GROUP_PERFORMANCE_DRILLDOWN', 'Permission to fetch performance drill down report');

CREATE TABLE group_user_acl (
	id bigserial PRIMARY KEY,
	user_id text NOT NULL,
	type varchar(128) NOT NULL CHECK (type::varchar = ANY(ARRAY['school_district'::varchar, 'district'::varchar, 'block'::varchar, 'cluster'::varchar, 'school'::varchar, 'country'::varchar, 'state'::varchar])),
	groups jsonb,
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC')	
);
