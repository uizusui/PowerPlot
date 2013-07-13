package rising.sun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rising.sun.Util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

//タグハンドラ用
interface TagObject {
	public boolean run(HashMap<String, String> elm);
}

// SurfaceViewを継承，Callbackを実装，Runnable
public class MainSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback, Runnable {

	public Thread thread = null; // スレッド
	private Context context = null; // コンテキスト
	private SurfaceHolder holder = null; // サーフェイスホルダー
	private Canvas canvas = null; // キャンバス

	BitmapLoader bloader;

	public ScenarioLoader sl = null; // シナリオローダー

	// 将来的には移動する(ファイル読み込みように一時的に配置)
	float x, y;
	String str = null;
	String str2 = null;
	String str3 = null;

	//テキスト・図形の表示などに使うPaintクラスをListで管理
	List<Paint> ptList;
	
	Paint p = new Paint();
	Paint p2 = new Paint();
	Paint p3 = new Paint();
	Paint p4 = new Paint();
	int lcounter;
	int mark;
	int area;
	boolean marker;

	int dispmag = 1;
	
	//ミニゲームのフレームカウント
	int minigameTime;

	// ゲーム進行制御
	// private Conductor conducter = null; // コンダクタ
	// TreeMap<String, TagObject> tagHandlersMap = null; // タグハンドラ

	private enum CMODE { // ゲームの状態
		DEFAULT, // デフォルト
		LOGOINIT, LOGO, // ロゴ
		TITLEINIT, TITLE, // タイトル
		MENUINIT, MENU, // メニュー
		SCENARIOINIT, SCENARIO, // シナリオ
		MINIGAMEINIT, MINIGAME, // ミニゲーム
	};

	private CMODE cmode = null;

	private enum GAMESTATE // ゲームの進行状況フラグ
	{
		DEFAULT, // デフォルト
		DAYINIT, // 毎日の始まり
		DAYSELECT, // 毎日の選択肢
		ATKSELECT, // 攻めこむ
		DEFSELECT, // 　やすむ
		DATASELECT, // データを見る
		SAVESELECT, // セーブ
		TEXT, // テキストを出力している
		WAITKEY_LINE, // キー入力待ち 行末
		WAITKEY_PAGE, // キー入力待ち 改ページ
		WAIT, // 一定時間待っている
		STOP, // 止まっている
		TRANS, // トランジション
		HISTORY, // 履歴中
		HIDEMESSAGE, // メッセージ非表示中
		WQ, // 画面揺れの終了を待っている
	};

	private GAMESTATE gameState = null;

	Resources res;

	/* ====================ここまで変数群==================== */

	// コンストラクタ
	public MainSurfaceView(Context context) {
		super(context); // (SurfaceViewのコンストラクタ)
		// tagHandlersMap = new TreeMap<String, TagObject>();// タグハンドラ用マップ生成
		// this.context = context; //コンテキストをセット
		holder = getHolder(); // ホルダーをゲット
		res = this.getContext().getResources(); // リソースをゲット

		initGame(); // ゲームの初期化
		initPaint(); // ペイントクラス郡の初期化

		sl = new ScenarioLoader(context, res); // シナリオローダー作成

		thread = new Thread(this); // 新しいスレッドを作成
		thread.start(); // 開始
	}

	private void initGame() { // 初期化
		cmode = CMODE.LOGOINIT; // 起動ロゴ表示状態にする
		gameState = GAMESTATE.DEFAULT;
		lcounter = 0;
		marker = false;
		mark = 0;
		area = 0;

		bloader = new BitmapLoader(res);
		// getTagHandlers(); // タグハンドラの登録
		// conducter = new Conductor(context, this);// コンダクタ(進行管理)生成
	}

	private void initPaint() {
		// ペイントクラスの初期化と追加
		ptList = new ArrayList<Paint>();
		Paint setp = new Paint();

		// 0.黒いの（デフォルトコンソール的な）
		setp.setColor(Color.BLACK);
		setp.setTextSize(14 * Util.dn);
		ptList.add(setp);

		// 1.白いの
		setp.setColor(Color.WHITE);
		setp.setTextSize(14 * Util.dn);
		ptList.add(setp);

		// 2.青いの
		setp.setColor(Color.BLUE);
		setp.setTextSize(14 * Util.dn);
		ptList.add(setp);
		// 3.赤いの
		setp.setColor(Color.RED);
		setp.setTextSize(14 * Util.dn);
		ptList.add(setp);

		// 4.緑の
		setp.setColor(Color.GREEN);
		setp.setTextSize(14 * Util.dn);
		ptList.add(setp);

		p.setAntiAlias(false);
		p.setTextSize(14 * Util.dn);
		p.setColor(Color.YELLOW);
		p3.setAntiAlias(true);
		p3.setColor(Color.RED); // 赤に設定(仮)
		p3.setTextSize(14 * Util.dn); // 文字の大きさ
		p3.setAntiAlias(false); // アンチエイリアス＞オン
		p4.setColor(Color.GREEN);
		p4.setAlpha(127);
	}

