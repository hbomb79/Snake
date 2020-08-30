package entity;

import main.SnakeGame;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class SnakeEntity extends Entity {
    protected final BufferedImage headImage;
    protected final BufferedImage bodyImage;
    protected final int startSize;
    protected final int startX;
    protected final int startY;
    protected final DIRECTION startDirection;

    protected final int partWidth;
    protected final int partHeight;

    protected int velocity = 10;
    protected DIRECTION direction;
    public enum DIRECTION {
        UP,
        RIGHT,
        DOWN,
        LEFT
    }

    private static class SnakePart {
        int x;
        int y;
        DIRECTION direction;

        public SnakePart(int X, int Y, DIRECTION dir) {
            x = X;
            y = Y;
            direction = dir;
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

    public SnakeEntity(SnakeGame game, int x, int y, int size, DIRECTION dir) {
        super(game);

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

    public SnakeEntity(SnakeGame game) {
        this(game, SnakeGame.WIDTH/2, SnakeGame.HEIGHT/2, 10, DIRECTION.UP);
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
            snake.add(new SnakePart(oX + (partWidth*i*offsetX), oY + (partHeight*i*offsetY), oD));
            System.out.println("Added snake part at x,y: " + snake.peekLast().x + ", " + snake.peekLast().y);
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


    public void moveSnake() {
        // So, when moving the snake we move all parts at the same velocity. As each part of the snake
        // may be travelling a different direction, we use a linked list (turns) to keep track of user turns.

        // When a dot needs to move, we first check if it's going to pass through one of these turns (look ahead). If we
        // are going to, we can apply the turn and continue on our new path.

        // Once all dots have moved past the turn, it is destroyed from the linked list.
        for (int i = 0, snakeSize = snake.size(); i < snakeSize; i++) {
            SnakePart s = snake.get(i);
            boolean isLastPart = (i+1) == snakeSize;

            // Find the velocity difference
            int xVel = (s.direction == DIRECTION.DOWN || s.direction == DIRECTION.UP) ? 0 : velocity * (s.direction == DIRECTION.LEFT ? -1 : 1);
            int yVel = (s.direction == DIRECTION.LEFT || s.direction == DIRECTION.RIGHT) ? 0 : velocity * (s.direction == DIRECTION.UP ? -1 : 1);
            // Starting
            int sX = s.x;
            int sY = s.y;
            // Target
            int tX = s.x + xVel;
            int tY = s.y + yVel;

            System.out.println("Moving snake part " + i + " from x,y: " + sX + ", " + sY + " - to x,y: " + tX + ", " + tY);
            System.out.println("Calculated x/y vel: " + xVel + ", " + yVel + " - direction: " + s.direction);


            // So, we know where we're trying to go (using velocity), check if this boundary intersects with a turn.
            // This boundary covers where we are, and where we're going. We use Math.min/max to ensure the rectangle
            // doesn't have a negative width and height, but instead just starts further up/left.
            Rectangle boundary = new Rectangle(Math.min(sX, tX), Math.min(sY, tY), Math.max( 1, Math.max(sX, tX) - Math.min(sX, tX) ), Math.max( 1, Math.max(sY, tY) - Math.min(sY, tY)));
            System.out.println("New part; searching for turns inside boundary: " + boundary.toString());
            SnakeTurn t = SnakeTurn.findNextTurn(boundary, turns);
            System.out.println("Found turn: " + t);
            if(t != null) {
                while (t != null) {
                    xVel = (s.direction == DIRECTION.DOWN || s.direction == DIRECTION.UP) ? 0 : velocity * (s.direction == DIRECTION.LEFT ? -1 : 1);
                    yVel = (s.direction == DIRECTION.LEFT || s.direction == DIRECTION.RIGHT) ? 0 : velocity * (s.direction == DIRECTION.UP ? -1 : 1);
                    System.out.println("Moving snake part " + i + " from x,y: " + sX + ", " + sY + " - to x,y: " + tX + ", " + tY);
                    System.out.println("Calculated x/y vel: " + xVel + ", " + yVel + " - direction: " + s.direction);
                    System.out.println("Processing TURN at: " + t.x + "," + t.y + " with direction " + t.newDirection + "("+i+")");
                    System.out.println("Is part the last? " + isLastPart);
                    // We're going to collide with a 'turn'; however we might not be landing right on it, we could be
                    // moving past it. Find the difference between the snake part and the turn and apply them so the
                    // dot moves fluidly around the 'turn/corner'.
                    int dX = Math.max(t.x, tX) - Math.min(t.x, tX);
                    int dY = Math.max(t.y, tY) - Math.min(t.y, tY);
                    System.out.println("dX, dY: " + dX + ", " + dY);

                    // Before we commit to the move, check if there's another turn between this turn, and the destination
                    boundary.setRect(Math.min(t.x, tX), Math.min(t.y, tY), Math.max(t.x, tX) - Math.min(t.x, tX), Math.max(t.y, tY) - Math.min(t.y, tY));
                    SnakeTurn nextTurn = SnakeTurn.findNextTurn(boundary, turns, t);
                    System.out.println("Using rect " + boundary.toString() + " to search for turns");
                    if (t.newDirection == DIRECTION.UP) {
                        s.x = t.x;
                        s.direction = DIRECTION.UP;
                        if (nextTurn != null) {
                            System.out.println("FOUND NEW TURN INSIDE LOOP");
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
                            System.out.println("FOUND NEW TURN INSIDE LOOP");
                            s.y = t.y;
                            tY = s.y + dY;
                        } else {
                            s.y = t.y + dY;
                        }
                    } else if (t.newDirection == DIRECTION.RIGHT) {
                        s.y = t.y;
                        s.direction = DIRECTION.RIGHT;
                        if (nextTurn != null) {
                            System.out.println("FOUND NEW TURN INSIDE LOOP");
                            s.x = t.x;
                            tX = s.x + dX;
                        } else {
                            s.x = t.x + dX;
                        }
                    } else if (t.newDirection == DIRECTION.LEFT) {
                        s.y = t.y;
                        s.direction = DIRECTION.LEFT;
                        if (nextTurn != null) {
                            System.out.println("FOUND NEW TURN INSIDE LOOP");
                            s.x = t.x;
                            tX = s.x - dX;
                        } else {
                            s.x = t.x - dX;
                        }
                    }

                    if (isLastPart) {
                        System.out.println("[DESTROY] Marked turn @ x,y: " + t.x + ", " + t.y + " for destruction");
                        t.markForDestruction();
                    }
                    t = nextTurn;
                }

                // Done.
            } else {
                s.x = tX;
                s.y = tY;
            }
        }
    }

    @Override
    public void update(double dt) {
        // Move depending on velocity
        moveSnake();
        SnakeTurn.destroyDwindling(turns);
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
    }
}
