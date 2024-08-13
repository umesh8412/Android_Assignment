package com.umesh.androidassignment.network

import com.google.gson.annotations.SerializedName


//data class DashboardResponse(
//    @SerializedName("status") val status: Boolean,
//    @SerializedName("statusCode") val statusCode: Int,
//    @SerializedName("message") val message: String,
//    @SerializedName("support_whatsapp_number") val supportWhatsappNumber: String,
//    @SerializedName("extra_income") val extraIncome: Double,
//    @SerializedName("total_links") val totalLinks: Int,
//    @SerializedName("total_clicks") val totalClicks: Int,
//    @SerializedName("today_clicks") val todayClicks: Int,
//    @SerializedName("top_source") val topSource: String? = null,
//    @SerializedName("top_location") val topLocation: String? = null,
//    @SerializedName("startTime") val startTime: String? = null,
//    @SerializedName("links_created_today") val linksCreatedToday: Int,
//    @SerializedName("applied_campaign") val appliedCampaign: Int,
//    @SerializedName("data") val data: Data
//)
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
//data class Data(
//    @SerializedName("recent_links") val recentLinks: List<Link> = emptyList(),
//    @SerializedName("top_links") val topLinks: List<Link> = emptyList(),
//    @SerializedName("favourite_links") val favouriteLinks: List<Link> = emptyList(),
//    @SerializedName("overall_url_chart") val overallUrlChart:  Map<String, Int>
//)
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
//    @SerializedName("url_id") val urlId: Int,
//    @SerializedName("web_link") val webLink: String,
//    @SerializedName("smart_link") val smartLink: String,
//    @SerializedName("title") val title: String,
//    @SerializedName("total_clicks") val totalClicks: Int,
//    @SerializedName("original_image") val originalImage: String,
//    @SerializedName("thumbnail") val thumbnail: String? = null,
//    @SerializedName("times_ago") val timesAgo: String,
//    @SerializedName("created_at") val createdAt: String,
//    @SerializedName("domain_id") val domainId: String,
//    @SerializedName("url_prefix") val urlPrefix: String? = null,
//    @SerializedName("url_suffix") val urlSuffix: String,
//    @SerializedName("app") val app: String,
//    @SerializedName("is_favourite") val isFavourite: Boolean
)
data class AnalyticsItem(
    val imgResource: Int,
    val tv1Text: String,
    val tv2Text: String
)
data class ClickData(
    val timestamp: Int,
    val clicks: Int
)
