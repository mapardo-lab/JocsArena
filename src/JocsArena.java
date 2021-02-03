/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mapardo
 */

import java.util.Scanner;
import java.util.Random;

public class JocsArena {
    static final short NUM_MAX_COMBATES = 5;
    static final String[] ESTRATEGIA = {"Ataque","Defensa","Engaño","Maniobra"};
    static final int EST_ATAQUE = 0;
    static final int EST_DEFENSA = 1;
    static final int EST_ENGANO = 2;
    static final int EST_MANIOBRA = 3;
    static final int[][] RESULTADO = {{1,0,0,0},{3,3,2,4},{1,0,1,4},{1,0,0,4}};
    static final int RES_NADA = 0;
    static final int RES_DANO = 1;
    static final int RES_DANOX2 = 2;
    static final int RES_RECUP = 3;
    static final int RES_PENALIZA = 4;
    static final String MENSAJE_INICIO = "COMIENZA EL TORNEO. DIEZ COMBATES TE ESPERAN.";
    static final String MENSAJE_VICTORIA = "Enhorabuena!! Eres el campeón.%nHas conseguido %d puntos.%n";
    static final String MENSAJE_DERROTA = "Lástima. Vuelve a intentarlo.";
    static final String MENSAJE_INICIO_COMBATE = "COMBATE NÚMERO %d%n";
    static final String MENSAJE_VICTORIA_COMBATE = "Has ganado el combate!!!";
    static final String MENSAJE_DERROTA_COMBATE = "Perdiste el combate";
    static final String MENSAJE_RONDA = "%n%nRONDA %d.%n";
    static final String MENSAJE_SEPARACION = "----------------------------------------------------------";
    static final String MENSAJE_ESTRATEGIA = "Elige acción (0-Atacar 1-Defender 2-Engaño 3-Maniobra): ";
    static final String MENSAJE_RESULTADO = "%s --> %s %d VS %d %s <-- %s%n";

    public static void main(String[] args) {
        JocsArena prg = new JocsArena();
        prg.inicio();
    }
    
    private void inicio() {
        System.out.println(MENSAJE_INICIO);
        Luchador jugador = new Luchador(Luchador.POS_JUGADOR);
        int numCombates = torneo (jugador);
        if (numCombates == 10) {
            System.out.printf(MENSAJE_VICTORIA,jugador.atribActual[Luchador.ID_PTOSEXP]);
        } else {
            System.out.println(MENSAJE_DERROTA);
        }
    }
   
    public int torneo(Luchador jugador) {
        boolean resultadoCombate = false;
        int numCombates = 0;        
        do {
            System.out.printf(MENSAJE_INICIO_COMBATE,numCombates+1);
            int idRival = Luchador.seleccionarRival(jugador);
            Luchador rival = new Luchador(idRival);
            resultadoCombate = combate(jugador, rival);
            if (resultadoCombate) {
                System.out.println(MENSAJE_VICTORIA_COMBATE);
                numCombates = numCombates + 1;
                postCombate(jugador, rival);                       
            } else {
                System.out.println(MENSAJE_DERROTA_COMBATE);
            }
        } while (resultadoCombate & numCombates < NUM_MAX_COMBATES);
        return numCombates;
    }
    
    private void postCombate(Luchador jugador, Luchador rival) {
        jugador.comprobarPenaliz();
        jugador.sumarPuntosExp(rival.atrib[Luchador.ID_PTOSEXP]);
        jugador.comprobarNivel();
        jugador.mostrarLuchador();
    }
    
    private boolean combate(Luchador jugador, Luchador rival) {
        int numRonda = 0;
        while (jugador.atribActual[Luchador.ID_VIDA] > 0 & rival.atribActual[Luchador.ID_VIDA] > 0) {
            numRonda = numRonda + 1;
            mostrarInfoRonda(numRonda,jugador,rival);
            int jugadorEstrategia = solicitarEstrategia();
            int rivalEstrategia = seleccionarEstrategia();
            resolverRonda(jugador, rival, jugadorEstrategia,rivalEstrategia);
            if (numRonda%5 == 0) {
                jugador.comprobarPenaliz();
                rival.comprobarPenaliz();
            }
        }
        if (jugador.atribActual[Luchador.ID_VIDA] <= 0) {
            return false;
        } else {
            return true;
        }
    }
    
    private void mostrarInfoRonda(int numRonda, Luchador jugador, Luchador rival) {
        System.out.printf(MENSAJE_RONDA,numRonda);
        System.out.println();
        jugador.mostrarLuchador();
        System.out.println(MENSAJE_SEPARACION);
        rival.mostrarLuchador();
        System.out.println(MENSAJE_SEPARACION);
    }
    
    private int solicitarEstrategia() {
        Scanner lectura = new Scanner(System.in);
        boolean okEstrategia = false;
        int estrategia = 0;
        while (!okEstrategia) {
            System.out.print(MENSAJE_ESTRATEGIA);
            okEstrategia = lectura.hasNextInt();
            if (okEstrategia) {
                estrategia = lectura.nextInt();
                if (estrategia < 0 & estrategia > 3) {
                    okEstrategia = false;
                    lectura.next();
                }
            }else {
                lectura.next();
            }
        }
        return estrategia;
    }
    
    private int seleccionarEstrategia() {
        Random aleat = new Random();
        return aleat.nextInt(4);
    }
    
    private void resolverRonda(Luchador jugador, Luchador rival, int jugadorEst, int rivalEst) {
        int jugadorExitos = tirada(jugadorEst,jugador);
        int rivalExitos = tirada(rivalEst,rival);
        System.out.println(MENSAJE_SEPARACION);
        System.out.printf(MENSAJE_RESULTADO,
                jugador.nombre,ESTRATEGIA[jugadorEst], jugadorExitos,
                rivalExitos, ESTRATEGIA[rivalEst],rival.nombre);
        int resultadoJugador = RESULTADO[jugadorEst][rivalEst];
        int resultadoRival = RESULTADO[rivalEst][jugadorEst];
        aplicarResultado(jugador,resultadoJugador,jugadorExitos,rivalExitos);
        aplicarResultado(rival,resultadoRival,rivalExitos,jugadorExitos);
    }
       
    private int tirada(int estrategia, Luchador combatiente) {
        Random aleat = new Random();
        if (estrategia == EST_ATAQUE | estrategia == EST_ENGANO) {
            return aleat.nextInt(combatiente.atribActual[Luchador.ID_ATAQUE]+1);
        } else {
            return aleat.nextInt(combatiente.atribActual[Luchador.ID_DEFENSA]+1);
        }
    }
    
    private void aplicarResultado(Luchador luchador, int resultado, int exitosLuch, int exitosCont) {
        switch (resultado){
            case RES_DANO:
                luchador.aplicarDano(exitosCont);
                break;
            case RES_DANOX2:
                luchador.aplicarDano(exitosCont*2);
                break;
            case RES_RECUP:
                luchador.aplicarRecup(exitosLuch);
                break;
            case RES_PENALIZA:
                luchador.aplicarPenal(exitosCont);
                break;
        }   
    }
}
