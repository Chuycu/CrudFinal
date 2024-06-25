package co.com.crudclase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.com.crudclase.databinding.ListMedicineBinding

class MedAdapter(): RecyclerView.Adapter<MedAdapter.MedViewHolder>(){

    private var medList: ArrayList<MedModel> = ArrayList()

    private var onClickItem: ((MedModel)->Unit)? = null
    private var onClickDeleteItem: ((MedModel)-> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MedViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_medicine,parent,false)
    )

    override fun getItemCount(): Int = medList.size

    override fun onBindViewHolder(holder: MedViewHolder, position: Int) {
        val itemList = medList[position]
        holder.bindView(itemList)

        holder.itemView.setOnClickListener { onClickItem?.invoke(itemList) }
        holder.binding.btnEliminar.setOnClickListener { onClickDeleteItem?.invoke(itemList) }
    }

    fun setOnClickItem(callback: (MedModel)->Unit){
        this.onClickItem = callback
    }

    fun setOnClickDeleteItem(callback: (MedModel)->Unit){
        this.onClickDeleteItem = callback
    }


    fun addItems(items: ArrayList<MedModel>){
        this.medList = items
        notifyDataSetChanged() //
    }

    inner class MedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding = ListMedicineBinding.bind(itemView)

        fun bindView(med: MedModel){
            with(binding){
                tvIdMed.text = med.id.toString()
                tvNombreMed.text= med.nombre
                tvPrecioMed.text=med.precio
                tvDosisMed.text= med.dosis
            }
        }
    }


}


