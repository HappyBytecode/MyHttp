package com.sesxh.smoothhttp.interceptor.log;


/**
 * @author LYH
 * @date 2021/1/11
 * @time 13:38
 * @desc 日志打印器
 */


class LogPrinter {

    /**
     * 打印方法加锁，解决并发请求时日志输出错乱问题
     *
     * @param entity 被打印的日志对象
     */
    static synchronized void printLog(LogEntity entity) {
        entity.printLog();
    }

}
