CREATE TABLE land_record
(
   "VALUE"  NUMBER NOT NULL DEFAULT 0,
   "OWNER"  VARCHAR(256) NOT NULL,
   "YEAR"   NUMBER NOT NULL DEFAULT 2023,
   BOOK     NUMBER NOT NULL DEFAULT 1,
   "ADDRESS" VARCHAR(256) NOT NULL,
   ID       BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY
);

CREATE TABLE app_user (
   ID    BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
   "ADDRESS" VARCHAR(256) NOT NULL,
   BUSINESS_TYPE VARCHAR(256),
   CITY VARCHAR(256) NOT NULL,
   COMPANY VARCHAR(256) NOT NULL,
   COUNTRY VARCHAR(2) NOT NULL,
   EMAIL VARCHAR(256) NOT NULL,
   FIRST_NAME VARCHAR(256) NOT NULL,
   LAST_NAME VARCHAR(256) NOT NULL,
   HOME VARCHAR(256) NOT NULL,
   MOBILE VARCHAR(256) NOT NULL,
   PASSWORD VARCHAR(256) NOT NULL,
   JOB VARCHAR(256) NOT NULL,
   ZIP NUMBER NOT NULL DEFAULT 11111
);

CREATE TABLE APP_USER_ROLES (
   ID    BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
   APP_USER  BIGINT NOT NULL,
   APP_USER_KEY  VARCHAR(256) NOT NULL,
   "ROLE" VARCHAR(256) NOT NULL,
   EMAIL VARCHAR(256) NOT NULL,
   CONSTRAINT FK_USERS_ROLE FOREIGN KEY(APP_USER) references APP_USER(ID)
);

-- CREATE TABLE SIMPLE_GRANTED_AUTHORITY (
--    APP_USER VARCHAR(256) NOT NULL,
--    APP_USER_KEY VARCHAR(256) NOT NULL,
--    "ROLE" VARCHAR(256) NOT NULL,
--    CONSTRAINT FK_GRANTED_AUTH_USERS FOREIGN KEY(APP_USER) references APP_USER(ID)
-- );

create table users(
	username varchar_ignorecase(50) not null primary key,
	password varchar_ignorecase(500) not null,
	enabled boolean not null
);

create table authorities (
	username varchar_ignorecase(50) not null,
	authority varchar_ignorecase(50) not null,
	constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username,authority);