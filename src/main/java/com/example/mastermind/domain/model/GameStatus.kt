package com.example.mastermind.domain.model

/* Stato corrente della partita:
   1) Playing  – partita in corso
   2) Won      – vittoria
   3) Lost     – sconfitta, con la sequenza segreta da mostrare */

interface GameStatus {
    object Playing : GameStatus
    object Won : GameStatus
    data class Lost(val secret: List<ColorPeg>) : GameStatus
}