package com.haidie.gridmember.ui.life.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseFragment
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_life.*

/**
 * Created by admin2
 *  on 2018/08/13  19:52
 * description  生活
 */
class LifeFragment : BaseFragment() {
    private var mTitle: String? = null
    private var mFragments = arrayListOf<BaseFragment>()
    private var mTabDataList = arrayListOf("社区新闻","参观")

    companion object {
        fun getInstance(title: String): LifeFragment {
            val fragment = LifeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }
    override fun getLayoutId(): Int = R.layout.fragment_life
    override fun initView() {
        tvTitle.text = mTitle
        mLayoutStatusView = multipleStatusView
        initViewPagerAndTabLayout()
    }
    private fun initViewPagerAndTabLayout() {
        mTabDataList.forEachIndexed { index, _ ->
            val fragment = LifeListFragment.getInstance(index+1)
            mFragments.add(fragment)
        }
        viewPagerLife.adapter = object : FragmentStatePagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment = mFragments[position]
            override fun getCount(): Int = mTabDataList.size
            override fun getPageTitle(position: Int): CharSequence? = mTabDataList[position]
        }
        tabLayoutLife.setViewPager(viewPagerLife)
        viewPagerLife.currentItem = 0
        viewPagerLife.offscreenPageLimit = mTabDataList.size
    }
    override fun lazyLoad() {}
}