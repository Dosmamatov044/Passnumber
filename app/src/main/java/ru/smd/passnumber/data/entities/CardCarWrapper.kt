package ru.smd.passnumber.data.entities

class CardCarWrapper (val type:Type,val passData: PassData)

enum class Type(val type: Int) {
    Green(0), Yellow(1),Red(2),Blue(3),Gray(4)
}