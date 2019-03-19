package com.haidie.gridmember.ui.home.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.bean.HistoryRecordData
import com.haidie.gridmember.ui.home.adapter.HistoryRecordAdapter
import com.haidie.gridmember.ui.home.view.HouseDetailRecyclerViewDividerItemDecoration
import kotlinx.android.synthetic.main.activity_history_record.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/12/17 16:02
 * description
 */
class HistoryRecordActivity : BaseActivity() {
    private val titles = arrayOf(
        "12月", "11月",
        "10月", "09月",
        "08月", "07月")
    private var mData = mutableListOf<HistoryRecordData>()
    private lateinit var mAdapter: HistoryRecordAdapter
    override fun getLayoutId(): Int = R.layout.activity_history_record

    override fun initData() {
        mData.clear()
        mData.add(HistoryRecordData(titles[0], "500"))
        mData.add(HistoryRecordData(titles[1], "400"))
        mData.add(HistoryRecordData(titles[2], "300"))
        mData.add(HistoryRecordData(titles[3], "200"))
        mData.add(HistoryRecordData(titles[4], "300"))
        mData.add(HistoryRecordData(titles[5], "160"))
    }

    override fun initView() {
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener{ onBackPressed() }
        tvTitle.text = "历史记录"

        mAdapter = HistoryRecordAdapter(R.layout.work_record_item, mData)
        recyclerView.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(this)
            it.addItemDecoration(HouseDetailRecyclerViewDividerItemDecoration(this))
            it.adapter = mAdapter
        }
    }
    override fun start() {
    }
}