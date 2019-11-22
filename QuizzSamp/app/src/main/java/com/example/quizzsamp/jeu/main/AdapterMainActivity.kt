package com.example.quizzsamp.jeu.main

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList
import android.app.Activity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.quizzsamp.structure.Quiz
import com.example.quizzsamp.R
import com.example.quizzsamp.jeu.createEditQuizz.CreateEditQuizzActivity


/**
 * Classe qui nous permet d'adpater les quizs dans notre activité principale
 */
class AdapterMainActivity : RecyclerView.Adapter<AdapterMainActivity.ViewHolder> {
    private var quizList: List<Quiz>? = null
    internal var ctx: Context

    /**
     * Fournir une référence aux vues pour chaque élément de données
     * Les éléments de données complexes peuvent nécessiter plus d'une vue par élément, et
     * vous donnez accès à toutes les vues d'un élément de données dans un détenteur de vue
     */
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        // nous avons comme donée un bouton et un score
        var quizButton: Button = v.findViewById<View>(R.id.quizzButton) as Button
        var score: TextView = v.findViewById<View>(R.id.quizzScore) as TextView
        var edit: Button = v.findViewById<View>(R.id.quizzEdit) as  Button
    }

    /**
     * Constructeur par défaut
     *
     * @param context : l'activité principale
     */
    constructor(context: MainActivity) {
        quizList = ArrayList()
        this.ctx = context
    }

    /**
     * Constructeur de l'adpatateur de l'activité principale
     *
     * @param context : l'activité principale
     * @param qz : la liste de quiz
     */
    constructor(context: MainActivity, qz: List<Quiz>) {
        this.quizList = qz
        this.ctx = context
    }

    /**
     * Créer de nouvelles vues (invoquées par le gestionnaire de disposition)
     *
     * @param parent : un ViewGroup
     * @param viewType : un Int
     * @return retourne ViewHolder(v), c'est ici qu'on définit les paramètres relatifs à la vue
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // création d'une nouvelle vue
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.item_quiz, parent, false)
        // définition de la taille, des margins, des paddings et les paramètres de représentation de la vue
        return ViewHolder(v)
    }

    /**
     * Remplacer le contenu d'une vue (appelée par le gestionnaire de disposition)
     *
     * @param holder : gestionnaire de position de la vue
     * @param position : position dans le gestionnaore
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Remplacement du contenu de la vue par cette élément
        val qzNom = quizList!![position].getQuizLabel()
        val qzScore = quizList!![position].getQuizScoreJoueur()
        val qzNbQuest = quizList!![position].getQuizQuestionListe().size.toString()
        holder.edit.setOnClickListener {
            //redirection vers l'édition du quiz
            val intent = Intent(holder.itemView.context, CreateEditQuizzActivity::class.java)
            intent.putExtra("Qz", quizList!![position])
            //holder.itemView.context.startActivity(intent)
            val t = (ctx as Activity).startActivityForResult(intent, 3)
            println(t)
            if (t.toString().equals(3)) {
                LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent)
            }
        }
        holder.quizButton.text = qzNom
        holder.quizButton.setOnClickListener {
            //redirection vers l'activité de jeu avec la position du quiz dans la liste de tous les quizs
            val intent = Intent(holder.itemView.context, QuizzChoisieAJouer::class.java)
            intent.putExtra("quizzChoisieParLUtilsiateur", quizList!![position])
            holder.itemView.context.startActivity(intent)
        }
        //score du joueur sur le quiz joué
        holder.score.text = qzNbQuest
    }

    /**
     * Retourne la taille de la liste
     *
     * @return la taille de la liste
     */
    override fun getItemCount(): Int {
        return quizList!!.size
    }

    /**
     * Setter pour afficher notre liste de quizzs
     *
     * @param quizz : les quizzs
     */
    fun setQuizz(quizz : List<Quiz>) {
        this.quizList = quizz
    }

}
