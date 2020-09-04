package entity;

import controllers.CollisionController;
import interfaces.CollisionElement;
import main.Player;
import main.SnakeGame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class SnakeEntity extends Entity implements CollisionElement {
    protected final BufferedImage headImage;
    protected final BufferedImage bodyImage;
    protected final int startSize;
    protected final int startX;
    protected final int startY;
    protected final DIRECTION startDirection;
    protected final Player player;

    protected final int partWidth;
    protected final int partHeight;

    protected int velocity = 2;

    public enum DIRECTION {
        UP,
        RIGHT,
        DOWN,
        LEFT
    }

    private static class SnakePart {
        SnakeEntity master;
        int x;
        int y;
        DIRECTION direction;

        public SnakePart(SnakeEntity snake, int X, int Y, DIRECTION dir) {
            x = X;
            y = Y;
            direction = dir;
            master = snake;
        }

        public int getXVelocityCoefficient(int velocity) {
            return direction == DIRECTION.DOWN || direction == DIRECTION.UP ? 0 : velocity * (direction == DIRECTION.LEFT ? -1 : 1);
        }

        public int getYVelocityCoefficient(int velocity) {
            return direction == DIRECTION.LEFT || direction == DIRECTION.RIGHT ? 0 : velocity * (direction == DIRECTION.UP ? -1 : 1);
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, master.partWidth, master.partHeight);
        }
    }

    private static class SnakeTurn {
        int x;
        int y;
        DIRECTION newDirection;
        protected boolean toDestroy = false;

        public SnakeTurn(int X, int Y, DIRECTION dir) {
            x = X;
            y = Y;
            newDirection = dir;
        }

        public void markForDestruction() {
            toDestroy = true;
        }

        public static SnakeTurn findNextTurn(Rectangle bounds, LinkedList<SnakeTurn> turns, SnakeTurn excludedTurn) {
            for(SnakeTurn turn : turns) {
                if(bounds.contains(turn.x, turn.y) && !turn.toDestroy && turn != excludedTurn) { return turn; }
            }

            return null;
        }

        public static SnakeTurn findNextTurn(Rectangle bounds, LinkedList<SnakeTurn> turns) {
            for(SnakeTurn turn : turns) {
                if(bounds.contains(turn.x, turn.y) && !turn.toDestroy) { return turn; }
            }

            return null;
        }

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

    protected LinkedList<SnakePart> snake = new LinkedList<>();
    protected LinkedList<SnakeTurn> turns = new LinkedList<>();

    public SnakeEntity(SnakeGame game, int id, int x, int y, int size, DIRECTION dir, Player player) {
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

    public SnakeEntity(SnakeGame game, Player player) {
        this(game, player.getId(), 100*(player.getId()-1) + SnakeGame.WIDTH/2, SnakeGame.HEIGHT/2, 10, DIRECTION.UP, player);
    }

    /*
     * Increases snake length by adding a new part to the END of the chain
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

    public void changeDirection(DIRECTION newDirection) {
        // To change direction, we simply insert a turn in-front of the snake; when it encounters this turn during the
        // update phase of the game loop, it will execute the turn.
        SnakePart head = snake.peekFirst();
        if(head == null) return;

        int offsetX = head.direction == DIRECTION.LEFT ? -1 : (head.direction == DIRECTION.RIGHT ? 1 : 0);
        int offsetY = head.direction == DIRECTION.UP ? -1 : (head.direction == DIRECTION.DOWN ? 1 : 0);
        turns.addFirst(new SnakeTurn(head.x + offsetX, head.y + offsetY, newDirection));
    }

    public Player getPlayer() {
        return player;
    }

    public int getPlayerId() {
        return player.getId();
    }

    protected Rectangle createCollisionBox(int x, int y, int deltaX, int deltaY) {
        return new Rectangle(Math.min(x, deltaX), Math.min(y, deltaY), partWidth, partHeight);
    }

    //TODO Major cleanup of this method required
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
                    //TODO Simplify boundary creation, potentially move to SnakePart class.
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

    /*
     * Returns a rough bounding box of this snake; of course, because a snake is not always rectangular, this
     * bounding box is for preliminary checks ONLY. It should only be used to ignore potential collisions, not to confirm
     * them.
     *
     * Use isPointColliding to test if a given point is colliding with the snake.
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

    @Override
    public Rectangle isCollisionBoxIntersecting(Rectangle collision, CollisionElement source) {
        if(!collision.intersects(getBounds())) return null;

        // Check each part of the snake, return true as soon as collision occurs.
        // Ignore the head as the heads movement will always 'collide' with it's own.. movement.. obviously.
        for (int i = 0, snakeSize = snake.size(); i < snakeSize; i++) {
            SnakePart part = snake.get(i);
            if (part.getBounds().intersects(collision) && (!source.equals(this) || proveCollisionIntent(collision, part.getBounds()))) {
                return part.getBounds();
            }
        }

        return null;
    }

    @Override
    public boolean collidedWithGameBoundary(Rectangle collisionBox) {
        gameInstance.snakeDeath(getPlayer());
        return true;
    }

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

    @Override
    public void update(double dt) {
        // Move depending on velocity
        LinkedList<Rectangle> collisionBoxes = moveSnake();
        SnakeTurn.destroyDwindling(turns);

        checkCollisions(collisionBoxes);
    }

    private void checkCollisions(LinkedList<Rectangle> collisionBoxes) {
        CollisionController collisions = gameInstance.getCollisionController();
        for(Rectangle bounds : collisionBoxes) {
            collisions.checkCollision(this, bounds);
        }
    }

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
