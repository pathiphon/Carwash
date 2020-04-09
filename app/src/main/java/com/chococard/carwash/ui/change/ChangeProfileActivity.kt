package com.chococard.carwash.ui.change

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chococard.carwash.R
import com.chococard.carwash.data.models.User
import com.chococard.carwash.data.networks.ChangeApi
import com.chococard.carwash.data.repositories.ChangeRepository
import com.chococard.carwash.util.BaseActivity
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.util.getFileName
import kotlinx.android.synthetic.main.activity_change_profile.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ChangeProfileActivity : BaseActivity<ChangeViewModel>() {

    private val REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_profile)

        init()

    }

    private fun init() {
        val user = intent.getParcelableExtra<User>(getString(R.string.user))

        val factory =
            ChangeFactory(ChangeRepository(ChangeApi.invoke(networkConnectionInterceptor)))
        viewModel = ViewModelProvider(this, factory).get(ChangeViewModel::class.java)

        setToolbar(toolbar)

        //set widgets
        et_full_name.setText(user?.fullName)
        et_identity_card.setText(user?.idCard)
        et_phone.setText(user?.phone)
        user?.image?.let { iv_photo.loadCircle(it) }

        //set event
        iv_arrow_back.setOnClickListener { onBackPressed() }
        iv_photo.setOnClickListener { selectImage() }
        iv_camera.setOnClickListener { selectImage() }
        bt_cancel.setOnClickListener { finish() }
        bt_confirm.setOnClickListener { changeProfile() }

        //observe
        viewModel.upload.observe(this, Observer {
            progress_bar.hide()
        })

        //exception
        viewModel.exception = {
            progress_bar.hide()
            toast(it)
        }

    }

    private fun selectImage() = Intent(Intent.ACTION_PICK).apply {
        type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(this, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val fileUri = data.data!!
            iv_photo.loadCircle(fileUri.toString())
            uploadFile(fileUri)
        }
    }

    private fun uploadFile(fileUri: Uri) {
        val parcelFileDescriptor = contentResolver.openFileDescriptor(fileUri, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(fileUri))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val requestFile = RequestBody
            .create(MediaType.parse(contentResolver.getType(fileUri)), file)

        val body = MultipartBody.Part.createFormData("uploaded_file", file?.name, requestFile)

        val descriptionString = "hello, this is description speaking"
        val description = RequestBody.create(MultipartBody.FORM, descriptionString)

        progress_bar.show()
        viewModel.uploadImageFile(body, description)
    }

    private fun changeProfile() {
        toast("Confirm")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_change_password -> toast("option_change_password")
            R.id.option_contact_admin -> toast("option_contact_admin")
            R.id.option_logout -> toast("option_logout")
        }
        return super.onOptionsItemSelected(item)
    }

}
