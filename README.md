# GwentStone - Rareș Constantin 

This is a game of cards set in a fantasy world, in which two heroes meet on a battlefield to fight for the land of *Truyssau*. There are multiple cards you can use during the game, but also multiple heroes to choose from.

## Installation

First, you should uninstall any game card you have on your device, because this one is what you'll want to play from now on.\
Second, use Java 11-19 to play the game and run it from Main Class with Json files as input. You can see an example below.

## Implementation
### Inheritance

There are multiple inheritances as it follows:
1. Card 
	* Minion - abstract card of a minion
		* BasicMinion - basic minion without ability
		* AbilityMinion - minions with abilities
	* Hero
	* Environment - card with environment ability

2. Command
	* Actions - the enum for actions internal anonymous classes
	* Debugs - the enum for debug commands internal anonymous classes
	* Stats - the enum for stats commands internal anonymous classes

### Used pattern
I used a **common pattern** found in games just like this one named *Command Pattern*, but I modified it a little bit to be easier when it comes to implementing new actions and commands.\
So, from the start, every card has every field (from name to abilities) that the game needs to run properly, which means I **decoupled** any ability or attribute from the cards themselves, this lets us create any type of card (name, description etc.) without the need of verifying it during the game. The constructor in each card class takes the input and makes the card as general as possible.\
When it comes to the commands/actions, there is an abstract enum for every type of command (presented above), which keeps every anon class that executes a specific command. It is also **easier to read** the code, as the name of each command is the starting point of the anon class.

### Flow

The main class of the game, Table, is a Singleton class, so the game would be able to run multiple games at once in the future. In this class, there is set up every game using the GameConfig class, which represents the configuration of the cards on table and the players during a session. The players are set up and the games start, the actions are taken one by one and filtered, then executed using the command classes (presented above). Each command is executed after the ErrorHandler returns no error back to the command and then it outputs to the Json file the result, which can be a debug, a statistics or an error. The errors are also decoupled from the commands/actions. If one hero dies after reaching 0 health, the game ends and it reloads the players for the next game.

### Abilities

There are abilities that can be used on minions or on rows. Each type is implemented using abstract methods in the enums, so the ability can be attached to a card as you please, without being dependent on it's name, resulting in an easier process of creating a new ability and then attaching it to a new card. Each card that can have an ability (AbilityMinion, Hero, Environment) has a method to call its ability over minions/rows.

### Error handler

The error handler is a special utility class that handles every possible error for every available command/action, so a command is not executed before checking its errors. It returns an error in a form you can choose **(ObjectNode or boolean)**, so the output can be whatever you need it to be. It is easy to implement a new error by using the abstract method inside the enum. By creating the error handler, the whole project is easier to read, mainly the commands enums, but it means it's important to implement also any error that can occur during the gameplay.

### Other important information

There are also constants that you can change so you can create different game modes.\
The Utility class is used for output into Json files, so it can be removed if another type of output is chosen.\
The players are represented by rows for each one, so the rows must be viewed mirrorred. So the row 0 of both players is the back row and same for row 1.\
The game is made to function and recoded for **three rows**, not just two, and even more cards on one row. Some fine adjustments are needed for **row indexing**.

## List of commands

The list of commands and their use can be found on the following link [OCW])(https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/tema).

## To improve

In the future, I will add other commands, abilities and attributes, some found in the other games (like destroying all cards on a row, applying buffs to the adiacent cards and hero secrets that are triggered when attacked), some original ones. But until then, **the battle for *Truyssau* continues!**

## License - Copyright 2022

[Github - GwentStone](https://github.com/RaresCon/OOP_Tema1)