package com.haidie.gridmember.ui.home.view

import android.content.Context
import android.graphics.Color
import com.yanyusong.y_divideritemdecoration.Y_Divider
import com.yanyusong.y_divideritemdecoration.Y_DividerBuilder
import com.yanyusong.y_divideritemdecoration.Y_DividerItemDecoration


/**
 * Created by admin2
 *  on 2018/08/11  16:09
 * description
 */
class HouseDetailRecyclerViewDividerItemDecoration(context: Context?) : Y_DividerItemDecoration(context) {
    override fun getDivider(itemPosition: Int): Y_Divider {
        return Y_DividerBuilder()
                .setBottomSideLine(true, Color.parseColor("#E4E4E4"), 10f, 0f, 0f)
                .create()
    }
}