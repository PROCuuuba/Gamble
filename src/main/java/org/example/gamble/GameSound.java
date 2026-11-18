package org.example.gamble;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

enum GameSound {
    COIN("coin.wav"),
    DRAW("draw.wav"),
    WIN("win.wav"),
    LOSS("loss.wav"),
    CHANCE("chance.wav");

    private static final ExecutorService audio = Executors.newSingleThreadExecutor(task -> {
        Thread thread = new Thread(task, "game-audio");
        thread.setDaemon(true);
        return thread;
    });
    private static volatile boolean enabled = true;

    private final String file;
    private Clip clip;

    GameSound(String file) {
        this.file = file;
    }

    static void setEnabled(boolean enabled) {
        GameSound.enabled = enabled;
        if (!enabled) {
            audio.execute(() -> {
                for (GameSound sound : values()) {
                    if (sound.clip != null)
                        sound.clip.stop();
                }
            });
        }
    }

    void play() {
        if (enabled)
            audio.execute(this::playClip);
    }

    private void playClip() {
        if (!enabled)
            return;

        try {
            if (clip == null) {
                URL resource = GameSound.class.getResource("sounds/" + file);
                if (resource == null)
                    return;

                try (var stream = AudioSystem.getAudioInputStream(resource)) {
                    clip = AudioSystem.getClip();
                    clip.open(stream);
                }
            }
            clip.stop();
            clip.setFramePosition(0);
            if (enabled)
                clip.start();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | IllegalArgumentException
                 | SecurityException e) {
            if (clip != null) {
                clip.close();
                clip = null;
            }
        }
    }

    static void close() {
        audio.execute(() -> {
            for (GameSound sound : values()) {
                if (sound.clip != null)
                    sound.clip.close();
            }
        });
        audio.shutdown();
    }
}
