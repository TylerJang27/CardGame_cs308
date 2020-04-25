# Rule XML Documentation/Instructions

* Used for card game engine
* CS 308 Spring 2020
## Authors: Andrew Krier & Tyler Jang

### Framework

#### Layout

```<?xml version="1.0" encoding="UTF-8" ?>
<data type="layout">
    <game>Game Name</game>
    <attributes>
        <players>number of players for this iteration</players>
        <screen_height># of pixels tall the screen should be, relative to a 100 pixel screen width</screen_height>
        <card_width># of pixels wide the card should be, relative to a 100 pixel screen width</card_width>
        <card_height># of pixels tall the card should be, relative to a 100 pixel screen width</card_height>
        <face_down_offset># of pixels the card offset should be if facing down, relative to a 100 pixel screen width</face_down_offset>
        <face_up_offset># of pixels the card offset should be if facing up, relative to a 100 pixel screen</face_up_offset>
    </attributes>
    <cards>
        <card>name of the card, should match the deck value, and a corresponding png in the file specified by card map</card>
    </cards>
    <card_map>
        <start>what the card file is preceeded by (eg. path to find the file) usually "game/"</start>
        <extension>what comes after the card name in locating the png (usually ".png")</extension>
    </card_map>
    <cells>
        <cell name="cellname">
            <!--name must match with the rules xml-->
            <x_val>x value of the center of the card, relative to the above dimensions</x_val>
            <y_val>y value of the center of the card, relative to the above dimensions (positive goes down)</y_val>
            <!--origin is in the top left of the screen-->
        </cell>
    </cells>
</data>
```


