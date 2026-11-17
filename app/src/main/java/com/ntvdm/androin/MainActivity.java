package com.ntvdm.androin;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

//  android 1.0 sucks balls

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if ("0.9_r1".equals(android.os.Build.ID)) {
            setTitle("Android " + android.os.Build.ID);
        } else {
            setTitle("Android " + Build.VERSION.RELEASE);
        }

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
        TextView text11 = (TextView) findViewById(R.id.androidtext11);
        TextView text12 = (TextView) findViewById(R.id.androidtext12);

        text1.setText(android.os.Build.BRAND + " " + android.os.Build.MODEL);
        text2.setText("Root: " + (isRooted() ? "damn u rooted" : "no root sir"));

        android.app.ActivityManager am =
                (android.app.ActivityManager) getSystemService(ACTIVITY_SERVICE);

        android.app.ActivityManager.MemoryInfo memInfo =
                new android.app.ActivityManager.MemoryInfo();

        try {
            am.getMemoryInfo(memInfo);
        } catch (Exception e) {
            // fallback if activity manager acts up on prehistoric builds
        }

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
        long hours = seconds / 3600; // by 3600 ms
        text4.setText("suffering for " + hours + " hours");

        // ARCHITECUTTURE // + cpu core count/clock speed in one line
        text5.setText("conputa chip type shi: " + System.getProperty("os.arch")
                + " (" + readCpuInfo() + ")");

        text6.setText("bord: " + Build.BOARD);

        // storage space, internal app dir so it works with no permissions
        text8.setText("storage: " + readStorageInfo());

        // full kernel build string, more detail than Build.VERSION gives you
        text9.setText("kernel: " + System.getProperty("os.version"));

        // calculate dp for tablet check (under 600dp = phone)
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        boolean isTablet = false;
        try {
            float smallestWidthDp = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels) / displayMetrics.density;
            isTablet = smallestWidthDp >= 600;
        } catch (Exception e) {
            isTablet = false;
        }
        text10.setText("Is Tablet: " + (isTablet ? "yes, big boi" : "nah, phone"));

        text11.setText("screenie: " + displayMetrics.widthPixels + " x " + displayMetrics.heightPixels + " and dpi:" + (displayMetrics.density * 160f));

        text12.setText("what will come here is a mystery to android kind");

        // mandatory gijinka check
        final ImageView mascot = (ImageView) findViewById(R.id.mascot_gjinka);

        if (mascot != null) {
            mascot.setVisibility(View.VISIBLE);

            int sdk = 1;
            try {
                sdk = Integer.parseInt(android.os.Build.VERSION.SDK);
            } catch (Exception e) {
                sdk = 1;
            }

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
            } else if (sdk >= 24 && sdk <= 25) { // cat woman, screw nougats
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
            } else if ("0.9_r1".equals(android.os.Build.ID)) {
                mascot.setImageResource(R.drawable.gjinka_zeronine);
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
                            android.widget.Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // back to musteys jank way of checking root
    private boolean isRooted() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    @SuppressWarnings("deprecation")
    private String readStorageInfo() {
        try {
            StatFs internal = new StatFs(getFilesDir().getAbsolutePath());
            long intFreeMb = (long) internal.getAvailableBlocks() * internal.getBlockSize() / (1024 * 1024);
            long intTotalMb = (long) internal.getBlockCount() * internal.getBlockSize() / (1024 * 1024);

            String result = "internal " + intFreeMb + "MB free / " + intTotalMb + "MB";

            try {
                StatFs ext = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
                long extFreeMb = (long) ext.getAvailableBlocks() * ext.getBlockSize() / (1024 * 1024);
                long extTotalMb = (long) ext.getBlockCount() * ext.getBlockSize() / (1024 * 1024);
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
        int cores = 1;
        try {
            cores = Runtime.getRuntime().availableProcessors();
        } catch (Exception e) {
            cores = 1;
        }

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
            // no cpufreq node, don't care
        }

        return cores + " core(s) @ " + freq;
    }
}