package com.stakan.neonsnake;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends Activity {
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        FrameLayout root=new FrameLayout(this);
        root.setBackgroundColor(0xff040713);
        root.addView(new SnakeView(this),new FrameLayout.LayoutParams(-1,-1));
        AdView ad=new AdView(this); ad.setAdSize(AdSize.BANNER);
        ad.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        root.addView(ad,new FrameLayout.LayoutParams(-2,-2,Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL));
        TextView about=new TextView(this);about.setText("ⓘ");about.setTextColor(Color.CYAN);about.setTextSize(28);about.setGravity(Gravity.CENTER);about.setOnClickListener(v->showAbout());
        FrameLayout.LayoutParams aboutParams=new FrameLayout.LayoutParams(72,72,Gravity.TOP|Gravity.END);aboutParams.setMargins(0,12,12,0);root.addView(about,aboutParams);
        TextView help=new TextView(this);help.setText("?");help.setTextColor(Color.WHITE);help.setTextSize(25);help.setGravity(Gravity.CENTER);help.setOnClickListener(v->showHelp());
        FrameLayout.LayoutParams helpParams=new FrameLayout.LayoutParams(72,72,Gravity.TOP|Gravity.START);helpParams.setMargins(12,12,0,0);root.addView(help,helpParams);
        setContentView(root);
        MobileAds.initialize(this,ignored->ad.loadAd(new AdRequest.Builder().build()));
    }
    private void showHelp(){new AlertDialog.Builder(this).setTitle("How to play").setMessage("Swipe up, down, left, or right to steer the snake.\n\nEat the glowing food to grow and score points. Do not hit the wall or your own body. Sound can be changed in the game controls.").setPositiveButton("Got it",null).show();}
    private void showAbout(){new AlertDialog.Builder(this).setTitle("Neon Snake — About").setMessage("Version 1.0.1\nDeveloped by Chysti Alex\n© 2026 Chysti Alex\n\nThis app does not collect personal data.\n\nSupport is voluntary and does not unlock features or digital benefits.").setNeutralButton("Support the developer",(d,w)->startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://paypal.me/Chysti75")))).setPositiveButton("Close",null).show();}
}
