package cz.tomkren.pikater;

import java.util.List;

public class SimpleDag {

    private final int id;
    private final String name;
    private final List<LinkTarget> targets;

    public static class LinkTarget {
        private final int id;
        private final int port;

        public LinkTarget(int id, int port) {this.id = id; this.port = port;}
        public int getId() {return id;}
        public int getPort() {return port;}
    }

    public SimpleDag(int id, String name, List<LinkTarget> targets) {
        this.id = id;
        this.name = name;
        this.targets = targets;
    }

    public int getId() {return id;}
    public String getName() {return name;}
    public List<LinkTarget> getTargets() {return targets;}

}



