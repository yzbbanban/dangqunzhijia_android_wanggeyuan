package com.haidie.gridmember.ui.home.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseFragment
import com.haidie.gridmember.mvp.bean.MessageListData
import com.haidie.gridmember.mvp.contract.home.MessageListContract
import com.haidie.gridmember.mvp.presenter.home.MessageListPresenter
import com.haidie.gridmember.net.exception.ApiErrorCode
import com.haidie.gridmember.ui.home.adapter.MessageListAdapter
import com.haidie.gridmember.utils.Preference
import com.haidie.gridmember.view.RecyclerViewDividerItemDecoration
import kotlinx.android.synthetic.main.fragment_notice_list.*

/**
 * Create by   Administrator
 *      on     2018/12/21 12:05
 * description
 */
class MessageListFragment : BaseFragment(),MessageListContract.View {

    private var title: String? = null
    private var isRefresh = false
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private val mPresenter by lazy { MessageListPresenter() }
    private lateinit var mAdapter: MessageListAdapter
    private  var mData = mutableListOf<MessageListData>()
    companion object {
        fun getInstance(title: String): MessageListFragment {
            val fragment = MessageListFragment()
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
        mAdapter = MessageListAdapter(R.layout.message_list_item, mData)

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
        mPresenter.getMessageListData(uid,token)
    }
    private var isFirst: Boolean = true
    override fun setMessageListData(messageListData: ArrayList<MessageListData>) {
        mData = messageListData
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