package br.com.trezzor.model;

public class Item {
    private int id;
    private String nome;
    private String categoria;
    private double valorEstimado;

    public Item() {}

    public Item(int id, String nome, String categoria, double valorEstimado) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.valorEstimado = valorEstimado;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public double getValorEstimado() { return valorEstimado; }
    public void setValorEstimado(double valorEstimado) { this.valorEstimado = valorEstimado; }
}