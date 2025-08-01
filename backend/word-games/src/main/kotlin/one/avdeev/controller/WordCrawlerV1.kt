package one.avdeev.controller

import avdeev.one.V1Resource
import avdeev.one.beans.FoundWords
import avdeev.one.beans.WordCriteria
import jakarta.inject.Inject
import one.avdeev.repository.AllWordsRepository
import one.avdeev.repository.OjegovRepository
import java.math.BigInteger
import java.time.Instant
import java.util.*


class WordCrawlerV1 : V1Resource {

    @Inject
    lateinit var ojegovRepository: OjegovRepository

    @Inject
    lateinit var allWordsRepository: AllWordsRepository

    override fun getWordByLetters(
        length: BigInteger?,
        page: BigInteger?,
        items: BigInteger?,
        xRequestID: String?,
        sessionId: String?,
        data: WordCriteria?
    ): FoundWords {
        val ojegovResults = ojegovRepository.findWord(length!!.toInt(), data!!.nonPresentLetters, data.exactLetters, data.misplacedLetters, 1, 20)
        val allWordsResults = allWordsRepository.findWord(length!!.toInt(), data!!.nonPresentLetters, data.exactLetters, data.misplacedLetters, 1, 20)

        var totalResults = listOf(ojegovResults.map{it.word}, allWordsResults.map{it.word}).flatten().distinct()

        val foundWords : FoundWords = FoundWords()
        foundWords.createdAt = Date.from(Instant.now())
        foundWords.words = totalResults
        foundWords.pageNumber = page!!.toInt()
        foundWords.totalElements = ojegovResults.size + allWordsResults.size

        return foundWords
    }

}