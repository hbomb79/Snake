package controllers;

import effects.Effect;
import main.SnakeGame;

import java.util.LinkedList;

public class EffectController extends Controller {
    protected LinkedList<Effect> effects = new LinkedList<>();
    protected LinkedList<Effect> effectsToRemove = new LinkedList<>();
    protected LinkedList<Effect> effectsToSpawn = new LinkedList<>();

    public EffectController(SnakeGame g) {
        super(g);
    }

    public void spawnEffect(Effect effect) {
        effectsToSpawn.add(effect);
    }

    public void destroyEffect(Effect effect) {
        effectsToRemove.add(effect);
    }

    private void removeEffects() {
        effects.removeAll(effectsToRemove);
        effectsToRemove.clear();
    }
    private void spawnEffects() {
        effects.addAll(effectsToSpawn);
        effectsToSpawn.clear();
    }

    public void update(double dt){
        spawnEffects();
        for(Effect effect : effects) {
            effect.update(dt);
        }

        removeEffects();
    }

    public void redraw(){
        for(Effect effect : effects) {
            effect.paintComponent();
        }
    }
}
