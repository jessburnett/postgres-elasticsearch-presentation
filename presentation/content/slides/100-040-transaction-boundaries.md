# However

Transactions can fail in PG

FDW writes during Transaction

No Transactions in ES

Note:
A trigger failing could abort the transaction, so they must be evaluated during the transaction

Isolation level can mean even a completely valid transaction can fail

Would have to record original state of everything altered in es to roll back
