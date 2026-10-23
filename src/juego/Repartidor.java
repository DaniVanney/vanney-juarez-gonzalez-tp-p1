package juego;

import java.awt.Color;
import entorno.Entorno;

public class Repartidor {
    double x;
    double y;
    double ancho;
    double alto;
    double velocidad;
    double dirX;
    double dirY;

    //Método constructor de repartidor
    public Repartidor(double x, double y){
        this.x = x;
        this.y = y;
        this.ancho = 20;
        this.alto = 20;
        this.velocidad = 2;
        this.dirX = 0;
        this.dirY = -1; // Arranca mirando hacia arriba
    }

    //Método para detectar colisiones
    public boolean colisionaCon(double otroX, double otroY, double otroAncho, double otroAlto) {
        // Le damos 10 píxeles de margen para que detecte el contacto
        boolean coincideX = Math.abs(this.x - otroX) <= (this.ancho + otroAncho) / 2 + 10;
        boolean coincideY = Math.abs(this.y - otroY) <= (this.alto + otroAlto) / 2 + 10;
        return coincideX && coincideY;
    }

    //método para dibujar al repartidor
    public void dibujar(Entorno entorno){
        entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.YELLOW);
    }

    //Métodos de movimiento
    public void moverDerecha(double limiteDerecho) {
        this.x = this.x + this.velocidad;
        this.dirX = 1;
        this.dirY = 0;
        if (this.x + this.ancho / 2 > limiteDerecho) {
            this.x = limiteDerecho - this.ancho / 2;
        }
    }

    public void moverIzquierda(double limiteIzquierdo) {
        this.x = this.x - this.velocidad;
        this.dirX = -1;
        this.dirY = 0;
        if (this.x - this.ancho / 2 < limiteIzquierdo) {
            this.x = limiteIzquierdo + this.ancho / 2;
        }
    }

    public void moverArriba(double limiteSuperior) {
        this.y = this.y - this.velocidad;
        this.dirX = 0;
        this.dirY = -1;
        if (this.y - this.alto / 2 < limiteSuperior) {
            this.y = limiteSuperior + this.alto / 2;
        }
    }

    public void moverAbajo(double limiteInferior) {
        this.y = this.y + this.velocidad;
        this.dirX = 0;
        this.dirY = 1;
        if (this.y + this.alto / 2 > limiteInferior) {
            this.y = limiteInferior - this.alto / 2;
        }
    }
}