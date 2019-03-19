package com.haidie.gridmember.ui.home.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.bean.ToDoItemData
import com.haidie.gridmember.mvp.bean.ToDoListData
import com.haidie.gridmember.mvp.contract.home.ToDoListContract
import com.haidie.gridmember.mvp.presenter.home.ToDoListPresenter
import com.haidie.gridmember.net.exception.ApiErrorCode
import com.haidie.gridmember.ui.home.adapter.ToDoListAdapter
import com.haidie.gridmember.utils.Preference
import com.haidie.gridmember.view.RecyclerViewDividerItemDecoration
import kotlinx.android.synthetic.main.activity_to_do_list.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/12/17 14:39
 * description  待办事项
 */
class ToDoListActivity : BaseActivity(), ToDoListContract.View {

    private val titles = arrayOf(
        "物业问题", "家庭走访",
        "矛盾纠纷", "公共安全",
        "人员监管", "基本诉求"
    )
    private var isRefresh = false
    private var mData = mutableListOf<ToDoItemData>()
    private lateinit var adapter: ToDoListAdapter
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private val mPresenter by lazy { ToDoListPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_to_do_list
    override fun initData() {
        val type = intent.getStringExtra(Constants.TYPE)
        if (type != null && type.isNotEmpty()) {
            toProblemReportingList(type)
        }
    }

    override fun initView() {
        mPresenter.attachView(this)
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener { onBackPressed() }
        tvTitle.text = "待办事项"
        setRefresh()
        initRecyclerView()
        mLayoutStatusView = multipleStatusView
    }
    private fun setRefresh() {
        smartLayout.setOnRefreshListener {
            isRefresh = true
            start()
            it.finishRefresh(1000)
        }
    }

    private fun initRecyclerView() {
        adapter = ToDoListAdapter(R.layout.to_do_list_item, mData)
        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
            when (position) {
//                物业问题、矛盾纠纷、公共安全
                0, 2, 3, 5 -> {
                    var type = ""
                    when (position) {
                        0 -> type = "1"
                        2 -> type = "2"
                        3 -> type = "3"
                        5 -> type = "4"
                    }
                    toProblemReportingList(type)
//                    val intent = Intent(this, ProblemReportingListActivity::class.java)
//                    intent.putExtra(Constants.TYPE, type)
//                    startActivity(intent)
//                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
                }
//                居民管理
                1 -> toActivity(ResidentManagementActivity::class.java)
//                人员监管
                4 -> toActivity(PersonnelSupervisionActivity::class.java)
//                关爱对象
//                5 -> toActivity(CaringObjectActivity::class.java)
            }
        }
        recyclerView.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(this)
            it.addItemDecoration(RecyclerViewDividerItemDecoration(this))
            it.adapter = adapter
        }
        smartLayout.setEnableHeaderTranslationContent(true)
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {
        mPresenter.getToDoListData(uid, token)
    }
    override fun setToDoListData(toDoListData: ToDoListData) {
        mData.clear()
        mData.add(ToDoItemData(titles[0], toDoListData.problem_report_sum))
        mData.add(ToDoItemData(titles[1], toDoListData.house_sum))
        mData.add(ToDoItemData(titles[2], toDoListData.contradiction_sum))
        mData.add(ToDoItemData(titles[3], toDoListData.public_safe_sum))
        mData.add(ToDoItemData(titles[4], toDoListData.person_sum))
        mData.add(ToDoItemData(titles[5], toDoListData.basic_appeal_sum))
        adapter.replaceData(mData)
    }
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
        when (errorCode) {
            ApiErrorCode.NETWORK_ERROR -> mLayoutStatusView?.showNoNetwork()
            else -> mLayoutStatusView?.showError()
        }
    }
    override fun showLoading() {
        if (!isRefresh) {
            isRefresh = false
            mLayoutStatusView?.showLoading()
        }
    }
    override fun dismissLoading() {
        mLayoutStatusView?.showContent()
    }
    private fun toProblemReportingList(type :String) {
        val intent = Intent(this, ProblemReportingListActivity::class.java)
        intent.putExtra(Constants.TYPE, type)
        startActivity(intent)
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
    }
}