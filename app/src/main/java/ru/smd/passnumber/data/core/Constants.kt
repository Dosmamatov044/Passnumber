package ru.smd.passnumber.data.core

import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.slots.Slot

object Constants {
    val PHONE_RUS = arrayOf(
        PredefinedSlots.hardcodedSlot('+'),
        PredefinedSlots.hardcodedSlot('7'),
        PredefinedSlots.hardcodedSlot(' ').withTags(Slot.TAG_DECORATION),
        PredefinedSlots.digit(),
        PredefinedSlots.digit(),
        PredefinedSlots.digit(),
        PredefinedSlots.hardcodedSlot(' ').withTags(Slot.TAG_DECORATION),
        PredefinedSlots.digit(),
        PredefinedSlots.digit(),
        PredefinedSlots.digit(),
        PredefinedSlots.hardcodedSlot(' ').withTags(Slot.TAG_DECORATION),
        PredefinedSlots.digit(),
        PredefinedSlots.digit(),
        PredefinedSlots.hardcodedSlot(' ').withTags(Slot.TAG_DECORATION),
        PredefinedSlots.digit(),
        PredefinedSlots.digit())

    const val CAMERA_PERMISSION_CODE = 100
    const val CAMERA_REQUEST = 1888
    const val BASE_URL = "https://pass.su/api/"
    const val DATE_MASK = "d MMMM,EEEE"
    const val DATE_MASK_NUMBER="dd.MM.yyyy"
    const val API_KEY_YANDEX="37dcc9a6-642b-4ee0-b0cd-503200770027"
    const val MASK_REG_NUMBER="""^[укенхваросмтУКЕНХВАРОСМТ]{1}[0-9]{3}[укенхваросмтУКЕНХВАРОСМТ]{2}$"""
}