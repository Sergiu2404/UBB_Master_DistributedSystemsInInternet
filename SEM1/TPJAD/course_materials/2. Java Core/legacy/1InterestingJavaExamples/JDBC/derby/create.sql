connect 'jdbc:derby:dbexemplu;create=true';
-- connect 'jdbc:derby:dbexemplu'; La nevoie, acest sir se copiaza si se da ca prima comanda la utilitarul ij

create table sectii (
id integer not null generated always as identity primary key,
nume varchar(20) unique,
descriere varchar(250)
);

create table angajati (
id integer not null generated always as identity primary key,
nume varchar(20) unique,
varsta integer,
sectia integer,
caracterizare varchar(100),
foreign key (sectia) references sectii(id)
);
