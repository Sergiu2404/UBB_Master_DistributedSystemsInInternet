create database dbexemplu;
use dbexemplu

drop table angajati;
drop table sectii;

create table sectii (
id int IDENTITY(1,1) primary key clustered,
nume varchar(20) unique,
descriere varchar(250)
);

create table angajati (
id int IDENTITY(1,1) primary key clustered,
nume varchar(20) unique,
varsta int,
sectia int,
caracterizare varchar(100),
foreign key (sectia) references sectii(id)
);
