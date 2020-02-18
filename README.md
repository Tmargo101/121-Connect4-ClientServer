# Connect 4
## A Client / Server Game Written in Java

### Created For ISTE-121 Final Project, Semester 2171


## Features

- Multi-Game Support: The Server can support many instances of the game being played simultaneously (Up to 8 concurrent games have been tested)

- Lobby: When a client connects to a server, they will be placed in a lobby until another player connects for them to play with.  At this time, both players will enter a new game.

- Chat: When players are connected to a game instance, they can send messages to each other via the chat window.

- Turn Lockout: When it is not a player's turn, the game controls are locked out to prevent accidental inputs.

## Bugs

- Game may occasionally lock up and force a restart of the client.  This will lose game state, and the other client will have to reconnect as well.

### Team Members: Tom Margosian, Brandon Hettler, Todd Bednarczyk, Josh Schrader
