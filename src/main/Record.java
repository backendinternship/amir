package main;

public class Record {
    public int id;
    public String title;
    public String des;
    public String link;
    public int viewCount;

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
