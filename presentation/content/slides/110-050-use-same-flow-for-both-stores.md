# Same Actions

# Different Databases

# Same State

Note:
Writer applications can read the log and apply it to the appropriate database

Messages have an offset, when two databases are at the same offset they are identical

Can include desired offset in requests, and have the system wait until it is reached
