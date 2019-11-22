package com.example.quizzsamp.jeu.createEditQuizz

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_create_quizz.*
import android.view.View
import com.example.quizzsamp.structure.Question
import com.example.quizzsamp.structure.Quiz
import com.example.quizzsamp.R

/**
 * Classe qui nous permet de créer et éditer des quizzs
 */
class CreateEditQuizzActivity : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    internal lateinit var adapterCreateQuizz: AdpaterCreateQuizz
    private lateinit var quizz: Quiz
    private lateinit var mTextE : MajTextEntree

    inner class MajTextEntree : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            quizz.setQuizLabel(s.toString())
        }
    }

    /**
     * Lorsque que cette activité est appelé pour la premièr fois, on arrive dans cette fonction.
     *
     * Cette fonction initialise les éléments sur l'activité principale.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_quizz)



        linearLayoutManager = LinearLayoutManager(this)
        qzRecyclerViewQuest.layoutManager = linearLayoutManager

        //Récupère le quizz passé dans l'intent
        val i = intent
        this.quizz = i.getSerializableExtra("Qz") as Quiz

        adapterCreateQuizz = AdpaterCreateQuizz(this, quizz)
        qzRecyclerViewQuest.adapter = adapterCreateQuizz


        var qzName : EditText  = findViewById<EditText>(R.id.qzName)
        if (quizz.getQuizLabel() == null) {
            qzName.setText("nom du quizz")
            //Ecoute pour supprimer le text au moment d'un clique de l'utilisateur
            qzName.setOnClickListener {
                qzName?.text?.clear()
            }
        } else {
            qzName.setText(quizz.getQuizLabel())
        }

        //Ecoute pour supprimer le text au moment d'un clique de l'utilisateur
        qzName.setOnClickListener(View.OnClickListener {
            (qzName as EditText).text.clear()
        })

        //Créer le quizz et retourner le résultat à l'activité principale
        qzCreate.setOnClickListener {
            Toast.makeText(this, "Création du quizz.", Toast.LENGTH_SHORT).show()
            setResult(2, Intent().putExtra("createQuiz", quizz))
            finish()
        }

        //Supprimer le quizz et retourner le résultat à l'activité principale
        qzDelete.setOnClickListener {
            Toast.makeText(this, "Suppression du quizz.", Toast.LENGTH_SHORT).show()
            setResult(3, Intent().putExtra("createQuiz", quizz))
            finish()
        }

        //Annuler le quizz
        qzCancel.setOnClickListener {
            Toast.makeText(this, "Annulation des modifications réalisées.", Toast.LENGTH_SHORT).show()
            finish()
        }

        //Ajouter une question dans le quizz
        qzAddQuestion.setOnClickListener {
            Toast.makeText(this, "Ajout d'une question.", Toast.LENGTH_SHORT).show()
            if (quizz.getQuizQuestionListe().size < 6) {
                var newProps = ArrayList<String>()
                newProps.add("proposition1")
                newProps.add("proposition2")
                quizz.addQuestion(quizz,
                    Question("nvQuestion", newProps, 1)
                )
                adapterCreateQuizz.notifyDataSetChanged()
            }
        }
    }
}