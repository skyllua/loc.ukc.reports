package loc.ukc.reports;

public class Issue {
    private String name;
    private int level;
    private int[] id;

    public Issue(String name, int level, int[] id) {
        this.name = name;
        this.level = level;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int[] getId() {
        return id;
    }
}
