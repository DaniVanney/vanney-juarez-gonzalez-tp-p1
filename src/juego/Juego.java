package juego;


import java.awt.Color;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;

	Mapa mapa;
	Repartidor repartidor;
	
	// Variables y métodos propios de cada grupo
	// ...
	
	// Inicializa el objeto entorno  (Método constructor)
	Juego()
	{
		
		
		
		// Inicializar lo que haga falta para el juego
		// ...
		this.entorno = new Entorno(this, "Delivery Rush", 800, 600);

		this.mapa = new Mapa(0, 60, 800, 540);

		this.repartidor = new Repartidor(400, 585);

		// Inicia el juego!
		this.entorno.iniciar();
	}

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y 
	 * por lo tanto es el método más importante de esta clase. Aquí se debe 
	 * actualizar el estado interno del juego para simular el paso del tiempo 
	 * (ver el enunciado del TP para mayor detalle).
	 */
	public void tick()
	{
		// Procesamiento de un instante de tiempo
		// ...

		if (this.entorno.estaPresionada('d')) {
    		this.repartidor.moverDerecha(this.mapa.x + this.mapa.ancho);
		}

		if (this.entorno.estaPresionada('a')) {
    		this.repartidor.moverIzquierda(this.mapa.x);
		}

		//Sector del mapa
		this.mapa.dibujar(this.entorno);
		this.repartidor.dibujar(this.entorno);

		//Panel superior de info
		this.entorno.dibujarRectangulo(400, 30, 800, 60, 0, new Color(30,30,30));

		this.entorno.cambiarFont("Arial", 20, Color.WHITE);
		this.entorno.escribirTexto("Delivery Rush", 20, 38);
		
	}

	

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
