package ru.smd.passnumber.data.entities

class DocsWrapper (val typeItem:TypeItem,val docs: Docs?)

enum class TypeItem(val type:Int){
    Add(0),Loaded(1)
}