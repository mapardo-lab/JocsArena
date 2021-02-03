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

public class Luchador {
    static final short POS_JUGADOR = 0;
    static final short ID_LUCHADOR = 0;
    static final short NUM_ATRIB = 6;
    static final short ID_NIVEL = 1;
    static final short ID_VIDA = 2;
    static final short ID_ATAQUE = 3;
    static final short ID_DEFENSA = 4;
    static final short ID_PTOSEXP = 5;
    static final int[][] LUCHADORES = {{0,1,5,2,2,0},
                          {1,1,5,2,2,50},
                          {2,2,7,3,2,100},
                          {3,2,7,2,3,100},
                          {4,3,10,3,3,150},
                          {5,3,11,3,3,150},
                          {6,3,12,3,3,150},
                          {7,4,12,4,3,200},
                          {8,4,14,4,3,200},
                          {9,5,14,4,4,250},
                          {10,6,16,4,5,300}};
    static final String[] NOMBRES_LUCHADORES = {"Aventurero","Goblin",
                                                "Ogro","Esqueleto",
                                                "Enano","Gladiador",
                                                "Semielfo","Troll",
                                                "Elfo","Hidra"};
    
    static final String TEXT_SELEC_RIVAL = "Introduce el nombre del rival: ";
    static final String INFO_LUCHADOR = "%s --> Nivel: %d  Vida: %d  Ataque: %d  Defensa: %d%n";
    static final String INFO_DANO = "%s pierde %d puntos de vida%n";
    static final String INFO_VIDA_TOTAL = "%s recupera todos los puntos de vida%n";
    static final String INFO_VIDA = "%s recupera %d puntos de vida%n";
    static final String INFO_PENA_ATAQUE = "%s pierde %d puntos de ataque%n";
    static final String INFO_PENA_DEFENSA = "%s pierde %d puntos de defensa%n";
    static final String INFO_RECUPERA_ATAQUE = "%s recupera toda su habilidad de ataque%n";
    static final String INFO_RECUPERA_DEFENSA = "%s recupera toda su habilidad de defensa%n";
    static final String INFO_PTOS_EXP = "Ganas %d puntos de experiencia.%n";
    static final String INFO_NIVEL = "Ganas %d niveles. Tu nivel ahora es %d";
    
    String nombre = new String();
    int[] atribActual = new int[NUM_ATRIB];
    int[] atrib = new int[NUM_ATRIB];
    
    public Luchador(int idLuchador) {
        nombre = NOMBRES_LUCHADORES[idLuchador];
        for (int i = 0; i < NUM_ATRIB; i++) {
            atrib[i] = LUCHADORES[idLuchador][i];
            atribActual[i] = LUCHADORES[idLuchador][i];
        }    
    }
    public void mostrarLuchador() {
        System.out.printf(INFO_LUCHADOR,nombre,atribActual[ID_NIVEL],
                atribActual[ID_VIDA],atribActual[ID_ATAQUE],atribActual[ID_DEFENSA]);        
    }
    
    public void actualizarAtrib() {
        for (int i = 0; i < NUM_ATRIB; i++) {
            atribActual[i] = atrib[i];
        }         
    }
    
    public void aplicarDano(int dano) {
        if (dano > 0) {
            atribActual[ID_VIDA] = atribActual[ID_VIDA] - dano;
            System.out.printf(INFO_DANO,nombre,dano);
        }
    }
    
    public void aplicarRecup(int recup) {
        if (recup > 0) {
            atribActual[ID_VIDA] = atribActual[ID_VIDA] + recup;
            if (atribActual[ID_VIDA] > atrib[ID_VIDA]) {
                atribActual[ID_VIDA] = atrib[ID_VIDA];
                System.out.printf(INFO_VIDA_TOTAL,nombre);
            } else {
                System.out.printf(INFO_VIDA,nombre, recup);            
            }
        }
    }
    
    public void aplicarPenal(int penal) {
        Random aleat = new Random();
        int numAleat = aleat.nextInt(2);
        int habilidad = 0;
        if (numAleat == 0) {
            habilidad = ID_ATAQUE;
            if (atribActual[habilidad] - penal < 1) {
                penal = atribActual[habilidad] - 1;
            }
            atribActual[habilidad] = atribActual[habilidad] - penal;
            System.out.printf(INFO_PENA_ATAQUE,nombre,penal);
        } else {
            habilidad = ID_DEFENSA;
            if (atribActual[habilidad] - penal < 1) {
                penal = atribActual[habilidad] - 1;
            }
            atribActual[habilidad] = atribActual[habilidad] - penal;
            System.out.printf(INFO_PENA_DEFENSA,nombre,penal);
        }
    }
       
    public static int seleccionarRival(Luchador jugador) {
        System.out.print(TEXT_SELEC_RIVAL);
        Scanner lectura = new Scanner(System.in);
        String nombreRival = lectura.next();
        int idRival = comprobarNombreRival(nombreRival);
        if ( idRival > 0 & LUCHADORES[idRival][ID_NIVEL] >= jugador.atribActual[ID_NIVEL]) {
            return idRival;
        } else {
            Random aleat = new Random();
            do {
                idRival = aleat.nextInt(LUCHADORES.length-1) + 1;
            } while (LUCHADORES[idRival][ID_NIVEL] < jugador.atribActual[ID_NIVEL] 
                    & LUCHADORES[idRival][ID_NIVEL] > jugador.atribActual[ID_NIVEL] + 1);
            return idRival;
        }
    }
    
    public static int comprobarNombreRival(String nombreRival) {
        int idRival = 0;
        int i = 1;
        boolean okNombre = false;
        while (i<LUCHADORES.length & !okNombre) {
            if (NOMBRES_LUCHADORES[i].equals(nombreRival)) {
                okNombre = true;
                idRival = i;
            }
            i = i + 1;
        }
        return idRival;
    }
    
    public void comprobarPenaliz() {
        if (atribActual[ID_ATAQUE] < atrib[ID_ATAQUE]) {
            atribActual[ID_ATAQUE] = atrib[ID_ATAQUE];
            System.out.printf(INFO_RECUPERA_ATAQUE,nombre);
        }
        if (atribActual[ID_DEFENSA] < atrib[ID_DEFENSA]) {
            atribActual[ID_DEFENSA] = atrib[ID_DEFENSA];
            System.out.printf(INFO_RECUPERA_DEFENSA,nombre);
        }
    }
    public void sumarPuntosExp(int ptosExp) {
        atrib[ID_PTOSEXP] = atrib[ID_PTOSEXP] + ptosExp;
        System.out.printf(INFO_PTOS_EXP,ptosExp);
    }
    
    public void comprobarNivel() {
        if (atrib[ID_PTOSEXP]/100 >= atrib[ID_NIVEL]) {
            int numNivel = (atrib[ID_PTOSEXP] - (atrib[ID_NIVEL]-1)*100)/100;
            atrib[ID_NIVEL] = atrib[ID_NIVEL] + numNivel;
            System.out.printf(INFO_NIVEL,numNivel,atrib[ID_NIVEL]);        
            atrib[ID_VIDA] = atrib[ID_VIDA] + numNivel *2;
            for (int i=0; i<numNivel;i++) {
                Random aleat = new Random();
                int habilidad = aleat.nextInt(2);
                if (habilidad == 0) {
                    atrib[ID_ATAQUE] = atrib[ID_ATAQUE] + 1;
                } else {
                    atrib[ID_DEFENSA] = atrib[ID_DEFENSA] + 1;
                }
            }
            actualizarAtrib();
        }
    }
}
