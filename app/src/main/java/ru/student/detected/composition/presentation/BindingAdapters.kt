package ru.student.detected.composition.presentation

import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import ru.student.detected.composition.R

@BindingAdapter("int")
fun bindInt(textView: TextView, int: Int) {
    textView.text = int.toString()
}

interface OnOptionClickListener{
    fun onOptionClick(int: Int) : Unit
}

@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(textView: TextView, count: Int) {
    textView.text =
        String.format(
            textView.context.getString(R.string.required_score),
            count
        )
}

@BindingAdapter("requiredPercentage")
fun bindRequiredPercentage(textView: TextView, count: Int) {
    textView.text =
        String.format(
            textView.context.getString(R.string.required_percentage),
            count
        )
}

@BindingAdapter("scoreAnswers")
fun bindScoreAnswers(textView: TextView, count: Int) {
    textView.text =
        String.format(
            textView.context.getString(R.string.score_answers),
            count
        )
}

@BindingAdapter("count", "required", requireAll = true)
fun bindScorePercentage(textView: TextView, count: Int, required: Int) {
    fun calculatePercent(): Int {
        if (required != 0) {
            return ((count * 100.0) / required).toInt()
        }
        return 0
    }
    textView.text =
        String.format(
            textView.context.getString(R.string.score_percentage),
            calculatePercent()
        )
}

@BindingAdapter("emoji")
fun bindEmoji(imageView: ImageView, winner: Boolean) {
    imageView.setImageDrawable(
        ContextCompat.getDrawable(
            imageView.context,
            if (winner) R.drawable.ic_smile else R.drawable.ic_sad
        )
    )
}

@BindingAdapter("enoughOfRightAnswers")
fun bindEnoughOfRightAnswers(textView: TextView, boolean: Boolean) {
    textView.setTextColor(getColorByState(boolean))
}

@BindingAdapter("enoughOfPercentRightAnswers")
fun bindEnoughPercentOfRightAnswers(progressBar: ProgressBar, boolean: Boolean) {
    progressBar.progressTintList = ColorStateList.valueOf(getColorByState(boolean))
}

@BindingAdapter("onOptionClickListener")
fun bindOnOptionClick(textView: TextView, clickListener: OnOptionClickListener) {
    textView.setOnClickListener {
        clickListener.onOptionClick(textView.text.toString().toInt())
    }
}

private fun getColorByState(it: Boolean) = if (it) Color.GREEN else Color.RED
