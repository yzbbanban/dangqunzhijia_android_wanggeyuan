package com.haidie.gridmember.ui.home.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseFragment
import com.haidie.gridmember.mvp.bean.FlowPeopleData
import com.haidie.gridmember.mvp.bean.FlowPeopleListData
import com.haidie.gridmember.mvp.contract.home.FlowPeopleContract
import com.haidie.gridmember.mvp.presenter.home.FlowPeoplePresenter
import com.haidie.gridmember.ui.home.activity.*
import com.haidie.gridmember.ui.home.adapter.FlowPeopleRecyclerViewAdapter
import com.haidie.gridmember.utils.LogHelper
import com.haidie.gridmember.utils.Preference
import com.haidie.gridmember.view.RecyclerViewDividerItemDecoration
import com.haidie.gridmember.view.RuntimeRationale
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_order_list.*

/**
 * Created by ban
 *  on 2019/3/17  20:09
 * description 流动人口
 */
class FlowPeopleInfoListFragment : BaseFragment(), FlowPeopleContract.View {

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, -1)
    private var mData = ArrayList<FlowPeopleListData>()
    private var orderAdapter: FlowPeopleRecyclerViewAdapter? = null
    private val mPresenter by lazy { FlowPeoplePresenter() }
    private var loginAccount by Preference(Constants.ACCOUNT, Constants.EMPTY_STRING)
    private var index = 0

    companion object {
        fun getInstance(index: Int): FlowPeopleInfoListFragment {
            val fragment = FlowPeopleInfoListFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.index = index
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_order_list

    override fun initView() {
        mPresenter.attachView(this)
        slOrder.setOnRefreshListener {
            lazyLoad()
            it.finishRefresh(1000)
        }
        orderAdapter = FlowPeopleRecyclerViewAdapter(R.layout.flow_item_view_item, mData)

        rvOrder?.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(activity)
            it.addItemDecoration(RecyclerViewDividerItemDecoration(activity))
            it.adapter = orderAdapter
        }
        lazyLoad()
        mPresenter.getFlowPeoData(uid, token, "" + index)

        orderAdapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
            AndPermission.with(this)
                .runtime()
                .permission(Permission.ACCESS_FINE_LOCATION)
                .rationale(RuntimeRationale())
                .onGranted {
                    //跳转到回访登记页面
                    val intent = Intent(activity, ReturnVisitActivity::class.java)
                    intent.putExtra(Constants.ID, mData[position].id)
                    startActivity(intent)
                    activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
                }
                .start()

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun setFlowPeoData(flowPeopleData: FlowPeopleData) {
        mData.clear()
        mData.addAll(flowPeopleData.list)
        orderAdapter?.let {
            it.replaceData(mData)
        }
    }


    override fun lazyLoad() {
//        var data1 = FlowPeopleListData(1, "banban1", 12, 1111, 111211)
//        var data2 = FlowPeopleListData(2, "banban2", 22, 2121, 22322)
//        var data3 = FlowPeopleListData(3, "banban3", 33, 3131, 333433)
//        var data4 = FlowPeopleListData(4, "banban4", 44, 41414, 44344)
//
//        mData.clear()
//        mData.add(data1)
//        mData.add(data2)
//        mData.add(data3)
//        mData.add(data4)
//        orderAdapter?.let {
//            it.replaceData(mData)
//        }
    }


    override fun showError(msg: String, errorCode: Int) {
    }


    override fun showLoading() {}
    override fun dismissLoading() {}
}