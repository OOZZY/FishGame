package main;

import processing.core.PApplet;
import processing.core.PVector;
import processing.core.PConstants;

/* A general Fish. */
public class Fish {
  protected PApplet p; // PApplet to draw this fish onto
  protected PVector pos, vel, acc; // position, velocity, acceleration
  protected float damp; // used to dampen velocity
  protected float width, height; // width, height
  protected int[] colors = new int[4]; // colors for tail, fins, body, head

  protected float theta = 0; // used for tail animation/oscillation

  /* Constructor: Initializes this fish with given arguments. */
  public Fish(PApplet p_, PVector pos_, PVector vel_, PVector acc_,
              float damp_, float width_, float height_) {
    p = p_;
    pos = pos_;
    vel = vel_;
    acc = acc_;
    damp = damp_;
    width = width_;
    height = height_;
  }

  /* Updates this fish. */
  public void update() {
    vel.add(acc); // update velocity
    vel.mult(damp); // dampen velocity
    pos.add(vel); // update position
    acc.mult(0); // reset acceleration
    theta += 0.08f;
  }

  /*
   * Draws this fish. If isPlayer is true, draws player fish variation.
   * Otherwise draws enemy fish variation.
   */
  public void draw(boolean isPlayer) {
    p.pushMatrix();

    p.translate(pos.x, pos.y);

    // p.noFill();
    // p.rectMode(PConstants.CENTER);
    // p.rect(0, 0, 200, 100);

    // scale to width and height specified in constructor
    p.scale(width / 200, height / 100);

    if (vel.x < 0) { // if moving left
      p.scale(-1, 1); // flip horizontally
    }

    p.stroke(0);
    p.strokeWeight(1);
    float sinTheta = PApplet.sin(theta); // a float between -1, 1

    // tail
    p.fill(colors[0]);
    for (int i = 0; i < 4; ++i) {
      if (isPlayer) {
        if (i % 2 == 0) {
          p.fill(colors[0]);
        } else {
          p.fill(0, 255, 0);
        }
      }
      // top half
      p.triangle(-100, -32 + 8*i + sinTheta*3, -75, 0, -40, 0);
      // bottom half
      p.triangle(-100, 32 - 8*i - sinTheta*3, -75, 0, -40, 0);
    }

    // body fins
    // top left
    p.fill(colors[1]);
    p.curve(100, 100, -40, -20, -10, -30, 100, 100);
    if (isPlayer) { p.fill(0, 255, 0); }
    p.curve(50, 50, -40, -20, -10, -30, 50, 50);
    // top right
    p.fill(colors[1]);
    if (isPlayer) {
      p.curve(80, 130, -10, -30, 50, -30, 80, 130);
      p.fill(0, 255, 0);
      p.curve(40, 65, -10, -30, 50, -30, 40, 65);
    } else {
      p.curve(-80, 130, -10, -30, 50, -30, -80, 130);
      p.curve(-40, 65, -10, -30, 50, -30, -40, 65);
    }
    // bottom left
    p.fill(colors[1]);
    p.curve(100, -100, -40, 20, -10, 30, 100, -100);
    if (isPlayer) { p.fill(0, 255, 0); }
    p.curve(50, -50, -40, 20, -10, 30, 50, -50);
    // bottom right
    p.fill(colors[1]);
    p.curve(175, -125, 10, 30, 40, 30, 175, -125);
    if (isPlayer) { p.fill(0, 255, 0); }
    p.curve(87.5f, -62.5f, 10, 30, 40, 30, 87.5f, -62.5f);

    // body
    p.fill(colors[2]);
    p.curve(0, 260, -60, 1, 100, 1, 40, 260); // top half
    if (isPlayer) { p.fill(255, 225, 0); }
    p.curve(0, -260, -60, -1, 100, -1, 40, -260); // bottom half

    // body scales
    for (int j = 0; j < 2; ++j) {
      p.pushMatrix();
      p.translate(-10 + j * 28, 0); // move center of grid
      p.rotate(PConstants.PI / 4); // rotate around center of grid
      // draw grid
      for (int i = 0; i < 40; i += 10) {
          p.line(i - 15, -20, i - 15, 20);
          p.line(-20, i - 15, 20, i - 15);
      }
      p.popMatrix();
    }

    // head
    p.fill(colors[3]);
    p.curve(-250, -50, 60, -28, 60, 28, -250, 50); // right curve
    p.curve(150, -50, 60, -28, 60, 28, 150, 50); // left curve (gill)
    p.noFill();
    p.curve(75, -30, 75, 0, 100, 0, 100, -30); // mouth/smile
    p.fill(0);
    p.ellipse(70, -10, 10, 10); // eye
    p.fill(255);
    p.ellipse(72, -11, 4, 4); // eye glint

    p.popMatrix();
  }

  /* Returns whether this fish is colliding with other fish. */
  public boolean isCollidingWith(Fish other) {
    if (PApplet.abs(pos.x - other.pos.x) < width/2 + other.width/2 &&
        PApplet.abs(pos.y - other.pos.y) < height/2 + other.height/2) {
      return true;
    }
    return false;
  }

  /* Returns whether this fish is colliding heads-on with other fish. */
  public boolean isCollidingHeadsOnWith(Fish other) {
    if (isCollidingWith(other)) {
      // if fish and other fish are colliding and moving in opposite directions
      if (vel.x * other.vel.x < 0) {
        return true;
      }
    }
    return false;
  }

  /* Returns whether this fish is off screen. */
  public boolean isOffScreen() {
    if (pos.x < -width/2 || pos.x > p.width + width/2 ||
        pos.y < -height/2 || pos.y > p.height + height/2) {
      return true;
    }
    return false;
  }

  /* Returns the area of the fish's bounding box. */
  public float size() {
    return width * height;
  }
}
