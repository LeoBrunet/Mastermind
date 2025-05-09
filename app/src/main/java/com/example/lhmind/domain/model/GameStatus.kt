package com.example.lhmind.domain.model

enum class GameStatus {
    INVITATION_SENT,
    INVITATION_CANCELED,
    WAITING_FOR_CODE_CREATION,
    WAITING_FOR_ATTEMPT,
    WAITING_FOR_FEEDBACK,
    WRONG_FEEDBACK,
    COMPLETED,
    ABANDONED
}

fun GameStatus.toDisplayString(isMaker: Boolean, isSender: Boolean): String =
    when (this) {
        GameStatus.INVITATION_SENT -> if (isSender) {
            "En attente de réponse à votre invitation"
        } else {
            "Invitation reçue, à vous de répondre"
        }

        GameStatus.INVITATION_CANCELED -> "La partie a été annulé"

        GameStatus.WAITING_FOR_CODE_CREATION -> if (isMaker) {
            "À vous de créer un code"
        } else {
            "En attente que l’adversaire crée son code"
        }

        GameStatus.WAITING_FOR_ATTEMPT -> if (isMaker) {
            "En attente d'une tentative de votre adversaire"
        } else {
            "À vous de proposer une tentative"
        }

        GameStatus.WAITING_FOR_FEEDBACK -> if (isMaker) {
            "À vous de donner un feedback"
        } else {
            "En attente du feedback de l’adversaire"
        }

        GameStatus.WRONG_FEEDBACK -> if (isMaker) {
            "Erreur dans votre feedback, corrigez-le"
        } else {
            "L’adversaire a fait une erreur de feedback"
        }

        GameStatus.COMPLETED -> "Partie terminée"
        GameStatus.ABANDONED -> "Partie abandonnée"
    }
