package br.com.jpm.fitnessmap

import br.com.jpm.fitnessmap.model.Calc

interface OnListClickListener {
    fun onClickUpdate(id: Int, type: String)
    fun onClickDelete(position: Int, calc: Calc)
}