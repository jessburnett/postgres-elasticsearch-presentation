```
apt-get install                     \
    postgresql-9.6-python-multicorn \
    python                          \
    python-pip
pip install elasticsearch
git clone https://github.com/matthewfranglen/postgres-elasticsearch-fdw pg-es-fdw

cd pg-es-fdw
python setup.py install
```
