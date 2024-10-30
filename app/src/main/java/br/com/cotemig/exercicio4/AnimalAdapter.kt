package br.com.cotemig.exercicio4

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

interface OnItemClickListener {
    fun onItemClick(position: Int)
}

class AnimalAdapter(val contexto: Context, val lista: ArrayList<AnimalModel>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<AnimalAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val tvNome: TextView = view.findViewById(R.id.tvNome)
        val tvRaca: TextView = view.findViewById(R.id.tvRaca)
        val tvIdade: TextView = view.findViewById(R.id.tvIdade)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(contexto).inflate(R.layout.lista_item, parent, false))
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvNome.text = lista[position].nome
        holder.tvRaca.text = lista[position].raca
        holder.tvIdade.text = lista[position].idade.toString()
    }
}
