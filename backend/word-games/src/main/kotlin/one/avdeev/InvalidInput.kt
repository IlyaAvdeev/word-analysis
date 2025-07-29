package one.avdeev

class InvalidInput(override val message: String, val details: List<String>) : Exception(message){
}