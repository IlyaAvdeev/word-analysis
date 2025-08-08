package one.avdeev.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Root
import one.avdeev.entity.OjegovWord
import one.avdeev.repository.base.cookPredicates

@ApplicationScoped
class OjegovRepository : PanacheRepository<OjegovWord> {

    @PersistenceContext
    lateinit var entityManger: EntityManager

    fun findWord(
        wordSize: Int,
        nonPresentLetters: List<String>,
        exactLetters: List<String>,
        misplacedLetters: List<String>,
        pageIndex: Int = 0,
        pageSize: Int = 20
    ): List<OjegovWord> {
        val criteriaBuilder: CriteriaBuilder = entityManger.criteriaBuilder
        val criteriaQuery: CriteriaQuery<OjegovWord> = criteriaBuilder.createQuery(OjegovWord::class.java)
        val root: Root<OjegovWord> = criteriaQuery.from(OjegovWord::class.java)

        val allPredicates = cookPredicates(
            wordSize, nonPresentLetters,
            exactLetters, misplacedLetters, criteriaBuilder, root
        )

        val whereClause = criteriaBuilder.and(*allPredicates.toTypedArray())
        criteriaQuery.select(root).where(whereClause)
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get<String>("word")))

        val typedQuery = entityManger.createQuery(criteriaQuery)
            .setFirstResult(pageIndex * pageSize)
            .setMaxResults(pageSize)

        return typedQuery.resultList
    }

    fun totalMatchingWordCount(
        wordSize: Int,
        nonPresentLetters: List<String>,
        exactLetters: List<String>,
        misplacedLetters: List<String>
    ): Int {
        val criteriaBuilder: CriteriaBuilder = entityManger.criteriaBuilder

        val countQuery = criteriaBuilder.createQuery(Long::class.java)
        val countRoot = countQuery.from(OjegovWord::class.java)
        val allPredicates = cookPredicates(
            wordSize, nonPresentLetters,
            exactLetters, misplacedLetters, criteriaBuilder, countRoot
        )
        val countPredicates = criteriaBuilder.and(*allPredicates.toTypedArray())
        countQuery.select(criteriaBuilder.count(countRoot)).where(countPredicates)
        val totalCount = getEntityManager().createQuery(countQuery).singleResult

        return totalCount.toInt()
    }
}