package com.example_2_060303.note.helper;

import com.example_2_060303.note.model.Tarefa;

import java.util.List;

public interface IDao {

    public boolean salvar(Tarefa tarefa);
    public boolean salvarSemStatus(Tarefa tarefa);
    public boolean deletar(Tarefa tarefa);
    public boolean atualizar(Tarefa tarefa);
    public List<Tarefa>listar(String ordenarPor);
    public List<Tarefa>listarSttsConcluido();
    public List<Tarefa>listarSttsNaoConcluido();
    public List<Tarefa>listarSemStts();
    public boolean atualizarStatus(Tarefa tarefa);
    public List<Tarefa> verificarTarefa(String titulo, String conteudo);
    public boolean verificarExistencia(String titulo);
    public Tarefa verificarStatus (String titulo);
    public List<Tarefa>listarTitulo();
}
