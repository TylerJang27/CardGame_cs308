final
====

This project implements a player for multiple related games.

Names:
 - Maverick Chung
 - Mariusz Derezinski-Choo
 - Sarah Gregorich
 - Tyler Jang
 - Andrew Krier

### Timeline

Start Date: 3/29/20

Finish Date: 4/25/20

Hours Spent: 450

### Primary Roles
Frontend
- Mariusz: Organized the different view scenes and stages, creating a pipeline for loading and saving files.
- Sarah: Organized the styling and languages for the games, creating a pipeline for languages and cell information.

Controller/Data
- Andrew: Designed XML regex and managed interactions between the frontend and backend.
- Tyler: Designed factories to parse and write XMLs so that the phase machine could be executed at runtime.

Backend
- Maverick: Designed the interactions of cells, decks, and cards with each other with a watertight backend interface.

### Resources Used
[XSD Formatting](https://www.w3schools.com/xml/schema_intro.asp)
[XML Validation in Java](https://www.journaldev.com/895/how-to-validate-xml-against-xsd-in-java)
[XML Reading in Java](https://coursework.cs.duke.edu/compsci308_2020spring/spike_simulation/blob/master/src/xml/XMLParser.java)
[TestFX in Java](https://coursework.cs.duke.edu/compsci308_2020spring/example_testing/-/blob/master/test/util/DukeApplicationTest.java)
[More TestFX in Java](https://medium.com/@mglover/java-fx-testing-with-testfx-c3858b571320)
[Error Printing in Java](https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println)

### Running the Program

Main class:

Main.java has a main() method to create a Controller instance to launch the application.

Data files needed: 
 - Rules for a game should be in data/ (of the form gamename/gamename_rules.xml)
 - Layouts for a game should be in data/ (of the form gamename/gamename_layout.xml)
 - default_score_orig.xml and default_style_orig.xml should be in data/
 - layout_schema.xsd, rules_schema.xsd, save_schema.xsd, score_schema.xsd, and style_schema.xsd must be in src/oorga/data/factories/schemas
 - languages files for messages and game names must be in src/ooga/resources/languages/messages and src/ooga/resources/languages/games, respectively and must be specified in src/ooga/resources/languages/supportedlanguages.properties
 - the card skins must be in src/ooga/resources/decks/standard/classic/
 - the background skins must be in src/ooga/resources/skins/ as specified by theme and designated accordingly in the supportedskins.properties file
 Relevant properties files for parsing XML files must be in src/ooga/resources/ 

Features implemented:
 - Games: Solitaire, Memory, War, 52 Pick Up, and Rock-Paper-Scissors
 - Top 50 High Scores saved locally and tracked across runs
 - Multiple customizable themes that apply to all games, providing a variety of color palettes
 - Saving of games to XML files
 - Loading of games from XML files
 - Pop-up instructions in your language for each game
 - Support for different languages
 - Extensible rules support with configuration detailed here: [Rules documentation](doc/XML_Documentation.md)

### Notes/Assumptions

Assumptions or Simplifications:
 - XML Files must match the format specified in the .xsd files and must follow the specifications in the [Rules documentation](doc/XML_Documentation.md).
 - If an critically invalid move or a control flow error occurs, a message will be displayed to the user.
 - Cells should not have empty decks whenever possible.
 - The user should be running a computer with at least 2 GB of RAM.
 - The rules that a game is based on should not change between the time of saving and the time of loading.
 - Game names should be lowercase.

Interesting data files:
 - data/solitaire/solitaire_rules_static_2.xml holds a solitaire game near victory
 - data/solitaire/solitaire_rules_static_3.xml throws an xsd error because the layout file is not specified
 - data/solitaire/solitaire_rules_static_4.xml throws an xml error during runtime because an invalid phase name is specified

Known Bugs:
- XML regex must be set specifically in order to produce the desired cell behavior. Cells must not be left empty.

Extra credit:
- A tribute to our professor
- The potential to implement "any turn-based card game"

### Impressions

This project covers a wide range of technologies, including XML Writing/Reading/Validating, Functional Interfaces, encapsulation, double-linked treemaps, resource binding, and css customization.

In this manner, the program serves as a creative and properly challenging capstone for the course.

In the future, we'd like to be able to extend our game to implement:
- basic gambling/betting
- checkers
- chess
- other cards/types of card games
- functionally hidden multiplayer-based play