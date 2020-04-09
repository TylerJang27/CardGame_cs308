4/6/2020:
* MC - Added the IAttribute class to allow for card suit and value to potentially be on different sides of the card.

4/7/2020:
* MC - Added the setName method to IPlayer.
* MC - Added the getName method to IPhase.

4/8/2020:
* MC - Added the peek, peekBottom, and peekCardAtIndex methods to IDeck
* MC - Added donor methods to IMove, and renamed "dragged" to "mover" to match controller terminology
* MC - Added addCell and isEmpty method in ICell
* MC - Added addDeck method in IDeck
* MC - Added setCellList and addPhase methods in IPhaseMachine
* MC - Added checkValidMove method to IRule
* MC - Modified execute to take an argument of type List<ICell> in ICardAction
* MC - Added addRule and setAutoAction to IPhase