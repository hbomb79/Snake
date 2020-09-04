package effects;

import interfaces.EffectFrame;
import ui.Text;

import java.awt.*;

public class TextFadeEffect extends Effect {
    protected final Color color;
    protected final Text text;
    protected final int riseAmount;
    public TextFadeEffect(int x, int y, Text text, Color color, int riseAmount) {
        super(x, y);

        this.color = color;
        this.text = text;
        this.riseAmount = riseAmount;

        provideFrames(generateFrames(10));
    }

    /*
     * Generates 'amount' of frames, with each frame being slightly higher and more transparent than the last.
     * The frames will last for 'amount' of game ticks, and at the end should be completely transparent.
     */
    private EffectFrame[] generateFrames(int amount) {
        EffectFrame[] frames = new EffectFrame[amount];
        // Get the transparency value of the color. We need to get this to 0, so find out how much we need to reduce it by
        // each frame.
        double alphaReductionPerFrame = (double)color.getAlpha() / amount;
        double yRisePerFrame = (double)riseAmount / amount;
        for(int i = 0; i < amount; i++) {
            Color frameColour = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() - (int)alphaReductionPerFrame*i);
            frames[i] = new Frame(text, frameColour, (int)yRisePerFrame * i);
        }

        return frames;
    }

    private static class Frame implements EffectFrame {
        private final Text text;
        private final Color color;
        private final int yRise;
        public Frame(Text text, Color color, int yRise) {
            this.text = text;
            this.color = color;
            this.yRise = yRise;
        }

        @Override
        public void drawFrame(Graphics2D graphics, int x, int y) {
            graphics.setFont(text.getFontInstance());
            graphics.setColor(color);
            graphics.drawString(text.getText(), x, y - yRise);
        }
    }
}
