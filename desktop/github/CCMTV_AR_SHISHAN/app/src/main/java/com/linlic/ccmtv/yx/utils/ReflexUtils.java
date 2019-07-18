package com.linlic.ccmtv.yx.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.widget.ImageView;

import com.linlic.ccmtv.yx.activity.base.BaseActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflexUtils {
	
	private Class cls;
	private Context context;
	
	public ReflexUtils(Class cls,Context context){
		this.cls = cls ;
		this.context = context;
		if_start();
	}
	
	/**
	 * 
	 * name:判断是否继续
	 * author:Tom
	 * 2016-3-3上午9:51:20
	 */
	public void if_start(){
		System.out.println("======================================进入判断======================================");
		if(if_activityAndFragment()){//如果不属于activity 或者 fragment 则不继续
			//获得类中的所有属性
			Field[] fields =  cls.getDeclaredFields();
			System.out.println("======================================进入判断2======================================");
			//循环类中的属性
			for (Field field : fields) {
				if_control(field);
			}
		}
		
	}
	
	/**
	 * 
	 * name:判断是否属于android控件
	 * author:Tom
	 * 2016-3-3上午10:29:08
	 * @return
	 */
	public Boolean if_control(Field field){
		 
		System.out.println("field.getType().getName():"+field.getType().getName());
		
		/*switch (field.getType().getName()) {
		case ImageView.this:
			
			break;

		default:
			break;
		}*/
		System.out.println(field.getName()+"-"+field.getType()+":"+field.getType().isAssignableFrom(ImageView.class));
		if(field.getType().isAssignableFrom(ImageView.class)){
			
			//抑制Java对其的检查  
	        field.setAccessible(true) ;  
	        Activity activity = (Activity) context;
	        try {  
	            //将 object 中 field 所代表的值 设置为 value  
//	             field.set(cls, activity.findViewById(R.));
	        } catch (IllegalArgumentException e) {  
	            e.printStackTrace();  
	        }
			
		}
		
		return false;
		
	}
	
	/**
	 * 
	 * name:判断是否是属于activity 或者是 Fragment  
	 * author:Tom
	 * 2016-3-3上午9:52:52
	 * @return 如果以上两种都不属于则返回false
	 */
	public Boolean if_activityAndFragment(){
		
/*		if( cls.isAssignableFrom(Fragment.class))  //判断c是否是List类的子类或父类
			return true;
		else if (cls.isAssignableFrom(Activity.class))  
			return true;
		else if (cls.isAssignableFrom(BaseActivity.class))
			return true;
		else 
			return false;*/
		System.out.println("cls.isAssignableFrom(Fragment.class):"+cls.isAssignableFrom(Fragment.class));
		System.out.println("cls.isAssignableFrom(Activity.class):"+cls.isAssignableFrom(Activity.class));
		System.out.println("cls.isAssignableFrom(BaseActivity.class):"+cls.isAssignableFrom(BaseActivity.class));
		
		return true;
		
	}
	  /** 
     * 循环向上转型, 获取对象的 DeclaredMethod 
     * @param object : 子类对象 
     * @param methodName : 父类中的方法名 
     * @param parameterTypes : 父类中的方法参数类型 
     * author:Tom
	 * 2016-3-3上午9:52:52
     * @return 父类中的方法对象 
     */  
      
    public static Method getDeclaredMethod(Object object, String methodName, Class<?> ... parameterTypes){  
        Method method = null ;  
          
        for(Class<?> clazz = object.getClass() ; clazz != Object.class ; clazz = clazz.getSuperclass()) {  
            try {  
                method = clazz.getDeclaredMethod(methodName, parameterTypes) ;  
                return method ;  
            } catch (Exception e) {  
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。  
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了  
              
            }  
        }  
          
        return null;  
    }  
      
    /** 
     * 直接调用对象方法, 而忽略修饰符(private, protected, default) 
     * @param object : 子类对象 
     * @param methodName : 父类中的方法名 
     * @param parameterTypes : 父类中的方法参数类型 
     * @param parameters : 父类中的方法参数 
     * author:Tom
	 * 2016-3-3上午9:52:52
     * @return 父类中方法的执行结果 
     */  
      
    public static Object invokeMethod(Object object, String methodName, Class<?> [] parameterTypes,  
            Object [] parameters) {  
        //根据 对象、方法名和对应的方法参数 通过反射 调用上面的方法获取 Method 对象  
        Method method = getDeclaredMethod(object, methodName, parameterTypes) ;  
          
        //抑制Java对方法进行检查,主要是针对私有方法而言  
        method.setAccessible(true) ;  
          
            try {  
                if(null != method) {  
                      
                    //调用object 的 method 所代表的方法，其方法的参数是 parameters  
                    return method.invoke(object, parameters) ;  
                }  
            } catch (IllegalArgumentException e) {  
                e.printStackTrace();  
            } catch (IllegalAccessException e) {  
                e.printStackTrace();  
            } catch (InvocationTargetException e) {  
                e.printStackTrace();  
            }  
          
        return null;  
    }  
  
    /** 
     * 循环向上转型, 获取对象的 DeclaredField 
     * @param object : 子类对象 
     * @param fieldName : 父类中的属性名 
     * author:Tom
	 * 2016-3-3上午9:52:52
     * @return 父类中的属性对象 
     */  
      
    public static Field getDeclaredField(Object object, String fieldName){  
        Field field = null ;  
          
        Class<?> clazz = object.getClass() ;  
          
        for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {  
            try {  
                field = clazz.getDeclaredField(fieldName) ;  
                return field ;  
            } catch (Exception e) {  
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。  
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了  
                  
            }   
        }  
      
        return null;  
    }     
      
    /** 
     * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter 
     * @param object : 子类对象 
     * author:Tom
	 * 2016-3-3上午9:52:52
     * @param fieldName : 父类中的属性名 
     * @param value : 将要设置的值 
     */  
      
    public static void setFieldValue(Object object, String fieldName, Object value){  
      
        //根据 对象和属性名通过反射 调用上面的方法获取 Field对象  
        Field field = getDeclaredField(object, fieldName) ;  
          
        //抑制Java对其的检查  
        field.setAccessible(true) ;  
          
        try {  
            //将 object 中 field 所代表的值 设置为 value  
             field.set(object, value) ;  
        } catch (IllegalArgumentException e) {  
            e.printStackTrace();  
        } catch (IllegalAccessException e) {  
            e.printStackTrace();  
        }  
          
    }  
      
    /** 
     * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter 
     * @param object : 子类对象 
     * @param fieldName : 父类中的属性名 
     * author:Tom
	 * 2016-3-3上午9:52:52
     * @return : 父类中的属性值 
     */  
      
    public static Object getFieldValue(Object object, String fieldName){  
          
        //根据 对象和属性名通过反射 调用上面的方法获取 Field对象  
        Field field = getDeclaredField(object, fieldName) ;  
          
        //抑制Java对其的检查  
        field.setAccessible(true) ;  
          
        try {  
            //获取 object 中 field 所代表的属性值  
            return field.get(object) ;  
              
        } catch(Exception e) {  
            e.printStackTrace() ;  
        }  
          
        return null;  
    }  
}  