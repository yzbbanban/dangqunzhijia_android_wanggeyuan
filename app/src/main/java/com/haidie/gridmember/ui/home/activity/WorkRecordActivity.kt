package com.haidie.gridmember.ui.home.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.bean.WorkRecordData
import com.haidie.gridmember.ui.home.adapter.WorkRecordAdapter
import com.haidie.gridmember.ui.home.view.HouseDetailRecyclerViewDividerItemDecoration
import com.haidie.gridmember.utils.ImageLoader
import kotlinx.android.synthetic.main.activity_work_record.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.work_record_footer_view.view.*

/**
 * Create by   Administrator
 *      on     2018/12/17 15:16
 * description
 */
class WorkRecordActivity : BaseActivity() {
    private lateinit var title : String
    private val titles = arrayOf(
        "问题上报", "居民管理",
        "矛盾纠纷", "关爱对象",
        "公共安全", "公共安全")
    private var mData = mutableListOf<WorkRecordData>()
    private var avatar: ImageView? = null
    private var username: TextView? = null
    private var phone: TextView? = null
    private val mHeaderView by lazy { getHeaderView() }
    private lateinit var mAdapter: WorkRecordAdapter
    override fun getLayoutId(): Int = R.layout.activity_work_record

    override fun initData() {
        title = intent.getStringExtra(Constants.TEXT)
        mData.clear()
        mData.add(WorkRecordData(titles[0], "20"))
        mData.add(WorkRecordData(titles[1], "300"))
        mData.add(WorkRecordData(titles[2], "20"))
        mData.add(WorkRecordData(titles[3], "10"))
        mData.add(WorkRecordData(titles[4], "15"))
        mData.add(WorkRecordData(titles[5], "16"))
    }

    override fun initView() {
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener{ onBackPressed() }
        tvTitle.text = title
        tvSubmit.visibility = View.VISIBLE
        tvSubmit.text = "历史记录"

        mAdapter = WorkRecordAdapter(R.layout.work_record_item, mData)
        recyclerView.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(this)
            it.addItemDecoration(HouseDetailRecyclerViewDividerItemDecoration(this))
            it.adapter = mAdapter
        }
        tvSubmit.setOnClickListener {
            toActivity(HistoryRecordActivity::class.java)
        }
    }

    override fun start() {
        mAdapter.let {
            it.replaceData(mData)
            val headerLayout = it.headerLayout
            if (headerLayout == null) {
                it.addHeaderView(mHeaderView)
            }
            ImageLoader.load(this, R.drawable.ic_header,avatar!!)
            username?.text = "何宝阳 网格员"
            phone?.text = "18512503890"
        }
    }
    private fun getHeaderView() : View{
        val view = layoutInflater.inflate(R.layout.work_record_footer_view, recyclerView.parent as ViewGroup, false)
        avatar = view.ivAvatar
        username = view.tvUsername
        phone = view.tvPhone
        return view
    }
}