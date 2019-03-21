package com.haidie.gridmember.ui.order

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseFragment
import com.haidie.gridmember.mvp.bean.OrderData
import com.haidie.gridmember.mvp.bean.OrderListData
import com.haidie.gridmember.mvp.contract.home.OrderContract
import com.haidie.gridmember.mvp.presenter.order.OrderPresenter
import com.haidie.gridmember.ui.home.adapter.OrderRecyclerViewAdapter
import com.haidie.gridmember.utils.LogHelper
import com.haidie.gridmember.utils.Preference
import com.haidie.gridmember.view.RecyclerViewDividerItemDecoration
import kotlinx.android.synthetic.main.fragment_order_list.*

/**
 * Created by ban
 *  on 2019/3/17  20:09
 * description 工单列表
 */
class OrderListFragment : BaseFragment(), OrderContract.View {
    override fun lazyLoad() {
    }

    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var mData = ArrayList<OrderListData>()
    private var orderAdapter: OrderRecyclerViewAdapter? = null
    private val mPresenter by lazy { OrderPresenter() }
    private var isRefresh = false
    private var page: Int = 1
    private var isFirst: Boolean = true
    private var index = 0

    companion object {
        fun getInstance(title: Int): OrderListFragment {
            val fragment = OrderListFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.index = title
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_order_list

    override fun initView() {
        mPresenter.attachView(this)
        setRefresh()
        mLayoutStatusView = orderMultipleStatusView

        mData = ArrayList()
        orderAdapter = OrderRecyclerViewAdapter(R.layout.order_item_view_item, mData)
        rvOrder?.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(activity)
            it.addItemDecoration(RecyclerViewDividerItemDecoration(activity))
            it.adapter = orderAdapter
        }

        orderAdapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
            LogHelper.d("----->" + mData[position].desc)
        }

        slOrder.setEnableHeaderTranslationContent(true)
        start()
    }

    private fun setRefresh() {
        slOrder.setOnRefreshListener {
            page = 1
            isRefresh = true
            start()
            it.finishRefresh(1000)
        }
        slOrder.setOnLoadMoreListener {
            page++
            isRefresh = false
            start()
            it.finishLoadMore(1000)
        }
    }

    private fun start() {
        mPresenter.getOrderData(uid, token, "" + index, "" + page, "" + 10)

    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun showError(msg: String, errorCode: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setOrderData(orderData: OrderData) {
        mData = orderData.list
        when {
            isRefresh -> {
                isFirst = false
                if (mData.isEmpty()) {
                    showShort("暂无数据内容")
                    mLayoutStatusView?.showEmpty()
                    slOrder.isEnableLoadMore = false
                    slOrder.isEnableRefresh = false
                    return
                }
                slOrder.isEnableRefresh = true
                orderAdapter!!.replaceData(mData)
                slOrder.isEnableLoadMore = mData.size >= Constants.SIZE
            }
            else -> {
                if (mData.isNotEmpty()) {
                    orderAdapter!!.addData(mData)
                    orderAdapter!!.notifyDataSetChanged()
                    slOrder.isEnableLoadMore = mData.size >= Constants.SIZE
                    isFirst = false
                } else {
                    if (page > 1) page--
                    if (isFirst) {
                        mLayoutStatusView?.showEmpty()
                        slOrder.isEnableRefresh = false
                        slOrder.isEnableLoadMore = false
                        showShort("暂无数据内容")
                    } else {
                        showShort(resources.getString(R.string.load_more_no_data))
                    }
                }
            }
        }

    }

    override fun showLoading() {}
    override fun dismissLoading() {}
}