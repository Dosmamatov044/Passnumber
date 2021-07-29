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
}