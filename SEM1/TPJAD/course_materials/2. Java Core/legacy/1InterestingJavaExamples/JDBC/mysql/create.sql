use test;

drop table angajati;
drop table sectii;

create table sectii (
id int auto_increment primary key,
nume varchar(20) unique,
descriere varchar(250)
);

create table angajati (
id int auto_increment primary key,
nume varchar(20) unique,
varsta int,
sectia int,
caracterizare varchar(100),
foreign key (sectia) references sectii(id)
);