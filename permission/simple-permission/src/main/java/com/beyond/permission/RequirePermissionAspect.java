package com.beyond.permission;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
public class RequirePermissionAspect {

    @Pointcut("execution(@com.beyond.permission.RequirePermission * *(..))")
    public void pointcut() {

    }

    @Around("pointcut()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequirePermission annotation = method.getAnnotation(RequirePermission.class);
        assert annotation != null;

        Context context = getContextParam(joinPoint, annotation);

        String[] requirePermissions = annotation.value();
        checkAndRequestPermission(context, requirePermissions, joinPoint);
    }

    @NonNull
    private Context getContextParam(ProceedingJoinPoint joinPoint, RequirePermission requirePermission) {
        Object[] args = joinPoint.getArgs();
        Context context = null;
        for (Object arg : args) {
            if (arg instanceof Context){
                context = (Context) arg;
                break;
            }
        }
        if (context == null){
            context = ApplicationHolder.get();
        }
        if (context == null){
            throw new RuntimeException("方法参数中要配置 Context context 或 ApplicationHolder.set(application)");
        }
        return context;
    }

    private void checkAndRequestPermission(@NonNull final Context context, @NonNull final String[] requirePermissions, final ProceedingJoinPoint joinPoint) {
        if (checkPermission(context, requirePermissions)) {
            try {
                joinPoint.proceed();
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
            return;
        }
        PermissionsUtil.requestPermission(context, new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {
                checkAndRequestPermission(context, requirePermissions, joinPoint);
            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {

            }
        }, requirePermissions, false, null);
    }

    private boolean checkPermission(Context context, @NonNull String[] requirePermissions) {
        boolean hasPermission = true;
        for (String requirePermission : requirePermissions) {
            hasPermission = hasPermission && ActivityCompat.checkSelfPermission(context, requirePermission) == PackageManager.PERMISSION_GRANTED;
        }
        return hasPermission;
    }

}
