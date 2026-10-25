package juego;

import java.awt.Color;
import entorno.Entorno;
import java.awt.Image;

public class Repartidor {
    double x;
    double y;
    double ancho;
    double alto;
    double velocidad;
    double dirX;
    double dirY;
    
    Image img;

    public Repartidor(double x, double y, Image img){
        this.x = x;
        this.y = y;
        this.ancho = 20;
        this.alto = 20;
        this.velocidad = 2;
        this.dirX = 0;
        this.dirY = -1; 
        this.img = img;
        if (img != null) {
            this.img = img.getScaledInstance((int) this.ancho, (int) this.alto, Image.SCALE_SMOOTH);
        }
    }

    public boolean colisionaCon(double otroX, double otroY, double otroAncho, double otroAlto) {
        boolean coincideX = Math.abs(this.x - otroX) < (this.ancho + otroAncho) / 2;
        boolean coincideY = Math.abs(this.y - otroY) < (this.alto + otroAlto) / 2;
        return coincideX && coincideY;
    }

    // Método para dibujar al repartidor
    public void dibujar(Entorno entorno){
        if (this.img != null) {
            double angulo = 0;
            
            if (this.dirX == 1) {
                angulo = 0; // 
            } else if (this.dirX == -1) {
                angulo = Math.PI;
            } else if (this.dirY == -1) {
                angulo = -Math.PI / 2; 
            } else if (this.dirY == 1) {
                angulo = Math.PI / 2; }
            
            entorno.dibujarImagen(this.img, this.x, this.y, angulo, 1);
        } else {
            entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.YELLOW);
        }
    }

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