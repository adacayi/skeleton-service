--liquibase formatted sql
--changeset asanver:1

create table user (
    user_id uuid primary key,
    name varchar(30),
    last_name varchar(30),
    date_of_birth date
);

--changeset asanver:2

INSERT INTO user (user_id,name,last_name,date_of_birth) values('f149b744-39eb-4050-8b35-c6650fe6c242','Ahmet','Salih','1985-06-01');
INSERT INTO user (user_id,name,last_name,date_of_birth) values('ad3b5a0c-6e99-4e10-9de8-cc8fed135c6b','Kasim','Ahmet','2005-12-13');