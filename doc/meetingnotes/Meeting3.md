# Meeting #3
Present: All

Date: 3/31/20

Team Name: Solitaire Confinement (Team 52 Pickup)

### Agenda
* Finish the Design Plan
    * DESIGN_PLAN.md
        * Done?
        * Link:
    * Implementation Plan
        * Done?
        * Link:
    * User Interface Wireframe (Front End)
        * Done?
        * Link:
    * Use Cases (8/Person)
        * Done?
        * Link:
    * APIs
        * Done?
        * Link:
    * API Examples
        * Done?
        * Link:
    * Example Data
        * Done?
        * Link:

## DESIGN PLAN
### DESIGN_PLAN.md

* Introduction
* Overview
* Design Goals
* Example Games
* Design Considerations

### Implementation Plan



### User Interface Wireframe

* Front end will do it 

### Use Cases



### APIs

Some borrowed from Meeting 2

* Front End
    * Internal
        * displayWinScreen
    * External
        * giveTable(Data?)
        * giveStyle
        * giveLocation
* Controller/Data
    * Internal
    * External
        * Rules
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

### API Examples



### Example Data
#### Write an XML File for Solitaire

* What needs to be included

* What won't be included (basic case)
    * Turns
    * Betting
    * 

### Rules Discussion
#### Maverick, Tyler, Andrew
* What types of rules need to exist
    * Move card from cell to cell
    * Different types of cells
    * What movements are allowed and not allowed
    * What cards can be flipped
    * What cards belong to other cards
    * How turns are dictated
        * Phases
        * Each phase has rules

* Table
    * List of phases
        * State machine
        * Some data structure
    * Index of which phase it's one
    * Start cell
    * End cell
    * Is the move valid?
        * No?
            * Respond accordingly
        * Yes?
            * Respond accordingly

* What's in a phase
    * Table gets start cell and end cell
    * Tree Node for flexibility and directionality

* Types of piles
    * Generic Pile Class?
        * Flexible?
        * Need to be specific about rules from XML

* Game XML
    * Cells with Names
    * Phases with Names

* Phase
    * Name
    * State machine
    * Valid Donor Cells
    * Valid Receptor Cells
    * ^ both could be all
    * Rules
        * Define movement
            * REGEX for concrete definitions
            * Ex. Draw from deck
                * 
        * 

Want a phase machine
Has logic of phases
Each phases has rules
Rule defines a movement from cell to cell.

Rule class from XML is given by controller in the constructor of the table