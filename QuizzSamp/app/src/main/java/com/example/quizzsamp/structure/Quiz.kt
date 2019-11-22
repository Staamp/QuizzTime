package com.example.quizzsamp.structure

import java.util.ArrayList
import java.io.Serializable

/**
 * Classe quiz qui permet de stocké tous les éléments relatifs à un quiz.
 * C'est-à-dire le nom du quiz, une liste de questions et le score du joueur.
 */
class Quiz : Serializable{

    private var  quizLabel: String = ""
    private var quizQuestionsListe : ArrayList<Question>
    private var quizScoreJoueur : Int = 0

    /**
     * Constructeur d'un quiz
     *
     * @param qzLabel : nom du quiz
     * @param qzQuestList : liste de questions du quiz
     */
    constructor(qzLabel:String, qzQuestList: ArrayList<Question>) {
        this.quizLabel = qzLabel
        this.quizQuestionsListe = qzQuestList
    }

    /**
     * Ajout d'une questions dans un quiz
     *
     * @param qz : le quiz dans lequel on veut ajouter une question
     * @param question : la question (de type Question) à ajouter dans le quiz
     * @param
     */
    fun addQuestion(qz: Quiz, question: Question): Quiz {
        qz.quizQuestionsListe.add(
            Question(
                question.getQuestionLabel(),
                question.getPropositions(),
                question.getIndiceReponse()
            )
        )
        return qz
    }

    /**
     * Mettre à jour le score du joueur
     *
     * @param scoreJoueur : le nouveau score
     */
    fun setScoreJoueur(scoreJoueur : Int) {
        this.quizScoreJoueur = scoreJoueur
    }

    /**
     * @return retourne le score du joueur sur ce quiz
     */
    fun getQuizScoreJoueur(): Int {
        return this.quizScoreJoueur
    }

    /**
     * @return retourne le nom du quiz
     */
    fun getQuizLabel(): String {
        return this.quizLabel
    }

	/**
     * Change le nom du quizz
     */
    fun setQuizLabel(s: String) {
        this.quizLabel = s
    }

    /**
     * @return retourne la liste de question du quiz
     */
    fun getQuizQuestionListe(): ArrayList<Question>{
        return this.quizQuestionsListe
    }
}
