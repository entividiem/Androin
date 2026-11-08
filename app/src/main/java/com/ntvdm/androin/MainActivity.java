package com.ntvdm.androin;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

//  android 1.0 sucks balls

public class MainActivity extends Activity {

    private static final int SAMPLE_RATE = 44100;
    private static final double FREQUENCY = 1000.0; // 1 kHz test tone
    private static final short AMPLITUDE = 12000;    // leaves headroom, avoids clipping

    private static final int MODE_OFF = 0;
    private static final int MODE_LEFT = 1;
    private static final int MODE_RIGHT = 2;

    private AudioTrack audioTrack;
    private Thread playThread;
    private volatile int soundMode = MODE_OFF;
    private volatile boolean soundRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Android " + android.os.Build.VERSION.RELEASE);

        // wall of find
        TextView text1 = (TextView) findViewById(R.id.androidtext);
        TextView text2 = (TextView) findViewById(R.id.androidtext2);
        TextView text3 = (TextView) findViewById(R.id.androidtext3);
        TextView text4 = (TextView) findViewById(R.id.androidtext4);
        TextView text5 = (TextView) findViewById(R.id.androidtext5);
        TextView text6 = (TextView) findViewById(R.id.androidtext6);
        TextView text8 = (TextView) findViewById(R.id.androidtext8);
        TextView text9 = (TextView) findViewById(R.id.androidtext9);
        TextView text10 = (TextView) findViewById(R.id.androidtext10);


        text1.setText(android.os.Build.BRAND + " " + android.os.Build.MODEL);
        text2.setText("Root: " + (isRooted() ? "damn u rooted" : "no root sir"));

        android.app.ActivityManager am =
                (android.app.ActivityManager) getSystemService(ACTIVITY_SERVICE);

        android.app.ActivityManager.MemoryInfo memInfo =
                new android.app.ActivityManager.MemoryInfo();

        am.getMemoryInfo(memInfo);

        long availableMegs = memInfo.availMem / (1024 * 1024);

        long totalMegs = -1;

