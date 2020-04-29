CREATE TABLE group_user_acl (
	id bigserial PRIMARY KEY,
	user_id text NOT NULL,
	type varchar(128) NOT NULL CHECK (type::varchar = ANY(ARRAY['school_district'::varchar, 'district'::varchar, 'block'::varchar, 'cluster'::varchar, 'school'::varchar, 'country'::varchar, 'state'::varchar])),
	groups jsonb,
	parent_reference_id bigint,
	tenant text NOT NULL,
	tenant_root text,
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	UNIQUE (user_id, type)
);

CREATE TABLE tenant_user_acl (
	id bigserial PRIMARY KEY,
	user_id uuid NOT NULL,
	tenant uuid NOT NULL,
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	UNIQUE (user_id, tenant)	
);


CREATE TABLE flexible_groups (
	id bigserial PRIMARY KEY,
	name text NOT NULL,
	code text,
	description text,
	type varchar(128) NOT NULL CHECK (type::varchar = ANY(ARRAY['school_district'::varchar, 'district'::varchar, 'block'::varchar, 'cluster'::varchar, 'school'::varchar, 'country'::varchar, 'state'::varchar])),
	parent_id bigint,
	reference_id text,
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC')
);

INSERT INTO flexible_groups (name, code, type, parent_id, reference_id) SELECT name, code, 'country', null, null  FROM country_ds;
INSERT INTO flexible_groups (name, code, type, parent_id, reference_id) SELECT name, code, 'state', country_id, null FROM state_ds;

CREATE TABLE temp_state_map AS SELECT fg.id AS new_id, sd.id AS old_id, fg.name AS new_name, sd.name AS old_name FROM flexible_groups fg, state_ds sd WHERE fg.code = sd.code AND fg.type = 'state';

INSERT INTO flexible_groups (name, code, type, parent_id, reference_id) SELECT name, code, 'school_district', (SELECT tmp.new_id FROM temp_state_map tmp WHERE tmp.old_id = g.state_id), reference_id FROM groups g WHERE g.sub_type = 'school_district';

CREATE TABLE temp_sd_map AS SELECT fg.id AS new_id, g.id AS old_id, fg.name as new_name, g.name AS old_name FROM flexible_groups fg, groups g WHERE fg.code = g.code AND fg.type = 'school_district';

ALTER TABLE group_school_mapping add column new_parent bigint;

UPDATE group_school_mapping SET new_parent = fg.id FROM flexible_groups fg, groups g WHERE fg.code = g.code AND g.id = group_school_mapping.group_id;

INSERT INTO flexible_groups (name, code, type, parent_id, reference_id) SELECT sch.name, sch.code, 'school', null , sch.reference_id FROM school_ds sch WHERE sch.id IN (SELECT school_id FROM group_school_mapping WHERE group_id IN (SELECT g.id FROM groups g WHERE g.sub_type='school_district'));

INSERT INTO flexible_groups (name, code, type, parent_id, reference_id) SELECT name, code, 'district', (SELECT tmp.new_id FROM temp_state_map tmp WHERE tmp.old_id = g.state_id), reference_id FROM groups g WHERE g.sub_type = 'district';

CREATE TABLE tmp_district_map AS SELECT fg.id AS new_id, g.id AS old_id, fg.code AS new_code, g.code AS old_code, fg.name AS new_name, g.name AS old_name FROM flexible_groups fg, groups g WHERE fg.code = g.code AND fg.type = 'district';

INSERT INTO flexible_groups (name, code, type, parent_id, reference_id) SELECT name, code, 'block', (SELECT tmp.new_id FROM tmp_district_map tmp WHERE tmp.old_id = g.parent_id), reference_id FROM groups g WHERE g.sub_type = 'block';

CREATE TABLE tmp_block_map AS SELECT fg.id AS new_id, g.id AS old_id, fg.code AS new_code, g.code AS old_code, fg.name AS new_name, g.name AS old_name FROM flexible_groups fg, groups g WHERE fg.code = g.code AND fg.type = 'block';

INSERT INTO flexible_groups (name, code, type, parent_id, reference_id) SELECT name, code, 'cluster', (SELECT tmp.new_id FROM tmp_block_map tmp WHERE tmp.old_id = g.parent_id), reference_id FROM groups g WHERE g.sub_type = 'cluster';

