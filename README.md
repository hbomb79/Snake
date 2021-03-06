# Snake Game

A simple classic snake game implemented in Java (OpenJDK 14.0.201) for a uni project (159.261 - Games Programming - Assignment 1).

All code is written by Harry Felton unless otherwise stated.

### Features
##### Smooth snake movement
This snake game takes modern twist by using a gridless, pixel precise movement system. This system is fairly complex and
is made possible by recording the players turns and having each, independantly moving part of the snake, react to these turns.

By doing away with the basic grid system, a new level of complexity is introduced to not only the gameplay, but also the development
of the movement system, and the accompanying collision logic.

*So, how does it work?*
1. The game sends and update tick to the snake.
2. The snake begins to calculate it's next move. It scans ahead by the amount of distance it's planning to travel,
and searches for any turns the player has made.
3. If a turn is found, it's executed. Of course, the snake won't always land exactly on a turn (as it's moving atleast 2 pixels
at a time), so we take care of excess distance by moving it to the new direction.
    
    If no turn is found, the snake simply moves forward
4. The snake records it's movement in the form of a collision box to be checked later
5. After the snake has finished moving, the collision boxes generated are handed to the `CollisionController` attached
to the `SnakeGame` instance, and the collision logic begins.
6.  * If a snake is found to collide with an Apple, it's length is increased and the apple respawned.

    * If a snake collides head on with another snake (two-player mode only), then a tie is reached

    * If a snake collides with another (not head-on), then the collided-with snake is the winner.
7. The cycle starts all over again

This system was exceptionally intricate to implement, and involves checking collisions for _intent_ to ensure the head of
the snake isn't simply colliding with the snake body behind it. Using a simple linked list strategy (moving the last element
to the front) would have saved a ridiculous amount of time and effort, but then, where's the fun in that?
##### Two player
This game features two-player. Simply select "Two Player" on the main menu and you and your opponent are launched in to the game.

In single player, either WASD, or the arrow keys can be used to control the single-snake. However, in two-player, the WASD keys
are reserved from Player One, and the arrow keys, for player two.
##### Animations
When an apple is picked up, the user accumulates 10 points. These points are shown on the end screen and serve as a way to
one-up yourself or your friends. Each time points are score, the point meter at the top glows yellow for 200ms, and a small
fading text effect is spawned where the apple once was.

Simple, yet effectively retro.

##### Pause screen
Accessed by hitting escape. Hit the escape key again to.. escape, the pause screen.

When the game is paused all entity updates are paused and any user input aside from the escape key is ignored; also, a nice
green overlay is displayed.

### Design Choices
#### Fragments
Taking inspiration from Android development, I've created a very basic Fragment system (package `fragment`) that maximises
code re-use and powers the UI of this game.

There are 4 fragments:
- `DeathFragment` Displayed when the game is over
- `PauseFragment` Displayed over the top of the game while it's paused, this is responsible for the overlay
- `GameFragment` Displays the current score of the player(s) above the game
- `MenuFragment` Provides the main menu UI

These fragments are dynamically activated based on the current state of the game. For example, if the game state is `GAME`,
and the game instance is paused, then the `GameFragment` and `PauseFragment` will be active, and will therefore receive update
ticks and redraw requests from the `GameEngine` superclass.

#### UI Library
Okay well maybe calling it a library is a little extreme, but to assist in the swift and effective design of the UI via
the aforementioned Fragments, I have implemented a very rudimentary UI library to enable real buttons and labels to be
displayed and interacted with by the user. These can be found in the `ui` package, and are used inside of Fragments by the
`UIController` to construct menus and overlays that the user can hover and click on.

#### Controllers
As Java classes do not support multiple-inheritance (for good reason albeit), I've opted to use a controller-based approach
to handling different aspects of the game.

There is a controller to handle the UI (`UIController`), the on-screen entities (`EntityController`), any animation effects (`EffectController`),
and to check for collisions we have the `CollisionController`. Each are fundamental, but also simple, thanks to code encapsulation and re-use.

### Documentation
All source code documentation is served [here](https://hbomb79.gitlab.io/snake-game/). This documentation
is generated by the JavaDoc tool; all documentation on that page, or under the `/doc` directory, is plucked directly
from the source code itself.

If you want to view the documentation locally, open `/snake-game/doc/index.html` in your web browser of choice.

You'll find that most of the inner-workings of this game are outlined inside of these comments.

### How To Run
Simple. Just compile and run the code, starting with `main.SnakeGame`.

Alternatively, open inside of any Java-capable IDE and run. The entry point for this game is inside of the `SnakeGame` class.


