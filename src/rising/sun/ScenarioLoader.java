package rising.sun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

public class ScenarioLoader {
	static Resources re = null;
	BufferedReader br = null;		//読み込み用
	AssetManager as = null;			//assetフォルダの読み込み用  
    InputStream is = null;			//読み込み用
    StringBuilder sb = null;		//ストリングビルダー
    StringBuilder sb2 = null;
    StringBuilder sb3 = null;
    
    BitmapFactory.Options opt = new BitmapFactory.Options();
    
    int touchwait;					//タッチイベント待ちフラグ
    int onlock;
    int menulock;
    int menuselect;
    
    int niti;						//何日目
    
    int newline;					//新しい文字列
    
    Bitmap oplg;
    Bitmap titlg;
    Bitmap menug;
    Bitmap backg;
    Bitmap nazug;
    Bitmap mapg;
    
    String menus[] = new String[4];
    String days[] = new String[6];
    
    int mapobj = 0;
    
    Bitmap oplogo;					//ロゴ
    Bitmap title;					//タイトルロゴ
    Bitmap menu;					//タイトルロゴ
    Bitmap hukidashi;				//メニューの吹き出し
    Bitmap komachara;				//一時的
    Bitmap nazu;					//ナズりん
    Bitmap msuji;
    Bitmap suuji[] = new Bitmap[3];					//何日目
    Rect menur[] = new Rect[4];			//メニューに表示されるキャラ4人分

    Rect oplogor;					//ロゴの大きさ(240x160)
    Rect titler;
    
    char temp;						//一文字読み込む用
    char stemp;						//スクリプト読み込み用
    StringBuilder ssb = null;		//スクリプト用ストリングビルダー
    StringBuilder ssb2 = null;
    StringBuilder ssb3 = null;
    String ss = null;
    String ss2 = null;
    String ss3 = null;
    int waittime = 5;				//待ち時間(暫定)
	int counter;					//待ち時間カウント(暫定)
	
	String str = null;				//文字の表示用バッファ
	String str2 = null;				//2行目用バッファ
	String str3 = null;				//3行目用バッファ
	
	Paint p = new Paint();			//文字の大きさ・カラー設定
	Paint menup = new Paint();
	int textsi;						//文字の基本のフォントサイズ
	
	int onscreenf;					//タッチされている状態が1
	int onscreenTime;				//タッチされている時間(フレーム)
	int fup;
	int smenu;
	float pointx,pointy;			//タッチされている座標
	float pradius;					//半径
	int smapx,smapy;
	int sbx,sby,sex,sey;		//一時的に作成（使用検討中）
	
	int lcflag;
	int rcflag;
	int centflag;
	Bitmap leftc;					//左側キャラクター
	Bitmap createl;
	Bitmap createl2;
	int leftcx,leftcy;				//左側画像内番号
	Bitmap rightc;					//右側キャラクター
	Bitmap center;					//センター背景
	int rightcx,rightcy;			//左側画像内番号
	Bitmap creater;
	Bitmap creater2;
	
	Rect mapr;
	int mapx=50,mapy=50;			//マップ表示位置（初期位置）
	
	int hitp = 30;	//体力
	int metp = 0;	//気力
	int luck = 0;	//運気
	Paint hp = new Paint();
	Paint mp = new Paint();
	Paint lp = new Paint();
	Rect hitpr;
	Rect metpr;
	Rect luckr;
	

