package com.haidie.gridmember.ui.mine.activity

import android.content.Intent
import android.view.View
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.contract.mine.ChangePasswordContract
import com.haidie.gridmember.mvp.presenter.mine.ChangePasswordPresenter
import com.haidie.gridmember.ui.main.activity.LoginActivity
import com.haidie.gridmember.utils.Preference
import kotlinx.android.synthetic.main.change_password.*
import kotlinx.android.synthetic.main.common_toolbar.*
import top.wefor.circularanim.CircularAnim

/**
 * Create by Administrator
 * on 2018/08/28 10:22
 * 修改密码
 */
class ChangePasswordActivity : BaseActivity(), ChangePasswordContract.View {

    private val mPresenter by lazy { ChangePasswordPresenter() }
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    override fun getLayoutId(): Int = R.layout.change_password
    override fun initData() {}
    override fun initView() {
        mPresenter.attachView(this)
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener { onBackPressed() }
        tvTitle.text = "修改密码"

        tv_modify.setOnClickListener {
            CircularAnim.hide(tv_modify)
                    .endRadius((progress_bar.height / 2).toFloat())
                    .go { progress_bar.visibility = View.VISIBLE }
            when {
                et_old_password.text.isEmpty() -> {
                    showShort("旧密码为空")
                    showModify()
                }
                et_new_password.text.isEmpty() -> {
                    showShort("新密码为空")
                    showModify()
                }
                et_confirm_new_password.text.isEmpty() -> {
                    showShort("再次输入新密码为空")
                    showModify()
                }
                et_new_password.text.toString() != et_confirm_new_password.text.toString() -> {
                    showShort("密码不一致")
                    showModify()
                }
                else ->{
                    //调用修改用户密码接口
                    mPresenter.getChangePasswordData(uid,token,et_old_password.text.toString(),et_new_password.text.toString(),et_confirm_new_password.text.toString())
                }
            }
        }
    }
    private fun showModify() {
        CircularAnim.show(tv_modify).go()
        progress_bar.visibility = View.GONE
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {}
    override fun changePasswordSuccess() {
        showShort("修改成功")
        val intent = Intent(this@ChangePasswordActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
        showModify()
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
}