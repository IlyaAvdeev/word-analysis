package one.avdeev.repository.base

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import one.avdeev.error.InvalidInput

fun <WordEntity> sizePredicateComposer(
    criteriaBuilder: CriteriaBuilder,
    root: Root<WordEntity>,
    wordSize: Int,
    allPredicates: MutableList<Predicate>
) {
    val predicateSize = criteriaBuilder.equal(criteriaBuilder.length(root.get("word")), wordSize)
    allPredicates.add(predicateSize)
}

fun<WordEntity> matchingLettersPredicateComposer(
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

fun<WordEntity> cookPredicates(
    wordSize: Int,
    nonPresentLetters: List<String>,
    exactLetters: List<String>,
    misplacedLetters: List<String>,
    criteriaBuilder: CriteriaBuilder,
    root: Root<WordEntity>
): List<Predicate> {
    val misplacedLettersError = misplacedLetters.filter { it.length != wordSize }
    if (misplacedLettersError.isNotEmpty()) {
        throw InvalidInput("Размер определяемого слова - $wordSize", misplacedLettersError)
    }

    val nonPresentLettersErrored = nonPresentLetters.filter { it.length != 1 }
    if (nonPresentLettersErrored.isNotEmpty()) {
        throw InvalidInput(
            "В качестве буквы отсутствующей в слове переданы неоднобуквенные значения",
            nonPresentLettersErrored
        )
    }

    if (exactLetters.size != wordSize) {
        throw InvalidInput(
            "Размер определяемого слова $wordSize не совпадает с длиной переданного слова",
            exactLetters
        )
    }
    //--------------------------------------

    val allPredicates: MutableList<Predicate> = ArrayList();

    sizePredicateComposer(criteriaBuilder, root, wordSize, allPredicates)

    matchingLettersPredicateComposer(misplacedLetters, criteriaBuilder, root, allPredicates)

    exactLettersPredicateComposer(exactLetters, criteriaBuilder, root, allPredicates)

    nonPresentLettersPredicateComposer(nonPresentLetters, criteriaBuilder, root, allPredicates)

    misplacedLettersPredicateComposer(misplacedLetters, criteriaBuilder, root, allPredicates)

    ellipsisSignExcluderPrepdicateComposer(criteriaBuilder, root, allPredicates)

    return allPredicates
}