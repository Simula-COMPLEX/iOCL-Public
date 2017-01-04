package no.simule.dataobjects;

public class Prop {
    private String key;
    private String value;
    private Boolean enable;

    public Prop(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Prop(String key, String value, Boolean enable) {
        this.key = key;
        this.value = value;
        this.enable = enable;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
