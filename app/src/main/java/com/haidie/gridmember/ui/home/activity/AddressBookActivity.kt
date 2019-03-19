package com.haidie.gridmember.ui.home.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.bean.AddressBookData
import com.haidie.gridmember.mvp.contract.home.AddressBookContract
import com.haidie.gridmember.mvp.presenter.home.AddressBookPresenter
import com.haidie.gridmember.net.exception.ApiErrorCode
import com.haidie.gridmember.ui.home.adapter.AddressBookRecyclerViewAdapter
import com.haidie.gridmember.utils.Preference
import com.haidie.gridmember.view.RecyclerViewDividerItemDecoration
import kotlinx.android.synthetic.main.activity_address_book.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/12/18 16:08
 * description  通讯录
 */
class AddressBookActivity : BaseActivity(),AddressBookContract.View {
    private var isRefresh = false
    private  var adapter: AddressBookRecyclerViewAdapter? = null
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private val mPresenter by lazy { AddressBookPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_address_book
    override fun initData() {}
    override fun initView() {
        mPresenter.attachView(this)
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener{ onBackPressed() }
        tvTitle.text = "通讯录"
        setRefresh()
        mLayoutStatusView = multipleStatusView
        recyclerView.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(this)
            it.addItemDecoration(RecyclerViewDividerItemDecoration(this))
        }
    }
    private fun setRefresh() {
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
    override fun start() {
        mPresenter.getAddressBookData(uid,token)
    }

    override fun setAddressBookData(addressBookData: ArrayList<AddressBookData>) {
        if (adapter == null) {
            adapter = AddressBookRecyclerViewAdapter(R.layout.address_book_recycler_view_item, addressBookData)
            recyclerView.adapter = adapter
        } else {
            adapter?.replaceData(addressBookData)
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