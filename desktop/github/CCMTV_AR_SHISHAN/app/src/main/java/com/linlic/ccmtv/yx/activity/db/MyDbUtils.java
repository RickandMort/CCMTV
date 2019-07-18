package com.linlic.ccmtv.yx.activity.db;

import android.content.Context;
import android.database.Cursor;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DaoConfig;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.SqlInfo;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import com.linlic.ccmtv.yx.activity.entity.CalendarReminder;
import com.linlic.ccmtv.yx.activity.entity.DbDownloadVideo;
import com.linlic.ccmtv.yx.activity.entity.DbUploadCase;
import com.linlic.ccmtv.yx.activity.entity.DbUploadVideo;
import com.linlic.ccmtv.yx.activity.entity.DbUser;
import com.linlic.ccmtv.yx.activity.entity.Examination_script;
import com.linlic.ccmtv.yx.activity.entity.Examination_script_paper;
import com.linlic.ccmtv.yx.activity.entity.Hot_search_grid;
import com.linlic.ccmtv.yx.activity.entity.RecordVideo;
import com.linlic.ccmtv.yx.activity.entity.User_search_grid;
import com.linlic.ccmtv.yx.activity.my.download.DownloadService;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.bean.DbSearchArticle;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import java.io.File;
import java.util.List;

/**
 * name：sqlite本地数据库工具类
 * author：MrSong
 * data：2016/4/5.
 * https://github.com/wyouflf/xUtils
 */
public class MyDbUtils {
    //    private static String dbPath = URLConfig.ccmtvapp_basesdcardpath + "/db/";
//    private static String dbName = "ccmtv.db";
    private static int dbVersion = 3;

    private static DbUtils configDb(Context context) {
        DaoConfig config = new DaoConfig(context);
        // DbUtils db = DbUtils.create(context);
//        /storage/emulated/0/ccmtvCache/db/    "ccmtv.db";
//        Environment.getExternalStorageDirectory() + "/ccmtvCache
        String dirPath=URLConfig.ccmtvapp_basesdcardpath + "/db/";
        File dir=new File(dirPath);
        if(!dir.exists())
            dir.mkdirs();

        config.setDbDir(context.getCacheDir( ).getPath());//db路径
        config.setDbName("ccmtv.db"); //db名
        config.setDbVersion(dbVersion);  //db版本
        DbUtils db = DbUtils.create(config);


//        DbUtils db = DbUtils.create(context, "/storage/emulated/0/ccmtvCache/db/", "ccmtv.db", dbVersion, new DbUtils.DbUpgradeListener() {
//            @Override
//            public void onUpgrade(DbUtils dbUtils, int i, int i1) {
//                Log.e("-=-=-=-=-=-=-=-=-",dbUtils.toString()+"======="+i+"========"+i1);
//            }
//        });


        return db;
    }

