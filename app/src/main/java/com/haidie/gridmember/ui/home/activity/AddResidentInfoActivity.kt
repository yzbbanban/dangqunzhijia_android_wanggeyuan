package com.haidie.gridmember.ui.home.activity

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.ui.home.adapter.AddResidentInfoAdapter
import com.haidie.gridmember.ui.home.view.AddResidentInfoDividerItemDecoration
import com.haidie.gridmember.utils.LogHelper
import com.haidie.gridmember.view.RuntimeRationale
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.activity_add_resident_info.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/12/13 17:29
 * description  添加居民信息
 */
class AddResidentInfoActivity : BaseActivity() {
    private var mId: Int? = null
    private var houseId: Int? = null
    private  var spaceId:Int? = null
    private var title: String? = null
    private val texts = arrayOf("基本信息", "学习工作信息", "党员信息",
        "关爱信息","家庭状况", "其他信息","矛盾纠纷", "基本诉求")
    private var dataList = mutableListOf<Map<String, Any>>()
    override fun getLayoutId(): Int = R.layout.activity_add_resident_info

    override fun initData() {
        mId = intent.getIntExtra(Constants.ID,-1)
        spaceId = intent.getIntExtra(Constants.SPACE_ID,-1)
        houseId = intent.getIntExtra(Constants.HOUSE_ID,-1)
        title = intent.getStringExtra(Constants.TEXT)
        dataList.clear()
        for (i in 0 until texts.size) {
            val map = HashMap<String,Any>()
            map[Constants.TEXT] = texts[i]
            dataList.add(map)
        }
    }

    override fun initView() {
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener{ onBackPressed() }
        tvTitle.text = title

        val adapter = AddResidentInfoAdapter(R.layout.add_resident_info_item, dataList)
        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener{
            _,_,position ->
            val title = adapter.data[position][Constants.TEXT] as String
            when (title) {
//                基本信息
                texts[0] -> {
                    AndPermission.with(this)
                        .runtime()
                        .permission(Permission.WRITE_EXTERNAL_STORAGE,Permission.CAMERA)
                        .rationale(RuntimeRationale())
                        .onGranted {
                            val intent = Intent(this, EditBasicInfoActivity::class.java)
                            intent.putExtra(Constants.SPACE_ID,spaceId)
                            intent.putExtra(Constants.HOUSE_ID,houseId)
                            intent.putExtra(Constants.ID,mId)
                            intent.putExtra(Constants.TYPE,position)
                            startActivity(intent)
                            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
                        }
                        .onDenied { permissions ->  showSettingDialog(this, permissions)  }
                        .start()

                }
                //学习工作信息
                texts[1] -> {
                    val intent = Intent(this, EducationalWorkInfoActivity::class.java)
                    intent.putExtra(Constants.ID,mId)
                    intent.putExtra(Constants.TYPE,position)
                    startActivity(intent)
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
                }
                //党员信息
                texts[2] -> {
                    val intent = Intent(this, PartyMemberInfoActivity::class.java)
                    intent.putExtra(Constants.ID,mId)
                    intent.putExtra(Constants.TYPE,position)
                    startActivity(intent)
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
                }
                //关爱信息
                texts[3] -> {
                    val intent = Intent(this, CaringInfoActivity::class.java)
                    intent.putExtra(Constants.ID,mId)
                    intent.putExtra(Constants.TYPE,position)
                    startActivity(intent)
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
                }
                //家庭状况
                texts[4] -> {
                    val intent = Intent(this, FamilyStatusActivity::class.java)
                    intent.putExtra(Constants.ID,mId)
                    intent.putExtra(Constants.TYPE,position)
                    startActivity(intent)
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
                }
                //其他信息
                texts[5] -> {
                    val intent = Intent(this, VehicleInformationListActivity::class.java)
                    intent.putExtra(Constants.ID,mId)
                    startActivity(intent)
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
                }
                texts[6],//矛盾纠纷
                texts[7] -> {//基本诉求
                    var type = -1
                    when (title) {
                        texts[6] -> type = 2
                        texts[7] -> type = 4
                    }
                    AndPermission.with(this)
                        .runtime()
                        .permission(
                            Permission.ACCESS_FINE_LOCATION,
                            Permission.WRITE_EXTERNAL_STORAGE,Permission.READ_EXTERNAL_STORAGE,Permission.CAMERA)
                        .rationale(RuntimeRationale())
                        //跳转到问题上报页面
                        .onGranted {
                            val intent = Intent(this, ContradictoryDisputeOrBasicAppealActivity::class.java)
                            intent.putExtra(Constants.TYPE,type)
                            startActivity(intent)
                            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
                        }
                        .onDenied { permissions ->  showSettingDialog(this, permissions)  }
                        .start()
                }
            }
        }
        recyclerView.let {
            it.setHasFixedSize(true)
            it.layoutManager = GridLayoutManager(this,3) as RecyclerView.LayoutManager?
            it.addItemDecoration(AddResidentInfoDividerItemDecoration(this))
            it.adapter = adapter
        }
    }

    override fun start() {
    }
}