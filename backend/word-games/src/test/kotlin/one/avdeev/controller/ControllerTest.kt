package one.avdeev.controller

import avdeev.one.beans.WordCriteria
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import jakarta.ws.rs.core.MediaType
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test

@QuarkusTest
class ControllerTest {
    @Test
    fun simpleSearchBy8LettersWithEmptyResults() {
        val search = WordCriteria()
        search.exactLetters = listOf("?", "?", "?", "?", "?", "?", "?", "?")
        search.misplacedLetters = ArrayList<String>()
        search.nonPresentLetters = ArrayList<String>()

        given()
            .queryParams(mapOf("length" to 8, "page" to 1, "items" to 20))
            .header("X-Request-ID", "11111")
            .cookie("session_id", "1")
            .contentType("application/json")
            .body(search)
            .`when`()
            .post("/v1/crawler/letters")
            .then()
            .statusCode(404)
            .body("message", Matchers.equalTo<String>("Нет слов подходящих под заданные критерии."))
            .body("noWordDetails.non_present_letters",Matchers.hasSize<String>(search.nonPresentLetters.size))
            .body("noWordDetails.misplaced_letters",Matchers.hasSize<String>(search.misplacedLetters.size))
            .body("noWordDetails.exact_letters",Matchers.equalTo(search.exactLetters))
    }

    @Test
    fun simpleSearchBy5LettersWithPositiveResults() {
        val search = WordCriteria()
        search.exactLetters = listOf("с", "?", "?", "?", "а")
        search.misplacedLetters = ArrayList<String>()
        search.nonPresentLetters = ArrayList<String>()

        val retArraySize : Int = 2;

        given()
            .queryParams(mapOf("length" to 5, "page" to 1, "items" to 20))
            .header("X-Request-ID", "11111")
            .cookie("session_id", "1")
            .contentType(MediaType.APPLICATION_JSON)
            .body(search)
            .`when`()
            .post("/v1/crawler/letters")
            .then()
            .statusCode(200)
            .body("totalElements", equalTo(retArraySize))
            .body("words", Matchers.hasSize<String>(retArraySize))
            .body("words", Matchers.hasItems<String>("слива", "смола"))
    }

    @Test
    fun checkIncorrectInputParamsWrongLengthOrExactLetters() {
        val search = WordCriteria()
        search.exactLetters = listOf("с", "?", "?", "?", "а")
        search.misplacedLetters = ArrayList<String>()
        search.nonPresentLetters = ArrayList<String>()

        given()
            .queryParams(mapOf("length" to 6, "page" to 1, "items" to 20))
            .header("X-Request-ID", "11111")
            .cookie("session_id", "1")
            .contentType(MediaType.APPLICATION_JSON)
            .body(search)
            .`when`()
            .post("/v1/crawler/letters")
            .then()
            .statusCode(400)
            .body("message", `is`("Размер определяемого слова 6 не совпадает с длиной переданного слова"))
            .body("details", Matchers.hasItems<String>("с", "?", "а"))
    }

    @Test
    fun simpleSearchBy5LettersUsingMisplacedAndNonPresentLettersWithPositiveResults() {
        val search = WordCriteria()
        search.exactLetters = listOf("с", "?", "?", "?", "?")
        search.misplacedLetters = listOf("а????", "????в")
        search.nonPresentLetters = listOf("т", "б")

        val retArraySize : Int = 1;

        given()
            .queryParams(mapOf("length" to 5, "page" to 1, "items" to 20))
            .header("X-Request-ID", "11111")
            .cookie("session_id", "1")
            .contentType(MediaType.APPLICATION_JSON)
            .body(search)
            .`when`()
            .post("/v1/crawler/letters")
            .then()
            .statusCode(200)
            .body("totalElements", equalTo(retArraySize))
            .body("words", Matchers.hasSize<String>(retArraySize))
            .body("words", Matchers.hasItems<String>("слива"))
    }

}