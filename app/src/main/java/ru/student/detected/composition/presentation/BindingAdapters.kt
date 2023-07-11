package ru.student.detected.composition.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import ru.student.detected.composition.R


@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(textView: TextView, count: Int){
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
fun bindScoreAnswers(textView: TextView, count: Int){
    textView.text =
        String.format(
            textView.context.getString(R.string.score_answers),
            count
        )
}

@BindingAdapter("count", "required", requireAll = true)
fun bindScorePercentage(textView: TextView, count: Int, required: Int){
    fun calculatePercent(): Int{
        if(required!=0){
            return ((count *100.0)/required).toInt()
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
fun bindEmoji(imageView: ImageView, winner: Boolean){
    imageView.setImageDrawable(
        ContextCompat.getDrawable(imageView.context,
            if (winner) R.drawable.ic_smile else R.drawable.ic_sad)
    )
}