package zjl.example.com.regularone.ui.news.presenter;


import com.jaydenxiao.common.baserx.RxSubscriber;

import java.util.List;

import zjl.example.com.regularone.R;
import zjl.example.com.regularone.bean.PhotoGirl;
import zjl.example.com.regularone.ui.news.contract.PhotosListContract;

/**
 * des:图片列表
 * Created by xsf
 * on 2016.09.12:01
 */
public class PhotosListPresenter extends PhotosListContract.Presenter{
    @Override
    public void getPhotosListDataRequest(int size, int page) {
             mRxManage.add(mModel.getPhotosListData(size,page).subscribeWith(new RxSubscriber<List<PhotoGirl>>(mContext,false) {
                 @Override
                 public void onStart() {
                     super.onStart();
                     mView.showLoading(mContext.getString(R.string.loading));
                 }
                 @Override
                 protected void _onNext(List<PhotoGirl> photoGirls) {
                     mView.returnPhotosListData(photoGirls);
                     mView.stopLoading();
                 }

                 @Override
                 protected void _onError(String message) {
                     mView.showErrorTip(message);
                 }
             }));
    }
}
