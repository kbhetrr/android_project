package com.example.softwareproject.util

enum class Topic(val topic: Int) {
    OPERATINGSYSTEM(1),
    NETWORK(2),
    ALGORITHM(3);

    companion object {
        fun fromTopic(topic: Int): Topic {
            return entries.firstOrNull { it.topic == topic } ?: OPERATINGSYSTEM
        }
    }
}
