create table sectii (
id integer primary key autoincrement,
nume varchar(20) unique,
descriere varchar(250)
);

create table angajati (
id integer primary key autoincrement,
nume varchar(20) unique,
varsta integer,
sectia integer,
caracterizare varchar(100),
foreign key (sectia) references sectii(id)
);
