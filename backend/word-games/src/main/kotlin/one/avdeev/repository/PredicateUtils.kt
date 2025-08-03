package one.avdeev.repository

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root

fun <WordEntity> sizePredicateComposer(
    criteriaBuilder: CriteriaBuilder,
    root: Root<WordEntity>,
    wordSize: Int,
    allPredicates: MutableList<Predicate>
) {
    val predicateSize = criteriaBuilder.equal(criteriaBuilder.length(root.get("word")), wordSize)
    allPredicates.add(predicateSize)
}

fun<WordEntity> existingLettersPredicateComposer(
    misplacedLetters: List<String>,
    criteriaBuilder: CriteriaBuilder,
    root: Root<WordEntity>,
    allPredicates: MutableList<Predicate>
) {
    val existingLetters =
        misplacedLetters.map { (it.toCharArray().filter { it != '?' }).distinct() }.flatten().distinct()
    if (existingLetters.isNotEmpty()) {
        val predicateContainsLetters =
            existingLetters.map { criteriaBuilder.like(root.get("word"), "%$it%") }
        allPredicates.addAll(predicateContainsLetters)
    }
}

fun<WordEntity> exactLettersPredicateComposer(
    exactLetters: List<String>,
    criteriaBuilder: CriteriaBuilder,
    root: Root<WordEntity>,
    allPredicates: MutableList<Predicate>
) {
    if (exactLetters.isNotEmpty()) {
        val predicateWordLikePattern = criteriaBuilder.like(
            root.get("word"),
            exactLetters.map { if (it == "?") "_" else it }.joinToString("")
        )
        allPredicates.add(predicateWordLikePattern)
    }
}

fun<WordEntity> nonPresentLettersPredicateComposer(
    nonPresentLetters: List<String>,
    criteriaBuilder: CriteriaBuilder,
    root: Root<WordEntity>,
    allPredicates: MutableList<Predicate>
) {
    if (nonPresentLetters.isNotEmpty()) {
        val predicateNotContainsLetters =
            nonPresentLetters.map { criteriaBuilder.notLike(root.get("word"), "%$it%") }
        allPredicates.addAll(predicateNotContainsLetters)
    }
}

fun<WordEntity> misplacedLettersPredicateComposer(
    misplacedLetters: List<String>,
    criteriaBuilder: CriteriaBuilder,
    root: Root<WordEntity>,
    allPredicates: MutableList<Predicate>
) {
    if (misplacedLetters.isNotEmpty()) {
        val predicateMisplacedLetters =
            misplacedLetters.map { it.replace('?', '_') }.map { criteriaBuilder.notLike(root.get("word"), "$it") }
        allPredicates.addAll(predicateMisplacedLetters)
    }
}

fun<WordEntity> ellipsisSignExcluderPrepdicateComposer(
    criteriaBuilder: CriteriaBuilder,
    root: Root<WordEntity>,
    allPredicates: MutableList<Predicate>
) {
    val no3dotsPredicate = criteriaBuilder.notLike(root.get("word"), "%…%")
    allPredicates.add(no3dotsPredicate)
}




