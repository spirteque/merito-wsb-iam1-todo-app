# Dokumentacja techniczna — DoZrobix

## Architektura

Aplikacja stosuje wzorzec **MVVM** (Model-View-ViewModel) z jedną Activity (Single Activity Architecture).

### Przepływ danych

```
UI (Screen) → ViewModel → Repository → Room / Retrofit / Firebase
                ↑                              |
                └──────── StateFlow ───────────┘
```

Ekrany obserwują `StateFlow` z ViewModelu przez `collectAsStateWithLifecycle()`.
ViewModel wywołuje metody Repository. Repository łączy dane z lokalnej bazy (Room) i zdalnego API (Retrofit).

---

## Warstwy aplikacji

### UI (`ui/`)
Ekrany Compose. Każdy ekran dostaje ViewModel przez `hiltViewModel()`.
Brak logiki biznesowej — tylko wyświetlanie stanu i przekazywanie zdarzeń.

### ViewModel
Przechowuje stan UI jako `StateFlow<UiState>`. Wywołuje Repository w `viewModelScope`.
Nie zna Androida (Activity, Context) — wyjątek: `MapViewModel` potrzebuje Context do `FusedLocationProviderClient`.

### Stan UI (UiState)
Każdy ViewModel udostępnia stan ekranu w postaci `StateFlow<UiState>`.
Model stanu UI:
- `Loading` — trwa operacja (np. pobieranie danych)
- `Success(data)` — poprawne dane gotowe do wyświetlenia
- `Error(message)` — błąd podczas operacji
Dzięki temu UI jest reaktywne i zawsze odzwierciedla aktualny stan aplikacji.

### Repository (`data/repository/`)
Jedyne miejsce które wie skąd pochodzą dane.
- `TaskRepositoryImpl` — łączy Room (lokalne) i Retrofit (zdalne propozycje)
- `AuthRepository` — wrapper na Firebase Authentication

### Obsługa offline i błędów
Aplikacja korzysta z modelu hybrydowego:
- Room pełni rolę lokalnego źródła danych (offline-first)
- Retrofit dostarcza dane z API (online)
- Firebase odpowiada wyłącznie za autoryzację
W przypadku braku internetu:
- dane z Room są nadal dostępne
- operacje sieciowe kończą się błędem, który jest propagowany do ViewModelu
Błędy są obsługiwane i mapowane na `UiState.Error`.

### Data layer (`data/local/`, `data/remote/`)
- **Room**: `TaskEntity` (tabela) + `TaskDao` (zapytania) + `TodoDatabase`
- **Retrofit**: `TodoApi` (endpoint) + `TodoDto` (struktura JSON)

### DI (`di/`)
Hilt dostarcza zależności. Cztery moduły:
- `DatabaseModule` — tworzy Room database i DAO
- `NetworkModule` — tworzy OkHttpClient, Retrofit, TodoApi
- `FirebaseModule` — dostarcza `FirebaseAuth.getInstance()`
- `RepositoryModule` — binduje `TaskRepositoryImpl` jako `TaskRepository`

### Odpowiedzialność źródeł danych
- Firebase Authentication → tylko logowanie i rejestracja użytkowników
- Room → lokalne przechowywanie zadań
- Retrofit (JSONPlaceholder API) → pobieranie przykładowych zadań z internetu

---

## Baza danych

**Tabela: `tasks`**

| Kolumna | Typ | Opis |
|---|---|---|
| id | INTEGER | Klucz główny, autoincrement |
| title | TEXT | Tytuł zadania |
| description | TEXT | Opis (domyślnie pusty) |
| isDone | INTEGER | 0 = nieukończone, 1 = ukończone |

Mapowanie: `TaskEntity` (Room) ↔ `Task` (domain model) — konwersja w `TaskRepositoryImpl`.

---

## Nawigacja

Single Activity + NavHost. Graf nawigacji:

```
Login ──→ Register
  │
  └──→ Home ←──┐
       Settings   │  (Bottom Navigation)
       Contact ───┘
       
       Map (Navigation Drawer)
```

Po wylogowaniu: `popUpTo(0)` czyści cały back stack i wraca do Login.

---

## Zewnętrzne zależności

| Biblioteka | Wersja | Zastosowanie |
|---|---|---|
| Jetpack Compose BOM | 2024.12.01 | UI |
| Navigation Compose | 2.8.5 | Nawigacja |
| Hilt | 2.53.1 | Dependency Injection |
| Room | 2.7.0-alpha11 | Lokalna baza danych |
| Retrofit | 2.11.0 | REST API |
| Firebase Auth | (BOM 33.7.0) | Uwierzytelnianie |
| Maps Compose | 6.2.1 | Google Maps |
| kotlinx.serialization | 1.7.3 | Parsowanie JSON |