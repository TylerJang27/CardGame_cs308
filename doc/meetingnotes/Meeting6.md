# Meeting #6
Present: ALL

Date: 4/9/20

Team Name: Solitaire Confinement (Team 52 Pickup)

### Agenda
* What have people done
* What needs to be done before tomorrow
* What changes need to be made

### What have people done
* Back End
    * Started work on phase machine
    * Implemented card table
    * Added API_CHANGES.md doc and has been updating it along the way
    * Added IAttribute
        * Extended by suit and value
    * Worried about: don't want frontend to have access to things that would change the deck

* Front End
    * Can drag a card across the screen
    * Talked about how to organize the frontend
    * Have a lot of questions for controller
    * How front end is organized
        * View
            * Gets stuff from controller
            * Hold two stages
                * Start screen
                * Actual game
            * Game is a stack pane inside a border pane
            * Class for each element in the border pane
                * Buttons and stuff
                * Can pick where the buttons will go
                * Multiple dashboards
            * DisplayTable class
                * Display cells or cell views
                * Cells have specific locations
                * Set a location
            * End of game display
            * Dialog boxes?

* Controller
    * XML files for 
        * Style
        * Rules 
    * Factories
        * Put together data classes 

### Discussion

Layout
* Updating front end on what controller has done
    * How a layout will be passed to the front end
* (0, 0) will be top left corner
* Coordinate will refer to the center of the card
* Need offset for face up and face down cards
    * Not defined by pixels, but by
* Screen Ratio

Style
* Data class moving from xml to front end in beginning of front end initialization
* Needs

How to access cards
* Cell.getDeck.getPeak is the card
* Cell.getDeck.getPeak.isFaceUp

Move
* Needs donor, receiver, and mover

How front end gets cards to display from the cells
Offset

Cells contained by the table are closest to the table, the lowest level is the most visible

Width is 100 for ILayout, everything is relative to width

Game Loop
* Need to give frontend an event handler from controller to be updated when a move is ready to update the table state
* Event handler to know when the game is started or to start a new game