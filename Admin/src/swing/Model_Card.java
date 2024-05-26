package swing;


public class Model_Card {

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public Model_Card(String title, String values, int count) {
        this.count = count;
        this.title = title;
        this.values = values;
    }

    public Model_Card() {
    }

    private int count;
    private String title;
    private String values;
}
