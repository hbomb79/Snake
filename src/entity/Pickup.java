package entity;

import interfaces.CollisionElement;
import main.SnakeGame;

import java.awt.*;

/**
 * The Pickup class handles some of the backend core functionality of on-screen Pickups
 *
 * @author Harry Felton
 */
public abstract class Pickup extends Entity {
    /**
     * The position of the {@code Pickup} on the X-axis
     */
    protected int x;

    /**
     * The position of the {@code Pickup} on the Y-axis
     */
    protected int y;

    /**
     * The width of the {@code Pickup}
     */
    protected int width;

    /**
     * The height of the {@code Pickup}
     */
    protected int height;

    /**
     * Represents if the Pickup is marked to be destroyed by the {@code EntityController}
     */
    protected boolean toBeDestroyed = false;

    /**
     * The dead-zone of the game (in pixels), used to stop {@code Pickup} instances from being spawned
     * too close to the edge of the screen.
     */
    protected int spawnDeadzone = 50;

    /**
     * Constructs a {@code Pickup} instance with the information provided, and calls the init callback
     *
     * @param game The {@code SnakeGame} instance this Pickup belongs too
     * @param pX The X position of the {@code Pickup}
     * @param pY The Y position of the {@code Pickup}
     */
    public Pickup(SnakeGame game, int pX, int pY) {
        super(game);
        x = pX;
        y = pY;

        init();
    }

    /**
     * Constructs the {@code Pickup} with default position of {@code x,y(1,1)}
     *
     * @param game The {@code SnakeGame} instance this Pickup belongs too
     */
    public Pickup(SnakeGame game) {
        this(game, 1, 1);
    }

    /**
     * Called when the {@code Pickup} is ready for initialisation
     */
    public void init(){};

    /**
     * Applies the effect by destroying the Pickup via the {@code EntityController} attached to the {@code gameInstance}
     *
     * @param e The entity the effect is being applied to
     */
    public void applyEffect(Entity e) {
        destroy();
    }

    /**
     * Destroy the {@code Pickup} by scheduling it's removal via the {@code EntityController}
     */
    public void destroy(){
        gameInstance.getEntityController().destroyPickup(this);
        toBeDestroyed = true;
    }

    /**
     * Sets the X position of the pickup
     *
     * @param dX The x position
     * @return The {@code Pickup} to enable method chaining
     */
    public Pickup setX(int dX) {
        x = dX;
        return this;
    }

    /**
     * Fetch the X position of the {@code Pickup}
     * @return The X position
     */
    public int getX() {
        return x;
    }
    /**
     * Sets the Y position of the pickup
     *
     * @param dY The Y position
     * @return The {@code Pickup} to enable method chaining
     */
    public Pickup setY(int dY) {
        y = dY;
        return this;
    }

    /**
     * Fetch the Y position of the {@code Pickup}
     * @return The Y position
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the width of the pickup
     *
     * @param w The width of the pickup
     * @return The {@code Pickup} to enable method chaining
     */
    public Pickup setWidth(int w) {
        width = w;
        return this;
    }

    /**
     * Fetch the width position of the {@code Pickup}
     * @return The width position
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the height of the pickup
     *
     * @param h The height of the pickup
     * @return The {@code Pickup} to enable method chaining
     */
    public Pickup setHeight(int h) {
        height = h;
        return this;
    }

    /**
     * Fetch the height position of the {@code Pickup}
     * @return The height position
     */
    public int getHeight() {
        return height;
    }

    /**
     * Creates a {@code Rectangle} which represents the boundary of the {@code Pickup}
     *
     * @return The boundary of the pickup
     */
    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    /**
     * Checks to see if the collision box provided intersects this pickups boundary
     *
     * @param collision The collision box to test
     * @param source The source of the collision
     * @return Returns true if the collision box is proven to intersect, false otherwise
     */
    @Override
    public Rectangle isCollisionBoxIntersecting(Rectangle collision, CollisionElement source) {
        return getBounds().intersects(collision) ? getBounds() : null;
    }

    /**
     * Called when the pickup has collided with the border of the game; no action required
     * @param collisionBox The collision box of the pickup that is said to be colliding with the boundary
     * @return Returns false as the collision is being ignored
     */
    @Override
    public boolean collidedWithGameBoundary(Rectangle collisionBox) { return false; }

    /**
     * Tests that the given co-ordinates are within the eligible zone of the game (as in not outside of the deadzone)
     *
     * @param x The x co-ordinate to be tested
     * @param y The y co-ordinate to be tested
     * @return Returns true if position is valid
     */
    public boolean checkSpawnPoint(int x, int y) {
        Rectangle eligibleZone = new Rectangle(spawnDeadzone, spawnDeadzone, SnakeGame.WIDTH - (spawnDeadzone*2), SnakeGame.HEIGHT - (spawnDeadzone*2));
        return eligibleZone.contains(x,y);
    }

    /**
     * Called for each tick of the game; no action is required for this entity
     * @param dt Amount of time passed since last update
     */
    @Override
    public void update(double dt) {}
}
