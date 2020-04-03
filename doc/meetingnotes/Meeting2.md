# Meeting #2
Present: Sarah, Mariusz, Maverick, Andrew

Date: 3/30/20

Team Name: Solitaire Confinement (Team 52 Pickup)

### Agenda
* Covering what Controller group discussed
* SOLIDifying group responsibilities 
* Various Discussions
* Anything urgent about Design Plan
* Use Case Example Games
* Next Meeting Time

### Controller Group Update
https://hackmd.io/WAR_opLbRUOkJtxjG3q1Uw

### SOLIDifying group responsibilities
* Front End
    * Internal
        * displayWinScreen
    * External
        * giveTable(Data?)
        * giveStyle
        * giveLocation
* Controller
    * Internal
    * External
* Back End
    * Internal
    * External
        * onClickAndDrag
            * Give where it intersects after moving
            * If it's in the same place, interpret as a click
        * getCells
        * winStatus
        * Gettors:
            * 
            * 

* Front End
    * What is the coordinate system going to look like?
    * Does the backend need locations?
    * Needs to talk to controller/table to get info on what to display
        * Accesses name
        * Cards at cell
        * Values
    * Images from resource folders/files
    * Locations for each named cell
    * Configuration needs to know how to add new cells?
    * Show each player their own cards
    * Game specific GUI controls

* Controller
    * Make table object
        * With XML Rules
        * Handled by controller
        * Front end will have necessary methods to display the game properly
    * Initiates frontend
    * Hands table to the front end
        * 
    * Responds to front end queries
    * Turns
        * Loop of rules
        * Draw
        * Place
        * Update
            * Win?
            * Lose?
            * Update game?
        * Next turn

* Back End API
    * Table.java
        * Holds a number of cells
    * Cell.java
        * Has name saying what it is
        * Holds location
            * Doesn't hold location, holds stacking type
        * Deck - Any group of cards
            * Will handle card logic in some way
            * Cards are held in trees?
            * Can be stacked on one another
        * isMovable
    * Card.java
        * Suit
            * Symbol
            * Color
                * RGB held value
        * Value
            * Name
            * Number
            * Character
        * Front
            * Info on front
        * Back
            * Info on back
        * Some info is hidden
        * Flipped boolean

#### Discussion about whether backend should have locations or not
* Yes?
    * No
* No?
    * Can just have generic stacks and info in the backend

Verdict: No

#### Mariusz has a point to make

Duplicate Cards: Yes

#### Debate on click/clickanddrag/both
* Both
    * User interactions are smoother
    * Some cells will have just click, some cells have just clickanddrag
* Just click or clickanddrag
    * Easier to code?
    * Click and drag, then release onto itself, interpret as a click
    * Nice
    * This is it 
    * Wow
    * Coolio

Verdict: Just click and drag

### Anything urgent about Design Plan
Not at the moment

### Use Case Game Examples
* Single Player
    * Solitaire
    * Percolation
    * Minesweeper

* Multi Player
    * Poker
        * Texas Hold 'Em
        * Blackjack
    * Go Fish
    * Chess
    * War
    * Chess
    * Checkers
    * Uno
    * Skip-bo
    * Memory
    * Sequence
    * Rummy
    * BS
    * Presidents

* Simultaneous Play:
    * Spoons???
    * ERS


### Next Meeting Time (3/31)
* Sarah: No Conflicts
* Andrew: No Conflicts
* Mariusz: No Conflicts
* Maverick: Meeting at 6p EDT

8p EDT? Cool