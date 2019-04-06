package com.example.pagination.common

import io.reactivex.Scheduler

interface SchedulersProvider {
    fun io(): Scheduler
    fun computation(): Scheduler
    fun main(): Scheduler
}