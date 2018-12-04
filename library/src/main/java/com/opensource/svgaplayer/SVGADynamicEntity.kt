package com.opensource.svgaplayer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.text.StaticLayout
import android.text.TextPaint
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by cuiminghui on 2017/3/30.
 */
class SVGADynamicEntity {

    var dynamicHidden: HashMap<String, Boolean> = hashMapOf()

    var dynamicImage: HashMap<String, Bitmap> = hashMapOf()

    var dynamicText: HashMap<String, String> = hashMapOf()

    var dynamicTextPaint: HashMap<String, TextPaint> = hashMapOf()

    var dynamicLayoutText: HashMap<String, StaticLayout> = hashMapOf()

    internal var dynamicDrawer: HashMap<String, (canvas: Canvas, frameIndex: Int) -> Boolean> = hashMapOf()

    internal var isTextDirty = false

    fun setHidden(value: Boolean, forKey: String) {
        this.dynamicHidden.put(forKey, value)
    }

    fun setDynamicImage(bitmap: Bitmap, forKey: String) {
        this.dynamicImage.put(forKey, bitmap)
    }

    fun setDynamicImage(url: String, forKey: String) {
        val handler = android.os.Handler()
        Thread {
            try {
                (URL(url).openConnection() as? HttpURLConnection)?.let {
                    it.connectTimeout = 20 * 1000
                    it.requestMethod = "GET"
                    it.connect()
                    BitmapFactory.decodeStream(it.inputStream)?.let {
                        handler.post { setDynamicImage(it, forKey) }
                    }
                    it.inputStream.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    fun setDynamicText(text: String, textPaint: TextPaint, forKey: String) {
        this.isTextDirty = true
        this.dynamicText.put(forKey, text)
        this.dynamicTextPaint.put(forKey, textPaint)
    }

    fun setDynamicText(layoutText: StaticLayout, forKey: String) {
        this.isTextDirty = true
        this.dynamicLayoutText.put(forKey, layoutText)
    }

    fun setDynamicDrawer(drawer: (canvas: Canvas, frameIndex: Int) -> Boolean, forKey: String) {
        this.dynamicDrawer.put(forKey, drawer)
    }

    fun clearDynamicObjects() {
        this.isTextDirty = true
        this.dynamicHidden.clear()
        this.dynamicImage.clear()
        this.dynamicText.clear()
        this.dynamicTextPaint.clear()
        this.dynamicLayoutText.clear()
        this.dynamicDrawer.clear()
    }

}