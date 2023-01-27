package com.noteapp.core.ext

fun <T> Set<T>.addButIfExistRemove(elem: T): Set<T> {
    return toMutableSet().apply {
        if (elem in this) {
            remove(elem)
        } else {
            add(elem)
        }
    }
}
