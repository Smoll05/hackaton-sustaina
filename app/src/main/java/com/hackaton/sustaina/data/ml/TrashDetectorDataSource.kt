package com.hackaton.sustaina.data.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.Log
import com.hackaton.sustaina.ml.TrashDetectorFloat32
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.label.Category
import javax.inject.Inject

class TrashDetectorDataSource (
    private val context: Context
) {
    private val imageSize = 640

    fun detectTrash(bitmap: Bitmap):  MutableList<Category> {
        val model = TrashDetectorFloat32.newInstance(context)

        val resizeBitMap = resizeBitmap(bitmap, imageSize, imageSize)
        val tensorImage = TensorImage.fromBitmap(resizeBitMap)

        val outputs = model.process(tensorImage)
        val detectionList = outputs.outputAsCategoryList

        model.close()

        Log.d("MLModel", "Detections: $detectionList")
        return detectionList
    }

    private fun resizeBitmap(bitmap: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
        val resizedBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resizedBitmap)
        val scaleX = targetWidth.toFloat() / bitmap.width
        val scaleY = targetHeight.toFloat() / bitmap.height
        val scaleFactor = minOf(scaleX, scaleY)

        val matrix = Matrix().apply {
            setScale(scaleFactor, scaleFactor)
            postTranslate(
                (targetWidth - bitmap.width * scaleFactor) / 2,
                (targetHeight - bitmap.height * scaleFactor) / 2
            )
        }

        canvas.drawBitmap(bitmap, matrix, Paint(Paint.ANTI_ALIAS_FLAG))
        return resizedBitmap
    }

}
