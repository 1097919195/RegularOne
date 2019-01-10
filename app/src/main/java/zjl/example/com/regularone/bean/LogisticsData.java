package zjl.example.com.regularone.bean;

import java.util.List;

public class LogisticsData {
    /**
     * message : ok
     * nu : 3101775486667
     * ischeck : 0
     * condition : 00
     * com : yunda
     * status : 200
     * state : 0
     * data : [{"time":"2018-05-24 08:40:05","ftime":"2018-05-24 08:40:05","context":"[河北秦皇岛公司]进行快件扫描，发往：河北秦皇岛公司经济技术开发区分部","location":"河北秦皇岛公司"},{"time":"2018-05-23 16:44:37","ftime":"2018-05-23 16:44:37","context":"[河北秦皇岛公司]到达目的地网点，快件很快进行派送","location":"河北秦皇岛公司"},{"time":"2018-05-23 16:44:25","ftime":"2018-05-23 16:44:25","context":"[河北秦皇岛公司]到达目的地网点，快件很快进行派送","location":"河北秦皇岛公司"},{"time":"2018-05-23 07:43:42","ftime":"2018-05-23 07:43:42","context":"[北京分拨中心]从站点发出，本次转运目的地：河北秦皇岛公司","location":"北京分拨中心"},{"time":"2018-05-23 07:05:27","ftime":"2018-05-23 07:05:27","context":"[北京分拨中心]在分拨中心进行卸车扫描","location":"北京分拨中心"},{"time":"2018-05-22 01:53:04","ftime":"2018-05-22 01:53:04","context":"[重庆分拨中心]进行装车扫描，发往：北京分拨中心","location":"重庆分拨中心"},{"time":"2018-05-22 01:48:36","ftime":"2018-05-22 01:48:36","context":"[重庆分拨中心]在分拨中心进行称重扫描","location":"重庆分拨中心"},{"time":"2018-05-22 00:06:07","ftime":"2018-05-22 00:06:07","context":"[重庆渝中区杨家坪公司]进行揽件扫描","location":"重庆渝中区杨家坪公司"},{"time":"2018-05-21 21:37:17","ftime":"2018-05-21 21:37:17","context":"[重庆渝中区杨家坪公司]进行下级地点扫描，发往：北京分拨中心","location":"重庆渝中区杨家坪公司"},{"time":"2018-05-21 20:16:09","ftime":"2018-05-21 20:16:09","context":"[重庆渝中区杨家坪公司]进行揽件扫描","location":"重庆渝中区杨家坪公司"}]
     */

    private String message;
    private String nu;
    private String ischeck;
    private String condition;
    private String com;
    private int status;
    private int state;
    private List<DataBean> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNu() {
        return nu;
    }

    public void setNu(String nu) {
        this.nu = nu;
    }

    public String getIscheck() {
        return ischeck;
    }

    public void setIscheck(String ischeck) {
        this.ischeck = ischeck;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * time : 2018-05-24 08:40:05
         * ftime : 2018-05-24 08:40:05
         * context : [河北秦皇岛公司]进行快件扫描，发往：河北秦皇岛公司经济技术开发区分部
         * location : 河北秦皇岛公司
         */

        private String time;
        private String ftime;
        private String context;
        private String location;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getFtime() {
            return ftime;
        }

        public void setFtime(String ftime) {
            this.ftime = ftime;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}
