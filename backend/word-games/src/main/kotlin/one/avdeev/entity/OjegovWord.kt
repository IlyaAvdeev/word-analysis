package one.avdeev.entity

import io.quarkus.hibernate.orm.panache.PanacheEntity

class OjegovWord (val word:String, val explanation:String) : PanacheEntity() {
}