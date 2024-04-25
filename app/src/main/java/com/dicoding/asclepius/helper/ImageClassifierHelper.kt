package com.dicoding.asclepius.helper

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import com.dicoding.asclepius.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.ops.ResizeOp


class ImageClassifierHelper(
    var threshold: Float = 0.1f,
    var maxResult: Int = 3,
    val modelName: String = "cancer_classification.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {

    private var imageClassifier: ImageClassifier? = null
    
    interface ClassifierListener {
        fun onError(error: String)
        fun onResult(
            result: MutableList<String>,
            inferenceTime: Long
        )
    }

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResult)
        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(4)
        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyStaticImage(contentResolver: ContentResolver, imageUri: Uri) {
        if (imageClassifier == null) {
            setupImageClassifier()
        }

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(CastOp(DataType.UINT8))
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, imageUri)
            val bitmap = ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
                decoder.setTargetSampleSize(1)
                decoder.setAllocator(ImageDecoder.ALLOCATOR_SOFTWARE)
            }
            val argbBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            val tensorImage = imageProcessor.process(TensorImage.fromBitmap(argbBitmap))
            val inferenceTime = SystemClock.uptimeMillis()
            val results = imageClassifier?.classify(tensorImage)

            results?.let { classifications ->
                val categoriesWithScores = mutableListOf<String>()
                for (classification in classifications) {
                    val label = classification.categories[0].label
                    val score = classification.categories[0].score
                    val formattedResult = "$label: ${score * 100}%"
                    categoriesWithScores.add("$label: ${score * 100}%")
                }
                classifierListener?.onResult(categoriesWithScores, inferenceTime)
            }
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, imageUri)?.let { bitmap ->
                val argbBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                val tensorImage = imageProcessor.process(TensorImage.fromBitmap(argbBitmap))
                val inferenceTime = SystemClock.uptimeMillis()
                val results = imageClassifier?.classify(tensorImage)

                results?.let { classifications ->
                    val categoriesWithScores = mutableListOf<String>()
                    for (classification in classifications) {
                        val label = classification.categories[0].label
                        val score = classification.categories[0].score
                        val formattedResult = "$label: ${score * 100}%"
                        categoriesWithScores.add("$label: ${score * 100}%")
                    }
                    classifierListener?.onResult(categoriesWithScores, inferenceTime)
                }
            }
        }
    }


    companion object {
        private const val TAG = "ImageClassifierHelper"
    }
}