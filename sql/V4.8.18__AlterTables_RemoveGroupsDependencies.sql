alter table class_performance_data_reports drop constraint class_performance_data_reports_country_id_fkey;
alter table class_performance_data_reports drop constraint class_performance_data_reports_school_id_fkey;
alter table class_performance_data_reports drop constraint class_performance_data_reports_state_id_fkey;

alter table group_performance_data_reports drop constraint group_performance_data_reports_country_id_fkey;
alter table group_performance_data_reports drop constraint group_performance_data_reports_group_id_fkey;
alter table group_performance_data_reports drop constraint group_performance_data_reports_school_id_fkey;
alter table group_performance_data_reports drop constraint group_performance_data_reports_state_id_fkey;

alter table class_competency_data_reports drop constraint class_competency_data_reports_country_id_fkey;
alter table class_competency_data_reports drop constraint class_competency_data_reports_school_id_fkey;
alter table class_competency_data_reports drop constraint class_competency_data_reports_state_id_fkey;

alter table group_competency_data_reports drop constraint group_competency_data_reports_country_id_fkey;
alter table group_competency_data_reports drop constraint group_competency_data_reports_group_id_fkey;
alter table group_competency_data_reports drop constraint group_competency_data_reports_school_id_fkey;
alter table group_competency_data_reports drop constraint group_competency_data_reports_state_id_fkey;