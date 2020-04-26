alter table QUESTION alter column ID long auto_increment;
drop index PRIMARY_KEY_C9;

alter table USER alter column ID long auto_increment;
drop index USER_ID_UINDEX;