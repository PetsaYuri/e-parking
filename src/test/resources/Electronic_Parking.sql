-- Adminer 4.8.1 PostgreSQL 14.3 dump

CREATE SEQUENCE booking_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;

CREATE TABLE "public"."booking" (
    "id" bigint DEFAULT nextval('booking_id_seq') NOT NULL,
    "days" integer NOT NULL,
    "end_booking" timestamp(6) NOT NULL,
    "hours" integer NOT NULL,
    "start_booking" timestamp(6) NOT NULL,
    "parking_lot_id" bigint,
    "user_id" bigint,
    "price" integer,
    CONSTRAINT "booking_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
INSERT INTO "booking" ("id", "days", "end_booking", "hours", "start_booking", "parking_lot_id", "user_id", "price") VALUES
(3,	2,	'2023-08-06 20:21:38',	0,	'2023-06-06 21:21:38',	NULL,	2,	10),
(4,	2,	'2023-09-08 20:21:38',	0,	'2023-06-06 21:21:38',	NULL,	2,	10),
(5,	2,	'2023-10-08 20:21:38',	0,	'2023-05-07 21:21:38',	NULL,	2,	10);

CREATE SEQUENCE cars_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;

CREATE TABLE "public"."cars" (
    "id" bigint DEFAULT nextval('cars_id_seq') NOT NULL,
    "color" character varying(255) NOT NULL,
    "image" character varying(255),
    "numbers" character varying(255) NOT NULL,
    "type" character(1) NOT NULL,
    "parking_id" bigint,
    "user_id" bigint,
    CONSTRAINT "cars_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
INSERT INTO "cars" ("id", "color", "image", "numbers", "type", "parking_id", "user_id") VALUES
(3,	'blue',	NULL,	'AO1234AB',	'C',	NULL,	2),
(4,	'blue',	NULL,	'AO1234AC',	'C',	NULL,	2),
(2,	'blue',	NULL,	'AO1234AA',	'C',	2,	2),
(6,	'red',	NULL,	'AO1235AC',	'C',	5,	5);

CREATE TABLE "public"."parking" (
    "id" bigint NOT NULL,
    "busy_end" timestamp(6),
    "busy_start" timestamp(6),
    "busy_days" integer NOT NULL,
    "busy_hours" integer NOT NULL,
    "is_available" boolean NOT NULL,
    "is_booking" boolean NOT NULL,
    "price_per_day" integer NOT NULL,
    "price_per_hour" integer,
    "promocode" character varying(255),
    "car_id" bigint,
    CONSTRAINT "parking_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
INSERT INTO "parking" ("id", "busy_end", "busy_start", "busy_days", "busy_hours", "is_available", "is_booking", "price_per_day", "price_per_hour", "promocode", "car_id") VALUES
(3,	NULL,	NULL,	0,	0,	'1',	'0',	1000,	50,	NULL,	NULL),
(4,	NULL,	NULL,	0,	0,	'1',	'0',	1000,	50,	NULL,	NULL),
(6,	NULL,	NULL,	0,	0,	'1',	'0',	1000,	50,	NULL,	NULL),
(7,	NULL,	NULL,	0,	0,	'1',	'0',	1000,	50,	NULL,	NULL),
(8,	NULL,	NULL,	0,	0,	'1',	'0',	1000,	50,	NULL,	NULL),
(9,	NULL,	NULL,	0,	0,	'1',	'0',	1000,	50,	NULL,	NULL),
(10,	NULL,	NULL,	0,	0,	'1',	'0',	1000,	50,	NULL,	NULL),
(11,	NULL,	NULL,	0,	0,	'1',	'0',	1000,	50,	NULL,	NULL),
(12,	NULL,	NULL,	0,	0,	'1',	'0',	1000,	50,	NULL,	NULL),
(13,	NULL,	NULL,	0,	0,	'1',	'0',	1000,	50,	NULL,	NULL),
(14,	NULL,	NULL,	0,	0,	'1',	'0',	1000,	50,	NULL,	NULL),
(15,	NULL,	NULL,	0,	0,	'1',	'0',	1000,	50,	NULL,	NULL),
(17,	NULL,	NULL,	0,	0,	'1',	'0',	1000,	50,	NULL,	NULL),
(18,	NULL,	NULL,	0,	0,	'1',	'0',	1000,	50,	NULL,	NULL),
(16,	NULL,	NULL,	0,	0,	'1',	'0',	1000,	50,	NULL,	NULL),
(1,	'2023-06-08 19:52:15.311',	'2023-06-07 17:52:15.311',	1,	2,	'0',	'1',	999,	45,	NULL,	2),
(5,	'2023-06-10 13:04:06.85',	'2023-06-08 11:04:06.85',	2,	2,	'0',	'0',	1000,	50,	NULL,	6),
(2,	'2023-06-08 19:52:20.257',	'2023-06-07 17:52:20.257',	1,	2,	'0',	'0',	1000,	50,	NULL,	2);

CREATE TABLE "public"."parking_booking_lot" (
    "parking_id" bigint NOT NULL,
    "booking_lot_id" bigint NOT NULL,
    CONSTRAINT "uk_qtdm1wj9eob9ohj2lurop7fu9" UNIQUE ("booking_lot_id")
) WITH (oids = false);


CREATE SEQUENCE promocodes_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;

CREATE TABLE "public"."promocodes" (
    "id" bigint DEFAULT nextval('promocodes_id_seq') NOT NULL,
    "amount" integer,
    "count" integer NOT NULL,
    "days" integer NOT NULL,
    "ends" timestamp(6),
    "percent" double precision,
    "title" character varying(255) NOT NULL,
    CONSTRAINT "promocodes_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
INSERT INTO "promocodes" ("id", "amount", "count", "days", "ends", "percent", "title") VALUES
(2,	0,	10,	1,	'2023-06-08 21:24:43.499',	25,	'4669e7'),
(3,	0,	10,	1,	'2023-06-06 22:38:28.122',	25,	'd758c4'),
(4,	0,	10,	1,	'2023-06-09 11:05:09.568',	25,	'2ebcda');

CREATE SEQUENCE reviews_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;

CREATE TABLE "public"."reviews" (
    "id" bigint DEFAULT nextval('reviews_id_seq') NOT NULL,
    "body" character varying(255) NOT NULL,
    "date" timestamp(6) NOT NULL,
    "grade" integer NOT NULL,
    "user_id" bigint,
    CONSTRAINT "reviews_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
INSERT INTO "reviews" ("id", "body", "date", "grade", "user_id") VALUES
(2,	'Все було чудово',	'2023-06-08 11:06:43.78',	5,	1);

CREATE SEQUENCE transactions_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;

CREATE TABLE "public"."transactions" (
    "id" bigint DEFAULT nextval('transactions_id_seq') NOT NULL,
    "car_color" character varying(255) NOT NULL,
    "car_numbers" character varying(255) NOT NULL,
    "date" timestamp(6) NOT NULL,
    "parking_ends" timestamp(6) NOT NULL,
    "parking_id" bigint NOT NULL,
    "price" integer NOT NULL,
    "price_with_promo_code" integer,
    "promo_code_title" character varying(255),
    "user_name" character varying(255) NOT NULL,
    "user_phone_number" character varying(255) NOT NULL,
    "car_id" bigint,
    "promo_code_id" bigint,
    "user_id" bigint,
    CONSTRAINT "transactions_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
INSERT INTO "transactions" ("id", "car_color", "car_numbers", "date", "parking_ends", "parking_id", "price", "price_with_promo_code", "promo_code_title", "user_name", "user_phone_number", "car_id", "promo_code_id", "user_id") VALUES
(1,	'blue',	'AO1234AA',	'2023-06-05 18:21:38.823',	'2023-06-06 20:21:38.823',	16,	1100,	0,	NULL,	'ddd tttt',	'099',	2,	NULL,	2),
(2,	'blue',	'AO1234AA',	'2023-06-06 15:06:09.44',	'2023-06-07 17:06:09.44',	1,	1100,	0,	NULL,	'ddd tttt',	'099',	2,	NULL,	2),
(3,	'blue',	'AO1234AA',	'2023-06-07 17:52:15.315',	'2023-06-08 19:52:15.311',	1,	1089,	0,	NULL,	'ddd tttt',	'099',	2,	NULL,	2),
(4,	'blue',	'AO1234AA',	'2023-06-07 17:52:20.257',	'2023-06-08 19:52:20.257',	2,	1100,	0,	NULL,	'ddd tttt',	'099',	2,	NULL,	2),
(5,	'red',	'AO1235AC',	'2023-06-08 11:04:06.85',	'2023-06-10 13:04:06.85',	5,	2100,	0,	NULL,	'test test',	'0992950357',	6,	NULL,	5);

CREATE SEQUENCE users_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;

CREATE TABLE "public"."users" (
    "id" bigint DEFAULT nextval('users_id_seq') NOT NULL,
    "date_registered" date NOT NULL,
    "email" character varying(255) NOT NULL,
    "first_name" character varying(255) NOT NULL,
    "image" character varying(255),
    "is_banned" boolean NOT NULL,
    "last_name" character varying(255) NOT NULL,
    "password" character varying(255) NOT NULL,
    "phone_number" character varying(255) NOT NULL,
    "role" character varying(255),
    "time_registered" time without time zone,
    CONSTRAINT "users_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
INSERT INTO "users" ("id", "date_registered", "email", "first_name", "image", "is_banned", "last_name", "password", "phone_number", "role", "time_registered") VALUES
(1,	'2023-06-04',	'petsa.yuri@gmail.com',	'Yuri',	'dd7a369e-4975-4170-913d-37b1ead5f7e2.jpg',	'0',	'Petsa',	'$2y$10$qz6R3OOW2zKfiA53Mg5HOuswRRHVg3X6hIYz9goli5q8s.8ojMG56',	'0954410994',	'owner',	'16:40:43'),
(5,	'2023-06-08',	'test@gmail.com',	'test',	NULL,	'0',	'test',	'$2a$10$F.UeONhteQSpN7/CKumZVuOLD1mPMbVY.c9C2ySVhC1zuEfuFiulW',	'0992950357',	'user',	'11:00:48'),
(2,	'2023-06-04',	'dddd',	'ddd',	'28a50223-9db4-495a-a853-d18e23119585.jpg',	'0',	'tttt',	'$2a$10$FgIizeWcPSlTTZxF90UPh.a05f2n37kuJVIp7ZS3UQHLwF4i4zpXW',	'099',	'user',	'18:36:44');

CREATE TABLE "public"."users_booking_lots" (
    "users_id" bigint NOT NULL,
    "booking_lots_id" bigint NOT NULL,
    CONSTRAINT "uk_slh0lfgg8fidxk32poreyusan" UNIQUE ("booking_lots_id")
) WITH (oids = false);


CREATE TABLE "public"."users_cars" (
    "users_id" bigint NOT NULL,
    "cars_id" bigint NOT NULL,
    CONSTRAINT "uk_55xrpivj2yxbnadh9kv55t37n" UNIQUE ("cars_id")
) WITH (oids = false);

<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
<br />
<b>Warning</b>:  Undefined property: stdClass::$flags in <b>C:\OSPanel\modules\system\html\openserver\adminer\adminer_core.php</b> on line <b>200</b><br />
INSERT INTO "users_cars" ("users_id", "cars_id") VALUES
(2,	2),
(2,	3),
(2,	4),
(5,	6);

CREATE TABLE "public"."users_reviews" (
    "users_id" bigint NOT NULL,
    "reviews_id" bigint NOT NULL,
    CONSTRAINT "uk_nwbr17s0jsngxjkc46al3yb21" UNIQUE ("reviews_id")
) WITH (oids = false);


ALTER TABLE ONLY "public"."booking" ADD CONSTRAINT "fk1htkw0iaod1ansrjo8yla03a9" FOREIGN KEY (parking_lot_id) REFERENCES parking(id) NOT DEFERRABLE;
ALTER TABLE ONLY "public"."booking" ADD CONSTRAINT "fk7udbel7q86k041591kj6lfmvw" FOREIGN KEY (user_id) REFERENCES users(id) NOT DEFERRABLE;

ALTER TABLE ONLY "public"."cars" ADD CONSTRAINT "fk69pyq96ia2d5q616mb9jy6fvc" FOREIGN KEY (parking_id) REFERENCES parking(id) NOT DEFERRABLE;
ALTER TABLE ONLY "public"."cars" ADD CONSTRAINT "fkqw4c8e6nqrvy3ti1xj8w8wyc9" FOREIGN KEY (user_id) REFERENCES users(id) NOT DEFERRABLE;

ALTER TABLE ONLY "public"."parking" ADD CONSTRAINT "fkbn81sa0wmmh1xo4eg57ed68im" FOREIGN KEY (car_id) REFERENCES cars(id) NOT DEFERRABLE;

ALTER TABLE ONLY "public"."parking_booking_lot" ADD CONSTRAINT "fk2xv5i9dkvgptrdl0ogwo6v4sc" FOREIGN KEY (parking_id) REFERENCES parking(id) NOT DEFERRABLE;
ALTER TABLE ONLY "public"."parking_booking_lot" ADD CONSTRAINT "fklu3f6rbsvmmb9jyv67woe7x9l" FOREIGN KEY (booking_lot_id) REFERENCES booking(id) NOT DEFERRABLE;

ALTER TABLE ONLY "public"."reviews" ADD CONSTRAINT "fkcgy7qjc1r99dp117y9en6lxye" FOREIGN KEY (user_id) REFERENCES users(id) NOT DEFERRABLE;

ALTER TABLE ONLY "public"."transactions" ADD CONSTRAINT "fkjgfk1ja6vc2nps7llxx72kxs8" FOREIGN KEY (promo_code_id) REFERENCES promocodes(id) NOT DEFERRABLE;
ALTER TABLE ONLY "public"."transactions" ADD CONSTRAINT "fkqwv7rmvc8va8rep7piikrojds" FOREIGN KEY (user_id) REFERENCES users(id) NOT DEFERRABLE;
ALTER TABLE ONLY "public"."transactions" ADD CONSTRAINT "fktc6m2p72iaxa44kea0r0q8l6u" FOREIGN KEY (car_id) REFERENCES cars(id) NOT DEFERRABLE;

ALTER TABLE ONLY "public"."users_booking_lots" ADD CONSTRAINT "fk7duhudclv2edwopk2ejirbggk" FOREIGN KEY (users_id) REFERENCES users(id) NOT DEFERRABLE;
ALTER TABLE ONLY "public"."users_booking_lots" ADD CONSTRAINT "fkdp6c357ai19rars6uovj5npuc" FOREIGN KEY (booking_lots_id) REFERENCES booking(id) NOT DEFERRABLE;

ALTER TABLE ONLY "public"."users_cars" ADD CONSTRAINT "fk3bcwnsx8frmbma7x5u4ts6dyn" FOREIGN KEY (cars_id) REFERENCES cars(id) NOT DEFERRABLE;
ALTER TABLE ONLY "public"."users_cars" ADD CONSTRAINT "fkmelc9vq85xbujff73aublnu62" FOREIGN KEY (users_id) REFERENCES users(id) NOT DEFERRABLE;

ALTER TABLE ONLY "public"."users_reviews" ADD CONSTRAINT "fkpk6xw4dyd9g7dfxyth6cb3ept" FOREIGN KEY (users_id) REFERENCES users(id) NOT DEFERRABLE;
ALTER TABLE ONLY "public"."users_reviews" ADD CONSTRAINT "fksrruhgv1krftmcioqdn9mplck" FOREIGN KEY (reviews_id) REFERENCES reviews(id) NOT DEFERRABLE;

-- 2023-06-18 20:54:21.07914+03
