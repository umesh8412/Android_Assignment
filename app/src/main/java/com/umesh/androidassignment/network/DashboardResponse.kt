package com.umesh.androidassignment.network
data class DashboardResponse(
    val applied_campaign: Int,
    val data: Data,
    val extra_income: Double,
    val links_created_today: Int,
    val message: String,
    val startTime: String,
    val status: Boolean,
    val statusCode: Int,
    val support_whatsapp_number: String,
    val today_clicks: Int,
    val top_location: String,
    val top_source: String,
    val total_clicks: Int,
    val total_links: Int
)
data class Data(
    val favourite_links: List<Any>,
    val overall_url_chart: Map<String, Int>,
    val recent_links: List<Link>,
    val top_links: List<Link>
)
data class Link(
    val app: String,
    val created_at: String,
    val domain_id: String,
    val is_favourite: Boolean,
    val original_image: String,
    val smart_link: String,
    val thumbnail: Any,
    val times_ago: String,
    val title: String,
    val total_clicks: Int,
    val url_id: Int,
    val url_prefix: String,
    val url_suffix: String,
    val web_link: String
)
data class AnalyticsItem(
    val imgResource: Int,
    val tv1Text: String,
    val tv2Text: String
)
