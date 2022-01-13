package com.example_2_060303.note.helper;

import com.example_2_060303.note.model.Tarefa;

import java.util.List;

public interface IDao {

    public boolean salvar(Tarefa tarefa);
    public boolean salvarSemStatus(Tarefa tarefa);
    public boolean deletar(Tarefa tarefa);
    public boolean atualizar(Tarefa tarefa);
    public List<Tarefa>listar();
    public boolean atualizarStatus(Tarefa tarefa);
    public boolean verificarTarefa(String titulo, String conteudo);
    public boolean verificarExistencia(String titulo);
    public Tarefa verificarStatus (String titulo);
}
