package com.example.lab33.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab33.data.LabRepository
import com.example.lab33.data.LabWork
import com.example.lab33.data.Subject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

// ViewModel відповідає за логіку екрану та підготовку даних для UI.
// Ми наслідуємося від ViewModel (а не AndroidViewModel), бо нам більше не потрібен Context тут.
// LabRepository передається в конструктор автоматично через Koin (Dependency Injection).
class MainViewModel(private val repository: LabRepository) : ViewModel() {

    init {
        // viewModelScope — це область видимості корутин, прив'язана до життя ViewModel.
        // Коли ViewModel знищується (наприклад, закривається екран), всі корутини тут скасовуються.
        // launch запускає корутину для виконання асинхронного коду (ініціалізації даних).
        viewModelScope.launch {
            repository.initDataIfNeeded()
        }
    }

    // Отримуємо Flow (потік даних) з репозиторію.
    // UI буде підписуватися на ці потоки (collectAsState) і автоматично оновлюватися.
    val subjects: Flow<List<Subject>> = repository.subjects
    val doneLabs: Flow<List<LabWork>> = repository.doneLabs

    fun getLabsForSubject(subjectId: Long): Flow<List<LabWork>> {
        return repository.getLabsForSubject(subjectId)
    }
    
    suspend fun getSubjectById(id: Long): Subject? {
        return repository.getSubjectById(id)
    }

    // Функція для оновлення статусу.
    // Викликається з UI (наприклад, при кліку на чекбокс).
    fun updateLabStatus(labId: Long, isDone: Boolean) {
        // Запускаємо корутину, бо запис у базу — це тривала операція.
        // Ми робимо це у фоні, щоб інтерфейс не "зависав".
        viewModelScope.launch {
            repository.updateLabStatus(labId, isDone)
        }
    }
}
