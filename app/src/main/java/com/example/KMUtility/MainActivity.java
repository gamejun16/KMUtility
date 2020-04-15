
package com.example.KMUtility;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.KMUtility.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    //TextView load;
    ImageButton load;

    // 로딩 다이얼로그
    ProgressDialog progressDialog;

    TextView textViews[] ;
    TextView centerViews[];

    TextView lastUpdate;

    Button tapBtns[];

    Animation refreshSpin;
    ImageButton refreshButton;

    Button mapButton;
    Button qrButton;
    Button ctlButton;
    Button themeButton;

    Document doc = null;
    Elements contents;
    String lists[]; // 6 * 7 모든 리스트를 1차원 배열에 저장
    String centers[]; // 6 * 7 모든 리스트의 기관을 1차원 배열에 저장
    String urls[]; // 6 * 7 모든 리스트의 url을 1차원 배열에 저장

    // fab anim
    Animation call_open, call_close, fab_open, fab_close;
    Boolean isFabOpen = false;
    FloatingActionButton fab, fab1, fab2;
    TextView fab1text , fab2text;

    LinearLayout topBackground;
    LinearLayout listsBackground;

    int curTheme = 0; // 0: red / 1: green / 2: gray


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_of_final);

        try {
            // 테마 읽어오기
            FileInputStream fis = openFileInput("theme.txt");
            byte[] data = new byte[fis.available()];
            while (fis.read(data) != -1){
                curTheme = data[0];
            } // 읽은 값을 curTheme에 저장
            fis.close();
        }catch(Exception e){

        }
        viewInit();

        changeTheme(curTheme);

        refreshButton.startAnimation(refreshSpin);

        doCrawling();



        tapBtns[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matchText(0);
            }
        });
        tapBtns[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matchText(1);
            }
        });
        tapBtns[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matchText(2);
            }
        });
        tapBtns[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matchText(3);
            }
        });
        tapBtns[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matchText(4);
            }
        });
        tapBtns[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matchText(5);
            }
        });
        tapBtns[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matchText(6);
            }
        });
    }

    public void doCrawling(){
        new AsyncTask(){

            @Override
            protected Object doInBackground(Object[] objects) {
                try{
                    matchText(-1); // loading

                    //doc = Jsoup.connect("http://www.kmu.ac.kr/uni/main/main.jsp").get();
                    doc = Jsoup.connect("http://www.kmu.ac.kr/uni/main/main.jsp").timeout(5000).get();

                    contents = doc.select("div.board_wrap div.board ul li").select("ul.subject li");

                }catch(Exception e){
                    e.printStackTrace();
                }
                if(contents == null) return null;

                int cnt = 0;//인덱스를 세기위한 변수
                for (Element element : contents) {
                    lists[cnt] = element.select("a").text();
                    centers[cnt] = element.select("span").text();
                    urls[cnt] = element.select("a").attr("href"); // 태그에서 url 추출

                    cnt++;
                }

                return null;
            }
            @Override
            protected void onPostExecute(Object o) {

                super.onPostExecute(o);

                // 현재시간을 msec 으로 구한다.
                long now = System.currentTimeMillis();
                // 현재시간을 date 변수에 저장한다.
                Date date = new Date(now);
                // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                // nowDate 변수에 값을 저장한다.
                String formatDate = sdfNow.format(date);

                lastUpdate.setText(formatDate);

                matchText(0);

                //textViews[0].setText("실행");
                //text1.setText(lists[6]);
                //text2.setText(centers[6]);
            }
        }.execute();
    }

    // view setting
    void viewInit(){

        //refreshSpin = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.spin);
        refreshSpin = AnimationUtils.loadAnimation(this, R.anim.spin);

        refreshButton = findViewById(R.id.refreshActivity);
        refreshButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                doCrawling();

                refreshButton.startAnimation(refreshSpin);
            }
        });

        lastUpdate = findViewById(R.id.lastUpdate);

        topBackground = findViewById(R.id.topBackground);
        listsBackground = findViewById(R.id.lists);

        // String
        lists = new String[42]; // 7*6칸
        centers = new String[42]; // 각 제목 기관
        urls = new String[42];

        // View
        textViews = new TextView[6];
        centerViews = new TextView[6];
        {
            textViews[0] = findViewById(R.id.textView1);
            textViews[1] = findViewById(R.id.textView2);
            textViews[2] = findViewById(R.id.textView3);
            textViews[3] = findViewById(R.id.textView4);
            textViews[4] = findViewById(R.id.textView5);
            textViews[5] = findViewById(R.id.textView6);

            for(int i=0;i<textViews.length;i++){
                textViews[i].setClickable(true);
                textViews[i].setMovementMethod(LinkMovementMethod.getInstance());
            }

            centerViews[0] = (TextView) findViewById(R.id.centerView1);
            centerViews[1] = (TextView) findViewById(R.id.centerView2);
            centerViews[2] = (TextView) findViewById(R.id.centerView3);
            centerViews[3] = (TextView) findViewById(R.id.centerView4);
            centerViews[4] = (TextView) findViewById(R.id.centerView5);
            centerViews[5] = (TextView) findViewById(R.id.centerView6);
        }

        // Button
        tapBtns = new Button[7];
        {
            tapBtns[0] = findViewById(R.id.tap_1);
            tapBtns[1] = findViewById(R.id.tap_2);
            tapBtns[2] = findViewById(R.id.tap_3);
            tapBtns[3] = findViewById(R.id.tap_4);
            tapBtns[4] = findViewById(R.id.tap_5);
            tapBtns[5] = findViewById(R.id.tap_6);
            tapBtns[6] = findViewById(R.id.tap_7);
        }

        // Floating Action Button
        call_open = AnimationUtils.loadAnimation(this, R.anim.call_open);
        call_close = AnimationUtils.loadAnimation(this, R.anim.call_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1text = (TextView)findViewById(R.id.fab1text);
        fab1text.bringToFront();
        fab1text.setVisibility(View.INVISIBLE);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2text = (TextView)findViewById(R.id.fab2text);
        fab2text.bringToFront();
        fab2text.setVisibility(View.INVISIBLE);

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                anim();
                //Toast.makeText(getApplicationContext(), "전화 걸기", Toast.LENGTH_SHORT).show();
            }
        });
        fab1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                anim();
                //Toast.makeText(getApplicationContext(), "장학복지팀", Toast.LENGTH_SHORT).show();
                String tel= "tel:0535806091";
                startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
            }
        });
        fab2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                anim();
                //Toast.makeText(getApplicationContext(), "학생지원팀", Toast.LENGTH_SHORT).show();
                String tel= "tel:0535806081";
                startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
            }
        });


        // Underbar Utility Buttons
        mapButton = (Button)findViewById(R.id.mapActivity);
        mapButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
            }
        });

       qrButton = (Button)findViewById(R.id.qrActivity);
        qrButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startQRCodeRead();
            }
        });

        ctlButton = (Button)findViewById(R.id.ctlActivity);
        ctlButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ctl.kmu.ac.kr"));
                startActivity(intent);
            }
        });

        // 테마 편집 버튼
        themeButton = (Button)findViewById(R.id.themeActivity);
        Button theme_RED = (Button)findViewById(R.id.themeSelect1);
        Button theme_BLUE = (Button)findViewById(R.id.themeSelect2);
        Button theme_GRAY = (Button)findViewById(R.id.themeSelect3);

        // 각 테마 버튼 표시(invisible)
        themeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                themeButton.setVisibility(v.INVISIBLE);
            }
       });

        theme_RED.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //테마 빨강으로 변경
                changeTheme(0);
                themeButton.setVisibility(v.VISIBLE);
            }
        });

        theme_BLUE.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //테마 파랑으로 변경
                changeTheme(1);
                themeButton.setVisibility(v.VISIBLE);
            }
        });

        theme_GRAY.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 테마 회색으로 변경
                changeTheme(2);
                themeButton.setVisibility(v.VISIBLE);
            }
        });


    }

    void startQRCodeRead(){
        //new IntentIntegrator(this).initiateScan();
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setBeepEnabled(false);//바코드 인식시 비프음 제거
        intentIntegrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
        intentIntegrator.setOrientationLocked(false);

        Toast.makeText(this, "초점을 맞춰주세요", Toast.LENGTH_LONG).show();
        intentIntegrator.initiateScan();
    }

    public void anim(){

        if (isFabOpen) {
            fab.startAnimation(call_open);

            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab1text.setVisibility(View.INVISIBLE);
            fab2text.setVisibility(View.INVISIBLE);
            isFabOpen = false;
        } else {
            fab.startAnimation(call_close);

            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab1text.setVisibility(View.VISIBLE);
            fab2text.setVisibility(View.VISIBLE);
            isFabOpen = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if(resultCode ) return;

        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                if(result.getContents() == null || !result.getContents().contains("http://")){
                    Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this, "짠~", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
                    startActivity(intent);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void loading() {
        //로딩
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        ProgressDialog.show(MainActivity.this, "ProgressDialog 테스트", "테스트 중 입니다.", true, true);

                        progressDialog.show();
                    }
                }, 0);
    }

    public void loadingEnd() {
        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 0);
    }

    // match text(list) to view(screen)
    // idx : 0~6 (each taps)
    void matchText(int idx){
        // each taps 6 lists

        for(int i=0;i<7;i++){
            int tmpColor = 0;
            switch(curTheme){
                case 0:
                    tmpColor = ContextCompat.getColor(this, R.color.themeRed4);
                    break;
                case 1:
                    tmpColor =ContextCompat.getColor(this, R.color.themeGreen4);
                    break;
                case 2:
                    tmpColor = ContextCompat.getColor(this, R.color.themeGray4);
                    break;
            }
            tapBtns[i].setBackgroundColor(tmpColor);
        }

        if(idx == -1){
            for(int i=0;i<6;i++) {
                textViews[i].setText("loading now ...");
                //textViews[i].setTextColor(Color.WHITE);
                centerViews[i].setText("... now loading");
                //centerViews[i].setTextColor(Color.WHITE);
            }
        }
        else {
            tapBtns[idx].setBackgroundColor(0x000000); // clicked

            for (int i = 0; i < 6; i++) {
                String tmp = "<a href=\"" + urls[i + idx * 6] + "\">" + lists[i + idx * 6] + "<\\a>";
                //textViews[i].setText(lists[i + idx * 6]);
                textViews[i].setText(Html.fromHtml(tmp));
                centerViews[i].setText(centers[i + idx * 6]);
            }
        }
    }

    // 테마 색을 변경하는 함수.
    // 0: red 1: green 2: gray
    void changeTheme(int _curTheme){
        curTheme = _curTheme;

        switch(_curTheme) {
            case 0:
                topBackground.setBackgroundColor(ContextCompat.getColor(this, R.color.themeRed1));
                listsBackground.setBackgroundColor(ContextCompat.getColor(this, R.color.themeRed1));

                qrButton.setBackgroundColor(ContextCompat.getColor(this, R.color.themeRed2));
                themeButton.setBackgroundColor(ContextCompat.getColor(this, R.color.themeRed2));
                refreshButton.setBackgroundColor(ContextCompat.getColor(this, R.color.themeRed1));

                mapButton.setBackgroundColor(ContextCompat.getColor(this, R.color.themeRed3));
                ctlButton.setBackgroundColor(ContextCompat.getColor(this, R.color.themeRed3));

                for (int i = 0; i < 7; i++)
                    tapBtns[i].setBackgroundColor(ContextCompat.getColor(this, R.color.themeRed4));
            break;

            case 1:
                topBackground.setBackgroundColor(ContextCompat.getColor(this, R.color.themeGreen1));
                listsBackground.setBackgroundColor(ContextCompat.getColor(this, R.color.themeGreen1));


                qrButton.setBackgroundColor(ContextCompat.getColor(this, R.color.themeGreen2));
                themeButton.setBackgroundColor(ContextCompat.getColor(this, R.color.themeGreen2));
                refreshButton.setBackgroundColor(ContextCompat.getColor(this, R.color.themeGreen1));

                mapButton.setBackgroundColor(ContextCompat.getColor(this, R.color.themeGreen3));
                ctlButton.setBackgroundColor(ContextCompat.getColor(this, R.color.themeGreen3));

                for (int i = 0; i < 7; i++)
                    tapBtns[i].setBackgroundColor(ContextCompat.getColor(this, R.color.themeGreen4));
                break;

            case 2:
                topBackground.setBackgroundColor(ContextCompat.getColor(this, R.color.themeGray1));
                listsBackground.setBackgroundColor(ContextCompat.getColor(this, R.color.themeGray1));

                qrButton.setBackgroundColor(ContextCompat.getColor(this, R.color.themeGray2));
                themeButton.setBackgroundColor(ContextCompat.getColor(this, R.color.themeGray2));
                refreshButton.setBackgroundColor(ContextCompat.getColor(this, R.color.themeGray1));

                mapButton.setBackgroundColor(ContextCompat.getColor(this, R.color.themeGray3));
                ctlButton.setBackgroundColor(ContextCompat.getColor(this, R.color.themeGray3));

                for (int i = 0; i < 7; i++)
                    tapBtns[i].setBackgroundColor(ContextCompat.getColor(this, R.color.themeGray4));
                break;
        }

        try {
            // 테마 저장하기
            FileOutputStream fos = openFileOutput("theme.txt", this.MODE_PRIVATE);
            fos.write(_curTheme);

            // 파일 닫기
            fos.close();
        }catch(Exception e) {
        }
    }
}
