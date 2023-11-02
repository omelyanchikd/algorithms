/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
    private final int N;
    private final ST<String, Integer> teams;
    private final String[] names;
    private final int[][] teamStats;
    private final int[][] schedule;
    private Boolean[] eliminated;
    private ST<String, SET<Integer>> eliminationCertificate;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        N = Integer.parseInt(in.readLine());
        teams = new ST<String, Integer>();
        names = new String[N];
        teamStats = new int[N][3];
        schedule = new int[N][N];
        eliminated = new Boolean[N];
        eliminationCertificate = new ST<String, SET<Integer>>();
        int teamId = 0;
        while (!(in.isEmpty())) {
            String newLine = in.readLine();
            if (newLine == null) throw new IllegalArgumentException("");
            String[] line = newLine.trim().split("\\s+");
            names[teamId] = line[0];
            teams.put(line[0], teamId);
            int[] stats = new int[3];
            for (int i = 0; i < 3; i++) {
                stats[i] = Integer.parseInt(line[i + 1]);
            }
            teamStats[teamId] = stats;
            for (int i = 4; i < line.length; i++) {
                schedule[teamId][i - 4] = Integer.parseInt(line[i]);
            }
            teamId += 1;
        }
    }

    // number of teams
    public int numberOfTeams() {
        return N;
    }

    // all teams
    public Iterable<String> teams() {
        SET<String> teamNames = new SET<String>();
        for (String team : teams.keys()) {
            teamNames.add(team);
        }
        return teamNames;
    }

    // number of wins for given team
    public int wins(String team) {
        if (!isValidTeam(team)) throw new IllegalArgumentException("Null argument");
        return teamStats[teams.get(team)][0];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!isValidTeam(team)) throw new IllegalArgumentException("Null argument");
        return teamStats[teams.get(team)][1];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!isValidTeam(team)) throw new IllegalArgumentException("Null argument");
        return teamStats[teams.get(team)][2];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if ((!isValidTeam(team1)) || (!isValidTeam(team2)))
            throw new IllegalArgumentException("Null argument");
        return schedule[teams.get(team1)][teams.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!isValidTeam(team)) throw new IllegalArgumentException("Null argument");
        int teamId = teams.get(team);
        if (eliminated[teamId] != null) return eliminated[teamId];
        checkTrivialElimination(team);
        if (eliminated[teamId] == null) buildEliminationNetwork(team);
        return eliminated[teamId];
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!isValidTeam(team)) throw new IllegalArgumentException("Null argument");
        if (isEliminated(team)) {
            SET<String> certificate = new SET<String>();
            for (int i : eliminationCertificate.get(team)) {
                certificate.add(names[i]);
            }
            return certificate;
        }
        return null;
    }

    // check for trivial elimination
    private void checkTrivialElimination(String team) {
        int id = teams.get(team);
        int maxWins = teamStats[id][0] + teamStats[id][2];
        for (int i = 0; i < N; i++) {
            if (i == id) continue;
            if (maxWins < teamStats[i][0]) {
                eliminated[id] = Boolean.TRUE;
                SET<Integer> certificate = new SET<Integer>();
                certificate.add(i);
                eliminationCertificate.put(team, certificate);
            }
        }
    }

    // build max flow elimination model
    private void buildEliminationNetwork(String team) {
        int id = teams.get(team);
        int[] vertexMatching = new int[N - 1];
        for (int i = 0; i < N; i++) {
            if (i == id) continue;
            if (i > id) {
                vertexMatching[i - 1] = i;
            }
            else {
                vertexMatching[i] = i;
            }
        }
        int V = 2 + (N - 1) * (N - 2) / 2 + N - 1;
        FlowNetwork network = new FlowNetwork(V);
        int s = 0, t = V - 1;
        int v = 1;
        int gamesCounter = 1 + (N - 1) * (N - 2) / 2;
        int teamI = 0;
        for (int i = 0; i < N; i++) {
            if (i == id) continue;
            int teamJ = teamI + 1;
            for (int j = i + 1; j < N; j++) {
                if (j == id) continue;
                network.addEdge(new FlowEdge(s, v, schedule[i][j]));
                network.addEdge(new FlowEdge(v, gamesCounter + teamI, Double.POSITIVE_INFINITY));
                network.addEdge(new FlowEdge(v, gamesCounter + teamJ, Double.POSITIVE_INFINITY));
                v += 1;
                teamJ += 1;
            }
            network.addEdge(
                    new FlowEdge(gamesCounter + teamI, t,
                                 teamStats[id][0] + teamStats[id][2] - teamStats[i][0]));
            teamI += 1;
        }
        FordFulkerson maxflow = new FordFulkerson(network, s, t);
        eliminated[id] = Boolean.FALSE;
        for (FlowEdge e : network.adj(s)) {
            if (e.flow() < e.capacity()) eliminated[id] = Boolean.TRUE;
        }
        if (eliminated[id]) {
            SET<Integer> certificate = new SET<Integer>();
            for (int i = gamesCounter; i < gamesCounter + N - 1; i++) {
                if (maxflow.inCut(i))
                    certificate.add(vertexMatching[i - gamesCounter]);
            }
            eliminationCertificate.put(team, certificate);
        }
    }

    // check validity of team name
    private boolean isValidTeam(String team) {
        if (team == null) return false;
        if (!teams.contains(team)) return false;
        return true;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
