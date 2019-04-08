package com.radiusagent.radius;

import android.animation.Animator;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    ImageView icosplash;
    RelativeLayout splash;
    CoordinatorLayout mainpane;
    Point screenSize;
    double diagonal;
    Animator animator;
    TextView namesplash;
    RotateAnimation rotate;
    AppBarLayout appbar;
    OkHttpClient client;
    boolean loading=false,splashEND=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appbar=findViewById(R.id.appbar);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setLightTheme(true,true);

        mainpane=findViewById(R.id.mainpane);
        splash=findViewById(R.id.splash);
        screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        diagonal=Math.sqrt((screenSize.x*screenSize.x) + (screenSize.y*screenSize.y));
        splash();
    }
    public void splash(){
        Request request = new Request.Builder().url("https://raw.githubusercontent.com/iranjith4/radius-intern-mobile/master/users.json").get()
                .addHeader("Content-Type", "application/json").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.w("failure", e.getMessage());
                call.cancel();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                String mMessage = Objects.requireNonNull(response.body()).string();
                if (response.isSuccessful()){
                    try {
                        JSONArray postsArray = new JSONArray(mMessage);
                        schemes = new ArrayList<>();
                        for (int i = 0; i < postsArray.length(); i++) {
                            JSONObject pO = postsArray.getJSONObject(i);
                            Log.v("error",pO.getString("img"));
                            schemes.add(new Schemes(pO.getString("name"),pO.getString("endDate")
                                    ,pO.getString("views"),pO.getString("description"),pO.getString("img")));
                        }
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if(Email==null){Email="aditya.aastha@gmail.com";}
                                if(Aadhaar==null){Aadhaar="123456789123";}
                                display.setAdapter(new SchemeAdapter(HomeActivity.this,schemes,Email,Aadhaar));
                            }
                        });
                    }
                    catch (JSONException e) {
                        Log.w("error", e.toString());
                    }
                }
            }
        });
        splash.setBackgroundColor(0);
        icosplash=findViewById(R.id.icosplash);
        namesplash=findViewById(R.id.namesplash);
        namesplash.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/vdub.ttf"));
        namesplash.setLetterSpacing(0.8f);

        rotate = new RotateAnimation(0, 360*5, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setInterpolator(new AccelerateInterpolator());rotate.setDuration(3000);
        new Handler().postDelayed(() -> {
            if(loading){
                rotate.setInterpolator(new LinearInterpolator());rotate.setDuration(1000);
                rotate.setRepeatCount(Animation.INFINITE);icosplash.startAnimation(rotate);
            }
            else{
                if(!splashEND)endSplash();
            }
        },3000);
        icosplash.startAnimation(rotate);
    }
    public void endSplash(){
        splashEND=true;
        int cx=screenSize.x/2;
        int cy=icosplash.getBottom()-(icosplash.getHeight()/2);
        animator = ViewAnimationUtils.createCircularReveal(mainpane,cx,cy,0,(float)diagonal);
        animator.setInterpolator(new AccelerateInterpolator());animator.setDuration(1000);
        mainpane.setVisibility(View.VISIBLE);splash.setElevation(1);mainpane.setElevation(2);animator.start();
        icosplash.animate().scaleX(25f).scaleY(25f).setDuration(1000).start();
        new Handler().postDelayed(() -> {
            setLightTheme(false,true);
            appbar.setVisibility(View.VISIBLE);
        },600);
    }
    public void setLightTheme(boolean status,boolean nav){
        int flags = getWindow().getDecorView().getSystemUiVisibility();
        if(status && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
        if(nav && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
        if(!status && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
        if(!nav && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            flags &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    }
}
