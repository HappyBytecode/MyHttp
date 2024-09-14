package com.sesxh.myapplication;

import com.sesxh.smoothhttp.SmoothHttp;
import com.sesxh.smoothhttp.annotation.DebugData;
import com.sesxh.smoothhttp.callbacks.DownloadCallBack;
import com.sesxh.smoothhttp.callbacks.UploadCallBack;

import java.io.File;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;


/**
 * @author LYH
 * @date 2020/12/28
 * @time 17:13
 * @desc
 **/
public class Api {


    @SuppressWarnings("unchecked")
    @DebugData(json = "{}")
    public static Observable<PhoneDTO> getNum(Map<String, Object> m, String url, BehaviorSubject<Integer> subject) {
        return SmoothHttp.
                get(url).
                addHeader("8","8").
                addParams(m)
                .life(subject)
                .convert(CastFunc1.class)
                .request(PhoneDTO.class);
    }
// .json("{\"resultcode\":\"200\",\"reason\":\"Return Successd!\",\"result\":{\"province\":\"山东\",\"city\":\"青岛11111\",\"areacode\":\"0532\",\"zip\":\"266000\",\"company\":\"移动\",\"card\":\"\"},\"error_code\":0}")

    public static Observable<AddressDTO> getIP(Map<String, Object> m) {
        m.put("ip","112.112.11.11");
        m.put("key","8179ba6ba2e8bc9da8fffdbd73826456");
        return SmoothHttp.post("ip/ipNew").addParams(m).isQuery(true).convert(CastFunc1.class).request(AddressDTO.class);
    }

    @SuppressWarnings("unchecked")
    public static void downloadFile(Map<String, Object> m, DownloadCallBack callBack) {
        SmoothHttp.download("http://download.llmmwl.com/dXB-4.1.2-release.apk").addParams(m).request(callBack);
    }

    @SuppressWarnings("unchecked")
    public static void uploadFile( Object tag, UploadCallBack callBack) {
        File file=new File(MainActivity.TEST_FILE_PATH);
        SmoothHttp.upload("http://192.168.129.194:84/master/test/con/uploadFile").urlParams("userid","222").file("file",file).request(callBack);
    }






}
