CREATE TABLE IF NOT EXISTS post (
   id SERIAL PRIMARY KEY,
   name TEXT,
   description TEXT,
   created TIMESTAMP,
   visible BOOLEAN,
   city_id INT
);
