package loc.ukc.reports;

public class ProblematicIssues {
    private int idPR_lvl_1;
    private int idPR_lvl_2;
    private int count;

    public ProblematicIssues(int idPR_lvl_1, int idPR_lvl_2, int count) {
        this.idPR_lvl_1 = idPR_lvl_1;
        this.idPR_lvl_2 = idPR_lvl_2;
        this.count = count;
    }

    public int getIdPR_lvl_1() {
        return idPR_lvl_1;
    }

    public int getIdPR_lvl_2() {
        return idPR_lvl_2;
    }

    public int getCount() {
        return count;
    }


    @Override
    public String toString() {
        return idPR_lvl_1 + "\t " + idPR_lvl_2 + "\t " + count;
    }
}
