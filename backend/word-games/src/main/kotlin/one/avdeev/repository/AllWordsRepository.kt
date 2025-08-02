package one.avdeev.repository
import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import one.avdeev.entity.AllWordsWord
import one.avdeev.entity.OjegovWord
import one.avdeev.error.InvalidInput

@ApplicationScoped
class AllWordsRepository : PanacheRepository<AllWordsWord> {

    @PersistenceContext
    lateinit var entityManger: EntityManager

    fun findWord(wordSize: Int,
                 nonPresentLetters: List<String>,
                 exactLetters: List<String>,
                 misplacedLetters: List<String>,
                 pageIndex: Int = 0,
                 pageSize: Int = 20): List<OjegovWord> {
        val misplacedLettersError = misplacedLetters.filter { it.length != wordSize }
        if (misplacedLettersError.isNotEmpty()) {
            throw InvalidInput("Размер определяемого слова - $wordSize", misplacedLettersError)
        }

        val nonPresentLettersErrored = nonPresentLetters.filter { it.length != 1 }
        if (nonPresentLettersErrored.isNotEmpty()) {
            throw InvalidInput("В качестве буквы отсутствующей в слове переданы неоднобуквенные значения", nonPresentLettersErrored)
        }

        if (exactLetters.isNotEmpty()) {
            if (exactLetters.size != wordSize) {
                throw InvalidInput("Размер определяемого слова $wordSize не совпадает с длиной переданного слова", exactLetters)
            }
        }

        val criteriaBuilder: CriteriaBuilder = entityManger.criteriaBuilder
        val criteriaQuery: CriteriaQuery<OjegovWord> = criteriaBuilder.createQuery(OjegovWord::class.java)
        val root: Root<OjegovWord> = criteriaQuery.from(OjegovWord::class.java)
        val allPredicates: MutableList<Predicate> = ArrayList();

        val predicateSize = criteriaBuilder.equal(criteriaBuilder.length(root.get("word")), wordSize)
        allPredicates.add(predicateSize)

        val no3dotsPredicate = criteriaBuilder.notLike(root.get("word"), "%…%")
        allPredicates.add(no3dotsPredicate)

        val existingLetters = misplacedLetters.map{it.toCharArray().filter{it != '?'}.joinToString("")}
        if (existingLetters.isNotEmpty()) {
            val predicateContainsLetters =
                existingLetters.map { criteriaBuilder.notLike(root.get("word"), "%$it%") }
            allPredicates.addAll(predicateContainsLetters)
        }

        if (exactLetters.isNotEmpty()) {
            val predicateWordLikePattern = criteriaBuilder.like(
                root.get("word"),
                exactLetters.map { if (it == "?") "_" else it }.joinToString("")
            )
            allPredicates.add(predicateWordLikePattern)
        }

        if (nonPresentLetters.isNotEmpty()) {
            val predicateNotContainsLetters =
                nonPresentLetters.map { criteriaBuilder.notLike(root.get("word"), "%$it%") }
            allPredicates.addAll(predicateNotContainsLetters)
        }

        if (misplacedLetters.isNotEmpty()) {
            val predicateMisplacedLetters = misplacedLetters.map { criteriaBuilder.like(root.get("word"), "$it") }
            allPredicates.addAll(predicateMisplacedLetters)
        }

        val whereClause = criteriaBuilder.and(*allPredicates.toTypedArray())
        criteriaQuery.select(root).where(whereClause)
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get<String>("word")))

        val typedQuery = entityManger.createQuery(criteriaQuery)
        /*
            //.setFirstResult(pageIndex * pageSize)
            //.setMaxResults(pageSize)

         */

        val results = typedQuery.resultList
/*
        val countQuery = criteriaBuilder.createQuery(Long::class.java)
        val countRoot = countQuery.from(OjegovWord::class.java)
        val countPredicates = criteriaBuilder.and(*allPredicates.toTypedArray())
        countQuery.select(criteriaBuilder.count(countRoot)).where(countPredicates)
        val totalCount = getEntityManager().createQuery(countQuery).singleResult
*/
        return results
    }
}