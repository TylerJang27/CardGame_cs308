# DESIGN.md

## Team Roles

#### Frontend
- Mariusz Derezinski-Choo: Organized the different view scenes and stages, creating a pipeline for loading and saving files.
- Sarah Gregorich: Organized the styling and languages for the games, creating a pipeline for languages and cell information.

#### Controller/Data
- Andrew Krier: Designed XML regex and managed interactions between the frontend and backend.
- Tyler Jang: Designed factories to parse and write XMLs so that the phase machine could be executed at runtime.

#### Backend
- Maverick Chung: Designed the interactions of cells, decks, and cards with each other with a watertight backend interface.

## Introduction
Our vision for this project was to have any card game that currently exists or could ever exist be playable with our engine. In particular, we define a card game as a game that can be played with nothing more than an arbitrary deck of cards and a table or other surface to place the cards on, where the technical definition of a card is an object with exactly two sides, a suit, and a value.

This was nearly accomplished - our framework is immensely extensible, with deck type and contents, card size and location, game rules, logic, and flow all being defined by XML documents. As a result, many different games can be played - we've created Solitaire, Memory, War, and 52 card pickup, but games like uno or crazy eights, or even games that aren't card games like rock-paper-scissors, chess, and checkers are theoretically definable and playable with our engine.

## High Level Design Overview

#### View
* View is responsible for displaying everything necessary to the user, receiving user GUI inputs, and reporting back to controller in a meaningful way. This structure was split between a main menu and a game screen. This divide allowed for a base screen and view to always be available, and for the games themselves to be run in their own windows independent of one another.

#### Controller
* Controller served as the manager for the entire data pipeline, and for the instance of the view and each game.
* Controller initializes the view with a limited number of lambdas to report game updates such as moves or button clicks.
* Controller also manages which data files are used and when, initializing view with a style xml, and later providing it with the proper layout xml for the game requested

#### Table
* Table handles all the rules and decision making inherit to the games, and is initialized with a rules XML file
* Table handles a hierarchy of phase machine, rules, cells, cards, and decks to restrict what moves are possible and how a game reacts to them.
* When given a move, table provides a list of cells that are updated in correspondence with the game rules to the controller to be processed and sent to the view.

#### Data
* Data was split into three unique file types, style, rules, and layout.
    * The style data files dictated many visual decisions enforced in view's construction, and is the simplist xml file.
    * The layout data files described how cells would be laid out by view, guaranteeing that the backend would have no need for any concept of location in their rules decisions.
    * The rules data file is the most complex, it sorts rules into different phases of a phase machine, and different rules are applied to different cells and cell groups.
* These data files are handled by unique factories that enable the transition between XML and meaningful Java objects.

#### Full Interaction
* When controller is initialized, it makes a view object with a style data class from data, from there it receives a game selection from the user. The game selection is then translated into a rules file that is then, through data processing, made into a table object complete with functional phase machine and rules. From there any move processed by the view is passed to controller and then the proper instance of the table. Table returns a map of cell names and cells, which is then used to update the view, ready for the next move by the player.

## Design Considerations
- To have the entirety of the game logic contained within XML
- To have a range of decisions to accommodate a variety of game logic settings
- To have the view of the game customizable
- To have a game that is runnable on most computers
- To have support for multiplayer games
- To have support for any type of card or deck that falls under the defintions above

For these reasons, the core data structure of our backend is a Cell, a doubly-linked tree of lists of cards with an arbitrary number of branches. This allows us to order cards in any way, permitting movement of stacks of cards, and parent ownership of children cards. The other major part of this is the many factories that exist in the backend, interpreting the regexes in the XML documents as the rules and game logic of arbitrary games, ensuring that none of this logic is stored in the Java code itself.

As all of this computation is handled in the backend, the frontend simply takes in the card data and faithfully reproduces it to the screen, making our game engine even more extensible and closed.


## Adding New Features

#### New Games

To add additional games (game names must be lowercase and contain more than just number characters) to the project, the following must be done:

1. Add a directory to the data/ directory matching the title of your game.
2. Add two files to the directory created, matching the title_rules.xml and title_layout.xml (each of which must adhere to the formatting specified in [src/ooga/data/factories/schemas/layout_schema.xsd] and [src/ooga/data/factories/schemas/rules_schema.xsd]).
    - These rules and layout files must match the specification in [XML_Documentation.md](XML_Documentation.md).
3. Add instructions for the game to src/ooga/resources/languages/messages/ for each of the languages with the key formatting title_insns.
4. Add the game and its translations to src/ooga/resources/languages/games/ for each of the languages with the key formatting title.

#### New Rule Options

To add additional rules regex to enable different types of rule conditions or different types of action behavior, the following must be done:

1. Modify the [XML_Documentation.md](XML_Documentation.md) file to include the regex you are trying to add.
2. Modify the [layout_schema.xsd](../src/ooga/data/factories/schemas/layout_schema.xsd) and [rules_schema.xsd](../src/ooga/data/factories/schemas/rules_schema.xsd) files to ensure that these changes can be validated.
3. Implement the appropriate condition in src/ooga/data/factories/ActionFactory.java, src/ooga/data/factories/MasterRuleFactory.java, and src/ooga/data/factories/RuleFactory.java, as necessary.


#### New Themes

To add additional themes for display to the frontend, the following must be done:

1. Create a package in src/ooga/resources/skins/ matching the theme name.
2. Add to the package to include a mainmenu.css file, along with any images detailed in the file.
3. Add the theme name to src/ooga/resources/skins/supportedskins.properties.
4. If you choose to add additional card skins for your theme, the card skins must be included in a package matching your theme name under src/ooga/resources/decks/.

#### New Buttons

To add additional buttons and other functionality to the frontend, the following must be done:

1. Add the appropriate buttons to src/ooga/view/gamescreen GameScreen.java or src/ooga/view/menu/Menu.java.
2. Customize the button appearance in the different mainmenu.css files in src/ooga/resources/skins/ for the different themes.
3. Add any appropriate handling to the src/ooga/controller/Controller.java, to allow the appropriate setter to be called, or allow the class in the View to have the appropriate handler/lambda to retrieve the information.

#### Additional Features that were Considered

We considered adding the following features but were constrained based on time and budget:

1. Implementing more multiplayer functionality, with different players being able to see their own cards during their turn.
2. Allowing players to input their name when a high score was saved.
3. Implementing additional rules, such as performing actions on named cells, for other turn-based games, such as checkers or chess.
4. Implementing additional games.
5. Implementing additional and more informative error-checking for loading saved games (to allow for more backwards compatibility).