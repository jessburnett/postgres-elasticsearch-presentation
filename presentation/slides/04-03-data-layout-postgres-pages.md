DATA-LAYOUT-POSTGRES-PAGES

Every table and index is stored as an array of pages of a fixed size (usually 8 kB, although a different page size can be selected when compiling the server). In a table, all the pages are logically equivalent, so a particular item (row) can be stored in any page. In indexes, the first page is generally reserved as a metapage holding control information, and there can be different types of pages within the index, depending on the index access method.
https://www.postgresql.org/docs/current/static/storage-page-layout.html

PostgreSQL uses a fixed page size (commonly 8 kB), and does not allow tuples to span multiple pages. Therefore, it is not possible to store very large field values directly. To overcome this limitation, large field values are compressed and/or broken up into multiple physical rows. This happens transparently to the user, with only small impact on most of the backend code. The technique is affectionately known as TOAST (or "the best thing since sliced bread"). The TOAST infrastructure is also used to improve handling of large data values in-memory.
https://www.postgresql.org/docs/current/static/storage-toast.html

Each heap and index relation, except for hash indexes, has a Free Space Map (FSM) to keep track of available space in the relation.
https://www.postgresql.org/docs/current/static/storage-fsm.html

Each heap relation has a Visibility Map (VM) to keep track of which pages contain only tuples that are known to be visible to all active transactions; it also keeps track of which pages contain only unfrozen tuples.
https://www.postgresql.org/docs/current/static/storage-vm.html
