package me.fru1t.subedit.utils

//data class Dialogue(val fields: List<String>) {
//    init {
//        require(fields.size == 10) { "The length of the fields has to be 10" }
//    }
//
//    fun toFormattedString(): String {
//        return "Dialogue: ${fields.joinToString(separator = ",")}"
//    }
//}

data class Dialogue(val layer: String, val start: String, val end: String, val style: String, val name: String,
                    val marginL: String, val marginR: String, val marginV: String, val effect: String, val text: String) {
    companion object {
        fun fromList(fields: List<String>): Dialogue {
            require(fields.size == 10) { "The length of the fields has to be 10" }
            return Dialogue(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5], fields[6], fields[7], fields[8], fields[9])
        }
    }
    fun toFormattedString(): String {
        return "Dialogue: ${layer},${start},${end},${style},${name},${marginL},${marginR},${marginV},${effect},${text}"
    }
}
