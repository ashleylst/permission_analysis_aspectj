package xyz.monkeytong.hongbao.aspectj;

/**
 * Created by Administrator on 2017/5/15.
 */

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;


import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

@Aspect
public class CheckPermission {

    /*所有需要检查的权限的定义*/
    private  String[] permission = {"android.permission.DISABLE_KEYGUARD"
            , "android.permission.INTERNET"
            , "android.permission.ACCESS_NETWORK_STATE"
            ,"android.permission.ACCESS_WIFI_STATE"};

    /*
    对所有xyz.monkeytong.hongbao.activities包和service包下的方法做检查
    如果检查utils/fragments包下的方法会有程序崩溃的情况，因此只能使用手动加注释的方法检查，需使用FindPermission.java下的Aspect来检查
    */
    @Pointcut("execution(* xyz.monkeytong.hongbao.activities..*.*(..)) || execution(* xyz.monkeytong.hongbao.services..*.*(..))")
    public void Check() {}

    /*在调用方法前检查*/
    @Before("Check()")
    public void log(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();//获取当前处理中的方法
        String className = methodSignature.getDeclaringType().getSimpleName();//获取类名
        String methodName = methodSignature.getName();//获取方法名
        //对需要检查的权限进行依次遍历并打印log
        for(String i : permission)
        {
            if (ContextCompat.checkSelfPermission((Context) joinPoint.getTarget(), i)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.e(className, className + ":" + methodName + "没有" + i + "的权限执行");
            }
            else{
                Log.e(className, className + ":" + methodName + "有" + i + "的权限执行");
            }
        }
    }
}
