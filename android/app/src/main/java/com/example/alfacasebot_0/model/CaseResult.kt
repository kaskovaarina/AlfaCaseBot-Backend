package com.example.alfacasebot_0.model

data class CaseResult(
    val score: Double,
    val points: Double,
    val attemptNum: Int,
    val attemptCoef: Double,
    val audience: Double,
    val finance: Double,
    val proves: Double,
    val risks: Double,
    val sol: Double,
    val feedback: String
)