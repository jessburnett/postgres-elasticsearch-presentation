Search Test with Postgres and Elastic Search
============================================

Elastic Search
--------------

The elastic search docker compose configuration may fail to start with the following error:

```
max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
```

If that happens then update the system configuration with the following command (linux):

```bash
sysctl -w vm.max_map_count=262144
```

You can find more details in the [elastic search docker compose documentation](https://www.elastic.co/guide/en/elasticsearch/reference/current/docker.html).
