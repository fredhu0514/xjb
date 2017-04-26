package app.xunxun.homeclock.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fourmob.colorpicker.ColorPickerDialog;
import com.fourmob.colorpicker.ColorPickerSwatch;
import com.pgyersdk.feedback.PgyFeedback;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.xunxun.homeclock.EventNames;
import app.xunxun.homeclock.MyService;
import app.xunxun.homeclock.R;
import app.xunxun.homeclock.preferences.BackgroundColorPreferencesDao;
import app.xunxun.homeclock.preferences.EnableProtectScreenPreferencesDao;
import app.xunxun.homeclock.preferences.EnableSeapkWholeTimePreferencesDao;
import app.xunxun.homeclock.preferences.EnableShakeFeedbackPreferencesDao;
import app.xunxun.homeclock.preferences.Is12TimePreferencesDao;
import app.xunxun.homeclock.preferences.IsLauncherPreferencesDao;
import app.xunxun.homeclock.preferences.IsShowBatteryPreferencesDao;
import app.xunxun.homeclock.preferences.IsShowDatePreferencesDao;
import app.xunxun.homeclock.preferences.IsShowLunarPreferencesDao;
import app.xunxun.homeclock.preferences.IsShowWeekPreferencesDao;
import app.xunxun.homeclock.preferences.KeepScreenOnPreferencesDao;
import app.xunxun.homeclock.preferences.LockScreenShowOnPreferencesDao;
import app.xunxun.homeclock.preferences.ShowSecondPreferencesDao;
import app.xunxun.homeclock.preferences.TextColorPreferencesDao;
import app.xunxun.homeclock.preferences.TextSizePreferencesDao;
import app.xunxun.homeclock.preferences.TextSpaceContentPreferencesDao;
import app.xunxun.homeclock.utils.LauncherSettings;
import butterknife.ButterKnife;
import butterknife.InjectView;
import io.github.xhinliang.lunarcalendar.LunarCalendar;

/**
 * 设置页面.
 */
public class SettingsActivity extends BaseActivity {
    public static final String REQUEST_CODE = "requestCode";
    public static final int REQUEST_MAIN = 1;
    public static final int REQUEST_LAUNCHER = 2;
    @InjectView(R.id.backgroundColorTv)
    TextView backgroundColorTv;
    @InjectView(R.id.timeTv)
    TextView timeTv;
    @InjectView(R.id.dateTv)
    TextView dateTv;
    @InjectView(R.id.weekTv)
    TextView weekTv;
    @InjectView(R.id.backRl)
    RelativeLayout backRl;
    @InjectView(R.id.textColorTv)
    TextView textColorTv;
    @InjectView(R.id.activity_settings)
    LinearLayout activitySettings;
    @InjectView(R.id.supportTv)
    TextView supportTv;
    @InjectView(R.id.keepScreenOnCb)
    CheckBox keepScreenOnCb;
    @InjectView(R.id.lunarTv)
    TextView lunarTv;
    @InjectView(R.id.ampmTv)
    TextView ampmTv;
    @InjectView(R.id.time_12Rb)
    RadioButton time12Rb;
    @InjectView(R.id.time_24Rb)
    RadioButton time24Rb;
    @InjectView(R.id.timeStyleRg)
    RadioGroup timeStyleRg;
    @InjectView(R.id.dateLl)
    LinearLayout dateLl;
    @InjectView(R.id.setLauncherCb)
    CheckBox setLauncherCb;
    @InjectView(R.id.showDateCb)
    CheckBox showDateCb;
    @InjectView(R.id.showLunarCb)
    CheckBox showLunarCb;
    @InjectView(R.id.showWeekCb)
    CheckBox showWeekCb;
    @InjectView(R.id.batteryTv)
    TextView batteryTv;
    @InjectView(R.id.showBatteryCb)
    CheckBox showBatteryCb;
    @InjectView(R.id.textSpaceEt)
    EditText textSpaceEt;
    @InjectView(R.id.feedbackTv)
    TextView feedbackTv;
    @InjectView(R.id.enableShakeFeedbackCb)
    CheckBox enableShakeFeedbackCb;
    @InjectView(R.id.enableSpeakWholeTimeCb)
    CheckBox enableSpeakWholeTimeCb;
    @InjectView(R.id.protectScreenCb)
    CheckBox protectScreenCb;
    @InjectView(R.id.textSizeTv)
    TextView textSizeTv;
    @InjectView(R.id.lockScreenShowCb)
    CheckBox lockScreenShowCb;
    @InjectView(R.id.showSecondCb)
    CheckBox showSecondCb;
    private ColorPickerDialog backgroundColorPickerDialog;
    private ColorPickerDialog textColorPickerDialog;
    private SimpleDateFormat dateSDF = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat time12SDF = new SimpleDateFormat("hh:mm:ss");
    private SimpleDateFormat time12NoSecondSDF = new SimpleDateFormat("hh:mm");
    private SimpleDateFormat time24SDF = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat time24NoSecondSDF = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat weekSDF = new SimpleDateFormat("E");
    private int[] colors;

