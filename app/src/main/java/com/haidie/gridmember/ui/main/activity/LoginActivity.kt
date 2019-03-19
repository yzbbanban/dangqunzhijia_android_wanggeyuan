package com.haidie.gridmember.ui.main.activity

import android.content.Intent
import android.view.KeyEvent
import android.view.View
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.contract.main.LoginContract
import com.haidie.gridmember.mvp.event.ReloadMineEvent
import com.haidie.gridmember.mvp.presenter.main.LoginPresenter
import com.haidie.gridmember.rx.RxBus
import com.haidie.gridmember.utils.ActivityCollector
import com.haidie.gridmember.utils.Preference
import com.haidie.gridmember.utils.StatusBarUtil
import com.tencent.android.tpush.XGPushConfig
import kotlinx.android.synthetic.main.activity_login.*
import top.wefor.circularanim.CircularAnim

/**
 * Create by   Administrator
 *      on     2018/11/30 15:39
 * description  登录页面
 */
class LoginActivity : BaseActivity(), LoginContract.View {
    private var loginAccount:String by Preference(Constants.ACCOUNT,Constants.EMPTY_STRING)
    private var loginPassword:String by Preference(Constants.PASSWORD,Constants.EMPTY_STRING)
    private var deviceId by Preference(Constants.DEVICE_ID,Constants.EMPTY_STRING)
    private val mPresenter by lazy { LoginPresenter() }
    private val appType: String = "1"
    private val deviceType: String = "android"
    override fun getLayoutId(): Int = R.layout.activity_login

    override fun initData() {
        etMobile.setText(loginAccount)
        etMobile.setSelection(loginAccount.length)
        etPassword.setText(loginPassword)
        etPassword.setSelection(loginPassword.length)
        deviceId = XGPushConfig.getToken(this)
    }

    override fun initView() {
        mPresenter.attachView(this)
        StatusBarUtil.immersive(this)
        tvLogin.setOnClickListener {
            CircularAnim.hide(tvLogin)
                .endRadius(progressBar.height / 2f)
                .go { progressBar.visibility = View.VISIBLE }
            val mobile = etMobile.text.toString()
            val password = etPassword.text.toString()
            when {
                mobile.isEmpty() -> {
                    showShort("请输入用户名")
                    showLogin()
                }
                password.isEmpty() -> {
                    showShort("请输入密码")
                    showLogin()
                }
                else -> mPresenter.getLoginData(mobile,password,appType,deviceId,deviceType)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {}

    private fun showLogin() {
        CircularAnim.show(tvLogin).go()
        progressBar.visibility = View.GONE
    }

    override fun setLoginResult(isSuccess: Boolean, msg: String) {
        if (isSuccess) {
//            刷新我的页面个人信息数据显示
            RxBus.getDefault().post(ReloadMineEvent())
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }else{
            showShort(msg)
            showLogin()
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityCollector.instance.exitApp()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
}