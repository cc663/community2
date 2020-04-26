alter table QUESTION alter column creator bigint default not null;
alter table  COMMENT alter column COMMENATOR bigint default not null;
alter table QUESTION alter column ID BIGINT auto_increment;
alter table USER alter column ID BIGINT auto_increment;
