package com.haidie.gridmember.api

import com.haidie.gridmember.mvp.bean.*
import com.haidie.gridmember.net.BaseResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * Create by   Administrator
 *      on     2018/11/30 15:49
 * description
 */
interface ApiService {
    /**
     * 网格员登录
     * http://xx.com/api/admin/login
     */
    @FormUrlEncoded
    @POST("api/admin/login")
    fun getLoginData(
        @Field("username") username: String, @Field("password") password: String,
        @Field("app_type") app_type: String, @Field("device_id") device_id: String,
        @Field("device_type") device_type: String
    ): Observable<BaseResponse<GridMemberData>>

    /**
     * 网格员登出
     * http://xx.com/api/admin/logout
     */
    @FormUrlEncoded
    @POST("api/admin/logout")
    fun getLogoutData(@Field("admin_id") admin_id: Int): Observable<BaseResponse<String>>

    /**
     * 户主绑定缴费账户页面
     * http://www.xxx.com/api/Receive/toPaymentAccount
     */
    @FormUrlEncoded
    @POST("api/Receive/toPaymentAccount")
    fun getBuildingUnitData(@Field("uid") uid: Int, @Field("token") token: String): Observable<BaseResponse<BuildingUnitData>>

    /**
     * 单元页面-获取数据接口
     * http://xxx.com/api/grid/house/getHouseList
     */
    @FormUrlEncoded
    @POST("api/grid/house/getHouseList")
    fun getHouseList(
        @Field("admin_id") admin_id: Int, @Field("token") token: String, @Field("page") page: Int,
        @Field("size") size: Int, @Field("space") space: String, @Field("unit") unit: String
    ): Observable<BaseResponse<HouseList>>

    /**
     * 获取房间详情接口
     * http://xxx.com/api/grid/house/getHouseDetail
     */
    @FormUrlEncoded
    @POST("api/grid/house/getHouseDetail")
    fun getHouseDetailData(
        @Field("admin_id") admin_id: Int, @Field("token") token: String, @Field("id") id: Int
    ): Observable<BaseResponse<HouseDetailData>>

    /**
     * 房间详情-变更房屋状态接口
     * http://xxx.com/api/grid/house/editHouseStatus
     */
    @FormUrlEncoded
    @POST("api/grid/house/editHouseStatus")
    fun getEditHouseStatusData(
        @Field("admin_id") admin_id: Int, @Field("token") token: String, @Field("id") id: Int, @Field("type") type: Int
    ): Observable<BaseResponse<Boolean>>

    /**
     * 获取楼栋列表
     * {{local}}fix_order/get_block_info
     * {{local}}grid/house/get_unit_list
     */
    @FormUrlEncoded
    @POST("api/grid/house/get_block_info")
    fun getBlockInfoData(@Field("admin_id") admin_id: Int, @Field("token") token: String): Observable<BaseResponse<ArrayList<BlockInfoData>>>

    /**
     * 获取单元的列表
     * {{local}}fix_order/get_unit_list
     */
    @FormUrlEncoded
    @POST("api/grid/house/get_unit_list")
    fun getUnitListData(
        @Field("admin_id") admin_id: Int, @Field("token") token: String,
        @Field("title") title: String
    ): Observable<BaseResponse<ArrayList<UnitListData>>>

    /**
     * 获取房子的列表
     * {{local}}fix_order/get_house_list
     */
    @FormUrlEncoded
    @POST("api/fix_order/get_house_list")
    fun getHouseListData(
        @Field("uid") uid: Int, @Field("token") token: String, @Field("title") title: String,
        @Field("unit") unit: String
    ): Observable<BaseResponse<ArrayList<HouseListData>>>

    /**
     * 房间详情-签到打卡接口
     * http://xxx.com/api/grid/house/visitSignIn
     */
    @Multipart
    @POST("api/grid/house/visitSignIn")
    fun getHouseDetailCheckInPunchData(
        @Part("admin_id") admin_id: RequestBody, @Part("token") token: RequestBody,
        @Part("id") id: RequestBody, @Part("childen_id") childen_id: RequestBody?, @Part("type") type: RequestBody,
        @Part("content") content: RequestBody, @Part("address") address: RequestBody,
        @Part("help_time") help_time: RequestBody, @Part("house_type") house_type: RequestBody?, @Part parts: MultipartBody.Part
    ): Observable<BaseResponse<Boolean>>

