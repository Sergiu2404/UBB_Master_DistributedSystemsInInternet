drop table angajati;
drop table sectii;
drop sequence sectii_seq;
drop sequence angajati_seq;

create table sectii (
id int primary key,
nume varchar2(20) unique,
descriere varchar2(250)
);

create table angajati (
id int primary key,
nume varchar2(20) unique,
varsta int,
sectia int,
caracterizare varchar2(100),
foreign key (sectia) references sectii(id)
);

create sequence sectii_seq;

create trigger sectii_trigger
before insert on sectii
for each row
begin
  select sectii_seq.nextval into :new.id from dual;
end;
/

create sequence angajati_seq;

create trigger angajati_trigger
before insert on angajati
for each row
begin
  select angajati_seq.nextval into :new.id from dual;
end;
/

