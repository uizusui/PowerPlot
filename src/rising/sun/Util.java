package rising.sun;

import java.io.*;
//import java.util.*;
//import java.util.regex.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
//import android.util.Log;
import android.widget.Toast;

//------------------------------------------------------------------
//	汎用ごった煮クラス
//		汎用メソッドを持つ
//		ゲーム全体で共有したい情報を持つ
//------------------------------------------------------------------
//どこからでも参照できるが，継承されなくするためにpublic final class
public final class Util
{
	public static String gameVersion = "version 0.0.1";		// バージョン情報の文字列(setversionタグで指定される)
	public static String gameTitle = "PowerPlot";			// ゲームのタイトル(setgametitleタグで指定される)

	public static int standardDispWidth = 240;		// 標準の解像度
	public static int standardDispHeight = 160;		// この解像度を基準として製作
	public static int dispWidth = 1280;				// ディスプレイの実際の解像度(Galaxy Noteの解像度で初期化
	public static int dispHeight = 720;				// これにあわせる
	public static int dispRate = 4;					// 拡大率
	public static int drawAreaWidth = 960;			// 拡大を適用した後のサイズ
	public static int drawAreaHeight = 640;			// standardDispX * rare
	public static int basePointX = 160;				// 描画開始位置x
	public static int basePointY = 40;				// 描画開始位置y
	public static int endPointX = 1120;				// 描画終了位置x
	public static int endPointY = 680;				// 描画終了位置y

	public static int quakeX = 0;					// 画面揺らしの補正
	public static int quakeY = 0;					// 画面揺らしの補正
	
	//追加部分
	public static int dw2,dh2;					//画面中心
	public static int dn;						//倍率
	public static int betw,beth;				//調整用
	
	public static int nitiji1x,nitiji1y,nitiji2x,nitiji2y,nitiji3x,nitiji3y;
	public static int nitiji1ex,nitiji1ey,nitiji2ex,nitiji2ey,nitiji3ex,nitiji3ey;
	
	public static Rect ni,ni1,ni2,ni3;
	
	public static int lsp,usp;						//左スペース，上スペース
	public static int selectA;						//選択エリア
	public static int menucx,menucy;				//メニュー選択テキスト左上
	public static int daysx,daysy;
	
	public static int htx,hty,htex,htey;
	public static int mex,mey,meex,meey;
	public static int lux,luy,luex,luey;
	
	public static int selecta[] = new int[4];
	public static int selectb[] = new int[4];
	public static int selectc[] = new int[4];
	public static int selectd[] = new int[4];
	
	public static Rect baseRect; //全体の矩形
	public static Rect src;	//　描画元の矩形イメージ
	public static Rect dst;	//　描画先の矩形イメージ
	public static Rect wst;	//　ウィンドウの矩形イメージ
	public static Rect dayst;	//なにをしようかなの時の矩形イメージ
	public static Rect clrc;	//左キャラクターの大きさ
	public static Rect clst;	// 左キャラクターの矩形
	public static Rect crst;	// 右キャラクターの矩形
	public static Rect cerc;	//　センター背景の大きさ
	public static Rect cest;	// センター背景の矩形
	public static Rect menuc;	//メニューの矩形
    public static Rect selrc;	//選択ポップアップの矩形
	public static Rect select[] = new Rect[4];	//セレクトの矩形
	public static Rect dayselect[] = new Rect[6];	//毎日選択の矩形
	public static Rect mapselect[] = new Rect[5];	//マップセレクトの矩形
	public static Rect smap[] = new Rect[10];		//マップオブジェクトの矩形
	
	public static Bitmap[] selectf = new Bitmap[4];
	
	public static float imageScale = 1f;			// 画像の読み込みスケール 一時保存場所(再起動後に有効にするため)

	public static Context context = null;
	public static Activity activity = null;

	public static String latestSaveDate = "--/--/-- --:--:--";	// 受け渡し用
	public static String latestSaveText = "no data";			// 受け渡し用

	public static boolean enableSdCard = true;