    /**
     * 人员信息采集-获取数据字典口
     * http://xxx.com/api/grid/newcitizen/getDic
     */
    @FormUrlEncoded
    @POST("api/grid/newcitizen/getDic")
    fun getNationalityListData(
        @Field("admin_id") admin_id: Int, @Field("token") token: String, @Field("key") key: String
    ): Observable<BaseResponse<ArrayList<NationalityListData>>>

    /**
     * 人员信息采集-获取数据字典子分类接口
     * http://xxx.com/api/grid/newcitizen/getSubject
     */
    @FormUrlEncoded
    @POST("api/grid/newcitizen/getSubject")
    fun getWomanDiseaseCategoryListData(
        @Field("admin_id") admin_id: Int, @Field("token") token: String,
        @Field("key") key: String, @Field("value") value: String
    ): Observable<BaseResponse<ArrayList<NationalityListData>>>

    /**
     * 人员信息采集-获取省市区三级联动接口
     * http://xxx.com/api/grid/newcitizen/getArea
     */
    @FormUrlEncoded
    @POST("api/grid/newcitizen/getArea")
    fun getProvinceListData(
        @Field("admin_id") admin_id: Int, @Field("token") token: String,
        @Field("province") province: String?, @Field("city") city: String?
    ): Observable<BaseResponse<ArrayList<ProvinceListData>>>

    /**
     * 人员信息采集-基本信息提交接口
     * http://xxx.com/api/grid/newcitizen/add
     */
    @Multipart
    @POST("api/grid/newcitizen/add")
    fun getBasicInfoSubmitData(
        @Part("admin_id") admin_id: RequestBody, @Part("token") token: RequestBody,
        @Part("name") name: RequestBody, @Part("gender") gender: RequestBody?,
        @Part("identity") identity: RequestBody?, @Part("birthday") birthday: RequestBody?,
        @Part("phone") phone: RequestBody?, @Part("nationality") nationality: RequestBody?,
        @Part("house_relation") house_relation: RequestBody?, @Part("register_address_province") register_address_province: RequestBody?,
        @Part("register_address_city") register_address_city: RequestBody?, @Part("register_address_area") register_address_area: RequestBody?,
        @Part("register_address_street") register_address_street: RequestBody?, @Part("live_address_province") live_address_province: RequestBody?,
        @Part("live_address_city") live_address_city: RequestBody?, @Part("live_address_area") live_address_area: RequestBody?,
        @Part("live_address_street") live_address_street: RequestBody?, @Part("live_address_type") live_address_type: RequestBody?,
        @Part("marriage_status") marriage_status: RequestBody?,
        @Part("religion") religion: RequestBody?,
        @Part("help_info") help_info: RequestBody?, @Part("house_id") house_id: RequestBody?,
        @Part("space_id") space_id: RequestBody?,
        @Part parts: MultipartBody.Part?
    ): Observable<BaseResponse<Boolean>>

    /**
     * 人员信息采集-获取个人基本信息接口
     * http://xxx.com/api/grid/newcitizen/detail
     */
    @FormUrlEncoded
    @POST("api/grid/newcitizen/detail")
    fun getPersonalInfoData(
        @Field("admin_id") admin_id: Int, @Field("token") token: String, @Field("id") id: Int,
        @Field("type") type: Int?
    ): Observable<BaseResponse<PersonalInfoData>>

    /**
     * 人员信息采集-修改个人基本信息接口
     * http://xxx.com/api/grid/newcitizen/edit
     */
    @FormUrlEncoded
    @POST("api/grid/newcitizen/edit")
    fun getEditPersonalInfoData(
        @Field("admin_id") admin_id: Int, @Field("token") token: String, @Field("id") id: Int,

        @Field("politics_status") politics_status: Int?, @Field("entry_party_time") entry_party_time: String?,
        @Field("org_relation_address_province") org_relation_address_province: Int?,
        @Field("org_relation_address_city") org_relation_address_city: Int?,
        @Field("org_relation_address_area") org_relation_address_area: Int?,
        @Field("org_relation_address") org_relation_address: String?,
        @Field("commend_status") commend_status: String?,

        @Field("education") education: Int?, @Field("company") company: String?,
        @Field("job") job: String?, @Field("special_skills") special_skills: Int?
    ): Observable<BaseResponse<Boolean>>

