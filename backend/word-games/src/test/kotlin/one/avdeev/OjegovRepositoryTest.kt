package one.avdeev

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import one.avdeev.repository.OjegovRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@QuarkusTest
class OjegovRepositoryTest {

    @Inject
    lateinit var ojegovRepository: OjegovRepository

    @Test
    fun given_OjegovRepositoryContainsRequiredWords_when_RequestWordSearchingByAllExactLetters_then_WordIsFound () {
        var foundWords = ojegovRepository.findWord(5, ArrayList<String>(), listOf("с", "м", "о", "л", "а"), ArrayList<String>())
        Assertions.assertEquals(1, foundWords.count(), "Найдено не одно слово")
        Assertions.assertEquals("смола", foundWords.get(0).word, {"Найденное слово НЕ смола"})
    }

    @Test
    fun given_OjegovRepositoryContainsRequiredWords_when_RequestWordSearchingBySeveralExactLetters_then_2WordsFound () {
        var foundWords = ojegovRepository.findWord(5, ArrayList<String>(), listOf("с", "?", "?", "?", "а"), ArrayList<String>())
        Assertions.assertEquals(2, foundWords.count(), "Изначально в БД не 2 слова")
        assert(foundWords.map{it.word}.contains("смола"), {"Слова Смола в списке найденных нет"})
        assert(foundWords.map{it.word}.contains("слива"), {"Слова Слива в списке найденных нет"})
    }

/*
    @Test
    fun testHelloEndpoint() {
        given()
          .`when`().get("/hello")
          .then()
             .statusCode(200)
             .body(`is`("Hello from Quarkus REST"))
    }
*/
}