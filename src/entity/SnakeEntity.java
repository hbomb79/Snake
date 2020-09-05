package entity;

import controllers.CollisionController;
import interfaces.CollisionElement;
import main.Player;
import main.SnakeGame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * The SnakeEntity class is responsible for moving and handling precise collision checks for the in-game snakes
 *
 * @author Harry Felton
 */
public class SnakeEntity extends Entity implements CollisionElement {
    /**
     * The image to be used for the head of the snake
     */
    protected final BufferedImage headImage;

    /**
     * The image to be used for the body parts of the snake
     */
    protected final BufferedImage bodyImage;

    /**
     * The starting size of the snake
     */
    protected final int startSize;

    /**
     * The starting X position of the snake
     */
    protected final int startX;

    /**
     * The starting Y position of the snake
     */
    protected final int startY;

    /**
     * The starting direction of the snake
     */
    protected final DIRECTION startDirection;

    /**
     * The {@code Player} instance attached to this snake
     */
    protected final Player player;

    /**
     * The width of the snake, as dictated by the width of the image used for the head/body
     */
    protected final int partWidth;

    /**
     * The height of the snake, as dictated by the height of the image used for the head/body
     */
    protected final int partHeight;

    /**
     * The velocity of the snakes movement
     */
    protected int velocity = 2;

    /**
     * An enum containing the possible directions of the snake movement
     */
    public enum DIRECTION {
        UP,
        RIGHT,
        DOWN,
        LEFT
    }

    /**
     * An inner class used to encapsulate the information of each part of the snake
     */
    private static class SnakePart {
        /**
         * The {@code SnakeEntity} the snake part belongs to
         */
        SnakeEntity master;

        /**
         * The x position of the snake part
         */
        int x;

        /**
         * The y position of the snake part
         */
        int y;

        /**
         * The direction of movement of the snake part
         */
        DIRECTION direction;

        /**
         * Constructs an instance of the {@code SnakePart} using the information provided
         *
         * @param snake The {@code SnakeEntity} this part is a part of
         * @param X The X position of this part
         * @param Y The Y position of this part
         * @param dir The direction of travel this part is moving
         */
        public SnakePart(SnakeEntity snake, int X, int Y, DIRECTION dir) {
            x = X;
            y = Y;
            direction = dir;
            master = snake;
        }

        /**
         * Used to get the velocity coefficient in the X-axis of movement.
         *
         * @param velocity The velocity of the snake
         * @return Returns 0 if not travelling on the X-axis, 1 if moving right, -1 if moving left
         */
        public int getXVelocityCoefficient(int velocity) {
            return direction == DIRECTION.DOWN || direction == DIRECTION.UP ? 0 : velocity * (direction == DIRECTION.LEFT ? -1 : 1);
        }

        /**
         * Used to get the velocity coefficient in the Y-axis of movement.
         *
         * @param velocity The velocity of the snake
         * @return Returns 0 if not travelling on the Y-axis, 1 if moving down, -1 if moving up
         */
        public int getYVelocityCoefficient(int velocity) {
            return direction == DIRECTION.LEFT || direction == DIRECTION.RIGHT ? 0 : velocity * (direction == DIRECTION.UP ? -1 : 1);
        }

        /**
         * Calculates the boundary of the {@code SnakePart}
         *
         * @return The boundary of the {@code SnakePart} as a Rectangle
         */
        public Rectangle getBounds() {
            return new Rectangle(x, y, master.partWidth, master.partHeight);
        }
    }

    /**
     * An inner class used to encapsulate a turn which needs to be made by the snake
     */
    private static class SnakeTurn {
        /**
         * The X position of the turn
         */
        int x;

        /**
         * The Y position of the turn
         */
        int y;

        /**
         * The direction that the turn needs to make the snake turn
         */
        DIRECTION newDirection;

        /**
         * Represents whether or not the turn has been completed by every {@code SnakePart}
         */
        protected boolean toDestroy = false;

