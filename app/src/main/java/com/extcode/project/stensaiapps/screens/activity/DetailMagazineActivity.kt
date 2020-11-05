package com.extcode.project.stensaiapps.screens.activity

import android.os.Bundle
import android.os.StrictMode
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.model.api.EventItem
import com.extcode.project.stensaiapps.model.api.MessageItem
import com.extcode.project.stensaiapps.other.kDetailEvent
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
        val detailEvent = intent.getParcelableExtra<EventItem>(kDetailEvent)

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
        }

        detailEvent.apply {
            if (this != null) {
                supportActionBar?.title = this.nama
                detailMagazineTitle.text = this.nama
                detailMagazineDescription.text = this.deskripsi
                detailMagazineAuthor.text = this.tanggal
                Glide.with(this@DetailMagazineActivity)
                    .load("http://stensai-apps.com/img/event/${this.foto}")
                    .into(detailMagazinePicture)
                Glide.with(this@DetailMagazineActivity)
                    .load("http://stensai-apps.com/img/event/${this.foto}")
                    .into(placeHolderDetailMagazinePicture)
            }
        }

        fabShareMagazine.setOnClickListener {
//                storagePermission(it, this!!)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return imageZoomHelper.onDispatchTouchEvent(ev) || super.dispatchTouchEvent(ev)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}