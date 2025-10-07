package com.cofincafe.clientes_api.Model;

public class ClienteCSV {
    private int id;
    private String nombre;
    private double balance;

    public ClienteCSV(int id, String nombre, double balance) {
        this.id = id;
        this.nombre = nombre;
        this.balance = balance;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}
