package com.haidie.gridmember.ui.order

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseFragment
import com.haidie.gridmember.mvp.bean.AddressBookData
import com.haidie.gridmember.mvp.bean.OrderData
import com.haidie.gridmember.mvp.contract.home.OrderContract
import com.haidie.gridmember.ui.home.adapter.OrderRecyclerViewAdapter
import com.haidie.gridmember.utils.ImageLoader
import com.haidie.gridmember.utils.Preference
import com.haidie.gridmember.view.RecyclerViewDividerItemDecoration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.fragment_order_list.*
import kotlinx.android.synthetic.main.mine_header_view.view.*

/**
 * Created by ban
 *  on 2019/3/17  20:09
 * description 工单列表
 */
class OrderListFragment : BaseFragment(), OrderContract.View {

    private var uid by Preference(Constants.UID, -1)
    private var mData = ArrayList<OrderData>()
    private var orderAdapter: OrderRecyclerViewAdapter? = null
    private var avatar by Preference(Constants.AVATAR, Constants.EMPTY_STRING)
    private var nickname by Preference(Constants.NICKNAME, Constants.EMPTY_STRING)
    private var mobile by Preference(Constants.MOBILE, Constants.EMPTY_STRING)
    private var mIvPhoto: ImageView? = null
    private var tvNickName: TextView? = null
    private var tvMobile: TextView? = null
    private var loginAccount by Preference(Constants.ACCOUNT, Constants.EMPTY_STRING)

    companion object {
        fun getInstance(title: Int): OrderListFragment {
            val fragment = OrderListFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_order_list

    override fun initView() {
        mLayoutStatusView = multipleStatusView
        slOrder.setOnRefreshListener {
            lazyLoad()
            it.finishRefresh(1000)
        }
        orderAdapter = OrderRecyclerViewAdapter(R.layout.order_item_view_item, mData)

        rvOrder?.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(activity)
            it.addItemDecoration(RecyclerViewDividerItemDecoration(activity))
            it.adapter = orderAdapter
        }
        lazyLoad()

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun lazyLoad() {
        var imageList = ArrayList<String>()
        var data1 = OrderData(1, "banban1", "18795980531", "xxx11", imageList, "11111")
        var data2 = OrderData(2, "banban2", "18795980532", "x22xx", imageList, "2222")
        var data3 = OrderData(3, "banban3", "18795980533", "xx33x", imageList, "33333")
        var data4 = OrderData(4, "banban4", "18795980534", "xx44x", imageList, "444444")

        mData.clear()
        mData.add(data1)
        mData.add(data2)
        mData.add(data3)
        mData.add(data4)
        orderAdapter?.let {
            it.replaceData(mData)
        }
    }


    override fun showError(msg: String, errorCode: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setOrderData(orderData: ArrayList<OrderData>) {

    }

    override fun showLoading() {}
    override fun dismissLoading() {}
}