package main;

import processing.core.PApplet;
import processing.core.PVector;
import processing.core.PConstants;

/* Enemy fishes. */
public class EnemyFish extends Fish {
  public static final int MIN_WIDTH = 25;
  public static final int MAX_WIDTH = 175;

  private float waveTheta, waveAmp; // used for wavy movement

  /* Constructor: Initializes this enemy fish with initial settings. */
  public EnemyFish(PApplet p) {
    super(p, null, null, new PVector(0, 0), 1, 0, 0);
    width = p.random(MIN_WIDTH, MAX_WIDTH);
    height = width / 2;

    if (p.random(0, 2) > 1) {
      // move in from let side
      pos = new PVector(-width / 2, p.random(p.height));
      vel = new PVector(p.random(1, 5), p.random(-1, 1));
    } else {
      // move in from right side
      pos = new PVector(p.width + width / 2, p.random(p.height));
      vel = new PVector(-p.random(1, 5), p.random(-1, 1));
    }

    // generate random colors
    for (int i = 0; i < colors.length; ++i) {
      colors[i] = p.color((int)p.random(50, 200),
                          (int)p.random(50, 200),
                          (int)p.random(50, 200));
    }

    waveTheta = p.random(PConstants.TWO_PI); // random angle between 0 and 2*PI
    waveAmp = PApplet.abs(vel.y);
  }

  /* Updates this enemy fish. */
  public void update() {
    super.update();
    // wavy movement
    waveTheta += 0.1f;
    pos.y += PApplet.sin(waveTheta) * waveAmp;
  }

  /* Draws this enemy fish. */
  public void draw() {
    super.draw(false);
  }

  /* Detects and resolves collisions with other enemy fish. */
  public void detectAndResolveCollisionWith(EnemyFish other) {
    if (isCollidingWith(other)) {
      float angle = PApplet.atan2(pos.y - other.pos.y, pos.x - other.pos.x);
      vel.x = p.random(1, 5) * PApplet.cos(angle);
      vel.y = p.random(1) * PApplet.sin(angle);
      other.vel.x = p.random(1, 5) * PApplet.cos(angle - PConstants.PI);
      other.vel.y = p.random(1) * PApplet.sin(angle - PConstants.PI);
    }
  }
}
