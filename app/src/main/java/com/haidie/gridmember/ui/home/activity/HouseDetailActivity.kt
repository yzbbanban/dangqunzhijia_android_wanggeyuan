package com.haidie.gridmember.ui.home.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.bean.HouseDetailData
import com.haidie.gridmember.mvp.contract.home.HouseDetailContract
import com.haidie.gridmember.mvp.presenter.home.HouseDetailPresenter
import com.haidie.gridmember.net.exception.ApiErrorCode
import com.haidie.gridmember.ui.home.adapter.HouseDetailAdapter
import com.haidie.gridmember.utils.Preference
import com.haidie.gridmember.view.RuntimeRationale
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.activity_house_detail.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.house_detail_header_view.view.*

/**
 * Create by   Administrator
 *      on     2018/12/13 09:03
 * description  房间详情
 */
class HouseDetailActivity : BaseActivity(),HouseDetailContract.View {

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, -1)
    private var mId: Int? = null
    private  var spaceId:Int? = null
    private var isRefresh = false
    private  var mData : ArrayList<HouseDetailData.ListData.Citizen>?  = arrayListOf()
    private val mPresenter by lazy { HouseDetailPresenter() }
    private lateinit var mAdapter: HouseDetailAdapter
    private var checkInPunch: LinearLayout? = null
    private val mHeaderView by lazy { getHeaderView() }
    override fun getLayoutId(): Int = R.layout.activity_house_detail

    override fun initData() {
        spaceId = intent.getIntExtra(Constants.SPACE_ID,-1)
        mId = intent.getIntExtra(Constants.ID,-1)
    }

    override fun initView() {
        mPresenter.attachView(this)
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener{ onBackPressed() }
//        ivAdd.visibility = View.VISIBLE
        tvSubmit.visibility = View.VISIBLE
        tvSubmit.text = "添加家庭成员"
        setRefresh()
        mLayoutStatusView = multipleStatusView
        mAdapter = HouseDetailAdapter(R.layout.house_detail_item, mData)
        recyclerView.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = mAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    private fun setRefresh() {
        smartLayout.setOnRefreshListener{
            isRefresh = true
            start()
            it.finishRefresh(1000)
        }
    }
    override fun start() {
        mPresenter.getHouseDetailData(uid,token,mId!!)
    }
    @SuppressLint("SetTextI18n")
    override fun setHouseDetailData(houseDetailData: HouseDetailData) {
        val list = houseDetailData.list
        tvSubmit.setOnClickListener {
            // 添加基本信息
            AndPermission.with(this)
                .runtime()
                .permission(Permission.WRITE_EXTERNAL_STORAGE,Permission.CAMERA)
                .rationale(RuntimeRationale())
                .onGranted {
                    //添加基本信息
                    val intent = Intent(this, BasicInfoActivity::class.java)
                    intent.putExtra(Constants.SPACE_ID,spaceId)
                    intent.putExtra(Constants.HOUSE_ID,list.id)
                    startActivity(intent)
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
                }
                .onDenied { permissions ->  showSettingDialog(this, permissions)  }
                .start()
//            val intent = Intent(this, ResidentInfoSearchActivity::class.java)
//            intent.putExtra(Constants.SPACE_ID,spaceId)
//            intent.putExtra(Constants.HOUSE_ID,list.id)
//            startActivity(intent)
//            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
        }
//        if (list.citizen_list == null) {
//            showShort("暂无数据")
//            mLayoutStatusView?.showError()
//            return
//        }
        mData = list.citizen_list
        tvTitle.text = "${list.num}栋${list.unit}单元${list.roomNo}"
        mAdapter.let { it ->
            if (mData != null){
                it.replaceData(mData!!)
            }
            val headerLayout = it.headerLayout
            if (headerLayout == null) {
                it.addHeaderView(mHeaderView)
            }
            checkInPunch?.setOnClickListener { _ ->
                AndPermission.with(this)
                    .runtime()
                    .permission(Permission.ACCESS_FINE_LOCATION)
                    .rationale(RuntimeRationale())
                    .onGranted {
                        // 走访完成
                        val intent = Intent(this, CheckInPunchActivity::class.java)
                        intent.putExtra(Constants.HOUSE_ID,list.id)
                        startActivity(intent)
                        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
                    }
                    .onDenied { permissions -> showSettingDialog(this, permissions) }
                    .start()
            }
        }
        mAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener{
                _,_,position ->
            //跳转到添加居民信息
            val intent = Intent(this, AddResidentInfoActivity::class.java)
            intent.putExtra(Constants.SPACE_ID,spaceId)
            intent.putExtra(Constants.HOUSE_ID,list.id)
            intent.putExtra(Constants.ID,mAdapter.data[position].id)
            intent.putExtra(Constants.TEXT,"${tvTitle.text}${mAdapter.data[position].name}")
            startActivity(intent)
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
        }
    }
    override fun setEditHouseStatusData(isSuccess: Boolean, msg: String) {}
    private fun getHeaderView() : View{
        val view = layoutInflater.inflate(R.layout.house_detail_header_view, recyclerView.parent as ViewGroup, false)
        checkInPunch = view.llCheckInPunch
        return view
    }
    override fun refresh() {
        smartLayout.autoRefresh()
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