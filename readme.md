# android 简单的动态权限申请模块

使用方法: 在需要权限的方法上加上注解@RequirePermission(注意不是android的@RequiresPermission)  
方法参数中需有 Context 参数, 如果没有, 需要用 ApplicationHolder 设置一个默认的Context, 比如在 app 创建时设置.

具体见 permission 项目

配置:
1. 导入 module : simple-permission
2. 在 app module libs 中加入依赖包
3. 在 app module 的 build.gradle 中加入配置(permission 中 copy)

实现原理: aspectJ

权限申请依赖: 'com.github.dfqin:grantor:2.5'
