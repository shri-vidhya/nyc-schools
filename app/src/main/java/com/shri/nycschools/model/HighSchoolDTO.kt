package com.shri.nycschools.model

data class HighSchoolDTO(
    val dbn: String,
    val school_name: String,
    val city: String,
    val overview_paragraph: String,
    val location: String,
    val phone_number: String,
    val website: String,
    val total_students: Int,
    val extracurricular_activities: String,
    val psal_sports_boys: String,
    val psal_sports_girls: String,
    val school_sports: String,
    val academicopportunities1: String,
    val start_time: String,
    val end_time: String,
    val grades2018: String,
    val graduation_rate: Double,
    val attendance_rate: Double,
    val pct_stu_enough_variety: Double,
    val college_career_rate: Double,
    val pct_stu_safe: Double,
    val girls: Int,
    val boys: Int,
    val international: Int,
    val specialized: Int,
    val transfer: Int,
    val ptech: Int,
    val earlycollege: Int,
    val school_10th_seats: Int,
): java.io.Serializable {
    override fun hashCode(): Int {
        return dbn.hashCode()
    }
}

data class SATScoresDTO (
    val dbn: String,
    val school_name: String,
    val num_of_sat_test_takers: String,
    val sat_critical_reading_avg_score: String,
    val sat_math_avg_score: String,
    val sat_writing_avg_score: String,
)
