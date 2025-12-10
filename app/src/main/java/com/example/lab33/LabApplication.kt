package com.example.lab33

import android.app.Application
import com.example.lab33.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

// Клас Application — це точка входу в програму. Він створюється найпершим.
// Ми використовуємо його для ініціалізації глобальних бібліотек, таких як Koin.
class LabApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // startKoin — це функція запуску Koin. Вона налаштовує контейнер залежностей.
        startKoin {
            // androidLogger() — підключає логування Koin (щоб бачити помилки в Logcat).
            androidLogger()

            // androidContext(this@LabApplication) — передає контекст нашої програми в Koin.
            // Це потрібно, щоб Koin міг створювати об'єкти, які потребують Context (наприклад, Room Database).
            androidContext(this@LabApplication)

            // modules(...) — завантажує наші модулі з описами залежностей.
            // Тут ми передаємо наш appModule, який ми створили в файлі AppModule.kt.
            modules(appModule)
        }
    }
}