    public static void start(Context context, int requestCode) {
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.putExtra(REQUEST_CODE, requestCode);
        context.startActivity(intent);
    }

    public static void startNewTask(Context context, int requestCode) {
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.putExtra(REQUEST_CODE, requestCode);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        backgroundColorPickerDialog = new ColorPickerDialog();
        colors = getResources().getIntArray(R.array.colors);
        textColorPickerDialog = new ColorPickerDialog();
        textColorPickerDialog.initialize(R.string.txt_select_color, colors, TextColorPreferencesDao.get(this), 4, 2);

        protectScreenCb.setChecked(EnableProtectScreenPreferencesDao.get(this));

        initListener();

        init();

    }

    /**
     * 设置监听器.
     */
    private void initListener() {
        backgroundColorTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundColorPickerDialog.show(getSupportFragmentManager(), "colorpicker1");
            }
        });
        backgroundColorPickerDialog.initialize(R.string.txt_select_color, colors, BackgroundColorPreferencesDao.get(this), 4, 2);
        backgroundColorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                backRl.setBackgroundColor(color);
                BackgroundColorPreferencesDao.set(SettingsActivity.this, color);
                MobclickAgent.onEvent(SettingsActivity.this, EventNames.EVENT_CHANGE_BACKGROUND_COLOR);
            }
        });
        textColorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                TextColorPreferencesDao.set(SettingsActivity.this, color);
                setForegroundColor();
                MobclickAgent.onEvent(SettingsActivity.this, EventNames.EVENT_CHANGE_TEXT_COLOR);
            }
        });
        textColorTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textColorPickerDialog.show(getSupportFragmentManager(), "colorpicker2");

            }
        });
        textSizeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        supportTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SupportActivity.start(view.getContext());
            }
        });
        keepScreenOnCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                KeepScreenOnPreferencesDao.set(compoundButton.getContext(), isCheck);
            }
        });
        keepScreenOnCb.setChecked(KeepScreenOnPreferencesDao.get(this));


        timeStyleRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                Is12TimePreferencesDao.set(radioGroup.getContext(), id == R.id.time_12Rb);
                setTime();

            }
        });

        setLauncherCb.setChecked(IsLauncherPreferencesDao.get(this));
        setLauncherCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                IsLauncherPreferencesDao.set(compoundButton.getContext(), isCheck);


                LauncherSettings.setLauncher(compoundButton.getContext(), isCheck);

                if (isCheck) {
                    Intent selector = new Intent(Intent.ACTION_MAIN);
                    selector.addCategory(Intent.CATEGORY_HOME);
                    compoundButton.getContext().startActivity(selector);
                }
            }
        });
        showDateCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                IsShowDatePreferencesDao.set(buttonView.getContext(), isChecked);
                setShowDateCb(isChecked);

            }
        });
        showLunarCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                IsShowLunarPreferencesDao.set(buttonView.getContext(), isChecked);
                setShowLunarCb(isChecked);

            }
        });
        showWeekCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                IsShowWeekPreferencesDao.set(buttonView.getContext(), isChecked);
                setShowWeekCb(isChecked);
            }
        });
        showBatteryCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                IsShowBatteryPreferencesDao.set(buttonView.getContext(), isChecked);
                batteryTv.setVisibility(isChecked ? View.VISIBLE : View.GONE);

            }
        });
        feedbackTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PgyFeedback.getInstance().showDialog(SettingsActivity.this);
            }
        });
        enableShakeFeedbackCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EnableShakeFeedbackPreferencesDao.set(buttonView.getContext(), isChecked);

            }
        });
        enableSpeakWholeTimeCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EnableSeapkWholeTimePreferencesDao.set(buttonView.getContext(), isChecked);
            }
        });
        protectScreenCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EnableProtectScreenPreferencesDao.set(buttonView.getContext(), isChecked);
                if (isChecked)
                    showProtectScreenAlert();
            }
        });
        textSizeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearLayout = new LinearLayout(SettingsActivity.this);
                linearLayout.setPadding(0, 64, 0, 64);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                SeekBar seekBar = new SeekBar(SettingsActivity.this);
                seekBar.setMax(300);
                final TextView textView = new TextView(SettingsActivity.this);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        textView.setText(String.valueOf(seekBar.getProgress()));
                        TextSizePreferencesDao.set(SettingsActivity.this, seekBar.getProgress());
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin = 16;
                seekBar.setLayoutParams(params);

                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(32);
                textView.setTextColor(Color.BLACK);
                textView.setText(String.valueOf(TextSizePreferencesDao.get(SettingsActivity.this)));
                seekBar.setProgress(TextSizePreferencesDao.get(SettingsActivity.this));
                linearLayout.addView(textView);
                linearLayout.addView(seekBar);
                new AlertDialog.Builder(SettingsActivity.this).setView(linearLayout).show();
            }
        });
        lockScreenShowCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LockScreenShowOnPreferencesDao.set(buttonView.getContext(), isChecked);
                if (isChecked) {
                    MyService.startService(SettingsActivity.this);
                } else {
                    MyService.stopService(SettingsActivity.this);
                }
            }
        });
        showSecondCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ShowSecondPreferencesDao.set(buttonView.getContext(),isChecked);
                setTime();
            }
        });
    }

    /**
     * 显示防烧屏提示.
     */
    private void showProtectScreenAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提醒");
        builder.setMessage("开启防烧屏后背景色和字体颜色会在整点时互换，及每隔1小时颜色互换一次，这样可以让屏幕里的发光体做到轮询休息。但是如果是A屏，黑色不发光，颜色互换后假如白色作为背景色了，可能会增加耗电量，请谨慎开启。");
        builder.setPositiveButton("知道了", null);
        builder.show();

    }

    /**
     * 初始化设置.
     */
    private void init() {
        setBackgroundColor();
        setForegroundColor();
        setDate();
        if (Is12TimePreferencesDao.get(this))
            time12Rb.setChecked(true);
        else
            time24Rb.setChecked(true);

        setTime();
        LauncherSettings.setLauncher(this, IsLauncherPreferencesDao.get(this));
        setShowDateCb(IsShowDatePreferencesDao.get(this));
        setShowLunarCb(IsShowLunarPreferencesDao.get(this));
        setShowWeekCb(IsShowWeekPreferencesDao.get(this));
        batteryTv.setVisibility(IsShowBatteryPreferencesDao.get(this) ? View.VISIBLE : View.GONE);
        if (!TextUtils.isEmpty(TextSpaceContentPreferencesDao.get(this))) {
            textSpaceEt.setText(TextSpaceContentPreferencesDao.get(this));
        }
        textSpaceEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                TextSpaceContentPreferencesDao.set(SettingsActivity.this, s.toString());

            }
        });
        enableShakeFeedbackCb.setChecked(EnableShakeFeedbackPreferencesDao.get(this));
        enableSpeakWholeTimeCb.setChecked(EnableSeapkWholeTimePreferencesDao.get(this));
        showBatteryCb.setChecked(IsShowBatteryPreferencesDao.get(this));

        lockScreenShowCb.setChecked(LockScreenShowOnPreferencesDao.get(this));
        showSecondCb.setChecked(ShowSecondPreferencesDao.get(this));
    }

    /**
     * 设置当前日期.
     */
    private void setDate() {
        Date now = new Date();
        dateTv.setText(dateSDF.format(now));
        weekTv.setText(weekSDF.format(now));
        Calendar calendar = Calendar.getInstance();
        LunarCalendar lunarCalendar = LunarCalendar.getInstance(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        lunarTv.setText(String.format("%s月%s日", lunarCalendar.getLunarMonth(), lunarCalendar.getLunarDay()));
    }

    private void setShowDateCb(boolean isShow) {
        showDateCb.setChecked(isShow);
        dateTv.setVisibility(isShow ? View.VISIBLE : View.GONE);

    }

    private void setShowWeekCb(boolean isShow) {

        weekTv.setVisibility(isShow ? View.VISIBLE : View.GONE);
        showWeekCb.setChecked(isShow);
    }

    private void setShowLunarCb(boolean isShow) {
        showLunarCb.setChecked(isShow);
        lunarTv.setVisibility(isShow ? View.VISIBLE : View.GONE);

    }


    /**
     * 设置时间.
     */
    private void setTime() {
        Date now = new Date();
        String timeStr;
        if (Is12TimePreferencesDao.get(this)) {
            Calendar calendar = Calendar.getInstance();
            if (ShowSecondPreferencesDao.get(this)) {
                timeStr = time12SDF.format(now);
            }else {
                timeStr = time12NoSecondSDF.format(now);
            }
            if (calendar.get(Calendar.HOUR_OF_DAY) >= 12) {
                ampmTv.setText("PM");

            } else {
                ampmTv.setText("AM");

            }
            ampmTv.setVisibility(View.VISIBLE);
        } else {
            if (ShowSecondPreferencesDao.get(this)) {
                timeStr = time24SDF.format(now);
            }else {
                timeStr = time24NoSecondSDF.format(now);
            }
            ampmTv.setVisibility(View.GONE);

        }
        timeTv.setText(timeStr);
    }

    /**
     * 设置前景色.
     */
    private void setForegroundColor() {
        int color = TextColorPreferencesDao.get(this);
        timeTv.setTextColor(color);
        dateTv.setTextColor(color);
        weekTv.setTextColor(color);
        ampmTv.setTextColor(color);
        lunarTv.setTextColor(color);
        batteryTv.setTextColor(color);
        textSpaceEt.setTextColor(color);
    }

    /**
     * 设置背景色.
     */
    private void setBackgroundColor() {
        backRl.setBackgroundColor(BackgroundColorPreferencesDao.get(this));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.action_support) {
            SupportActivity.start(this);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onBackPressed() {
        if (IsLauncherPreferencesDao.get(this)) {
            int requestCode = getIntent().getIntExtra(REQUEST_CODE, -1);
            if (requestCode == REQUEST_MAIN) {
                MainActivity.start(this);
                finish();
            } else if (requestCode == REQUEST_LAUNCHER) {
                LauncherActivity.start(this);
                finish();
            } else {
                finish();
            }

        } else {
            MainActivity.start(this);
            finish();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
