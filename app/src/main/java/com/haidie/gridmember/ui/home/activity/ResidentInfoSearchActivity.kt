package com.haidie.gridmember.ui.home.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.EditorInfo
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.bean.ResidentInfoSearchListData
import com.haidie.gridmember.mvp.contract.home.ResidentInfoSearchContract
import com.haidie.gridmember.mvp.presenter.home.ResidentInfoSearchPresenter
import com.haidie.gridmember.net.exception.ApiErrorCode
import com.haidie.gridmember.ui.home.adapter.ResidentInfoSearchAdapter
import com.haidie.gridmember.utils.Preference
import com.haidie.gridmember.view.RuntimeRationale
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.activity_resident_info_search.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/12/25 09:45
 * description  居民信息搜索
 */
class ResidentInfoSearchActivity : BaseActivity(), ResidentInfoSearchContract.View {
    private var isRefresh = false
    private var houseId: Int? = null
    private  var spaceId:Int? = null
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private val mPresenter by lazy { ResidentInfoSearchPresenter() }
    private lateinit var mAdapter: ResidentInfoSearchAdapter
    private lateinit var mData: ArrayList<ResidentInfoSearchListData>
    override fun getLayoutId(): Int = R.layout.activity_resident_info_search

    override fun initData() {
        spaceId = intent.getIntExtra(Constants.SPACE_ID,-1)
        houseId = intent.getIntExtra(Constants.HOUSE_ID,-1)
    }

    override fun initView() {
        mPresenter.attachView(this)
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener { onBackPressed() }
        tvTitle.text = "添加居民信息"
        ivAdd.visibility = View.VISIBLE
        setRefresh()
        initRecyclerView()
        mLayoutStatusView = multipleStatusView
        ivAdd.setOnClickListener { _ ->
            AndPermission.with(this)
                .runtime()
                .permission(Permission.WRITE_EXTERNAL_STORAGE,Permission.CAMERA)
                .rationale(RuntimeRationale())
                .onGranted {
                    //添加基本信息
                    val intent = Intent(this, BasicInfoActivity::class.java)
                    intent.putExtra(Constants.SPACE_ID,spaceId)
                    intent.putExtra(Constants.HOUSE_ID,houseId)
                    startActivity(intent)
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
                }
                .onDenied { permissions ->  showSettingDialog(this, permissions)  }
                .start()

        }
        tvSearch.setOnClickListener {
            closeKeyboard(etName,this)
            if (etName.text.isEmpty()) {
                showShort("请输入需要添加的居民姓名")
                return@setOnClickListener
            }
            isLoad = true
            mPresenter.getResidentInfoSearchListData(uid, token, etName.text.toString())
        }
        etName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH){//搜索按键action
                closeKeyboard(etName,this)
                if (etName.text.isEmpty()) {
                    showShort("请输入需要添加的居民姓名")
                     return@setOnEditorActionListener true
                }else{
                    isLoad = true
                    mPresenter.getResidentInfoSearchListData(uid, token, etName.text.toString())
                    return@setOnEditorActionListener true
                }
            }
            return@setOnEditorActionListener false
        }
    }
    private fun setRefresh() {
        smartLayout.setOnRefreshListener {
            isRefresh = true
            start()
            it.finishRefresh(1000)
        }
    }
    private fun initRecyclerView() {
        mData = ArrayList()
        mAdapter = ResidentInfoSearchAdapter(R.layout.resident_info_search_item, mData)
        recyclerView.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = mAdapter
        }
        mAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener{
                _,_,position ->
            //跳转到添加基本信息
            val intent = Intent(this, EditBasicInfoActivity::class.java)
            intent.putExtra(Constants.HOUSE_ID,houseId)
            intent.putExtra(Constants.ID,mAdapter.data[position].id)
            intent.putExtra(Constants.IS_ADD,true)
            startActivity(intent)
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    private var isLoad: Boolean = false
    override fun start() {
        if (isLoad) {
            mPresenter.getResidentInfoSearchListData(uid, token, etName.text.toString())
        }
    }
    private var isFirst: Boolean = true
    override fun setResidentInfoSearchListData(residentInfoSearchListData: ArrayList<ResidentInfoSearchListData>) {
        mData = residentInfoSearchListData
        isLoad = false
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
                    mAdapter.replaceData(mData)
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
            ApiErrorCode.NETWORK_ERROR -> mLayoutStatusView?.showNoNetwork()
            else -> mLayoutStatusView?.showError()
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