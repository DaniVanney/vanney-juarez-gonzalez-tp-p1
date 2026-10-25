package juego;

import java.awt.Color;
import entorno.Entorno;

public class Enemigo {
    double x;
    double y;
    double ancho;
    double alto;
    double velocidad;

    // Dirección: 1 = derecha/abajo, -1 = izquierda/arriba
    int direccionX;
    int direccionY;

    // true si se mueve horizontalmente, false si es vertical
    boolean horizontal;

    // Puede dejar bombas o no
    boolean dejaBombas;

    // Todos los tiempos se expresan en milisegundos.
    long ultimaBomba;
    int intervaloBomba;

    public Enemigo(double x, double y, boolean horizontal, boolean dejaBombas, long tiempoActual) {
        this.x = x;
        this.y = y;
        this.ancho = 25;
        this.alto = 25;
        this.velocidad = 1.5;
        this.horizontal = horizontal;
        this.dejaBombas = dejaBombas;
        this.direccionX = 1;
        this.direccionY = 1;
        this.intervaloBomba = 4000;
        this.ultimaBomba = tiempoActual;
    }

    // Devuelve true una sola vez por intervalo y registra el momento de la bomba.
    public boolean debeDejarBomba(long tiempoActual) {
        if (this.dejaBombas && tiempoActual - this.ultimaBomba >= this.intervaloBomba) {
            this.ultimaBomba = tiempoActual;
            return true;
        }
        return false;
    }

    public void dibujar(Entorno entorno) {
        Color color = this.dejaBombas ? new Color(150, 0, 200) : new Color(200, 40, 40);
        entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, color);
    }

    // Mueve al enemigo dentro de los límites dados. Si toca un borde, cambia de dirección.
    public void mover(double limiteIzq, double limiteDer, double limiteSup, double limiteInf) {
        if (this.horizontal) {
            this.x += this.velocidad * this.direccionX;
            if (this.x + this.ancho / 2 > limiteDer) {
                this.x = limiteDer - this.ancho / 2;
                this.direccionX = -1;
            } else if (this.x - this.ancho / 2 < limiteIzq) {
                this.x = limiteIzq + this.ancho / 2;
                this.direccionX = 1;
            }
        } else {
            this.y += this.velocidad * this.direccionY;
            if (this.y + this.alto / 2 > limiteInf) {
                this.y = limiteInf - this.alto / 2;
                this.direccionY = -1;
            } else if (this.y - this.alto / 2 < limiteSup) {
                this.y = limiteSup + this.alto / 2;
                this.direccionY = 1;
            }
        }
    }

    public boolean colisionaCon(double otroX, double otroY, double otroAncho, double otroAlto) {
        boolean coincideX = Math.abs(this.x - otroX) < (this.ancho + otroAncho) / 2;
        boolean coincideY = Math.abs(this.y - otroY) < (this.alto + otroAlto) / 2;
        return coincideX && coincideY;
    }
}