	private void getTagHandlers() {
		// TODO Auto-generated method stub
	}

	public void run()// Runnableにより定期実行される(threadオンで開始)
	{
		boolean flag = true;
		canvas = holder.lockCanvas();

		while (flag && thread != null) // メインの無限ループ
		{
			// fps制御
			// startTime = SystemClock.currentThreadTimeMillis();

			// スキップの処理
			// skip();
			// 入力に対する処理
			// input();
			// コンダクタに指示を仰いだり
			// conduct();
			// オートモード
			// auto();
			// 画面揺れ
			// quake();
			// 音関連
			// sound();
			// 移動関係
			// move();
			// ウェイト関連
			// myWait();

			// 描画
			draw();
			// スリープ
			// mySleep();

			// debugCount++;
		}
	}

	// 実際の描画データ受け渡しと描画
	public void draw() {
		canvas = holder.lockCanvas(); // ダブルバッファリング

		if (canvas != null) {
			switch (cmode) { // 　cmodeでループ制御
			case LOGOINIT: // 　ロゴ表示
				transmenuinit(); // 　ロゴ表示関数
				break;
			case LOGO: // ロゴ
				transmenu();
				break;
			case TITLEINIT: // タイトル初期化
				titleinit();
				break;
			case TITLE: // タイトル
				title();
				break;
			case MENUINIT:
				menuinit();
				break;
			case MENU:
				menu();
				break;
			case SCENARIOINIT:
				scenarioinit();
				break;
			case SCENARIO:
				scenario();
				break;
			case MINIGAMEINIT:
				minigameinit();
				break;
			case MINIGAME:
				minigame();
				break;
			default:
				canvas.drawColor(Color.CYAN);
				canvas.drawRect(Util.baseRect, p3);
				canvas.drawRect(Util.wst, p4);
				break;
			}

			/*
			 * switch(gameState) { case TRANS: transDraw(canvas); break; case
			 * HISTORY: drawLayers(canvas); history.onDraw(canvas); break;
			 * default: drawLayers(canvas); // レイヤの描画 break; }
			 */
			holder.unlockCanvasAndPost(canvas); // 描画

			// holder.unlockCanvasAndPost(c2);

		}

	}

	private void minigame() {
		Matrix matrix = new Matrix();
//		matrix.setScale((float)Util.dn, (float)Util.dn);
		matrix.preTranslate((float)0, (float)minigameTime);
		matrix.postScale((float)Util.dn, (float)Util.dn);
		canvas.drawBitmap(BitmapLoader.getBitmap("dododo"), matrix, p);
		
		Matrix matrix2 = new Matrix();
		matrix2.preTranslate((float)0, (float)(minigameTime-160));
		matrix2.postScale((float)Util.dn, (float)Util.dn);
		canvas.drawBitmap(BitmapLoader.getBitmap("dododo"), matrix2, p);

//		canvas.drawBitmap(BitmapLoader.getBitmap("dododo"), 0,minigameTime, p);
//		canvas.drawBitmap(BitmapLoader.getBitmap("dododo"), 0,minigameTime-160, p);
		minigameTime++;
		if(minigameTime>160){
			minigameTime = 0;
		}
	}

	private void minigameinit() {
		cmode = CMODE.MINIGAME;
		minigameTime = 0;
		BitmapLoader.setBitmap("dododo");
	}

	public void transmenuinit() { // ロゴ表示関数初期化
		BitmapLoader.setBitmap("balloobluene"); // ロゴ登録
		cmode = CMODE.LOGO;
	}

	public void transmenu() {
		canvas.drawColor(Color.BLUE); // 水色で初期化
		if (lcounter < 128) { // ウェイト
			p.setAlpha(0); // 　透明度の処理
		} else if (lcounter < 384) {
			p.setAlpha(lcounter - 128);
		} else if (lcounter < 512) {
			p.setAlpha(255);
		} else if (lcounter < 768) {
			p.setAlpha(255 - (lcounter - 512));
		}
		if (lcounter >= 896 || sl.fup == 1) { // 　ロゴの表示がひと通り終わったら
			sl.fup = 0;
			sl.touchwait = 1; // 　タッチ待ちに設定
			cmode = CMODE.TITLEINIT; // 　タイトル画面に移動
		}
		lcounter++; // 　ロゴがだんだん濃くなる処理(Alpha値を制御,0→255)
		canvas.drawBitmap(BitmapLoader.getBitmap("balloobluene"),
				BitmapLoader.getRect("balloobluene"), Util.dst, p);// 　oplogoをoplogoの大きさ(Rect)から画面の倍率に合わせて(dst)表示している
	}

