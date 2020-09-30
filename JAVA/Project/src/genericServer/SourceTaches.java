package genericServer;

public interface SourceTaches {
    public Runnable getTache() throws InterruptedException;
    public boolean areMoreTaches();
    public void addTache(Runnable r);
}
