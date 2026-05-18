# 🌿 Grameen-Light

**Android App Development using GenAI — Project #47**

Citizen-led Streetlight Audit App for rural villages.

---

## Setup Instructions

### 1. Firebase Setup (Required)
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project named `grameen-light`
3. Add an Android app with package name: `com.grameenlight.app`
4. Download the real `google-services.json`
5. Replace `app/google-services.json` with your downloaded file
6. Enable **Firestore Database** in the Firebase console (Start in test mode)

### 2. Android Studio
- Open Android Studio (Hedgehog / Iguana or later)
- File → Open → select the `GrameenLight/` folder
- Wait for Gradle sync to complete
- Run on emulator (API 26+) or physical device

### 3. Gradle Wrapper Jar
The `gradle-wrapper.jar` binary is not included (binary file).
Run this once to generate it:
```bash
gradle wrapper --gradle-version 8.4
```
Or Android Studio will auto-download it on first sync.

---

## Architecture

```
MVVM + Repository Pattern + Hilt DI
├── UI Layer       (Jetpack Compose + Material3)
├── ViewModel      (PoleViewModel — state holder)
├── Repository     (PoleRepository — offline-first)
├── Remote         (FirestoreDataSource — Firebase)
└── Local          (Room DB — offline cache)
```

## Features
- 🗺 **Pole Map** — Color-coded interactive village map (Green/Red/Amber)
- 📋 **Quick Report** — One-tap pole status reporting
- 🎫 **Complaint ID** — Auto-generated unique IDs (GRL-XXXXXX)
- 🔥 **Firebase Sync** — Real-time Firestore updates
- 🔧 **Repair Tracker** — Pending / Assigned / Fixed status
- ⚡ **Energy Goal** — Monthly kWh & ₹ savings dashboard
- 🌙 **Dark/Light Mode** — Toggle between night/day audit modes
- 📦 **Offline First** — Room DB ensures app works without internet

## Package Structure
```
com.grameenlight.app
├── data/
│   ├── local/         (Room: DB, DAO, Entity)
│   ├── remote/        (Firestore data source)
│   └── repository/    (PoleRepository)
├── di/                (Hilt AppModule)
├── model/             (Domain models)
├── navigation/        (NavGraph, AppNavHost)
├── ui/
│   ├── components/    (StatsBar, ReportBottomSheet)
│   ├── screens/       (PoleMapScreen, TrackerScreen, EnergyScreen)
│   └── theme/         (Color, Theme, Typography)
├── utils/             (Mappers, EnergyCalculator, SampleData, IDGenerator)
└── viewmodel/         (PoleViewModel)
```
