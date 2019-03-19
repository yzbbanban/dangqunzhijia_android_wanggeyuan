package com.haidie.gridmember.ui.home.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.bean.PersonnelSupervisionListData
import com.haidie.gridmember.mvp.contract.home.PersonnelSupervisionListContract
import com.haidie.gridmember.mvp.presenter.home.PersonnelSupervisionListPresenter
import com.haidie.gridmember.net.exception.ApiErrorCode
import com.haidie.gridmember.ui.home.adapter.DrugAddictAdapter
import com.haidie.gridmember.ui.home.view.AddResidentInfoDividerItemDecoration
import com.haidie.gridmember.utils.Preference
import com.haidie.gridmember.view.RuntimeRationale
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.activity_drug_addict.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/12/14 19:50
 * description  吸毒人员
 */
class DrugAddictActivity : BaseActivity(),PersonnelSupervisionListContract.View {

    private var mData = mutableListOf<PersonnelSupervisionListData.PersonnelSupervisionListItemData>()
    private lateinit var title : String
    private var id : Int = -1
    private var isRefresh = false
    private var page: Int = 1
    private lateinit var mAdapter: DrugAddictAdapter
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private val mPresenter by lazy { PersonnelSupervisionListPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_drug_addict

    override fun initData() {
        title = intent.getStringExtra(Constants.TEXT)
        id = intent.getIntExtra(Constants.ID,-1)
    }

    override fun initView() {
        mPresenter.attachView(this)
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener{ onBackPressed() }
        tvTitle.text = title
        mLayoutStatusView = multipleStatusView
        mAdapter = DrugAddictAdapter(R.layout.drug_addict_item, mData)
        mAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener{
            _,_,position ->
            AndPermission.with(this)
                .runtime()
                .permission(Permission.ACCESS_FINE_LOCATION)
                .rationale(RuntimeRationale())
                .onGranted {
                    //跳转到回访登记页面
                    val intent = Intent(this, ReturnVisitRegistrationActivity::class.java)
                    intent.putExtra(Constants.ID,mAdapter.data[position].id)
                    intent.putExtra(Constants.HOUSE_ID,mAdapter.data[position].house_id)
                    intent.putExtra(Constants.TYPE,position)
                    startActivity(intent)
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
                }
                .onDenied { permissions -> showSettingDialog(this, permissions) }
                .start()

        }
        recyclerView.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(this)
            it.addItemDecoration(AddResidentInfoDividerItemDecoration(this))
            it.adapter = mAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {
        mPresenter.getPersonnelSupervisionData(uid,token,page,Constants.SIZE,id.toString())
    }
    private var isFirst: Boolean = true
    override fun setPersonnelSupervisionData(personnelSupervisionListData: PersonnelSupervisionListData) {
        mData = personnelSupervisionListData.list
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