import java.util.*;

public class BridgeTorch {
    static class State {
        int mask;        // bitmask: 1 = izquierda, 0 = derecha
        int torchSide;   // 0 = izquierda, 1 = derecha
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

    static class Move {
        String direction;
        List<Integer> persons;
        int time;
        Move(String direction, List<Integer> persons, int time) {
            this.direction = direction;
            this.persons = persons;
            this.time = time;
        }
        public String toString() {
            return direction + " " + persons + " -> " + time + " min";
        }
    }

    static class Node implements Comparable<Node> {
        State state;
        int dist;
        List<Move> path;
        Node(State s, int d, List<Move> p) {
            state = s; dist = d; path = p;
        }
        public int compareTo(Node other) {
            return Integer.compare(dist, other.dist);
        }
    }

    public static void solve(int[] times) {
        int N = times.length;
        int ALL_MASK = (1<<N) - 1;

        State start = new State(ALL_MASK, 0);
        State goal  = new State(0, 1);

        PriorityQueue<Node> pq = new PriorityQueue<>();
        Map<State, Integer> dist = new HashMap<>();
        pq.add(new Node(start, 0, new ArrayList<>()));
        dist.put(start, 0);

        int bestTime = -1;
        List<Move> bestPath = null;

        while (!pq.isEmpty()) {
            Node cur = pq.poll();
            State s = cur.state;
            if (s.equals(goal)) {
                bestTime = cur.dist;
                bestPath = cur.path;
                break;
            }
            if (cur.dist > dist.getOrDefault(s, Integer.MAX_VALUE)) continue;

            // generar movimientos
            List<Integer> people = new ArrayList<>();
            if (s.torchSide == 0) { // antorcha izquierda
                for (int i=0;i<N;i++) if ((s.mask & (1<<i))!=0) people.add(i);
            } else { // antorcha derecha
                for (int i=0;i<N;i++) if ((s.mask & (1<<i))==0) people.add(i);
            }

            for (int i=0;i<people.size();i++) {
                for (int j=i;j<people.size();j++) { // permitir 1 o 2
                    List<Integer> combo = new ArrayList<>();
                    combo.add(people.get(i));
                    if (i!=j) combo.add(people.get(j));

                    int t = 0;
                    for (int idx: combo) t = Math.max(t, times[idx]);

                    int newMask = s.mask;
                    if (s.torchSide==0) { // mov Izquierda -> Derecha
                        for (int idx: combo) newMask &= ~(1<<idx);
                        State ns = new State(newMask,1);
                        int nd = cur.dist + t;
                        if (nd < dist.getOrDefault(ns, Integer.MAX_VALUE)) {
                            dist.put(ns, nd);
                            List<Move> newPath = new ArrayList<>(cur.path);
                            newPath.add(new Move("L->R", combo, t));
                            pq.add(new Node(ns, nd, newPath));
                        }
                    } else { // mov Derecha -> Izquierda
                        for (int idx: combo) newMask |= (1<<idx);
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

        // Resultado
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

    public static void main(String[] args) {
        // Ejemplo: A=1, B=2, C=7, D=10
        int[] times = {1,2,7,10};
        solve(times);
    }
}
