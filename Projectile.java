package DoodleJump;

import java.io.File;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;

public class Projectile extends ImageView {

    private static ArrayList<Projectile> removeProjectiles = new ArrayList<Projectile>();
    private static Image ProjectileImage = new Image("DoodleJump/pics/Projectile.png");
    private double Xspeed = 0;
    private double Yspeed = 0;
    private double Angle = 0;
    private static AudioClip laserClip = new AudioClip(
            new File("C:\\Users\\kimos\\Downloads\\CSE1 2nd Term\\Programming\\VS Code Java Projects\\lazer.wav").toURI()
                    .toString());
    private static AudioClip kill1Clip = new AudioClip(
            new File("C:\\Users\\kimos\\Downloads\\CSE1 2nd Term\\Programming\\VS Code Java Projects\\kill2.wav").toURI()
                    .toString());
    private static AudioClip kill2Clip = new AudioClip(
            new File("C:\\Users\\kimos\\Downloads\\CSE1 2nd Term\\Programming\\VS Code Java Projects\\kill1.wav").toURI()
                    .toString());

    Projectile() {
        super(ProjectileImage);
        this.setVisible(false);
        this.setFitHeight(15);
        this.setFitWidth(15);
    }

    public static void Loop(ArrayList<Projectile> newProjectiles, Monster newMonsters[]){
        for (Projectile pro : newProjectiles) {
            for (int i = 0; i < 2; i++) {
                pro.setX(pro.getX() + pro.getXspeed());
                pro.setY(pro.getY() - pro.getYspeed());
                pro.setRotate(pro.getRotate() + 13);
                for (Monster mon : newMonsters) {
                    if (pro.getBoundsInParent().intersects(mon.getBoundsInParent())
                            && mon.getStatus() == true) {
                        removeProjectiles.add(pro);
                        pro.setVisible(false);
                        mon.Deactivate();
                        if(Math.random() > 0.5)
                            kill1Clip.play(0.2);
                        else
                            kill2Clip.play(0.2);
                    }
                }
                if (pro.getX() > GamePane.PlayerRightBorder || pro.getX() < GamePane.PlayerLeftBorder - 50 || pro.getY() > 1080
                        || pro.getY() < -20) {
                    removeProjectiles.add(pro);
                    pro.setVisible(false);
                }
            }
        }
        newProjectiles.removeAll(removeProjectiles);
        removeProjectiles.removeAll(removeProjectiles);
    }

    public static void create(Player Doodle, ArrayList<Projectile> newProjectiles, Pane gamePane, double X, double Y) {
        if(Y > Doodle.Hitbox.getY()* GamePane.Factor/3){
            return;
        }
        Projectile nowProjectile = new Projectile();
        gamePane.getChildren().add(nowProjectile);
        newProjectiles.add(nowProjectile);

        double tempX = X - (Doodle.Hitbox.getX() + 10) * GamePane.Factor/3;
        double tempY = (Doodle.Hitbox.getY() +10 ) * GamePane.Factor/3 - Y;
        nowProjectile.Angle = Math.atan2(tempX, tempY);
        nowProjectile.Xspeed = Math.sin(nowProjectile.Angle) * 10;
        nowProjectile.Yspeed = Math.cos(nowProjectile.Angle) * 10;
        nowProjectile.setX(Doodle.Hitbox.getX()+10);
        nowProjectile.setY(Doodle.Hitbox.getY()+10);
        nowProjectile.setVisible(true);
        Doodle.shoot(nowProjectile.Angle);
        laserClip.play(0.2);
        
        // System.out.println("screen X: " + X);
        // System.out.println("screen Y: " + Y);
        // System.out.println("doodle X: " + Doodle.Hitbox.getX()* 2.5/3);
        // System.out.println("doodle Y: " + Doodle.Hitbox.getY()* 2.5/3);
        // System.out.println("traingle X: " + tempX);
        // System.out.println("traingle Y: " + tempY);
        // System.out.println("Angle: " + nowProjectile.Angle);
        // System.out.println("Speed X: " + nowProjectile.Xspeed);
        // System.out.println("Speed Y: " + nowProjectile.Yspeed);
        // System.out.println(newProjectiles.size());
        // System.out.println();

    }

    public double getXspeed() {
        return Xspeed;
    }

    public double getYspeed() {
        return Yspeed;
    }

}