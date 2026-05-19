package com.example.grameenlight.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.grameenlight.data.repository.PoleRepository
import com.example.grameenlight.model.Complaint
import com.example.grameenlight.model.Pole
import com.example.grameenlight.model.PoleStatus
import com.example.grameenlight.model.RepairStatus
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val repository: PoleRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isDarkMode = MutableStateFlow(true)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    private val _isTechnicianMode = MutableStateFlow(false)
    val isTechnicianMode: StateFlow<Boolean> = _isTechnicianMode

    // Filtered list of poles based on search query
    val filteredPoles: StateFlow<List<Pole>> = repository.getAllPoles()
        .combine(_searchQuery) { poles, query ->
            if (query.isBlank()) poles
            else poles.filter { 
                it.id.contains(query, ignoreCase = true) || 
                it.name.contains(query, ignoreCase = true) 
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val complaints: StateFlow<List<Complaint>> = repository.getAllComplaints()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Calculate energy based on base value + resolved reports
    val energySavedKWh: StateFlow<Float> = repository.getAllComplaints()
        .map { complaints ->
            val resolvedCount = complaints.count { it.currentRepairStatus == RepairStatus.RESOLVED }
            450f + (resolvedCount * 15.5f)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 450f)

    val personalImpactKWh: StateFlow<Float> = repository.getAllComplaints()
        .map { complaints ->
            val resolvedCount = complaints.count { it.currentRepairStatus == RepairStatus.RESOLVED }
            resolvedCount * 15.5f
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)

    init {
        viewModelScope.launch {
            repository.seedFromAssets()
        }
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun toggleTechnicianMode() {
        _isTechnicianMode.value = !_isTechnicianMode.value
    }

    fun resetDemoData() {
        viewModelScope.launch {
            repository.clearAllData()
            repository.seedFromAssets()
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun reportStatus(pole: Pole, newStatus: PoleStatus) {
        viewModelScope.launch {
            val complaintId = generateComplaintId(pole.id)
            repository.reportStatus(pole, newStatus, complaintId)
        }
    }


    private fun generateComplaintId(poleId: String): String {
        val timestamp = System.currentTimeMillis()
        val randomDigits = (100000..999999).random()
        return "${poleId}_${timestamp}_$randomDigits"
    }
}

class MainViewModelFactory(private val repository: PoleRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
