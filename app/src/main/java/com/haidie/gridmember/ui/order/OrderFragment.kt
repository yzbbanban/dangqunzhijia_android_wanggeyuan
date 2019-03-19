package com.haidie.gridmember.ui.order

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseFragment
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_order.*

/**
 * Created by ban
 *  on 2018/08/13  19:52
 * description  工单
 */
class OrderFragment : BaseFragment() {
    private var mTitle: String? = null
    private var mFragments = arrayListOf<BaseFragment>()
    private var mTabDataList = arrayListOf("未派单", "已派单")

    companion object {
        fun getInstance(title: String): OrderFragment {
            val fragment = OrderFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_order
    override fun initView() {
        tvTitle.text = mTitle
        mLayoutStatusView = multipleStatusOrderView
        initViewPagerAndTabLayout()
    }

    private fun initViewPagerAndTabLayout() {
        mTabDataList.forEachIndexed { index, _ ->
            val fragment = OrderListFragment.getInstance(index + 1)
            mFragments.add(fragment)
        }
        viewPagerOrder.adapter = object : FragmentStatePagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment = mFragments[position]
            override fun getCount(): Int = mTabDataList.size
            override fun getPageTitle(position: Int): CharSequence? = mTabDataList[position]
        }
        tabLayoutOrder.setViewPager(viewPagerOrder)
        viewPagerOrder.currentItem = 0
        viewPagerOrder.offscreenPageLimit = mTabDataList.size
    }

    override fun lazyLoad() {}
}