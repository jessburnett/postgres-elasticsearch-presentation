# Foreign Transactions

Elastic Search has no transactions

You can make large scale changes using aliases

Using them for every transaction would be impossible

How would you serialize the changes (serlizable isolation level rejects unserializable situations, without transaction boundaries you have still made the change to elastic search)
