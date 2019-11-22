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
import android.widget.Switch
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzsamp.structure.Question
import com.example.quizzsamp.R

/**
 * Classe qui nous permet d'adpater les promositions dans l'activité de création édition des quizzs
 */
class AdapterCreateEditAnswer : RecyclerView.Adapter<AdapterCreateEditAnswer.ViewHolder> {

    private lateinit var quest : Question
    private var propositions: ArrayList<String>? = null
    private var ctx : Context
    lateinit var rv : RecyclerView

    /**
     * Fournir une référence aux vues pour chaque élément de données
     * Les éléments de données complexes peuvent nécessiter plus d'une vue par élément, et
     * vous donnez accès à toutes les vues d'un élément de données dans un détenteur de vue
     */
    inner class ViewHolder(v: View, MTE: MajTextEntree) : RecyclerView.ViewHolder(v) {
        var aswAnswer: EditText
        var aswDelete: Button
        var aswSwitch: Switch
        var mTextE : MajTextEntree

        /**
         * Initialisation
         */
        init {
            aswAnswer = v.findViewById<View>(R.id.aswAnswer) as EditText
            aswDelete = v.findViewById<View>(R.id.aswDelete) as Button
            aswSwitch = v.findViewById<View>(R.id.aswSwitch) as Switch

            mTextE = MTE
            aswAnswer.addTextChangedListener(mTextE)
        }
    }

    /**
     * Permet de mettre à jour les textes dans les champs à remplir
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

        //Change le texte
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (position != -1 && propositions!!.size != position) {
                propositions!![position] = s.toString()
            }
        }
    }

    /**
     * Constructeur par défaut
     *
     * @param context : l'activité ayant besoin de cet adapter (createEditionQuizz)
     */
    constructor(context: Activity) {
        propositions = ArrayList()
        ctx = context
    }

    /**
     * Constructeur de l'adpatateur pour ajouter des propositions (activité createEditionQuizz)
     *
     * @param context : l'activité principale
     * @param qst : la liste de questions
     */
    constructor(context: Activity, qst: Question) {
        propositions = qst.getPropositions()
        quest = qst
        ctx = context
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
        val v = inflater.inflate(R.layout.item_add_answer, parent,false)
        return ViewHolder(v, MajTextEntree())
    }

    /**
     * Remplacer le contenu d'une vue (appelée par le gestionnaire de disposition)
     *
     * @param holder : gestionnaire de position de la vue
     * @param position : position dans le gestionnaore
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val propo = propositions!![position]
        val rep = quest.getIndiceReponse()

        holder.aswAnswer.setText(propo)
        if (rep-1 == position) {
            println("POSITION : $position")
            holder.aswSwitch.isChecked = true

        }

        //Ecoute sur le text pour le clear au moment du clique
        holder.aswAnswer.setOnClickListener {
            holder.aswAnswer.text.clear()
        }

        //Supprimer une proposition
        holder.aswDelete.setOnClickListener {
            if (propositions!!.size > 2) {
                propositions!!.removeAt(position)
                rv.removeViewAt(position)
                quest.removePropositions(quest.getPropositions()[position])
                notifyItemRemoved(position)
                notifyDataSetChanged()
            }
        }

        //Change l'indice de la réponse
        holder.aswSwitch.setOnClickListener {
            quest.setIndiceReponse(position-1)
            notifyDataSetChanged()
        }

        //Pour mettre à jour les textes
        holder.mTextE.setposition(position)
    }

    /**
     * Retourne la taille de la liste des propositions
     *
     * @return la taille de la liste des propositions
     */
    override fun getItemCount(): Int {
        return propositions!!.size
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