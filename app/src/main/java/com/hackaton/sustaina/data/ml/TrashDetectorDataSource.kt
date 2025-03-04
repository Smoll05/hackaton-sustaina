package com.hackaton.sustaina.data.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.Log
import com.hackaton.sustaina.ml.TrashDetectorFloat32
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.label.Category
import javax.inject.Inject

class TrashDetectorDataSource (
    private val context: Context
) {
    private val imageSize = 640

//    fun detectTrash(bitmap: Bitmap)/*: MutableList<Category>*/ {
//        val model = TrashDetectorFloat32.newInstance(context)
//
//        val resizeBitMap = resizeBitmap(bitmap, imageSize, imageSize)
//        val tensorImage = preprocessImage(resizeBitMap)
//
//        val outputs = model.process(tensorImage)
//
//        val outputTensorBuffer = outputs.outputFeature0AsTensorBuffer
//        val outputArray = outputTensorBuffer.floatArray // Convert output to float array
//
//        // Extract labels from detections
//        val detectedLabels = extractLabels(outputArray)
//
//        Log.d("MLModel", "Detected Labels: $detectedLabels")
//        Log.d("MLModel", "Total Labels Found: ${detectedLabels.size}")
//
//        return detectedLabels
//
//
//        model.close()
//
//        Log.d("MLModel", "Detections: $detectionList")
//        return detectionList

//        val model = TrashDetectorFloat32.newInstance(context)
//
//        val image = TensorImage.fromBitmap(
//            resizeBitmap(bitmap, imageSize, imageSize)
//        )
//
//        val outputs = model.process(image)
//        val output = outputs.outputAsCategoryList
//
//        output.forEach {
//            Log.d("ML MODEL", it.toString())
//        }
//
//        model.close()
//    }
//
//    private fun resizeBitmap(bitmap: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
//        val resizedBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(resizedBitmap)
//        val scaleX = targetWidth.toFloat() / bitmap.width
//        val scaleY = targetHeight.toFloat() / bitmap.height
//        val scaleFactor = minOf(scaleX, scaleY)
//
//        val matrix = Matrix().apply {
//            setScale(scaleFactor, scaleFactor)
//            postTranslate(
//                (targetWidth - bitmap.width * scaleFactor) / 2,
//                (targetHeight - bitmap.height * scaleFactor) / 2
//            )
//        }
//
//        canvas.drawBitmap(bitmap, matrix, Paint(Paint.ANTI_ALIAS_FLAG))
//        return resizedBitmap
//    }
//
//    private fun preprocessImage(bitmap: Bitmap): TensorImage {
//        val tensorImage = TensorImage(DataType.FLOAT32)
//        tensorImage.load(bitmap)
//
//        val imageProcessor = ImageProcessor.Builder()
//            .add(ResizeOp(640, 640, ResizeOp.ResizeMethod.BILINEAR)) // Resize correctly
//            .add(NormalizeOp(0f, 255f)) // Normalize pixel values to [0,1]
//            .build()
//
//        return imageProcessor.process(tensorImage)
//    }
    /*
    fun detectTrash(bitmap: Bitmap) {
        val model = TrashDetectorFloat32.newInstance(context)

// Creates inputs for reference.
        val image = TensorImage.fromBitmap(bitmap)

// Runs model inference and gets result.
        val outputs = model.process(image)
        val outputBuffer = outputs.outputAsTensorBuffer
        val outputArray = outputBuffer.floatArray

        outputArray.forEach {
            Log.d("ML MODEL", it.toString())
        }

        val numBoxes = 8400
        val numValuesPerBox = 63
        val confidenceThreshold = 0.3f // Adjust as needed

        val classNames = listOf(
            "Aerosol",
            "Aluminium blister pack",
            "Aluminium foil",
            "Battery",
            "Broken glass",
            "Carded blister pack",
            "Cigarette",
            "Clear plastic bottle",
            "Corrugated carton",
            "Crisp packet",
            "Disposable food container",
            "Disposable plastic cup",
            "Drink can",
            "Drink carton",
            "Egg carton",
            "Foam cup",
            "Foam food container",
            "Food Can",
            "Food waste",
            "Garbage bag",
            "Glass bottle",
            "Glass cup",
            "Glass jar",
            "Magazine paper",
            "Meal carton",
            "Metal bottle cap",
            "Metal lid",
            "Normal paper",
            "Other carton",
            "Other plastic",
            "Other plastic bottle",
            "Other plastic container",
            "Other plastic cup",
            "Other plastic wrapper",
            "Paper bag",
            "Paper cup",
            "Paper straw",
            "Pizza box",
            "Plastic bottle cap",
            "Plastic film",
            "Plastic gloves",
            "Plastic lid",
            "Plastic straw",
            "Plastic utensils",
            "Polypropylene bag",
            "Pop tab",
            "Rope-strings",
            "Scrap metal",
            "Shoe",
            "Single-use carrier bag",
            "Six pack rings",
            "Spread tub",
            "Squeezable tube",
            "Styrofoam piece",
            "Tissues",
            "Toilet tube",
            "Tupperware",
            "Unlabeled litter",
            "Wrapping paper"
        )

        val classCount = mutableMapOf<String, Int>()

        for (i in 0 until numBoxes) {
            val offset = i * numValuesPerBox

            val xCenter = outputArray[offset]
            val yCenter = outputArray[offset + 1]
            val width = outputArray[offset + 2]
            val height = outputArray[offset + 3]
            val confidence = outputArray[offset + 4]

            // Find the class with the highest probability
            var maxClassScore = 0f
            var classId = -1
            for (j in 5 until numValuesPerBox) {
                if (outputArray[offset + j] > maxClassScore) {
                    maxClassScore = outputArray[offset + j]
                    classId = j - 5
                }
            }

            // Filter based on confidence threshold
            if (confidence > confidenceThreshold) {
                val className = classNames[classId]
                classCount[className] = classCount.getOrDefault(className, 0) + 1
                Log.d("ML MODEL", "Detected object: Class $className with confidence $confidence at ($xCenter, $yCenter, $width, $height)")
            }
        }

        Log.d("ML MODEL", "Class count: $classCount")
        model.close()
    }

    */

    fun detectTrash() {
        val model = TrashDetectorFloat32.newInstance(context)

        val bitMap = BitmapFactory.decodeFile("drawable/trash.jpg")
        val image = TensorImage.fromBitmap(bitMap)

        val outputs = model.process(image)
        val output = outputs.outputAsCategoryList

        model.close()

    }
}
