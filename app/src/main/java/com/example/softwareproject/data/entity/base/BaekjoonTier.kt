package com.example.softwareproject.data.entity.base

enum class BaekjoonTier(val tierValue: Int, val displayName: String) {
    BRONZE_5(1, "Bronze V"),
    BRONZE_4(2, "Bronze IV"),
    BRONZE_3(3, "Bronze III"),
    BRONZE_2(4, "Bronze II"),
    BRONZE_1(5, "Bronze I"),

    SILVER_5(6, "Silver V"),
    SILVER_4(7, "Silver IV"),
    SILVER_3(8, "Silver III"),
    SILVER_2(9, "Silver II"),
    SILVER_1(10, "Silver I"),

    GOLD_5(11, "Gold V"),
    GOLD_4(12, "Gold IV"),
    GOLD_3(13, "Gold III"),
    GOLD_2(14, "Gold II"),
    GOLD_1(15, "Gold I"),

    PLATINUM_5(16, "Platinum V"),
    PLATINUM_4(17, "Platinum IV"),
    PLATINUM_3(18, "Platinum III"),
    PLATINUM_2(19, "Platinum II"),
    PLATINUM_1(20, "Platinum I"),

    DIAMOND_5(21, "Diamond V"),
    DIAMOND_4(22, "Diamond IV"),
    DIAMOND_3(23, "Diamond III"),
    DIAMOND_2(24, "Diamond II"),
    DIAMOND_1(25, "Diamond I"),

    RUBY_5(26, "Ruby V"),
    RUBY_4(27, "Ruby IV"),
    RUBY_3(28, "Ruby III"),
    RUBY_2(29, "Ruby II"),
    RUBY_1(30, "Ruby I"),

    MASTER(31, "Master"),
    LEGEND(32, "Legend");

    companion object {
        fun fromTierValue(value: Int): BaekjoonTier {
            return values().firstOrNull { it.tierValue == value } ?: BRONZE_5
        }
    }
}