        /**
         * Constructs a new {@code SnakeTurn} instance using the information provided
         *
         * @param X the X position of the turn
         * @param Y the Y position of the turn
         * @param dir The new direction of the snakes movement
         */
        public SnakeTurn(int X, int Y, DIRECTION dir) {
            x = X;
            y = Y;
            newDirection = dir;
        }

        /**
         * Marks the turn for destruction
         */
        public void markForDestruction() {
            toDestroy = true;
        }

        /**
         * Finds the next {@code SnakeTurn} inside the {@code bounds} provided that isn't the {@code SnakeTurn} {@code excludedTurn}
         * provided
         *
         * @param bounds The boundary provided to search inside of
         * @param turns The list of {@code SnakeTurn} instances to test
         * @param excludedTurn The {@code SnakeTurn} to exclude
         * @return Returns the {@code SnakeTurn} found, or null if none
         */
        public static SnakeTurn findNextTurn(Rectangle bounds, LinkedList<SnakeTurn> turns, SnakeTurn excludedTurn) {
            for(SnakeTurn turn : turns) {
                if(bounds.contains(turn.x, turn.y) && !turn.toDestroy && turn != excludedTurn) { return turn; }
            }

            return null;
        }

        /**
         * Finds the next {@code SnakeTurn} inside the {@code bounds} provided.
         *
         * @param bounds The boundary provided to search inside of
         * @param turns The list of {@code SnakeTurn} instances to test
         * @return Returns the {@code SnakeTurn} found, or null if none
         */
        public static SnakeTurn findNextTurn(Rectangle bounds, LinkedList<SnakeTurn> turns) {
            for(SnakeTurn turn : turns) {
                if(bounds.contains(turn.x, turn.y) && !turn.toDestroy) { return turn; }
            }

            return null;
        }

        /**
         * Destroys any turns that are marked for destruction from the list of turns provided
         *
         * @param turns The list of turns to search for {@code SnakeTurns} to be removed
         */
        public static void destroyDwindling(LinkedList<SnakeTurn> turns) {
            // Iterate backwards to avoid index out-of-sync issues when removing elements.
            // Also, realistically makes the most sense as the oldest turns will be at the end of the list; older=more
            // likely to be in need of destruction.
            for (int i = turns.size() - 1; i > 0; i--) {
                SnakeTurn turn = turns.get(i);
                if (turn.toDestroy) { turns.remove(i); }
            }
        }
    }

    /**
     * The parts of the snake
     */
    protected LinkedList<SnakePart> snake = new LinkedList<>();

    /**
     * A record of turns made by the snake
     */
    protected LinkedList<SnakeTurn> turns = new LinkedList<>();

    /**
     * Constructs a snake instance, sets the initial size and direction, and finds the width and height of each
     * snake part
     *
     * @param game The {@code SnakeGame} instance the snake is attached to
     * @param x The starting-x position of the snake
     * @param y The starting-y position of the snake
     * @param size The starting-size of the snake
     * @param dir The starting-direction of the snake
     * @param player The player the snake is attached to
     */
    public SnakeEntity(SnakeGame game, int x, int y, int size, DIRECTION dir, Player player) {
        super(game);
        this.player = player;

        headImage = game.snakeHeadImage;
        bodyImage = game.snakeBodyImage;

        partWidth = headImage.getWidth();
        partHeight = headImage.getHeight();

        startX = x;
        startY = y;
        startSize = size;
        startDirection = dir;

        increaseLength(startSize);
    }

    /**
     * Constructs an instance of the {@code SnakeEntity} by using default starting parameters
     *
     * @param game The {@code SnakeGame} instance the snake is attached to
     * @param player The {@code Player} instance the snake belongs to
     */
    public SnakeEntity(SnakeGame game, Player player) {
        this(game, 100*(player.getId()-1) + SnakeGame.WIDTH/2, SnakeGame.HEIGHT/2, 10, DIRECTION.UP, player);
    }

