package com.example.aaron.canvastest.customView

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.graphics.Bitmap




class CanvasView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    var mBitmap: Bitmap? = null

    companion object {
        private val STROKE_WIDTH = 5f
        private val HALF_STROKE_WIDTH = STROKE_WIDTH / 2
    }
    private val paint = Paint()
    private val path = Path()

    private var lastTouchX: Float = 0.toFloat()
    private var lastTouchY: Float = 0.toFloat()
    private val dirtyRect = RectF()

    init {
        paint.isAntiAlias = true
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = STROKE_WIDTH
    }

    fun save(v: View) {
        Log.v("log_tag", "Width: " + v.width)
        Log.v("log_tag", "Height: " + v.height)
        if (mBitmap == null) {
            mBitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565)
        }
        val canvas = Canvas(mBitmap)
        try {
           //val mFileOutStream = FileOutputStream(mypath)

            v.draw(canvas)
            //mBitmap?.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream)
            //mFileOutStream.flush()
            //mFileOutStream.close()
            val url = MediaStore.Images.Media.insertImage(context.contentResolver, mBitmap, "title", null)
            Log.v("log_tag", "url: $url")
            //In case you want to delete the file
            //boolean deleted = mypath.delete();
            //Log.v("log_tag","deleted: " + mypath.toString() + deleted);
            //If you want to convert the image to string use base64 converter

        } catch (e: Exception) {
            Log.v("log_tag", e.toString())
        }

    }

    fun getBitmap(): Bitmap {
        val v = this.parent as View
        val b = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        v.layout(v.left, v.top, v.right, v.bottom)
        v.draw(c)

        return b
    }

    fun clear() {
        path.reset()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val eventX = event.x
        val eventY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(eventX, eventY)
                lastTouchX = eventX
                lastTouchY = eventY
                return true
            }

            MotionEvent.ACTION_MOVE,

            MotionEvent.ACTION_UP -> {

                resetDirtyRect(eventX, eventY)
                val historySize = event.historySize
                for (i in 0 until historySize) {
                    val historicalX = event.getHistoricalX(i)
                    val historicalY = event.getHistoricalY(i)
                    expandDirtyRect(historicalX, historicalY)
                    path.lineTo(historicalX, historicalY)
                }
                path.lineTo(eventX, eventY)
            }

            else -> {
                debug("Ignored touch event: " + event.toString())
                return false
            }
        }

        invalidate(
            (dirtyRect.left - HALF_STROKE_WIDTH).toInt(),
            (dirtyRect.top - HALF_STROKE_WIDTH).toInt(),
            (dirtyRect.right + HALF_STROKE_WIDTH).toInt(),
            (dirtyRect.bottom + HALF_STROKE_WIDTH).toInt()
        )

        lastTouchX = eventX
        lastTouchY = eventY

        return true
    }

    private fun debug(string: String) {}

    private fun expandDirtyRect(historicalX: Float, historicalY: Float) {
        if (historicalX < dirtyRect.left) {
            dirtyRect.left = historicalX
        } else if (historicalX > dirtyRect.right) {
            dirtyRect.right = historicalX
        }

        if (historicalY < dirtyRect.top) {
            dirtyRect.top = historicalY
        } else if (historicalY > dirtyRect.bottom) {
            dirtyRect.bottom = historicalY
        }
    }

    private fun resetDirtyRect(eventX: Float, eventY: Float) {
        dirtyRect.left = Math.min(lastTouchX, eventX)
        dirtyRect.right = Math.max(lastTouchX, eventX)
        dirtyRect.top = Math.min(lastTouchY, eventY)
        dirtyRect.bottom = Math.max(lastTouchY, eventY)
    }
}