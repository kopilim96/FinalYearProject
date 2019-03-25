package kopilim.scs.prototyping;

public class information {
    private String title;
    private int thumbnail;

    public information(){

    }

    public information(String title, int thumbnail){
        this.thumbnail = thumbnail;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
