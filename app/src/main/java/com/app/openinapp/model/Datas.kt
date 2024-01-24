package com.app.openinapp.model


data class Datas(
    val recent_links: List<Links>,
    val top_links: List<Links>,
    val overall_url_chart: Map<String, Int>
)