CREATE TABLE tmp_cluster_map AS SELECT fg.id AS new_id, g.id AS old_id, fg.code AS new_code, g.code AS old_code, fg.name AS new_name, g.name AS old_name FROM flexible_groups fg, groups g WHERE fg.code = g.code AND fg.type = 'cluster';

INSERT INTO flexible_groups (name, code, type, parent_id, reference_id) SELECT sch.name, sch.code, 'school', null , sch.reference_id FROM school_ds sch WHERE sch.id IN (SELECT school_id FROM group_school_mapping WHERE group_id IN (SELECT g.id FROM groups g WHERE g.sub_type='cluster'));

UPDATE group_school_mapping SET new_parent = fg.id FROM flexible_groups fg, groups g WHERE fg.code = g.code AND g.id = group_school_mapping.group_id AND new_parent is null;

CREATE TABLE temp_school_map AS SELECT fg.id AS new_id, sch.id AS old_id, fg.code AS new_code, sch.code AS old_code, fg.name AS new_name, sch.name as old_name FROM flexible_groups fg, school_ds sch WHERE fg.code = sch.code AND fg.type = 'school';
UPDATE flexible_groups SET parent_id = gsm.new_parent FROM group_school_mapping gsm, temp_school_map tmp WHERE gsm.school_id = tmp.old_id and tmp.new_code = flexible_groups.code;

/* LAST Steps */

ALTER TABLE class ADD COLUMN group_id BIGINT REFERENCES flexible_groups (id);
update class SET group_id = tmp.new_id FROM temp_school_map tmp WHERE tmp.old_id = class.school_id;

/* DROP temp tables */
DROP TABLE temp_state_map;
DROP TABLE temp_sd_map;
DROP TABLE tmp_district_map;
DROP TABLE tmp_block_map;
DROP TABLE tmp_cluster_map;

/* Gruops table migration */
create table groups_backup as select * from groups;
create table group_school_mapping_backup as select * from group_school_mapping;

drop table group_school_mapping;
drop table groups;

CREATE TABLE groups (
	id bigserial PRIMARY KEY,
	name text NOT NULL,
	code text,
	description text,
	type varchar(128) NOT NULL CHECK (type::varchar = ANY(ARRAY['school_district'::varchar, 'district'::varchar, 'block'::varchar, 'cluster'::varchar, 'school'::varchar, 'country'::varchar, 'state'::varchar])),
	parent_id bigint,
	tenant text NOT NULL,
	tenant_root text,
	hierarchy_id bigint NOT NULL REFERENCES group_hierarchy(id),
	creator_id text NOT NULL,	
	modifier_id text NOT NULL,
	reference_id text,
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC')	
);

insert into group_hierarchy_mapping (group_id, hierarchy_id, type, parent_id, tenant, tenant_root) values (101, 1, 'country', null, '8f5b6520-0932-4c53-8be9-4b70f0119937', null);
insert into group_hierarchy_mapping (group_id, hierarchy_id, type, parent_id, tenant, tenant_root) values (50, 1, 'state', 101, '8f5b6520-0932-4c53-8be9-4b70f0119937', null);
insert into group_hierarchy_mapping (group_id, hierarchy_id, type, parent_id, tenant, tenant_root) select id, 1, sub_type, state_id, '8f5b6520-0932-4c53-8be9-4b70f0119937', null from groups where tenant = '8f5b6520-0932-4c53-8be9-4b70f0119937' and sub_type = 'district';
insert into group_hierarchy_mapping (group_id, hierarchy_id, type, parent_id, tenant, tenant_root) select id, 1, sub_type, parent_id, '8f5b6520-0932-4c53-8be9-4b70f0119937', null from groups where tenant = '8f5b6520-0932-4c53-8be9-4b70f0119937' and sub_type = 'block';
insert into group_hierarchy_mapping (group_id, hierarchy_id, type, parent_id, tenant, tenant_root) select id, 1, sub_type, parent_id, '8f5b6520-0932-4c53-8be9-4b70f0119937', null from groups where tenant = '8f5b6520-0932-4c53-8be9-4b70f0119937' and sub_type = 'cluster';
insert into group_hierarchy_mapping (group_id, hierarchy_id, type, parent_id, tenant, tenant_root) select school_id, 1, 'school', group_id, '8f5b6520-0932-4c53-8be9-4b70f0119937', null from group_school_mapping where group_id in (select id from groups where tenant = '8f5b6520-0932-4c53-8be9-4b70f0119937' and sub_type = 'cluster');


insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'country', '[101]', null, '8f5b6520-0932-4c53-8be9-4b70f0119937');
insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'state', '[50]', null, '8f5b6520-0932-4c53-8be9-4b70f0119937');

CREATE TABLE group_user_acl (
	id bigserial PRIMARY KEY,
	user_id text NOT NULL,
	type varchar(128) NOT NULL CHECK (type::varchar = ANY(ARRAY['school_district'::varchar, 'district'::varchar, 'block'::varchar, 'cluster'::varchar, 'school'::varchar, 'country'::varchar, 'state'::varchar, 'class'::varchar])),
	groups jsonb,
	parent_reference_id bigint,
	tenant text NOT NULL,
	tenant_root text,
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
);

insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'country', '[101]', null, '8f5b6520-0932-4c53-8be9-4b70f0119937');

insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'state', '[296]', 101, '8f5b6520-0932-4c53-8be9-4b70f0119937');

insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'district', '[224525, 224526]', 296 , '8f5b6520-0932-4c53-8be9-4b70f0119937');

insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'block', '[224702, 224794]', 224525, '8f5b6520-0932-4c53-8be9-4b70f0119937');
insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'block', '[224700, 224721]', 224526, '8f5b6520-0932-4c53-8be9-4b70f0119937');

insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'cluster', '[225012, 225013]', 224702, '8f5b6520-0932-4c53-8be9-4b70f0119937');
insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'cluster', '[225046, 225047]', 224794, '8f5b6520-0932-4c53-8be9-4b70f0119937');
insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'cluster', '[225282, 225283]', 224700, '8f5b6520-0932-4c53-8be9-4b70f0119937');
insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'cluster', '[225318, 225319]', 224721, '8f5b6520-0932-4c53-8be9-4b70f0119937');

insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'school', '[282105, 230862]', 225012  ,'8f5b6520-0932-4c53-8be9-4b70f0119937');
insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'school', '[285707, 290428]', 225013  ,'8f5b6520-0932-4c53-8be9-4b70f0119937');
insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'school', '[240862, 240902]', 225046  ,'8f5b6520-0932-4c53-8be9-4b70f0119937');
insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'school', '[283419]', 225047  ,'8f5b6520-0932-4c53-8be9-4b70f0119937');
insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'school', '[233327]', 225282  ,'8f5b6520-0932-4c53-8be9-4b70f0119937');
insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'school', '[287339]', 225283  ,'8f5b6520-0932-4c53-8be9-4b70f0119937');
insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'school', '[287890]', 225318  ,'8f5b6520-0932-4c53-8be9-4b70f0119937');
insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'school', '[]', 225319  ,'8f5b6520-0932-4c53-8be9-4b70f0119937');



insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'class', '["430401fa-8244-43b4-84d8-3ca585e76e3d", "cd9c540f-1570-43ae-9b07-b76974f3aab1"]', 282105, '8f5b6520-0932-4c53-8be9-4b70f0119937');
insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'class', '["c9cf7fa3-2227-4f85-a74e-c32ff7be82c6", "e8111f6e-79e6-4a6a-91ab-ea5296f6f9cd"]', 230862, '8f5b6520-0932-4c53-8be9-4b70f0119937');
insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'class', '["5e0d9259-4f3f-4a24-964f-2d92c1ca3c9c", "77293851-a9e1-412c-bf11-66c083ff4bbe"]', 285707, '8f5b6520-0932-4c53-8be9-4b70f0119937');
insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'class', '["88b71c62-e29c-4a14-b221-c8778b8a1235", "0c57d59d-4f92-4793-85a5-400083444a52"]', 290428, '8f5b6520-0932-4c53-8be9-4b70f0119937');
insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'class', '["2f1f89fe-11e1-48b7-ac0d-2f4d6087fd60", "73d0737c-167e-4248-9d33-79899def42d0"]', 240862, '8f5b6520-0932-4c53-8be9-4b70f0119937');
insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'class', '["82a6f829-0f3a-4c44-b06c-2a9e1814d4cf", "0ad3bca9-9a47-413e-a1a7-21c38333671b"]', 240902, '8f5b6520-0932-4c53-8be9-4b70f0119937');
insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'class', '["6d3e3a2f-51c2-449c-b970-7553f773c4ff", "6a3c7196-e0fc-4d8e-8e02-74b639432d50"]', 283419, '8f5b6520-0932-4c53-8be9-4b70f0119937');
insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'class', '["2f643615-4122-42e1-b0c3-b6d76c66a382", "03c641db-458d-4b2f-83fe-0ed738437995"]', 233327, '8f5b6520-0932-4c53-8be9-4b70f0119937');
insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'class', '["d6996c67-01de-4fa0-9af3-7314b419beee", "c6561880-ed19-4ee1-9817-1c5baf91c7ba"]', 287339, '8f5b6520-0932-4c53-8be9-4b70f0119937');
insert into group_user_acl (user_id, type, groups,parent_reference_id, tenant) values ('07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 'class', '["ce694d72-5772-4fd1-99e7-a16ed777553e", "246c33ad-8ca7-461f-a897-9fa72b1ca421"]', 287890, '8f5b6520-0932-4c53-8be9-4b70f0119937');

UPDATE class set group_id = 282105 WHERE id in ('430401fa-8244-43b4-84d8-3ca585e76e3d', 'cd9c540f-1570-43ae-9b07-b76974f3aab1');
UPDATE class set group_id = 230862 WHERE id in ('c9cf7fa3-2227-4f85-a74e-c32ff7be82c6', 'e8111f6e-79e6-4a6a-91ab-ea5296f6f9cd');
UPDATE class set group_id = 285707 WHERE id in ('5e0d9259-4f3f-4a24-964f-2d92c1ca3c9c', '77293851-a9e1-412c-bf11-66c083ff4bbe');
UPDATE class set group_id = 290428 WHERE id in ('88b71c62-e29c-4a14-b221-c8778b8a1235', '0c57d59d-4f92-4793-85a5-400083444a52');
UPDATE class set group_id = 240862 WHERE id in ('2f1f89fe-11e1-48b7-ac0d-2f4d6087fd60', '73d0737c-167e-4248-9d33-79899def42d0');
UPDATE class set group_id = 240902 WHERE id in ('82a6f829-0f3a-4c44-b06c-2a9e1814d4cf', '0ad3bca9-9a47-413e-a1a7-21c38333671b');
UPDATE class set group_id = 283419 WHERE id in ('6d3e3a2f-51c2-449c-b970-7553f773c4ff', '6a3c7196-e0fc-4d8e-8e02-74b639432d50');
UPDATE class set group_id = 233327 WHERE id in ('2f643615-4122-42e1-b0c3-b6d76c66a382', '03c641db-458d-4b2f-83fe-0ed738437995');
UPDATE class set group_id = 287339 WHERE id in ('d6996c67-01de-4fa0-9af3-7314b419beee', 'c6561880-ed19-4ee1-9817-1c5baf91c7ba');
UPDATE class set group_id = 287890 WHERE id in ('ce694d72-5772-4fd1-99e7-a16ed777553e', '246c33ad-8ca7-461f-a897-9fa72b1ca421');

alter table group_hierarchy_details drop constraint group_hierarchy_details_type_check, add constraint group_hierarchy_details_type_check CHECK (type::character varying::text = ANY (ARRAY['school_district'::character varying, 'district'::character varying, 'block'::character varying, 'cluster'::character varying, 'school'::character varying, 'country'::character varying, 'state'::character varying, 'class'::character varying]::text[]));

insert into group_hierarchy_details(name, type, hierarchy_id, sequence) values ('Class', 'class', 1, 7);

insert into group_hierarchy_details(name, type, hierarchy_id, sequence) values ('Class', 'class', 2, 5);

insert into group_hierarchy_details(name, type, hierarchy_id, sequence) values ('Class', 'class', 3, 3);

 insert into class (title, creator_id, modifier_id, gooru_version,tenant, code) values('test-class-1', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 3, '8f5b6520-0932-4c53-8be9-4b70f0119937', 'XPLHCV7');
 insert into class (title, creator_id, modifier_id, gooru_version,tenant, code) values('test-class-2', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 3, '8f5b6520-0932-4c53-8be9-4b70f0119937', '8YFXZJL');
 insert into class (title, creator_id, modifier_id, gooru_version,tenant, code) values('test-class-3', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 3, '8f5b6520-0932-4c53-8be9-4b70f0119937', 'QZFJ8G5');
 insert into class (title, creator_id, modifier_id, gooru_version,tenant, code) values('test-class-4', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 3, '8f5b6520-0932-4c53-8be9-4b70f0119937', 'PXH9JCI');
 insert into class (title, creator_id, modifier_id, gooru_version,tenant, code) values('test-class-5', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 3, '8f5b6520-0932-4c53-8be9-4b70f0119937', 'VFN962Z');
 insert into class (title, creator_id, modifier_id, gooru_version,tenant, code) values('test-class-6', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 3, '8f5b6520-0932-4c53-8be9-4b70f0119937', 'C8JQPCZ');
 insert into class (title, creator_id, modifier_id, gooru_version,tenant, code) values('test-class-7', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 3, '8f5b6520-0932-4c53-8be9-4b70f0119937', '9C7NSTM');
 insert into class (title, creator_id, modifier_id, gooru_version,tenant, code) values('test-class-8', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 3, '8f5b6520-0932-4c53-8be9-4b70f0119937', 'ELRXC3S');
 insert into class (title, creator_id, modifier_id, gooru_version,tenant, code) values('test-class-9', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 3, '8f5b6520-0932-4c53-8be9-4b70f0119937', 'IW9PQ8N');
 insert into class (title, creator_id, modifier_id, gooru_version,tenant, code) values('test-class-10', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 3, '8f5b6520-0932-4c53-8be9-4b70f0119937', 'L2YRXCV');
 insert into class (title, creator_id, modifier_id, gooru_version,tenant, code) values('test-class-11', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 3, '8f5b6520-0932-4c53-8be9-4b70f0119937', 'UHLT92R');
 insert into class (title, creator_id, modifier_id, gooru_version,tenant, code) values('test-class-12', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 3, '8f5b6520-0932-4c53-8be9-4b70f0119937', 'IKSGE62');
 insert into class (title, creator_id, modifier_id, gooru_version,tenant, code) values('test-class-13', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 3, '8f5b6520-0932-4c53-8be9-4b70f0119937', 'UPLAQKN');
 insert into class (title, creator_id, modifier_id, gooru_version,tenant, code) values('test-class-14', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 3, '8f5b6520-0932-4c53-8be9-4b70f0119937', 'OASQXDE');
 insert into class (title, creator_id, modifier_id, gooru_version,tenant, code) values('test-class-15', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 3, '8f5b6520-0932-4c53-8be9-4b70f0119937', '56NQEJ5');
 insert into class (title, creator_id, modifier_id, gooru_version,tenant, code) values('test-class-16', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 3, '8f5b6520-0932-4c53-8be9-4b70f0119937', 'GQNJHME');
 insert into class (title, creator_id, modifier_id, gooru_version,tenant, code) values('test-class-17', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 3, '8f5b6520-0932-4c53-8be9-4b70f0119937', 'VLLQNUE');
 insert into class (title, creator_id, modifier_id, gooru_version,tenant, code) values('test-class-18', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 3, '8f5b6520-0932-4c53-8be9-4b70f0119937', 'UGIPSX6');
 insert into class (title, creator_id, modifier_id, gooru_version,tenant, code) values('test-class-19', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 3, '8f5b6520-0932-4c53-8be9-4b70f0119937', 'JBZ9ZTT');
 insert into class (title, creator_id, modifier_id, gooru_version,tenant, code) values('test-class-20', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', '07cb9876-9c4b-4b25-9f8a-33f9d4d0a4a1', 3, '8f5b6520-0932-4c53-8be9-4b70f0119937', 'OMN7WCG');

 
 
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('430401fa-8244-43b4-84d8-3ca585e76e3d', 50, 23, 12, 120, 16, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('cd9c540f-1570-43ae-9b07-b76974f3aab1', 34, 56, 12, 234, 16, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('c9cf7fa3-2227-4f85-a74e-c32ff7be82c6', 12, 65, 34, 123, 16, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('e8111f6e-79e6-4a6a-91ab-ea5296f6f9cd', 56, 23, 23, 345, 16, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('77293851-a9e1-412c-bf11-66c083ff4bbe', 67, 87, 23, 129, 16, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('5e0d9259-4f3f-4a24-964f-2d92c1ca3c9c', 11, 34, 12, 78, 16, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('0c57d59d-4f92-4793-85a5-400083444a52', 89, 78, 45, 82, 16, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('88b71c62-e29c-4a14-b221-c8778b8a1235', 101, 12, 12, 123, 16, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('2f1f89fe-11e1-48b7-ac0d-2f4d6087fd60', 33, 123, 45, 34, 16, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('73d0737c-167e-4248-9d33-79899def42d0', 56, 45, 21, 54 , 16, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('82a6f829-0f3a-4c44-b06c-2a9e1814d4cf', 76, 67, 32, 129, 16, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('0ad3bca9-9a47-413e-a1a7-21c38333671b', 12, 23, 43, 238, 16, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('6d3e3a2f-51c2-449c-b970-7553f773c4ff', 56, 78, 12, 32, 16, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('6a3c7196-e0fc-4d8e-8e02-74b639432d50', 89, 345, 45, 45, 16, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('2f643615-4122-42e1-b0c3-b6d76c66a382', 22, 34, 32, 45, 16, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('03c641db-458d-4b2f-83fe-0ed738437995', 90, 77, 12, 43, 16, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('c6561880-ed19-4ee1-9817-1c5baf91c7ba', 17, 123, 32, 67, 16, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('d6996c67-01de-4fa0-9af3-7314b419beee', 27, 65, 21, 12, 16, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('ce694d72-5772-4fd1-99e7-a16ed777553e', 92, 12, 1, 78, 16, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('246c33ad-8ca7-461f-a897-9fa72b1ca421', 12, 67, 2, 34, 16, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);

 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('430401fa-8244-43b4-84d8-3ca585e76e3d', 50, 23, 12, 120, 17, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('cd9c540f-1570-43ae-9b07-b76974f3aab1', 34, 56, 12, 234, 17, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('c9cf7fa3-2227-4f85-a74e-c32ff7be82c6', 12, 65, 34, 123, 17, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('e8111f6e-79e6-4a6a-91ab-ea5296f6f9cd', 56, 23, 23, 345, 17, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('77293851-a9e1-412c-bf11-66c083ff4bbe', 67, 87, 23, 129, 17, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('5e0d9259-4f3f-4a24-964f-2d92c1ca3c9c', 11, 34, 12, 78, 17, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('0c57d59d-4f92-4793-85a5-400083444a52', 89, 78, 45, 82, 17, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('88b71c62-e29c-4a14-b221-c8778b8a1235', 101, 12, 12, 123, 17, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('2f1f89fe-11e1-48b7-ac0d-2f4d6087fd60', 33, 123, 45, 34, 17, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('73d0737c-167e-4248-9d33-79899def42d0', 56, 45, 21, 54 , 17, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('82a6f829-0f3a-4c44-b06c-2a9e1814d4cf', 76, 67, 32, 129, 17, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('0ad3bca9-9a47-413e-a1a7-21c38333671b', 12, 23, 43, 238, 17, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('6d3e3a2f-51c2-449c-b970-7553f773c4ff', 56, 78, 12, 32, 17, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('6a3c7196-e0fc-4d8e-8e02-74b639432d50', 89, 345, 45, 45, 17, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('2f643615-4122-42e1-b0c3-b6d76c66a382', 22, 34, 32, 45, 17, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('03c641db-458d-4b2f-83fe-0ed738437995', 90, 77, 12, 43, 17, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('c6561880-ed19-4ee1-9817-1c5baf91c7ba', 17, 123, 32, 67, 17, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('d6996c67-01de-4fa0-9af3-7314b419beee', 27, 65, 21, 12, 17, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('ce694d72-5772-4fd1-99e7-a16ed777553e', 92, 12, 1, 78, 17, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);
 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values('246c33ad-8ca7-461f-a897-9fa72b1ca421', 12, 67, 2, 34, 17, 4, 2020, '8f5b6520-0932-4c53-8be9-4b70f0119937', null, 'K12.MA', 'CCSS', 45);

 insert into class_competency_base_reports_weekly (class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month, year, tenant, tenant_root, subject, framework, grade) values(