package interfaces;

import java.awt.*;

public interface CollisionElement {
    public boolean collidedWithGameBoundary(Rectangle collisionBox);
    public boolean collidedWithBy(Rectangle collisionBox, CollisionElement source);
    public boolean isCollisionBoxIntersecting(Rectangle collision);
    public Rectangle getBounds();
}
