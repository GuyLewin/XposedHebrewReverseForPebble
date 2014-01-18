package com.guylewin.hebrewreverseforpebble;

import android.view.accessibility.AccessibilityEvent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import com.guylewin.hebrewreverseforpebble.HebrewFixFilter;;

public class HebrewReverseForPebble implements IXposedHookLoadPackage {
	public static final String[] RECEIVERS_ARRAY = {"AppProtocolReceiver", "GmailReceiver", "MusicNowPlayingReceiver", "PebbleNotificationReceiver", "PhoneReceiver", "SmsReceiver"};
	public static final String RECEIVERS_CLASS_PATH = "com.getpebble.android.receivers";
	
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
    	if (!lpparam.packageName.equals("com.getpebble.android"))
            return;
    	else
    		XposedBridge.log("Loaded into Pebble App!");
    	
    	XC_MethodHook reverseHebrewInIntentMethod = new XC_MethodHook() {
    		@Override
    		protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				Intent intent = (Intent)param.args[1];
				
				HebrewFixFilter hebrewFixer = new HebrewFixFilter();
				
				Bundle extras = intent.getExtras();
				Object[] intentExtras = extras.keySet().toArray();
				for (int i = 0; i < intentExtras.length; i++) {
					String keyName = (String)intentExtras[i];
					try {
						Object oldValue = extras.get(keyName);
						String newValue = hebrewFixer.applyFixForHebrew((String)oldValue);
						intent.putExtra(keyName, newValue);
					} catch (Exception e) {	}
				}
				
				param.args[1] = intent;
    		}
    	};
    	
    	XC_MethodReplacement reverseHebrewInAccessibilityMethod = new XC_MethodReplacement() {
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
            	String ret = (String)XposedBridge.invokeOriginalMethod(param.method, param.thisObject, param.args);
            	
            	HebrewFixFilter hebrewFixer = new HebrewFixFilter();
            	
            	if (ret != null)
            		ret = hebrewFixer.applyFixForHebrew(ret);
            	
            	return ret;
            }
        };
    	
    	
    	for (int i = 0; i < RECEIVERS_ARRAY.length; i++) {
    		findAndHookMethod(RECEIVERS_CLASS_PATH + "." + RECEIVERS_ARRAY[i], lpparam.classLoader, "handleReceive", Context.class, Intent.class, reverseHebrewInIntentMethod);
    	}
    	findAndHookMethod(RECEIVERS_CLASS_PATH + ".PebbleAccessibilityService", lpparam.classLoader, "getEventText", AccessibilityEvent.class, reverseHebrewInAccessibilityMethod);
    	
    	findAndHookMethod(RECEIVERS_CLASS_PATH + ".GmailCheckerService", lpparam.classLoader, "slurpMessage", AccessibilityEvent.class, reverseHebrewInAccessibilityMethod);
    }
}