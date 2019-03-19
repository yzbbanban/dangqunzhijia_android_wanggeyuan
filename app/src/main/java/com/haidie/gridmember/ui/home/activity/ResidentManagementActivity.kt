package com.haidie.gridmember.ui.home.activity

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.util.SparseArray
import android.view.View
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.bean.BlockInfoData
import com.haidie.gridmember.mvp.bean.UnitListData
import com.haidie.gridmember.mvp.contract.home.ResidentManagementContract
import com.haidie.gridmember.mvp.presenter.home.ResidentManagementPresenter
import com.haidie.gridmember.net.exception.ApiErrorCode
import com.haidie.gridmember.ui.home.adapter.BlockListAdapter
import com.haidie.gridmember.ui.home.adapter.ResidentManagementAdapter
import com.haidie.gridmember.ui.home.view.HouseSearchListRecyclerViewDividerItemDecoration
import com.haidie.gridmember.utils.DateUtils
import com.haidie.gridmember.utils.Preference
import kotlinx.android.synthetic.main.activity_resident_management.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/12/11 11:15
 * description  居民管理
 */
class ResidentManagementActivity : BaseActivity(),ResidentManagementContract.View {

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, -1)
    private val mPresenter by lazy { ResidentManagementPresenter() }
    private var isRefresh = false
    private lateinit var residentManagementAdapter: ResidentManagementAdapter
    private lateinit var mData: ArrayList<BlockInfoData>
    private lateinit var mAdapter: BlockListAdapter
    private var mPvOptions: OptionsPickerView<String>? = null
    override fun getLayoutId(): Int = R.layout.activity_resident_management
    override fun initData() {}
    override fun initView() {
        mPresenter.attachView(this)
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener{ onBackPressed() }
        tvTitle.text = "${DateUtils.getNowTimeMonth()}月居民走访"
        initRecyclerView()
        mLayoutStatusView = multipleStatusView

        smartLayout.setOnRefreshListener{
            isRefresh = true
            start()
            it.finishRefresh(1000)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    private var currentPosition: Int = -1
    private fun initRecyclerView() {
        mData = ArrayList()
        mAdapter = BlockListAdapter(R.layout.house_search_list_item,mData)
        mAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener{
                _,_,position ->
            currentPosition = position
            mPresenter.getUnitListData(uid, token,mAdapter.data[position].title)
//            val intent = Intent(this, HouseDetailActivity::class.java)
//            intent.putExtra(Constants.ID,mAdapter.data[position].id)
//            startActivity(intent)
//            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
        }
        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.addItemDecoration(HouseSearchListRecyclerViewDividerItemDecoration(this))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = mAdapter
        smartLayout.setEnableHeaderTranslationContent(true)
    }
    override fun start() {
//        mPresenter.getBlockUnitData(uid, token)
        mPresenter.getBlockListData(uid, token)
    }
    override fun setBlockUnitData(map: SparseArray<ArrayList<UnitListData>>,list : ArrayList<MultiItemEntity>) {
        residentManagementAdapter = ResidentManagementAdapter(list)
        recyclerView.let {
            it.setHasFixedSize(true)
            val manager = GridLayoutManager(this, 3)
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (residentManagementAdapter.getItemViewType(position) == 2) 1 else manager.spanCount
                }
            }
            it.adapter = residentManagementAdapter
            it.layoutManager = manager
        }
    }
    private var isFirst: Boolean = true
    override fun setBlockListData(blockInfoData: ArrayList<BlockInfoData>) {
        mData = blockInfoData
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
            }
            else -> {
                if (mData.isNotEmpty()) {
                    mAdapter.addData(mData)
                    mAdapter.notifyDataSetChanged()
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
    override fun setUnitListData(unitListData: ArrayList<UnitListData>) {
        val data = ArrayList<String>()
        unitListData.forEach {
            data.add("${it.unit}单元")
        }
        mPvOptions = OptionsPickerBuilder(this@ResidentManagementActivity, OnOptionsSelectListener { options1, _, _, _ ->
            val intent = Intent(this@ResidentManagementActivity, HouseSearchListActivity::class.java)
            intent.putExtra(Constants.SPACE_ID,mAdapter.data[currentPosition].id)
            intent.putExtra(Constants.SPACE,mAdapter.data[currentPosition].title)
            intent.putExtra(Constants.UNIT,unitListData[options1].unit)
            startActivity(intent)
        }).build()
        mPvOptions!!.setPicker(data)
        if (mPvOptions != null && !mPvOptions!!.isShowing) {
            mPvOptions?.show()
        }
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