    @FormUrlEncoded
    @POST("api/grid/newcitizen/edit")
    fun getEditFamilyPersonalInfoData(
        @Field("admin_id") admin_id: Int, @Field("token") token: String, @Field("id") id: Int,

        @Field("family_income_source") family_income_source: Int?,
        @Field("family_income") family_income: String?,
        @Field("family_wage_income") family_wage_income: String?,
        @Field("family_operational_income") family_operational_income: String?,
        @Field("family_low_income") family_low_income: String?,
        @Field("family_pension_income") family_pension_income: String?,
        @Field("family_area_num") family_area_num: String?,
        @Field("family_person_num") family_person_num: String?,
        @Field("family_area_time") family_area_time: String?,
        @Field("family_area_flow_num") family_area_flow_num: String?,
        @Field("is_enter_baseic_old_insurance") is_enter_baseic_old_insurance: Int?,
        @Field("is_enter_baseic_medical_insurance") is_enter_baseic_medical_insurance: Int?,
        @Field("is_enter_new_insurance") is_enter_new_insurance: Int?
    ): Observable<BaseResponse<Boolean>>

    @Multipart
    @POST("api/grid/newcitizen/edit")
    fun getEditBasicInfoSubmitData(
        @Part("admin_id") admin_id: RequestBody, @Part("token") token: RequestBody, @Part("id") id: RequestBody,

        @Part("name") name: RequestBody?,
        @Part("gender") gender: RequestBody?, @Part("identity") identity: RequestBody?,
        @Part("birthday") birthday: RequestBody?, @Part("phone") phone: RequestBody?,
        @Part("nationality") nationality: RequestBody?, @Part("house_relation") house_relation: RequestBody?,
        @Part("live_address_type") live_address_type: RequestBody?,
        @Part("register_address_province") register_address_province: RequestBody?,
        @Part("register_address_city") register_address_city: RequestBody?,
        @Part("register_address_area") register_address_area: RequestBody?,
        @Part("live_address_province") live_address_province: RequestBody?,
        @Part("live_address_city") live_address_city: RequestBody?,
        @Part("live_address_area") live_address_area: RequestBody?,
        @Part("house_id") house_id: RequestBody?,
        @Part("space_id") space_id: RequestBody?,
        @Part("marriage_status") marriage_status: RequestBody?,
        @Part("religion") religion: RequestBody?,
        @Part("help_info") help_info: RequestBody?, @Part parts: MultipartBody.Part?
    ): Observable<BaseResponse<Boolean>>

    @FormUrlEncoded
    @POST("api/grid/newcitizen/edit")
    fun getEditCaringPersonalInfoData(
        @Field("admin_id") admin_id: Int, @Field("token") token: String, @Field("id") id: Int,

        @Field("is_children") is_children: Int?,
        @Field("school") school: String?,
        @Field("grade") grade: String?,
        @Field("class") clazz: String?,
        @Field("student_special_skills") student_special_skills: String?,
        @Field("student_hobby") student_hobby: String?,
        @Field("student_character") student_character: String?,
        @Field("student_score") student_score: Int?,
        @Field("student_good_subject") student_good_subject: Int?,
        @Field("student_bad_subject") student_bad_subject: Int?,
        @Field("is_single_parent") is_single_parent: Int?,
        @Field("is_allergy") is_allergy: Int?,
        @Field("family_information") family_information: String?,

        @Field("is_problem_youth_type") is_problem_youth_type: Int?,

        @Field("is_disease_patients_type") is_disease_patients_type: Int?,

        @Field("is_voman_disease_type") is_voman_disease_type: Int?,
        @Field("is_voman_disease_category") is_voman_disease_category: Int?,

        @Field("is_difficult_masses_type") is_difficult_masses_type: Int?,
        @Field("is_difficult_masses_reason") is_difficult_masses_reason: Int?,

        @Field("is_disabled_person_type") is_disabled_person_type: Int?,
        @Field("is_disabled_person_rank") is_disabled_person_rank: Int?,

        @Field("is_mental_retardation_rank") is_mental_retardation_rank: Int?,

        @Field("old_teacher_old_party") old_teacher_old_party: Int?

    ): Observable<BaseResponse<Boolean>>

    /**
     * 通讯录接口
     * http://xx.com/api/grid/system/get_address_book_list
     */
    @FormUrlEncoded
    @POST("api/grid/system/get_address_book_list")
    fun getAddressBookData(@Field("admin_id") admin_id: Int, @Field("token") token: String): Observable<BaseResponse<ArrayList<AddressBookData>>>

