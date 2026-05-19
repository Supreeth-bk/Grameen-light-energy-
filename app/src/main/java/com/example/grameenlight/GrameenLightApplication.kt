package com.example.grameenlight

import android.app.Application
import androidx.room.Room
import com.example.grameenlight.data.local.AppDatabase
import com.example.grameenlight.data.repository.PoleRepository

class GrameenLightApplication : Application() {

    // Database and Repository instances
    val database by lazy { 
        Room.databaseBuilder(this, AppDatabase::class.java, "grameen_light_db")
            .fallbackToDestructiveMigration()
            .build() 
    }
    
    val repository by lazy { 
        PoleRepository(this, database.poleDao(), database.complaintDao()) 
    }
}
