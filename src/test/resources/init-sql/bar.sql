-- Create the external database for the bar service.
CREATE USER bar PASSWORD 'bar';
CREATE DATABASE bar OWNER bar;
GRANT ALL PRIVILEGES ON DATABASE bar TO bar;
\connect bar;
CREATE EXTENSION pgcrypto;
CREATE SCHEMA bar AUTHORIZATION bar
  CREATE TABLE bar(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(64) UNIQUE NOT NULL
  );
