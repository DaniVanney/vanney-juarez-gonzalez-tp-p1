package juego;

import java.awt.Color;
import entorno.Entorno;

public class Repartidor {
    double x;
    double y;
    double ancho;
    double alto;
    double velocidad;



    public Repartidor(double x, double y){
        this.x = x;
        this.y=y;
        this.ancho = 20;
        this.alto = 20;
        this.velocidad = 2;
    }



    public void dibujar(Entorno entorno){
        entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.YELLOW);
    }




    public void moverDerecha(){
        this.x = this.x + this.velocidad;

    }

    public void moverIzquierda(){
        this.x = this.x - this.velocidad;
    }
}
