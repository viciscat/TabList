# Tab List (Player List) mod

## What in the world is this

A client-side mod for babric b1.7.3 that adds a simple Tab Player List to the game! The server needs to have some kind
of player list command (currently only /list) for this to work. If you know of other plugins that use different commands let me know.

![image](https://github.com/viciscat/TabList/assets/51047087/c6aa3ff9-172e-4ae0-ae1c-c02485e58bca)


This mainly exists cuz I wanted a tab list on RetroMC, so this was only tested over there!

## Known issues

- If a player sends a message on the same tick as the server responds, the player message will incorrectly be interpreted as a player in the list. Wait a few seconds and press the player list key again.
- If the response is not sent in one go due to lag, the second part will not be taken. Wait a few seconds and press the player list key again.
