package com.example.quizzsamp.jeu.createEditQuizz

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzsamp.structure.Question
import com.example.quizzsamp.structure.Quiz
import com.example.quizzsamp.R

/**
 * Classe qui nous permet d'adpater les quizs dans notre activité d'édition et création
 */
class AdpaterCreateQuizz : RecyclerView.Adapter<AdpaterCreateQuizz.ViewHolder> {

    private lateinit var quizz: Quiz
    private var quest: ArrayList<Question>? = null
    internal var ctx: Context
    lateinit var rv: RecyclerView

    /**
     * Fournir une référence aux vues pour chaque élément de données
     * Les éléments de données complexes peuvent nécessiter plus d'une vue par élément, et
     * vous donnez accès à toutes les vues d'un élément de données dans un détenteur de vue
     */
    inner class ViewHolder(v: View, MTE: MajTextEntree) : RecyclerView.ViewHolder(v) {

        var qstName: EditText
        var qstDelete: Button
        var qstAddAnswer: Button
        var qstRecyclerViewAnswer: RecyclerView
        var mTextE : MajTextEntree

        lateinit var linearLayoutManager: LinearLayoutManager
        lateinit var adapterCreateEditAnswer: AdapterCreateEditAnswer

        /**
         * Initialisation
         */
        init {
            qstName = v.findViewById(R.id.qstName) as EditText
            qstDelete = v.findViewById(R.id.qstDelete) as Button
            qstAddAnswer = v.findViewById(R.id.qstAddAnswer) as Button
            qstRecyclerViewAnswer = v.findViewById(R.id.qstRecyclerViewAnswer) as RecyclerView

            mTextE = MTE
            qstName.addTextChangedListener(mTextE)
        }
    }

    /**
     * Permet de mettre à jour les zones de texte de saisie
     */
    inner class MajTextEntree : TextWatcher {
        var position : Int = -1

        fun setposition(nvPosition: Int) {
            position = nvPosition
        }

        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        //Change le texte saisie
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (position != -1 && quest!!.size != position) {
                quest!![position].setQuestionLabel(s.toString())
            }
        }
    }

    /**
     * Constructeur par défaut
     *
     * @param context : l'activité createEditQuizz
     */
    constructor(context: Context) {
        quest = ArrayList()
        ctx = context
    }

    /**
     * Constructeur de l'adpatateur de l'activité createEditQuizz
     *
     * @param context : l'activité createEditQuizz
     * @param quiz : la liste de quiz
     */
    constructor(context: Context, quiz: Quiz) {
        ctx = context
        this.quest = quiz.getQuizQuestionListe()
        this.quizz = quiz
    }

    /**
     * Créer de nouvelles vues (invoquées par le gestionnaire de disposition)
     *
     * @param parent : un ViewGroup
     * @param viewType : un Int
     * @return retourne ViewHolder(v), c'est ici qu'on définit les paramètres relatifs à la vue
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.item_add_question, parent, false)
        return ViewHolder(v, MajTextEntree())
    }

    /**
     * Retourne la taille de la liste de questions
     *
     * @return la taille de la liste de questions
     */
    override fun getItemCount(): Int {
        return quest!!.size
    }

    /**
     * Remplacer le contenu d'une vue (appelée par le gestionnaire de disposition)
     *
     * @param holder : gestionnaire de position de la vue
     * @param position : position dans le gestionnaore
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = quest!![position].getQuestionLabel()

        holder.qstName.setText(name)
        //Ecouteur pour enlever le texte au moment du clique
        holder.qstName.setOnClickListener {
            holder.qstName.text.clear()
        }
        holder.linearLayoutManager = LinearLayoutManager(holder.itemView.context)
        holder.qstRecyclerViewAnswer.layoutManager = holder.linearLayoutManager
        holder.adapterCreateEditAnswer =
            AdapterCreateEditAnswer(
                ctx as Activity,
                quest!![position]
            )
        holder.qstRecyclerViewAnswer.adapter = holder.adapterCreateEditAnswer

        //Ecouteur pour ajouter une proposition
        holder.qstAddAnswer.setOnClickListener {
            Toast.makeText(ctx, "Ajoute une propositions.", Toast.LENGTH_SHORT).show()
            if (quest!![position].getPropositions().size < 5) {
                quest!![position].addPropositions("nv propo")
                holder.adapterCreateEditAnswer.notifyDataSetChanged()
            }
        }

        //Ecouteur pour supprimer une question
        holder.qstDelete.setOnClickListener {
            if (quest!!.size > 1) {
                quest!!.removeAt(position)
                rv.removeViewAt(position)
                notifyItemRemoved(position)
                notifyDataSetChanged()
            }
        }

        // Permet de mettre à jour les textes
        holder.mTextE.setposition(position)
    }

    /**
     * Permet de remettre à jour la vue
     *
     * @param recyclerView : un recyclerView
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.rv = recyclerView
    }
}