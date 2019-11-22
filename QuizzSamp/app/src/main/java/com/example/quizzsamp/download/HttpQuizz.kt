package com.example.quizzsamp.download

import android.os.AsyncTask
import com.example.quizzsamp.jeu.main.MainActivity
import com.example.quizzsamp.structure.Question
import com.example.quizzsamp.structure.Quiz

import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.BufferedReader
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Classe qui permet de télécharger et parser un fichier XML respectant la syntaxe des quizs.
 * Une fois parser, cette classe met les quizs téléchargés dans une structure quiz avec tous ses éléments respectifs
 */
internal class HttpQuizz(var ma: MainActivity) {
    var httpQuestionnaire: HttpQuizzs

    /**
     * Initialisations
     */
    init {
        httpQuestionnaire = HttpQuizzs()
    }

    /**
     * Exécutions
     */
    fun execute() {
        httpQuestionnaire.execute()
    }

    /**
     * Classe interne qui télécharge, parse et met dans une structure les quizs.
     */
    internal inner class HttpQuizzs : AsyncTask<Void, Void, List<Quiz>>() {
        private val quizzs = ArrayList<Quiz>()

        /**
         * Fonction qui télécharge le quiz, le parse et le met dans la structure quiz
         *
         * @param adresseURL : l'adresse URL des quizs à télécharger
         */
        private fun getPage(adresseURL: String) {

            val bufferedReader: BufferedReader? = null
            var urlConnection: HttpURLConnection? = null

            try {

                val url = URL(adresseURL)
                urlConnection = url.openConnection() as HttpURLConnection
                val responseCode = urlConnection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = urlConnection.inputStream
                    val dbf = DocumentBuilderFactory.newInstance()
                    val db = dbf.newDocumentBuilder()
                    val doc = db.parse(inputStream)
                    doc.documentElement.normalize() // normalisation du document

                    /**
                     * Récupération des noeuds dans le XML pour récuper les quizs
                     */
                    val quizzList = doc.getElementsByTagName("Quizz") // liste des quizs (encore à parser)
                    var i = 0
                    while (i < quizzList.length) {
                        val quizzLabel : String = quizzList.item(i).attributes.getNamedItem("type").nodeValue // nom du quiz
                        val quizzQuestionListe : NodeList = (quizzList.item(i) as Element).getElementsByTagName("Question") // liste des questions (encore à parser)
                        val questions = ArrayList<Question>() // liste de questions

                        var j = 0
                        while (j < quizzQuestionListe.length) {
                            val questionLabel = quizzQuestionListe.item(j).firstChild.nodeValue // label de la question
                            val indiceReponse = (quizzQuestionListe.item(j) as Element).getElementsByTagName("Reponse").item(0).attributes.getNamedItem("valeur").nodeValue // indice de la réponse
                            val propositions = ArrayList<String>() // liste de propositions
                            val questionsPropositionListe : NodeList = (quizzQuestionListe.item(j) as Element).getElementsByTagName("Proposition") // liste des propositions (encore à parser)

                            var k = 0
                            while (k < questionsPropositionListe.length) {
                                val proposition : String = questionsPropositionListe.item(k).firstChild.nodeValue // proposition de réponse à une question
                                //Ajout de la propositions et suppression des \t qui sont présent dans le xml
                                propositions.add(proposition.replace("\t", ""))
                                k += 1
                            }

                            //Ajout de la question dans la strcuture et dans la liste des questions du quiz
                            questions.add(
                                Question(
                                    questionLabel.replace(
                                        "\t",
                                        ""
                                    ), propositions, indiceReponse.toInt()
                                )
                            )
                            j += 1
                        }
                        //Ajout d'un quiz dans la liste des quiz existants
                        quizzs.add(Quiz(quizzLabel, questions))
                        i += 1
                    }
                }

                // En cas de problème, on récupère proprement les erreurs
            } catch (e: Exception) {
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close()
                    } catch (e: IOException) {
                    }

                }
                urlConnection?.disconnect()
            }
        }

        /**
         * @return retourne la liste des quizs téléchargés
         */
        override fun doInBackground(vararg params: Void): List<Quiz> {
            getPage("https://dept-info.univ-fcomte.fr/joomla/images/CR0700/Quizzs.xml")
            return quizzs
        }

        /**
         * L'action réalisé apres le téléchargement et le parsing.
         * On ajoute les quizzs dans notre activité principale
         *
         * @param quizzsXML la liste des quiz téléchargés
         */
        override fun onPostExecute(quizzsXML: List<Quiz>) {
            for (qz in quizzsXML) {
                ma.addQuizz(qz)
            }
        }
    }

}
