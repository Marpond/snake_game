package snake.snake_game;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class SoundController {
    private static MediaPlayer mediaPlayer;

    /**
     * Public method used to play medias
     *
     * @param mediaName name of the media to be played
     */
    public static void play(String mediaName) {
        mediaPlayer = new MediaPlayer(new Media(new File("src/main/java/snake/snake_game/sounds/" + mediaName + ".mp3").toURI().toString()));
        mediaPlayer.play();
    }
}
