package hao.bk.com.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import hao.bk.com.utils.Util;
import hao.bk.com.vdmvsi.R;
public class ViewToolBar {
	String tag = this.getClass().getSimpleName();
	
	Context context;
	View viewRoot;
	
	protected Toolbar toolbar;
	protected EditText edtSearch;
	protected ImageView  imgRight;
	protected TextView tvTitle;
	
	public ViewToolBar(Context context, View viewRoot) {
		this.context = context;
		this.viewRoot = viewRoot;
		toolbar = (Toolbar)viewRoot.findViewById(R.id.toolbar);
		edtSearch = (EditText)viewRoot.findViewById(R.id.edt_search);
		imgRight = (ImageView)viewRoot.findViewById(R.id.ic_right_act);
		tvTitle = (TextView)viewRoot.findViewById(R.id.tv_title);
	}
	public EditText getEdtSearch(){
		return edtSearch;
	}
	public void showButtonBack(boolean show) {
		Util.LOGD(tag, "---showActionbar: show=" + show);
		if(show){
			toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
			toolbar.setNavigationOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	Util.LOGD("ViewToolBar", "onclick navigation---------");
	                ((Activity)context).onBackPressed();
	            }
	        });
			edtSearch.setVisibility(View.INVISIBLE);
			imgRight.setVisibility(View.INVISIBLE);
		}else{
			toolbar.setNavigationIcon(null);
		}
	}
	public void showTextTitle(String title){
		if(toolbar!=null){
			Util.LOGD(tag, "title setToolBar:" + title);
			if(TextUtils.isEmpty(title)){
				tvTitle.setVisibility(View.GONE);
			}else{
				edtSearch.setVisibility(View.INVISIBLE);
				imgRight.setVisibility(View.INVISIBLE);
				tvTitle.setText(title);
				tvTitle.setVisibility(View.VISIBLE);
			}
		}
	}
	public void showButtonCancel(boolean show, View.OnClickListener listener) {
		Util.LOGD(tag, "---showActionbar: show="+show);
		if(show){
			toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
			toolbar.setNavigationOnClickListener(listener);
		}
	}

}