    /**
     * Increases snake length by adding a new part to the END of the chain
     *
     * @param amount The amount that the snake should be increased by
     */
    public void increaseLength(int amount) {
        SnakePart last = snake.peekLast();
        int oX, oY;
        DIRECTION oD;
        if(last == null) {
            oX = startX;
            oY = startY;
            oD = startDirection;
        } else {
            oX = last.x;
            oY = last.y;
            oD = last.direction;
        }

        int offsetX = oD == DIRECTION.LEFT ? 1 : (oD == DIRECTION.RIGHT ? -1 : 0);
        int offsetY = oD == DIRECTION.UP ? 1 : (oD == DIRECTION.DOWN ? -1 : 0);
        for (int i = 0; i < amount; i++) {
            snake.addLast(new SnakePart(this,oX + (partWidth*(i+1)*offsetX), oY + (partHeight*(i+1)*offsetY), oD));
        }
    }

    /**
     * Change the direction of the snake by inserting a {@code SnakeTurn} just in front of the snake
     *
     * @param newDirection The new direction of the snakes movement
     */
    public void changeDirection(DIRECTION newDirection) {
        // To change direction, we simply insert a turn in-front of the snake; when it encounters this turn during the
        // update phase of the game loop, it will execute the turn.
        SnakePart head = snake.peekFirst();
        if(head == null) return;

        int offsetX = head.direction == DIRECTION.LEFT ? -1 : (head.direction == DIRECTION.RIGHT ? 1 : 0);
        int offsetY = head.direction == DIRECTION.UP ? -1 : (head.direction == DIRECTION.DOWN ? 1 : 0);
        turns.addFirst(new SnakeTurn(head.x + offsetX, head.y + offsetY, newDirection));
    }

    /**
     * Fetch the attached player
     *
     * @return Returns the {@code Player} instance that the snake belongs to
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Fetch the player ID from the attached player
     *
     * @return Returns the {@code Player} instances ID
     */
    public int getPlayerId() {
        return player.getId();
    }

    /**
     * Creates a collision box representing the movement of the snake during the last update tick
     *
     * @param x The x value of the collision
     * @param y The y value of the collision
     * @param deltaX The deltaX value of the collision
     * @param deltaY The deltaY value of the collision
     * @return The collision box
     */
    protected Rectangle createCollisionBox(int x, int y, int deltaX, int deltaY) {
        return new Rectangle(Math.min(x, deltaX), Math.min(y, deltaY), partWidth, partHeight);
    }

