# dirsync

Simple server for sharing a directory with a remote computer. The directory will be 100% mirrored to the remote client.

## Use cases

My use case is quite simple. I play Minecraft with my sister from time to time. And we like playing with mods.
That means that everytime we want a new mod I have to add a set of JAR files to my PC, her PC, and the remote AWS server.
And we live in different countries. So it takes time to maintain the consistent set of mod JAR files. With this solution,
I can set the mod files on the server, and the dirsync client takes care of keeping both clients up-to-date. Simple yet effective.

