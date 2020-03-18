- There will be multiple hierarchies defined in the system which will then be used by the tenants. We will maintain the hierarchies and tenant mapping. Leaf will always be a class.
- groups table will be enhanced to contain the flexible hierarchy as per the tenant
- Class can have multiple type of parents group which will be stored in the class table (or may be other mapping table). Whenever class is created, parent group can be inferred from the teacher of the class. How teacher will be assigned group to be associated with class?? 
- Data for the uncategorised classes will be then aggregated in other type of group type and will be displayed at FE separately. 
- At the write path, we will need to refer to tenant hierarchy and apply the aggregation logic based on the class (leaf) level data
- At read time, refer to the tenant hierarchy OR user selected hierarchy in which they want to see the data. Now check the group access to the user and fetch the data accordingly. 
- For the migration, we need to update the aggregated reports based on the updated groups table where the internal group ids will be changed
- Oneroster will also be updated to map the classes under the tenant defined hierarchy. We will still use the school information coming from oneroster even if the school is not parent of the class. School can be at any level in the hierarchy. Class will not be associated with the school and data will be then aggregated in other bucket. 


##Changes for SAP
- Schema to persist the different type of hierarchies. Start with currently supported hierarchies
- Update tenant settings to define the hierarchy used by the tenants
- Add country column in school table or have separate mapping of school to country to resolve the schools by country (specifically for SAP)
- Update write path:
	* Change the performance aggregation logic to use the tenant specified hierarchy and roll up 
	* Change the time spent aggregation logic to use the tenant specified hierarchy and roll up 
	* Create schema to store the user competency status per day
	* Change the competency count aggregation logic to persist date, user, competency and status details which will persist the competency status per day.
	* Add post processor for the competency aggregation to compute the weekly counts by the class and persist. Here we need to compute the class completions based on the class grade and users in the class
	* Change the group competency count aggregation logic to use the tenant specified hierarchy and roll up the data till the root node.
	
- Update Read APIs:
	* Based on the logged in user tenant, all groups read APIs will serve the data for specific tenant (unless it is super admin)
	* Update group performance read api to use the tenant specific hierarchy and return the data for the given node and its child
	* Update time spent read api to use the tenant specific hierarchy and return the data for the given node and its child
	* Update competency count api to use the tenant specific hierarchy and return the data for the given node and its child

- Migration
As we are not changing the group definitions and the way aggregated data is stored, there is no need of any migration for this.



###Implementation Details

#####DB Models

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

CREATE TABLE user_competency_data_reports ( 
	id bigserial PRIMARY KEY,
	user_id text NOT NULL,
	competency text NOT NULL,
	status int NOT NULL,
	created_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
	updated_at timestamp NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC')	
);

We will store the country and school mapping just to resolve the SAP hierarchy. This will be temporary table until we have flexible group hierarchy changes in place.

CREATE TABLE country_school_mapping (
	country_id bigint NOT NULL REFERENCES country_ds(id),
	school_id bigint NOT NULL REFERENCES school_ds(id),
	tenant text NOT NULL
);

Add new key in tenant_settings table 'applicable_group_hierarchy' which should be set to the corresponding hierarchy id from the group_hierarchy table.

#####DAP Changes

DAP will process the aggregation of the data based on the selected tenant hierarchy. There will be data aggregation processor for each hierarchy type and new processor will be added for new hierarchy.

##### Read API Changes

Reports APIs will also work based on the tenant hierarchy to return the data. We are not storing the aggregated counts for all the groups levels. Some of them are aggregated at read time.

##### User Competency Count Computation

We are planning to store the status of each competency for the user to arrive at different competency counts. This data will be stored on a day, user, competency basis where status will be updated for multiple activities on the same competency. From this table we will compute the daily competency counts of the users. which will then aggregated at class levels 

