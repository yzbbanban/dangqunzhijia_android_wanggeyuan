package com.haidie.gridmember.ui.home.activity

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.bean.SuperviseCountData
import com.haidie.gridmember.mvp.contract.home.PersonnelSupervisionContract
import com.haidie.gridmember.mvp.presenter.home.PersonnelSupervisionPresenter
import com.haidie.gridmember.net.exception.ApiErrorCode
import com.haidie.gridmember.ui.home.adapter.PersonnelSupervisionAdapter
import com.haidie.gridmember.ui.home.view.AddResidentInfoDividerItemDecoration
import com.haidie.gridmember.utils.Preference
import kotlinx.android.synthetic.main.activity_personnel_supervision.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/12/14 18:41
 * description  人员监管
 */
class PersonnelSupervisionActivity : BaseActivity(),PersonnelSupervisionContract.View {
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var isRefresh = false
    private var mData = mutableListOf<SuperviseCountData>()
    private lateinit var adapter: PersonnelSupervisionAdapter
    private val mPresenter by lazy { PersonnelSupervisionPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_personnel_supervision

    override fun initData() {}

    override fun initView() {
        mPresenter.attachView(this)
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener{ onBackPressed() }
        tvTitle.text = "人员监管"
        setRefresh()
        initRecyclerView()
        mLayoutStatusView = multipleStatusView
    }
    private fun setRefresh() {
        smartLayout.setOnRefreshListener{
            isRefresh = true
            start()
            it.finishRefresh(1000)
        }
    }
    private fun initRecyclerView() {
        adapter = PersonnelSupervisionAdapter(R.layout.personnel_supervision_item, mData)
        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener{
                _,_,position ->
            val intent = Intent(this, DrugAddictActivity::class.java)
            intent.putExtra(Constants.TEXT,adapter.data[position].keyname)
            intent.putExtra(Constants.ID,adapter.data[position].value)
            startActivity(intent)
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
        }
        recyclerView.let {
            it.setHasFixedSize(true)
            it.layoutManager = GridLayoutManager(this,3)
            it.addItemDecoration(AddResidentInfoDividerItemDecoration(this))
            it.adapter = adapter
        }
        smartLayout.setEnableHeaderTranslationContent(true)
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {
        mPresenter.getSuperviseCountData(uid,token)
    }
    private var isFirst: Boolean = true
    override fun setSuperviseCountData(superviseCountData: ArrayList<SuperviseCountData>) {
        mData = superviseCountData
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
                adapter.replaceData(mData)
                smartLayout.isEnableLoadMore = mData.size >= Constants.SIZE
            }
            else -> {
                if (mData.isNotEmpty()) {
                    adapter.addData(mData)
                    adapter.notifyDataSetChanged()
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