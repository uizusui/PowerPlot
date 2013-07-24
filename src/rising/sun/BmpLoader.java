package rising.sun;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

class BitmapDetail{
	Bitmap bmp;
	Rect rect;
	
	public BitmapDetail(Bitmap bmp,Rect r){
		this.bmp = bmp;
		this.rect = r;
	}
}

//画像は全てこの画像保持クラスから呼び出し(したい)
public class BmpLoader {
	static Resources res;
	
	static Map<String,BitmapDetail> map = new HashMap<String,BitmapDetail>();
	
	static BitmapFactory.Options opt = new BitmapFactory.Options();
	
    
    public BmpLoader(Resources res){			//コンストラクタ
    	BmpLoader.res = res;
    	opt.inScaled = false;				//Samsung対策
    }
    
    public static void setBitmap(String str){
    	int charId = res.getIdentifier(str, "drawable", "rising.sun");//charIdに入っているstrよりリソースのID番号を取得
    	Bitmap bmp = BitmapFactory.decodeResource(res, charId, opt);
    	Rect r = new Rect(0,0,bmp.getWidth()-1,bmp.getHeight()-1);
		map.put(str,new BitmapDetail(bmp,r));
    }
    
    public static Bitmap getBitmap(String str){
    	return map.get(str).bmp;
    }
    
    public static Rect getRect(String str){
    	return map.get(str).rect;
    }

}
