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

4/9/2020:
* MC - Added isEmpty method to IDeck
* MC - Added setCellAtOffset, removeCellAtOffset, hasOffsetChildren, getParent, and getOffsetFromParent methods to ICell

4/10/2020:
* AK - Added accessors to numPlayers, screenRatio, cardWidthRatio, cardHeightRatio, downOffsetRatio, upOffsetRatio to
better serve the front end's needs for displaying the full game from the layout data class.

4/15/2020:
* MC - Added the copy method to ICell, IDeck, ICard
* MC - Added the findNamedCell and followNamespace methods to ICell

4/23/2020: 
* MC - Added toStorageString method to ICell, IDeck, ICard, IAttribute

4/28/2020 summary of other API_changes:
* TJ - Added setDraw to ICell so that initial population of cards could be specified on a cell-by-cell basis
* TJ - Added initializeCards to ICell to apply the function from setDraw 
* TJ - Added getTotalSize to ICell to determine how many cards were in an ICell and its children for rules purposes
* TJ - Added getAllCells to ICell to retrieve a List of an ICell and all its children
* MC - Added updateParentage to ICell to update the naming conventions of ICells after a move or remove has occurred
* TJ - Added isFixed to ICell to determine if the top card was considered to be a fixed card
* TJ - Added reverse to IDeck to allow decks to be reversed if an action merited it
* TJ - Added getName to IDeck so that the deck's name could be retrieved
* TJ - Added getCardByName to IDeck so that a card could be pre-positioned during initialization
* TJ - Added isFixed to IDeck to determine if the top card was considered to be a fixed card
* TJ - Added setFixed to ICard to set its fixed status
* TJ - Added isFixed to ICard to determine if it was considered to be a fixed card
* TJ - Added getColor to IColor to retrieve the color for rules comparison
* TJ - Added setScore to IPlayer to set a player's score during a phase change
* TJ - Added getCurrentPlayer to ITable to allow the frontend to display current scores
* TJ - Added restartGame to ITable to allow the frontend to restart the game
* TJ - Added getSaveData to ITable to allow the frontend to save a game
* TJ - Added executeMove to IPhase to allow the phase itself to proceed with a move
* TJ - Modified executeAutomaticActions in IPhase to return an IPhaseArrow instead of an IGameState
* TJ - Added getMyCellMap to IPhase to allow the current top level cells to be retrieved
* TJ - Added getMyCellGroupMap to IPhase to allow cell groups to be retrieved
* TJ - Added getMyName to IPhase to allow the phase's name to be retrieved for phase transition purposes
* TJ - added isValidDonor to IPhase as a means of quickly evaluating the first check for a phase
* TJ - added restartGame to IPhaseMachine to allow the ITable to restart the game
* TJ - removed addPhase from IPhaseMachine to prohibit behavior from being modified at runtime
* TJ - added isValidDonor to IPhaseMachine to quickly check if a donor is valid for the current phase
* TJ - added getSettings to IPhaseMachine to allow a pipeline for retrieving a game's layout
* TJ - added setCellData to APIChanges to allow cell information to be loaded
* TJ - added setPhase to APIChanges to allow a phase to be loaded
* AK - renamed setTablePath in IStyle to setTheme to better represent how skins are displayed
* AK - added getCardImagePaths in ILayout to allow the frontend to have clear locations for card skin images
* TJ - added IHighScores to store high score data with the following methods:
    * getSavedGames for retrieving all games that have available high scores
    * saveScores to update the XML with the current information
    * getScore to retrieve the available scores for a given game
    * setScore to add an additional score for a given game
* TJ - added ICardBlock for wrapper initialization with the following methods:
    * getCard to retrieve the card the wrapper class stores
    * getOffset to retrieve the offset for the initialization process
    * getFutureRotation to retrieve the rotation for the initialization process
* TJ - added Cellular to relate ICell and ICellGroup with the following methods:
    * getCellsByName to retrieve all valid cells based on a cell name or cell group name
    * isInGroup to evaluate whether the name is valid for this cellular implementation
* TJ - added ICardAction to move cards with the following methods:
    * execute to apply the IMove based on XML rules for actions
* TJ - added ICellGroup to take an Object-Oriented approach to ICells with the following methods:
    * getName to retrieve the group's name
    * getCellMap to retrieve the cells the group refers to
    * initializeAll to initialize all the cells in a cell group
* TJ - added IControlAction to retrieve phase change information from rule execution with the following methods:
    * execute to retrieve the IPhaseArrow and update score
* TJ - renamed IRule to IMasterRule and made the following changes:
    * removed checkValidAcceptor given validity is based on transactions
    * removed checkValidDonor given its relevance to phases, less so rules
    * added executeMove to allow an IMasterRule to process moves directly
    * added executeAutoActions to adequately process phase changes
    * added checkAutoRules for automatic conditional phases
    * removed checkValidTransfer as this was handled via other methods
    * removed getAcceptorRegex as regex was applied in the IMasterRule's construction
    * removed getDonorRegex as regex was applied in the IMasterRule's construction
    * removed getTransferRegex as regex was applied in the IMasterRule's construction
* TJ - added IRule as a smaller conditional check with the following methods:
    * checkValidMove to replace IMasterRule's checkValidTransfer
    * getName to retrieve the rule's name if necessary
* TJ - added ISaveConfiguration to store game saving with the following methods:
    * getGameName to retrieve the name of the game
    * getRulePath to allow reconstruction of a phase machine
    * getCurrentPhase to set the current phase
    * getScore to set the current score
    * getCellMap to restore cell states
    * writeConfiguration to save the file to a specified filepath after construction
* TJ - added IWriter with the following static helper methods for writers:
    * writeOutput to transfer output to the document
    * buildDocumentWithRoot to prepare the document
* TJ - added Factory with the following static helper methods for factories:
    * getVal to retrieve and translate a tag from an XML file


