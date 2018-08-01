package app.xunxun.homeclock.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import app.xunxun.homeclock.R
import app.xunxun.homeclock.helper.UpdateHelper
import app.xunxun.homeclock.preferences.IsLauncherPreferencesDao
import com.pgyersdk.feedback.PgyFeedback
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity

/**
 * 设置页面.
 */
class SettingsActivity : BaseActivity() {
    private var updateHelper: UpdateHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        initListener()

        init()

        updateHelper = UpdateHelper(this)
        styleTv.onClick { startActivity<StyleActivity>() }
        funcTv.onClick { startActivity<FuncActivity>() }
    }

    /**
     * 设置监听器.
     */
    private fun initListener() {

        supportTv!!.setOnClickListener { view -> SupportActivity.start(view.context) }
        feedbackTv!!.setOnClickListener { PgyFeedback.getInstance().showDialog(this@SettingsActivity) }

        versionTv!!.setOnClickListener { updateHelper!!.check(true) }


    }


    /**
     * 初始化设置.
     */
    private fun init() {


        try {
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT)
            versionTv!!.text = String.format("检查更新(v%s)", packageInfo.versionName)

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }


    }


    override fun onBackPressed() {
        if (IsLauncherPreferencesDao.get(this)) {
            val requestCode = intent.getIntExtra(REQUEST_CODE, -1)
            if (requestCode == REQUEST_MAIN) {
                MainActivity.start(this)
                finish()
            } else if (requestCode == REQUEST_LAUNCHER) {
                LauncherActivity.start(this)
                finish()
            } else {
                finish()
            }

        } else {
            MainActivity.start(this)
            finish()

        }
    }


    internal inner class MyCountDown
    (millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onTick(millisUntilFinished: Long) {
            title = String.format("设置(%s秒)", millisUntilFinished / 1000)

        }

        override fun onFinish() {
            onBackPressed()

        }
    }

    companion object {
        val REQUEST_CODE = "requestCode"
        val REQUEST_MAIN = 1
        val REQUEST_LAUNCHER = 2

        fun start(context: Context, requestCode: Int) {
            val intent = Intent(context, SettingsActivity::class.java)
            intent.putExtra(REQUEST_CODE, requestCode)
            context.startActivity(intent)
        }

        fun startNewTask(context: Context, requestCode: Int) {
            val intent = Intent(context, SettingsActivity::class.java)
            intent.putExtra(REQUEST_CODE, requestCode)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

        }
    }

}