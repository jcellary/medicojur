

CREATE TABLE Account
(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_name VARCHAR NOT NULL,
  password VARCHAR NOT NULL,
  role VARCHAR NOT NULL,
  first_name VARCHAR NOT NULL,
  last_name VARCHAR NOT NULL
);