    /**
     * Move the snake according to the {@code velocity} of the snake. Any {@code SnakeTurn} instances found
     * in the way of the snakes movement will be executed
     *
     * @return A list of collision boxes that represent the snakes movement
     */
    public LinkedList<Rectangle> moveSnake() {
        // So, when moving the snake we move all parts at the same velocity. As each part of the snake
        // may be travelling a different direction, we use a linked list (turns) to keep track of user turns.

        // When a dot needs to move, we first check if it's going to pass through one of these turns (look ahead). If we
        // are going to, we can apply the turn and continue on our new path.

        // Once all dots have moved past the turn, it is destroyed from the linked list.

        // Store rectangles that map the movement of the head of the snake for collision testing in the next step.
        // This allows us to see if the snake left the map, collided with an entity, etc.
        // We can't simply look ahead to see if it's GOING to hit something, as we don't know that there isn't a SnakeTurn
        // before then; implementing a separate system to check seems like a waste when this entire method is essentially
        // doing that already and there's no real advantage to us calculating the path of the snake twice.
        LinkedList<Rectangle> collisionBoxes = new LinkedList<>();
        for (int i = 0, snakeSize = snake.size(); i < snakeSize; i++) {
            SnakePart s = snake.get(i);
            boolean isHead = i == 0;
            boolean isLastPart = (i+1) == snakeSize;

            // Starting
            int sX = s.x;
            int sY = s.y;
            // Target
            int tX = s.x + s.getXVelocityCoefficient(velocity);
            int tY = s.y + s.getYVelocityCoefficient(velocity);

            // So, we know where we're trying to go (using velocity), check if this boundary intersects with a turn.
            // This boundary covers where we are, and where we're going. We use Math.min/max to ensure the rectangle
            // doesn't have a negative width and height, but instead just starts further up/left.

            Rectangle boundary = new Rectangle(Math.min(sX, tX), Math.min(sY, tY), Math.max( 1, Math.max(sX, tX) - Math.min(sX, tX) ), Math.max( 1, Math.max(sY, tY) - Math.min(sY, tY)));

            SnakeTurn t = SnakeTurn.findNextTurn(boundary, turns);
            if(t != null) {
                while (t != null) {
                    // We're going to collide with a 'turn'; however we might not be landing right on it, we could be
                    // moving past it. Find the difference between the snake part and the turn and apply them so the
                    // dot moves fluidly around the 'turn/corner'.
                    int dX = Math.max(t.x, tX) - Math.min(t.x, tX);
                    int dY = Math.max(t.y, tY) - Math.min(t.y, tY);

                    // Before we commit to the move, check if there's another turn between this turn, and the destination
                    boundary.setRect(Math.min(t.x, tX), Math.min(t.y, tY), Math.max(t.x, tX) - Math.min(t.x, tX), Math.max(t.y, tY) - Math.min(t.y, tY));
                    SnakeTurn nextTurn = SnakeTurn.findNextTurn(boundary, turns, t);

                    switch(t.newDirection){
                        case UP:
                            dY = dY * -1;
                        case DOWN:
                            s.x = t.x;
                            if(nextTurn != null) {
                                s.y = t.y;
                                tY = s.y + dY;
                            } else {
                                s.y = t.y + dY;
                            }

                            break;
                        case LEFT:
                            dX = dX * -1;
                        case RIGHT:
                            s.y = t.y;
                            if(nextTurn != null) {
                                s.x = t.x;
                                tX = s.x + dX;
                            } else {
                                s.x = t.x + dX;
                            }

                            break;
                    }

                    if(isLastPart)  t.markForDestruction();
                    if(isHead)      collisionBoxes.add(createCollisionBox(sX, sY, s.x, s.y));
                    s.direction =   t.newDirection;
                    t = nextTurn;
                }

                // Done.
            } else {
                s.x = tX;
                s.y = tY;

                if(isHead) collisionBoxes.add(createCollisionBox(sX, sY, tX, tY));
            }
        }

        return collisionBoxes;
    }

    /**
     * Returns a rough bounding box of this snake; of course, because a snake is not always rectangular, this
     * bounding box is for preliminary checks ONLY. It should only be used to ignore potential collisions, not to confirm
     * them.
     *
     * @return Returns the boundary
     */
    public Rectangle getBounds() {
        int minX = 0;
        int minY = 0;
        int maxX = 0;
        int maxY = 0;
        // Find the min and max parts of the snake
        for (int i = 0, snakeSize = snake.size(); i < snakeSize; i++) {
            SnakePart part = snake.get(i);
            if(i == 0) {
                minX = part.x;
                minY = part.y;
                maxX = part.x;
                maxY = part.y;
            }

            if (part.x < minX) {
                minX = part.x;
            } else if (part.x > maxX) {
                maxX = part.x;
            }
            if (part.y < minY) {
                minY = part.y;
            } else if (part.y > maxY) {
                maxY = part.y;
            }
        }

        return new Rectangle(minX, minY, maxX - minX + partWidth, maxY - minY + partHeight);
    }

