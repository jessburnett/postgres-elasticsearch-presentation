FROM postgres:9.6

RUN apt-get update
RUN apt-get install --yes           \
    postgresql-9.6-python-multicorn \
    python                          \
    python-pip                      \
    git
RUN pip install elasticsearch
RUN git clone https://github.com/matthewfranglen/postgres-elasticsearch-fdw /pg-es-fdw

WORKDIR /pg-es-fdw
RUN python setup.py install
