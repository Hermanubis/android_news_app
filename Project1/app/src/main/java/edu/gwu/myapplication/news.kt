package edu.gwu.myapplication

import java.io.Serializable

data class news(
    val title: String,
    val source: String,
    val iconurl: String,
    val content: String,
    val link: String
): Serializable