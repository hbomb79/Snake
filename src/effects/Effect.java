package effects;

import controllers.EffectController;
import interfaces.EffectFrame;
import interfaces.EngineComponent;
import main.SnakeGame;

import java.util.Arrays;
import java.util.LinkedList;

public abstract class Effect implements EngineComponent {
    protected int frame = 0;
    protected int endFrame;
    protected final LinkedList<EffectFrame> frames = new LinkedList<>();
    protected final int x;
    protected final int y;
    protected final SnakeGame gameInstance;
    protected final EffectController fx;

    protected Effect(int x, int y) {
        this.x = x;
        this.y = y;

        this.gameInstance = SnakeGame.getGameInstance();
        this.fx = gameInstance.getEffectsController();
    }

    protected void provideFrames(EffectFrame[] frames) {
        this.frames.addAll(Arrays.asList(frames));
        endFrame = this.frames.size();
    }

    @Override
    public void update(double dt) {
        frame++;
        if(frame > endFrame) {
            destroyEffect();
            return;
        }
    }

    @Override
    public void paintComponent() {
        if(frame <= endFrame) frames.get(frame).drawFrame(gameInstance.getGameGraphics(), x, y);
    }

    private void destroyEffect() {
        fx.destroyEffect(this);
    }
}
