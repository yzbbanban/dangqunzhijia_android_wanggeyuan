package com.haidie.gridmember.ui.home.activity

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.bean.HouseList
import com.haidie.gridmember.mvp.bean.HouseListData
import com.haidie.gridmember.mvp.contract.home.HouseSearchListContract
import com.haidie.gridmember.mvp.presenter.home.HouseSearchListPresenter
import com.haidie.gridmember.net.exception.ApiErrorCode
import com.haidie.gridmember.ui.home.adapter.HouseSearchListAdapter
import com.haidie.gridmember.ui.home.view.HouseSearchListRecyclerViewDividerItemDecoration
import com.haidie.gridmember.utils.Preference
import kotlinx.android.synthetic.main.activity_house_search_list.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/12/12 18:03
 * description  1栋1单元
 */
class HouseSearchListActivity : BaseActivity(),HouseSearchListContract.View {

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, -1)
    private  var spaceId:Int? = null
    private lateinit var space:String
    private lateinit var unit:String
    private var isRefresh = false
    private var page: Int = 1
    private val mPresenter by lazy { HouseSearchListPresenter() }
    private lateinit var mAdapter: HouseSearchListAdapter
    private lateinit var mData: ArrayList<HouseList.HouseListItem>
    override fun getLayoutId(): Int = R.layout.activity_house_search_list

    override fun initData() {
        spaceId = intent.getIntExtra(Constants.SPACE_ID,-1)
        space = intent.getStringExtra(Constants.SPACE)
        unit = intent.getStringExtra(Constants.UNIT)
    }

    override fun initView() {
        mPresenter.attachView(this)
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener{ onBackPressed() }
        tvTitle.text = "${space}栋${unit}单元"
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
            page = 1
            isRefresh = true
            start()
            it.finishRefresh(1000)
        }
        smartLayout.setOnLoadMoreListener{
            page++
            isRefresh = false
            start()
            it.finishLoadMore(1000)
        }
    }

    private fun initRecyclerView() {
        mData = ArrayList()
        mAdapter = HouseSearchListAdapter(R.layout.house_search_list_item,mData)
        mAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener{
            _,_,position ->
            val intent = Intent(this, HouseDetailActivity::class.java)
            intent.putExtra(Constants.SPACE_ID,spaceId)
            intent.putExtra(Constants.ID,mAdapter.data[position].id)
            startActivity(intent)
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
        }
        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.addItemDecoration(HouseSearchListRecyclerViewDividerItemDecoration(this))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = mAdapter
        smartLayout.setEnableHeaderTranslationContent(true)
    }
    override fun start() {
        mPresenter.getHouseList(uid,token,page,Constants.SIZE,spaceId.toString(),unit)
    }
    private var isFirst: Boolean = true
    override fun setHouseList(houseList: HouseList) {
        mData = houseList.list
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
                    isFirst = false
                } else {
                    if (page > 1) page --
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
    override fun setHouseListData(houseListData: ArrayList<HouseListData>) {}
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
        when (errorCode) {
            ApiErrorCode.NETWORK_ERROR -> if (isFirst) {
                mLayoutStatusView?.showNoNetwork()
            }
            else -> if (isFirst) {
                mLayoutStatusView?.showError()
            }
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