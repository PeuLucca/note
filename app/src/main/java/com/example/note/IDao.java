package com.example.note;

import java.util.List;

public interface IDao {

    public boolean salvar(Tarefa tarefa);
    public boolean deletar(Tarefa tarefa);
    public boolean atualizar(Tarefa tarefa);
    public List<Tarefa>listar();

}
