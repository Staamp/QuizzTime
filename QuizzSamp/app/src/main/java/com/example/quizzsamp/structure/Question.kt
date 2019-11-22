package com.example.quizzsamp.structure

import java.io.Serializable

/**
 * Classe question qui permet de stocké tous les éléments relatifs à une question.
 * C'est-à-dire la question, les propositons associées et l'indice parmis les propositions de la réponse à la question.
 */
class Question: Serializable {

    private var questionLabel: String = ""
    private var propositions : ArrayList<String> = ArrayList()
    private var indiceReponse : Int = 0

    /**
     * Constructeur d'une question
     *
     * @param question : la question
     * @param propositions : les propositions associées à la questions
     * @param indiceReponse : l'indice de la bonne réponse parmis les propositions associées à la question
     */
    constructor(question: String, propositions: ArrayList<String>, indiceReponse: Int) {
        this.questionLabel = question
        this.propositions = propositions
        this.indiceReponse = indiceReponse
    }

    /**
     * @return retourne la question
     */
    fun getQuestionLabel(): String {
        return this.questionLabel
    }

    /**
     * Changement de l'intitulé de la question
     *
     * @param s : la nouvelle question
     */
    fun setQuestionLabel(s: String) {
        this.questionLabel = s
    }

    /**
     * @return retourne la liste des propositions de la questions
     */
    fun getPropositions(): ArrayList<String>{
        return this.propositions
    }

    /**
     * @return retourne l'indice dde la bonne réponse parmis les propositons de la questions
     */
    fun getIndiceReponse(): Int{
        return this.indiceReponse
    }

    /**
     * Définis une nouvelle réponse à la question
     *
     * @param ind: l'indice de la nouvelle réponse à la question
     */
    fun setIndiceReponse(ind : Int){
        this.indiceReponse = ind
    }

    /**
     * Ajoute une propositions à la liste des propositions
     *
     * @param propo: la proposition à ajouter
     */
    fun addPropositions(propo: String) {
        propositions.add(propo)
    }

    /**
     * Retire une propositions à la liste des propositions
     *
     * @param propo: la proposition à retirer
     */
    fun removePropositions(propo: String) {
        propositions.remove(propo)
    }
}