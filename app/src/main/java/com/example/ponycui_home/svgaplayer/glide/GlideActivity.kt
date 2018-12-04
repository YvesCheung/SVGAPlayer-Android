package com.example.ponycui_home.svgaplayer.glide

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Layout
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.StaticLayout
import android.text.TextPaint
import android.text.style.ForegroundColorSpan
import android.view.View
import com.bumptech.glide.Glide
import com.example.ponycui_home.svgaplayer.R
import com.opensource.svgaplayer.SVGADynamicEntity
import com.opensource.svgaplayer.glideplugin.SVGATarget
import kotlinx.android.synthetic.main.activity_test.*

/**
 * Created by 张宇 on 2018/11/26.
 * E-mail: zhangyu4@yy.com
 * YY: 909017428
 */
class GlideActivity : AppCompatActivity() {

    private val svgaFiles = mapOf(
        "norsvga" to listOf(
            "logo-revenge-start",
            "logo-start"),
        "ranksvga" to listOf(
            "king-star",
            "rank-add",
            "rank-loss",
            "star-add",
            "star-loss"),
        "ranksvga2" to listOf(
            "att_failed",
            "att_succ",
            "def_failed",
            "def_succ",
            "failed_and_upgrade",
            "no_upgrade",
            "rank_failed",
            "rank_succ",
            "succ_and_upgrade")
    )

    private var curIdx = 0

    private val fileUrl = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        loadAssetFileUrl()
    }

    private fun loadAssetFileUrl() {
        svgaFiles.entries.forEach { (path, list) ->
            list.forEach { fileName ->
                fileUrl.add("$path/$fileName.svga")
            }
        }
    }

    fun loadSVGAFromNetwork(v: View) {
        curIdx = ++curIdx % fileUrl.size
        val url = "https://github.com/YvesCheung/SVGAPlayer-Android/" +
            "raw/master/app/src/main/assets/${fileUrl[curIdx]}"
        loadSVGAFromUrl(url)
    }

    fun loadSVGAFromAssets(v: View) {
        val fileUrl = "file:///android_asset/${fileUrl[curIdx]}"
        loadSVGAFromUrl(fileUrl)
    }

    private fun loadSVGAFromUrl(url: String) {
        tv_assets_name.text = url
        Glide.with(this).load(url).into(iv_img)
    }

    fun loadSVGAFromNetworkAndAddText(v: View) {
        GlideApp.with(this)
            .asSVGA()
            .load("https://github.com/yyued/SVGA-Samples/blob/master/kingset.svga?raw=true")
            .into(SVGATarget(iv_img, requestDynamicItemWithSpannableText()))
    }

    private fun requestDynamicItemWithSpannableText(): SVGADynamicEntity {
        val dynamicEntity = SVGADynamicEntity()
        val spannableStringBuilder = SpannableStringBuilder("Pony 送了一打风油精给主播")
        spannableStringBuilder.setSpan(ForegroundColorSpan(Color.YELLOW), 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        val textPaint = TextPaint()
        textPaint.color = Color.WHITE
        textPaint.textSize = 28f
        dynamicEntity.setDynamicText(StaticLayout(
            spannableStringBuilder,
            0,
            spannableStringBuilder.length,
            textPaint,
            0,
            Layout.Alignment.ALIGN_CENTER,
            1.0f,
            0.0f,
            false
        ), "banner")
        dynamicEntity.setDynamicDrawer({ canvas, frameIndex ->
            val aPaint = Paint()
            aPaint.color = Color.WHITE
            canvas.drawCircle(50f, 54f, (frameIndex % 5).toFloat(), aPaint)
            false
        }, "banner")
        return dynamicEntity
    }
}
