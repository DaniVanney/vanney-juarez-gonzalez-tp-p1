package juego;

import java.awt.Color;
import entorno.Entorno;

public class Proyectil {
    double x;
    double y;
    double ancho;
    double alto;
    double velocidad;
    double dirX;
    double dirY;

    public Proyectil(double x, double y, double dirX, double dirY) {
        this.x = x;
        this.y = y;
        this.ancho = 8;
        this.alto = 8;
        this.velocidad = 6;
        this.dirX = dirX;
        this.dirY = dirY;
    }

    public void mover() {
        this.x += this.dirX * this.velocidad;
        this.y += this.dirY * this.velocidad;
    }

    public boolean fueraDe(double limiteIzq, double limiteDer, double limiteSup, double limiteInf) {
        return this.x + this.ancho / 2 < limiteIzq
                || this.x - this.ancho / 2 > limiteDer
                || this.y + this.alto / 2 < limiteSup
                || this.y - this.alto / 2 > limiteInf;
    }

    public boolean colisionaCon(double otroX, double otroY, double otroAncho, double otroAlto) {
        boolean coincideX = Math.abs(this.x - otroX) < (this.ancho + otroAncho) / 2;
        boolean coincideY = Math.abs(this.y - otroY) < (this.alto + otroAlto) / 2;
        return coincideX && coincideY;
    }

    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.CYAN);
    }
}