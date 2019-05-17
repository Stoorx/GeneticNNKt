package util

interface Visitable {
    fun accept(v: Visitor)
}