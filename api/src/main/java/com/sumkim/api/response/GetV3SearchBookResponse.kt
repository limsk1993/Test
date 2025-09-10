package com.sumkim.api.response

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

data class GetV3SearchBookResponse(
    @SerializedName("meta")
    val meta: Meta? = null,

    @SerializedName("documents")
    val documents: List<Document>? = null,
)

data class Meta(
    @SerializedName("is_end")
    val isEnd: Boolean? = null,

    @SerializedName("pageable_count")
    val pageableCount: Int? = null,

    @SerializedName("total_count")
    val totalCount: Int? = null,
)

@Entity(tableName = "favorite_table", primaryKeys = ["isbn"])
data class Document(
    @SerializedName("authors")
    val authors: List<String>? = null,

    @SerializedName("contents")
    val contents: String? = null,

    @SerializedName("datetime")
    val datetime: String? = null,

    @SerializedName("isbn")
    val isbn: String,

    @SerializedName("price")
    val price: Int? = null,

    @SerializedName("publisher")
    val publisher: String? = null,

    @SerializedName("sale_price")
    val salePrice: Int? = null,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("thumbnail")
    val thumbnail: String? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("translators")
    val translators: List<String>? = null,

    @SerializedName("url")
    val url: String? = null,
)