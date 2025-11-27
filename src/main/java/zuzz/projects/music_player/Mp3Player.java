package zuzz.projects.music_player;

import java.io.InputStream;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Mp3Player extends Player {

    public Mp3Player(InputStream stream) throws JavaLayerException {
        super(stream);
    }

    @Override
    public boolean decodeFrame() throws JavaLayerException {
        return super.decodeFrame();
    }
}