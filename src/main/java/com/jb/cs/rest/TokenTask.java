package com.jb.cs.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TokenTask implements Runnable {

    private static final long hour = 3600000;
    private static final long minute = 60000;
    private static boolean isWorking = false;
    private Map<String, ClientSession> tokensMap;

    @Autowired
    public TokenTask(@Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.tokensMap = tokensMap;
    }

    @Override
    public void run() {
        isWorking = true;
        while (isWorking) {
            for (Map.Entry<String, ClientSession> entries : tokensMap.entrySet()){

                long tokenMillis = tokensMap.get(entries.getKey()).getLastAccessedMillis() + hour ;

                if (tokenMillis< System.currentTimeMillis())
                    tokensMap.remove(entries.getKey());
                }
                try {
                    Thread.sleep(minute);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    public static void stopTokenTask() {
        isWorking = false;
    }

}
