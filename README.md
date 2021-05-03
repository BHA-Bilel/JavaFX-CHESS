# JavaFX-CHESS 🕹️

> Play Chess with your friends online!
> 
> Jump to the [Setup](#setup) section to to download the game, enjoy!
> 
> Discover [How to play](#how-to-play) and give me your [Feedback](#feedback) on this project.
> 
> You can find 5 more board games in the [Collection](#collection) section, give them a try!

## Table of contents
* [Checkers mutant](#checkers-mutant)
* [Game Charachteristics](game-charachteristics)
* [How to play](#how-to-play)
* [Setup](#setup)
* [Collection](#collection)
* [Project dependencies](#project-dependencies)
* [Additional info](#additional-info)
* [Feedback](#feedback)

# Checkers mutant
Chess is the **third** learning project of my collection, a quite similar one to Checkers;

This project was indeed a **mutant** of Checkers, **minor changes** were made in logic/gui to handle the **diversity** of Pieces with their **unique** movements.

# Game Charachteristics
- Turn based
- Multiplayer (no AI)
- Tabletop view (2D)
- Distributed (client/server)
- Socket programming (TCP)
- Message oriented communication (except game app)
- Supports multiple resolutions thanks to JavaFX scaling
- Supports fullscreen mode
- Doesn't support castling

# How to play
![Main app gui](./screenshots/mainApp.png)

After connecting to the server, the main app gui is presented to the player, 
allowing him to enter any username, then either Host, Join a specific room through its ID, 
or Join public rooms.

![Join app gui](./screenshots/joinApp.png)

If the player chooses to play with random people, he will be presented with a list of public rooms

![Room app gui](./screenshots/roomApp.png)

After joining a **room** the player is presented with this gui, where he can:
- Start a **chat** with people already in the room
- Change his **name**
- Take an empty **place**
- Set his **ready** status

The host has in addition the ability to:
- Change the room's **privacy** to either public/private
- **Kick** someone out of the room
- **Start** the game

Host privileges are **passed** automatically to the next player if the host leaves the room

![Game app gui](./screenshots/gameApp.png)

The game works as a regular Chess game but in a distributed environment, 
you can end the game without leaving the room using the **Return to..** menu at the top.

# Setup
Describe how to install / setup your local environement / add link to demo version.
Portable exe file in setup folder

# Collection
- [XO](https://github.com/BHA-Bilel/JavaFX-XO)
- [Checkers](https://github.com/BHA-Bilel/JavaFX-CHECKERS)
- Chess (current)
- [Connect4](https://github.com/BHA-Bilel/JavaFX-CONNECT4)
- [Dominoes](https://github.com/BHA-Bilel/JavaFX-DOMINOS)
- [Coinche](https://github.com/BHA-Bilel/JavaFX-COINCHE)

# Project dependencies
- **Java**-15.0.1
- **JavaFX**-11.0.2
- **controlsfx**-11.0.0
- **jfoenix**-9.0.10

# Additional info
- This project was developed intermittently due to other preoccupations, that's why I can't tell how much time it took me to complete it.
All the collection was initially developed in **Eclipse IDE** in late 2019, before I migrated to **Intellij IDEA** in 2021 to code the remaining parts while redesigning some parts to fit the new workflow model.

- This project wasn't my first nor my last experience coding in JavaFX, I'll do my best to publish other projects on my GitHub.

- **All** of the projects/repositories (so far) in my profile are produced by an **individual** effort, that I coded from **scratch**.

# Feedback
What do you think of this project? leave your thoughts/recommendations !

<p align="center">
  <a href="https://gist.github.com/BHA-Bilel/8b624cce0a1263299f84d59ddc05eaf9">
    <img src="https://gist.githubusercontent.com/BHA-Bilel/6eb01c298f0ccceff7511427afb52534/raw/bfaf1c70946579fe9f4991d923215d7f402de250/bguestbook.gif" alt="Click here to sign my guestbook!">
  </a>
</p>
