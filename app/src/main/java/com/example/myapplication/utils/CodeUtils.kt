package com.example.myapplication.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

data class CodeBlock(
    val language: String,
    val code: String
)

object SyntaxHighlighter {
    val languageKeywords = mapOf(
        "kotlin" to listOf("fun", "val", "var", "if", "else", "when", "class", "object", "interface", "for", "while", "return"),
        "python" to listOf("def", "class", "if", "else", "elif", "for", "while", "import", "from", "return", "lambda"),
        "java" to listOf("public", "private", "class", "interface", "static", "final", "void", "int", "String", "return"),
        "javascript" to listOf("function", "var", "let", "const", "if", "else", "for", "while", "return", "class", "import", "from"),
        "c++" to listOf("int", "float", "double", "char", "if", "else", "for", "while", "return", "class", "public", "private", "protected")
    )

    val keywordColor = Color(0xFF8E4EFF) // 紫色
    val stringColor = Color(0xFF008000) // 绿色
    val commentColor = Color(0xFF808080) // 灰色
    val numberColor = Color(0xFFFF0000) // 红色
    val functionColor = Color(0xFF0000FF) // 蓝色
    val classColor = Color(0xFF00FFFF) // 青色
    val commonColor = Color.White
}

fun highlightSyntax(code: String, language: String): AnnotatedString {
    return buildAnnotatedString {
        val keywords = SyntaxHighlighter.languageKeywords[language] ?: emptyList()
        val delimiters = arrayOf(" ")
        val functionPattern = Regex("""\b\w+(?=\()""")
        val classPattern = Regex("""\b[A-Z]\w*""")

        code.split("\n").forEach { line ->
            line.split(*delimiters).forEach { word ->
                when {
                    keywords.contains(word) -> withStyle(style = SpanStyle(color = SyntaxHighlighter.keywordColor)) {
                        append(word)
                    }
                    word.startsWith("\"") && word.endsWith("\"") -> withStyle(style = SpanStyle(color = SyntaxHighlighter.stringColor)) {
                        append(word)
                    }
                    word.startsWith("//") -> withStyle(style = SpanStyle(color = SyntaxHighlighter.commentColor)) {
                        append(word)
                    }
                    word.toDoubleOrNull() != null -> withStyle(style = SpanStyle(color = SyntaxHighlighter.numberColor)) {
                        append(word)
                    }
                    functionPattern.matches(word) -> withStyle(style = SpanStyle(color = SyntaxHighlighter.functionColor)) {
                        append(word)
                    }
                    classPattern.matches(word) -> withStyle(style = SpanStyle(color = SyntaxHighlighter.classColor)) {
                        append(word)
                    }
                    else -> withStyle(style = SpanStyle(color = SyntaxHighlighter.commonColor)) {
                        append(word)
                    }
                }
                append(" ") // 保留分隔符
            }
            append("\n")
        }
    }
}



