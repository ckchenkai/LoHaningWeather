package com.ck.weather.util;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by Administrator on 2015/8/5.
 */
public class Locate {
    private LocationClient locationClient;

    private BDLocation mbdLocation;
    
    private Handler mHandler;
    private boolean flag = false;
    public Locate(Context context, Handler handler){
    	this.mHandler = handler;
        locationClient = new LocationClient(context);
        initLocation();
        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                mbdLocation = bdLocation;
                parseBack();
                flag = true;
            }
        });
        
        mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(!flag){
					Message msg = mHandler.obtainMessage();
			        msg.what = 0x01;
			        mHandler.sendMessage(msg);
				}
			}
		}, 3000);
    }

    public void locate(){
        if (!locationClient.isStarted()){
            //瀹氫綅SDK start涔嬪悗浼氶粯璁ゅ彂璧蜂竴娆″畾浣嶈姹傦紝寮�鍙戣�呮棤椤诲垽鏂璱sstart骞朵富鍔ㄨ皟鐢╮equest
            locationClient.start();
        }else {
            locationClient.requestLocation();
        }
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        //Battery_Saving    浣庡姛鑰楁ā寮�   涓嶄細浣跨敤GPS锛屽彧浼氫娇鐢ㄧ綉缁滃畾浣嶏紙Wi-Fi鍜屽熀绔欏畾浣嶏級
        //Device_Sensors    浠呰澶�(Gps)妯″紡  涓嶉渶瑕佽繛鎺ョ綉缁滐紝鍙娇鐢℅PS杩涜瀹氫綅锛岃繖绉嶆ā寮忎笅涓嶆敮鎸佸鍐呯幆澧冪殑瀹氫綅
        //Hight_Accuracy    楂樼簿搴︽ā寮�   浼氬悓鏃朵娇鐢ㄧ綉缁滃畾浣嶅拰GPS瀹氫綅锛屼紭鍏堣繑鍥炴渶楂樼簿搴︾殑瀹氫綅缁撴灉
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//鍙�夛紝榛樿楂樼簿搴︼紝璁剧疆瀹氫綅妯″紡
        //"gcj02"锛氬浗瀹舵祴缁樺眬鏍囧噯
        //"bd09ll"锛氱櫨搴︾粡绾害鏍囧噯
        //"bd09"锛氱櫨搴﹀ⅷ鍗℃墭鏍囧噯
        option.setCoorType("bd09ll");//鍙�夛紝榛樿gcj02锛岃缃繑鍥炵殑瀹氫綅缁撴灉鍧愭爣绯伙紝
        option.setScanSpan(0);//鍙�夛紝榛樿0锛屽嵆浠呭畾浣嶄竴娆★紝璁剧疆鍙戣捣瀹氫綅璇锋眰鐨勯棿闅旈渶瑕佸ぇ浜庣瓑浜�1000ms鎵嶆槸鏈夋晥鐨�
        option.setIsNeedAddress(true);//鍙�夛紝璁剧疆鏄惁闇�瑕佸湴鍧�淇℃伅锛岄粯璁や笉闇�瑕�
        option.setOpenGps(true);//鍙�夛紝榛樿false,璁剧疆鏄惁浣跨敤gps
        option.setLocationNotify(false);//鍙�夛紝榛樿false锛岃缃槸鍚﹀綋gps鏈夋晥鏃舵寜鐓�1S1娆￠鐜囪緭鍑篏PS缁撴灉
        option.setIgnoreKillProcess(true);//鍙�夛紝榛樿true锛屽畾浣峉DK鍐呴儴鏄竴涓猄ERVICE锛屽苟鏀惧埌浜嗙嫭绔嬭繘绋嬶紝璁剧疆鏄惁鍦╯top鐨勬椂鍊欐潃姝昏繖涓繘绋嬶紝榛樿涓嶆潃姝�
        option.setEnableSimulateGps(false);//鍙�夛紝榛樿false锛岃缃槸鍚﹂渶瑕佽繃婊ps浠跨湡缁撴灉锛岄粯璁ら渶瑕�
        option.setIsNeedLocationDescribe(false);//鍙�夛紝榛樿false锛岃缃槸鍚﹂渶瑕佷綅缃涔夊寲缁撴灉锛屽彲浠ュ湪BDLocation.getLocationDescribe閲屽緱鍒帮紝缁撴灉绫讳技浜庘�滃湪鍖椾含澶╁畨闂ㄩ檮杩戔��
        option.setIsNeedLocationPoiList(false);//鍙�夛紝榛樿false锛岃缃槸鍚﹂渶瑕丳OI缁撴灉锛屽彲浠ュ湪BDLocation.getPoiList閲屽緱鍒�
        locationClient.setLocOption(option);
    }

    public void stop(){
        locationClient.stop();
    }

    private String parseBack(){
        StringBuffer sb = new StringBuffer(256);
        /*sb.append("time : ");
        sb.append(mbdLocation.getTime());
        sb.append("\nerror code : ");
        sb.append(mbdLocation.getLocType());
        sb.append("\nlatitude : ");
        sb.append(mbdLocation.getLatitude());
        sb.append("\nlontitude : ");
        sb.append(mbdLocation.getLongitude());
        sb.append("\nradius : ");
        sb.append(mbdLocation.getRadius());*/
        if (mbdLocation.getLocType() == BDLocation.TypeGpsLocation){// GPS瀹氫綅缁撴灉
            /*sb.append("\nspeed : ");
            sb.append(mbdLocation.getSpeed());// 鍗曚綅锛氬叕閲屾瘡灏忔椂
            sb.append("\nsatellite : ");
            sb.append(mbdLocation.getSatelliteNumber());
            sb.append("\nheight : ");
            sb.append(mbdLocation.getAltitude());// 鍗曚綅锛氱背
            sb.append("\ndirection : ");
            sb.append(mbdLocation.getDirection());
            sb.append("\naddr : ");*/
            sb.append(mbdLocation.getAddrStr());
            /*sb.append("\ndescribe : ");
            sb.append("gps瀹氫綅鎴愬姛");*/

        } else if (mbdLocation.getLocType() == BDLocation.TypeNetWorkLocation){// 缃戠粶瀹氫綅缁撴灉
            //sb.append("\naddr : ");
            sb.append(mbdLocation.getAddrStr());
            //杩愯惀鍟嗕俊鎭�
            /*sb.append("\noperationers : ");
            sb.append(mbdLocation.getOperators());
            sb.append("\ndescribe : ");
            sb.append("缃戠粶瀹氫綅鎴愬姛");*/
        } else if (mbdLocation.getLocType() == BDLocation.TypeOffLineLocation) {// 绂荤嚎瀹氫綅缁撴灉
            /*sb.append("\ndescribe : ");
            sb.append("绂荤嚎瀹氫綅鎴愬姛锛岀绾垮畾浣嶇粨鏋滀篃鏄湁鏁堢殑");*/
        } else if (mbdLocation.getLocType() == BDLocation.TypeServerError) {
            //sb.append("\ndescribe : ");
            sb.append("鏈嶅姟绔綉缁滃畾浣嶅け璐ワ紝鍙互鍙嶉IMEI鍙峰拰澶т綋瀹氫綅鏃堕棿鍒發oc-bugs@baidu.com锛屼細鏈変汉杩芥煡鍘熷洜");
        } else if (mbdLocation.getLocType() == BDLocation.TypeNetWorkException) {
            //sb.append("\ndescribe : ");
            sb.append("缃戠粶涓嶅悓瀵艰嚧瀹氫綅澶辫触锛岃妫�鏌ョ綉缁滄槸鍚﹂�氱晠");
        } else if (mbdLocation.getLocType() == BDLocation.TypeCriteriaException) {
            //sb.append("\ndescribe : ");
            sb.append("鏃犳硶鑾峰彇鏈夋晥瀹氫綅渚濇嵁瀵艰嚧瀹氫綅澶辫触锛屼竴鑸槸鐢变簬鎵嬫満鐨勫師鍥狅紝澶勪簬椋炶妯″紡涓嬩竴鑸細閫犳垚杩欑缁撴灉锛屽彲浠ヨ瘯鐫�閲嶅惎鎵嬫満");
        }
        /*sb.append("\nlocationdescribe : ");// 浣嶇疆璇箟鍖栦俊鎭�
        sb.append(mbdLocation.getLocationDescribe());
        List<Poi> list = mbdLocation.getPoiList();// POI淇℃伅
        if (list != null) {
            sb.append("\npoilist size = : ");
            sb.append(list.size());
            for (Poi p : list) {
                sb.append("\npoi= : ");
                sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
            }
        }*/
        Message msg = mHandler.obtainMessage();
        msg.what = 0x01;
        Bundle bundle = new Bundle();
        bundle.putString("address", sb.toString());
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        return sb.toString();
    }
    //public abstract void onReceive(String location, boolean isSuccess);

}
