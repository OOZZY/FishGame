package main;

import java.util.ArrayList;
import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import processing.core.PApplet;

/* A game like Shark! Shark! */
public class Main extends PApplet {
  static final int MAX_ENEMIES = 10;

  private ControlP5 controlP5;
  private Button playButton;
  private enum ScreenState {INTRO, PLAYING, OVER}
  ScreenState screenState;

  // game variables
  private PlayerFish player;
  private ArrayList<EnemyFish> enemies;
  private ArrayList<Trace> traces;

  public void setup() {
    size(1200, 700);
    smooth();

    controlP5 = new ControlP5(this);
    playButton = controlP5.addButton("Play");
    playButton.setValue(0);
    playButton.setSize(60, 25);
    playButton.setPosition(width / 2 - playButton.getWidth() / 2,
                           height / 4 - playButton.getHeight() / 2);
    playButton.show();
    screenState = ScreenState.INTRO;

    initializeOrResetGame();
  }

  public void draw() {
    if (screenState == ScreenState.INTRO) {
      introScreen();
    } else if (screenState == ScreenState.PLAYING) {
      updateGame();
    } else { // screenState == ScreenState.OVER
      overScreen();
    }
  }

  /* Initializes or resets all game variables. */
  void initializeOrResetGame() {
    player = new PlayerFish(this);
    enemies = new ArrayList<EnemyFish>();
    traces = new ArrayList<Trace>();
  }

  /* Draws intro screen with instructions on how to play the game. */
  void introScreen() {
    background(0, 255, 255);
    fill(0);
    textAlign(CENTER, CENTER);
    int y = height / 2;
    text("This is a game like Shark! Shark!", width / 2, y);
    text("Use arrow keys to move your fish.", width / 2, y += 40);
    text("You can score points and grow your fish by eating smaller fish.",
      width / 2, y += 40);
    text("You can lose lives by getting eaten by bigger fish.",
      width / 2, y += 20);
    text("Eating occurs when your fish and an enemy fish collide heads-on.",
      width / 2, y += 20);
    text("If your fish gets eaten while you still have lives left, ",
      width / 2, y += 20);
    text("it will respawn in the center of the window.", width / 2, y += 20);
    text("You can win by scoring 500 points or more.", width / 2, y += 40);
    text("You can also win by growing your fish beyond the enemies' max size.",
      width / 2, y += 20);
    text("You lose when your fish gets eaten while you have 0 lives left.",
      width / 2, y += 20);
    text("Click the PLAY button above to start playing.", width / 2, y += 40);
  }

  /* Draws game-over screen with a winning or lossing message. */
  void overScreen() {
    background(0, 255, 255);
    fill(0);
    textAlign(CENTER, CENTER);
    int y = height / 2;
    text("GAME OVER!", width / 2, y);
    if (player.score() >= 500 || player.width > EnemyFish.MAX_WIDTH) {
      // winning message
      text("YOU WIN!", width / 2, y += 20);
    }
    else if (player.lives() < 0) {
      // losing message
      text("YOU LOSE!", width / 2, y += 20);
    }
    text("Click the PLAY button above to play again.", width / 2, y += 20);
    player.drawHUD();
  }

  /* Updates all game variables. */
  void updateGame() {
    background(0, 255, 255);

    // spawn enemies every half-second when there are less than MAX_ENEMIES
    // enemies
    if (frameCount % 30 == 0 && enemies.size() < MAX_ENEMIES) {
      enemies.add(new EnemyFish(this));
    }

    // remove enemies that are off screen
    for (int i = 0; i < enemies.size(); ++i) {
      if (enemies.get(i).isOffScreen()) { enemies.remove(i); }
    }

    // remove finished traces
    for (int i = 0; i < traces.size(); ++i) {
      Trace trace = traces.get(i);
      if (trace.countDown() <= 0) { traces.remove(i); }
    }

    // detect and resolve collisions between player fish and enemy fishes
    for (int i = 0; i < enemies.size(); ++i) {
      player.detectAndResolveHeadsOnCollisionWith(enemies, i, traces);
    }

    // update/draw traces
    for (int i = 0; i < traces.size(); ++i) {
      Trace trace = traces.get(i);
      trace.update();
      trace.draw();
    }

    // update/draw enemies
    for (int i = 0; i < enemies.size(); ++i) {
      EnemyFish enemy = enemies.get(i);
      for (int j = i + 1; j < enemies.size(); ++j) {
        enemy.detectAndResolveCollisionWith(enemies.get(j));
      }
      enemy.update();
      enemy.draw();
    }

    // update/draw player
    player.update();
    player.draw();
    player.drawHUD();

    // check and set game over conditions
    if (player.score() >= 500 || player.lives() < 0 ||
        player.width > EnemyFish.MAX_WIDTH) {
      playButton.show();
      screenState = ScreenState.OVER;
    }

    // for debugging purposes
    // System.out.print(enemies.size() + " ");
    // System.out.print(traces.size() + " ");
    // System.out.print(player.lives() + " ");
    // System.out.print(player.score() + " ");
    // System.out.print(player.width + " ");
    // System.out.println();
  }

  /* Modifies boolean variables when certain keys are pressed. */
  public void keyPressed() {
    if (key == CODED && keyCode == UP) { KeysPressed.up = true; }
    if (key == CODED && keyCode == DOWN) { KeysPressed.down = true; }
    if (key == CODED && keyCode == LEFT) { KeysPressed.left = true; }
    if (key == CODED && keyCode == RIGHT) { KeysPressed.right = true; }
  }

  /* Modifies boolean variables when certain keys are released. */
  public void keyReleased() {
    if (key == CODED && keyCode == UP) { KeysPressed.up = false; }
    if (key == CODED && keyCode == DOWN) { KeysPressed.down = false; }
    if (key == CODED && keyCode == LEFT) { KeysPressed.left = false; }
    if (key == CODED && keyCode == RIGHT) { KeysPressed.right = false; }
  }

  public void controlEvent(ControlEvent event) {
    // do something when play button is pressed
    if (event.getController().getName() == "Play") {
      if (screenState != ScreenState.PLAYING) {
        if (screenState == ScreenState.OVER) {
          initializeOrResetGame();
        }
        playButton.hide();
        screenState = ScreenState.PLAYING;
      }
    }
  }
}