    /**
     * 更新信鸽推送token
     * http://xx.com/api/grid/system/update_device_token
     */
    @FormUrlEncoded
    @POST("api/grid/system/update_device_token")
    fun getXGData(
        @Field("admin_id") admin_id: Int, @Field("device_token") device_token: String,
        @Field("token") token: String
    ): Observable<BaseResponse<String>>

    /**
     * 人员监管-获取人员列表接口
     * http://xxx.com/api/grid/newcitizen/getSuperviseList
     */
    @FormUrlEncoded
    @POST("api/grid/newcitizen/getSuperviseList")
    fun getPersonnelSupervisionListData(
        @Field("admin_id") admin_id: Int, @Field("token") token: String, @Field("page") page: Int,
        @Field("size") size: Int, @Field("help_info") help_info: String
    ): Observable<BaseResponse<PersonnelSupervisionListData>>

    /**
     * 获取车辆信息列表接口
     * http://xxx.com/api/grid/newcitizen/listVehicle
     */
    @FormUrlEncoded
    @POST("api/grid/newcitizen/listVehicle")
    fun getVehicleInformationListData(
        @Field("admin_id") admin_id: Int, @Field("token") token: String, @Field("id") id: Int
    ): Observable<BaseResponse<ArrayList<VehicleInformationListData>>>

    /**
     * 车辆信息新增接口
     * http://xxx.com/api/grid/newcitizen/addVehicle
     */
    @FormUrlEncoded
    @POST("api/grid/newcitizen/addVehicle")
    fun getNewVehicleInformationData(
        @Field("admin_id") admin_id: Int, @Field("token") token: String, @Field("cid") cid: Int,
        @Field("vehicle_type") vehicle_type: Int, @Field("lecense_id") lecense_id: String,
        @Field("engine_id") engine_id: String, @Field("motor_id") motor_id: String, @Field("color") color: String
    ): Observable<BaseResponse<Boolean>>

    /**
     * 删除车辆信息接口
     * http://xxx.com/api/grid/newcitizen/delVehicle
     */
    @FormUrlEncoded
    @POST("api/grid/newcitizen/delVehicle")
    fun getDeleteVehicleInformationData(
        @Field("admin_id") admin_id: Int, @Field("token") token: String, @Field("id") id: Int
    ): Observable<BaseResponse<Boolean>>

    /**
     * 获取车辆信息详情接口
     * http://xxx.com/api/grid/newcitizen/detailVehicle
     */
    @FormUrlEncoded
    @POST("api/grid/newcitizen/detailVehicle")
    fun getVehicleInformationDetailData(
        @Field("admin_id") admin_id: Int, @Field("token") token: String, @Field("id") id: Int
    ): Observable<BaseResponse<VehicleInformationDetailData>>

    /**
     * 编辑车辆信息接口
     * http://xxx.com/api/grid/newcitizen/editVehicle
     */
    @FormUrlEncoded
    @POST("api/grid/newcitizen/editVehicle")
    fun getEditVehicleInformationData(
        @Field("admin_id") admin_id: Int, @Field("token") token: String, @Field("id") id: Int,
        @Field("vehicle_type") vehicle_type: Int?, @Field("lecense_id") lecense_id: String?,
        @Field("engine_id") engine_id: String?, @Field("motor_id") motor_id: String?, @Field("color") color: String?
    ): Observable<BaseResponse<Boolean>>

    /**
     * 人员监管人员类型页面接口
     * http://xxx.com/api/grid/newcitizen/getSuperviseCount
     */
    @FormUrlEncoded
    @POST("api/grid/newcitizen/getSuperviseCount")
    fun getSuperviseCountData(@Field("admin_id") admin_id: Int, @Field("token") token: String):
            Observable<BaseResponse<ArrayList<SuperviseCountData>>>

    /**
     * 代办事项
     * http://xx.com/api/grid/grid_problem_report/report_todo
     */
    @FormUrlEncoded
    @POST("api/grid/grid_problem_report/report_todo")
    fun getToDoListData(@Field("admin_id") admin_id: Int, @Field("token") token: String):
            Observable<BaseResponse<ToDoListData>>

    /**
     * 通知列表(点对点)
     * http://xx.com/api/grid/system/get_notice_list
     */
    @FormUrlEncoded
    @POST("api/grid/system/get_notice_list")
    fun getNoticeListData(@Field("admin_id") admin_id: Int, @Field("token") token: String):
            Observable<BaseResponse<ArrayList<NoticeListData>>>

