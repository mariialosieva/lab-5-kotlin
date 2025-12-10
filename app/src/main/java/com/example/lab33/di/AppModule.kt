package com.example.lab33.di

import androidx.room.Room
import com.example.lab33.data.AppDatabase
import com.example.lab33.data.LabRepository
import com.example.lab33.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

// Визначаємо модуль Koin, де описуємо, як створювати наші об'єкти (залежності).
val appModule = module {

    // single { ... } означає, що цей об'єкт буде створений лише ОДИН раз (Singleton)
    // і буде жити протягом усього часу роботи програми.
    // Тут ми реєструємо базу даних AppDatabase.
    single {
        Room.databaseBuilder(
            androidContext(), // Отримуємо Context програми, який Koin підставляє сам
            AppDatabase::class.java,
            "lab_database"
        ).build()
    }

    // Реєструємо DAO (Data Access Object).
    // get() — це магічна функція Koin. Вона шукає в контейнері вже створений об'єкт потрібного типу.
    // У цьому випадку get() знайде AppDatabase (яку ми оголосили вище) і викличе в неї labDao().
    single { get<AppDatabase>().labDao() }

    // Реєструємо Репозиторій.
    // Він залежить від DAO. Koin автоматично знайде DAO (через get()) і передасть його в конструктор LabRepository.
    single { LabRepository(get()) }

    // viewModel { ... } означає, що цей об'єкт (ViewModel) буде створюватися КОЖЕН РАЗ,
    // коли він знадобиться (наприклад, при відкритті екрану або Activity).
    // Але він буде "переживати" повороти екрану завдяки механізмам Android ViewModel.
    // Koin автоматично знайде LabRepository (через get()) і передасть його в конструктор MainViewModel.
    viewModel { MainViewModel(get()) }
}
