package preparedata;

import java.util.ArrayList;

/**
 * Created by secret on 16-3-19.
 */
public class wordbag {
    private String skuid;
    private ArrayList<String> typelist;
    public wordbag() {

    }

    public void setWordBag(String skuid, ArrayList<String> typelist) {
        this.skuid = skuid;
        this.typelist = typelist;
    }

    public String getSkuid() {
        return skuid;
    }

    public ArrayList<String> getTypeList() {
        return typelist;
    }
}
