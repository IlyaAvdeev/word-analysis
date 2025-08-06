package one.avdeev.repository

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import one.avdeev.error.InvalidInput
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@QuarkusTest
class OjegovRepositoryTest {

    @Inject
    lateinit var ojegovRepository: OjegovRepository

    @Test
    fun given_OjegovRepositoryContainsWords_when_RequestWordSearchingByWordLength_then_WordIsFound () {
        var foundWords = ojegovRepository.findWord(4, ArrayList<String>(), listOf("?", "?", "?", "?"), ArrayList<String>())
        Assertions.assertEquals(1, foundWords.count(), "Найдено не одно слово")
        Assertions.assertEquals("яйцо", foundWords.get(0).word, "Найденное слово НЕ яйцо")
    }

    @Test
    fun given_OjegovRepositoryContainsWords_when_RequestWordSearchingByMatchedLettersButInputIsIncorrect_then_ExceptionIsThrown () {
        val exception = Assertions.assertThrows(InvalidInput::class.java, {ojegovRepository.findWord(4, ArrayList<String>(), listOf("?", "?", "?", "?"), listOf("брак", "опапа", "jdshfsj"))})
        Assertions.assertEquals("Размер определяемого слова - 4", "Размер определяемого слова - 4")
        Assertions.assertArrayEquals(arrayOf("опапа", "jdshfsj"), exception.details.toTypedArray())
    }

    @Test
    fun given_OjegovRepositoryContainsWords_when_RequestWordSearchingByMisplacedLettersButInputIsIncorrect_then_ExceptionIsThrown () {
        val exception = Assertions.assertThrows(InvalidInput::class.java, {ojegovRepository.findWord(4, ArrayList<String>(), listOf("?", "?", "п", "?","?"), ArrayList<String>())})
        Assertions.assertEquals(exception.message, "Размер определяемого слова 4 не совпадает с длиной переданного слова")
        Assertions.assertArrayEquals(arrayOf("?", "?", "п", "?", "?"), exception.details.toTypedArray())
    }

    @Test
    fun given_OjegovRepositoryContainsWords_when_RequestWordSearchingByNonPresentLettersButInputIsIncorrect_then_ExceptionIsThrown () {
        val exception = Assertions.assertThrows(InvalidInput::class.java, {ojegovRepository.findWord(3, listOf("а", "рр", "п",), listOf("?", "?", "?"), ArrayList<String>())})
        Assertions.assertEquals(exception.message, "В качестве буквы отсутствующей в слове переданы неоднобуквенные значения")
        Assertions.assertArrayEquals(arrayOf("рр"), exception.details.toTypedArray())
    }

    @Test
    fun given_OjegovRepositoryContainsRequiredWords_when_RequestWordSearchingByAllExactLetters_then_WordIsFound () {
        var foundWords = ojegovRepository.findWord(5, ArrayList<String>(), listOf("с", "м", "о", "л", "а"), ArrayList<String>())
        Assertions.assertEquals(1, foundWords.count(), "Найдено не одно слово")
        Assertions.assertEquals("смола", foundWords.get(0).word, "Найденное слово НЕ смола")
    }

    @Test
    fun given_OjegovRepositoryContainsRequiredWords_when_RequestWordSearchingBySeveralExactLetters_then_2WordsFound () {
        var foundWords = ojegovRepository.findWord(5, ArrayList<String>(), listOf("с", "?", "?", "?", "а"), ArrayList<String>())
        Assertions.assertEquals(2, foundWords.count(), "Найдено не 2 слова")
        Assertions.assertTrue(foundWords.map{it.word}.contains("смола"), "Слова Смола в списке найденных нет")
        Assertions.assertTrue(foundWords.map{it.word}.contains("слива"), "Слова Слива в списке найденных нет")
    }

    @Test
    fun given_OjegovRepositoryContainsWords_when_RequestWordSearchingByNonPresentLetters_then_AllRequestedWordsFound() {
        var foundWords = ojegovRepository.findWord(5, listOf("я","ц"), listOf("?", "?", "?", "?", "?"), ArrayList<String>())
        Assertions.assertEquals(5, foundWords.count(), "Ожидаемо количество найденных слов 5")
        Assertions.assertTrue(foundWords.map{it.word}.contains("смола"), "Слова Смола в списке найденных нет")
        Assertions.assertTrue(foundWords.map{it.word}.contains("слива"), "Слова Слива в списке найденных нет")
        Assertions.assertTrue(foundWords.map{it.word}.contains("вышка"), "Слова Вышка в списке найденных нет")
        Assertions.assertTrue(foundWords.map{it.word}.contains("пижон"), "Слова Пижон в списке найденных нет")
        Assertions.assertTrue(foundWords.map{it.word}.contains("купон"), "Слова Купон в списке найденных нет")
    }

    @Test
    fun given_OjegovRepositoryContainsWords_when_RequestWordSearchingByAllParams_then_AllRequestedWordsFound() {
        var foundWords = ojegovRepository.findWord(5, listOf("ы"), listOf("?", "?", "?", "?", "а"), listOf("?в???"))
        Assertions.assertEquals(1, foundWords.count(), "Ожидаемо количество найденных слов 1")
        Assertions.assertTrue(foundWords.map{it.word}.contains("слива"), "Слова Слива в списке найденных нет")
    }

    @Test
    fun given_OjegovRepositoryContainsWords_when_RequestWordSearching_then_IgnoreWordsWithEllipsis() {
        var foundWords = ojegovRepository.findWord(5, ArrayList<String>(), listOf("с", "л", "и", "в", "?"), ArrayList<String>())
        Assertions.assertEquals(1, foundWords.count(), "Ожидаемо количество найденных слов 1")
        Assertions.assertTrue(foundWords.map{it.word}.contains("слива"), "Слова Слива в списке найденных нет")
    }
}