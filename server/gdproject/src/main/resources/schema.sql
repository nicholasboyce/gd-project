CREATE TABLE land_record
(
   "VALUE"  NUMBER NOT NULL DEFAULT 0,
   "OWNER"  VARCHAR(256) NOT NULL,
   "YEAR"   NUMBER NOT NULL DEFAULT 2023,
   BOOK     NUMBER NOT NULL DEFAULT 1,
   "ADDRESS" VARCHAR(256) NOT NULL,
   ID       BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY
);
