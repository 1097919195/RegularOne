package zjl.example.com.regularone.bean;

import java.util.List;

public class VideoData {
    /**
     * error : false
     * results : [{"_id":"5aff4645421aa95f55cab5f4","createdAt":"2018-05-15T00:00:00.0Z","desc":"我吃都没有这么讲究过。。😂😂","publishedAt":"2018-05-15T00:00:00.0Z","source":"web","type":"休息视频","url":"https://www.iesdouyin.com/share/video/6554614997924711684/?region=CN&mid=6554615056712239886&titleType=title_CN_2&utm_source=copy_link&utm_campaign=client_share&utm_medium=android&app=aweme&iid=32312367085&timestamp=1526569521","used":true,"who":"lijinshanmx"},{"_id":"56de2f2c6776592b6192bf48","createdAt":"2016-03-08T09:47:24.755Z","desc":"妇女节快乐：【One Day I Will 】Google庆祝2016国际妇女节短片","publishedAt":"2016-03-08T12:55:59.161Z","source":"chrome","type":"休息视频","url":"http://weibo.com/p/230444d3263ce9d1a1c46cafb56b1033c8feb6","used":true,"who":"lxxself"},{"_id":"56cc6d1d421aa95caa7075e7","createdAt":"2015-05-25T09:25:17.212Z","desc":"阿三又开挂啦！以前的达人秀都弱爆了！全程高能无尿点！\n","publishedAt":"2015-05-26T12:31:59.877Z","type":"休息视频","url":"http://video.weibo.com/show?fid=1034:143f2668ef7b509c0e9d323b16a6c138","used":true,"who":"YJX"},{"_id":"578c4e1c421aa90df33fe7c0","createdAt":"2016-07-18T11:33:48.193Z","desc":"褪下网路美女外衣之后的真实世界","publishedAt":"2016-07-18T11:49:19.322Z","source":"chrome","type":"休息视频","url":"http://v.qq.com/x/page/h0306fwf13h.html","used":true,"who":"代码家"},{"_id":"58587eca421aa97240ef9efa","createdAt":"2016-12-20T08:43:54.995Z","desc":"2016年清华本科生特奖答辩\u2014\u2014计算机系陈立杰","publishedAt":"2016-12-20T11:48:13.616Z","source":"chrome","type":"休息视频","url":"https://v.qq.com/x/page/d0345ygll9e.html","used":true,"who":"daimajia"},{"_id":"57cebfd4421aa91a108fc6dc","createdAt":"2016-09-06T21:08:36.812Z","desc":"【文曰小强】6分钟看完2016雨果奖《北京折叠》原著","publishedAt":"2016-09-08T11:43:04.471Z","source":"chrome","type":"休息视频","url":"http://www.bilibili.com/video/av6128318/","used":true,"who":"LHF"},{"_id":"58ae6af7421aa957f9dd6dc3","createdAt":"2017-02-23T12:54:15.286Z","desc":"我媽要我告訴你 ","publishedAt":"2018-03-12T08:44:50.326Z","source":"chrome","type":"休息视频","url":"https://v.qq.com/x/page/m0377ib544o.html?start=1","used":true,"who":"lxxself"},{"_id":"5823372a421aa91369f95a1b","createdAt":"2016-11-09T22:48:10.70Z","desc":"新鲜的四六级资料已到货：特朗普胜选演讲双语字幕版 ","publishedAt":"2016-11-10T11:40:42.4Z","source":"chrome","type":"休息视频","url":"http://weibo.com/tv/v/EgMsAbOaI?fid=1034:037eab745ddcbc30e436e78cdae67900","used":true,"who":"lxxself"},{"_id":"56f8c5ac677659164d564434","createdAt":"2016-03-28T13:48:28.800Z","desc":"Google搜索中的彩蛋","publishedAt":"2016-04-01T11:17:05.676Z","source":"chrome","type":"休息视频","url":"http://open.163.com/movie/2016/3/E/U/MBI38BMFD_MBI38FIEU.html","used":true,"who":"JohnTsai"},{"_id":"57517beb421aa9565763b41e","createdAt":"2016-06-03T20:45:31.139Z","desc":"【毕业季，不分手】我最羡慕能从学生时代走到婚姻的感情，彼此约定，互不分离。","publishedAt":"2016-06-15T11:55:46.992Z","source":"chrome","type":"休息视频","url":"http://weibo.com/p/23044451f0e5c4b762b9e1aa49c3091eea4d94","used":true,"who":"lxxself"}]
     */

    private boolean error;
    private List<ResultsBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * _id : 5aff4645421aa95f55cab5f4
         * createdAt : 2018-05-15T00:00:00.0Z
         * desc : 我吃都没有这么讲究过。。😂😂
         * publishedAt : 2018-05-15T00:00:00.0Z
         * source : web
         * type : 休息视频
         * url : https://www.iesdouyin.com/share/video/6554614997924711684/?region=CN&mid=6554615056712239886&titleType=title_CN_2&utm_source=copy_link&utm_campaign=client_share&utm_medium=android&app=aweme&iid=32312367085&timestamp=1526569521
         * used : true
         * who : lijinshanmx
         */

        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }
    }
}
