package com.linlic.ccmtv.yx.activity.step.utils;

import android.content.Context;

import com.linlic.ccmtv.yx.activity.entity.StepData;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;
import com.litesuits.orm.log.OrmLog;

import java.util.List;

import static com.litesuits.orm.LiteOrm.TAG;

/**
 * 数据库操作类
 * Created by lenovo on 2017/1/5.
 */
public class DbUtils {
    public static LiteOrm liteOrm;

    public static void createDb(Context _activity, String DB_NAME) {
        DB_NAME = DB_NAME + ".db";
        if (liteOrm == null) {
            liteOrm = LiteOrm.newCascadeInstance(_activity, DB_NAME);
            liteOrm.setDebugged(true);
        }
    }

    public static LiteOrm getLiteOrm() {
        return liteOrm;
    }

    /**
     * 插入一条记录
     *
     * @param t
     */
    public static <T> void insert(T t) {
        liteOrm.save(t);
    }

    /**
     * 插入所有记录
     *
     * @param list
     */
    public static <T> void insertAll(List<T> list) {
        liteOrm.save(list);
    }

    /**
     * 查询所有
     *
     * @param cla
     * @return
     */
    public static <T> List<T> getQueryAll(Class<T> cla) {
        List list = liteOrm.query(StepData.class);
//        OrmLog.i(TAG, list.size());
        return liteOrm.query(cla);
    }
    /**
     * 查询所有
     *
     * @param cla
     * @return
     */
    public static <T> List<StepData> getQueryAll2(Class<T> cla) {

        //自己拼SQL语句
        QueryBuilder<StepData> qb = new QueryBuilder<StepData>(StepData.class)
                .columns(new String[]{"id","today","step","previousStep"})
                .appendOrderDescBy("today") ;       //升序

        List<StepData> list = liteOrm.query(qb);
        for(StepData stepData:list){
            stepData.toString();
        }
//        List list = liteOrm.query(StepData.class);
//        OrmLog.i(TAG, list.size());
        return list;
//        return liteOrm.query(cla);
    }



    /**
     * 查询  某字段 等于 Value的值
     *
     * @param cla
     * @param field
     * @param value
     * @return
     */
    public static <T> List<T> getQueryByWhere(Class<T> cla, String field, Object[] value) {
        return liteOrm.<T>query(new QueryBuilder(cla).where(field + "=?", value));
    }
    /**
     * 查询  某字段 等于 Value的值
     *
     * @param
     * @param
     * @param value
     * @return
     */
    public static  List<StepData> getQueryByWhere(   Object[] value) {
        return liteOrm. query(new QueryBuilder<StepData>(StepData.class).whereIn("today", value));
    }

    /**
     * 查询  某字段 等于 Value的值  可以指定从1-20，就是分页
     *
     * @param cla
     * @param field
     * @param value
     * @param start
     * @param length
     * @return
     */
    public static <T> List<T> getQueryByWhereLength(Class<T> cla, String field, Object[] value, int start, int length) {
        return liteOrm.<T>query(new QueryBuilder(cla).where(field + "=?", value).limit(start, length));
    }

    /**
     * 删除所有 某字段等于 Vlaue的值
     * @param cla
     * @param field
     * @param value
     */
//        public static <T> void deleteWhere(Class<T> cla,String field,String [] value){
//            liteOrm.delete(cla, WhereBuilder.create().where(field + "=?", value));
//        }

    /**
     * 删除所有
     *
     * @param cla
     */
    public static <T> void deleteAll(Class<T> cla) {
        liteOrm.deleteAll(cla);
    }

    /**
     * 仅在以存在时更新
     *
     * @param t
     */
    public static <T> void update(T t) {
        liteOrm.update(t, ConflictAlgorithm.Replace);
    }


    public static <T> void updateALL(List<T> list) {
        liteOrm.update(list);
    }

    public static void closeDb(){
        liteOrm.close();
    }

}
