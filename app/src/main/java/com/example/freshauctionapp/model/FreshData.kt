package com.example.freshauctionapp.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

//검색 결과(json string)를 FreshData 형태로 맵핑하기 위한 FreshWrapper 클래스(Moshi)
data class FreshWrapper(
    val list: List<FreshData>?
)

@Entity(
    tableName = "Fresh",
    foreignKeys = arrayOf(
        ForeignKey(
            /*참조한 부모 테이블이 delete 이벤트가 발생하면 자동 반영 */
            onDelete = ForeignKey.CASCADE,
            entity = SaveItem::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("saveId")
        )
    )
)

data class FreshData(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var saveId: Long? = null,

    //@Json(name = "resultCode")
    //var resultCode: String,
    //@Json(name = "resultMsg")
    //var resultMsg: String,
    //@Json(name = "totalCount")
    //var totalCount: String,
    //@Json(name = "numOfRows")
    //var numOfRows: String,
    //@Json(name = "pageNo")
    //var pageNo: String,
    //@Json(name = "type")
    //var type: String,
    @Json(name = "create_date")
    var create_date: String,
    @Json(name = "location_id")
    var location_id: String,
    @Json(name = "location_name")
    var location_name: String,
    @Json(name = "md101_sn")
    var md101_sn: String,
    @Json(name = "msg")
    var msg: String,
    @Json(name = "send_platform")
    var send_platform: String
)


@Entity(tableName = "SaveItem")
data class SaveItem(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    var saveTitle: String
)
