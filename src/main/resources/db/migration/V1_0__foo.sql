CREATE EXTENSION pgcrypto; -- provides the gen_random_uuid() function
CREATE TABLE foo(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(64)
);
