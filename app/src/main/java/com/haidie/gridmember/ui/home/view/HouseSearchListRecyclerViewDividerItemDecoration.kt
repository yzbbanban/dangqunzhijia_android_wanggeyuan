package com.haidie.gridmember.ui.home.view

import android.content.Context
import android.graphics.Color
import com.haidie.gridmember.Constants
import com.yanyusong.y_divideritemdecoration.Y_Divider
import com.yanyusong.y_divideritemdecoration.Y_DividerBuilder
import com.yanyusong.y_divideritemdecoration.Y_DividerItemDecoration


/**
 * Created by admin2
 *  on 2018/08/11  16:09
 * description
 */
class HouseSearchListRecyclerViewDividerItemDecoration(context: Context?) : Y_DividerItemDecoration(context) {
    override fun getDivider(itemPosition: Int): Y_Divider {
        var divider: Y_Divider? = null
        when (itemPosition % 2) {
            0 ->
                divider = Y_DividerBuilder()
                    .setTopSideLine(true, Color.parseColor(Constants.F_5_F_5_F_5), 10f, 0f, 0f)
                    .setLeftSideLine(true, Color.parseColor(Constants.F_5_F_5_F_5), -1f, 0f, 0f)
                    .create()
            1 ->
                divider = Y_DividerBuilder()
                    .setTopSideLine(true, Color.parseColor(Constants.F_5_F_5_F_5), 10f, 0f, 0f)
                    .setRightSideLine(true, Color.parseColor(Constants.F_5_F_5_F_5), -1f, 0f, 0f)
                    .create()
        }
        return divider!!
    }
}