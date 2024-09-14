package com.sesxh.smoothhttp.transformer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Dns;

/**
 * @author LYH
 * @date 2021/5/26
 * @time 17:40
 * @desc
 **/
public class TimeoutDns implements Dns {

    private long timeout;
    private TimeUnit unit;
    private static final long DEFAULT_TIMEOUT=10;

    public TimeoutDns(long timeout,TimeUnit unit) {
        this.timeout = timeout<=0?DEFAULT_TIMEOUT:timeout;
        this.unit=unit;
    }

    @Override
    public List<InetAddress> lookup(final String hostname) throws UnknownHostException {
        if (hostname == null) {
            throw new UnknownHostException("hostname == null");
        } else {
            try {
                FutureTask<List<InetAddress>> task = new FutureTask<>(
                        new Callable<List<InetAddress>>() {
                            @Override
                            public List<InetAddress> call() throws Exception {
                                return Arrays.asList(InetAddress.getAllByName(hostname));
                            }
                        });
                Executors.newSingleThreadExecutor().execute(task);
                return task.get(timeout, unit);
            } catch (Exception var4) {
                UnknownHostException unknownHostException =
                        new UnknownHostException("Broken system behaviour for dns lookup of " + hostname);
                unknownHostException.initCause(var4);
                throw unknownHostException;
            }
        }
    }

}
