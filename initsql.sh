psql <<- EOSQL
  CREATE USER POSTGRES_USER;
  CREATE DATABASE POSTGRES_DB;
  GRANT ALL PRIVILEGES ON DATABASE POSTGRES_DB TO POSTGRES_USER;

  CREATE USER AUTH_PG_USER;
  GRANT INSERT ON DATABASE POSTGRES_DB TO AUTH_PG_USER;
  GRANT UPDATE ON DATABASE POSTGRES_DB TO AUTH_PG_USER;
  GRANT DELETE ON DATABASE POSTGRES_DB TO AUTH_PG_USER;
EOSQL
