package com.haidie.gridmember.ui.home.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.api.UrlConstant
import com.haidie.gridmember.base.BaseFragment
import com.haidie.gridmember.mvp.bean.HomeBannerData
import com.haidie.gridmember.mvp.bean.MainData
import com.haidie.gridmember.mvp.contract.home.HomeContract
import com.haidie.gridmember.mvp.presenter.home.HomePresenter
import com.haidie.gridmember.ui.home.activity.*
import com.haidie.gridmember.ui.home.adapter.HomeAdapter
import com.haidie.gridmember.ui.home.adapter.HomeRecyclerViewHeaderAdapter
import com.haidie.gridmember.utils.DateUtils
import com.haidie.gridmember.utils.GlideImageLoader
import com.haidie.gridmember.utils.Preference
import com.haidie.gridmember.utils.StatusBarUtil
import com.haidie.gridmember.view.RuntimeRationale
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home_banner_view.view.*

/**
 * Create by   Administrator
 *      on     2018/12/05 09:52
 * description
 */
class HomeFragment : BaseFragment(), HomeContract.View {

    private var mBanner: Banner? = null
    private var adapter: HomeAdapter? = null
    private var mRecyclerViewHeader: RecyclerView? = null
    private val mHeaderView by lazy { getHeaderView() }
    private var mData = mutableListOf<MainData>()
    private var dataList = mutableListOf<Map<String, Any>>()
    private var mTitle: String? = null
    private var isRefresh = false
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private val mPresenter by lazy { HomePresenter() }
    private val icons = arrayOf(
        R.drawable.property_problem, R.drawable.family_visit, R.drawable.family_visit, R.drawable.public_safety,
        R.drawable.family_visit, R.drawable.family_visit, R.drawable.personnel_supervision, R.drawable.public_safety
    )
    private val texts = arrayOf(
        "物业问题", "家庭走访", "矛盾纠纷", "公共安全",
        "通讯录", "流动人口", "监管人员", "关爱对象"
    )
//    private val titles = arrayOf(
//        "${DateUtils.getNowTimeMonth()}月待办事项", "${DateUtils.getNowTimeMonth()}月上报管理",
//        "${DateUtils.getNowTimeMonth()}月工作统计", "消息通知",
//        "打卡签到"
//    )

    private val titles = arrayOf(
        "工作任务",
        "${DateUtils.getNowTimeMonth()}月上报管理"
    )


    companion object {
        fun getInstance(title: String): HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initView() {
        mPresenter.attachView(this)
        //状态栏透明
        StatusBarUtil.immersive(activity.window)
        adapter = HomeAdapter(R.layout.home_item, mData)
        recyclerView?.let {
            it.setHasFixedSize(true)
            it.layoutManager = GridLayoutManager(activity, 1)
            it.adapter = adapter
        }
        adapter?.let {
            val headerLayout = it.headerLayout
            if (headerLayout == null) {
                it.addHeaderView(mHeaderView)
            }
        }

        adapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
            val title = adapter!!.data[position].title
            when (title) {
//                12月待办事项
//                titles[0] -> {
//                    toActivity(ToDoListActivity::class.java)
//                }
                //工作任务
                titles[0] -> {
                    toActivity(WorkTaskInfoActivity::class.java)
                }
                //12月上报管理
                titles[1] -> {
                    toActivity(ReportManagementActivity::class.java)
                }
//                12月工作统计
//                titles[2] -> {
//                    showShort("敬请期待")
////                    val intent = Intent(activity, WorkRecordActivity::class.java)
////                    intent.putExtra(Constants.TEXT,title)
////                    startActivity(intent)
////                    activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
//                }
//                //消息通知
//                titles[3] -> {
//                    toActivity(MessageNotificationActivity::class.java)
//                }
//                //打卡签到
//                titles[4] -> {
//                    showShort("敬请期待")
//                }

            }

        }
        val arrayList = ArrayList<Any>()
        arrayList.add(R.drawable.banner_1)
        arrayList.add(R.drawable.main_bg)
        mBanner?.setImages(arrayList)
        initBanner()
        val headerAdapter = HomeRecyclerViewHeaderAdapter(R.layout.home_recycler_view_header_item, dataList)
        mRecyclerViewHeader?.let {
            it.setHasFixedSize(true)
            it.layoutManager = GridLayoutManager(activity, 4)
            it.adapter = headerAdapter
        }
        smartLayout.apply {
            //内容跟随偏移
            setEnableHeaderTranslationContent(true)
            setOnRefreshListener {
                isRefresh = true
                lazyLoad()
                it.finishRefresh(1000)
            }
        }

        //"物业问题", "家庭走访", "矛盾纠纷", "公共安全",
        //"通讯录", "流动人口", "监管人员", "关爱对象"

        //"物业问题", "家庭走访","人员监管","公共安全"
        headerAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
            val title = headerAdapter.data[position][Constants.TEXT] as String
            when (title) {
                texts[0],
                texts[2],
                texts[3] -> {
                    var type = -1
                    when (title) {
                        texts[0] -> type = 1
//                        texts[2] -> type = 2
                        texts[3] -> type = 3
                    }
                    AndPermission.with(this)
                        .runtime()
                        .permission(
                            Permission.ACCESS_FINE_LOCATION,
                            Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE, Permission.CAMERA
                        )
                        .rationale(RuntimeRationale())
                        //跳转到问题上报页面
                        .onGranted {
                            val intent = Intent(activity, ProblemReportingActivity::class.java)
                            intent.putExtra(Constants.TYPE, type)
                            startActivity(intent)
                            activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
                        }
                        .onDenied { permissions -> showSettingDialog(activity, permissions) }
                        .start()
                }
//                走访
                texts[1] -> toActivity(ResidentManagementActivity::class.java)
                //通讯录
                texts[4] -> toActivity(AddressBookActivity::class.java)
                //流动人口
                texts[5] -> toActivity(FlowPeopleInfoActivity::class.java)
//                人员监管
                texts[6] -> toActivity(PersonnelSupervisionActivity::class.java)
//                关爱对象
                texts[7] -> toActivity(CaringObjectActivity::class.java)


            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    private fun showSettingDialog(context: Context, permissions: List<String>) {
        val permissionNames = Permission.transformText(context, permissions)
        val message =
            context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames))
        AlertDialog.Builder(context)
            .setCancelable(false)
            .setTitle(R.string.title_dialog)
            .setMessage(message)
            .setPositiveButton(R.string.setting) { _, _ -> setPermission() }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .show()
    }

