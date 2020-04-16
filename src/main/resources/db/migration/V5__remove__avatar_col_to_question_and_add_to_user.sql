alter table QUESTION drop column AVATAR_URL;

alter table user
	add avatar_url varchar2(100);