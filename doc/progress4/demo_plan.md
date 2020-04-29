# DEMO_PLAN.md

Files in Order
* solitaire_rules.xml
* rules_schema.xsd
* solitaire_layout.xml
* default_style.xml
* default_score.xml
* ControllerTest.java
* CellTests.java
* StyleTest.java
* //DeckFactoryTest.java
* //SaveConfigurationTest.java
* ClassUML.jpg
* //ICell.java
* //IPhase.java
* //DisplayCell.java
* //PhaseMachine.java 

## Introduction Solitaire Confinement (Team 52 Pickup)
1. Introduce overall project - what game genre was chosen? 
    - ***Andrew*** Chose card games. We interpreted as any turn based game (?). Developed application calld Solitaire Confinement which allows a user to play any game currently held in is data files. 
2. Introduce yourself and what part of application you were responsible for
    - ***Sarah*** (view/frontend: rendering/interacting with game elements, clean interface for user, good game experience + faithful representation of game state, lanugages/skinning)
    - ***Andrew*** (controller: maintaining reliable data passing and communication between frontend and backend/data, also data file design and production)
    - ***Tyler*** (data files: factories for XML and building the internal black box that is the FSM)
    - ***Maverick*** (backend/game state: ...)


## Functionality

1. Run through the program with a series of planned steps  (***Here we will discuss high level what is going on under the hood, we will later be more specific on how it is done***)
    - Open the application (have it alreayd launched/ready to go)
    - Go through all themes and two languages (***Discuss visuals***) 
        - (***Sarah***: skinning the application, dynamic language changing, everything is from resource files)
        - Duke
        - SpaceRetro
        - CS308
        - BDay
        - Landscape
    - Load solitaire (***Discuss game state/initialization***)
        - (***Maverick***: how is a game state held in back end? Cells enclose cards, cells have cells as children at different offsets, etc.)
        - (***Sarah***: how are cards rendered by frontend? DisplayCell. Optimizations? Caching images + initialize vs update.)
    - Make valid/invalid move in solitaire (***Discuss user input/game play***)
        - (***Sarah***: Every cell who is not flagged as fixed by back end is clickable and draggable. When two display cells intersect, that intersection is reported to controller in the form of a move.  )
        - (***Andrew***: how is a move defined? Donor, mover, recipient. How are valid/invalid move distinguished? etc. Cue driver to make valid/invalid moves to illustrate points.)
        - (****Sarah***: Once the move is processed by controller and in turn the backend, part of frontend api's allows for a call to rerender select cells on screen to reflect the new game state. External api also allows controller to send messages back to the user regarding moves, for example, invalid move.)
    - Close tab
    - Open a new game of Solitaire, emphasize the new random cards
    - Save the solitaire
    - Load it in a new tab, show the same cards have appeared
        - (***Tyler***: _high level_ this demonstrates layout factories, etc)
    - Open 52-card-pick-up and play briefly
        - (***tyler***: talk about writing data files for different games?)
    - Close tab
    - Open War and play briefly
        - (***tyler***: talk about writing data files for different games?)
    - Close the tab
    - Show the High Score Page
        - (***tyler***: Show the different pages and scores and explain how it works)
    - Open several Rock, Paper, Scissors at once, show that it runs fairly smoothly
        - Discuss
        - Open one of the instructions.
    - Color-Blind Solitaire 
        - (***Maverick*** and segue)
2. Data Files
    - Game rules (***andrew***)
        - Purpose
        - Rules xsd schema
    - Game layout
        - Purpose
        - Layout xsd schema
    - Styling, High Scores, and Save Configurations
        - Purpose
        - Mention each has their own schema
    - CSS Files for each theme, and mention how those use images
        - (***Sarah***: run through CSS file for main menu and for gamescreen for duke theme)
3. Tests
    - Tests for Controller (print stack trace)
        - Detail the different things that happened:
            - startTable(), processMove(), getAndUpdateScoreForGame(), loadGame() all required a view
            - As a workaround, created wrapper classes for ApplicationTest and dependencies, taking in the Application object rather than the class itself--allowing the specific Controller to have its own View initialized
            - Anything with reportError() threw an IllegalStateException because of the popup
            - For reportError(), set the error output to a new ByteArrayOutputStream, which we then check its length to see if a stack trace is printed
    - Cell Tests
        - 
        - 
        - 
        - 
    - Factory Tests
        - Style (sad and happy)
            - testCreateStyle() for the StyleFactories: checking that order doesn't matter and checking that defaults are added correctly (4) or generate errors (3 and 5)
            - testStyleAccessor() verifies the efficacy of the equals() method,making sure that each accessor works as intended
            - testStyleWriter() verifies writing IStyles to XML, then reading them back to ensure that the process is **conservative**
        - Deck
            - createDeck() tests both reading in decks directly in the rules and from an additional deckPath
            - Ensures the deck matches the expectation, and that equality requires order
        - Saving
            - Much like IStyle, tests both the reading and writing elements of ISaveConfiguration objects to make sure that the process is **conservative**
    - no - Frontend (dont explain unless duvall asks :/)

4. The entire codebase (pipeline)
    - Graphic/UML (start from Controller and working downward)
        - Sarah frontend section
        - Andrew backend section

## Design
1. Goals of flexibility/openness/closedness
    - The Finite State Machine
    - XML is ***everything***
        - All cards in existence
        - Card placements
        - Game rules and finite states
            - Mention solitaire vs. colorless solitaire
        - Different players
        - Layout for display
    - Complex Cell structure (***Maverick***)
    - Watertight backend and frontend
        - no trust
2. 2 APIs
    - Phase
        -  Summary of methods
            -  executeMove(IMove move, IPlayer player) (validates all IMasterRules)
            -  executeAutomaticActions(IPlayer player, IMove move) (return an IPhaseArrow)
        -  Services open to extension
            -  getMyCellMap() for IPhaseMachine and ITable
            -  getMyName() for IPhaseMachine
            -  isAutomatic() for IPhaseMachine
        -  How the API is used across the project
            -  Internally across the backend, but its execution is integral to the functionality of our FSM
    -  USE CASE IPHASEMACHINE
        -  How has the API changed
            -  Less of a receiver, mover, donor point of view, more from a transactional point of view (IMove)
            -  More immutable (less/no setters and ways to manipulate the IPhase during gameplay)
    -  Cell
        -  Summary of methods
        -  Services open to extension
        -  How the API is used across the project
            -  
        -  How has the API changed
            -  Data manipulation (adding and removing cards/cells during runtime)

            
3. Show two use cases for the APIs
    - DisplayCell
        - Is a cell which contains a cell + frontend info (location, offset amounts, imageview, etc), so a cell is really the info specific to the game state rather than the frotnend
        - myCell.getDeck().peek() to see if card is held by cell 
        - myCell.isFixed() to determine whether to enable click/drag
        - myCell.getDeck.isFaceUp() to determine what image to attach
        - However, not able to edit these attributes
4. One element of design that has changed over time
    - Rule regex
        - Initially up in the air
        - As it was flushed out, things about mover, donor, receiver
        - Adding additional rules, including conditions for named cells for automatic phases
        - Future note to include actions on named cells

## Team

1. Result vs. Original Plans (***Tyler***)
    - Very close to goals of nearly unlimited turn-based games
    - Chess as a goal
    - Checkers as a goal (but no actions on named cells)
    - ***Maverick*** on Percolation: doable but painnnnn
        - 64,000 different phases
2. One thing each person learned from the Agile process
    - ***Maverick***: Frequent communication was excellent in creating exact action items to act on.
    - ***Tyler***: Clear team roles
    - ***Andrew***: Documentation
    - ***Sarah***: It allows us to plan for development in a less linear fashion which is consistent with the nature of the code -> for example, frontend needed more backend to be fleshed out before we could get into the specifics of making display cells or other interactive elements bc all data came from backend
3. Timeline of 4 significant events
    - Planning: Moving and Meeting2-1.md and the Data+Controller subteam (***Tyler***)
    - Planning: Clicking vs. Clicking and Dragging (***Sarah***)
    - Sprint 1: Layout and flushing out the API for Layout (100 as the width) (***Andrew***)
    - Complete: XML Documentation and more games (***Tyler***)
4. One thing each person learned from managing a large project
    - ***Maverick***: Plan big early, but be ready to scale down.
    - ***Tyler***: Ambition is great and also dangerous
    - ***Sarah***: teamwork and good communication is key
    - ***Andrew***: Intentionally checking in with individuals is vital to keeping everyone engaged
5. One thing the team worked actively to improve on and can still improve on (***Sarah***)
    - Working well before the deadline (improved during Complete)
    - Bug reporting
    - Ambition and expectations (@Christina)
    - Thinking of new features when proposing the API
        - Proposing new APIs at each sprint (and updating API_CHANGES.md regularly)
6. One thing each person learned from creating a positive team culture (***Andrew??summary***)
    - Preserving levity
    - Positive value add
    - Sharing the weight (backend)
    - Communication and documentation (Slack and meeting notes)
    - The #random channel
7. Revisit Team Contract (***Maverick***)
    - Sufficient
    - Unspoken understanding and accountability
8. One thing each person learned from communicating and solving problems (***Sarah???***)
    - Defaulting to a call instead of text communication
        - Subteam Zoom calls
        - Pseudo-Pair Programming
    - Timelines and to-do lists
    - Enjoyable meetings are important

9. Wrap-Up (***Tyler***)
    - Good job everyone.
    - Proud of our accomplishments, time to import to GitHub.
    - Questions?

10. ***Tyler*** speak with Duvall if necessary (sarah vouch for tyler, andrew also pledges to vouch)

