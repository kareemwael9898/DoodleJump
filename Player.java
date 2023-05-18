package DoodleJump;

import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import java.io.File;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Player extends ImageView {

    private double lastXPostion = 0;
    private double lastYPostion = 0;
    private double yVelocity = 0;
    private double xVelocity = 5;
    private double jumpHeight = -20;
    private double Score = 0;
    private double xHitBoxOffset = 0;
    private Point2D initialPosition = new Point2D(262, 940);
    private Boolean hasSomething = false;
    private Boolean canFlip = true;
    private boolean shooting = false;

    public void setCanFlip(Boolean canFlip) {
        this.canFlip = canFlip;
    }

    static private Image playerTiles = new Image("DoodleJump/pics/DoodleTile.png");

    private static AudioClip pop = new AudioClip(
            new File("C:\\Users\\kimos\\Downloads\\CSE1 2nd Term\\Programming\\VS Code Java Projects\\jump.wav").toURI()
                    .toString());
    private double accerlationOfGravity = 10;
    static final int LEFT = -1;
    static final int RIGHT = 1;

    Rectangle Hitbox = new Rectangle(GamePane.LeftBorder + initialPosition.getX(), initialPosition.getY(), 40, 59);
    ImageView Nozzle = new ImageView(new Image("DoodleJump/pics/Nozzle.png"));

    Player() {
        super(playerTiles);
        this.setFitHeight(60);
        this.setFitWidth(60);
        this.setX(GamePane.LeftBorder + initialPosition.getX());
        this.setY(initialPosition.getY());
        Hitbox.setVisible(false);
        Hitbox.setFill(Color.color(0, 0, 0, 0.5));
        this.setViewport(new Rectangle2D(0, 0, 92, 90));
        Nozzle.xProperty().bind(Hitbox.xProperty().add(10));
        Nozzle.yProperty().bind(Hitbox.yProperty().subtract(14));
        Nozzle.setRotate(90);
        Nozzle.setFitHeight(78);
        Nozzle.setFitWidth(20);
        Nozzle.setVisible(false);
    }

    public void moveY(int distance, Obstacle newObstacles[], PowerUp newPowerUp[]) {

        for (int i = 0; i < Math.abs(yVelocity); i++) {
            for (int index = 0; index < newObstacles.length; index++) {
                if (Hitbox.getBoundsInParent().intersects(newObstacles[index].getBoundsInParent()) && Hitbox.getY()
                        + Hitbox.getHeight() < (GamePane.GameScreenHeight - GamePane.GameScreenHeightOffset)) {
                    if (Hitbox.getY() + Hitbox.getHeight() == newObstacles[index].getY() && distance > 0) {
                        Hitbox.setY(lastYPostion);
                        // canJump = true;
                        yVelocity = jumpHeight;
                        pop.play(0.2);
                        // direction = -1;
                        return;
                    }
                }
            }

            for (int index = 0; index < newPowerUp.length; index++) {
                if (Hitbox.getBoundsInParent().intersects(newPowerUp[index].getBoundsInParent())) {
                    newPowerUp[index].Execute(this, Hitbox, distance, lastYPostion, jumpHeight);

                }
            }

            lastYPostion = Hitbox.getY();

            if (distance < 0) {
                Hitbox.setY(Hitbox.getY() - 1);
            }
            if (distance > 0) {
                Hitbox.setY(Hitbox.getY() + 1);
            }
            this.setY(Hitbox.getY());
        }
    }

    public void moveX(int direction, Obstacle newObstacles[]) {

        for (int i = 0; i < Math.abs(xVelocity); i++) {
            for (int index = 0; index < newObstacles.length; index++) {
                if (Hitbox.getBoundsInParent().intersects(newObstacles[index].getBoundsInParent())) {
                    if (Hitbox.getY() + Hitbox.getHeight() == newObstacles[index].getY() && direction > 0) {
                        Hitbox.setX(lastXPostion);
                        // canJump = true;
                        return;
                    }
                }
            }

            lastXPostion = Hitbox.getX();

            if (direction == -1) {
                Hitbox.setX(Hitbox.getX() - 1);
                if (canFlip == true && shooting == false) {
                    this.setViewport(new Rectangle2D(0, 90, 92, 90));
                    xHitBoxOffset = 20;
                }

                if (this.getX() < GamePane.PlayerLeftBorder - Hitbox.getWidth()) {
                    Hitbox.setX(GamePane.PlayerRightBorder);
                }
            }

            if (direction == 1) {
                Hitbox.setX(Hitbox.getX() + 1);
                if (canFlip == true && shooting == false) {
                    this.setViewport(new Rectangle2D(0, 0, 92, 90));
                    xHitBoxOffset = 0;
                }

                if (Hitbox.getX() > GamePane.PlayerRightBorder) {
                    Hitbox.setX(GamePane.PlayerLeftBorder - Hitbox.getWidth());
                }
            }

            this.setX(Hitbox.getX() - xHitBoxOffset);
        }

    }

    public void screenScroll(Obstacle newObstacles[], ImageView BG, ImageView BG2) {

        double damping = 0.05;

        if (this.getY() < (GamePane.GameScreenHeight / 1.8)) {
            for (int i = 0; i < (GamePane.GameScreenHeight / 1.8) - this.getY(); i++) {
                for (int index = 0; index < newObstacles.length; index++) {
                    if (newObstacles[index].getActivated() == false
                            && newObstacles[index].getY() > GamePane.GameScreenHeight) {
                        newObstacles[index].setVisible(false);
                    } else {
                        newObstacles[index].setY(newObstacles[index].getY() + damping);

                    }

                }
                Hitbox.setY(Hitbox.getY() + damping);
                this.setY(this.getY() + damping);
                Score += damping;
                BG.setY(BG.getY() + 0.005);
                BG2.setY(BG.getY() - GamePane.GameScreenHeight);
            }

            for (int index = 0; index < newObstacles.length; index++) {
                if (newObstacles[index].getActivated() == false
                        && newObstacles[index].getY() > GamePane.GameScreenHeight) {

                } else {
                    newObstacles[index].setY(Math.ceil(newObstacles[index].getY()));
                }

            }
            Hitbox.setY(Math.ceil(Hitbox.getY()));
            this.setY(Math.ceil(this.getY()));
            Score = Math.ceil(Score);
            BG.setY(Math.ceil(BG.getY()));
            BG2.setY(BG.getY() - GamePane.GameScreenHeight);

        }
    }

    public void gravityCycle(Obstacle newObstacles[], PowerUp newPowerUp[], Monster newMonsters[]) {
        if (yVelocity < accerlationOfGravity)
            yVelocity++;
        this.moveY((int) yVelocity, newObstacles, newPowerUp);
    }

    public void shoot(double Angle) {

        Timeline delay = new Timeline(new KeyFrame(Duration.millis(10), e -> {
                this.setViewport(new Rectangle2D(0, 180, 92, 90));
                xHitBoxOffset = 10;
                this.setX(Hitbox.getX() - xHitBoxOffset);
                this.Nozzle.setRotate(Math.toDegrees(Angle));
                this.Nozzle.setVisible(true);
                this.shooting = true;
        }));

        delay.setOnFinished(e -> {
            this.setViewport(new Rectangle2D(0, 90, 92, 90));
            xHitBoxOffset = 20;
            this.setX(Hitbox.getX() - xHitBoxOffset);
            this.Nozzle.setVisible(false);
            this.shooting = false;
        });
        delay.setCycleCount(20);
        delay.playFromStart();
        //delay.setDelay(Duration.millis(2000));

    }

    public void setyVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
    }

    public double getxVelocity() {
        return xVelocity;
    }

    public void setxVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }

    public double getScore() {
        return Score;
    }

    public Boolean getHasSomething() {
        return hasSomething;
    }

    public void setHasSomething(Boolean hasSomething) {
        this.hasSomething = hasSomething;
    }

}
