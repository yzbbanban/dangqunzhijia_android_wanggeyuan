package com.haidie.gridmember.ui.home.activity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.base.BaseFragment
import com.haidie.gridmember.ui.home.fragment.FlowPeopleInfoListFragment
import com.haidie.gridmember.ui.home.fragment.WorkListFragment
import com.haidie.gridmember.ui.order.OrderListFragment

import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_order.*

/**
 * Create by   Administrator
 *      on     2018/12/18 10:31
 * description  流动人口
 */
class WorkTaskInfoActivity : BaseActivity() {
    override fun initData() {
    }

    override fun start() {
    }

    private var mFragments = arrayListOf<BaseFragment>()
    private var mTabDataList = arrayListOf("未处理", "已处理")

    override fun getLayoutId(): Int = R.layout.fragment_work
    override fun initView() {
        tvTitle.text = "任务清单"
        mLayoutStatusView = multipleStatusOrderView
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener { onBackPressed() }
        initViewPagerAndTabLayout()
    }

    private fun initViewPagerAndTabLayout() {
        mTabDataList.forEachIndexed { index, _ ->
            val fragment = WorkListFragment.getInstance(index + 1)
            mFragments.add(fragment)
        }
        viewPagerOrder.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment = mFragments[position]
            override fun getCount(): Int = mTabDataList.size
            override fun getPageTitle(position: Int): CharSequence? = mTabDataList[position]
        }
        tabLayoutOrder.setViewPager(viewPagerOrder)
        viewPagerOrder.currentItem = 0
        viewPagerOrder.offscreenPageLimit = mTabDataList.size
    }

}