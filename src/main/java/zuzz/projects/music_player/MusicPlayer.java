package zuzz.projects.music_player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import com.mpatric.mp3agic.Mp3File;

public class MusicPlayer {

    private Mp3Player player;
    private Thread playerThread;

    private volatile boolean playing;
    private String currentFilePath;

    public MusicPlayer() {
        this.playing = false;
    }

    public void play(String filePath) {
        if (playing) {
            stop();
        }

        this.currentFilePath = filePath;
        this.playing = true;

        this.playerThread = new Thread(() -> {
            try {
                try (FileInputStream fis = new FileInputStream(currentFilePath);
                        BufferedInputStream bis = new BufferedInputStream(fis)) {

                    player = new Mp3Player(bis);
                    while (playing) {
                        if (!player.decodeFrame()) {
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                if (playing) {
                    System.err.println("Problema de reproduccion: " + e.getMessage());
                }
            } finally {
                cleanUp();
            }
        });

        playerThread.setDaemon(true);
        playerThread.start();
    }

    public void stop() {
        this.playing = false;

        if (player != null) {
            player.close();
        }
    }

    public long getTotalDuration(String filePath) {
        try {
            Mp3File mp3File = new Mp3File(filePath);
            long seconds = mp3File.getLengthInSeconds();
            return seconds * 1000;
        } catch (Exception e) {
            System.err.println("Error leyendo metadatos: " + e.getMessage());
            return 0;
        }
    }

    private void cleanUp() {
        this.playing = false;
        if (player != null) {
            player.close();
        }
        player = null;
        playerThread = null;
    }

    public int getPosition() {
        if (player != null) {
            return player.getPosition();
        }
        return -1;
    }

    public boolean isPlaying() {
        return playing;
    }
}
