create table USER
(
    ID INTEGER auto_increment,
    NAME VARCHAR2(50),
    ACCOUNT_ID VARCHAR2(100),
    TOKEN VARCHAR2(50),
    GMT_CREATE BIGINT,
    GMT_MODIFIED BIGINT,
    constraint USER_PK
        primary key (ID)
);

create unique index USER_ID_UINDEX
    on USER (ID);