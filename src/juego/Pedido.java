package juego;

public class Pedido {
    Negocio origen;
    Casa destino;
    String estado;

    public Pedido(Negocio origen, Casa destino){
        this.origen = origen;
        this.destino = destino;
        this.estado = "Realizado";
    }

    public void recibirEnNegocio(){
        if(this.estado.equals("Realizado")){
            this.estado = "Esperando";
        }
    }

    public void retirar() {
        if (this.estado.equals("Esperando")) {
            this.estado = "En camino";
        }
    }

    public void entregar() {
        if (this.estado.equals("En camino")) {
            this.estado = "Entregado";
        }
    }

    public void perder() {
        if (this.estado.equals("En camino") || this.estado.equals("Esperando")) {
            this.estado = "Perdido";
        }
    }

    public boolean estaActivo() {
        return this.estado.equals("Esperando") || this.estado.equals("En camino");
    }
}