    /**
     * Prove the collision provided is with intent, and not just an incidental collision caused by the snake
     * parts being close together.
     *
     * A collision is proven to have intent, if the head of the snake is heading TOWARDS the collision
     *
     * @param sourceCollision The collision box to be tested
     * @param infringedBoundary The collision box that has been collided with
     * @return Returns true if the collision should be acted upon, false otherwise
     */
    private boolean proveCollisionIntent(Rectangle sourceCollision, Rectangle infringedBoundary) {
        SnakePart head = snake.getFirst();
        Rectangle headBounds = head.getBounds();

        if(head.direction == DIRECTION.UP && headBounds.y + headBounds.height <= infringedBoundary.y + infringedBoundary.height) {
            return false;
        } else if(head.direction == DIRECTION.DOWN && headBounds.y >= infringedBoundary.y) {
            return false;
        } else if(head.direction == DIRECTION.LEFT && headBounds.x + headBounds.width <= infringedBoundary.x + infringedBoundary.width) {
            return false;
        } else return head.direction != DIRECTION.RIGHT || headBounds.x < infringedBoundary.x;
    }

    /**
     * Tests if the collision box provided is intersecting with the boundary of this snake
     *
     * @param collision The collision box to test
     * @param source The source of the collision
     * @return Returns true if the collision box is intersecting this snake, false otherwise
     */
    @Override
    public Rectangle isCollisionBoxIntersecting(Rectangle collision, CollisionElement source) {
        if(!collision.intersects(getBounds())) return null;

        // Check each part of the snake, return true as soon as collision occurs.
        // Ignore the head as the heads movement will always 'collide' with it's own.. movement.. obviously.
        for (SnakePart part : snake) {
            if (part.getBounds().intersects(collision) && (!source.equals(this) || proveCollisionIntent(collision, part.getBounds()))) {
                return part.getBounds();
            }
        }

        return null;
    }

    /**
     * Informs the game that the snake has died due to a collision with the game boundary
     *
     * @param collisionBox The collision box to test
     * @return Returns true to confirm that the collision has been consumed
     */
    @Override
    public boolean collidedWithGameBoundary(Rectangle collisionBox) {
        gameInstance.snakeDeath(getPlayer());
        return true;
    }

    /**
     * Informs the snake that it's been collided with. This method will check if the collision originated
     * from itself (loss), or if another snake has collided with this entity (win). If both snakes collide head-on,
     * a tie condition is reached.
     *
     * @param collision The collision box
     * @param source The source of the collision
     * @param infringedBoundary The boundary that has been infringed
     * @return Returns true if the collision was consumed, false otherwise
     */
    @Override
    public boolean collidedWithBy(Rectangle collision, CollisionElement source, Rectangle infringedBoundary) {
        if(source instanceof SnakeEntity) {
            if(source.equals(this)) {
                gameInstance.snakeDeath(getPlayer());
            } else {
                // Another player ran in to us
                if(source.isCollisionBoxIntersecting(snake.getFirst().getBounds(), this) != null){
                    // We collided mutually; both players lose
                    gameInstance.snakeDeath(getPlayer());
                }
                gameInstance.snakeDeath(((SnakeEntity) source).getPlayer());
            }

            return true;
        }

        return false;
    }

    /**
     * Updates the snake every tick, moves the snake, checks collisions and updates the attached {@code Player} instance
     *
     * @param dt Time since the last update
     */
    @Override
    public void update(double dt) {
        // Move depending on velocity
        LinkedList<Rectangle> collisionBoxes = moveSnake();
        SnakeTurn.destroyDwindling(turns);

        checkCollisions(collisionBoxes);

        getPlayer().update(dt);
    }

    /**
     * Checks the collisions provided by handing them off to the {@code CollisionController} attached to the {@code gameInstance}
     *
     * @param collisionBoxes A list of the collisions to be tested
     */
    private void checkCollisions(LinkedList<Rectangle> collisionBoxes) {
        CollisionController collisions = gameInstance.getCollisionController();
        for(Rectangle bounds : collisionBoxes) {
            collisions.checkCollision(this, bounds);
        }
    }

    /**
     * Draws the snake
     */
    @Override
    public void paintComponent() {
        // Draw the snake head and tail
        boolean head = true;
        for(SnakePart part : snake) {
            if(head) {
                gameInstance.drawImage(headImage, part.x, part.y);
                head = false;
            } else {
                gameInstance.drawImage(bodyImage, part.x, part.y);
            }
        }
    }
}
