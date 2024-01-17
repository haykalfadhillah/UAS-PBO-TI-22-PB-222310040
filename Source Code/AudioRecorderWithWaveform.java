package com.kelompok3.PBO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.sound.sampled.*;

public class AudioRecorderWithWaveform extends JFrame {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 300;

    private AudioRecorderWithWaveform() {
        initUI();
    }

    private void initUI() {
        setTitle("Audio Recorder with Waveform");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Tambahkan panel untuk menampilkan gelombang
        WaveformPanel waveformPanel = new WaveformPanel();
        add(waveformPanel);

        // Buat objek AudioFormat
        AudioFormat format = new AudioFormat(16000, 16, 1, true, true);

        try {
            // Buat objek DataLine.Info untuk mendapatkan informasi tentang Line yang diperlukan
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line tidak didukung");
                System.exit(0);
            }

            // Dapatkan dan buka TargetDataLine untuk merekam suara
            TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            System.out.println("Mulai merekam...");

            // Buat objek AudioInputStream untuk membaca data dari TargetDataLine
            AudioInputStream ais = new AudioInputStream(line);

            // Tampilkan output gelombang
            new Thread(() -> displayWaveform(ais, waveformPanel)).start();

            // Tunggu sampai proses rekaman selesai
            Thread.sleep(5000); // Ganti dengan durasi rekaman yang diinginkan

            // Stop dan tutup TargetDataLine
            line.stop();
            line.close();

            System.out.println("Selesai merekam.");
        } catch (LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }

        setLocationRelativeTo(null);
    }

    // Metode untuk menampilkan output gelombang
    private static void displayWaveform(AudioInputStream ais, WaveformPanel waveformPanel) {
        try {
            byte[] data = new byte[1024];
            SourceDataLine line = AudioSystem.getSourceDataLine(ais.getFormat());
            line.open();
            line.start();

            int bytesRead;

            while ((bytesRead = ais.read(data, 0, data.length)) != -1) {
                line.write(data, 0, bytesRead);
                waveformPanel.updateWaveform(data);
            }

            line.drain();
            line.close();
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AudioRecorderWithWaveform ex = new AudioRecorderWithWaveform();
            ex.setVisible(true);
        });
    }
}

class WaveformPanel extends JPanel {
    private int[] waveform;
    WaveformPanel() {
        waveform = new int[800];
        setBackground(Color.WHITE);
    }

    void updateWaveform(byte[] audioData) {
        // Konversi data audio menjadi data gelombang untuk digambar
        for (int i = 0; i < waveform.length; i++) {
            int value = (int) Math.abs(audioData[i] * 2); // Menggunakan faktor penguatan 2
            waveform[i] = value;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int xScale = getWidth() / waveform.length;

        g.setColor(Color.BLUE);

        for (int i = 0; i < waveform.length - 1; i++) {
            int x1 = i * xScale;
            int y1 = getHeight() - waveform[i];
            int x2 = (i + 1) * xScale;
            int y2 = getHeight() - waveform[i + 1];

            g.drawLine(x1, y1, x2, y2);
        }
    }
}