    private fun setPermission() {
        AndPermission.with(this)
            .runtime()
            .setting()
            .start()
    }

    override fun lazyLoad() {
        dataList.clear()
        for (i in 0 until icons.size) {
            val map = HashMap<String, Any>()
            map[Constants.ICON] = icons[i]
            map[Constants.TEXT] = texts[i]
            dataList.add(map)
        }
        mData.clear()
        mData.add(MainData(titles[0], null, R.color.bg_orange))
        mData.add(MainData(titles[1], null, R.color.bg_orange))
//        mData.add(MainData(titles[2], null, R.color.bg_orange))
//        mData.add(MainData(titles[3], null, R.color.bg_orange))
//        mData.add(MainData(titles[4], null, R.color.bg_orange))
        adapter?.replaceData(mData)
        mPresenter.getHomeBannerData(uid, token)
    }

    private fun initBanner() {
        mBanner?.apply {
            //设置banner样式
            setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
            //设置图片加载器
            setImageLoader(GlideImageLoader())
            //设置banner动画效果
            setBannerAnimation(Transformer.DepthPage)
            //设置指示器位置（当banner模式中有指示器时）
            setIndicatorGravity(BannerConfig.CENTER)
            //banner设置方法全部调用完毕时最后调用
            start()
        }
    }

    override fun setHomeBannerData(homeBannerData: HomeBannerData) {
        val list = homeBannerData.list
        val arrayList = arrayListOf<String>()
        list.forEach {
            arrayList.add(UrlConstant.BASE_URL_HOST + it.cover_one)
        }
        mBanner?.apply {
            //设置图片集合
            setImages(arrayList)
            initBanner()
            setOnBannerListener { position ->
                // 跳转到详情页面
//                /index/toutiao/detail/admin_id/${admin_id}/article_id/${val.id}/appid/2?w=0
//            index/toutiao/detail/user_id/${user_id}/article_id/${val.id}?w=1      后台发布设置w为1
                val url = "index/toutiao/detail/admin_id/$uid/article_id/${list[position].id}/appid/2?w=1"
                WebViewDetailActivity.startActivity(activity, url)
            }
        }
    }

    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
    }

    override fun showLoading() {
        if (!isRefresh) {
            isRefresh = false
            mLayoutStatusView?.showLoading()
        }
    }

    override fun dismissLoading() {
        mLayoutStatusView?.showContent()
        smartLayout.finishRefresh()
    }

    override fun onStart() {
        super.onStart()
        mBanner?.startAutoPlay()
    }

    override fun onStop() {
        super.onStop()
        mBanner?.stopAutoPlay()
    }

    private fun getHeaderView(): View {
        val view = layoutInflater.inflate(R.layout.home_banner_view, recyclerView.parent as ViewGroup, false)
        mBanner = view.banner
        mRecyclerViewHeader = view.recyclerViewHeader
        return view
    }

    override fun toProblemReporting(type: String) {
        val intent = Intent(activity, ToDoListActivity::class.java)
        intent.putExtra(Constants.TYPE, type)
        startActivity(intent)
        activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
    }
}