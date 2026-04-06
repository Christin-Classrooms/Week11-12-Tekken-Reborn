FROM mysql:8.0
# When the container starts for the first time, it will run any .sql scripts in this folder.
COPY init.sql /docker-entrypoint-initdb.d/
