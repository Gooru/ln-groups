CREATE TABLE group_client_subject_data_reports
(
	  id bigserial NOT NULL,
	  tx_subject_code text NOT NULL,
	  tx_sub_category_code text NOT NULL,
	  total_count bigint NOT NULL DEFAULT 0,
	  country_id bigint,
	  month integer,
	  year integer,
	  tenant text,
	  partner text,
	  created_at timestamp without time zone NOT NULL DEFAULT timezone('UTC'::text, now()),
	  updated_at timestamp without time zone NOT NULL DEFAULT timezone('UTC'::text, now()),
	  CONSTRAINT gcsdr_pkey PRIMARY KEY (id),
	  CONSTRAINT gcsdr_country_id_fkey FOREIGN KEY (country_id)
	      REFERENCES public.country (id) MATCH SIMPLE
	      ON UPDATE NO ACTION ON DELETE NO ACTION
);


CREATE TABLE group_client_content_data_reports
(
	  id bigserial NOT NULL,
	  content_type text NOT NULL,
	  total_count bigint NOT NULL DEFAULT 0,
	  country_id bigint,
	  month integer,
	  year integer,
	  tenant text,
	  partner text,
	  created_at timestamp without time zone NOT NULL DEFAULT timezone('UTC'::text, now()),
	  updated_at timestamp without time zone NOT NULL DEFAULT timezone('UTC'::text, now()),
	  CONSTRAINT gccdr_pkey PRIMARY KEY (id),
	  CONSTRAINT gccdr_content_type_check CHECK (((content_type)::text = ANY (ARRAY['assessment'::text, 'collection'::text, 'assessment-external'::text, 'question'::text, 'resource'::text, 'collection-external'::text, 'offline-activity'::text, 'course'::text]))),
	  CONSTRAINT gccdr_country_id_fkey FOREIGN KEY (country_id)
	      REFERENCES public.country (id) MATCH SIMPLE
	      ON UPDATE NO ACTION ON DELETE NO ACTION
);

