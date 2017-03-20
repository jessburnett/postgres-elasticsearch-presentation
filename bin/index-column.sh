#!/bin/bash

OFFSET=0
readonly BATCH_SIZE=10000

function main () {
    while has_remaining_rows
    do
        update_rows
    done
}

function has_remaining_rows () {
    COUNT=$(count_remaining_rows)

    echo "Done ${OFFSET} rows, there are ${COUNT} rows total"
    [ ${COUNT} -gt $OFFSET ]
}

update_rows () {
    if perform_row_update
    then
        echo "Failed to update rows, offset was ${OFFSET}"
    else
        echo "Updated ${BATCH_SIZE} rows"
    fi

    OFFSET=$((${OFFSET} + ${BATCH_SIZE}))
}

perform_row_update () {
    db <<<"
        update postgres_document
            set body_tsvector = to_tsvector('english', body)
            where id in (
                select id from postgres_document
                    order by id asc
                    limit ${BATCH_SIZE}
                    offset ${OFFSET}
            );
    " 2>&1 | egrep -v '^NOTICE:' | egrep -v '^DETAIL:' | fgrep ERROR
}

function count_remaining_rows () {
  db <<<'select count(id) from postgres_document;' | sed -e '/^$/d ; s/ //g'
}

function db () {
    psql -v ON_ERROR_STOP=1 -q -t -X -p 5432 -h localhost -U postgres postgres
}

main
