-- Adminer 4.8.1 PostgreSQL 14.3 dump

DROP TABLE IF EXISTS "cars";
DROP SEQUENCE IF EXISTS cars_id_seq;
CREATE SEQUENCE cars_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;

CREATE TABLE "public"."cars" (
    "id" bigint DEFAULT nextval('cars_id_seq') NOT NULL,
    "color" character varying(255) NOT NULL,
    "numbers" character varying(255) NOT NULL,
    "type" character(1) NOT NULL,
    "parking_id" bigint,
    "user_id" bigint,
    "image" character varying(255),
    CONSTRAINT "cars_pkey" PRIMARY KEY ("id")
) WITH (oids = false);


DROP TABLE IF EXISTS "parking";
CREATE TABLE "public"."parking" (
    "id" bigint NOT NULL,
    "busy_end" timestamp(6),
    "busy_start" timestamp(6),
    "busy_days" integer NOT NULL,
    "busy_hours" integer NOT NULL,
    "is_available" boolean NOT NULL,
    "price_per_day" integer NOT NULL,
    "price_per_hour" integer,
    "total_price" integer NOT NULL,
    "car_id" bigint,
    "promocode" character varying(255),
    CONSTRAINT "parking_pkey" PRIMARY KEY ("id")
) WITH (oids = false);


DROP TABLE IF EXISTS "promocodes";
DROP SEQUENCE IF EXISTS promocodes_id_seq;
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


DROP TABLE IF EXISTS "reviews";
DROP SEQUENCE IF EXISTS reviews_id_seq;
CREATE SEQUENCE reviews_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;

CREATE TABLE "public"."reviews" (
    "id" bigint DEFAULT nextval('reviews_id_seq') NOT NULL,
    "body" character varying(255) NOT NULL,
    "date" timestamp(6) NOT NULL,
    "grade" integer NOT NULL,
    CONSTRAINT "reviews_pkey" PRIMARY KEY ("id")
) WITH (oids = false);


DROP TABLE IF EXISTS "transactions";
DROP SEQUENCE IF EXISTS transactions_id_seq;
CREATE SEQUENCE transactions_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;

CREATE TABLE "public"."transactions" (
    "id" bigint DEFAULT nextval('transactions_id_seq') NOT NULL,
    "car_color" character varying(255) NOT NULL,
    "car_numbers" character varying(255) NOT NULL,
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
    "date" timestamp(6),
    CONSTRAINT "transactions_pkey" PRIMARY KEY ("id")
) WITH (oids = false);


DROP TABLE IF EXISTS "users";
DROP SEQUENCE IF EXISTS users_id_seq;
CREATE SEQUENCE users_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;

CREATE TABLE "public"."users" (
    "id" bigint DEFAULT nextval('users_id_seq') NOT NULL,
    "date_registered" date NOT NULL,
    "email" character varying(255) NOT NULL,
    "first_name" character varying(255) NOT NULL,
    "image" character varying(255),
    "last_name" character varying(255) NOT NULL,
    "phone_number" character varying(255) NOT NULL,
    "role" character varying(255),
    "time_registered" time without time zone,
    "password" character varying(255),
    "is_banned" boolean,
    CONSTRAINT "users_pkey" PRIMARY KEY ("id")
) WITH (oids = false);


DROP TABLE IF EXISTS "users_cars";
CREATE TABLE "public"."users_cars" (
    "users_id" bigint NOT NULL,
    "cars_id" bigint NOT NULL,
    CONSTRAINT "uk_55xrpivj2yxbnadh9kv55t37n" UNIQUE ("cars_id")
) WITH (oids = false);


ALTER TABLE ONLY "public"."cars" ADD CONSTRAINT "cars_parking_id_fkey" FOREIGN KEY (parking_id) REFERENCES parking(id) NOT DEFERRABLE;
ALTER TABLE ONLY "public"."cars" ADD CONSTRAINT "fkqw4c8e6nqrvy3ti1xj8w8wyc9" FOREIGN KEY (user_id) REFERENCES users(id) NOT DEFERRABLE;

ALTER TABLE ONLY "public"."parking" ADD CONSTRAINT "fkbn81sa0wmmh1xo4eg57ed68im" FOREIGN KEY (car_id) REFERENCES cars(id) NOT DEFERRABLE;

ALTER TABLE ONLY "public"."transactions" ADD CONSTRAINT "fkjgfk1ja6vc2nps7llxx72kxs8" FOREIGN KEY (promo_code_id) REFERENCES promocodes(id) NOT DEFERRABLE;
ALTER TABLE ONLY "public"."transactions" ADD CONSTRAINT "fkqwv7rmvc8va8rep7piikrojds" FOREIGN KEY (user_id) REFERENCES users(id) NOT DEFERRABLE;
ALTER TABLE ONLY "public"."transactions" ADD CONSTRAINT "fktc6m2p72iaxa44kea0r0q8l6u" FOREIGN KEY (car_id) REFERENCES cars(id) NOT DEFERRABLE;

ALTER TABLE ONLY "public"."users_cars" ADD CONSTRAINT "fk3bcwnsx8frmbma7x5u4ts6dyn" FOREIGN KEY (cars_id) REFERENCES cars(id) NOT DEFERRABLE;
ALTER TABLE ONLY "public"."users_cars" ADD CONSTRAINT "fkmelc9vq85xbujff73aublnu62" FOREIGN KEY (users_id) REFERENCES users(id) NOT DEFERRABLE;

-- 2023-06-01 23:04:17.009832+03
