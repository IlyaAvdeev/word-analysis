package one.avdeev.error

import avdeev.one.beans.WordCriteria

class NoMatchedWords(override val message: String, val details: WordCriteria) : Exception(message){
}