SELECT * FROM newledgerapi.entry;
alter table entry 
add column vendor varchar (255) NULL;

alter table entry 
add column amount varchar (255) null;