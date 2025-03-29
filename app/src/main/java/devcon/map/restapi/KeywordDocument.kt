package devcon.map.restapi

data class KeywordDocument(
    val place_name: String,
    val address_name: String,
    val road_address_name: String,
    val x: String,
    val y: String,
    val distance: String,
    // 필요한 다른 필드들을 정의
)