	public void titleinit() {
		p.setAlpha(255); // 　Paint pの透明度は255(完全表示)に固定
		BitmapLoader.setBitmap("image3");
		cmode = CMODE.TITLE;
	}

	public void title() {
		canvas.drawColor(Color.BLUE); // 白で初期化
		canvas.drawBitmap(BitmapLoader.getBitmap("image3"),
				BitmapLoader.getRect("image3"), Util.dst, p);// タイトル画像を表示
		if (sl.fup == 1) {
			sl.fup = 0;
			cmode = CMODE.MENUINIT;// メニュー画面に移行
		}
	}

	public void menuinit() {// 初期化
		sl.setMenu();
		sl.sethuki();
		sl.setkoma();
		sl.setnazu();
		sl.setMenuc();
		sl.touchwait = 1;
		cmode = CMODE.MENU;
	}

	public void menu() {
		canvas.drawColor(Color.GREEN); // 白で初期化
		canvas.drawBitmap(sl.menu, Util.src, Util.dst, p);
		if (sl.onscreenf == 1) { // タッチされている間表示
			area = sl.marea();
			if (area != 0) { // エリア判定
				canvas.drawBitmap(sl.hukidashi, sl.oplogor, Util.dst, p); // 吹き出しの表示
				canvas.drawText(sl.menus[area - 1], Util.menucx, Util.menucy, p); // キャラのしゃべり
				canvas.drawBitmap(sl.komachara, sl.menur[area - 1], Util.menuc,
						p); // キャラの画像
			}
			canvas.drawCircle(sl.pointx, sl.pointy, sl.pradius * Util.dn, p3);
			canvas.drawText("x=" + this.x, 100, 100, p3); //
			canvas.drawText("y=" + this.y, 100, 150, p3); // デバッグ用
		}
		if (sl.touchwait == 0 && sl.onscreenf == 0) { // 指が離れた場合の処理
			sl.touchwait = 1;
			sl.onscreenTime = 0;
			sl.smenu = 0;
			switch (area) {
			case 0:
				break;
			case 1: // 1つめ，シナリオモード
				cmode = CMODE.SCENARIOINIT;
				gameState = GAMESTATE.DAYINIT;
				break;
			case 2:
				// cmode = CMODE.DATA;
				break;
			case 3:
				break;
			case 4: // 4つめ，ミニゲームモード
				cmode = CMODE.MINIGAMEINIT;
				gameState = GAMESTATE.DAYINIT;
				break;
			default:
				break;
			}
		}
	}

	public void scenarioinit() {
		BitmapLoader.setBitmap("superbottom");
		BitmapLoader.setBitmap("image3");
		BitmapLoader.setBitmap("smap");
		sl.setDays();
		sl.fup = 0;
		cmode = CMODE.SCENARIO;
	}

