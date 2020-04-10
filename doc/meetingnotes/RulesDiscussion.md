# Types of Actions in Card Games

### 04/08/2020
### Present: Tyler and Andrew

* Solitaire: Draw from deck, move from hand to piles, move from pile to pile, move from pile to suit, move from hand to suits, shuffle deck
* War: Play a card, play another card, compare values of cards
* Chess: Move from one player’s piles to very specific other player’s or neutral piles, remove player’s card
* Memory: Click on a card to flip, compare flipped card values
* Blackjack: Card arithmetic. (Saving grace: few movements)

Need the source cell for a move
Ex. Reject in solitaire, Checking valid move in chess.

Phase: In a turn (Solitaire)
	Move 1 card from hand to pile
	Hand == Donor
	Pile checks receiver rules
	Doesn’t care about previous pile at all
	Cares that the card coming in doesn’t match color and is 1 value less (card comparison)
	Does action: puts card on top (fan)

Phase: In a turn (Solitaire pt. 2)
	Move 3 cards from pile to other pile
	Pile == donor
	Pile checks receiver rules against card on bottom
	Doesn’t care about previous pile at all
	Cares that the card coming in doesn’t match color and is 1 value less (card comparison)
	Does action: puts card on top (fan)
	Tricky part: Taking the right # of cards from the first pile, having the receiver rules of second pile check the right card

Phase: Both Player Turn (War)
	Move 1 card from hand(deck) to center?
	Hand (both) == donor
	Compare card values in the center deck 
	If bottom is higher => player 1, else if top is higher => player 2, else WAR
	Doesn’t care about the previous pile at all
	Doesn’t care what card it is
	Tricky part: Making sure all the comparisons are right, and handle war behavior in the state machine

Phase: White Turn (Chess)
	Move bishop diagonal to capture 3 spaces away
	Bishop == donor
	All white pieces are donors
	All non-white pieces are receivers
	Card that’s coming in came from 


Things to consider for chess movement:
* What pile it is going to 
* What pile it came from 
* What pile is moving there in the first place
* What piles are in between (but not the horsies)

XML Rules Solitaire
* Data
    * Setting eg. Players etc.
    * Deck
    * Cell Groups
        * Cell
            * Name
            * Attributes
            * Initialization (card Rd regex)
    * Phases
        * Phase 1
            * Valid Donors
            * Rule A
                * Receiver
                    * Regex
                * Action
            * Rule B
            * Rule C
            * Rule D
        * Phase 2
    * Layout (point to file)


Attributes we need to handle in the receiver regex: 
* What pile is moving there in the first place (Mover)
    * Value relationship between top of receiver and bottom of the mover
        * Up/Down
        * By how much
        * Suit
        * Color
    * Number of card in mover
    * Name
    * Group Name
* What pile it came from (Donor)
    * Value relationship between top of receiver and top of donor
        * Up/Down
        * By how much
        * Suit
        * Color
    * Name
* What pile it’s going to (Receiver)

Actions
* Destination for mover
    * Stack Up/Down
    * How many cards
    * Shuffle
    * Direction
    * Offset
* Destination for receiver
    * Stack Up/Down
    * How many cards
    * Shuffle
    * Direction
    * Offset
* Points
* Next Phase





