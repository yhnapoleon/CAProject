//By Zhao Jiayi

package sg.nusiss.t6.caproject.util;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DataResult implements Code{
    private int code;
    private Object data;
    private String message;

    public DataResult() {
    }

    public DataResult(int code, Object data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

}