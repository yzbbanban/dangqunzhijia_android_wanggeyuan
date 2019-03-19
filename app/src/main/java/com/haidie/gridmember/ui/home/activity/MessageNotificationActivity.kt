package com.haidie.gridmember.ui.home.activity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.base.BaseFragment
import com.haidie.gridmember.ui.home.fragment.MessageListFragment
import kotlinx.android.synthetic.main.activity_message_notification.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/12/20 16:42
 * description  消息通知
 */
class MessageNotificationActivity : BaseActivity() {
    private var mTabDataList = arrayListOf("消息"
//        ,"通知"
    )
    private val mFragments = ArrayList<BaseFragment>()
    override fun getLayoutId(): Int = R.layout.activity_message_notification
    override fun initData() {}
    override fun initView() {
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener{ onBackPressed() }
        tvTitle.text = "消息通知"
        val fragmentMessage = MessageListFragment.getInstance(mTabDataList[0])
//        val fragmentNotice = NoticeListFragment.getInstance("通知")
        mFragments.add(fragmentMessage)
//        mFragments.add(fragmentNotice)
        tabLayout.visibility = View.GONE
        viewPager.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment = mFragments[position]
            override fun getCount(): Int = mTabDataList.size
            override fun getPageTitle(position: Int): CharSequence = mTabDataList[position]
        }
        tabLayout.setViewPager(viewPager)
        viewPager.currentItem = 0
    }
    override fun start() {
    }
}