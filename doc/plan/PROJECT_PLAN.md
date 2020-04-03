# PROJECT_PLAN.md

### API Responsibilities
Sarah: Front End External - provides user input to controller, accepts display classes such as style, layout, accepts changes in game state such as cell data and player outcomes.

Mariusz: Front End Internal - Manages movement of GUI elements such as card stacking/movements/flips, fetches user movements requested using External API

Andrew, Tyler: Controller Internal - manages the order and function of the skeleton for the program. initializes front end and triggers construction of back end. manages data flow, listens for updates given by frontend and sends them to backend, receives the resulting state from the backend and updates the front end.

Tyler, Andrew: Data External - manages the backbone of extensibility of the program. reads in xml and properties files, parses them accordingly, and creates readable classes with which both the front end styling and the back end table with internal rules can be constructed.

Maverick: Back End External - manages the hierarchy of cards to be displayed to the user, as well as the logic for which cards are movable and visible. Additionally, manages players and player score.

Maverick: Back End Internal - manages the game flow through a finite state machine of phases, dictating game rules, game logic, and automatic game actions.

### Rough Timeline

#### Sprints 
- Sprint #1 (ends Thursday, 4/9)
    - Goals: basic game functionalities working, controller able to correctly deliver limited but usable data to both front end and back end. Front end able to render simple game layout, reflect updates from controller on screen, and understand/pass basic user input correctly to controller. Back end able to update state according to user input, and maintain a state consistent with game rules and display.
    - Individual Responsibilities
        - Andrew: Initialize framework for starting up the program, and calling the appropriate classes at the appropriate times. Help Tyler with construction of rules and data processing.
        - Tyler: Manages construction of rules for 3 different games types, ensuring appropriate pipelining to backend. Completes front end styling XML pipeline, at a minimum covering read-only processing.
        - Mariusz: Develop classes for various forms of:
            - card design (image/color)
            - table shape/design
            - dashboard options (buttons, user-inputted values such as a value to bet)
            - Welcome screen with game options
            - settings that can toggle betwen light/dark mode, different languages
        - Maverick: Have cell and card data structures, as well as functional phase machine
            - Rotate between phases depending on user input
            - Have at least win, loss, and continue phases
        - Sarah: Understand/pass user input from front end to controller. Help Mariusz in rendering game layout and using display elements passed by controller.

- Sprint #2 (ends Thursday, 4/16)
    - Goals: at least two different games fully realized, with controller able to correctly load rules, display elements, and initial game states. Front end able to recognize and adapt to new cell creation and placement, and display customized buttons and start/end/high score screens. Back end able to maintain a game state consistent with multiplayer and more advanced rules/deck types.
    - Individual Responsibilities
        - Andrew: Finalize listening/updating/updating program flow. Make sure to allow multiplayer implementation to be straightforward for data/back end. Implement high score functionality. Help with data finalization.
        - Tyler: Extends XML operations to include writing to XML files (for saving games and style preferences). Extends to at least 2 other game types. Begin integrating multiple player functionality into rules.
        - Mariusz: Expand support to include:
            - different shapes (circle, triangle, etc)
            - Create support for switching to the end/high score screens, allow for game-specific high score and end screens that are parsed in via the controller. 
            - Create support for sound which can be triggered by the generation of an IMove
        - Maverick: Expand phase machine to include and parse complex rules which may depend on multiple cell states. Have stress tested cell validity regex to verify edge cases. Make a framework for all possible actions to be implemented via XML.
        - Sarah: Deploy customized buttons determined by controller input and a method for passing these inputs back to the controller when clicked by player. Help Mariusz in switching/displaying start/end/high score screens. 

- Complete (ends Friday, 4/24)
    - Goals: Diverse set of games currently stored in library. Front end shows clean user interface and fully customizable graphics. Controller/back end have support for complex game setups/play. Multiplayer and high score fully functional. 
    - Individual Responsibilities
        - Andrew: Finalize high scores, help make rules as extensible as possible and test/debug more use case games.
        - Tyler: Ensure all other necessary stored XML information is operational and pipelined, including leaderboards, customization, and house rules. Verify a rigorous stable pipeline within all parts of the program.
        - Mariusz: allow for the dashboard/user move options to be as flexible as possible by allowing for them to be presented in any location on the screen as indicated by the xml configuration, continue testing and expanding on the range of options for animating movements on the table
        - Maverick: Write additional XML files including complex game logic. Verify these are functional on the backend. Time allowing, create complex XML files for systems with simple but intricate rules and states such as Go.
        - Sarah: Ensures graphics are as customizable as possible including accepting new styling from both the player and the initial config files of the controller.