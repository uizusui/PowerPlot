package rising.sun;

import rising.sun.MainSurfaceView;
import rising.sun.Util;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class PowerActivity extends Activity {
    /** Called when the activity is first created. */
	MainSurfaceView mainView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
     // 画面サイズ取得、Utilクラスに教える
		WindowManager manager = getWindow().getWindowManager();
		Display disp = manager.getDefaultDisplay();
		Util.setDispSize(disp.getWidth(), disp.getHeight());
		
		/*	SurfaceViewの配置
	    LinearLayout layout = new LinearLayout(this);  
	    layout.setBackgroundColor(Color.GREEN);  
	    View view = new View(this);  
	    view.setBackgroundColor(Color.YELLOW);  
	    view.setLayoutParams(new LayoutParams(320,44));  
	    layout.addView(view);  
	    setContentView(layout);  
	    */
		
		LinearLayout layout = new LinearLayout(this);	//レイアウトを作成（親）
	    layout.setBackgroundColor(Color.GREEN);			//レイアウトの背景を設定
	    layout.setGravity(Gravity.CENTER);				//レイアウトの子要素を中心に来るように設定
	    
		
		// メインのviewを生成
		mainView = new MainSurfaceView(this.getApplicationContext());
		LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(Util.drawAreaWidth,Util.drawAreaHeight);
		mainView.setLayoutParams(l);	//Viewの大きさを設定（240x4,160x4）

		// ビューの表示
		layout.addView(mainView);	//レイアウトにビューを登録（mainViewがlayoutの子要素になる）
		setContentView(layout);		//レイアウトを表示
    }
}