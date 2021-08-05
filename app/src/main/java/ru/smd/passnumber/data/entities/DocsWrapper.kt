package ru.smd.passnumber.data.entities

class DocsWrapper (val type:TypeDocs ,val typeItem:TypeItem,val docs: Docs?)

enum class TypeDocs(val type: Int) {
    Pts(1), Sts(2),Dk(3),DriverCard(4),Passport(5),CardCompany(6),ContractCredit(7),ContractCarriage(8)
}

enum class TypeItem(val type:Int){
    Add(0),Loaded(1)
}