	// ディスプレイサイズと拡大率(初回に設定)
	static public void setDispSize(int width, int height)
	{
		dispWidth = width;
		dispHeight = height;

		int widthRate = dispWidth / standardDispWidth;
		int heightRate = dispHeight / standardDispHeight;
		if(widthRate <= heightRate){ //拡大率の小さい方を使用
			dispRate = widthRate;
			dn = dispRate;
		}else{
			dispRate = heightRate;
			dn = dispRate;
		}
		//ここまでで拡大率が決定
		
		drawAreaWidth = standardDispWidth * dn;
		drawAreaHeight = standardDispHeight * dn;
		dw2 = width / 2 ;			//画面中心x
		dh2 = height / 2 ;			//画面中心y
		basePointX = dw2 - ( standardDispWidth * dispRate ) / 2; //画面中心座標x-表示領域x/2 →　描画開始x
		basePointY = dh2 - ( standardDispHeight * dispRate ) / 2; //描画開始y
		endPointX = dw2 + ( standardDispWidth * dispRate ) / 2; //画面中心座標x-表示領域x/2 →　描画終了x
		endPointY = dh2 + ( standardDispHeight * dispRate ) / 2; //描画終了y
		baseRect = new Rect(basePointX,basePointY,endPointX,endPointY);
		betw = dw2 - standardDispWidth * dn / 2;
		beth = dh2 - standardDispHeight * dn / 2;
		//ここまでで描画開始点が確定
		
		//読み込み用の画像サイズ(基準値)
		src = new Rect(0, 0, 239, 159);						//画面の解像度		240x180
		clrc = new Rect(0,0,63,63);							//キャラクターの解像度	64x64
		selrc = new Rect(0,0,47,47);						//メニューのキャラクタ		48x48
		cerc = new Rect(0,0,119,79);						//背景				120x80
        cest = new Rect(84,66,156,114);						//?
        
        //実際の描画用のサイズ
		dst = createR(0,0,240,160);							//全体
		wst = createR(18,110,222,160);						//ウィンドウ
		dayst = createR(18,103,222,125);
		clst = createR(10,36,73,103);						//左キャラクター
        crst = createR(166,36,229,103);						//右キャラクター
        menuc = new Rect(dw2-115*dn,dh2-75*dn,dw2-67*dn,dh2-27*dn);
        
        //メニュー選択部分
        select[0] = createR(0,60,59,149);
        select[1] = createR(60,60,119,149);
        select[2] = createR(120,60,179,149);
        select[3] = createR(180,60,239,149);
        
        //毎日選択部分
        dayselect[0] = createR(5,130,35,165);
        dayselect[1] = createR(45,130,75,165);
        dayselect[2] = createR(85,130,115,165);
        dayselect[3] = createR(125,130,155,165);
        dayselect[4] = createR(165,130,195,165);
        dayselect[5] = createR(205,130,235,165);
        
        //マップ選択部分
        mapselect[0] = createR(0,0,240,25);		//上	↑
        mapselect[1] = createR(0,135,240,160);	//下	↓
        mapselect[2] = createR(0,25,20,135);	//左	←
        mapselect[3] = createR(220,25,240,135);	//右	→
        mapselect[4] = createR(20,25,220,135);	//真ん中
        
        //マップオブジェクト部分（随時追加
        smap[0] = new Rect(150,100,200,150);
        
        menucx = dw2-60*dn;	//メニュー選択時の吹き出し文字の位置
        menucy = dh2-55*dn;
        daysx = 25*dn+betw;	//毎日選択時の文字の位置
        daysy = 119*dn+beth;
        	       
        nitiji1x = dw2 - 110 * dn;nitiji1y = dh2 - 75 * dn;
        nitiji1ex = dw2 - 91 * dn;nitiji1ey = dh2 - 50 * dn;
        nitiji2x = dw2 - 90 * dn;nitiji2ex = dw2 - 71 * dn;
        nitiji3x = dw2 - 70 * dn;nitiji3ex = dw2 - 51 * dn;
        
        ni = new Rect(0,0,20,26);
        ni1 = new Rect(nitiji1x,nitiji1y,nitiji1ex,nitiji1ey);
        ni2 = new Rect(nitiji2x,nitiji1y,nitiji2ex,nitiji1ey);
        ni3 = new Rect(nitiji3x,nitiji1y,nitiji3ex,nitiji1ey);
        
        lsp = dw2 - 120 * dn ;
        usp = dh2 - 80 * dn ;
        
        
        
        htx = dw2 + 20 * dn ; hty = dh2 - 76 * dn ; htey = dh2 - 70 * dn ;
        mex = dw2 + 15 * dn ; mey = dh2 - 65 * dn ; meey = dh2 - 59 * dn ;
        lux = dw2 + 10 * dn ; luy = dh2 - 54 * dn ; luey = dh2 - 48 * dn ;
	}
	
	//Rect作成関数,元々の解像度でのRectを入れると拡大したRectを返す
	static public Rect createR(int sx,int sy,int ex,int ey){
		//Rect rect = new Rect(sx*dn+betw,sy*dn+beth,ex*dn+betw-1,ey*dn+beth-1);
		Rect rect = new Rect(sx*dn,sy*dn,ex*dn,ey*dn);
		return rect;
	}
	
	//画面4分割の選択0-3計算関数
	public static void selectArea(int x){
		selectA = ( x - lsp ) / (60 * dn) ;
	}

	// トーストメッセージ
	static private Toast toastLog = null;
	static public void toastMessage(String message)
	{
		if(toastLog != null)
			toastLog.cancel();
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.show();
		toastLog = toast;
	}

	// ダイアログメッセージ MainSurfaceViewから呼ぶと落ちる
	static private AlertDialog.Builder ad = null;
	static public void dialogMessage(String message)
	{
		if(ad != null)
			return;
		ad = new AlertDialog.Builder(activity);
		ad.setTitle("");
		ad.setMessage(message);
		ad.setPositiveButton("OK", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				activity.setResult(Activity.RESULT_OK);
				ad = null;
			}
		});
		ad.create();
		ad.show();
	}

	// 与えられた文字列が数値ならtrue
	static public boolean isNumber(String str)
	{
		try
		{
			Float.parseFloat(str);
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
//		Pattern p = Pattern.compile("-?([0-9]+\\.?[0-9]+)|([0-9]+)");
//		return Pattern.compile("^\\-?([0-9]+\\.[0-9]+)|([0-9]+)$").matcher(str).find();
	}
}
