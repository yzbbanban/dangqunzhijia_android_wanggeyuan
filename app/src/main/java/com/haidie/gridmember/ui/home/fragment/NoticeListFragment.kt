package com.haidie.gridmember.ui.home.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseFragment
import com.haidie.gridmember.mvp.bean.NoticeListData
import com.haidie.gridmember.mvp.contract.home.NoticeListContract
import com.haidie.gridmember.mvp.presenter.home.NoticeListPresenter
import com.haidie.gridmember.net.exception.ApiErrorCode
import com.haidie.gridmember.ui.home.activity.WebViewDetailActivity
import com.haidie.gridmember.ui.home.adapter.NoticeListAdapter
import com.haidie.gridmember.utils.LogHelper
import com.haidie.gridmember.utils.Preference
import com.haidie.gridmember.view.RecyclerViewDividerItemDecoration
import kotlinx.android.synthetic.main.fragment_notice_list.*
import org.json.JSONObject

/**
 * Create by   Administrator
 *      on     2018/12/20 18:31
 * description
 */
class NoticeListFragment : BaseFragment(),NoticeListContract.View {

    private var title: String? = null
    private var isRefresh = false
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private val mPresenter by lazy { NoticeListPresenter() }
    private  var mData = mutableListOf<NoticeListData>()
    private lateinit var mAdapter: NoticeListAdapter
    companion object {
        fun getInstance(title: String): NoticeListFragment {
            val fragment = NoticeListFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.title = title
            return fragment
        }
    }
    override fun getLayoutId(): Int = R.layout.fragment_notice_list

    override fun initView() {
        mPresenter.attachView(this)
        setRefresh()
        initRecyclerView()
        mLayoutStatusView = multipleStatusView
    }
    private fun setRefresh() {
        smartLayout.setOnRefreshListener{
            isRefresh = true
            lazyLoad()
            it.finishRefresh(1000)
        }
    }
    private fun initRecyclerView() {
        mAdapter = NoticeListAdapter(R.layout.notice_list_item, mData)
        mAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener{
            _,_,position ->
            val data = mAdapter.data[position]
            val jumpId = data.jump_id
            if (1 == jumpId) {
                val jsonObject = JSONObject(data.params)
                val id = jsonObject.opt("id")
                val url = "/index/grid/detail?id=$id"
                WebViewDetailActivity.startActivity(activity, url)
            }
        }
        recyclerView.let {
            it.layoutManager = LinearLayoutManager(activity)
            it.addItemDecoration(RecyclerViewDividerItemDecoration(activity))
            it.setHasFixedSize(true)
            it.adapter = mAdapter
        }

        smartLayout.setEnableHeaderTranslationContent(true)
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun lazyLoad() {
        mPresenter.getNoticeListData(uid,token)
    }
    private var isFirst: Boolean = true
    override fun setNoticeListData(noticeListData: ArrayList<NoticeListData>) {
        mData = noticeListData
        noticeListData.forEach {
            if (it.params.isNotEmpty() && it.params != "0") {
                LogHelper.d("===========\n${it.params}")
                val jsonObject = JSONObject(it.params)
                val adminId = jsonObject.opt("admin_id")
                val token = jsonObject.opt("token")
                val id = jsonObject.opt("id")
                LogHelper.d("===========\n$adminId\n$token\n$id")
            }
        }
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