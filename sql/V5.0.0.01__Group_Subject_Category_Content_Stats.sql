CREATE TABLE group_client_subject_distribution_reports
(
	  id bigserial NOT NULL,
	  tx_subject_code text NOT NULL,
	  tx_sub_category_code text NOT NULL,
	  total_count bigint NOT NULL DEFAULT 0,
	  tenant text,
	  tenant_root text,
	  partner text,
	  created_at timestamp without time zone NOT NULL DEFAULT timezone('UTC'::text, now()),
	  updated_at timestamp without time zone NOT NULL DEFAULT timezone('UTC'::text, now()),
	  CONSTRAINT gcsdr_pkey PRIMARY KEY (id)
);

CREATE TABLE group_client_content_distribution_reports
(
	  id bigserial NOT NULL,
	  content_type text NOT NULL,
	  total_count bigint NOT NULL DEFAULT 0,
	  tenant text,
	  tenant_root text,
	  partner text,
	  created_at timestamp without time zone NOT NULL DEFAULT timezone('UTC'::text, now()),
	  updated_at timestamp without time zone NOT NULL DEFAULT timezone('UTC'::text, now()),
	  CONSTRAINT gccdsr_pkey PRIMARY KEY (id),
      CONSTRAINT gccdsr_content_type_check CHECK (((content_type)::text = ANY (ARRAY['assessment'::text, 'collection'::text, 'assessment-external'::text, 'question'::text, 'resource'::text, 'collection-external'::text, 'offline-activity'::text, 'course'::text])))
);


CREATE TABLE group_client_content_usage_reports
(
	  id bigserial NOT NULL,
	  content_type text NOT NULL,
	  total_count bigint NOT NULL DEFAULT 0,
	  country_id bigint,
	  month integer,
	  year integer,
	  tenant text,
	  tenant_root text,
	  partner text,
	  created_at timestamp without time zone NOT NULL DEFAULT timezone('UTC'::text, now()),
	  updated_at timestamp without time zone NOT NULL DEFAULT timezone('UTC'::text, now()),
	  CONSTRAINT gccur_pkey PRIMARY KEY (id),
	  CONSTRAINT gccur_content_type_check CHECK (((content_type)::text = ANY (ARRAY['assessment'::text, 'collection'::text, 'assessment-external'::text, 'question'::text, 'resource'::text, 'collection-external'::text, 'offline-activity'::text, 'course'::text]))),
	  CONSTRAINT gccur_country_id_fkey FOREIGN KEY (country_id)
	      REFERENCES public.country (id) MATCH SIMPLE
	      ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE group_client_data_reports  ADD COLUMN tenant_root text;
