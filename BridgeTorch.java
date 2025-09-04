/*
Integrantes del equipo:

Guzmán Dolores Alexis 
Olguin Hernandez Johan Gael
Araujo Galán Maximiliano 
Gathe Esquivel Arleth  Elizabeth
Flores Madrigal Diego
*/

import java.util.*;

public class BridgeTorch {
    // Clase que representa el estado actual del problema
    static class State {
        int mask;        // bitmask: 1 = persona a la izquierda, 0 = derecha
        int torchSide;   // 0 = antorcha a la izquierda, 1 = derecha
        State(int mask, int torchSide) {
            this.mask = mask;
            this.torchSide = torchSide;
        }
        @Override
        public int hashCode() { return Objects.hash(mask, torchSide); }
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof State)) return false;
            State s = (State) o;
            return mask == s.mask && torchSide == s.torchSide;
        }
    }

    // Clase que representa un movimiento realizado
    static class Move {
        String direction;      // Dirección del movimiento ("L->R" o "R->L")
        List<Integer> persons; // Índices de las personas que cruzan
        int time;              // Tiempo que toma el movimiento
        Move(String direction, List<Integer> persons, int time) {
            this.direction = direction;
            this.persons = persons;
            this.time = time;
        }
        public String toString() {
            return direction + " " + persons + " -> " + time + " min";
        }
    }

    // Nodo para la búsqueda de caminos mínimos
    static class Node implements Comparable<Node> {
        State state;       // Estado actual
        int dist;          // Tiempo acumulado
        List<Move> path;   // Secuencia de movimientos realizados
        Node(State s, int d, List<Move> p) {
            state = s; dist = d; path = p;
        }
        public int compareTo(Node other) {
            return Integer.compare(dist, other.dist);
        }
    }

    // Algoritmo principal para resolver el problema del puente
    public static void solve(int[] times) {
        int N = times.length;
        int ALL_MASK = (1<<N) - 1; // Todos a la izquierda

        State start = new State(ALL_MASK, 0); // Estado inicial
        State goal  = new State(0, 1);        // Estado objetivo: todos a la derecha

        PriorityQueue<Node> pq = new PriorityQueue<>(); // Cola de prioridad para Dijkstra
        Map<State, Integer> dist = new HashMap<>();     // Distancias mínimas por estado
        pq.add(new Node(start, 0, new ArrayList<>()));
        dist.put(start, 0);

        int bestTime = -1;
        List<Move> bestPath = null;

        // Búsqueda de caminos mínimos
        while (!pq.isEmpty()) {
            Node cur = pq.poll();
            State s = cur.state;
            if (s.equals(goal)) { // Si se alcanza el objetivo, guardar resultado
                bestTime = cur.dist;
                bestPath = cur.path;
                break;
            }
            if (cur.dist > dist.getOrDefault(s, Integer.MAX_VALUE)) continue;

            // Generar movimientos posibles según la posición de la antorcha
            List<Integer> people = new ArrayList<>();
            if (s.torchSide == 0) { // Antorcha a la izquierda
                for (int i=0;i<N;i++) if ((s.mask & (1<<i))!=0) people.add(i);
            } else { // Antorcha a la derecha
                for (int i=0;i<N;i++) if ((s.mask & (1<<i))==0) people.add(i);
            }

            // Probar todas las combinaciones de 1 o 2 personas
            for (int i=0;i<people.size();i++) {
                for (int j=i;j<people.size();j++) {
                    List<Integer> combo = new ArrayList<>();
                    combo.add(people.get(i));
                    if (i!=j) combo.add(people.get(j));

                    int t = 0;
                    for (int idx: combo) t = Math.max(t, times[idx]); // Tiempo máximo del grupo

                    int newMask = s.mask;
                    if (s.torchSide==0) { // Movimiento Izquierda -> Derecha
                        for (int idx: combo) newMask &= ~(1<<idx); // Mover personas a la derecha
                        State ns = new State(newMask,1);
                        int nd = cur.dist + t;
                        if (nd < dist.getOrDefault(ns, Integer.MAX_VALUE)) {
                            dist.put(ns, nd);
                            List<Move> newPath = new ArrayList<>(cur.path);
                            newPath.add(new Move("L->R", combo, t));
                            pq.add(new Node(ns, nd, newPath));
                        }
                    } else { // Movimiento Derecha -> Izquierda
                        for (int idx: combo) newMask |= (1<<idx); // Mover personas a la izquierda
                        State ns = new State(newMask,0);
                        int nd = cur.dist + t;
                        if (nd < dist.getOrDefault(ns, Integer.MAX_VALUE)) {
                            dist.put(ns, nd);
                            List<Move> newPath = new ArrayList<>(cur.path);
                            newPath.add(new Move("R->L", combo, t));
                            pq.add(new Node(ns, nd, newPath));
                        }
                    }
                }
            }
        }

        // Mostrar resultado
        if (bestTime >= 0) {
            System.out.println("Tiempo minimo = " + bestTime + " minutos");
            for (Move m: bestPath) {
                List<String> names = new ArrayList<>();
                for (int idx: m.persons) names.add("P"+(idx+1)+"("+times[idx]+")");
                System.out.println(m.direction + " " + names + " -> " + m.time + " min");
            }
        } else {
            System.out.println("No hay solucion");
        }
    }

    // Método principal de ejemplo
    public static void main(String[] args) {
        // Ejemplo: A=1, B=2, C=7, D=10
        int[] times = {1,2,7,10};
        solve(times);
    }
}
