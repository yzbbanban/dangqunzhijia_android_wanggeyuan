package com.haidie.gridmember.ui.home.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.bean.VehicleInformationListData
import com.haidie.gridmember.mvp.contract.home.VehicleInformationListContract
import com.haidie.gridmember.mvp.presenter.home.VehicleInformationListPresenter
import com.haidie.gridmember.net.exception.ApiErrorCode
import com.haidie.gridmember.ui.home.adapter.VehicleInformationListAdapter
import com.haidie.gridmember.ui.home.view.HouseSearchListRecyclerViewDividerItemDecoration
import com.haidie.gridmember.utils.Preference
import kotlinx.android.synthetic.main.activity_vehicle_inforamtion_list.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/12/19 11:13
 * description  其他信息
 */
class VehicleInformationListActivity : BaseActivity(),VehicleInformationListContract.View {

    private var isRefresh = false
    private var mId: Int? = null
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private val mPresenter by lazy { VehicleInformationListPresenter() }
    private lateinit var mData: ArrayList<VehicleInformationListData>
    private lateinit var mAdapter: VehicleInformationListAdapter
    override fun getLayoutId(): Int = R.layout.activity_vehicle_inforamtion_list

    override fun initData() {
        mId = intent.getIntExtra(Constants.ID,-1)
    }

    override fun initView() {
        mPresenter.attachView(this)
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener{ onBackPressed() }
        tvTitle.text = "其他信息"
        ivAdd.visibility = View.VISIBLE
        ivAdd.setOnClickListener {
//            跳转到车辆信息新增页面
            val intent = Intent(this, NewVehicleInformationActivity::class.java)
            intent.putExtra(Constants.ID,mId)
            startActivity(intent)
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
        }
        setRefresh()
        initRecyclerView()
        mLayoutStatusView = multipleStatusView
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    private fun setRefresh() {
        smartLayout.setOnRefreshListener{
            isRefresh = true
            start()
            it.finishRefresh(1000)
        }
    }

    private fun initRecyclerView() {
        mData = ArrayList()
        mAdapter = VehicleInformationListAdapter(R.layout.vehicle_information_list_item,mData)
        mAdapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener{
            _,view,position ->
            when (view.id) {
                R.id.tvEdit -> {
                    val intent = Intent(this, EditVehicleInformationActivity::class.java)
                    intent.putExtra(Constants.ID,mAdapter.data[position].id)
                    startActivity(intent)
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
                }
                R.id.tvDelete -> {
                    mPresenter.getDeleteVehicleInformationData(uid,token,mAdapter.data[position].id)
                }
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(HouseSearchListRecyclerViewDividerItemDecoration(this))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = mAdapter
        smartLayout.setEnableHeaderTranslationContent(true)
    }
    override fun start() {
        mPresenter.getVehicleInformationListData(uid,token,mId!!)
    }
    private var isFirst: Boolean = true
    override fun setVehicleInformationListData(vehicleInformationListData: ArrayList<VehicleInformationListData>) {
        mData = vehicleInformationListData
        when{
            isRefresh -> {
                isFirst = false
                if (mData.isEmpty()) {
                    showShort( "暂无数据内容")
                    mLayoutStatusView?.showEmpty()
                    smartLayout.isEnableLoadMore = false
                    smartLayout.isEnableRefresh = false
                    return
                }
                smartLayout.isEnableRefresh = true
                mAdapter.replaceData(mData)
                smartLayout.isEnableLoadMore = mData.size >= Constants.SIZE
            }
            else -> {
                if (mData.isNotEmpty()) {
                    mAdapter.addData(mData)
                    mAdapter.notifyDataSetChanged()
                    smartLayout.isEnableLoadMore = mData.size >= Constants.SIZE
                } else {
                    if (isFirst) {
                        mLayoutStatusView?.showEmpty()
                        smartLayout.isEnableRefresh = false
                        smartLayout.isEnableLoadMore = false
                        showShort( "暂无数据内容")
                    }else{
                        showShort(resources.getString(R.string.load_more_no_data))
                    }
                }
            }
        }
    }

    override fun setDeleteVehicleInformationData(isSuccess: Boolean, msg: String) {
        if (isSuccess) {
            showShort("删除成功")
            refresh()
        }else{
            showShort(if (msg.isEmpty()) "删除失败" else msg)
        }
    }
    override fun refresh() {
        smartLayout.autoRefresh()
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