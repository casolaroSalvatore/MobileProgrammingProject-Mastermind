# 🎮 Mastermind - Android App

A modern reimplementation of the classic *Mastermind* game, fully developed in **Kotlin** using **Jetpack Compose**, **Room**, and **DataStore**. The app allows for custom game setups, persistent history saving, and features a built-in solver to evaluate guesses intelligently.

---

## 🧩 Features

- 🎨 **Custom Game Settings**: Choose the number of colors, code length, maximum attempts, and whether duplicates are allowed.
- 🧠 **Smart Solver**: Displays how many secret code combinations remain compatible with your current game history.
- 💾 **Persistent Storage**:
  - Room database to store ongoing and finished games.
  - DataStore to manage user settings and app language.
- 🕹️ **Game Status Management**: Tracks whether the game is active, won, or lost (with secret code revealed).
- 🌍 **Multilingual Support**: Change the app’s language dynamically through settings.
