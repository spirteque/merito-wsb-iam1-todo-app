# DoZrobix

## Autorzy

- Sylwia Banach
- Paweł Kruczek
- Eryk Kucharski
- Michał Nosko

Aplikacja zaliczeniowa z przedmiotu "Podstawy programowania na platformę Android".

## Cel projektu

Aplikacja DoZrobix jest prostą aplikacją typu ToDo, stworzoną w celu nauki nowoczesnych technologii Android (Jetpack Compose, MVVM, Firebase, Room oraz integracji API).

## Technologie

- Kotlin + Jetpack Compose
- Firebase Authentication
- Room (lokalna baza danych)
- Retrofit + JSONPlaceholder API
- Google Maps
- Hilt (dependency injection)
- MVVM Architecture

## Konfiguracja przed uruchomieniem

### 1. Firebase

1. Utwórz projekt w [Firebase Console](https://console.firebase.google.com)
2. Dodaj aplikację Android z package name: `com.example.todoapp`
3. Pobierz plik `google-services.json` i umieść go w katalogu `app/`
4. W Firebase Console włącz **Authentication → Sign-in method → Email/Password**

### 2. Google Maps (opcjonalne)

Bez klucza API mapa będzie szara, ale aplikacja działa poprawnie.

Jeśli chcesz włączyć mapę:
1. Utwórz klucz API w [Google Cloud Console](https://console.cloud.google.com)
2. Włącz **Maps SDK for Android**
3. W pliku `local.properties` dodaj: MAPS_API_KEY=twój_klucz_api
### 3. Uruchomienie

1. Otwórz projekt w Android Studio
2. Wgraj `google-services.json` do katalogu `app/`
3. Kliknij **Run**

## Funkcje aplikacji

- Rejestracja i logowanie przez Firebase Authentication
- Dodawanie, edytowanie, usuwanie zadań (Room)
- Pobieranie propozycji zadań z internetu (JSONPlaceholder API)
- Importowanie zadań z API do lokalnej bazy
- Ciemny/jasny motyw
- Mapa z markerem uczelni i bieżącą lokalizacją
- Kontakt przez email, telefon, przeglądarkę i mapy
- Navigation Drawer + Bottom Navigation

## Scenariusze testowe

- Rejestracja nowego konta i logowanie
- Dodanie, edycja i usunięcie zadania
- Oznaczenie zadania jako ukończone
- Importowanie zadania z API
- Odświeżenie listy z API
- Przełączenie ciemnego motywu w Ustawieniach
- Wylogowanie i ponowne logowanie
- Obrót ekranu podczas pracy
- Uruchomienie aplikacji bez połączenia z internetem
- Przyciski kontaktowe (email, telefon, strona, mapy)
- Przycisk "Pokaż mnie" na mapie (wymaga zgody na lokalizację)