        // code i stole of some random forum
        try {
            java.io.BufferedReader reader =
                    new java.io.BufferedReader(
                            new java.io.FileReader("/proc/meminfo"));

            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("MemTotal:")) {
                    String[] parts = line.split("\\s+");
                    totalMegs = Long.parseLong(parts[1]) / 1024;
                    break;
                }
            }

            reader.close();
        } catch (Exception e) {
            // couldn't read total RAM for some reason
        }

        text3.setText("RAMies: " + availableMegs + " MB / " + totalMegs + " MB");



        long seconds = android.os.SystemClock.elapsedRealtime() / 1000;
        long hours = seconds / 3600; //by 3600 ms
        text4.setText("suffering for " + hours + " hours");

        // ARCHITECUTTURE // + cpu core count/clock speed in one line
        text5.setText("conputa chip type shi: " + System.getProperty("os.arch")
                + " (" + readCpuInfo() + ")");

        text6.setText("bord: " + Build.BOARD);


        // storage space, internal app dir so it works with no permissions
        text8.setText("storage: " + readStorageInfo());

        // full kernel build string, more detail than Build.VERSION gives you
        text9.setText("kernel: " + readKernelVersion());

        // tablet check, the resource system does the 600dp math for us
        // (see res/values*/bools.xml), safe all the way down to API 1
        boolean isTablet = getResources().getBoolean(R.bool.is_tablet);
        text10.setText("Is Tablet: " + (isTablet ? "yes, big boi" : "nah, phone"));


        // mandetory gjinka check
        final ImageView mascot = (ImageView) findViewById(R.id.mascot_gjinka);

        if (mascot != null) {
            mascot.setVisibility(View.VISIBLE);

            int sdk = Integer.parseInt(android.os.Build.VERSION.SDK);

            if (sdk >= 37) { // LATEST ASSHOLE
                mascot.setImageResource(R.drawable.gjinka_cynthia);
            } else if (sdk == 36) { // android sixteenie TURKIYE TURKIYE
                mascot.setImageResource(R.drawable.gjinka_berice);
            } else if (sdk == 35) { // android 15
                mascot.setImageResource(R.drawable.gjinka_vannela);
            } else if (sdk == 34) { // upside down WHAT
                mascot.setImageResource(R.drawable.gjinka_upsica);
            } else if (sdk == 33) { // Tirashitsu
                mascot.setImageResource(R.drawable.gjinka_tifu);
            } else if (sdk >= 31 && sdk <= 32) { // snowbro
                mascot.setImageResource(R.drawable.gjinka_snowie);
            } else if (sdk == 30) { // android 11
                mascot.setImageResource(R.drawable.gjinka_reveca);
            } else if (sdk == 29) { // android 10
                mascot.setImageResource(R.drawable.gjinka_quincie);
            } else if (sdk == 28) { // the pie is a lie a lie a lie
                mascot.setImageResource(R.drawable.gjinka_paifira);
            } else if (sdk >= 26 && sdk <= 27) { // octopus head
                mascot.setImageResource(R.drawable.gjinka_oaklee);
            } else if (sdk >= 24 && sdk <= 25) { // material girl
                mascot.setImageResource(R.drawable.gjinka_nina);
            } else if (sdk == 23) { // fluff v6
                mascot.setImageResource(R.drawable.gjinka_marshymo);
            } else if (sdk >= 21 && sdk <= 22) { // material girl
                mascot.setImageResource(R.drawable.gjinka_lollypo);
            } else if (sdk == 19 || sdk == 20) { // kitkat
                mascot.setImageResource(R.drawable.gjinka_kathie);
            } else if (sdk >= 16 && sdk <= 18) { // jerly
                mascot.setImageResource(R.drawable.gjinka_jellybo);
            } else if (sdk == 14 || sdk == 15) { // Ice Scream Sandbitch
                mascot.setImageResource(R.drawable.gjinka_icelyn);
            } else if (sdk >= 11 && sdk <= 13) { // Honeybomb
                mascot.setImageResource(R.drawable.gjinka_haniku);
            } else if (sdk == 9 || sdk == 10) { // Ginger
                mascot.setImageResource(R.drawable.gjinka_genny);
            } else if (sdk == 8) { // Foyo
                mascot.setImageResource(R.drawable.gjinka_froyo);
            } else if (sdk >= 5 && sdk <= 7) { // Eclairiese
                mascot.setImageResource(R.drawable.gjinka_eclairee);
            } else if (sdk == 4) { // doh'nut
                mascot.setImageResource(R.drawable.gjinka_donna);
            } else if (sdk == 3) { // some ass Cupcake
                mascot.setImageResource(R.drawable.gjinka_capri);
            } else {
                // else??? 1 and 2 = JUNKDROID
                mascot.setImageResource(R.drawable.gjinka_apphie);
            }


            mascot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.widget.Toast.makeText(
                            MainActivity.this,
                            "stop tapping",
                            android.widget.Toast.LENGTH_SHORT                   ).show();
                }
            });


        }


        // here lies my hope
        // start of code i didnt write underneath :(

        // speaker channel tester buttons
        Button btnLeft = (Button) findViewById(R.id.btnLeft);
        Button btnRight = (Button) findViewById(R.id.btnRight);
        Button btnBoth = (Button) findViewById(R.id.btnBoth);
        Button btnStop = (Button) findViewById(R.id.btnStop);

        btnLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { soundMode = MODE_LEFT; }
        });
        btnRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { soundMode = MODE_RIGHT; }
        });
        // Re-purpose the button (formerly btnBoth) for haptics
        Button btnVibrate = (Button) findViewById(R.id.btnBoth);

        btnVibrate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Stop audio so it doesn't clash
                soundMode = MODE_OFF;

                android.os.Vibrator vibrator = (android.os.Vibrator) getSystemService(VIBRATOR_SERVICE);
                        @SuppressWarnings("deprecation")
                        long duration = 400;
                        vibrator.vibrate(duration);
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { soundMode = MODE_OFF; }
        });

        startAudioThread();
    }


    @SuppressWarnings("deprecation")
    private void startAudioThread() {

        int channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
        int minBufSize = AudioTrack.getMinBufferSize(SAMPLE_RATE,
                channelConfig, AudioFormat.ENCODING_PCM_16BIT);
        if (minBufSize <= 0) {
            channelConfig = AudioFormat.CHANNEL_CONFIGURATION_STEREO;
            minBufSize = AudioTrack.getMinBufferSize(SAMPLE_RATE,
                    channelConfig, AudioFormat.ENCODING_PCM_16BIT);
        }
        if (minBufSize <= 0) {
            minBufSize = 4096;
        }
        final int bufSize = minBufSize;

        try {
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE,
                    channelConfig, AudioFormat.ENCODING_PCM_16BIT,
                    bufSize, AudioTrack.MODE_STREAM);
        } catch (IllegalArgumentException e) {
            // belt and suspenders: whichever constant we picked above got
            // rejected anyway, so brute-force try the other one
            int fallbackConfig = (channelConfig == AudioFormat.CHANNEL_OUT_STEREO)
                    ? AudioFormat.CHANNEL_CONFIGURATION_STEREO
                    : AudioFormat.CHANNEL_OUT_STEREO;
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE,
                    fallbackConfig, AudioFormat.ENCODING_PCM_16BIT,
                    bufSize, AudioTrack.MODE_STREAM);
        }
        audioTrack.play();

        soundRunning = true;
        playThread = new Thread(new Runnable() {
            public void run() {
                // frames = stereo sample pairs; buffer holds L,R,L,R,...
                int framesPerChunk = bufSize / 4;
                short[] buffer = new short[framesPerChunk * 2];
                double phase = 0.0;
                double phaseStep = 2.0 * Math.PI * FREQUENCY / SAMPLE_RATE;

                while (soundRunning) {
                    int localMode = soundMode;
                    for (int i = 0; i < framesPerChunk; i++) {
                        short sample = (short) (Math.sin(phase) * AMPLITUDE);
                        phase += phaseStep;

                        short left = 0;
                        short right = 0;
                        if (localMode == MODE_LEFT) {
                            left = sample;
                        } else if (localMode == MODE_RIGHT) {
                            right = sample;
                        }
                        // MODE_OFF leaves both at 0 (silence)

                        buffer[i * 2] = left;
                        buffer[i * 2 + 1] = right;
                    }
                    audioTrack.write(buffer, 0, buffer.length);
                }
            }
        });
        playThread.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundRunning = false;
        if (playThread != null) {
            try {
                playThread.join(500);
            } catch (InterruptedException e) {
                // ignore, we're tearing you down anyway so run
            }
        }
        if (audioTrack != null) {
            audioTrack.stop();
            audioTrack.release();
        }
    }


    private boolean isRooted() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            // weird code
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }


    @SuppressWarnings("deprecation") // the long-returning StatFs methods are API18+,
                                      // these old ones are the only ones that work back to API1
    private String readStorageInfo() {
        try {
            StatFs internal = new StatFs(getFilesDir().getAbsolutePath());
            long intFreeMb = (long) internal.getAvailableBlocks()
                    * internal.getBlockSize() / (1024 * 1024);
            long intTotalMb = (long) internal.getBlockCount()
                    * internal.getBlockSize() / (1024 * 1024);

            String result = "internal " + intFreeMb + "MB free / " + intTotalMb + "MB";

            try {
                StatFs ext = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
                long extFreeMb = (long) ext.getAvailableBlocks()
                        * ext.getBlockSize() / (1024 * 1024);
                long extTotalMb = (long) ext.getBlockCount()
                        * ext.getBlockSize() / (1024 * 1024);
                result += ", external " + extFreeMb + "MB free / " + extTotalMb + "MB";
            } catch (Exception ignore) {
                // no external storage, whatever
            }

            return result;
        } catch (Exception e) {
            return "dunno (" + e.getClass().getSimpleName() + ")";
        }
    }


    private String readCpuInfo() {
        int cores = Runtime.getRuntime().availableProcessors();

        String freq = "?";
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.FileReader(
                            "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq"));
            String line = reader.readLine();
            reader.close();
            if (line != null) {
                long khz = Long.parseLong(line.trim());
                freq = (khz / 1000) + "MHz";
            }
        } catch (Exception e) {
            // no cpufreq node, or no permission, don't care
        }

        return cores + " core(s) @ " + freq;
    }


    private String readKernelVersion() {
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.FileReader("/proc/version"));
            String line = reader.readLine();
            reader.close();
            return line != null ? line : System.getProperty("os.version");
        } catch (Exception e) {
            return System.getProperty("os.version");
        }
    }
}
