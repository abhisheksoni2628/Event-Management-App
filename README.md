# Event Management App (Android)

An Android application built using **Jetpack Compose** that integrates with a remote Event Management API to display events, event details, and event locations on a map.

---

## 🚀 Features

### Discover Screen
- Paginated list of events
- Search events by name
- Pull-to-refresh support

### Event Details Screen
- Detailed event information
- Image carousel for event images

### Event Map Screen
- Google Maps integration
- Event markers displayed on the map
- Single marker selection at a time

---

## 🧠 Architecture

The app follows **MVVM with Clean Architecture principles**.

UI (Jetpack Compose)

↓

ViewModel

↓

UseCase

↓

Repository

↓

Remote API (Retrofit)

This approach ensures clean separation of concerns, scalability, and maintainability.

---

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Architecture**: MVVM + Use Cases
- **Dependency Injection**: Hilt
- **Networking**: Retrofit + Gson
- **Async Handling**: Kotlin Coroutines & Flow
- **Navigation**: Navigation Compose
- **Image Loading**: Coil
- **Maps**: Google Maps SDK + Maps Compose

---

## Author

**Abhishek Soni** - Android Developer
