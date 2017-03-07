Search Test with Postgres and Elastic Search
============================================

Wikipedia XML
-------------

This was tested with enwiki-20161020-pages-articles.xml. This is a 56GB dump of every article from the English language Wikipedia.

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

Elastic Search Loader
---------------------

This is a script that can load the Wikipedia XML into Elastic Search. This requires the elasticsearch_dsl python module. This was developed for python 3.6.0.

### Usage

```
python3 elasticsearch/es-loader.py enwiki-20161020-pages-articles.xml
```

### Requirements

You can use pyenv and virtualenv to install this in a compartmentalized way.

https://github.com/pyenv/pyenv

```
curl -L https://raw.githubusercontent.com/yyuu/pyenv-installer/master/bin/pyenv-installer | bash
```

You need to make sure that you have something like the following in your bashrc or zshrc:

```
export PATH="${HOME}/.pyenv/bin:$PATH"
eval "$(pyenv init -)"
eval "$(pyenv virtualenv-init -)"
```

Then in a new terminal session you can run:

```
pyenv install 3.6.0
pyenv virtualenv 3.6.0 pg-es-venv
pyenv activate pg-es-venv
pip install -r elasticsearch/requirements.txt
```
