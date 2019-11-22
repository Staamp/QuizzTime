package com.example.quizzsamp.jeu.main

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.example.quizzsamp.structure.Question
import com.example.quizzsamp.structure.Quiz
import com.example.quizzsamp.R
import com.example.quizzsamp.download.HttpQuizz
import com.example.quizzsamp.jeu.createEditQuizz.CreateEditQuizzActivity
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Activité principale qui permet de télécharger, jouer, créer et éditer des quizs.
 */
class MainActivity : AppCompatActivity() {

    internal lateinit var vueQuiz: RecyclerView
    internal lateinit var adapter: RecyclerView.Adapter<*>
    internal lateinit var layoutManager: RecyclerView.LayoutManager

    private lateinit var quizz: ArrayList<Quiz>
    internal lateinit var adap: AdapterMainActivity

    private var pressionBtnDownload: Int = 0

    /**
     * Lorsque que cette activité est appelé pour la premièr fois, on arrive dans cette fonction.
     *
     * Cette fonction initialise les éléments sur l'activité principale.
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT

        /**
         * Téléchargement et affichage des quizs téléchargés.
         */
        download.setOnClickListener {
            if (pressionBtnDownload == 0) {
                Toast.makeText(this, "Téléchargement...", Toast.LENGTH_SHORT).show()
                val httpQuizzXML = HttpQuizz(this)
                httpQuizzXML.execute()
                pressionBtnDownload++
            } else {
                Toast.makeText(this, "Les quizzs ont déjà été téléchargés.", Toast.LENGTH_SHORT).show()
            }
        }

        /**
         * Redirection sur une nouvelle activité pour créer des quizs.
         */
        createQuizz.setOnClickListener {
            Toast.makeText(this, "Menu création", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, CreateEditQuizzActivity::class.java)

            val qzReponse1 = "Vrai"
            val qzReponse2 = "Faux"
            val qzPropositionReponse : ArrayList<String> = ArrayList()
            qzPropositionReponse.add(qzReponse1)
            qzPropositionReponse.add(qzReponse2)
            val qzStructQuest =
                Question("nvQuestion", qzPropositionReponse, 0)
            val qzQuestion : ArrayList<Question> = ArrayList()
            qzQuestion.add(qzStructQuest)
            val qzStruct = Quiz("nvQuizz", qzQuestion)

            intent.putExtra("Qz", qzStruct)
            startActivityForResult(intent, 2)

        }

        layoutManager = LinearLayoutManager(this)
        vueQuiz = findViewById<RecyclerView>(R.id.vue_quiz)
        quizz = ArrayList()
        vueQuiz.layoutManager = layoutManager
        adap = AdapterMainActivity(this, quizz)
        vueQuiz.adapter = adap

        updateQuizz()
    }

    /**
     * Fonction qui récupère les données d'une activité lancé par cette activité
     *
     * @param requestCode : le code de retour
     * @param resultCode : le code mis dans l'autre activité pour retrouver les données résultats
     * @param data : les données reçues
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 2) {
            //On crée le quizz reçu dans l'intent
            if (resultCode == 2) {
                addQuizz(data!!.extras!!.getSerializable("createQuiz") as Quiz)
            }
            //On supprime le quizz reçu dans l'intent
            if (resultCode == 3) {
                removeQuizz(data!!.extras!!.getSerializable("createQuiz") as Quiz)
            }
            //On met à jour notre vue (notre liste de quizz)
            updateQuizz()
        }
    }

    /**
     * Fonction qui ajoute des quizzs dans notre liste de quiz
     *
     * @param qz : le quizz à ajouter
     */
    fun addQuizz(qz: Quiz) {
        quizz.add(qz)
        updateQuizz()
    }

    /**
     * Fonction qui retire des quizzs dans notre liste de quiz
     *
     * @param qz : le quizz à ajouter
     */
    fun removeQuizz(qz: Quiz) {
        quizz.remove(qz)
    }

    /**
     * Fonction qui met à jour notre vue
     */
    fun updateQuizz() {
        adap.setQuizz(quizz)
        adap.notifyDataSetChanged()
    }
}


