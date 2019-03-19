package com.haidie.gridmember.ui.home.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.bean.ReportManagementData
import com.haidie.gridmember.mvp.bean.ReportManagementItemData
import com.haidie.gridmember.mvp.contract.home.ReportManagementContract
import com.haidie.gridmember.mvp.presenter.home.ReportManagementPresenter
import com.haidie.gridmember.net.exception.ApiErrorCode
import com.haidie.gridmember.ui.home.adapter.ReportManagementAdapter
import com.haidie.gridmember.utils.DateUtils
import com.haidie.gridmember.utils.Preference
import com.haidie.gridmember.view.RecyclerViewDividerItemDecoration
import kotlinx.android.synthetic.main.activity_report_management.*
import kotlinx.android.synthetic.main.common_toolbar.*
import java.util.*

/**
 * Create by   Administrator
 *      on     2018/12/20 14:15
 * description  上报管理
 */
class ReportManagementActivity : BaseActivity(),ReportManagementContract.View {

    private val title = arrayOf(
        "物业问题","矛盾纠纷",
        "公共安全","基本诉求")
    private val mData = mutableListOf<ReportManagementItemData>()
    private lateinit var mAdapter: ReportManagementAdapter
    private var pvTime: TimePickerView? = null
    private var isRefresh = false
    private var yearMonth: String = DateUtils.getNowTimeYearMonth()
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private val mPresenter by lazy { ReportManagementPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_report_management

    override fun initData() {}

    override fun initView() {
        mPresenter.attachView(this)
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener { onBackPressed() }
        tvTitle.text = "上报管理"
        mLayoutStatusView = multipleStatusView
        initTimePicker()
        linearLayout.setOnClickListener {
            if (pvTime != null) {
                pvTime!!.show()
            }
        }
        mAdapter = ReportManagementAdapter(R.layout.report_management_item, null)
        mAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener{
            _,_,position ->
            val intent = Intent(this, ReportManagementWebViewActivity::class.java)
            intent.putExtra(Constants.TYPE, "${position+1}")
            intent.putExtra(Constants.YEAR_MONTH, yearMonth)
            startActivity(intent)
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
        recyclerView?.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(this)
            it.addItemDecoration(RecyclerViewDividerItemDecoration(this))
            it.adapter = mAdapter
        }

    }

    private fun initTimePicker() {
        val endDate = Calendar.getInstance()
        val nowTime = DateUtils.getNowTime()
        val strings = nowTime.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val calendar = strings[0].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        endDate.set(Integer.parseInt(calendar[0]), Integer.parseInt(calendar[1]) - 1,
            Integer.parseInt(calendar[2]))
        val start = Calendar.getInstance()
        start.set(2018, 6, 1)
        pvTime = TimePickerBuilder(this) { date, _ ->
            tvYearMonth.text = DateUtils.dateToYearMonth(date)
            yearMonth = DateUtils.dateToYearMonth(date)
            start()
        }
            .setType(booleanArrayOf(true,true,false,false,false,false))
            .isDialog(true)
            .setDate(start)
            .setRangDate(start, endDate)
            .build()
        val mDialog = pvTime!!.dialog
        if (mDialog != null) {
            val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM)
            params.leftMargin = 0
            params.rightMargin = 0
            pvTime!!.dialogContainerLayout.layoutParams = params
            val dialogWindow = mDialog.window
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim)//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM)//改成Bottom,底部显示
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {
        mPresenter.getReportManagementData(uid,token,yearMonth)
    }
    override fun setReportManagementData(reportManagementData: ReportManagementData) {
        mData.clear()
        mData.add(ReportManagementItemData(title[0],reportManagementData.module1_sum))
        mData.add(ReportManagementItemData(title[1],reportManagementData.module2_sum))
        mData.add(ReportManagementItemData(title[2],reportManagementData.module3_sum))
        mData.add(ReportManagementItemData(title[3],reportManagementData.module4_sum))
        mAdapter.replaceData(mData)
    }
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
        when (errorCode) {
            ApiErrorCode.NETWORK_ERROR ->   mLayoutStatusView?.showNoNetwork()
            else ->   mLayoutStatusView?.showError()
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
}