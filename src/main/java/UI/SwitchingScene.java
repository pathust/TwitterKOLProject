package UI;

public interface SwitchingScene {
    public void switchToSearching();
    public void switchToWaiting();
    public void switchToDisplayKOL();
    public void switchToDisplayTweet();
    public void closeHome();
    public void closeWaiting();
    public void closeDisplay();
    public void switchToAddFile();
}