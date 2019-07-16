package main;

public class Record {
    int id;
    String title;
    String des;
    String link;
    private int viewCount;

    Record(int id, String title, String des, String link, int viewCount) {
        this.id = id;
        this.title = title;
        this.des = des;
        this.link = link;
        this.viewCount = viewCount;
    }

    @Override
    public String toString() {
        return "id: " + id + "\ntitle: " + title + "\ndes: " + des + "\nlink: " + link + "\nviewCount: " + viewCount;
    }
}
