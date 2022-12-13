package com.shri.nycschools.network

import com.google.gson.GsonBuilder
import com.shri.nycschools.model.HighSchoolDTO
import com.shri.nycschools.model.SATScoresDTO
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

open class NycSchoolsAPI {
    interface Api {
        /**
         * Get list of High Schools in NYC
         */
        @GET("/resource/s3k6-pzi2.json")
        suspend fun getNycHighSchoolList(): Response<List<HighSchoolDTO>>

        /**
         * Get SAT Scores of the school
         * @param dbn - The unique dbn number of the school
         */
        @GET("/resource/f9bf-2cp4.json")
        suspend fun getSATScores(
            @Query("dbn") dbn: String
        ): Response<List<SATScoresDTO>>
    }

    open suspend fun getNycHighSchoolList(): Response<List<HighSchoolDTO>> {
        return webservice().getNycHighSchoolList()
    }
    open suspend fun getSATScores(dbn: String): Response<List<SATScoresDTO>> {
        return webservice().getSATScores(dbn)
    }

    companion object {
        private const val BASE_URL = "https://data.cityofnewyork.us"

        private fun webservice(): Api {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build().create(Api::class.java)
        }
    }
}