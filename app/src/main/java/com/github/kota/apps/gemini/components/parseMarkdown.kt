package com.github.kota.apps.gemini.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

fun parseMarkdown(text: String): AnnotatedString {
    return buildAnnotatedString {
        val lines = text.lines()
        for (line in lines) {
            when {
                line.startsWith("```") -> {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("\n") // コードブロックの開始・終了を適切に処理
                    }
                }

                line.startsWith("# ") -> {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)) {
                        append(line.removePrefix("# ") + "\n")
                    }
                }

                line.startsWith("## ") -> {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)) {
                        append(line.removePrefix("## ") + "\n")
                    }
                }

                line.startsWith("### ") -> {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp)) {
                        append(line.removePrefix("### ") + "\n")
                    }
                }

                line.startsWith("- ") -> {
                    append("• ")
                    parseInlineMarkdown(line.removePrefix("- "), this) // インライン処理を追加
                    append("\n")
                }

                else -> {
                    parseInlineMarkdown(line, this) // インライン処理を追加
                    append("\n")
                }
            }
        }
    }
}

// **インライン処理を追加**
private fun parseInlineMarkdown(text: String, builder: AnnotatedString.Builder) {
    var currentIndex = 0
    val regex = "(\\*\\*|\\*)(.+?)\\1".toRegex()

    regex.findAll(text).forEach { match ->
        val start = match.range.first
        val end = match.range.last + 1
        val style = when (match.groups[1]?.value) {
            "**" -> SpanStyle(fontWeight = FontWeight.Bold)
            "*" -> SpanStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
            else -> SpanStyle()
        }

        builder.append(text.substring(currentIndex, start))
        builder.withStyle(style) {
            append(match.groups[2]?.value ?: "")
        }
        currentIndex = end
    }

    if (currentIndex < text.length) {
        builder.append(text.substring(currentIndex))
    }
}

