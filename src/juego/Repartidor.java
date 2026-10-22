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



    //Métodos de movimiento horizontal
    public void moverDerecha(double limiteDerecho) {
        this.x = this.x + this.velocidad;

        if (this.x + this.ancho / 2 > limiteDerecho) {
            this.x = limiteDerecho - this.ancho / 2;
        }
    }

    public void moverIzquierda(double limiteIzquierdo) {
        this.x = this.x - this.velocidad;

        if (this.x - this.ancho / 2 < limiteIzquierdo) {
            this.x = limiteIzquierdo + this.ancho / 2;
        }
    }

        //Métodos de movimiento vertical
    public void moverArriba(double limiteSuperior) {
        this.y = this.y - this.velocidad;

        if (this.y - this.alto / 2 < limiteSuperior) {
            this.y = limiteSuperior + this.alto / 2;
        }
    }

    public void moverAbajo(double limiteInferior) {
        this.y = this.y + this.velocidad;

        if (this.y + this.alto / 2 > limiteInferior) {
            this.y = limiteInferior - this.alto / 2;
        }
    }
}
