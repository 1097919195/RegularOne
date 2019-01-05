package zjl.example.com.regularone.bean;

import java.util.List;

public class StatisticsData {
    /**
     * code : 200
     * msg : 成功!
     * data : [{"type":"点击统计","typeId":1,"count":259011659},{"type":"注册统计","typeId":0,"count":118},{"type":"点击统计","typeId":2,"count":76},{"type":"点击统计","typeId":34,"count":34896},{"type":"ekt","typeId":9912,"count":2},{"type":"点击统计","typeId":6,"count":204},{"type":"点击统计","typeId":100,"count":201},{"type":"书名A","typeId":111,"count":222},{"type":"使用","typeId":10,"count":1},{"type":"我的最爱","typeId":1222,"count":2},{"type":"信心统计","typeId":12,"count":4},{"type":"嘿嘿","typeId":123456,"count":7001},{"type":"ctpv","typeId":989,"count":1},{"type":"点击统计","typeId":3,"count":38},{"type":"点击统计","typeId":12312,"count":123216},{"type":"5","typeId":5,"count":81},{"type":"点击统计","typeId":4,"count":23},{"type":"点击统计","typeId":9,"count":23}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * type : 点击统计
         * typeId : 1
         * count : 259011659
         */

        private String type;
        private int typeId;
        private int count;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
