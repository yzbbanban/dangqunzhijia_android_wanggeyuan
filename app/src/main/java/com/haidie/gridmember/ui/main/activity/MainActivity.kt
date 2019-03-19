package com.haidie.gridmember.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.view.KeyEvent
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.base.BaseFragment
import com.haidie.gridmember.mvp.contract.main.MainContract
import com.haidie.gridmember.mvp.presenter.main.MainPresenter
import com.haidie.gridmember.ui.home.fragment.HomeFragment
import com.haidie.gridmember.ui.life.fragment.LifeFragment
import com.haidie.gridmember.ui.mine.fragment.MineFragment
import com.haidie.gridmember.ui.order.OrderFragment
import com.haidie.gridmember.utils.ActivityCollector
import com.haidie.gridmember.utils.LogHelper
import com.haidie.gridmember.utils.Preference
import com.tencent.android.tpush.XGIOperateCallback
import com.tencent.android.tpush.XGPushManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainContract.View {

    private var mIndex = 0
    private val mTitle = arrayOf("工作", "工单", "生活", "我的")
    private
    val mTag = arrayOf("home", "order", "life", "mine")
    private
    val mPresenter by lazy { MainPresenter() }
    //未选中图标
    private
    val mIconUnselectedIds = intArrayOf(
        R.drawable.ic_home_unselected,
        R.drawable.ic_life_unselected,
        R.drawable.ic_life_unselected,
        R.drawable.ic_mine_unselected
    )
    //选中图标
    private
    val mIconSelectIds = intArrayOf(
        R.drawable.ic_home_select,
        R.drawable.ic_life_select,
        R.drawable.ic_life_select,
        R.drawable.ic_mine_select
    )
    private
    var mHomeFragment: HomeFragment? = null
    private
    var mLifeFragment: LifeFragment? = null
    private
    var mOrderFragment: OrderFragment? = null
    private
    var mMineFragment: MineFragment? = null
    private
    var currentFragment: BaseFragment? = null
    private
    var loginAccount by Preference(
        Constants.ACCOUNT,
        Constants.EMPTY_STRING
    )
    private
    var uid by Preference(
        Constants.UID,
        -1
    )
    private
    var token by Preference(
        Constants.TOKEN,
        Constants.EMPTY_STRING
    )

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        initXGRegister()
        super.onCreate(
            savedInstanceState
        )
    }

    override fun initXG() {
        initXGRegister()
    }

    private fun initXGRegister() {
        XGPushManager.appendAccount(
            this,
            loginAccount,
            object :
                XGIOperateCallback {
                override fun onSuccess(
                    data: Any?,
                    p1: Int
                ) {
                    mPresenter.getXGData(
                        uid,
                        data as String,
                        token
                    )
                }

                override fun onFail(
                    p0: Any?,
                    errCode: Int,
                    msg: String?
                ) {
//                initXGRegister()
                    //避免递归造成oom java.lang.StackOverflowError: stack size 8MB
                    XGPushManager.appendAccount(
                        this@MainActivity,
                        loginAccount
                    )
                }
            })
    }

    override fun onResume() {
        super.onResume()
        val click =
            XGPushManager.onActivityStarted(
                this
            )
        if (click != null) { // 判断是否来自信鸽的打开方式
            LogHelper.d(
                "====================\n通知被点击:" + click.toString()
            )
        }
    }

    override fun onPause() {
        super.onPause()
        XGPushManager.onActivityStoped(
            this
        )
    }

    override fun getLayoutId(): Int =
        R.layout.activity_main

    override fun onNewIntent(
        intent: Intent?
    ) {
        super.onNewIntent(
            intent
        )
        setIntent(
            intent
        )
        var tab =
            intent!!.getIntExtra(
                Constants.TAB,
                -1
            )
        if (tab == -1) {
            tab = 0
        }
        switchFragment(
            tab
        )
    }

    override fun initData() {
        switchFragment(
            mIndex
        )

        rlHome.setOnClickListener {
            switchFragment(
                0
            )
        }
        rlOrder.setOnClickListener {
            switchFragment(
                1
            )
        }
        rlLife.setOnClickListener {
            switchFragment(
                2
            )
        }
        rlMine.setOnClickListener {
            switchFragment(
                3
            )
        }
    }

    /**
     * 切换Fragment
     */
    private fun switchFragment(
        position: Int
    ) {
        val transaction =
            supportFragmentManager.beginTransaction()
        hideFragments(
            transaction
        )
        when (position) {
            0 // 首页
            -> mHomeFragment?.let {
                transaction.show(
                    it
                )
                currentFragment =
                    it
            }
                ?: HomeFragment.getInstance(
                    mTitle[position]
                ).let {
                    mHomeFragment =
                        it
                    transaction.add(
                        R.id.flContainer,
                        it,
                        mTag[position]
                    )
                    currentFragment =
                        it
                }
            1  //工单
            -> mOrderFragment?.let {
                transaction.show(
                    it
                )
                currentFragment =
                    it
            }
                ?: OrderFragment.getInstance(
                    mTitle[position]
                ).let {
                    mOrderFragment =
                        it
                    transaction.add(
                        R.id.flContainer,
                        it,
                        mTag[position]
                    )
                    currentFragment =
                        it
                }
            2 //生活
            -> mLifeFragment?.let {
                transaction.show(
                    it
                )
                currentFragment =
                    it
            }
                ?: LifeFragment.getInstance(
                    mTitle[position]
                ).let {
                    mLifeFragment =
                        it
                    transaction.add(
                        R.id.flContainer,
                        it,
                        mTag[position]
                    )
                    currentFragment =
                        it
                }
            3 //我的
            -> mMineFragment?.let {
                transaction.show(
                    it
                )
                currentFragment =
                    it
            }
                ?: MineFragment.getInstance(
                    mTitle[position]
                ).let {
                    mMineFragment =
                        it
                    transaction.add(
                        R.id.flContainer,
                        it,
                        mTag[position]
                    )
                    currentFragment =
                        it
                }
        }
        mIndex =
            position
        transaction.commitAllowingStateLoss()

        showNormal(
            position
        )
    }

    /**
     * 隐藏所有的Fragment
     */
    private fun hideFragments(
        transaction: FragmentTransaction
    ) {
        mHomeFragment?.let {
            transaction.hide(
                it
            )
        }
        mOrderFragment?.let {
            transaction.hide(
                it
            )
        }
        mLifeFragment?.let {
            transaction.hide(
                it
            )
        }
        mMineFragment?.let {
            transaction.hide(
                it
            )
        }
    }

    private fun showNormal(
        tab: Int
    ) {
        ivHome.setImageResource(
            mIconUnselectedIds[0]
        )
        ivOrder.setImageResource(
            mIconUnselectedIds[1]
        )
        ivLife.setImageResource(
            mIconUnselectedIds[2]
        )
        ivMine.setImageResource(
            mIconUnselectedIds[3]
        )
        when (tab) {
            0 -> ivHome.setImageResource(
                mIconSelectIds[0]
            )
            1 -> ivOrder.setImageResource(
                mIconSelectIds[1]
            )
            2 -> ivLife.setImageResource(
                mIconSelectIds[2]
            )
            3 -> ivMine.setImageResource(
                mIconSelectIds[3]
            )
        }
    }

    override fun initView() {
        mPresenter.attachView(
            this
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun start() {}
    private
    var clickTime: Long =
        0

    override fun onKeyDown(
        keyCode: Int,
        event: KeyEvent?
    ): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val currentTime =
                System.currentTimeMillis()
            when {
                currentTime - clickTime > 2000 -> {
                    showShort(
                        "再按一次退出程序"
                    )
                    clickTime =
                        System.currentTimeMillis()
                }
                else -> ActivityCollector.instance.exitApp()
            }
            return true
        }
        return super.onKeyDown(
            keyCode,
            event
        )
    }

    override fun showLoading() {}
    override fun dismissLoading() {}
}