	public void scenario() {
		// 以下は必ず表示
		canvas.drawColor(Color.WHITE); // 白で初期化
		// canvas.drawBitmap(sl.suuji[0], Util.ni, Util.ni1,p);
		// canvas.drawBitmap(sl.suuji[1], Util.ni, Util.ni2,p);
		// canvas.drawBitmap(sl.suuji[2], Util.ni, Util.ni3,p); // 何日目
		canvas.drawRect(sl.hitpr, sl.hp); // ステータスバー
		canvas.drawRect(sl.metpr, sl.mp); // ステータスバー
		canvas.drawRect(sl.luckr, sl.lp); // ステータスバー
		canvas.drawBitmap(BitmapLoader.getBitmap("superbottom"),
				BitmapLoader.getRect("superbottom"), Util.dst, p); // ブロック↑
		canvas.drawBitmap(BitmapLoader.getBitmap("image3"),
				BitmapLoader.getRect("image3"), Util.clst, p);

		switch (gameState) {
		case DAYINIT:
			canvas.drawRect(Util.dayst, p4);
			canvas.drawText("なにを　しようかな", Util.daysx, Util.daysy, p);
			if (sl.fup == 1) {
				sl.fup = 0;
				gameState = GAMESTATE.DAYSELECT;
			}
			break;
		case DAYSELECT:
			canvas.drawRect(Util.dayst, p4);
			for (int i = 0; i < 6; i++) {
				canvas.drawRect(Util.dayselect[i], p3);
			}

			if (sl.onscreenf == 1) { // タッチされている間表示
				area = sl.darea();
				if (area != 0) {
					canvas.drawText(sl.days[area - 1], Util.daysx, Util.daysy,
							p);
				}
			}

			/*
			 * if(sl.centflag == 1){ canvas.drawBitmap(sl.center, Util.cerc,
			 * Util.cest, p); //センター背景 } if(sl.lcflag == 1){
			 * canvas.drawBitmap(sl.createl, Util.clrc, Util.clst, p); //左キャラ }
			 * if(sl.rcflag == 1){ canvas.drawBitmap(sl.creater, Util.clrc,
			 * Util.crst, p); //右キャラ }
			 */
			if (sl.fup != 0) {
				sl.fup = 1;
				switch (area) {
				case 0:
					break;
				case 1:
					gameState = GAMESTATE.ATKSELECT;
					break;
				case 2:

					break;
				case 3:
					// gameState = GAMESTATE.DEFSELECT;
					break;
				case 4:
					break;
				case 5:
					// gameState = GAMESTATE.DATASELECT;
					break;
				case 6:
					// gameState = GAMESTATE.SAVESELECT;
					break;
				default:
					break;
				}
			}
			break;
		case ATKSELECT:
			canvas.drawBitmap(BitmapLoader.getBitmap("smap"),
					BitmapLoader.getRect("smap"), Util.dst, p);
			if (sl.onscreenf == 1) { // タッチされているときの処理
				area = sl.maparea(); // マップ選択時のタッチエリア判定
				// for(int j=0;j<4;j++){ //上下左右の選択部分に半透明表示
				// canvas.drawRect(Util.mapselect[j], p4);
				// }
				switch (area) { // タッチエリア分岐
				case 1:
					if (sl.mapy > 0) {
						sl.mapy -= 4;
						sl.createMap();
					}
					break;
				case 2:
					if (sl.mapy < 512) {
						sl.mapy += 4;
						sl.createMap();
					}
					break;
				case 3:
					if (sl.mapx > 0) {
						sl.mapx -= 4;
						sl.createMap();
					}
					break;
				case 4:
					if (sl.mapx < 512) {
						sl.mapx += 4;
						sl.createMap();
					}
					break;
				case 5:
					if (sl.onscreenTime > 200) {
						sl.smapx = (int) (sl.mapx + (sl.pointx)) / Util.dn; // 元倍率のマップ上選択座標x
						sl.smapy = (int) (sl.mapy + (sl.pointy)) / Util.dn; // 元倍率のマップ上選択座標y
						if (sl.mapobject() != 0) { // もし選択座標がマップオブジェクト上にあったら
							// sl.mapobjecton(); //オブジェクトを選択状態にする
							sl.sbx = (sl.smapx - sl.mapx) / Util.dn;
							sl.sby = (sl.smapy - sl.mapy) / Util.dn;
							sl.sex = sl.sbx + 50;
							sl.sey = sl.sby + 50;
							canvas.drawRect(sl.sbx, sl.sby, sl.sex, sl.sey, p4);
							// canvas.drawRect(Util.smap[0], p4);
						}
						if (sl.mapobj == 1 && sl.fup == 1) { // 選択状態で指が離れたら
							sl.fup = 0;
							// gameState = GAMESTATE.TEXT;
							// //選択場所の変数を引き継いでシナリオへ移動
						}
					}
					break;
				default:
					break;
				}
				// 移動制限
				if (sl.mapx < 0)
					sl.mapx = 0;
				if (sl.mapx > 512)
					sl.mapx = 512;
				if (sl.mapy < 0)
					sl.mapy = 0;
				if (sl.mapy > 512)
					sl.mapy = 512;
				sl.createMap();
			}
			break;
		case WAIT:
			break;
		default:
			break;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

	public boolean onTouchEvent(MotionEvent ev) { // 画面がタッチされたときに行われる処理
		if (ev.getAction() == MotionEvent.ACTION_DOWN) { // タッチダウン(触れたときの処理)
			x = ev.getX(); //
			y = ev.getY(); // 座標を取得している
			sl.lock();
			sl.onscreenTime = 300;
			sl.onFinger(x, y); // タッチされた座標を引数にして関数呼び出し
			MainSurfaceView.this.invalidate(); // おまじない
		}
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			x = ev.getX(); //
			y = ev.getY(); // 座標を取得している
			sl.onFinger(x, y);
		}
		if (ev.getAction() == MotionEvent.ACTION_UP) {
			sl.ontouch(); // シナリオローダーのタッチ処理関数呼び出し
		}
		return true;
	}

	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
	}

	public void surfaceCreated(SurfaceHolder holder) {// 生成時に呼び出されるメソッド
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	}

}
