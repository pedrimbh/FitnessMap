package br.com.jpm.fitnessmap.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CalcDao {

//    @Query -> buscar
//    @Update ->atualizar
//    @Delete -> excluir
//    @Insert -> inserir
    @Insert
    fun insert(calc: Calc)

    @Query("SELECT * FROM Calc WHERE type = :type")
    fun getRegisterByType(type:String): List<Calc>
}