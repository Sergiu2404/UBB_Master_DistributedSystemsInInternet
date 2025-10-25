use test;

drop table angajat;
drop table sectie;

create table sectie (
id int auto_increment primary key,
nume varchar(20) unique,
descriere varchar(250)
);

create table angajat (
id int auto_increment primary key,
nume varchar(20) unique,
varsta int,
sectie_id int, 
caracterizare varchar(100)
);
