package com.extcode.project.stensaiapps.other

import android.text.TextUtils
import android.util.Patterns
import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout


const val kUserName = "userName"
const val kUserClass = "userClass"
const val kUserNIS = "userNIS"
const val kUserNIP = "userNIP"
const val kUserEmail = "userEmail"
const val kIsEdit = "isEdit"
const val kIsFinished = "isFinished"
const val kUnfinishedData = "unfinishedData"
const val kFinishedData = "finishedData"
const val kExtraTitle = "extraTitle"
const val kExtraMessage = "extraMessage"
const val kExtraID = "extraID"
const val kDateFormat = "yyyy-MM-dd"
const val kTimeFormat = "HH:mm"
const val kId = "id"
const val kIdStatus = "idStatus"
const val kUserData = "userData"
const val kDetailMagazine = "detailMagazine"

fun showProgressBar(view: View, isShow: Boolean) {
    view.visibility = if (isShow) View.VISIBLE else View.GONE
}

fun showShimmer(shimmer: ShimmerFrameLayout, isShow: Boolean) {
    if (isShow) shimmer.startShimmer() else shimmer.stopShimmer()
    shimmer.visibility = if (isShow) View.VISIBLE else View.GONE
}

fun showNotFound(view: View, isShow: Boolean) {
    view.visibility = if (isShow) View.VISIBLE else View.GONE
}

fun String.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}