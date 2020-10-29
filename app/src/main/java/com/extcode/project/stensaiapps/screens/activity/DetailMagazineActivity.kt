package com.extcode.project.stensaiapps.screens.activity

import android.os.Bundle
import android.os.StrictMode
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.model.api.MessageItem
import com.extcode.project.stensaiapps.other.kDetailMagazine
import com.viven.imagezoom.ImageZoomHelper
import kotlinx.android.synthetic.main.activity_detail_magazine.*


class DetailMagazineActivity : AppCompatActivity() {

    private lateinit var imageZoomHelper: ImageZoomHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_magazine)

        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        imageZoomHelper = ImageZoomHelper(this)
        ImageZoomHelper.setViewZoomable(detailMagazinePicture)

        setSupportActionBar(detailTopAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val detailMagazine = intent.getParcelableExtra<MessageItem>(kDetailMagazine)

        detailMagazine.apply {
            if (this != null) {
                supportActionBar?.title = this.judul
                detailMagazineTitle.text = this.judul
                detailMagazineDescription.text = this.deskripsi
                detailMagazineAuthor.text = if (this.nama.isNullOrEmpty()) getString(
                    R.string.uploadedBy,
                    "Admin"
                ) else getString(R.string.authorBy, this.nama)

                Glide.with(this@DetailMagazineActivity)
                    .load("http://stensai-apps.com/img/thumbnail/${this.thumbnail}")
                    .into(detailMagazinePicture)
                Glide.with(this@DetailMagazineActivity)
                    .load("http://stensai-apps.com/img/thumbnail/${this.thumbnail}")
                    .into(placeHolderDetailMagazinePicture)
            }

            fabShareMagazine.setOnClickListener {
//                storagePermission(it, this!!)
            }
        }
    }
//
//    private fun storagePermission(detailMagazine: MessageItem) {
//        Dexter.withContext(this)
//            .withPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//            .withListener(object : PermissionListener {
//                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
//                    actionShare(detailMagazine)
//                }
//
//                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
//                }
//
//                override fun onPermissionRationaleShouldBeShown(
//                    p0: PermissionRequest?,
//                    p1: PermissionToken?
//                ) {
//
//                }
//            })
//    }
//
//    private fun actionShare(detailMagazine: MessageItem) {
//
//        val img = URL("http://stensai-apps.com/img/thumbnail/${detailMagazine.thumbnail}")
//        val result = img.toBitmap()
//        val text = detailMagazine.judul
//        val uri = getImageUriFromBitmap(result)
//        val intent = Intent(Intent.ACTION_SEND)
//
//        intent.type = "image/*"
//        intent.putExtra(Intent.EXTRA_TEXT, text)
//        intent.putExtra(Intent.EXTRA_STREAM, uri)
//        startActivity(Intent.createChooser(intent, "Share Image"))
//    }
//
//    private fun URL.toBitmap(): Bitmap? {
//        return try {
//            BitmapFactory.decodeStream(openStream())
//        } catch (e: IOException) {
//            null
//        }
//    }
//
//    private fun getImageUriFromBitmap(bitmap: Bitmap?): Uri {
//        val bytes = ByteArrayOutputStream()
//        lateinit var uri: Uri
//        try {
//            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, bytes)
//            val path = MediaStore.Images.Media.insertImage(
//                contentResolver,
//                bitmap,
//                "com.extcode.project.stensaiapps",
//                null
//            )
//            uri = Uri.parse(path.toString())
//        } catch (e: Exception) {
//
//        }
//        return uri
//    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return imageZoomHelper.onDispatchTouchEvent(ev) || super.dispatchTouchEvent(ev)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}