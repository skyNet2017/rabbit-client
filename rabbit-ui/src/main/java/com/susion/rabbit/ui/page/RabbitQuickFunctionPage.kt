package com.susion.rabbit.ui.page

import android.content.Context
import android.widget.ArrayAdapter
import com.susion.rabbit.base.ui.dp2px
import com.susion.rabbit.storage.RabbitStorage
import com.susion.rabbit.base.ui.page.RabbitBasePage
import com.susion.rabbit.base.ui.view.RabbitSwitchButton
import com.susion.rabbit.ui.monitor.R
import com.susion.rabbit.ui.RabbitUi
import kotlinx.android.synthetic.main.rabbit_page_quick_function.view.*

/**
 * susionwang at 2020-01-09
 */
class RabbitQuickFunctionPage(context: Context) : RabbitBasePage(context) {

    override fun getLayoutResId() = R.layout.rabbit_page_quick_function

    init {
        setTitle("快捷功能")
        initQuickClear()
    }

    private fun initQuickClear() {
        val monitors = RabbitUi.mConfig.monitorList.filter { it.getMonitorInfo().showInExternal }
            .map { it.getMonitorInfo().name }
        val stringAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, monitors)
        mRabbitQuickPageSpinnerMonitors.adapter = stringAdapter
        mRabbitQuickPageClearDataBtn.setOnClickListener {
            RabbitStorage.clearDataByMonitorName(monitors[mRabbitQuickPageSpinnerMonitors.selectedItemPosition])
            showToast("清空成功!")
        }

        //自定义添加的一些配置
        RabbitUi.mConfig.customConfigList.forEach {
            val switchBtn = RabbitSwitchButton(context).apply {
                LayoutParams(LayoutParams.MATCH_PARENT, dp2px(60f))
            }
            mRabbitQuickPageRootLl.addView(switchBtn)
            switchBtn.checkedStatusChangeListener =
                object : RabbitSwitchButton.CheckedStatusChangeListener {
                    override fun checkedStatusChange(isChecked: Boolean) {
                        it.statusChangeCallBack?.onChange(isChecked)
                        it.isEnable = isChecked
                    }
                }
            switchBtn.refreshUi(
                it.configName,
                it.isEnable
            )
        }

        mRabbitQuickPageViewConfig.setOnClickListener {
            RabbitUi.openPage(RabbitCurrentConfigListPage::class.java)
        }
    }

}