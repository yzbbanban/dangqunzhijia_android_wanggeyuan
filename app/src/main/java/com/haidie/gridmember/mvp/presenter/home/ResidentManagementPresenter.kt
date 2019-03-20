package com.haidie.gridmember.mvp.presenter.home

import android.util.SparseArray
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.BlockInfoData
import com.haidie.gridmember.mvp.bean.UnitListData
import com.haidie.gridmember.mvp.contract.home.ResidentManagementContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/12/11 11:53
 * description
 */
class ResidentManagementPresenter : BasePresenter<ResidentManagementContract.View>(),ResidentManagementContract.Presenter{
    override fun getBlockUnitData(uid: Int, token: String) {
        checkViewAttached()
        mRootView?.showLoading()
        var title = -1
        var id = -1
        var sum = -1
        val map = SparseArray<ArrayList<UnitListData>>()
        val res = ArrayList<MultiItemEntity>()
        var level1Item : BlockInfoData

        val disposable = RetrofitManager.service.getBlockInfoData(uid, token)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .doOnNext {
                mRootView?.setBlockListData(it)
            }
            .concatMap {
                sum = it.size
                Observable.fromIterable(it)
            }
            .map {
                "${it.id}-${it.title}"
            }
            .concatMap {
                val split = it.split("-")
                id = split[0].toInt()
                title = split[1].toInt()
                RetrofitManager.service.getUnitListData(uid, token, split[1])
                    .compose(SchedulerUtils.ioToMain())
            }
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<ArrayList<UnitListData>>("获取数据失败"){
                override fun onNext(t: ArrayList<UnitListData>) {
                    mRootView?.apply {
                        map.put(title,t)
                        level1Item = BlockInfoData(id,title.toString(),null,1)
                        t.forEach {
                            level1Item.addSubItem(it)
                        }
                        res.add(level1Item)
                        if (sum == map.size()) {
                            dismissLoading()
                            setBlockUnitData(map,res)
                        }
                    }
                }
                override fun onFail(e: ApiException) {
                    mRootView?.apply {
                        dismissLoading()
                        showError(e.mMessage,e.errorCode)
                    }
                }
            })
        addSubscription(disposable)
    }
    override fun getBlockListData(uid: Int, token: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.getBlockInfoData(uid, token)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<ArrayList<BlockInfoData>>("获取楼栋列表失败") {
                override fun onNext(t: ArrayList<BlockInfoData>) {
                    mRootView?.apply {
                        dismissLoading()
                        setBlockListData(t)
                    }
                }
                override fun onFail(e: ApiException) {
                    mRootView?.apply {
                        dismissLoading()
                        showError(e.mMessage,e.errorCode)
                    }
                }
            })
        addSubscription(disposable)
    }
    override fun getUnitListData(uid: Int, token: String, title: String) {
        val disposable = RetrofitManager.service.getUnitListData(uid, token, title)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<ArrayList<UnitListData>>("获取单元的列表失败") {
                override fun onNext(t: ArrayList<UnitListData>) {
                    mRootView?.setUnitListData(t)
                }
                override fun onFail(e: ApiException) {
                    mRootView?.showError(e.mMessage, e.errorCode)
                }
            })
        addSubscription(disposable)
    }
}