	public ScenarioLoader(Context c, Resources res){		//コンストラクタ(コンテキストを入れて作成)
		re = res;
		as = c.getResources().getAssets();	//アセットマネージャーを作成
		sb = new StringBuilder();			//ストリングビルダーを初期化
		sb2 = new StringBuilder();
		sb3 = new StringBuilder();
		ssb = new StringBuilder();			//スクリプト読み込み用ストリングビルダー
		ssb2 = new StringBuilder();			//スクリプト指定用ストリングビルダー
		ssb3 = new StringBuilder();
		
		opt.inScaled = false;				//Samsung対策
		
		try {
			is = as.open("first.txt");		//InputStreamにfirst.txtを読み込み
			br = new BufferedReader(new InputStreamReader(is));//BufferedReaderに更に読み込み
			temp = (char)br.read();			//一文字読み込み
			sb.append(temp);				//StringBuilderに追加
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		touchwait = 0;
		onlock = 0;
		menulock = 0;
		menuselect = 0;
		newline = 0;
		
		niti = 147;
		
		onscreenf = 0;
		onscreenTime = 0;
		fup = 0;
		smenu = 0;
		pradius = (float) 5.0;
		
		lcflag = 0;
		rcflag = 0;
		centflag = 0;
		
		textsi = 12;
		
		p.setColor(Color.GREEN);	//碧に設定(仮)
		p.setTextSize(12);			//文字の大きさ
		p.setAntiAlias(false);		//アンチエイリアス＞オン
		
		menup.setColor(Color.rgb(127, 127, 0));
		//setNiti();
		//changeNiti();
		//oplg = new BitmapLoader("balloobluene");
		//oplg = BitmapLoader.getBitmap("ballobluene");
		
		//backg = BitmapLoader.getBitmap("superbottom");
		//nazug = BitmapLoader.getBitmap("image3");
		//mapg = BitmapLoader.getBitmap("smap");
		
		createMap();
		
		hitpr = new Rect(Util.htx, Util.hty, Util.htx + (int)(80.0*((double)50/100.0)) * Util.dn ,Util.htey);
		metpr = new Rect(Util.mex, Util.mey, Util.mex + (int)(20.0*((double)50/100.0)) * Util.dn ,Util.meey);
		luckr = new Rect(Util.lux, Util.luy, Util.lux + (int)(30.0*((double)50/100.0)) * Util.dn ,Util.luey);
		hp.setColor(Color.RED);mp.setColor(Color.BLUE);lp.setColor(Color.GREEN);
	}
	
	/*
	public void setBmp(BitmapLoader bl){	//試作関数
		int charId = re.getIdentifier(bl.str, "drawable", "rising.sun");//charIdに入っているstrよりリソースのID番号を取得
		bl.bmp = BitmapFactory.decodeResource(re, charId, opt);		
		bl.rect = new Rect(0,0,bl.bmp.getWidth(),bl.bmp.getHeight());
	}
	*/
	
	/*
	public void setOplogo(){		//起動時ロゴの指定(今は直接指定)
		int charId = re.getIdentifier("balloobluene", "drawable", "rising.sun");//charIdに入っているstrよりリソースのID番号を取得
		oplogo = BitmapFactory.decodeResource(re, charId, opt);		
		oplogor = new Rect(0,0,oplogo.getWidth(),oplogo.getHeight());
	}
	
	public void setTitle(){			//タイトル画像の指定(今は直接指定)
		int charId = re.getIdentifier("image2", "drawable", "rising.sun");
		title = BitmapFactory.decodeResource(re, charId, opt);
		titler = new Rect(0,0,title.getWidth(),title.getHeight());
	}
	*/
	
	public void setMenu(){			//メニュー画面の指定(今は直接指定)
		int charId = re.getIdentifier("menu", "drawable", "rising.sun");
		menu = BitmapFactory.decodeResource(re, charId, opt);
	}
	
	public void sethuki(){			//吹き出しの指定(今は直接指定)
		int charId = re.getIdentifier("hukidashi", "drawable", "rising.sun");
		hukidashi = BitmapFactory.decodeResource(re, charId, opt);
	}
	public void setkoma(){
		int charId = re.getIdentifier("komachara", "drawable", "rising.sun");
		komachara = BitmapFactory.decodeResource(re, charId, opt);
	}
	public void setnazu(){
		int charId = re.getIdentifier("nazu7", "drawable", "rising.sun");
		nazu = BitmapFactory.decodeResource(re, charId, opt);
	}
	public void setNiti(){			//起動時
		int charId = re.getIdentifier("suuji", "drawable", "rising.sun");
		msuji = BitmapFactory.decodeResource(re, charId, opt);
		suuji[0]= Bitmap.createBitmap(msuji, 0, 0, 20, 26);
		suuji[1]= Bitmap.createBitmap(msuji, 60, 0, 20, 26);
		suuji[2]= Bitmap.createBitmap(msuji, 120, 0, 20, 26);
	}
	public void changeNiti(){
		int n1,n2,n3;
		n1 = niti / 100;
		n2 = (niti % 100) / 10;
		n3 = niti % 10;
		suuji[0]= Bitmap.createBitmap(msuji, n1*20, 0, 20, 26);
		suuji[1]= Bitmap.createBitmap(msuji, n2*20, 0, 20, 26);
		suuji[2]= Bitmap.createBitmap(msuji, n3*20, 0, 20, 26);
	}
	public void setMenuc(){
		menur[0] = komar(2,4);
		menur[1] = komar(2,1);
		menur[2] = komar(2,3);
		menur[3] = komar(2,2);
		menus[0] = "しなりおをきどうするよ。";
		menus[1] = "みにげーむをするよ。";
		menus[2] = "でーたをみるよ。";
		menus[3] = "そのただよ。";
	}
	
	public void setDays(){
		days[0] = "せめこみます";
		days[1] = "うろつきます";
		days[2] = "やすみます";
		days[3] = "かんゆうします";
		days[4] = "でーたをみます";
		days[5] = "せーぶします";
	}
	
	public Rect komar(int x,int y){
		Rect rect = new Rect(x * 64,y * 64, (x+1)*64-1,(y+1)*64-1);
		return rect;
	}
	
	public void createMap(){
		mapr = new Rect(mapx,mapy,mapx+240,mapy+160);
	}
	
	public void next(){
		if(touchwait == 0){					//タッチ待ちのフラグがオフなら
			counter++;						//カウンターをカウントアップ
		}
		if(counter > waittime && smenu == 0){				//カウンターが待ち時間を超えていたら実行
			counter = 0;					//カウンターをリセット＜ここにあるべきでない?
			try {
				temp = (char)br.read();		//一文字読み込み
				} catch (IOException e) {
					e.printStackTrace();
				}
			if(newline == 1){			//改行フラグがオンの時
				if(sb2!=null)sb3 = sb2;
				if(sb!=null)sb2 = sb;
				sb = new StringBuilder();	//sbを新しくする
				newline = 0;
				}
			switch(temp){
			
			case (char)(-1):
				break;
			
			case '\n' :					//改行コードだった場合
				newline = 1;			//改行フラグをオン
				touchwait = 1;			//画面タッチ待ちのフラグをオンにする
				break;
				
			case '#' :					//スクリプト指定の場合
				ssb.setLength(0);	//スクリプト用ストリングビルダーを初期化
				while(true){	
					try{
						stemp = (char)br.read();
					} catch(IOException e){
						e.printStackTrace();
					}
					if(stemp == '.' || stemp == 0){ //ドットが出てくるまで読み込み
						break;
					}else{
						ssb.append(stemp);
					}
				}
				ss = new String(ssb);	//ssにStringとして入力
				ssb2.setLength(0);		//読み込み用ストリングビルダーを初期化
				while(true){			//無限ループ
					try{
						stemp = (char)br.read();
					} catch(IOException e){
						e.printStackTrace();
					}
					if(stemp == '#' || stemp == 0){ //終了記号が出てくるまで読み込み
						break;
					}else if(stemp == '.'){			//スクリプト指定2つの場合の例外処理
						ssb3.setLength(0);
						while(true){
							try {
								stemp = (char) br.read();	
								} catch (IOException e) {
									e.printStackTrace();
									}
								if(stemp == '#' || stemp == 0){
									break;
								}else{
									ssb3.append(stemp);
								}
							}
						break;
					}else{
						ssb2.append(stemp);
					}
				}
				ss2 = new String(ssb2);			//この時点でssにドットより前のstr，ss2にドットより後のstrが入っている
				ss3 = new String(ssb3);
				script();
				break;
				
			case '<' :					// < の次の文字は必ず表示(#とかでも)
				try {
					temp = (char)br.read();		//一文字読み込み
					} catch (IOException e) {
						e.printStackTrace();
					}
				sb.append(temp);		//ストリングビルダーに追加
				break;
			default:
				sb.append(temp);		//ストリングビルダーに追加
				break;
			}
			
			/*
			if(temp=='\n'){				//改行コードだった場合
				newline = 1;			//改行フラグをオン
				touchwait = 1;			//画面タッチ待ちのフラグをオンにする
				}else{
					sb.append(temp);			//ストリングビルダーに追加
				}
				*/
		}
		
	}
	
	public void script(){
		if("cl".equals(ss)){
			setLeftc();
		}
		if("cr".equals(ss)){
			setRightc();
		}
		if("ce".equals(ss)){
			setCenter();
		}
		if("sp".equals(ss)){
			setParam();
		}
		if("cls".equals(ss)){
			setLeftArea();
		}
		if("cla".equals(ss)){
			changeLeftArea();
		}
		if("crs".equals(ss)){
			setRightArea();
		}
		if("cra".equals(ss)){
			changeRightArea();
		}
	}
	
	public void setLeftc(){				//左側キャラクタグラフィックの指定
		int charId = re.getIdentifier(ss2, "drawable", "my.start");	//ss2に入っているstrよりリソースのID番号を取得
		leftc = null;
		leftc = BitmapFactory.decodeResource(re, charId);	//leftcに指定されたグラフィックをセット
		lcflag = 1;											//lcフラグをオン
	}
	
	public void setRightc(){			//右側キャラクタグラフィックの指定
		int charId = re.getIdentifier(ss2, "drawable", "my.start");	//ss2に入っているstrよりリソースのID番号を取得
		rightc = null;
		rightc = BitmapFactory.decodeResource(re, charId);	//leftcに指定されたグラフィックをセット
		rcflag = 1;											//lcフラグをオン
	}
	
	public void setCenter(){			
		int charId = re.getIdentifier(ss2, "drawable", "my.start");	//ss2に入っているstrよりリソースのID番号を取得
		center = null;
		center = BitmapFactory.decodeResource(re, charId);	//leftcに指定されたグラフィックをセット
		centflag = 1;
	}
	
	public void setLeftArea(){			//左側キャラクタグラフィック(分割用)の指定
		Matrix matrix = new Matrix();
		matrix.preScale(-1, 1);
		int charId = re.getIdentifier(ss2, "drawable", "my.start");	//ss2に入っているstrよりリソースのID番号を取得
		leftc = null;		
		leftc = BitmapFactory.decodeResource(re, charId);	//leftcに指定されたグラフィックをセット
		leftcx = Integer.parseInt(ss3) % 7;
		leftcy = Integer.parseInt(ss3) / 7;
		lcflag = 1;											//lcフラグをオン
//		createl = Bitmap.createBitmap(leftc, 64*leftcx, 64*leftcy, 64, 64);	//元のbitmap,始点左，始点上，横幅，高さ
		createl = Bitmap.createBitmap(leftc, 64*leftcx, 64*leftcy, 64, 64, matrix, false);
//		matrix.setScale(1.0f, 1.0f); // 左右反転 -1.0,-1.0 上下反転
//		matrix.setScale(-1.0f, 1.0f, createl.getWidth()/2, createl.getHeight()/2);
//		createl2 = Bitmap.createBitmap(createl, 0, 0, createl.getWidth(), createl.getHeight(), matrix, true);
	}
	
	public void changeLeftArea(){		//左側グラフィックの変更(リソース変更なし)
		Matrix matrix = new Matrix();
		matrix.preScale(-1, 1);
		leftcx = Integer.parseInt(ss2) % 7;
		leftcy = Integer.parseInt(ss2) / 7;
		lcflag = 1;
		createl = Bitmap.createBitmap(leftc, 64*leftcx, 64*leftcy, 64, 64, matrix, false);
	}
	
	public void setRightArea(){			//右側キャラクタグラフィック(分割用)の指定
		int charId = re.getIdentifier(ss2, "drawable", "my.start");	//ss2に入っているstrよりリソースのID番号を取得
		rightc = null;		
		rightc = BitmapFactory.decodeResource(re, charId);	//rightcに指定されたグラフィックをセット
		rightcx = Integer.parseInt(ss3) % 7;
		rightcy = Integer.parseInt(ss3) / 7;
		rcflag = 1;											//rcフラグをオン
		creater = Bitmap.createBitmap(rightc, 64*rightcx, 64*rightcy, 64, 64);
	}
	
	public void changeRightArea(){		//右側グラフィックの変更(リソース変更なし)
		rightcx = Integer.parseInt(ss2) % 7;
		rightcy = Integer.parseInt(ss2) / 7;
		rcflag = 1;
		creater = Bitmap.createBitmap(rightc, 64*rightcx, 64*rightcy, 64, 64);
	}
	
	//文字列→drawableのリソースID
    public static int str2drawableID(Context context,String str) {
        return re.getIdentifier(
            str,"drawable",context.getPackageName());        
    }
	
	//文字列→drawableのリソースID
//    public static int str2drawableID(Context context,String str) {
//        return context.getResources().getIdentifier(
//            str,"drawable",context.getPackageName());        
//   }
    
    //文字列→rawのリソースID
//    public static int str2rawID(Context context,String str) {
//       return context.getResources().getIdentifier(
//            str,"raw",context.getPackageName());        
//    }
    
    public void onFinger(float x, float y){
    	pointx = x;
    	pointy = y;
    	onscreenf = 1;
     }
    
    public int marea(){
    	if(pointx<0.0||pointx>Util.drawAreaWidth||pointy<0.0||pointy>Util.drawAreaHeight){
    		return 0;
    	}
    	if(Util.select[0].contains((int)pointx, (int)pointy))return 1;
    	if(Util.select[1].contains((int)pointx, (int)pointy))return 2;
    	if(Util.select[2].contains((int)pointx, (int)pointy))return 3;
    	if(Util.select[3].contains((int)pointx, (int)pointy))return 4;
    	return 0;
    }
    
    public int darea(){
    	if(pointx<Util.basePointX||pointx>Util.endPointX||pointy<Util.basePointY||pointy>Util.endPointY){
    		return 0;
    	}
    	if(Util.dayselect[0].contains((int)pointx, (int)pointy))return 1;
    	if(Util.dayselect[1].contains((int)pointx, (int)pointy))return 2;
    	if(Util.dayselect[2].contains((int)pointx, (int)pointy))return 3;
    	if(Util.dayselect[3].contains((int)pointx, (int)pointy))return 4;
    	if(Util.dayselect[4].contains((int)pointx, (int)pointy))return 5;
    	if(Util.dayselect[5].contains((int)pointx, (int)pointy))return 6;
    	return 0;
    }
    
    public int maparea(){
    	if(pointx<Util.basePointX||pointx>Util.endPointX||pointy<Util.basePointY||pointy>Util.endPointY){
    		return 0;
    	}
    	if(Util.mapselect[0].contains((int)pointx, (int)pointy))return 1;
    	if(Util.mapselect[1].contains((int)pointx, (int)pointy))return 2;
    	if(Util.mapselect[2].contains((int)pointx, (int)pointy))return 3;
    	if(Util.mapselect[3].contains((int)pointx, (int)pointy))return 4;
    	if(Util.mapselect[4].contains((int)pointx, (int)pointy))return 5;
       	return 0;
    }
    
    public int mapobject(){
    	if(Util.smap[0].contains(smapx, smapy))return 1;
    	return 0;
    }
    
    public void mapobjecton(){
    	mapobj = 1;
    }
	
	public void ontouch(){				//画面をタッチして指を離したときの処理
		onscreenf = 0;
		onlock = 0;
		onscreenTime = 0;
		touchwait = 0;					//タッチ待ちのフラグをオフにするだけ
		fup = 1;
	}
	
	public void lock(){
		onlock=1;
	}
	
	public String lineset(){
		return new String(sb);			//strにセットスルダケー
	}
	
	public String line2set(){
		return new String(sb2);			//strにセットスルダケー
	}
	
	public String line3set(){
		return new String(sb3);			//strにセットスルダケー
	}
	
	public Paint paintset(int dn){
		menup.setTextSize(textsi*dn);
		p.setTextSize(textsi*dn);
		return p;
	}
	
	public void setParam(){
		hitp = Integer.valueOf(ss2);
		hitpr = new Rect(Util.htx ,Util.hty, Util.htx + (int)(50.0*((double)hitp/100.0)) * Util.dn ,Util.htey);
	}
	
	/*
	public Paint paintset(){
		return p;
	}
	*/
}
