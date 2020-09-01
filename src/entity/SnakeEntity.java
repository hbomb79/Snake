package entity;

import controllers.CollisionController;
import interfaces.CollisionElement;
import main.SnakeGame;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class SnakeEntity extends Entity implements CollisionElement {
    protected final BufferedImage headImage;
    protected final BufferedImage bodyImage;
    protected final int startSize;
    protected final int startX;
    protected final int startY;
    protected final DIRECTION startDirection;
    protected final int playerId;

    protected final int partWidth;
    protected final int partHeight;

    protected int velocity = 2;
    protected DIRECTION direction;

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

    public SnakeEntity(SnakeGame game, int id, int x, int y, int size, DIRECTION dir) {
        super(game);

        headImage = game.snakeHeadImage;
        bodyImage = game.snakeBodyImage;

        partWidth = headImage.getWidth();
        partHeight = headImage.getHeight();

        startX = x;
        startY = y;
        startSize = size;
        startDirection = dir;
        playerId = id;

        increaseLength(startSize);
    }

    public SnakeEntity(SnakeGame game, int id) {
        this(game, id, SnakeGame.WIDTH/2, SnakeGame.HEIGHT/2, 10, DIRECTION.UP);
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
            snake.add(new SnakePart(this,oX + (partWidth*i*offsetX), oY + (partHeight*i*offsetY), oD));
        }
    }

    public void changeDirection(DIRECTION newDirection) {
        // To change direction, we simply insert a turn in-front of the snake; when it encounters this turn during the
        // update phase of the game loop, it will execute the turn.
        SnakePart head = snake.peekFirst();
        if(head == null) return;

        int offsetX = head.direction == DIRECTION.LEFT ? -1 : (head.direction == DIRECTION.RIGHT ? 1 : 0);
        int offsetY = head.direction == DIRECTION.UP ? -1 : (head.direction == DIRECTION.DOWN ? 1 : 0);
        SnakeTurn newTurn = new SnakeTurn(head.x + offsetX, head.y + offsetY, newDirection);
        System.out.println("[TURN] Added turn at " + (head.x + offsetX) + ", " + (head.y + offsetY));
        turns.addFirst(newTurn);
    }

    public int getPlayerId() {
        return playerId;
    }

    protected Rectangle createCollisionBox(int x, int y, int deltaX, int deltaY) {
        System.out.println("x: " + x + ", y: " + y + ", dX: " + deltaX + ", dY: " + deltaY);
        int minX = Math.min(x, deltaX);
        int maxX = Math.max(x, deltaX);
        int minY = Math.min(y, deltaY);
        int maxY = Math.max(y, deltaY);

        Rectangle collision = new Rectangle(minX, minY, Math.max(1, maxX - minX), Math.max(1, maxY - minY));

        System.out.println("New collision box created: " + collision.toString());
        return collision;
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

            // Find the velocity difference
            int xVel = s.getXVelocityCoefficient(velocity);
            int yVel = s.getYVelocityCoefficient(velocity);
            // Starting
            int sX = s.x;
            int sY = s.y;
            // Target
            int tX = s.x + xVel;
            int tY = s.y + yVel;

            // So, we know where we're trying to go (using velocity), check if this boundary intersects with a turn.
            // This boundary covers where we are, and where we're going. We use Math.min/max to ensure the rectangle
            // doesn't have a negative width and height, but instead just starts further up/left.

            //TODO Simplify boundary creation, potentially move to SnakePart class.
            Rectangle boundary = new Rectangle(Math.min(sX, tX), Math.min(sY, tY), Math.max( 1, Math.max(sX, tX) - Math.min(sX, tX) ), Math.max( 1, Math.max(sY, tY) - Math.min(sY, tY)));

            SnakeTurn t = SnakeTurn.findNextTurn(boundary, turns);
            if(t != null) {
                while (t != null) {
                    xVel = s.getXVelocityCoefficient(velocity);
                    yVel = s.getYVelocityCoefficient(velocity);
                    // We're going to collide with a 'turn'; however we might not be landing right on it, we could be
                    // moving past it. Find the difference between the snake part and the turn and apply them so the
                    // dot moves fluidly around the 'turn/corner'.
                    int dX = Math.max(t.x, tX) - Math.min(t.x, tX);
                    int dY = Math.max(t.y, tY) - Math.min(t.y, tY);

                    // Before we commit to the move, check if there's another turn between this turn, and the destination
                    //TODO Simplify boundary creation, potentially move to SnakePart class.
                    boundary.setRect(Math.min(t.x, tX), Math.min(t.y, tY), Math.max(t.x, tX) - Math.min(t.x, tX), Math.max(t.y, tY) - Math.min(t.y, tY));
                    SnakeTurn nextTurn = SnakeTurn.findNextTurn(boundary, turns, t);

                    //TODO Should be able merge blocks for same axis of movement in to one if block
                    if (t.newDirection == DIRECTION.UP) {
                        s.x = t.x;
                        s.direction = DIRECTION.UP;
                        if (nextTurn != null) {
                            // We've found another turn in the path of our planned change. Update the target X and Y
                            // and allow the loop to continue.
                            s.y = t.y;
                            tY = s.y - dY;
                        } else {
                            s.y = t.y - dY;
                        }
                    } else if (t.newDirection == DIRECTION.DOWN) {
                        s.x = t.x;
                        s.direction = DIRECTION.DOWN;
                        if (nextTurn != null) {
                            s.y = t.y;
                            tY = s.y + dY;
                        } else {
                            s.y = t.y + dY;
                        }
                    } else if (t.newDirection == DIRECTION.RIGHT) {
                        s.y = t.y;
                        s.direction = DIRECTION.RIGHT;
                        if (nextTurn != null) {
                            s.x = t.x;
                            tX = s.x + dX;
                        } else {
                            s.x = t.x + dX;
                        }
                    } else if (t.newDirection == DIRECTION.LEFT) {
                        s.y = t.y;
                        s.direction = DIRECTION.LEFT;
                        if (nextTurn != null) {
                            s.x = t.x;
                            tX = s.x - dX;
                        } else {
                            s.x = t.x - dX;
                        }
                    }

                    if (isLastPart) {
                        t.markForDestruction();
                    }

                    if(isHead) collisionBoxes.add(createCollisionBox(sX, sY, s.x, s.y));
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

    public boolean isCollisionBoxIntersecting(Rectangle collision) {
        if(collision.intersects(getBounds())) return false;

        // Check each part of the snake, return true as soon as collision occurs.
        for(SnakePart part : snake) {
            if(collision.intersects(part.getBounds())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean collidedWithGameBoundary(Rectangle collisionBox) {
        gameInstance.snakeDeath(playerId);
        return true;
    }

    @Override
    public boolean collidedWithBy(Rectangle collision, CollisionElement source) {
        if(source instanceof SnakeEntity) {
            if(source.equals(this)) {
                // We ran in to ourselves
                gameInstance.snakeDeath(playerId);
            } else {
                // Another player ran in to us
                gameInstance.snakeDeath(((SnakeEntity) source).getPlayerId());
            }

            return true;
        }

        return false;
    }

    protected void checkCollisions(LinkedList<Rectangle> collisionBoxes) {
        CollisionController collisions = gameInstance.getCollisionController();
        for(Rectangle bounds : collisionBoxes) {
            collisions.checkCollision(this, bounds);
        }
    }

    @Override
    public void update(double dt) {
        // Move depending on velocity
        LinkedList<Rectangle> collisionBoxes = moveSnake();
        for(Rectangle b : collisionBoxes) {
            System.out.println(b.toString());
        }
        SnakeTurn.destroyDwindling(turns);

        checkCollisions(collisionBoxes);
    }

    @Override
    public void paintComponent() {
        Graphics2D g = gameInstance.getGameGraphics();

        // Draw the snake head and tail
        boolean head = true;
        for(SnakePart part : snake) {
            if(head) {
                System.out.println("Drawing image HEAD at x,y: " + part.x + ", " + part.y);
                gameInstance.drawImage(headImage, part.x, part.y);
                head = false;
            } else {
                gameInstance.drawImage(bodyImage, part.x, part.y);
            }
        }

        g.draw(getBounds());
    }
}
