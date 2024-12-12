package pachinko.model;

public class StoreData {
    private int machineNumber;
    private int totalGames;
    private int diffMedals;
    private int bb;
    private int rb;
    private String combinedProb;
    private String bbProb;
    private String rbProb;
    

    // ゲッターとセッターを追加
    public int getMachineNumber() {
        return machineNumber;
    }

    public void setMachineNumber(int machineNumber) {
        this.machineNumber = machineNumber;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(int totalGames) {
        this.totalGames = totalGames;
    }

    public int getDiffMedals() {
        return diffMedals;
    }

    public void setDiffMedals(int diffMedals) {
        this.diffMedals = diffMedals;
    }

    public int getBb() {
        return bb;
    }

    public void setBb(int bb) {
        this.bb = bb;
    }

    public int getRb() {
        return rb;
    }

    public void setRb(int rb) {
        this.rb = rb;
    }

    public String getCombinedProb() {
        return combinedProb;
    }

    public void setCombinedProb(String combinedProb) {
        this.combinedProb = combinedProb;
    }

    public String getBbProb() {
        return bbProb;
    }

    public void setBbProb(String bbProb) {
        this.bbProb = bbProb;
    }

    public String getRbProb() {
        return rbProb;
    }

    public void setRbProb(String rbProb) {
        this.rbProb = rbProb;
    }
    private String modelName; // 機種名を追加
    // 他のフィールド (machineNumber, totalGames など) の定義

    // Getter と Setter
    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