#### Rules
```
<?xml version="1.0" encoding="UTF-8" ?>
<data type="rule">
    <settings>
        <players></players>
        <layout></layout>
    </settings>
    <deck>
        <deck_path></deck_path>
        <deck_name></deck_name>
        <shuffle></shuffle>
        <card>
            <name></name>
            <value></value>
            <color></color>
            <suit></suit>
            <fixed></fixed>
        </card>
    </deck>
    <cell_groups>
        <group category="___">
            <cell name="_">
                <fan></fan>
                <rotation></rotation>
                <init_cards>
                    <card></card>
                </init_cards>
            </cell>
        </group>
    </cell_groups>
    <phases>
        <phase name="___">
            <phase_type></phase_type>
            <valid_donors>
                <category></category>
            </valid_donors>
            <rules>
                <rule category="___">
                    <validation>
                        <receiver>
                            <value>*</value>
                            <color>*</color>
                            <suit>*</suit>
                            <num_cards>*</num_cards>
                            <is_faceup>*</is_faceup>
                            <name>*</name>
                        </receiver>
                        <mover></mover>
                        <donor></donor>
                        <condition category="___">
                    </validation>
                    <action>
                        <receiver_dest>
                            <destination></destination>
                            <num_cards></num_cards>
                            <collapse></collapse>
                            <shuffle></shuffle>
                            <rotation></rotation>
                            <offset></offset>
                            <flip_up></flip_up>
                            except></except>
                        </receiver_dest>
                        <mover_dest></mover_dest>
                        <donor_dest></donor_dest>
                        <next_phase phase="___"></next_phase>
                    </action>
                </rule>
            </rules>
        </phase>
    </phases>
</data>
```

 1. **settings**
    * General identifiers that don't impact the backend to a large degree
    * **players**
        * Input a number, should be a positive integer
    * **layout**
        * Give datapath for the layout XML file
 2. **deck** (*order matters*)
    * List of all the cards and their attributes
    * **deck_path** (*exclusive*)
        * Give file path to the deck xml, if this is used, do not specify the deck in this file
    * **deck_name** (*optional*)
        * The name of the deck to use 
    * **shuffle** (*optional*)
        * "yes" shuffles deck in initialization
        * Anything else does not shuffle the deck
        * 'y' = shuffle deck
        * 'n' = don't shuffle deck
        * Default is 'n'
    * **card** (*optional*, *multiple*, *order matters*)
        * Gives all the important comparative attributes of a card
        * **name**
            * Card name, must match layout file, and png file in corresponding resource package
        * **value** (*optional*)
            * Integer value of the card
        * **color** (*optional*)
            * Lowercase color of the card
        * **suit** (*optional*)
            * Suit of the card (should be consistent)
        * **fixed** (*optional*)
            * Is the card of type fixed? This means it can't be moved from pile to pile
            * Usually "y" for cell anchors or outlines
            * 'y' = fixed
            * Default = not
 3. **cell_groups**
    * Gives initial conditions and definitions for cells on the table
    * **group** (*multiple*)
        * A collection of cells with similar same behavior
        * *Attribute*: **category**
            * Name of the cell group, should match with rules
        * **category**
            * What the group is named, should match with phases and rules later
        * **cell** (*multiple*)
            * What cells belong to each different category
            * *Attribute*: **name**
                * Name of the cell, should match with layout XML file and rules
            * **fan** (*optional*)
                * What direction the stack of cards fans (usually "none" if no fanning, otherwise should be a lowercase cardinal direction). This can be overwritten in init_cards
                * Uses all lowercase of enumerated direction
                * Defaults to none
            * **rotation** (*optional*)
                * How many degrees the card is rotated (usually 0)
            * **init_cards**
                * Describes what cards that particular cell begins with (can be empty)
                * **card** (*multiple*)
                    * Regex describing which cards go in this cell, starting closest to the table
                    * Format: 2 letter card name + "," + U/D if face up or down + "," + fan direction + "," + rotation double
                    * Card name can be replaced with Rd to take a random card from the deck
                    * Card name can be replaced with ** to take the rest of the deck
                    * Acceptable regex examples:
                        * **,D
                        * Rd,U,south
                        * KH,D,none,90.0
 4. **phases**
    * Group of the independent phases that the game moves through to check different states, or allow different actions
    * **phase** (*multiple* *order matters*)
        * Describes the behavior involved in one phase
        * *Attribute*: **name**
            * Phase names should match the "next phase" tags found in each phase action
            * Generally describe what is happening in the phase
        * **phase_type**
            * Dictates whether the phase executes to the rules automatically or waits for a move from frontend ("manual"/"auto")
            * Default is "manual"
        * **valid_donors**
            * Lists the different cells that are allowed to give cards in this phase
            * Cell and cell group names must match from their definitions in cell_groups
            * **category** (*optional*, *multiple*)
                * Cell or cell group name that is a valid donor
        * **rules**
            * Collection of all the rules for each category of cell
            * Rules are checked sequentially in order given in the rules group. The first to be satisfied will execute
            * **rule** (*multiple*)
                * Describes rules for one specific cell or cell_group. Multiple rules will be evaluated in order, and the first rule to be evaluated as true will execute its action(s)
                * *Attribute*: **category** 
                    * Must match cell name so that this rule applies to those cells
                * **validation** (*multiple* *optional* *order matters*)
                    * Details restricting what kinds of cards this action will receive, lots of regex-specific entries, all according to receiver/mover/donor attributes.
                    * **receiver** (*multiple* *optional* *order matters*)
                        * Restricts what the conditions for the receiver pile must be for a successful move
                        * **num_cards** (*optional*)
                            * Requirement for how many cards are in that cell and its children
                            * Give a number or it doesn't matter (*)
                        * **value** (*optional*)
                            * What relation the mover card has to the receiver card (i.e. donating 3 to a 4 should be -1)
                            * 'l' = Less than (is true if the mover is less than the receiver)
                            * 'g' = Greater than (is true if the mover is greater than the receiver)
                        * **color** (*optional*)
                            * Regex for how the color matters relative to the receiver's card color ('=' is the same, '!' is different, and '*' is it doesn't matter, can specify color)
                        * **suit** (*optional*)
                            * Same as color, just for suit instead, can also specify specific suit regex (eg. c, d, h, s)
                        * **is_faceup** (*optional*)
                            * Specifies if the receiving cards have to be faceup or not to make a valid move (y/n/*)
                            * Default is 'n'
                        * **name** (*optional*)
                            * Specifies if the receiving cards must have a specific cell or cell_group name to make a valid move (usually just '*')
                            * '*' = does not care
                    * **mover** (*multiple* *optional* *order matters*)
                        * Restricts what the conditions for the mover pile must be for a successful move
                        * All have the same tags and behavior as receiver cells
                    * **donor** (*multiple* *optional* *order matters*)
                        * All have the same tags and behavior as receiver cells
                    * **condition** (*multiple* *optional* *order matters*)
                        * Restricts what the conditions for another pile must be for a successful move
                        * Should be reserved for "auto" phases
                        * **category**
                            * Gives which cell_group or cell name the check is for
                        * **num_cards** (*optional*)
                            * Requirement for how many cards are in that cell and its children
                            * Give a number or it doesn't matter (*)
                        * **value** (*optional*)
                            * What relation the mover card has to the receiver card (i.e. donating 3 to a 4 should be -1)
                        * **color** (*optional*)
                            * Regex for how the color matters relative to the receiver's card color ('=' is the same, '!' is different, and '*' is it doesn't matter, can specify color)
                        * **suit** (*optional*)
                            * Same as color, just for suit instead, can also specify specific suit regex (eg. c, d, h, s)
                        * **is_faceup** (*optional*)
                            * Specifies if the receiving cards have to be faceup or not to make a valid move (y/n/*)
                            * Default is 'n'
                        * **name** (*optional*)
                            * Specifies if the receiving cards must have a specific cell or cell_group name to make a valid move (usually just '*')
                            * '*' = does not care
                * **action**
                    * Details what to do at the end of this phase, where each different cell goes, and what its behavior is once it gets there 
                    * **receiver_dest** (*optional*)
                        * Specify what to do with the cards in the receiver cell
                        * **destination** (*optional*)
                            * What cell the cards in the receiver should go to (usually 'm' for mover, 'r' for receiver, can be 'd' for donor, otherwise will go by cell name (if the cell exists))
                            * Defaults to the receiver
                        * **num_cards** (*optional*)
                            * Which cards from the pile to move (cardinal direction/t/b/#/*)
                            * '*' = the entire cell and its children
                            * Offset = pick the card the most in that offset (i.e. "south")
                            * 't' = the top card from all its children, maintains cell hierarchy
                            * 'b' = same as top, but for bottom of each child
                            * 'r' = a random card from the deck of the cell
                            * 'd' = the deck at the root of the current cell, collapsing its children 
                            * Postive integer = takes that many cards off the top of the leaf
                        * **collapse** (*optional*)
                            * 'y' = collapses the cells into a single deck
                            * 'n' = does not collapse the cells
                            * Defaults to 'n'
                        * **shuffle** (*optional*)
                            * Should this cell be shuffled in its new destination? (y/n/rev)
                            * Defaults to 'n'
                        * **rotation** (*optional*)
                            * What double direction (in degrees) should the cards be facing when placed in its destination location
                        * **offset** (*optional*)
                            * What offset should these cards have at their destination
                            * Enum direction = Offset in that direction
                            * "preserve" = inherit from parents at the destination
                            * Defaults to "none"
                        * **flip_up** (*optional*)
                            * Should the cards specified be flipped faceup when being added to their new destination (cardinal direction/*/n)
                            * 'n' = Flips all face down
                            * 'y' = Flips all face up
                            * Offset = Flip the card the most in that direction face up
                            * Default = do nothing
                            * Will never flip fixed cards
                        * **except** (*optional*, *multiple*)
                            * What cell names (not cell groups) should be exempted from this behavior
                    * **mover_dest** (*optional*)
                        * Specify what to do with the cards in the mover cell
                        * Tags are the same as receiver
                        * **except** (*optional*, *multiple*)
                            * What cell names (not cell groups) should be exempted from this behavior
                    * **donor_dest** (*optional*)
                        * Specify what to do with the cards in the donor cell
                        * Tags are the same as receiver
                    * **next_phase**
                        * Defines which phase the phase machine enters upon completion of this action
                        * *Attribute*: **phase**
                            * Must match one of the other given phase names in this XML file
                        * Can include the point value to award (or remove) from the player (*optional*)

        * **phase_type = "auto"**
            * Special case if the phase_type is "auto" which means it will automatically execute the rules in the order of the XML file, then execute the first action associated with the first successful rule
            * **valid_donors** (*optional*, *multiple*)
                * Same as a standard phase
            * **rules**
                * Group of rules to be automatically called over the runtime of this phase
                * **rule** (*optional*, *multiple*)
                    * Defines what conditions must be met in order for this rule to execute its action sequence
                    * *Attribute*: **category**
                        * Name does not matter
                    * **receive_rule**
                        * Group of conditions that, if met, the action in this rule will be executed
                        * **condition** (*multiple* *optional* *order matters*)
                            * *Attribute*: **category**
                                * The cell group or cell name to which this condition should apply
                            * Restricts what the conditions for another pile must be for a successful move
                            * **num_cards** (*optional*)
                                * Specifies how many cards must be in the pile ('*' means any)
                            * **direction** (*optional*)
                                * Direction in which the value of the two cards are compared, ("d" is less in value, "u" is more in value)
                            * **value** (*optional*)
                                * How much in the given direction the card compared must be
                            * **color** (*optional*)
                                * Regex for how the color matters relative to the receiver's card color ('=' is the same, '!' is different, and '*' is it doesn't matter)
                            * **suit** (*optional*)
                                * Same as color, just for suit instead, can also specify specific suit regex (eg. c, d, h, s)
                            * **is_faceup** (*optional*)
                                * Specifies if the condition cards have to be faceup or not to make a valid move (y/*)
                            * **name** (*optional*)
                                * Specifies the condition cards to check
                    * **action**
                        * **next_phase**
                            * Defines which phase the phase machine enters upon completion of this action
                            * *Attribute*: **phase**
                                * Must match one of the other given phase names in this XML file
                            * Can include the point value to award (or remove) from the player (*optional*)

### Syntax Details
    
* (*optional*) Used to denote that an element is not strictly required for a phase machine to be built
* (*exclusive*) Used to denote that if this element is present, no other elements in the sequence will be processed
* (*multiple*) Used to denote that several of this element may exist
* (*order matters*) Used to denote that the order of the sequence must strictly adhere to the order described here
* Otherwise, assume that an element is required

### 


### General Rules

In order for the game to end, a phase must be called "win" or "loss"