    /**
     * name：保存下载视频资料
     * author：MrSong
     * data：2016/3/30 14:21
     */
    public static void saveVideoMsg(Context context, String videoId, String videoName, String downURL, String downProgress, String state, String picurl, String videoClass) {
        try {
            DbUtils db = configDb(context);
            // DbUtils db = DbUtils.create(context, dbPath, dbName);
            db.createTableIfNotExist(DbDownloadVideo.class);//创建一个表User
            db.configAllowTransaction(true);//开启事务
            DbDownloadVideo video = new DbDownloadVideo();
            video.setVideoId(videoId);
            video.setVideoName(videoName);
            video.setDownURL(downURL);
            video.setDownProgress(downProgress);
            video.setState(state);
            video.setTotal("");
            video.setCurrent("");
            video.setFilePath("default");
            video.setSpeed("");
            video.setPicUrl(picurl);
            video.setVideoClass(videoClass);
            db.save(video);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * name：查询等待下载的视频
     * author：MrSong
     * data：2016/4/5 19:45
     */
    public static String findWaitDown(Context context) {
        try {
            DbUtils db = configDb(context);
            DbDownloadVideo video = db.findFirst(Selector.from(DbDownloadVideo.class).where("state", "=", DownloadService.download_wait));
            if (video != null) {
                String id = video.getId() + "";
                return id;
            } else {
                return "";
            }
        } catch (DbException e) {
            return "";
        }
    }

    /**
     * name：根据视频ID查询当前视频是否有下载过
     * author：MrSong
     * data：2016/4/6 16:51
     * return true下载过   false没有下载过
     */
    public static boolean findVideoExist(Context context, String videoId) {
        DbDownloadVideo video = findVideoMsg(context, videoId);
        try {
            if (video.getVideoName().isEmpty()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * name：根据视频ID查找数据库，更改下载状态
     * author：MrSong
     * data：2016/4/6 17:23
     */
    public static void updateDownState(Context context, String videoId, String state) {
        try {
            DbUtils db = configDb(context);
            String sql = "update video set state = " + state + " where videoId = " + videoId;
            db.execNonQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * name：根据视频ID获取当前视频的详细信息
     * author：MrSong
     * data：2016/4/6 17:46
     */
    public static DbDownloadVideo findVideoMsg(Context context, String videoId) {
        try {
            DbUtils db = configDb(context);
            return db.findFirst(Selector.from(DbDownloadVideo.class).where("videoId", "=", videoId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * name：根据数据库ID获取当前视频的详细信息
     * author：MrSong
     * data：2016/4/7 12:33
     */
    public static DbDownloadVideo findVideoMsgForId(Context context, String id) {
        try {
            DbUtils db = configDb(context);
            return db.findFirst(Selector.from(DbDownloadVideo.class).where("id", "=", id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * name：根据视频ID查找数据库，更改下载状态与下载进度
     * author：MrSong
     * data：2016/4/6 17:23 total  current  filePath
     */
    public static void updateDownStateAndProgress(
            Context context, String videoId, String state, String progress, String total, String current, String filePath, String speed) {
        try {
            DbUtils db = configDb(context);
            String sql = "update video set state = '" + state + "' , downProgress = '" + progress + "' " + ", total = '"
                    + total + "' , current = '" + current + "' , filePath = '" + filePath + "' , speed = '" + speed + "' " +
                    " where videoId = " + videoId;
//            Log.d("更改数据库sql", sql);
            db.execNonQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * name：查询下载列表数据
     * author：MrSong
     * data：2016/4/7 14:10
     */
    public static List<DbDownloadVideo> findAll(Context context) {
        try {
            DbUtils db = configDb(context);
            return db.findAll(DbDownloadVideo.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * name：查询下载列表数据
     * author：MrSong
     * data：2016/4/7 14:10
     */
    public static List<DbModel> findAllGroup(Context context) {
        try {
            DbUtils db = configDb(context);
            String sql = "select count(videoClass) as count , picUrl , videoClass , total , sum(substr(total,1, length(total)-1)) as sum from video where state = 200  group by videoClass ";
            SqlInfo info = new SqlInfo();
            info.setSql(sql);
            return db.findDbModelAll(info);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * name：根据分组名称查找当前分组下的所有视频
     * author：MrSong
     * data：2016/7/13 11:41
     */
    public static List<DbDownloadVideo> findGroupMsg(Context context, String videoClass) {
        try {
            DbUtils db = configDb(context);
            return db.findAll(Selector.from(DbDownloadVideo.class).where("videoClass", "=", videoClass));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * name：查询多少个任务正在下载
     * author：MrSong
     * data：2016/6/28 16:28
     */
    public static List<DbDownloadVideo> findDownloading(Context context) {
        try {
            DbUtils db = configDb(context);
            return db.findAll(Selector.from(DbDownloadVideo.class).where("state", "!=", "200"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * name：查询当前类型下所有已完成的视频
     * author：MrSong
     * data：2016/6/28 16:28
     */
    public static List<DbDownloadVideo> findDownlodVideoOK(Context context, String videoClass) {
        try {
            DbUtils db = configDb(context);
            return db.findAll(Selector.from(DbDownloadVideo.class).where("videoClass", "=", videoClass).and("state", "=", "200"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * name：查询所有已完成的视频
     * author：Niklaus
     * data：2017/11/13
     */
    public static List<DbDownloadVideo> findAllDownlodVideoOK(Context context) {
        try {
            DbUtils db = configDb(context);
            return db.findAll(Selector.from(DbDownloadVideo.class).where("state", "=", "200"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * name：保存用户信息
     * author：Larry
     * data：2016/4/13 17:41
     */
    public static void saveUserInfo(Context context, String username) {
        try {
            DbUtils db = configDb(context);
            db.createTableIfNotExist(DbUser.class);//创建一个表User
            db.configAllowTransaction(true);//开启事务
            DbUser user = new DbUser();
            user.setUsername(username);
            db.save(user);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * name：查询登陆用户名
     * author：Larry
     * data：2016/4/13 17:53
     */
    public static DbUser findUserInfo(Context context, String userStr) {
        try {
            DbUtils db = configDb(context);
            return db.findFirst(Selector.from(DbUser.class).where("username", "=", userStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * name：查询所有用户名
     * author：Larry
     * data：2016/4/13 17:58
     */
    public static List<DbUser> findAllUserInfo(Context context) {
        try {
            DbUtils db = configDb(context);
            return db.findAll(DbUser.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * name：删除所有的用户信息
     * author：Larry
     * data：2016/5/23 11:39
     */
    public static void deleteAllUserInfo(Context context) {
        try {
            DbUtils db = configDb(context);
            String sql = "delete from user";
            db.execNonQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * name：记录视频播放进度
     * author：Larry
     * data：2017/4/7 17:41
     */
    public static void saveRecordVideo(Context context, String videoId, long viewingTime) {
        try {
            DbUtils db = configDb(context);
            db.createTableIfNotExist(RecordVideo.class);//创建一个表User
            db.configAllowTransaction(true);//开启事务
            RecordVideo recordVideo = new RecordVideo();
            recordVideo.setVideoId(videoId);
            recordVideo.setViewingTime(viewingTime);
            db.save(recordVideo);

        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * name：查询视频播放进度
     * author：Larry
     * data：2016/4/13 17:53
     */
    public static long findRecordVideo(Context context, String videoId) {
        try {
            DbUtils db = configDb(context);
            RecordVideo recordVideo = db.findFirst(Selector.from(RecordVideo.class).where("videoId", "=", videoId));
//            RecordVideo recordVideo = db.findFirst(Selector.from(RecordVideo.class).where("videoId", "in", "('12','45','46')"));
            if (recordVideo != null) {
                long viewingTime = recordVideo.getViewingTime();
                return viewingTime;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * name：更新视频播放进度
     * author：Larry
     * data：2016/4/13 17:53
     */
    public static void updateRecordVideo(Context context, String videoId, long viewingTime) {
        try {
            DbUtils db = configDb(context);
            String sql = "update recordvideo set viewingTime = " + viewingTime + " where videoId = " + videoId;
            db.execNonQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * name：查询本地视频播放进度
     * author：Larry
     */
    public static long findLocalRecordVideo(Context context, String path) {
        try {
            DbUtils db = configDb(context);
            RecordVideo recordVideo = db.findFirst(Selector.from(RecordVideo.class).where("videoId", "=", path));
            if (recordVideo != null) {
                long viewingTime = recordVideo.getViewingTime();
                return viewingTime;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * name：更新本地视频播放进度
     * author：Larry
     */
    public static void updateLocalRecordVideo(Context context, String path, long viewingTime) {
        try {
            DbUtils db = configDb(context);
            RecordVideo record = db.findFirst(Selector.from(RecordVideo.class).where("videoId", "=", path));
            if ( record!= null) {
                String sql = "update recordvideo set viewingTime = " + viewingTime + " where videoId = " + path;
                db.execNonQuery(sql);
            } else {
                db.createTableIfNotExist(RecordVideo.class);//创建一个表User
                db.configAllowTransaction(true);//开启事务
                RecordVideo recordVideo = new RecordVideo();
                recordVideo.setVideoId(path);
                recordVideo.setViewingTime(viewingTime);
                db.save(recordVideo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * name：根据视频ID删除当前数据
     * author：MrSong
     * data：2016/4/18 15:17
     */
    public static void deleteVideo(Context context, String videoId) {
        try {
            DbUtils db = configDb(context);
            String sql = "delete from video where videoId = " + videoId;
            db.execNonQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * name：保存上传视频资料
     * author：MrSong
     * data：2016/4/22 14:21
     */
    public static void saveUploadVideoMsg(Context context, String state, String videoName, String videoMsg, String videoFilePath, String imageFilePath) {
        try {
            DbUtils db = configDb(context);
            db.createTableIfNotExist(DbUploadVideo.class);//创建一个表User
            db.configAllowTransaction(true);//开启事务

            DbUploadVideo video = new DbUploadVideo();
            video.setVideoName(videoName);
            video.setVideoMsg(videoMsg);
            video.setState(state);
            video.setFilePath(videoFilePath);
            video.setFileImgPath(imageFilePath);
            video.setUploadProgress("0%");
            video.setTotal("0");
            video.setCurrent("0");
            db.save(video);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * name：根据上传视频信息，获取当前数据库ID
     * author：MrSong
     * data：2016/4/22 23:52
     */
    public static int findUploadVideoMsg(Context context, String videoName, String videoMsg, String filePath, String fileImgPath) {
        try {
            DbUtils db = configDb(context);
            DbUploadVideo dbUploadVideo = db.findFirst(Selector.from(DbUploadVideo.class).where("videoName", "=", videoName)
                            .and("videoMsg", "=", videoMsg).and("state", "=", "300").and("filePath", "=", filePath)
                            .and("fileImgPath", "=", fileImgPath).and("uploadProgress", "=", "0%").and("total", "=", "0")
                            .and("current", "=", "0")
            );
            return dbUploadVideo.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * name：根据数据库ID查找数据库，更改上传状态
     * author：MrSong
     * data：2016/4/22 17:23
     */
    public static void updateUploadState(Context context, String state, int dbid) {
        try {
            DbUtils db = configDb(context);
            String sql = "update upload set state = " + state + " where id = " + dbid;
            db.execNonQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * name：根据数据库ID查询数据库，更改当前视频信息状态
     * author：MrSong
     * data：2016/4/23 0:42
     */
    public static void updateUploadStateMsg(Context context, String state, int dbid, String progress, String total, String current) {
        try {
            DbUtils db = configDb(context);
            String sql = "update upload set state = '" + state + "' , uploadProgress = '" + progress + "' " + ", total = '"
                    + total + "' , current = '" + current + "' " +
                    " where id = " + dbid;
//            Log.d("更改数据库sql", sql);
            db.execNonQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * name：查询所有上传视频
     * author：MrSong
     * data：2016/4/23 2:56
     */
    public static List<DbUploadVideo> findAllUploadVideo(Context context) {
        try {
            DbUtils db = configDb(context);
            return db.findAll(DbUploadVideo.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * name：删除上传视频记录
     * author：MrSong
     * data：2016/4/23 4:47
     */
    public static void deleteUploadVideo(Context context, String id) {
        try {
            DbUtils db = configDb(context);
            String sql = "delete from upload where id = " + id;
            db.execNonQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * name：保存上传病例信息
     * author：Larry
     * data：2016/5/4 18:21
     */
    public static void saveUploadCaseMsg(Context context, String case_title, String state, String upfileA_1, String upfileA_2, String upfileA_3,
                                         String upfileB_1, String upfileB_2, String upfileB_3,
                                         String upfileC_1, String upfileC_2, String upfileC_3,
                                         String upfileD_1, String upfileD_2, String upfileD_3,
                                         String upfileE_1, String upfileE_2, String upfileE_3,
                                         String upfileF_1, String upfileF_2, String upfileF_3,
                                         String upfileG_1, String upfileG_2, String upfileG_3,
                                         String upfileH_1, String upfileH_2, String upfileH_3
    ) {
        try {
            DbUtils db = configDb(context);
            db.createTableIfNotExist(DbUploadCase.class);//创建一个表User
            db.configAllowTransaction(true);//开启事务

            DbUploadCase Case = new DbUploadCase();
            Case.setState(state);
            Case.setUpfileA_1(upfileA_1);
            Case.setUpfileA_2(upfileA_2);
            Case.setUpfileA_3(upfileA_3);

            Case.setUpfileB_1(upfileB_1);
            Case.setUpfileB_2(upfileB_2);
            Case.setUpfileB_3(upfileB_3);

            Case.setUpfileC_1(upfileC_1);
            Case.setUpfileC_2(upfileC_2);
            Case.setUpfileC_3(upfileC_3);

            Case.setUpfileD_1(upfileD_1);
            Case.setUpfileD_2(upfileD_2);
            Case.setUpfileD_3(upfileD_3);

            Case.setUpfileE_1(upfileE_1);
            Case.setUpfileE_2(upfileE_2);
            Case.setUpfileE_3(upfileE_3);

            Case.setUpfileF_1(upfileF_1);
            Case.setUpfileF_2(upfileF_2);
            Case.setUpfileF_3(upfileF_3);

            Case.setUpfileG_1(upfileG_1);
            Case.setUpfileG_2(upfileG_2);
            Case.setUpfileG_3(upfileG_3);

            Case.setUpfileH_1(upfileH_1);
            Case.setUpfileH_2(upfileH_2);
            Case.setUpfileH_3(upfileH_3);
            Case.setCaseTitle(case_title);
            Case.setUploadProgress("0%");
            Case.setTotal("0");
            Case.setCurrent("0");
            Case.setUptime("0");
            db.save(Case);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * name：根据上传病例信息，获取当前数据库ID
     * author：Larry
     * data：2016/5/5 16:22
     */
    public static int findUploadCaseMsg(Context context, String upfileA_1, String upfileA_2, String upfileA_3,
                                        String upfileB_1, String upfileB_2, String upfileB_3,
                                        String upfileC_1, String upfileC_2, String upfileC_3,
                                        String upfileD_1, String upfileD_2, String upfileD_3,
                                        String upfileE_1, String upfileE_2, String upfileE_3,
                                        String upfileF_1, String upfileF_2, String upfileF_3,
                                        String upfileG_1, String upfileG_2, String upfileG_3,
                                        String upfileH_1, String upfileH_2, String upfileH_3) {
        try {
            DbUtils db = configDb(context);
            DbUploadCase dbUploadCase = db.findFirst(Selector.from(DbUploadCase.class).where("state", "=", "300")
                            .and("upfileA_1", "=", upfileA_1).and("upfileA_2", "=", upfileA_2).and("upfileA_3", "=", upfileA_3)
                            .and("upfileB_1", "=", upfileB_1).and("upfileB_2", "=", upfileB_2).and("upfileB_3", "=", upfileB_3)
                            .and("upfileC_1", "=", upfileC_1).and("upfileC_2", "=", upfileC_2).and("upfileC_3", "=", upfileC_3)
                            .and("upfileD_1", "=", upfileD_1).and("upfileD_2", "=", upfileD_2).and("upfileD_3", "=", upfileD_3)
                            .and("upfileE_1", "=", upfileE_1).and("upfileE_2", "=", upfileE_2).and("upfileE_3", "=", upfileE_3)
                            .and("upfileF_1", "=", upfileF_1).and("upfileF_2", "=", upfileF_2).and("upfileF_3", "=", upfileF_3)
                            .and("upfileG_1", "=", upfileG_1).and("upfileG_2", "=", upfileG_2).and("upfileG_3", "=", upfileG_3)
                            .and("upfileH_1", "=", upfileH_1).and("upfileH_2", "=", upfileH_2).and("upfileH_3", "=", upfileH_3)
                            .and("uploadProgress", "=", "0%").and("total", "=", "0")
                            .and("current", "=", "0").and("uptime", "=", "0")
            );
            return dbUploadCase.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * name：根据病例在数据库中存储Id，获取当前病例信息
     * author：Larry
     * data：2016/5/5 16:22
     */
    public static DbUploadCase findUploadCaseMsg_Img(Context context, String case_id) {
        try {
            DbUtils db = configDb(context);
            DbUploadCase dbUploadCase = db.findFirst(Selector.from(DbUploadCase.class).where("id", "=", case_id)
            );
            return dbUploadCase;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * name：根据数据库ID查找数据库，更改上传状态
     * author：Larry
     * data：2016/5/4 19:05
     */
    public static void updateUploadCaseState(Context context, String state, int case_id) {
        try {
            DbUtils db = configDb(context);
            String sql = "update uploadCase set state = " + state + " where id = " + case_id;
            db.execNonQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * name：根据数据库ID查询数据库，更改当前图片信息状态
     * author：Larry
     * data：2016/5/4 19:11
     */
    public static void updateUploadCaseStateMsg(Context context, String state, int case_id, String progress, String total, String current) {
        try {
            DbUtils db = configDb(context);
            String sql = "update uploadCase set state = '" + state + "' , uploadProgress = '" + progress + "' " + ", total = '"
                    + total + "' , current = '" + current + "' " +
                    " where id = " + case_id;
//            Log.d("更改数据库sql", sql);
            db.execNonQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * name：根据数据库ID查询数据库，更改当前病例上传成功时间
     * author：Larry
     * data：2016/5/4 19:11
     */
    public static void updateUploadCaseTime(Context context, String uptime, int case_id) {
        try {
            DbUtils db = configDb(context);
            String sql = "update uploadCase set uptime = '" + uptime + "' where id = " + case_id;
//            Log.d("更改数据库sql", sql);
            db.execNonQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * name：查询所有上传图片
     * author：MrSong
     * data：2016/4/23 2:56
     */
    public static List<DbUploadCase> findAllUploadCase(Context context) {
        try {
            DbUtils db = configDb(context);
            return db.findAll(DbUploadCase.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * name：删除上传病例记录
     * author：MrSong
     * data：2016/4/23 4:47
     */
    public static void deleteUploadCase(Context context, String id) {
        try {
            DbUtils db = configDb(context);
            String sql = "delete from uploadCase where id = " + id;
            db.execNonQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存文章搜索记录
     * @param context
     * @param articleName
     */
    public static void saveAtricle(Context context, String articleName) {
        try {
            DbUtils db = configDb(context);
            db.createTableIfNotExist(DbSearchArticle.class);//创建一个表User
            db.configAllowTransaction(true);//开启事务

            try {
                DbSearchArticle dbSearchArticle = db.findFirst(Selector.from(DbSearchArticle.class).where("articleName", "=", articleName));
                if (dbSearchArticle != null) {
                    String sql = "update article set search_num = '" + (dbSearchArticle.getSearch_num() + 1) + "' where id = " + dbSearchArticle.getId();
                    db.execNonQuery(sql);
                } else {
                    dbSearchArticle = new DbSearchArticle();
                    dbSearchArticle.setId(SharedPreferencesTools.getHot_search_id(context));
                    dbSearchArticle.setArticleName(articleName);
                    dbSearchArticle.setSearch_num(0);
                    db.save(dbSearchArticle);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询文章搜索历史
     * @param context
     * @return
     */
    public static List<DbSearchArticle> findArticleAll(Context context) {
        try {
            DbUtils db = configDb(context);
            return db.findAll(Selector.from(DbSearchArticle.class).orderBy("search_num", true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteArticle(Context context, String id) {
        try {
            DbUtils db = configDb(context);
            String sql = "delete from article where id = " + id;
            db.execNonQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * name：保存下载视频资料
     * author：MrSong
     * data：2016/3/30 14:21
     */
    public static void saveHot_search_grid(Context context,   String hot_search_name) {
        try {
            DbUtils db = configDb(context);
            // DbUtils db = DbUtils.create(context, dbPath, dbName);
            db.createTableIfNotExist(Hot_search_grid.class);//创建一个表User
            db.configAllowTransaction(true);//开启事务

            try {

                Hot_search_grid hot_search_grid =   db.findFirst(Selector.from(Hot_search_grid.class).where("hot_search_name", "=", hot_search_name));
                if(hot_search_grid!=null){
                    String sql = "update hot_search_grid set search_num = '" +(hot_search_grid.getSearch_num()+1)  + "' where id = " + hot_search_grid.getId();
//            Log.d("更改数据库sql", sql);
                    db.execNonQuery(sql);
                } else {
                    hot_search_grid = new Hot_search_grid();
                    hot_search_grid.setId(SharedPreferencesTools.getHot_search_id(context));
                    hot_search_grid.setHot_search_name(hot_search_name);
                    hot_search_grid.setSearch_num(0);

                    db.save(hot_search_grid);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * name：查询本地搜索记录
     * author：tom
     * data：2016/4/13 17:53
     */
    public static List<Hot_search_grid> findHot_search_grid_All(Context context) {
        try {
            DbUtils db = configDb(context);
            return db.findAll(Selector.from(Hot_search_grid.class).orderBy("search_num", true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * name：删除搜搜记录
     * author：tom
     * data：2016/4/23 4:47
     */
    public static void deleteHot_search_gridALL(Context context) {
        try {
            DbUtils db = configDb(context);
            db.deleteAll(Hot_search_grid.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * name：保存同事关注搜索记录
     * author：MrSong
     * data：2016/3/30 14:21
     */
    public static void saveUser_search_grid(Context context,   String user_search_name) {
        try {
            DbUtils db = configDb(context);
            // DbUtils db = DbUtils.create(context, dbPath, dbName);
            db.createTableIfNotExist(User_search_grid.class);//创建一个表User
            db.configAllowTransaction(true);//开启事务

            try {

                User_search_grid user_search_grid =   db.findFirst(Selector.from(User_search_grid.class).where("user_search_name", "=", user_search_name));
                if(user_search_grid!=null){
                    String sql = "update user_search_grid set search_num = '" +(user_search_grid.getSearch_num()+1)  + "' where id = " + user_search_grid.getId();
//            Log.d("更改数据库sql", sql);
                    db.execNonQuery(sql);
                } else {
                    user_search_grid = new User_search_grid();
                    user_search_grid.setId(SharedPreferencesTools.getHot_search_id(context));
                    user_search_grid.setUser_search_name(user_search_name);
                    user_search_grid.setSearch_num(0);

                    db.save(user_search_grid);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * name：查询本地同事关注搜索记录
     * author：tom
     * data：2016/4/13 17:53
     */
    public static List<User_search_grid> findUser_search_grid_All(Context context) {
        try {
            DbUtils db = configDb(context);
            return db.findAll(Selector.from(User_search_grid.class).orderBy("search_num", true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * name：删除同事关注搜索记录
     * author：tom
     * data：2016/4/23 4:47
     */
    public static void deleteUser_search_gridALL(Context context) {
        try {
            DbUtils db = configDb(context);
            db.deleteAll(User_search_grid.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * name：保存试卷答案
     * author：Tom
     * data：2016/3/30 14:21
     */
    public static void saveExamination_script(Context context, String eid, String option_id, String option_name) {
        try {
            DbUtils db = configDb(context);
            // DbUtils db = DbUtils.create(context, dbPath, dbName);
            db.createTableIfNotExist(Examination_script.class);//创建一个表User
            db.configAllowTransaction(true);//开启事务

            try {

                Examination_script examination_script = db.findFirst(Selector.from(Examination_script.class).where("option_id", "=", option_id).and("eid", "=", eid));
                if (examination_script != null) {
                    String sql = "update examination_script set option_name = '" + option_name + "' where option_id = " + examination_script.getOption_id() + " and eid = " + examination_script.getEid();
//            Log.d("更改数据库sql", sql);
                    db.execNonQuery(sql);
                } else {
                    examination_script = new Examination_script();
                    examination_script.setEid(eid);
                    examination_script.setOption_id(option_id);
                    examination_script.setOption_name(option_name);

                    db.save(examination_script);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * name：查询本地试卷答案
     * author：tom
     * data：2016/4/13 17:53
     */
    public static List<Examination_script> findExamination_script_All(Context context, String eid) {
        try {
            DbUtils db = configDb(context);
            return db.findAll(Selector.from(Examination_script.class).where("eid", "=", eid));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * name：查询本地试卷答案
     * author：tom
     * data：2016/4/13 17:53
     */
    public static List<Examination_script> findExamination_script_All(Context context) {
        try {
            DbUtils db = configDb(context);
            return db.findAll(Selector.from(Examination_script.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * name：查询本地试卷答案
     * author：tom
     * data：2016/4/13 17:53
     */
    public static void findExamination_script_All_eid(Context context) {
        try {
            DbUtils db = configDb(context);
            Cursor cursor = db.execQuery("select distinct eid from examination_script");
            while (cursor.moveToNext()) {
//                LogUtil.e("试卷-本地保存答案", cursor.getString(cursor
//                        .getColumnIndex("eid")));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * name：删除试卷答案
     * author：tom
     * data：2016/4/23 4:47
     */
    public static void deleteExamination_scriptALL(Context context) {
        try {
            DbUtils db = configDb(context);
            db.deleteAll(Examination_script.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * name：删除试卷答案
     * author：tom
     * data：2016/4/23 4:47
     */
    public static void deleteExamination_scriptALL(Context context, String eid, String problem_id) {
        try {
            DbUtils db = configDb(context);
            if (db.findFirst(Selector.from(Examination_script.class).where("option_id", "=", problem_id).and("eid", "=", eid)) != null) {
                db.delete(db.findFirst(Selector.from(Examination_script.class).where("option_id", "=", problem_id).and("eid", "=", eid)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * name：保存试卷信息
     * author：Tom
     * data：2016/3/30 14:21
     */
    public static void  saveExamination_script_paper(Context context, String eid,  String config,long datetime ) {
        try {
            DbUtils db = configDb(context);
            // DbUtils db = DbUtils.create(context, dbPath, dbName);
            db.createTableIfNotExist(Examination_script_paper.class);//创建一个表User
            db.configAllowTransaction(true);//开启事务

            try {

                Examination_script_paper examination_script =   db.findFirst(Selector.from(Examination_script_paper.class).where("eid", "=", eid));
                if(examination_script!=null){
                    examination_script.setDatetiem_count((Integer.parseInt(examination_script.getDatetiem_count())+datetime)+"");
                    String sql = "update examination_script_paper set config = '" +config  + "',datetiem_count = '"+examination_script.getDatetiem_count()+"'  where eid = " + examination_script.getEid();
//            Log.d("更改数据库sql", sql);
                    db.execNonQuery(sql);
                }else{
                    examination_script = new Examination_script_paper();
                    examination_script.setEid(eid);
                    examination_script.setConfig(config);
                    examination_script.setDatetiem_count(datetime+"");
                    db.save(examination_script);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * name：查询本地试卷信息
     * author：tom
     * data：2016/4/13 17:53
     */
    public static  Examination_script_paper  findExamination_script_paper(Context context,String eid ) {
        try {
            DbUtils db = configDb(context);
            return   db.findFirst(Selector.from(Examination_script_paper.class).where("eid", "=", eid));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * name：保存日程信息
     * author：Tom
     * data：2016/3/30 14:21
     */
    public static void saveCalendarReminder(Context context,   String aid, String calendar_id,String uri) {
        try {
            DbUtils db = configDb(context);
            // DbUtils db = DbUtils.create(context, dbPath, dbName);
            db.createTableIfNotExist(CalendarReminder.class);//创建一个表User
            db.configAllowTransaction(true);//开启事务

            try {

                CalendarReminder calendarReminder = db.findFirst(Selector.from(CalendarReminder.class).where("aid", "=", aid) );
                if (calendarReminder != null) {
                    String sql = "update calendarreminder set calendar_id = '" + calendar_id + "',uri = '"+uri+"' where aid = " + calendarReminder.getAid() + " and id = " + calendarReminder.getId();
//            Log.d("更改数据库sql", sql);
                    db.execNonQuery(sql);
                } else {
                    calendarReminder = new CalendarReminder();
                    calendarReminder.setAid(aid);
                    calendarReminder.setCalendar_id(calendar_id);
                    calendarReminder.setUri(uri);

                    db.save(calendarReminder);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * name：查询日程
     * author：tom
     * data：2016/4/13 17:53
     */
    public static  CalendarReminder  findCalendarReminder(Context context,String aid ) {
        try {
            DbUtils db = configDb(context);
            return   db.findFirst(Selector.from(CalendarReminder.class).where("aid", "=", aid));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}