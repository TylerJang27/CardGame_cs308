# Meeting #2.1
Present: Tyler, Andrew (Data/Controller Subteam)

Date: 3/30/20

Team Name: Solitaire Confinement (Team 52 Pickup)

### Agenda
 * Data Overview and Planning
 * Test Cases
 * API Brainstorming

### Data Overview and Planning
 * Rules (Game Rules and Player Rules)
 * XML Handling
 * Players
     * Turns
     * Hands
 * Cards
     * What makes up the deck
     * Inter-Card interactions
     * Card orders??
     * Card comparison??
     * Characteristics (Suit/Value)
 * Game Chooser/Descriptions
     * Game names/descriptions for front-end
 * Languages
     * Support

### XML/Properties Files to Read From
 * Master XML for Styling/Preferences (used by Front-End)
     * Languages
     * Card Skin Pack (package of images)
         * If skin not found, front-end should refer to default card pack for deck in question, alternatively add text on top of image with the card name
         * cardskins ->
             * default ->
                 * pack52
                     * hearts5.png
                     * spadesJ.png
                 * packuno
                     * red1.png
                     * blueR.png
             * faces ->
                 * pack52
                     * hearts5.png
                     * spadesJ.png
             * poker ->
                 * pack52
                     * hearts5.png
                     * spadesJ.png
     * Dark/Light
     * Screen Size
     * Player Names?
     * Difficulty
     * Sound On/Off?
     * Etc.
 * Master XML for Each Game
     * Game Name
     * Game Description
     * Number of Players
     * Deck (pack52.xml)
     * Game Rules
     * Card Rules
     * Keep in mind future possibility of house rules (separate XML probably), e.g. solitaire draw 1/3
 * XML for Deck (e.g. pack52)
     * Number of Cards
     * Cards
         * Card Name (used for Skin retrieval)
         * Card Number
         * Card Suit
         * Card Color
 * Game Rules
     * Game Flow (TODO)
     * ______
     * ______
 * Card Rules
     * Draw Pile (location and number)
     * Discard Pile (location and number)
     * Target Pile (location and number)
         * Properties/Rules for Removal
             * ______
             * ______
             * ______
         * Properties/Rules for Entry (solitaire e.g. playable, suits, active--bypass)
             * Condition color (e.g. different, *, !)
             * Condition suit (e.g. *, same, !)
             * Condition value (e.g. -1, 1, 0)
             * Condition numcards (e.g. *, *, *)
             * Action (e.g. x y z+1, x y z+1)
             * Action displayed (e.g. layered stack, covered stack)
         * Initial States
             * E.g. Rd,Rd,Rd,Rd,Ru (or Rd*4,Ru)
     * Display Pile** (location and number)
     * Active Pile** (location and number)
 * Text/Properties Front-End Readable Descriptions
 * Front-End Skins as Necessary (see Styling XML)

### Test Cases
 * Solitaire Steps
 * War Steps
 * Blackjack Steps
 * Chess Steps

### API Brainstorming
 * XML Reader/Writer for Frontend
     * Will read/write styling info
 * XML Reader for Backend
     * Will initialize GameEngine, etc.
 * Controller
     * Initialize the whole project (launch frontend)
         * Subsequently launch backend dependent on frontend input
 * Parser for XML Rule regex *** Talk to Mav about all this
     * Communicate mostly with backend, some controller?
     * Create instance of the table
     * Rules class?
