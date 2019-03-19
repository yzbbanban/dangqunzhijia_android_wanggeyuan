package com.haidie.gridmember.ui.mine.activity

import android.content.Intent
import android.view.View
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.api.UrlConstant
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.bean.ModifyAvatarData
import com.haidie.gridmember.mvp.contract.mine.ModifyAvatarContract
import com.haidie.gridmember.mvp.event.ReloadMineEvent
import com.haidie.gridmember.mvp.presenter.mine.ModifyAvatarPresenter
import com.haidie.gridmember.rx.RxBus
import com.haidie.gridmember.utils.CommonUtils
import com.haidie.gridmember.utils.ImageLoader
import com.haidie.gridmember.utils.Preference
import com.haidie.gridmember.view.RuntimeRationale
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.activity_modify_avatar.*
import kotlinx.android.synthetic.main.common_toolbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import top.wefor.circularanim.CircularAnim
import java.io.File

/**
 * Create by   Administrator
 *      on     2019/01/04 10:06
 * description  修改头像
 */
class ModifyAvatarActivity : BaseActivity(),ModifyAvatarContract.View {

    private var mAvatar: String? = null
    private var themeId: Int = 0
    private val maxSelectNum = 1
    private var selectList = mutableListOf<LocalMedia>()
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private val mPresenter by lazy { ModifyAvatarPresenter() }
    private var avatar by Preference(Constants.AVATAR, Constants.EMPTY_STRING)
    override fun getLayoutId(): Int = R.layout.activity_modify_avatar

    override fun initData() {
        mAvatar = intent.getStringExtra(Constants.AVATAR)
    }

    override fun initView() {
        mPresenter.attachView(this)
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener{ onBackPressed() }
        tvTitle.text = "修改头像"
        ImageLoader.load(this,mAvatar!!,ivPic)
        themeId = R.style.picture_default_style
        ivAddPic.setOnClickListener { _ ->
            AndPermission.with(this)
                .runtime()
                .permission(Permission.WRITE_EXTERNAL_STORAGE, Permission.CAMERA)
                .rationale(RuntimeRationale())
                .onGranted {
                    //弹出相册选择
                    showPictureChoose()
                }
                .onDenied { permissions -> showSettingDialog(this, permissions) }
                .start()
        }
        ivDelete.setOnClickListener {
            ivAddPic.visibility = View.VISIBLE
            ivPic.visibility = View.GONE
            ivDelete.visibility = View.GONE
            selectList.clear()
        }
        ivPic.setOnClickListener {
            if (selectList.isNotEmpty()) {
                PictureSelector.create(this@ModifyAvatarActivity)
                    .themeStyle(R.style.picture_default_style)
                    .openExternalPreview(0, selectList)
            } else {
                val localMedia = ArrayList<LocalMedia>()
                val localMedia1 = LocalMedia()
                localMedia1.path = UrlConstant.BASE_URL_HOST + mAvatar
                localMedia.add(localMedia1)
                PictureSelector.create(this@ModifyAvatarActivity)
                    .themeStyle(R.style.picture_default_style)
                    .openExternalPreview(0, localMedia)
            }
        }
        tvSubmitModify.setOnClickListener {
            CircularAnim.hide(tvSubmitModify)
                .endRadius((progressBar.height / 2).toFloat())
                .go { progressBar.visibility = View.VISIBLE }
            if (selectList.isEmpty()) {
                showShort("请上传头像")
                showSubmit()
                return@setOnClickListener
            }
            val filePath = Constants.PATH_PIC + Constants.PIC_PNG
            val file = File(filePath)
            CommonUtils.copyFile(selectList[0].compressPath,filePath)
            val requestFile = RequestBody.create(MediaType.parse(Constants.MULTIPART_FORM_DATA), file)
            val part = MultipartBody.Part.createFormData(Constants.AVATAR, file.name, requestFile)
            mPresenter.getModifyAvatarData(toRequestBody(uid.toString()),toRequestBody(token),part)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    private fun showSubmit() {
        CircularAnim.show(tvSubmitModify).go()
        progressBar.visibility = View.GONE
    }
    private fun showPictureChoose() {
        PictureSelector.create(this@ModifyAvatarActivity)
            .openGallery(PictureMimeType.ofImage())
            .theme(themeId)
            .maxSelectNum(maxSelectNum)
            .minSelectNum(1)
            .imageSpanCount(4)
            .selectionMode(PictureConfig.MULTIPLE)
            .previewImage(true)
            .isCamera(true)
            .isZoomAnim(true)
            .imageFormat(PictureMimeType.PNG)
            .compress(true)
            .synOrAsy(true)
            .glideOverride(160, 160)
            .selectionMedia(selectList)
            .minimumCompressSize(100)
            .forResult(PictureConfig.CHOOSE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    selectList = PictureSelector.obtainMultipleResult(data)
                    val localMedia = selectList[0]
                    ivPic.visibility = View.VISIBLE
                    ivDelete.visibility = View.VISIBLE
                    ivAddPic.visibility = View.GONE
                    ImageLoader.load(this, File(localMedia.compressPath),ivPic)
                }
            }
        }
    }
    override fun start() {}
    override fun setModifyAvatarData(modifyAvatarData: ModifyAvatarData) {
        showShort("修改成功")
        avatar = modifyAvatarData.avatar
        RxBus.getDefault().post(ReloadMineEvent())
        finish()
    }
    override fun showError(msg: String, errorCode: Int) {
        showShort(if (msg.isEmpty()) "修改失败" else msg)
        showSubmit()
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
}