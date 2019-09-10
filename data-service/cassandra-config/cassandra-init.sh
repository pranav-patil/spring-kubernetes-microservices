#!/bin/bash

CQL="DROP keyspace testdb;
CREATE KEYSPACE testdb WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'} AND durable_writes = true;
ALTER USER cassandra WITH PASSWORD 'admin123';
CREATE USER appuser WITH PASSWORD 'test123';
GRANT ALL ON KEYSPACE testdb TO appuser;"

until echo $CQL | cqlsh; do
  echo "cqlsh: Cassandra is unavailable to initialize - will retry later"
  sleep 2
done &

exec /docker-entrypoint.sh "$@"