    /**
     * 广播的消息通知
     * http://xx.com/api/grid/system/get_broadcast_notice_list
     */
    @FormUrlEncoded
    @POST("api/grid/system/get_broadcast_notice_list")
    fun getMessageListData(@Field("admin_id") admin_id: Int, @Field("token") token: String):
            Observable<BaseResponse<ArrayList<MessageListData>>>

    /**
     * APP首页轮播文章, 暂时设计只有3页滚动.
     * http://xx.com/api/grid/toutiao_article/slideshow_article
     */
    @FormUrlEncoded
    @POST("api/grid/toutiao_article/slideshow_article")
    fun getHomeBannerData(@Field("admin_id") admin_id: Int, @Field("token") token: String): Observable<BaseResponse<HomeBannerData>>

    /**
     * 修改密码
     * http://xx.com/api/admin/changepwd
     */
    @FormUrlEncoded
    @POST("api/admin/changepwd")
    fun getChangePasswordData(
        @Field("admin_id") admin_id: Int, @Field("token") token: String, @Field("oldpwd") oldpwd: String,
        @Field("newpwd") newpwd: String, @Field("repwd") repwd: String
    ): Observable<BaseResponse<String>>

    /**
     * 版本检查
     * http://xx.com/index/app/version_check
     */
    @FormUrlEncoded
    @POST("index/app/version_check")
    fun getCheckVersionData(@Field("apptype") appType: Int): Observable<BaseResponse<CheckVersionData>>

    /**
     * 居民信息搜索功能接口
     * http://xxx.com/api/grid/newcitizen/getCitizenBy
     */
    @FormUrlEncoded
    @POST("api/grid/newcitizen/getCitizenBy")
    fun getResidentInfoSearchListData(
        @Field("admin_id") admin_id: Int, @Field("token") token: String, @Field("name") name: String
    ): Observable<BaseResponse<ArrayList<ResidentInfoSearchListData>>>

    /**
     * 网格员-修改头像
     * http://xx.com/api/admin/profile
     */
    @Multipart
    @POST("api/admin/profile")
    fun getModifyAvatarData(
        @Part("admin_id") admin_id: RequestBody, @Part("token") token: RequestBody, @Part parts: MultipartBody.Part?
    ): Observable<BaseResponse<ModifyAvatarData>>

    /**
     * 上报管理
     * http://xx.com/api/grid/grid_problem_report/report_management
     */
    @FormUrlEncoded
    @POST("api/grid/grid_problem_report/report_management")
    fun getReportManagementData(
        @Field("admin_id") admin_id: Int, @Field("token") token: String, @Field("year_month") year_month: String
    ): Observable<BaseResponse<ReportManagementData>>


    /**
     * 工单列表
     * http://xx.com/api/grid/newcitizen/getFlowPeopleList
     */
    @FormUrlEncoded
    @POST("api/grid/assign/assignJobList")
    fun getOrderData(
        @Field("admin_id") admin_id: Int, @Field("token") token: String,
        @Field("status") status: String,
        @Field("page") page: String, @Field("size") size: String
    ): Observable<BaseResponse<OrderData>>

    /**
     * 流动人口列表
     * http://xx.com/api/grid/newcitizen/getFlowPeopleList
     */
    @FormUrlEncoded
    @POST("api/grid/newcitizen/getFlowPeopleList")
    fun getFlowPeopleData(
        @Field("admin_id") admin_id: Int, @Field("token") token: String,
        @Field("status") status: String
    ): Observable<BaseResponse<FlowPeopleData>>

    /**
     * 关爱人员统计列表
     * http://xx.com/api/grid/newcitizen/getCareCount
     */
    @FormUrlEncoded
    @POST("api/grid/newcitizen/getCareCount")
    fun getCareCountData(@Field("admin_id") admin_id: Int, @Field("token") token: String): Observable<BaseResponse<ArrayList<CareCountData>>>

    /**
     * 关爱人员统计列表
     * http://xx.com/api/grid/newcitizen/getCareCount
     */
    @FormUrlEncoded
    @POST("api/grid/newcitizen/getCarePeopleList")
    fun getCarePeopleListData(
        @Field("admin_id") admin_id: Int, @Field("token") token: String,
        @Field("status") status: String, @Field("is_children") is_children: String
    ): Observable<BaseResponse<CarePeopleData>>

}