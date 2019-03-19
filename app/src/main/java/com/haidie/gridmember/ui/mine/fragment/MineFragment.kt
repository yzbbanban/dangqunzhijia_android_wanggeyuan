package com.haidie.gridmember.ui.mine.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseFragment
import com.haidie.gridmember.mvp.contract.mine.MineContract
import com.haidie.gridmember.mvp.presenter.mine.MinePresenter
import com.haidie.gridmember.ui.main.activity.LoginActivity
import com.haidie.gridmember.ui.mine.activity.AboutUsActivity
import com.haidie.gridmember.ui.mine.activity.ChangePasswordActivity
import com.haidie.gridmember.ui.mine.activity.ModifyAvatarActivity
import com.haidie.gridmember.ui.mine.adapter.MineAdapter
import com.haidie.gridmember.utils.ImageLoader
import com.haidie.gridmember.utils.Preference
import com.haidie.gridmember.view.RecyclerViewDividerItemDecoration
import com.just.agentweb.AgentWebConfig
import com.tencent.android.tpush.XGPushManager
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.mine_header_view.view.*

/**
 * Create by   Administrator
 *      on     2018/12/05 10:39
 * description  我的
 */
class MineFragment : BaseFragment(), MineContract.View  {

    private var uid by Preference(Constants.UID, -1)
    private val mPresenter by lazy { MinePresenter() }
    private var mData = mutableListOf<String>()
    private var mineAdapter : MineAdapter? = null
    private val title = arrayOf("修改密码","关于我们","退出登录")
    private var mTitle: String? = null
    private val mHeaderView by lazy { getHeaderView() }
    private var avatar by Preference(Constants.AVATAR, Constants.EMPTY_STRING)
    private var nickname by Preference(Constants.NICKNAME, Constants.EMPTY_STRING)
    private var mobile by Preference(Constants.MOBILE, Constants.EMPTY_STRING)
    private var mIvPhoto: ImageView? = null
    private var tvNickName: TextView? = null
    private var tvMobile: TextView? = null
    private var loginAccount by Preference(Constants.ACCOUNT,Constants.EMPTY_STRING)
    companion object {
        fun getInstance(title: String): MineFragment {
            val fragment = MineFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }
    override fun getLayoutId(): Int = R.layout.fragment_mine

    override fun initView() {
        mPresenter.attachView(this)
        mLayoutStatusView = multipleStatusView
        smartLayout.setOnRefreshListener {
            lazyLoad()
            it.finishRefresh(1000)
        }
        mineAdapter = MineAdapter(R.layout.mine_item, mData)

        recyclerView?.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(activity)
            it.addItemDecoration(RecyclerViewDividerItemDecoration(activity))
            it.adapter = mineAdapter
        }
        lazyLoad()
        mIvPhoto?.setOnClickListener {
//            修改头像
            val intent = Intent(activity, ModifyAvatarActivity::class.java)
            intent.putExtra(Constants.AVATAR, if (avatar.isEmpty())
                Constants.EMPTY_STRING else avatar.trim())
            startActivity(intent)
            activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
        mineAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener{
            _,_,position ->
            when (position) {
                //跳转到修改密码页面
                0 ->  toActivity(ChangePasswordActivity::class.java)
                //跳转到关于我们页面
                1 ->  toActivity(AboutUsActivity::class.java)
                // 退出登录
                2 ->  mPresenter.getLogoutData(uid)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun lazyLoad() {
        mData.clear()
        mData.addAll(title)
        mineAdapter?.let {
            it.replaceData(mData)
            val headerLayout = it.headerLayout
            if (headerLayout == null) {
                it.addHeaderView(mHeaderView)
            }
        }
        ImageLoader.loadCircle(activity,avatar,mIvPhoto!!)
        tvNickName?.text = nickname
        tvMobile?.text = mobile
    }
    private fun getHeaderView() : View {
        val view = layoutInflater.inflate(R.layout.mine_header_view, recyclerView.parent as ViewGroup, false)
        mIvPhoto = view.ivPhoto
        tvNickName = view.tvNickName
        tvMobile = view.tvMobile
        return view
    }
    override fun setLogoutData(isSuccess: Boolean, msg: String) {
        if (isSuccess) {
            //解绑指定账号
            XGPushManager.delAccount(activity, loginAccount)
            AgentWebConfig.clearDiskCache(activity)
            AgentWebConfig.removeAllCookies()
            startActivity(Intent(activity, LoginActivity::class.java))
        }else{
            showShort(msg)
        }
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
    override fun refresh() {
        smartLayout.autoRefresh()
    }
}