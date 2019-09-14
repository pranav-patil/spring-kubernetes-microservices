#!/bin/bash

CQL="DROP keyspace $CASSANDRA_APP_KEYSPACE;
CREATE KEYSPACE $CASSANDRA_APP_KEYSPACE WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'} AND durable_writes = true;
CREATE ROLE $CASSANDRA_APP_USERNAME WITH SUPERUSER = false AND LOGIN = true AND PASSWORD = '$CASSANDRA_APP_PASSWORD';
GRANT ALL ON KEYSPACE $CASSANDRA_APP_KEYSPACE TO $CASSANDRA_APP_USERNAME;
CREATE ROLE $CASSANDRA_ROOT_USERNAME WITH SUPERUSER = true AND LOGIN = true AND PASSWORD = '$CASSANDRA_ROOT_PASSWORD';
ALTER ROLE cassandra WITH SUPERUSER = false AND LOGIN = false;

until echo $CQL | cqlsh; do
  echo "cqlsh: Cassandra is unavailable to initialize - will retry later"
  sleep 2
done &

exec /docker-entrypoint.sh "$@"
