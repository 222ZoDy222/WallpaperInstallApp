package com.zdy.wallpaperinstallapp.models.Web.ObjectsWeb.NekoImageObjects


data class NekoImage(
    //val artist: String,
    val characters: List<Any>,
    val color_dominant: List<Int>,
    val color_palette: List<List<Int>>,
    val created_at: Double,
    val duration: String,
    val hash_md5: String,
    val hash_perceptual: String,
    val id: Int,
    val id_v2: String,
    val image_height: Int,
    val image_size: Int,
    val image_url: String,
    val image_width: Int,
    val is_animated: Boolean,
    val is_flagged: Boolean,
    val is_original: Boolean,
    val is_screenshot: Boolean,
    val rating: String,
    val sample_height: Int,
    val sample_size: Int,
    val sample_url: String,
    val sample_width: Int,
    val source: String,
    val source_id: String,
    val tags: List<Tag>,
    val updated_at: Double,
    val verification: String,

)