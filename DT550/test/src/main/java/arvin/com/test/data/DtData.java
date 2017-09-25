package arvin.com.test.data;

import java.util.List;

/**
 * Created by arvin on 2017/9/25 0025.
 */

public class DtData {

    private List<String> list;

    public DtData(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String get(int i) {
        if (list != null && list.size() > i) {
            return list.get(i);
        }
        return null;
    }

    public int size() {
        return list != null ? list.size() : 0;
    }
}
