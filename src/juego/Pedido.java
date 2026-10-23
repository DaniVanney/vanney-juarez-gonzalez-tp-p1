package juego;

public class Pedido {
    //variables de instancia
    Negocio origen;
    Casa destino;
    String estado;

    //metodo constructor
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
    
}
