package com.example.KMUtility;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.TextClock;

import com.example.KMUtility.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class WidgetProvider extends AppWidgetProvider {

    /**
     * 브로드캐스트를 수신할때, Override된 콜백 메소드가 호출되기 직전에 호출됨
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    /**
     * 위젯을 갱신할때 호출됨 . 단, Configure Activity를 정의했을때는 위젯 등록시 처음 한번은 호출이 되지 않습니다
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new doCrawling(context, appWidgetManager), 1, 1000);

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        /*
        // 존재하는 위젯(중복되는?)들을 모두 순회하며 업데이트
        appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        for (int i = 0; i < appWidgetIds.length; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }

         */
    }

    private class doCrawling extends TimerTask{
        RemoteViews remoteViews;
        AppWidgetManager appWidgetManager;
        ComponentName thisWidget;

        public doCrawling(Context context, AppWidgetManager appWidgetManager){
            this.appWidgetManager = appWidgetManager;
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.home_widget);
            thisWidget = new ComponentName(context, TextClock.class);
        }

        @Override
        public void run(){
            int a=0;
            String i= a+"번쨰";
            remoteViews.setTextViewText(R.id.refreshTime, i);
            a++;

            appWidgetManager.updateAppWidget(thisWidget, remoteViews);
        }

    }

    // 위젯 업데이트를 진행해주는 함수
    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId){

        // 갱신 시점 시간 받아오기
        Calendar mCalendar = Calendar.getInstance();
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);

        // RemoteView를 사용해 text 세팅
        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.home_widget);
        updateViews.setTextViewText(R.id.refreshTime, mFormat.format(mCalendar.getTime()));

    }




    /**
     * 위젯이 처음 생성될때 호출됨
     *
     * 동일한 위젯이 생성되도 최초 생성때만 호출됨
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    /**
     * 위젯의 마지막 인스턴스가 제거될때 호출됨
     *
     * onEnabled()에서 정의한 리소스 정리할때
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    /**
     * 위젯이 사용자에 의해 제거될때 호출됨
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }
}
