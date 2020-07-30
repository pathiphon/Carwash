package com.chococard.carwash.ui.service

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import com.chococard.carwash.R
import com.chococard.carwash.data.models.ServiceImage
import com.chococard.carwash.data.networks.request.DeleteImageServiceRequest
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.payment.PaymentActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.FlagConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.ServiceViewModel
import kotlinx.android.synthetic.main.activity_service.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ServiceActivity : BaseActivity() {

    val viewModel by viewModel<ServiceViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)

        init()
    }

    private fun init() {
        // set toolbar
        setToolbar(toolbar)

        // set widget
        val adt = ServiceAdapter()
        recycler_view.apply {
            layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
            adapter = adt
        }
        adt.onLongClick = { otherImage ->
            otherImage.imageId?.let { deleteOtherImageService(it) }
        }
        adt.onRemoveOtherImage = { otherImage ->
            otherImage.imageId?.let { deleteOtherImageService(it) }
        }

        // set event
        iv_arrow_back.setOnClickListener { onBackPressed() }

        iv_image_front_before.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE_FRONT_BEFORE) }
        iv_image_back_before.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE_BACK_BEFORE) }
        iv_image_left_before.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE_LEFT_BEFORE) }
        iv_image_right_before.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE_RIGHT_BEFORE) }
        iv_image_front_after.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE_FRONT_AFTER) }
        iv_image_back_after.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE_BACK_AFTER) }
        iv_image_left_after.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE_LEFT_AFTER) }
        iv_image_right_after.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE_RIGHT_AFTER) }

        iv_image_front_before.setOnLongClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_FRONT_BEFORE) }
        iv_image_back_before.setOnLongClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_BACK_BEFORE) }
        iv_image_left_before.setOnLongClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_LEFT_BEFORE) }
        iv_image_right_before.setOnLongClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_RIGHT_BEFORE) }
        iv_image_front_after.setOnLongClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_FRONT_AFTER) }
        iv_image_back_after.setOnLongClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_BACK_AFTER) }
        iv_image_left_after.setOnLongClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_LEFT_AFTER) }
        iv_image_right_after.setOnLongClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_RIGHT_AFTER) }

        card_remove_front_before.setOnClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_FRONT_BEFORE) }
        card_remove_back_before.setOnClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_BACK_BEFORE) }
        card_remove_left_before.setOnClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_LEFT_BEFORE) }
        card_remove_right_before.setOnClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_RIGHT_BEFORE) }
        card_remove_front_after.setOnClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_FRONT_AFTER) }
        card_remove_back_after.setOnClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_BACK_AFTER) }
        card_remove_left_after.setOnClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_LEFT_AFTER) }
        card_remove_right_after.setOnClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_RIGHT_AFTER) }

        iv_add_other_image.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE_OTHER_IMAGE) }

        bt_service.setOnClickListener { startActivity<PaymentActivity>() }

        // observe
        viewModel.state.observe { state ->
            // progress bar
            if (state.loading) progress_bar.show() else progress_bar.hide()
            if (state.loadingFrontBefore) progress_bar_front_before.show() else progress_bar_front_before.hide()
            if (state.loadingBackBefore) progress_bar_back_before.show() else progress_bar_back_before.hide()
            if (state.loadingLeftBefore) progress_bar_left_before.show() else progress_bar_left_before.hide()
            if (state.loadingRightBefore) progress_bar_right_before.show() else progress_bar_right_before.hide()
            if (state.loadingFrontAfter) progress_bar_front_after.show() else progress_bar_front_after.hide()
            if (state.loadingBackAfter) progress_bar_back_after.show() else progress_bar_back_after.hide()
            if (state.loadingLeftAfter) progress_bar_left_after.show() else progress_bar_left_after.hide()
            if (state.loadingRightAfter) progress_bar_right_after.show() else progress_bar_right_after.hide()
            if (state.loadingOtherImage) progress_bar_other_image.show() else progress_bar_other_image.hide()

            // service image
            adt.setList(state.serviceImage?.otherImageService)
            state.serviceImage?.let { setImageJobService(it) }

            // other image
            if (state.isValidMaximumOtherImage) {
                card_add_other_image.show()
                tv_maximum_other_image.hide()
            } else {
                card_add_other_image.hide()
                tv_maximum_other_image.show()
            }

            // button service
            if (state.isConfirmService) bt_service.ready() else bt_service.unready()
        }

        viewModel.attachFirstTime.observe {
            viewModel.callFetchImageService()
        }

        viewModel.error.observeError()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option_busy, menu)
        menu?.findItem(R.id.option_report)?.isVisible = false
        return true
    }

    private fun deleteImageService(statusService: Int): Boolean {
        dialogNegative(R.string.delete_image_service, R.string.delete_image_service_questions) {
            viewModel.callDeleteServiceImage(statusService)
        }
        return true
    }

    private fun deleteOtherImageService(imageId: Int) {
        dialogNegative(R.string.delete_image_service, R.string.delete_image_service_questions) {
            val deleteImageService = DeleteImageServiceRequest(imageId)
            viewModel.callDeleteServiceOtherImage(deleteImageService)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_contact_admin -> dialogContactAdmin { startActivityActionDial() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val statusService: Int = when (requestCode) {
                CommonsConstant.REQUEST_CODE_IMAGE_FRONT_BEFORE -> {
                    iv_camera_front_before.hide()
                    FlagConstant.STATUS_SERVICE_FRONT_BEFORE
                }
                CommonsConstant.REQUEST_CODE_IMAGE_BACK_BEFORE -> {
                    iv_camera_back_before.hide()
                    FlagConstant.STATUS_SERVICE_BACK_BEFORE
                }
                CommonsConstant.REQUEST_CODE_IMAGE_LEFT_BEFORE -> {
                    iv_camera_left_before.hide()
                    FlagConstant.STATUS_SERVICE_LEFT_BEFORE
                }
                CommonsConstant.REQUEST_CODE_IMAGE_RIGHT_BEFORE -> {
                    iv_camera_right_before.hide()
                    FlagConstant.STATUS_SERVICE_RIGHT_BEFORE
                }
                CommonsConstant.REQUEST_CODE_IMAGE_FRONT_AFTER -> {
                    iv_camera_front_after.hide()
                    FlagConstant.STATUS_SERVICE_FRONT_AFTER
                }
                CommonsConstant.REQUEST_CODE_IMAGE_BACK_AFTER -> {
                    iv_camera_back_after.hide()
                    FlagConstant.STATUS_SERVICE_BACK_AFTER
                }
                CommonsConstant.REQUEST_CODE_IMAGE_LEFT_AFTER -> {
                    iv_camera_left_after.hide()
                    FlagConstant.STATUS_SERVICE_LEFT_AFTER
                }
                CommonsConstant.REQUEST_CODE_IMAGE_RIGHT_AFTER -> {
                    iv_camera_right_after.hide()
                    FlagConstant.STATUS_SERVICE_RIGHT_AFTER
                }
                CommonsConstant.REQUEST_CODE_IMAGE_OTHER_IMAGE ->
                    FlagConstant.STATUS_SERVICE_OTHER_IMAGE
                else -> 0
            }

            viewModel.callUploadImageService(
                convertToMultipartBody(data.data!!),
                statusService
            )
        }
    }

    private fun setImageJobService(serviceImage: ServiceImage) {
        val (frontBefore, backBefore, leftBefore, rightBefore, frontAfter, backAfter, leftAfter, rightAfter, _) = serviceImage

        setImageView(
            frontBefore,
            iv_image_front_before,
            iv_camera_front_before,
            card_remove_front_before
        )
        setImageView(
            backBefore,
            iv_image_back_before,
            iv_camera_back_before,
            card_remove_back_before
        )
        setImageView(
            leftBefore,
            iv_image_left_before,
            iv_camera_left_before,
            card_remove_left_before
        )
        setImageView(
            rightBefore,
            iv_image_right_before,
            iv_camera_right_before,
            card_remove_right_before
        )
        setImageView(
            frontAfter,
            iv_image_front_after,
            iv_camera_front_after,
            card_remove_front_after
        )
        setImageView(
            backAfter,
            iv_image_back_after,
            iv_camera_back_after,
            card_remove_back_after
        )
        setImageView(
            leftAfter,
            iv_image_left_after,
            iv_camera_left_after,
            card_remove_left_after
        )
        setImageView(
            rightAfter,
            iv_image_right_after,
            iv_camera_right_after,
            card_remove_right_after
        )
    }

    private fun setImageView(
        url: String?,
        ivImage: ImageView,
        ivCamera: ImageView,
        cardRemove: CardView
    ) {
        if (url != null) {
            ivCamera.hide()
            ivImage.setImageFromInternet(url)
            cardRemove.show()
        } else {
            ivCamera.show()
            ivImage.setImageResource(0)
            cardRemove.hide()
        }
    }

}
