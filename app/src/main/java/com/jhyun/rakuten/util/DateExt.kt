package com.jhyun.rakuten.util

import java.text.SimpleDateFormat
import java.util.Date

object DateExt {
    fun Date.toSimpleFormat() : String {
        return SimpleDateFormat("yy-MM-dd HH:mm").format(this)
    }
}