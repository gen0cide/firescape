# Firescape To-Do List

## Make dedicated persistence class for SQL

There should be some dedicated class to handle PostgreSQL I/O. The following Objects should be serialized and saved into tables:

  * Player
  * Stats
  * Skills
  * Inventory
  * Bank
  * Friends List
  
## Move passwords to bcrypt
No idea why they're plaintext right now, but this should change.

## Dedicated Persistence CLI Utility
Right now, the way to manipulate a persisted character is like this:

```
$ redis-cli GET players_player_name > /tmp/player_name.txt
$ sed -i 's/loggedin=true/loggedin=false/' /tmp/player_name.txt
$ redis-cli SET players_player_name "$(cat /tmp/player_name.txt)"
```

This is cumbersome and should have a better way to do this.

## Explore an event pipeline
I'm curious about how much "big data" we could collect in the game - trade transactions, attacks, kills, etc. Considering what to do on this front.

## [DONE] Move some configurations to ENV vars in the bot
Right now, the bot uses `settings.ini` with a hardcoded username and password. We should be able to pass ENV vars instead making this cleaner.

## clean up CommandHandler code
It's a mess and difficult to read.