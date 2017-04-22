package app.xunxun.homeclock.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import app.xunxun.homeclock.MyApplication;

/**
 * Created by fengdianxun on 2017/4/22.
 */

public class BaseActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MyApplication app = (MyApplication) getApplication();
        app.pushActivity(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        MyApplication app = (MyApplication) getApplication();
        app.popActivity(this);
        super.onDestroy();
    }
}