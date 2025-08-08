package one.avdeev.entity

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*

@Entity(name = "Ojegov")
data class OjegovWord (@Id
                       @GeneratedValue(strategy = GenerationType.IDENTITY)
                       val id : Long,
                       val word:String,
                       val explanation:String) : PanacheEntityBase() {
    constructor () : this(-1, "","") {}
}