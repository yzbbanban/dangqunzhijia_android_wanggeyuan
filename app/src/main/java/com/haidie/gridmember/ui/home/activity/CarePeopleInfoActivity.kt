package com.haidie.gridmember.ui.home.activity

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.base.BaseFragment
import com.haidie.gridmember.ui.home.fragment.CarePeopleInfoListFragment
import com.haidie.gridmember.ui.home.fragment.FlowPeopleInfoListFragment
import com.haidie.gridmember.utils.LogHelper

import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_order.*

/**
 * Create by   Administrator
 *      on     2018/12/18 10:31
 * description  关爱
 */
class CarePeopleInfoActivity : BaseActivity() {

    private var delivery_id: String? = null


    override fun initData() {
        title = intent.getStringExtra(Constants.NAME)
        delivery_id = intent.getStringExtra(Constants.TEXT)
        tvTitle.text = title
    }

    override fun start() {
    }

    private var mFragments = arrayListOf<BaseFragment>()
    private var mTabDataList = arrayListOf("未回访", "已回访")

    override fun getLayoutId(): Int = R.layout.activity_flow_people

    override fun initView() {
        mLayoutStatusView = multipleStatusOrderView
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener { onBackPressed() }
        initViewPagerAndTabLayout()
    }

    private fun initViewPagerAndTabLayout() {
        mTabDataList.forEachIndexed { index, _ ->
            val fragment = CarePeopleInfoListFragment.getInstance(index + 1, "" + delivery_id)
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