package DoodleJump.GameLogic;
import javafx.scene.layout.Pane;

public class KeyboardListener {
    private Boolean wMove = false;
    private Boolean sMove = false;
    private Boolean aMove = false;
    private Boolean dMove = false;
    private Boolean canJump = true;
    private Pane selectedPane;
    private Player selectedPlayer;
    private Obstacle[] selectedObstacles;

    KeyboardListener(Pane scene, Player player, Obstacle obstacle[]) {
        selectedPane = scene;
        selectedPlayer = player;
        selectedObstacles = obstacle;
    }

    public void Start() {
        selectedPane.setOnKeyPressed(PressedKey -> {
            switch (PressedKey.getCode()) {
                case W:
                    // Move = true;
                    if (canJump == true) {
                        selectedPlayer.setyVelocity(-18);
                        // canJump = false;
                    }
                    break;
                case S:
                    sMove = true;
                    break;
                case A:
                    aMove = true;
                    break;
                case D:
                    dMove = true;
                    break;
                case SPACE:
                    selectedPlayer.setxVelocity(15);
                default:
            }
        });

        selectedPane.setOnKeyReleased(PressedKey -> {

            switch (PressedKey.getCode()) {
                case W:
                    wMove = false;
                    break;
                case S:
                    sMove = false;
                    break;
                case A:
                    aMove = false;
                    break;
                case D:
                    dMove = false;
                    break;
                case SPACE:
                    selectedPlayer.setxVelocity(5);
                    break;
                default:
            }
        });

        
    }

    public void Loop() {
        if (wMove == true)
            selectedPlayer.moveY(5, selectedObstacles);
        if (sMove == true)
            selectedPlayer.moveY(5, selectedObstacles);
        if (aMove == true)
            selectedPlayer.moveX(Player.LEFT, selectedObstacles);
        if (dMove == true)
            selectedPlayer.moveX(Player.RIGHT, selectedObstacles);

        selectedPane.requestFocus();
    }
    

}