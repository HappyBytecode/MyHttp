package com.sesxh.smoothhttp.interceptor.log;

import android.util.Log;

import com.sesxh.smoothhttp.HttpGlobalConfig;
import com.sesxh.smoothhttp.SmoothHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author LYH
 * @date 2021/1/11
 * @time 13:38
 * @desc 日志存储对象
 */
  
class LogEntity {

    private final String tag = SmoothHttp.TAG+"———Request";
    private List<String> logs = new ArrayList<>();
    private final String LINE_SEPARATOR = Objects.requireNonNull(System.getProperty("line.separator"));
    private StringBuilder sb;

    LogEntity() {
        addLog(" ");
        addLog("╔════════════════════════  HTTP  START  ══════════════════════════");
        addLog("");
    }

    /**
     * 临时保存日志
     *
     * @param log 日志
     */
    void addLog(String log) {
        if (!getConfig().isDebug()) return;
        if (log == null) return;
        if (log.equals(" ") || log.startsWith("{")
                || log.startsWith("╔") || log.startsWith("╚")) {
            logs.add(log);
        } else {
            logs.add("║" + log);
        }
    }

    /**
     * 输出日志到控制台
     */
    void printLog() {
        if (!getConfig().isDebug()) return;
        sb=new StringBuilder();
        addLog("");
        addLog("╚════════════════════════  HTTP  END  ═══════════════════════════");
        addLog(" ");
        for (String log : logs) {
            sb.append(log).append("\n");
            logJson(log);
        }
        logs.clear();
        logs = null;
    }

    /**
     * 格式化 json 后输出日志（网络日志拦截器信息打印）
     *
     * @param msg 格式化前 json 数据
     */
    private void logJson(String msg) {
        String message;
        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(2);//最重要的方法，就一行，返回格式化的json字符串，其中的数字2是缩进字符数
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(2);
            } else {
                message = msg;
                if (getConfig().isDebug()) {
                    Log.d(tag, message);
                }
                return;
            }
        } catch (JSONException e) {
            message = msg;
            if (getConfig().isDebug()) {
                Log.d(tag, message);
            }
            return;
        }
        // 输出 json 格式数据
        printLine(true);
        message = LINE_SEPARATOR + message;
        String[] lines = message.split(LINE_SEPARATOR);
        for (String line : lines) {
            if (!line.isEmpty() && getConfig().isDebug()) {
                Log.d(tag, "║ " + line);
            }
        }
        printLine(false);
    }

    /**
     * 打印日志分割线
     *
     * @param isTop 是否为顶部分割线
     */
    private void printLine(boolean isTop) {
        if (isTop) {
            if (getConfig().isDebug()) {
                Log.d(tag, "║");
                Log.d(tag, "║——————————————————JSON START——————————————————");
            }
        } else {
            if (getConfig().isDebug()) {
                Log.d(tag, "║——————————————————JSON END———————————————————");
                Log.d(tag, "║");
            }
        }
    }
    
    private HttpGlobalConfig getConfig(){
        return SmoothHttp.getInstance().globalConfig();
    }

    public List<String> getLogs() {
        return logs;
    }

    public StringBuilder getSb() {
        return sb;
    }
}
