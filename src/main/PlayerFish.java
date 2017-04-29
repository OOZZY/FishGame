package main;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

/* A Fish controlled by a player. */
public class PlayerFish extends Fish {
  // vectors saved to use for player fish's movement
  private static final PVector up = new PVector(0, -1);
  private static final PVector down = new PVector(0, 1);
  private static final PVector left = new PVector(-1, 0);
  private static final PVector right = new PVector(1, 0);

  private int lives = 5, score = 0;

  /* Constructor: Initializes this player fish with game-initial settings. */
  public PlayerFish(PApplet p) {
    super(p,
          new PVector(p.width / 2, p.height / 2), // position: center
          new PVector(0, 0), // velocity
          new PVector(0, 0), // acceleration
          0.8f, // damp factor
          100, 50); // width, height

    // colors for tail, fins, body, head
    colors[0] = p.color(255, 128, 0);
    colors[1] = p.color(255, 160, 0);
    colors[2] = p.color(255, 255, 0);
    colors[3] = p.color(255, 192, 0);
  }

  /* Updates this player fish. */
  public void update() {
    // modify acceleration based on keyboard input
    if (KeysPressed.up) { acc.add(up); }
    if (KeysPressed.down) { acc.add(down); }
    if (KeysPressed.left) { acc.add(left); }
    if (KeysPressed.right) { acc.add(right); }
    super.update();
  }

  /* Draws this player fish. */
  public void draw() {
    super.draw(true);
  }

  /*
   * Detects and resolves heads-on collisions with other enemy fish located
   * in given index in enemies ArrayList. Also spawns traces if any eating
   * occurs.
   */
  public void detectAndResolveHeadsOnCollisionWith
  (ArrayList<EnemyFish> enemies, int index, ArrayList<Trace> traces) {
    EnemyFish enemy = enemies.get(index);
    if (isCollidingHeadsOnWith(enemy)) {
      if (size() > enemy.size()) {
        // update score
        if (enemy.size() / size() > 0.9) {
          score += 40;
        } else if (enemy.size() / size() > 0.5) {
          score += 20;
        } else {
          score += 10;
        }

        traces.add(new Trace(p, enemy.pos.get())); // spawn trace
        enemies.remove(index);

        // grow player fish
        width += 2;
        height = width / 2;
      } else {
        lives--;
        traces.add(new Trace(p, pos.get())); // spawn trace

        // move player fish back to center
        pos.x = p.width / 2;
        pos.y = p.height / 2;
      }
    }
  }

  /* Draws heads-up display showing lives and score. */
  public void drawHUD() {
    p.fill(0);
    p.textAlign(PConstants.LEFT, PConstants.TOP);
    p.text("Lives: " + lives, 10, 10);
    p.text("Score: " + score, 10, 30);
    p.text("Percentage of Enemy Max Size: " +
           (width / EnemyFish.MAX_WIDTH * 100) + "%", 10, 50);
  }

  public int lives() { return lives; }
  public int score() { return score; }
}
