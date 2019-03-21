package com.haidie.gridmember.ui.home.activity

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.bean.CareCountData
import com.haidie.gridmember.mvp.contract.home.GetCareContract
import com.haidie.gridmember.mvp.presenter.home.GetCarePresenter
import com.haidie.gridmember.ui.home.adapter.GetCareAdapter
import com.haidie.gridmember.ui.home.view.AddResidentInfoDividerItemDecoration
import com.haidie.gridmember.utils.Preference
import kotlinx.android.synthetic.main.activity_personnel_supervision.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/12/17 09:03
 * description  关爱对象
 */
class CaringObjectActivity : BaseActivity(), GetCareContract.View {

    private val mPresenter by lazy { GetCarePresenter() }
    private var adapter: GetCareAdapter? = null
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, -1)
    private val texts = arrayOf(
        "问题青少年", "空巢老人", "疾病患者", "特困群众", "失业人员",
        "残疾人员", "智力障碍人员"
    )

    private var mData = mutableListOf<CareCountData>()
    override fun getLayoutId(): Int = R.layout.activity_personnel_supervision

    override fun initData() {
        mPresenter.attachView(this)
//        mData.add(CareCountData(1, "疾病患者", 1, 10, 10))
        mPresenter.getCareListData(uid, token)

    }


    override fun initView() {
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener { onBackPressed() }
        tvTitle.text = "关爱对象"
        adapter = GetCareAdapter(R.layout.personnel_supervision_item, mData)
        adapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
            val intent = Intent(this, CarePeopleInfoActivity::class.java)
            intent.putExtra(Constants.TEXT, adapter!!.data[position].delivery_id)
            intent.putExtra(Constants.NAME, adapter!!.data[position].name)
            startActivity(intent)
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
        recyclerView.let {
            it.setHasFixedSize(true)
            it.layoutManager = GridLayoutManager(this, 3)
            it.addItemDecoration(AddResidentInfoDividerItemDecoration(this))
            it.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun start() {
    }

    override fun setCareListData(careCountData: ArrayList<CareCountData>) {
        mData.clear()
        mData.addAll(careCountData)
        adapter?.let {
            it.replaceData(mData)
        }

    }

    override fun showError(msg: String, errorCode: Int) {

    }

    override fun showLoading() {

    }

    override fun dismissLoading() {

    }
}