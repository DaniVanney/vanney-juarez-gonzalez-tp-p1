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
	Pedido pedido;
	
	int pedidosEntregados;
	
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

		int indiceNegocio = (int) (Math.random() * this.mapa.negocios.length);
		int indiceCasa = (int) (Math.random() * this.mapa.casas.length);

		this.pedido = new Pedido(this.mapa.negocios[indiceNegocio], this.mapa.casas[indiceCasa]);
		this.pedido.recibirEnNegocio();

		this.pedidosEntregados =0;


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
    		double xAnterior = this.repartidor.x;

    		this.repartidor.moverDerecha(this.mapa.x + this.mapa.ancho);

    		if (this.mapa.hayColision(this.repartidor)) {
        		this.repartidor.x = xAnterior;
    		}
		}

		
		if (this.entorno.estaPresionada('a')) {
    		double xAnterior = this.repartidor.x;

    		this.repartidor.moverIzquierda(this.mapa.x);

    		if (this.mapa.hayColision(this.repartidor)) {
        		this.repartidor.x = xAnterior;
    		}
		}

		if (this.entorno.estaPresionada('w')) {
    		double yAnterior = this.repartidor.y;

    		this.repartidor.moverArriba(this.mapa.y);

    		if (this.mapa.hayColision(this.repartidor)) {
        		this.repartidor.y = yAnterior;
    		}
		}

		if (this.entorno.estaPresionada('s')) {
    		double yAnterior = this.repartidor.y;

    		this.repartidor.moverAbajo(this.mapa.y + this.mapa.alto);

    		if (this.mapa.hayColision(this.repartidor)) {
        		this.repartidor.y = yAnterior;
    		}
		}

			//bloque para retirar el pedido del negocio
		if (this.pedido.estado.equals("Esperando") && this.entorno.sePresionoBoton(this.entorno.BOTON_DERECHO)) {

    		Negocio negocio = this.pedido.origen;
   			double yFlecha = this.mapa.obtenerYFlechaNegocio(negocio);

    		boolean sobreFlecha =Math.abs(this.repartidor.x - negocio.x) <= this.repartidor.ancho / 2 && Math.abs(this.repartidor.y - yFlecha) <= this.repartidor.alto / 2;

    		boolean clicEnNegocio = negocio.contienePunto(this.entorno.mouseX(), this.entorno.mouseY());

    		if (sobreFlecha && clicEnNegocio) {
        		this.pedido.retirar();
    		}
		}
		//Bloque para concretar la entrega del pedido
		if (this.pedido.estado.equals("En camino") && this.entorno.sePresionoBoton(this.entorno.BOTON_DERECHO)) {

    		Casa casa = this.pedido.destino;
    		double yFlecha = this.mapa.obtenerYFlechaCasa(casa);
    		boolean sobreFlecha =Math.abs(this.repartidor.x - casa.x) <= this.repartidor.ancho / 2 && Math.abs(this.repartidor.y - yFlecha) <= this.repartidor.alto / 2;
			boolean clicEnCasa = casa.contienePunto(this.entorno.mouseX(), this.entorno.mouseY());

    		if (sobreFlecha && clicEnCasa) {
        		this.pedido.entregar();
    			this.pedidosEntregados++;
    		}
		}


		//Sector del mapa
		this.mapa.dibujar(this.entorno);
		if (this.pedido.estado.equals("En camino")) {
    		this.mapa.dibujarFlechaCasa(this.entorno, this.pedido.destino);
		}

		if (this.pedido.estado.equals("Esperando")) {
    		this.mapa.dibujarFlechaNegocio(
        	this.entorno, this.pedido.origen
    		);
		}



		this.repartidor.dibujar(this.entorno);

		//Panel superior de info
		this.entorno.dibujarRectangulo(400, 30, 800, 60, 0, new Color(30,30,30));

		this.entorno.cambiarFont("Arial", 20, Color.WHITE);
		this.entorno.escribirTexto("Delivery Rush", 20, 38);
		this.entorno.escribirTexto("Pedido: " + this.pedido.estado, 210,38);
		this.entorno.escribirTexto(aaaaaaaaaaaaa"Entregados: " + this.pedidosEntregados, 500, 38);
		
	}

	

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
