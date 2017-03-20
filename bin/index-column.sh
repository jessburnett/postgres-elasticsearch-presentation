#!/bin/bash

OFFSET=0
BATCH_SIZE=10000

function main () {
    while :
    do
        perform_bulk_update
        if [ ${BATCH_SIZE} -le 1 ]
        then
            echo "Cannot reduce batch size further, there are ${OFFSET} bad rows"
            return
        fi

        BATCH_SIZE=$((${BATCH_SIZE} / 10))
        OFFSET=0
    done
}

function perform_bulk_update () {
    COUNT=$(count_remaining_rows)
    echo "There are ${COUNT} rows remaining, updating them ${BATCH_SIZE} at a time"

    while has_remaining_rows
    do
        update_rows
    done
}

function has_remaining_rows () {
    [ ${COUNT} -gt ${OFFSET} ]
}

update_rows () {
    if perform_row_update
    then
        OFFSET=$((${OFFSET} + ${BATCH_SIZE}))

        if [ ${OFFSET} -ge ${COUNT} ]
        then
            echo "Failed to update ${BATCH_SIZE} rows, offset is now ${OFFSET}, there are no more rows remaining"
        else
            echo "Failed to update ${BATCH_SIZE} rows, offset is now ${OFFSET}, there are $((${COUNT} - ${OFFSET})) rows remaining"
        fi
    else
        COUNT=$((${COUNT} - ${BATCH_SIZE}))
        echo "Updated ${BATCH_SIZE} rows, there are $((${COUNT} - ${OFFSET})) rows remaining"
    fi
}

perform_row_update () {
    db <<<"
        update postgres_document
            set body_tsvector = to_tsvector('english', body)
            where id in (
                select id from postgres_document
                    where body_tsvector is null
                    order by id asc
                    limit ${BATCH_SIZE}
                    offset ${OFFSET}
            );
    " 2>&1 | egrep -v '^NOTICE:' | egrep -v '^DETAIL:' | fgrep ERROR
}

function count_remaining_rows () {
  db <<<'select count(id) from postgres_document where body_tsvector is null;' | sed -e '/^$/d ; s/ //g'
}

function db () {
    psql -v ON_ERROR_STOP=1 -q -t -X -p 5432 -h localhost -U postgres postgres
}

main
