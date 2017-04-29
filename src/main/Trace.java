package main;

import processing.core.PApplet;
import processing.core.PVector;

/* Traces that appear after fish has been eaten. */
public class Trace {
  private class TraceParticle {
    private PVector pos, vel; // position, velocity
    private float width, height; // width, height

    /* Constructor: Initializes this particle with given position. */
    public TraceParticle(PVector pos_) {
      pos = pos_;
      // random velocity, width, height
      vel = new PVector(p.random(-3, 3), p.random(-3, 3));
      width = p.random(5);
      height = p.random(5);
    }

    /* Updates this particle. */
    public void update() {
      pos.add(vel);
    }

    /* Draws this particle onto given PApplet. */
    public void draw(PApplet p) {
      p.pushMatrix();
      p.translate(pos.x, pos.y);
      p.stroke(0);
      p.strokeWeight(1);
      p.fill(0);
      p.ellipse(0, 0, width, height);
      p.popMatrix();
    }
  }

  private PApplet p; // PApplet to draw this trace onto
  private PVector pos; // position
  private TraceParticle[] particles = new TraceParticle[16];
  private int countDown = 30; // duration of trace in frames

  /* Constructor: Initializes this trace with given position. */
  public Trace(PApplet p_, PVector pos_) {
    p = p_;
    pos = pos_;

    // initialize particles
    for (int i = 0; i < particles.length; ++i) {
      particles[i] = new TraceParticle(pos.get());
    }
  }

  /* Updates this trace. */
  public void update() {
    countDown--;
    for (int i = 0; i < particles.length; ++i) {
      particles[i].update();
    }
  }

  /* Draws this trace. */
  public void draw() {
    for (int i = 0; i < particles.length; ++i) {
      particles[i].draw(p);
    }
  }

  public int countDown() { return countDown; }
}
