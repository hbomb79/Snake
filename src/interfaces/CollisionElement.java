package interfaces;

import java.awt.*;

public interface CollisionElement {
    public boolean collidedWithGameBoundary(Rectangle collisionBox);
    public boolean collidedWithBy(Rectangle collisionBox, CollisionElement source, Rectangle infringedBoundary);
    public Rectangle isCollisionBoxIntersecting(Rectangle collision);
    public Rectangle getBounds();
}
