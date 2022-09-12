-- Prerequisites: CREATE EXTENSION pgcrypto;

CREATE TABLE foo(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(64)
);
