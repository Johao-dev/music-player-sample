package zuzz.projects.music_player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayerUI extends JFrame {

    private MusicPlayer musicPlayer;

    private JList<String> songList;
    private DefaultListModel<String> listModel;
    private JProgressBar progressBar;
    private JLabel lblStatus;
    private JButton btnPlay, btnStop, btnPrev, btnNext;

    private List<File> playlistFiles;
    private int currentIndex = -1;

    private long totalDurationsMs = 0;
    private Timer progressTimer;

    public MusicPlayerUI() {
        musicPlayer = new MusicPlayer();
        playlistFiles = new ArrayList<>();

        initUI();
        setupEvents();
    }

    private void initUI() {
        setTitle("Reproductor MP3");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        JButton btnLoad = new JButton("Abrir Carpeta");
        lblStatus = new JLabel("Sin archivos...");
        topPanel.add(btnLoad);
        topPanel.add(lblStatus);
        add(topPanel, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        songList = new JList<>(listModel);
        songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(songList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setString("00:00");
        bottomPanel.add(progressBar, BorderLayout.NORTH);

        JPanel controlPanel = new JPanel();
        btnPrev = new JButton("<<");
        btnPlay = new JButton("Play");
        btnStop = new JButton("Stop");
        btnNext = new JButton(">>");

        controlPanel.add(btnPrev);
        controlPanel.add(btnPlay);
        controlPanel.add(btnStop);
        controlPanel.add(btnNext);

        bottomPanel.add(controlPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupEvents() {
        JButton btnLoad = (JButton) ((JPanel) getContentPane().getComponent(0)).getComponent(0);
        btnLoad.addActionListener(e -> openFolderChooser());

        songList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    currentIndex = songList.getSelectedIndex();
                    playCurrentSong();
                }
            }
        });

        btnPlay.addActionListener(e -> {
            if (currentIndex == -1 && !playlistFiles.isEmpty()) {
                currentIndex = 0;
            }
            playCurrentSong();
        });

        btnStop.addActionListener(e -> stopSong());

        btnPrev.addActionListener(e -> {
            if (currentIndex > 0) {
                currentIndex--;
                playCurrentSong();
            }
        });

        btnNext.addActionListener(e -> {
            if (currentIndex < playlistFiles.size() - 1) {
                currentIndex++;
                playCurrentSong();
            }
        });

        progressTimer = new Timer(1000, e -> updateProgress());
    }

    private void openFolderChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File folder = chooser.getSelectedFile();
            loadSongsFromFolder(folder);
        }
    }

    private void loadSongsFromFolder(File folder) {
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3"));

        playlistFiles.clear();
        listModel.clear();

        if (files != null) {
            for (File file : files) {
                playlistFiles.add(file);
                listModel.addElement(file.getName());
            }
            lblStatus.setText(files.length + " canciones encontradas.");
        }
    }

    private void playCurrentSong() {
        if (currentIndex < 0 || currentIndex >= playlistFiles.size())
            return;

        File song = playlistFiles.get(currentIndex);

        songList.setSelectedIndex(currentIndex);
        songList.ensureIndexIsVisible(currentIndex);

        String songAbsolutePath = song.getAbsolutePath();
        totalDurationsMs = musicPlayer.getTotalDuration(songAbsolutePath);

        progressBar.setMaximum((int) totalDurationsMs);
        progressBar.setValue(0);

        musicPlayer.play(songAbsolutePath);

        progressTimer.start();
        btnPlay.setText("Playing...");
    }

    private void stopSong() {
        musicPlayer.stop();
        progressTimer.stop();
        progressBar.setValue(0);
        progressBar.setString("00:00");
        btnPlay.setText("Play");
    }

    private void updateProgress() {
        if (musicPlayer.isPlaying()) {
            long currentMs = musicPlayer.getPosition();

            if (totalDurationsMs > 0) {
                progressBar.setValue((int) Math.min(currentMs, totalDurationsMs));
            }

            String currentStr = formatTime(currentMs);
            String totalStr = formatTime(totalDurationsMs);

            progressBar.setString(currentStr + " / " + totalStr);
        }
    }

    private String formatTime(long millis) {
        long seconds = (millis / 1000) % 60;
        long minutes = (millis / 1000) / 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MusicPlayerUI().setVisible(true);
        });
    }
}