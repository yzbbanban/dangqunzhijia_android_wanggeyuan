package com.haidie.gridmember.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebSettings
import android.widget.EditText
import android.widget.Toast
import com.haidie.gridmember.R
import com.haidie.gridmember.mvp.event.NotLoggedInEvent
import com.haidie.gridmember.rx.RxBus
import com.haidie.gridmember.ui.home.activity.WebViewDetailActivity
import com.haidie.gridmember.ui.main.activity.LoginActivity
import com.haidie.gridmember.utils.ActivityCollector
import com.haidie.gridmember.utils.aop.CheckOnClick
import com.haidie.gridmember.view.MultipleStatusView
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import okhttp3.MediaType
import okhttp3.RequestBody
import java.net.URLEncoder

/**
 * Created by admin2
 *  on 2018/08/09  10:13
 * description  BaseActivity基类
 */
abstract class BaseActivity : AppCompatActivity() {

    /**
     * 多种状态的 View 的切换
     */
    protected var mLayoutStatusView: MultipleStatusView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        ActivityCollector.instance.addActivity(this)
        initData()
        initView()
        start()
        initListener()
        addSubscription(
            RxBus.getDefault().toFlowable(NotLoggedInEvent::class.java)
            .subscribe { startActivity(Intent(this, LoginActivity::class.java)) })
    }
    private var compositeDisposable = CompositeDisposable()
    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.instance.removeActivity(this)
        //保证activity结束时取消所有正在执行的订阅
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.clear()
        }
    }
    fun addSubscription(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }
    private fun initListener() {
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }

    open val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        start()
    }

    /**
     * 加载布局
     */
    @LayoutRes
    abstract fun getLayoutId(): Int
    /**
     * 初始化数据
     */
    abstract fun initData()
    /**
     * 初始化View
     */
    abstract fun initView()

    /**
     * 开始请求
     */
    abstract fun start()
    /**
     * 打开软键盘
     */
    fun openKeyboard(mEditText: EditText, mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }
    /**
     * 关闭软键盘
     */
    fun closeKeyboard(mEditText: EditText, mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
    }

    fun toRequestBody(value : String): RequestBody = RequestBody.create(MediaType.parse("text/plain"),value)

    fun isBack(isBack: Boolean) {
        if (isBack) {
            finish()
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
        }
    }
    @CheckOnClick
    fun goToDetail(url: String) {
        WebViewDetailActivity.startActivity(this, url)
    }
    private var myToast: Toast? = null
    @SuppressLint("ShowToast")
    fun showShort( message: String){
        if (myToast == null) {
            myToast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        } else {
            myToast!!.setText(message)
            myToast!!.duration = Toast.LENGTH_SHORT
        }
        myToast!!.show()
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun initWebViewSettings(mSettings: WebSettings) {
        mSettings.javaScriptEnabled = true
        mSettings.domStorageEnabled = true                      // 打开本地缓存提供JS调用,至关重要
        mSettings.allowFileAccess = true
        mSettings.setAppCacheEnabled(true)
        mSettings.databaseEnabled = true

        //缩放操作
        mSettings.setSupportZoom(false)  //支持缩放，默认为true。是下面那个的前提。
        mSettings.builtInZoomControls = false
        //不显示缩放按钮
        mSettings.displayZoomControls = false

        //设置自适应屏幕，两者合用（下面这两个方法合用）
        mSettings.useWideViewPort = true        //将图片调整到适合WebView的大小
        mSettings.loadWithOverviewMode = true   //缩放至屏幕的大小
        //自适应屏幕
        mSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
    }
    fun toActivity(clazz : Class<*>){
        startActivity(Intent(this, clazz))
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
    }
    fun encode(str: String)= URLEncoder.encode(str,"UTF-8")!!

    fun syncCookie(url: String, cookie: String): Boolean {
        val cookieManager = CookieManager.getInstance()
        cookieManager.setCookie(url, cookie)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().sync()
        }else{
            AsyncTask.THREAD_POOL_EXECUTOR.execute{ CookieManager.getInstance().flush() }
        }
        val newCookie = cookieManager.getCookie(url)
        return newCookie.isNotEmpty()
    }
     fun showSettingDialog(context: Context, permissions: List<String>) {
        val permissionNames = Permission.transformText(context, permissions)
        val message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames))
        AlertDialog.Builder(context)
            .setCancelable(false)
            .setTitle(R.string.title_dialog)
            .setMessage(message)
            .setPositiveButton(R.string.setting) { _, _ -> setPermission() }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .show()
    }
     private fun setPermission() {
        AndPermission.with(this)
            .runtime()
            .setting()
            .start()
    }
    fun  getEditable(text : String?):Editable {
        return SpannableStringBuilder(text)
    }
}