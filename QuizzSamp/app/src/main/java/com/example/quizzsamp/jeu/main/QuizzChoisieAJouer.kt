package com.example.quizzsamp.jeu.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_quiz_play.*
import java.util.ArrayList
import android.widget.LinearLayout
import android.widget.Toast
import com.example.quizzsamp.structure.Question
import com.example.quizzsamp.structure.Quiz
import com.example.quizzsamp.R

/**
 * Activité qui gère le déroulement d'un quiz.
 * C'est ici que les quizs démarrent et que les questions s'enchainent.
 * A la fin du quiz, on est redirigé vers l'activité principale avec le score de la partie
 */
class QuizzChoisieAJouer : AppCompatActivity() {

    private var questIndice: Int = 0
    private var quizScore: Int = 0

    private lateinit var quizQuestions: ArrayList<Question>
    private lateinit var questName: TextView
    private lateinit var quizzName: String
    private lateinit var quizActuel: Quiz

    private var ma: MainActivity? = null

    /**
     * Lorsque que cette activité est appelé pour la premièr fois, on arrive dans cette fonction.
     *
     * Cette fonction initialise le défilemment des questions.
     * On commence par récupérer avec quelle vue on va intéragir, ici activity_quiz_play.
     * On récupère le TextView pour afficher la question et on intialise les variables nécessaires.
     * Ensuite, on récupère le quizz choisie par l'utilisateur avec un intent
     * On intialise le nom de quizz et on récupère les questions.
     * On peut ensuite afficher les questions du quizz choisie.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_play)

        questName = findViewById<View>(R.id.questionName) as TextView
        questIndice = 0
        quizScore = 0

        val i = intent
        this.quizActuel = i.getSerializableExtra("quizzChoisieParLUtilsiateur") as Quiz

        this.quizQuestions = quizActuel.getQuizQuestionListe()
        this.quizzName = quizActuel.getQuizLabel()
        afficherQuestionSuivante(0)
    }

    /**
     * Cette fonction permet d'afficher l'intitulé d'une question et ses propositions associées.
     *
     * @param indice : indice de la question à afficher
     */
    private fun afficherQuestionSuivante(indice: Int) {
        if(indice >= quizActuel.getQuizQuestionListe().size){
            return
        }
        //linearlayout pour les réponses
        questionListePropositions.setPadding(30, 4, 30, 2)
        questionListePropositions.removeAllViews()
        //Propositions de réponses stockées dans notre structure
        val questionListePropositionsXML = quizActuel.getQuizQuestionListe()[indice].getPropositions()
        questionName.text = quizActuel.getQuizQuestionListe()[indice].getQuestionLabel()

        //Création des boutons de propositions en fonction du nombre de celle-ci
        for (i in 0 until questionListePropositionsXML.size) {
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            val btn = Button(this)
            btn.id = i
            btn.text = questionListePropositionsXML[i]
            btn.setBackgroundResource(R.drawable.button_shape)
            btn.setPadding(0,2,0,2)

            val tv = TextView(this)
            questionListePropositions.addView(tv, params)
            questionListePropositions.addView(btn, params)
            questionListePropositions.setPadding(30, 4, 30, 4)

            val btnReponse = findViewById<View>(btn.id) as Button
            //Listener sur tous les boutons
            btnReponse.setOnClickListener(View.OnClickListener {
                //Vérification de la réponse donnée par l'utilisateur
                verifieReponseChoisie(i,indice)
            })
        }
    }

    /**
     * Cette fonction vérifie si la réponse donnée par l'utilisateur à la question est la bonne.
     *
     * @param indiceReponse : indice de la réponse saisie
     * @param indice : indice de la question actuelle
     */
    private fun verifieReponseChoisie(indiceReponse:Int, indice: Int){
        val nbPropositionTotal = quizActuel.getQuizQuestionListe().size
        questIndice++

        //Notification du score et si la réponse est juste via un toast
        if(quizActuel.getQuizQuestionListe()[indice].getIndiceReponse() == indiceReponse + 1){
            quizScore += 1
            Toast.makeText(this, "Bien joué champion ! Ton score : $quizScore", Toast.LENGTH_SHORT).show()
        } else{
            quizScore += 0
            Toast.makeText(this, "Tu peux mieux faire. Ton score : $quizScore", Toast.LENGTH_SHORT).show()
        }

        //Passage à la question suivante
        if(indice < quizActuel.getQuizQuestionListe().size){
            afficherQuestionSuivante((questIndice))
        }

        //Message en fin de quiz, pour notifié le score total à l'utilisateur et redirection vers l'activité principale
        if (questIndice == nbPropositionTotal) {
            if (quizScore == nbPropositionTotal) {
                Toast.makeText(this, "Tu as tout juste, bravo ! Ton score : $quizScore/$nbPropositionTotal", Toast.LENGTH_SHORT).show()
            }
            if (quizScore >= (nbPropositionTotal / 2) && quizScore != nbPropositionTotal) {
                Toast.makeText(this, "T'as la moyenne, c'est bien ! Ton score : $quizScore/$nbPropositionTotal", Toast.LENGTH_SHORT).show()
            }
            if (quizScore <= (nbPropositionTotal / 2) && quizScore != 0) {
                Toast.makeText(this, "Tu n'as pas la moyenne, tu dois réviser ! Ton score : $quizScore/$nbPropositionTotal", Toast.LENGTH_SHORT).show()
            }
            if (quizScore == 0) {
                Toast.makeText(this, "On touche le fond... Ton pitoyable score : $quizScore/$nbPropositionTotal", Toast.LENGTH_SHORT).show()
            }
            ma?.updateQuizz()
            quizActuel.setScoreJoueur(quizScore)
            this.finish()
        }

    }
}
