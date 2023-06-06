package com.agrafast.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agrafast.domain.UIState
import com.agrafast.data.firebase.model.Plant
import com.agrafast.domain.repository.PlantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
//class HomeViewModel @Inject constructor(
//  private val plantRepository: PlantRepository
//) : ViewModel() {
//
//
//}
