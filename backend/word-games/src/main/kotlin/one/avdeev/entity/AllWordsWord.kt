package one.avdeev.entity

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*

@Entity(name = "all_words")
data class AllWordsWord (@Id
                         @GeneratedValue(strategy = GenerationType.IDENTITY)
                         val id : Long,
                         val word:String) : PanacheEntityBase() {
    constructor () : this(-1, "") {}
}