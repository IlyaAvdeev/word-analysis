package one.avdeev.error

class InvalidInput(override val message: String, val details: List<String>) : Exception(message){
}