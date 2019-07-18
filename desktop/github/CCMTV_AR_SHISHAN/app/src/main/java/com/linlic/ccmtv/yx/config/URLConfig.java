package com.linlic.ccmtv.yx.config;

import android.os.Environment;

/**
 * 接口URL存储
 *
 * @author LTA4
 */
public class

URLConfig {
    //测试环境
    // public static String Interface_URL1 = "http://192.168.30.201:8080/";
//    public static String Interface_URL = "http://192.168.30.201/";
//    public static String Interface_URL1 = "http://192.168.30.201:8083/";
//    public static String Interface_URL2 = "http://192.168.30.201:8082/";
//    public static String Interface_URL3 = "http://192.168.30.201:8082/";
//    public static String Interface_URL4 = "http://192.168.30.201:8083/";
//    public static String Interface_URL5 = "http://192.168.30.201:8083/";

//        正式环境
    public static String Interface_URL = "http://www.ccmtv.cn/";
    public static String Interface_URL1 = "http://yun.ccmtv.cn/";
    public static String Interface_URL2 = "http://www.ccmtv.cn/upload_files/new_upload_files/ccmtvtp/";
    public static String Interface_URL3 = "http://www.ccmtv.cn/upload_files/new_upload_files/ccmtvtp/";
    public static String Interface_URL4 = "http://yun.ccmtv.cn/";
    public static String Interface_URL5 = "http://yy.ccmtv.cn/";
    //私人医生专业版下载地址
    public static String SuiFangJia_Doctor = "http://fusion.qq.com/cgi-bin/qzapps/unified_jump?appid=12176514&from=mqq&actionFlag=0&params=pname%3Dcom.linlic%26versioncode%3D4%26channelid%3D%26actionflag%3D0";
    //私人医生大众版下载地址
    public static String SuiFangJia_Patient = "http://fusion.qq.com/cgi-bin/qzapps/unified_jump?appid=12176557&from=mqq&actionFlag=0&params=pname%3Dcom.linlic.patient%26versioncode%3D3%26channelid%3D%26actionflag%3D0";
    // 商户PID
    public static final String PARTNER = "2088511301093807";
    // 商户收款账号
    public static final String SELLER = "ccmtv.shanghai@ccmtv.cn";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKeB67g18PpzPxvacjJg/03s+TfpQ2prd2vxXou/Jq8aiaet56P0Pe6MX8AQaJaTYTkHC81BCnJ9dwdAM72ncUB3rz1+lzPS/bvN/V6EVdBAMSwYPBiwypDm352C5LsZ3zTF4ZP6O+JvcNo++hno6tMrLf3sdJlgV3C6WxbfovCdAgMBAAECgYBhY2kADeSQSGb9s7DHV5u327o95pH6koa5LHkyuK0uCzjTU4mkykEjq1uUGDYmrYNOG6mh6VnAnO+SOKKcdnih+Js3k0JFwqZuTIMq3EbyrtA+r4Fz/cka7/Xa3A9X6pOAvohDoo/Rl2uQgbVJ1tyhyw+QbtJ6fRmKBbbHTSPm4QJBANm2FnWGklIaeWU1X7Dji9RAq57YgP5k+ZBvsVxZ9ekpxta1veNwVchsmcC8HsI8WYk0jJiXK/3mCocyfR6f4WkCQQDE94g14s9HLGRdmuJPIYQtJBIaKIFuAdrdvQbUsAguqdZvhGZwo+wCIjX2lLVL5ku7q7YmkYuDgwqhQhWSeHsVAkEAllvJmxV7zLR4DswT8IjWs5qObMA+JnP6YCSjq5J94bB2oScXpBKbTDak4ZWK8L7ZX4cpauFNAqdzsffOre3hsQJAezLY39ueCsaZgHMAWr58DkAhknsDeetvGoLBYwc4FvfkSJxn9syRMkRzvGgHfhHuDNS6eiVPPlMYswTCuXrigQJASHBxHRx32txrLSphHrKlAVpHvZdLe5PtehF3+Y8bULhM5Z+obtORA0KNv8+QLvljqq1ongunqQQiPqfpMtltZQ==";
    public static final int SDK_PAY_FLAG = 1;
    //微信
    public static final String APP_ID = "wx37532d8c612b2c3f";
    // 商户号
    public static final String MCH_ID = "1328628101";
    // API密钥，在商户平台设置
    public static final String API_KEY = "linlicccmtv2016YanKaiWangjuanAnd";


    // 常用接口URLccmtvappandroid/androidccmtvapp.php
    public static String CCMTVAPP = Interface_URL + "do/ccmtvappandroid/ccmtvapp.php";
    public static String PDCCMTVAPP = Interface_URL + "do/ccmtvappandroid/pdccmtvapp.php";
    public static String CCMTVAPP1 = Interface_URL + "do/ccmtvappandroid/androidccmtvapp.php";
    public static String QR_CCMTVAPP = Interface_URL + "do/ccmtvapplogin/control.php";
    public static String CCMTVAppPayNew = Interface_URL + "do/ccmtvappandroid/WeChatpay/example/ccmtvAppPayNew.php";
    public static String CCMTVAPP_SEARCH = Interface_URL + "do/ccmtvappandroid/ccmtvapp_search.php";
    public static String CCMTVAPP_Commodity = Interface_URL + "do/ccmtvappandroid/androidccmtvapp_jifen.php";//积分商城
    public static String CCMTVAPP_ccmtvapplive = Interface_URL + "do/ccmtvappandroid/ccmtvapplive.php";//直播
    public static String CCMTVAPP_GpApi = Interface_URL4 + "admin.php/AndroidGpApi/index";//规赔
    public static String CCMTVAPP_kq = Interface_URL4 + "admin.php/SignInApi/index";//考勤
    public static String CCMTVAPP_PUTHApi = Interface_URL4 + "admin.php/AndroidGpApi";//推送
    // 更新头像调用接口
    public static String ccmtvapp_Myphoto = Interface_URL + "do/ccmtvappandroid/ccmtvapp_UserInfo.php";
    // 上传视频
    public static String ccmtvapp_uploadVideo = Interface_URL + "do/ccmtvappandroid/ccmtvapp_UserInfo.php";
    //上传病例
    public static String ccmtvapp_uploadCase = Interface_URL + "do/ccmtvappandroid/ccmtvapp_UserInfo.php";
    //sdcard存储路径
    public static String ccmtvapp_basesdcardpath = Environment.getExternalStorageDirectory() + "/ccmtvCache";

    //医考
    public static String Medical_examination = Interface_URL1 + "admin.php/AndroidApis/index";
    //医院培训本院资源首页
    public static String Hospital_training_index = Interface_URL1 + "admin.php/AndroidYyApi/index";
    //医院培训
    public static String Hospital_training = Interface_URL1 + "admin.php/AndroidYyApi";
    //public static String ccmtvapp_sdcardpath = "/storage/sdcard1" + "/ccmtvCache";
    //任务
    public static String Learning_task = Interface_URL1 + "admin.php/AndroidApis/index";
    public static String Learning_task1 = Interface_URL5 + "admin.php/AndroidApis/taskFileUpload";
    //记录视频签到亮起记录
    public static String Video_sign_light = Interface_URL1 + "admin.php/AndroidApis/index";
    //上传 教学活动
    public static String teaching_activities_upload = Interface_URL1 + "admin.php/AndroidGpApi/index";
    //空中拜访
    public static String Skyvisit = Interface_URL2 + "Skyvisit/AndroidApi/index";
    public static String SkyvisitDownload = Interface_URL3 + "Skyvisit/AndroidApi/index";
    //用户统计
    public static String UserTongji = Interface_URL1 + "admin.php/AndroidApis/index.html";
    //会议
    public static String conference = Interface_URL + "do/ccmtvappandroid/pdccmtvapp.php";
    //规培个人信息网页地址 可编辑
    public static String GpPersonalInformation = Interface_URL4 + "index.php/gphome/gp_user/userinfo";
    //规培个人信息网页地址
    public static String GpPersonalInformation3 = Interface_URL4 + "index.php/gphome/gp_user/gpcycleinfo";
    //规培个人信息网页地址 不可编辑
    public static String GpPersonalInformation2 = Interface_URL4 + "index.php/gphome/gp_user/manageInfo";
    //学生查看结业申请
    public static String GpPersonalInformation5 = Interface_URL4 + "index.php/gphome/gp_user/rotaryCareer";
    //实体书
    public static String book_url = Interface_URL + "ccmtvtp/Home/Ccmtvapp/index";
    //延时出科申请列表
    public static String GpApplyfordelay = Interface_URL1+"admin.php/AndroidGpApi/index";
    //延时出科 提交申请
    public static String GpSubmitApply = Interface_URL1+"admin.php/AndroidGpApi/index";
    /***************
     * act参数
     ******************/
    // 登陆
    public static String ulogin = "ulogin";
    // 发送短信(忘记密码)
    public static String sendMesg = "sendMesg";
    // 忘记密码
    public static String findPassword = "findPassword";
    // 注册
    public static String uregister = "uregister";
    // 科室大类
    public static String hosDepartment = "hosDepartment";
    // 科室小类
    public static String hosDepartment_o = "hosDepartment_o";
    // 职称
    public static String docPositions = "docPositions";
    // 会员类型
    public static String memberStates = "memberStates";
    // 单位（省）
    public static String hosProvince = "hosProvince";
    // 单位（市）
    public static String hosCity = "hosCity";
    // 单位（医院）
    public static String hosName = "hosName";
    // 我的资料-提交
    public static String completeUserinfo = "completeUserinfo";
    // 头像修改
    public static String myPhoto = "myPhoto";
    //获取积分
    public static String getIntegration = "getIntegration";
    //获取积分权限、
    public static String checkIntegration = "checkIntegration";
    //意见反馈
    public static String feedBack = "feedBack";
    //意见反馈 new 20190104
    public static String newFeedBack = "newFeedBack";
    //积分记录
    public static String integrationRecord = "integrationRecord";
    //积分记录-新
    public static String integrationAllRecord = "integrationAllRecord";
    //积分规则
    public static String integralRule = "integralRule";
    //我的收藏
    public static String myCollection = "myCollection";
    //我的同事
    public static String hisColleague = "hisColleague";
    //他的同事
    public static String myColleague = "myColleague";
    //我的关注、
    public static String myAttention = "myAttention";
    //他的关注、
    public static String hisAttention = "hisAttention";
    //我的消息
    public static String myMessage = "myMessage";
    //获取消息
    public static String getMessage = "getMessage";
    //认证手机
    public static String mob_certification = "mob_certification";
    //修改手机绑定
    public static String mob_ChangeBind = "mob_ChangeBind";
    //获取用户信息
    public static String getUserInfo = "getUserInfo";
    //医生执照认证
    public static String license_certification = "license_certification";
    //首页
    public static String home_page = "appIndex";
    //新版 首页 new 20180510
    public static String index = "index";
    //新版 首页 new 20180622
    public static String newIndex = "newIndex";
    //新版 首页 视频 new 20180510
    public static String getVideo = "getVideo";
    //获取视频信息
    public static String video_play_act = "getVideoInfo";
    //首页类型换一批
    public static String Change_a_lot = "changeOther";
    //点赞
    public static String doVideo = "doVideo";
    //视频播放
    public static String VideoPlay = "videoPlay";
    //专家详细信息
    public static String getExpertOnly = "getExpertOnly";
    //专家
    public static String getVideoExpert = "getVideoExpert";
    //视频详细信息
    public static String introductionInfo = "introductionInfo";
    //视频摘要
    public static String videoIntroduction = "videoIntroduction";
    //视频相关视频
    public static String videoRelevant = "videoRelevant";
    //视频相关视频 new
    public static String secvideoRelevant = "secvideoRelevant";
    //视频分享成功--增加积分
    public static String videoShareSuc = "videoShareSuc";
    //最近浏览
    public static String recentlyViewed = "recentlyViewed";
    //新版最近浏览 2018 02 03
    public static String recentlyWatchVideo = "recentlyWatchVideo";
    //热门搜索
    public static String hotKeyword = "hotKeyword";
    //发送消息
    public static String sendMessage = "sendMessage";
    //删除消息
    public static String delMessage = "delMessage";
    //搜索
    public static String videoSearch = "videoSearch";
    //新搜索
    public static String xinvideoSearch = "xinvideoSearch";
    //搜索 条件
    public static String newvideoSearch = "newvideoSearch";
    //费用详情接口
    public static String costDetails = "costDetails";
    //专题
    public static String indexCarouselList = "indexCarouselList";
    //专题 更多
    public static String indexCarouselData = "indexCarouselData";
    //视频评论
    public static String replyComment = "replyComment";
    //视频评论列表
    public static String videoContent = "videoContent";
    //邀请好友
    public static String inviteReg = "inviteReg";
    //视频评论删除
    public static String delContent = "delContent";
    //已经上传（视频）
    public static String upVideo = "upVideo";
    //正在审核（视频）
    public static String video = "video";
    //已经上传（病例）
    public static String upMedicalrecord = "upMedicalrecord";
    //正在审核（病例）
    public static String medicalrecord = "medicalrecord";
    //正在审核页
    public static String reviewVM = "reviewVM";
    //删除我的收藏
    public static String delMyCollection = "delMyCollection";
    //是否关注
    public static String attention = "attention";
    //上传视频
    public static String uploadVideo = "uploadVideo";
    //上传病例
    public static String uploadCase = "uploadCase";
    //修改密码
    public static String modifyPasswordApp = "modifyPasswordApp";
    //分享视频
    public static String videoShare = "videoShare";
    //获取预支付
    public static String getInformation = "getInformation";
    //微信支付完成调用接口
    public static String checkInformation = "checkInformation";
    //支付宝完成调用接口
    public static String completePayment = "completePayment";
    //支付宝信息提交
    public static String WeChatAlipay = "WeChatAlipay";
    //支付宝支付  充值接口所用
    public static String alipayInfo = "alipayInfo";
    public static String weixinpayurl = "http://www.ccmtv.cn/do/ccmtvappandroid/WeChatpay/example/check_notify.php";
    public static String weixinpayurlNEW = "http://www.ccmtv.cn/do/ccmtvappandroid/WeChatpay/example/check_notifynew.php";
    //获取当前服务器版本
    public static String updateDownApp = "updateDownApp";
    //当前APP下载地址
    //public static String APP_DOWNLOAD = "http://fir.im/yxsp";
    public static String APP_DOWNLOAD = "http://dwz.cn/3qVXwE";
    //微信支付访问URL
    public static String pripayIdurl = Interface_URL + "do/ccmtvappandroid/WeChatpay/example/ccmtvAppPay.php";
    //支付宝回调地址
    public static String alipayNotify_url = Interface_URL + "do/ccmtvappandroid/ccmtvappalipay.php";
    //支付宝回调地址 充值余额所用
    public static String alipayNotify_url_new = Interface_URL + "do/ccmtvappandroid/ccmtvappalipay_new.php";

    // public static String APP_DOWNLOAD = "http://dwz.cn/3hPzV3";
    //APK 下载地址
    public static String DOWNLOAD_APK = "http://www.ccmtv.cn/do/ccmtvappandroid/ccmtvapp.apk";
    public static String phone = "021-51082567-8674";
    public static String qq = "516614873";
    public static String app = "wushiwei";
    //新版视频回复列表
    public static String newVideoContent = "newVideoContent";
    //评论删除 新版
    public static String delNewVc = "delNewVc";
    //评论回复 评论 新版
    public static String newReplyComment = "newReplyComment";
    //回复 评论 加载全部
    public static String getAllReplyData = "getAllReplyData";
    // AR列表
    public static String doARData = "doARData";
    // 关注--用户搜索
    public static String searchUser = "searchUser";
    // 关注--推荐用户
    public static String recommendAttention = "recommendAttention";
    // 关注--一键关注
    public static String followAllUser = "followAllUser";
    // 收银台--消费记录
    public static String cashierConsume = "cashierConsume";
    // 收银台--消费记录2
    public static String cashierBill = "cashierbill";
    // 收银台--收入记录
    public static String cashierEarning = "cashierEarning";
    // 收银台--提现记录
    public static String cashierCashquery = "cashierCashquery";
    // 收银台--提现申请
    public static String cashierWithdraw = "cashierWithdraw";
    // 收银台--充值
    public static String cashierRecharge = "cashierRecharge";
    // 收银台
    public static String getMyCashier = "getMyCashier";
    // 余额支付
    public static String cashierPayMoney = "cashierPayMoney";
    // 医学会议
    public static String medicalConference = "medicalConference";
    // 执考列表
    public static String examDocQualification = "examDocQualification";
    // 执考列表 分页
    public static String examDocQualificationPage = "examDocQualificationPage";
    // 执考详情 分页
    public static String examDocQualificationDetails = "examDocQualificationDetails";
    // 兑换积分
    public static String exchangePoints = "exchangePoints";
    // 医学直播间
    public static String directSeeding = "directSeeding";
    // 医学直播间 分页
    public static String directSeedingMore = "directSeedingMore";
    // 扫描登陆 确认登录
    public static String confirm_login = "confirm_login";
    // 扫描登陆
    public static String scan_valid = "scan_valid";
    // 快捷登录
    public static String app_sendsms = "app_sendsms";
    // 快捷登录
    public static String app_sendsms_valid = "app_sendsms_valid";
    //柳叶杯
    public static String lybIndex = "lybIndex";
    //杏林杯
    public static String xlbIndex = "xlbIndex";
    //投票
    public static String lybVote = "lybVote";
    //投票
    public static String xlbVote = "xlbVote";
    //用户上传记录
    public static String getUserUploadInfo = "getUserUploadInfo";
    //Vip专区
    public static String vipArea = "vipArea";
    //Vip专区_换一换
    public static String vipChangeVideo = "vipChangeVideo";
    //旧-订阅科室
    public static String subscribeKs = "subscribeKs";
    //新-订阅科室
    public static String subscribeKsnew = "subscribeKsnew";
    //订阅数据
    public static String getSubscribeData = "getSubscribeData";
    //订阅操作（新增、修改、顺序）
    public static String doSubscribeKs = "doSubscribeKs";
    //圈子-个人
    public static String myCircleInfo = "myCircleInfo";
    //圈子-我上传的视频
    public static String getUpVideo = "getUpVideo";
    //医考-试卷列表
    public static String getPaperList = "getPaperList";
    //医考-验证用户权限
    public static String checkHosUser = "checkHosUser";
    //医考-考试须知
    public static String getPaperDetail = "getPaperDetail";
    //医考-考试须知  20180502
    public static String getPaperDetails = "getPaperDetails";
    //医考-考试须知 新20180821
    public static String getExamDetails = "getExamDetails";
    //医考-模拟考试 开始考试考题获取
    public static String simulationPaperStart = "simulationPaperStart";
    //医考-开始考试考题获取
    public static String doPaperStart = "doPaperStart";
    public static String doPaperStarts = "doPaperStarts";
    //医考-交卷
    public static String handPaper = "handPaper";
    //医考-模拟考试-交卷
    public static String simulationPaperSubmit = "simulationPaperSubmit";
    //医考-模拟考试-查看答卷 获取题目
    public static String getSimulationPaperCardDetail = "getSimulationPaperCardDetail";
    //医考-模拟考试-查看答卷 获取题号
    public static String getSimulationPaperCard = "getSimulationPaperCard";
    //医考-查看答题卡明细
    public static String getPaperCardDetail = "getPaperCardDetail";
    public static String getPaperCardDetails = "getPaperCardDetails";
    //医考-查看答题卡总览
    public static String getPaperCard = "getPaperCard";
    //上传-收费金额设置
    public static String upmoney = "upmoney";
    //上传-上传完成数据
    public static String myUpload = "myUpload";
    //上传-上传完成单个病例详情数据
    public static String vmDetail = "vmdetail";
    //医考-公告列表
    public static String getNoticeList = "getNoticeList";
    //医考-公告详细
    public static String getNoticeInfo = "getNoticeInfo";//老版本，没有uid 3.7.8
    public static String getNoticeInfoNew = "getNoticeInfoNew";//新版本，有uid 3.7.9
    //医考-本院视频列表
    public static String hosVideoList = "hosVideoList";
    //医考-测试文件上传
    public static String taskFileUpload = "taskFileUpload";
    //医考-任务测试入口 ppt
    public static String getPptTestInfo = "getPptTestInfo";
    //医考-任务测试入口 视频
    public static String getVideoTest = "getVideoTest";
    //医考-任务测试入口 ppt 开始考试
    public static String getPptTestExam = "getPptTestExam";
    //医考-任务测试入口 视频 开始考试
    public static String getTaskExam = "getTaskExam";
    //医考-任务测试入口 ppt 开始考试 交卷
    public static String postPptPunAnswer = "postPptPunAnswer";
    //医考-任务测试入口 视频 开始考试 交卷
    public static String postAnswer = "postAnswer";
    //医考-正式考试-用户作答-单题提交服务器
    public static String disasterSave = "disasterSave";
    //任务-进行中列表
//    public static String taskNoList = "taskNoList";
    public static String getYesStartTask = "getYesStartTask";
    //任务-未开始列表
//    public static String taskYesList = "taskYesList";
    public static String getNoStartTask = "getNoStartTask";
    //任务-已结束列表
//    public static String taskOverdueList = "taskOverdueList";
    public static String getListEndTask = "getListEndTask";
    //任务详情-任务内容列表
    public static String getTaskInfo = "getTaskInfo";
    //任务详情-PPT详情
    public static String getTaskPptInfo = "getTaskPptInfo";
    //任务详情-音频详情
    public static String getAudioInfo = "getAudioInfo";
    //任务详情-图文删除
    public static String userDelTaskFile = "userDelTaskFile";
    //任务详情-视频签到
    public static String taskVideoSign = "taskVideoSign";
    //视频签到-视频播放权限
    public static String getVideoPower = "getVideoPower";
    //视频签到-视频签到点
    public static String getSignNum = "getSignNum";
    //我的-红点
    public static String hosSelectLook = "hosSelectLook";
    //订阅-科室下的疾病
    public static String getJbKs = "getJbKs";
    //提交订阅
    public static String doSubscribeKsnew = "doSubscribeKsnew";
    //订阅
    public static String subscribeCase = "subscribeCase";
    //获取验证码
    public static String codecheck = "codecheck";
    //注册完成
    public static String passcheck = "passcheck";
    //注册后的完善资料
    public static String perfectInfo = "perfectInfo";
    //积分商城
    public static String jifenIndex = "jifenIndex";
    //积分商城-更多
    public static String morelist = "morelist";
    //商品详情
    public static String proDetail = "proDetail";
    //兑换礼品
    public static String doOrder = "doOrder";
    //兑换记录
    public static String exchange = "exchange";
    //兑换记录-订单详情
    public static String exchangeDetail = "exchangeDetail";
    //医院培训-首页四个视频
    public static String trainVideoIndex = "trainVideoIndex";
    //医院培训-首页四个入口
    public static String yyindex = "yyindex";
    //医院培训-验证权限
    public static String checkYyUser = "checkYyUser";
    //医院培训-首页政策法规
    public static String trainArticleIndex = "trainArticleIndex";
    //医院培训-视频列表
    public static String trainVideoList = "trainVideoList";
    //医院培训-视频列表-条件
    public static String trainVideoListCate = "trainVideoListCate";
    //医院培训-学习包
    public static String trainStudy = "trainStudy";
    //医院培训-政策法规列表
    public static String trainArticleList = "trainArticleList";
    //医院培训-政策法规收藏列表
    public static String collectionList = "collectionList";
    //医院培训-文章详细
    public static String trainArticleInfo = "trainArticleInfo";
    //医院培训-文章-收藏
    public static String collection = "collection";
    //医院培训-文章-点赞
    public static String detail = "detail";
    //医院培训-题目类型
    public static String questionCate = "questionCate";
    //医院培训-题目
    public static String testQuestion = "testQuestion";
    //医院培训-题目报错
    public static String reportError = "reportError";
    //医院培训-题目收藏
    public static String exerciseCollection = "exerciseCollection";
    //医院培训-科室选择（max）
    public static String cateSelData = "cateSelData";
    //医院培训-科室选择（min）
    public static String parentData = "parentData";
    //医院培训-收藏列表
    public static String collectList = "collectList";
    //医院培训-收藏列表 新 20180524
    public static String collectListNew = "collectListNew";
    //医院培训-收藏练习
    public static String collectQuestion = "collectQuestion";
    //医院培训-历史作答
    public static String history = "history";
    //医院培训-历史作答
    public static String historyRecord = "historyRecord";
    //节日活动
    public static String activityimg = "activityimg";
    //签署协议
    public static String subscribe = "subscribe";
    //是否签署协议
    public static String isSubscribe = "isSubscribe";
    //药讯动态
    public static String recommend = "recommend";
    //专题指南
    public static String subject = "subject";
    //相关文献
    public static String document = "document";
    //药讯助手
    public static String helper = "helper";
    //药讯动态更多
    public static String trends = "trends";
    //个人中心
    public static String memberIndex = "memberIndex";
    //关注更多
    public static String focusMany = "focusMany";
    //文章详细
    public static String trendsInfo = "trendsInfo";
    //文章评论详细页
    public static String infoComment = "infoComment";
    //收藏/取消收藏
    public static String articleCollect = "articleCollect";
    //空中拜访 点赞/取消点赞
    public static String articleLaud = "articleLaud";
    //点赞成功后延迟请求的接口（用于模拟假数据的点赞数）
    public static String updateLookLaudNumber = "updateLookLaudNumber";
    //文章详情-投票提交
    public static String voteUserAnswer = "voteUserAnswer";
    //文章详情-调研提交
    public static String surveyUserAnswer = "surveyUserAnswer";
    //查看更多留言
    public static String surveyUserList = "surveyUserList";
    //空中拜访-消息中心
    public static String userChatList = "userChatList";
    //评论点赞/取消点赞
    public static String laudComment = "laudComment";
    //药讯助手-关注
    public static String focus = "focus";
    //药讯助手-取消关注
    public static String cancelFocus = "cancelFocus";
    //收件箱
    public static String inbox = "inbox";
    //发件箱
    public static String alreadyPost = "alreadyPost";
    //发送消息
    public static String postMessage = "postMessage";
    //消息详情
    public static String messageInfo = "messageInfo";
    //删除消息
    public static String messageDel = "messageDel";
    //写消息下拉列表
    public static String focusList = "focusList";
    //我的关注列表
    public static String userFocusList = "userFocusList";
    //发布评论
    public static String postComment = "postComment";
    //文章分享
    public static String share = "share";
    //分享成功后增加积分
    public static String shareSuccess = "shareSuccess";
    //文章收藏列表
    public static String articleCollectList = "articleCollectList";
    //搜索
    public static String search = "search";
    //用户在线统计
    public static String onlineRecord = "onlineRecord";
    //用户统计接收
    public static String userMonitor = "userMonitor";
    //下载文件
    public static String selUserIntegral = "selUserIntegral";
    //记录播放视频
    public static String watchVideoRecord = "watchVideoRecord";
    //直播首页
    public static String liveIndex = "liveIndex";
    //预约直播提醒
    public static String reservation = "reservation";
    //直播搜索
    public static String selmeet = "selmeet";
    //积分攻略
    public static String word = "word";
    //书架列表
    public static String getBooksList = "getBooksList";
    //电子书详情页
    public static String getDzsDetial = "getDzsDetial";
    //实体书详情页
    public static String BooksDetails = "BooksDetails";
    //书架 我的订单
    public static String myBooksOrder = "myBooksOrder";
    //书架 我的订单详情
    public static String booksOrderDetails = "booksOrderDetails";
    //通过序列号获取电子书
    public static String codeGetDzs = "codeGetDzs";
    //电子书访问记录
    public static String recordDzsRead = "recordDzsRead";
    //电子书购买
    public static String payDzs = "payDzs";
    //科室列表
    public static String pdkeshi = "pdkeshi";
    //正式考试防作弊 - 解锁
    public static String checkCode = "checkCode";
    //正式考试防作弊 - 获取锁的状态
    public static String checkCheat = "checkCheat";
    //频道首页
    public static String ksIndex = "ksIndex";
    //频道首页-视频获取
    public static String diseaseVideo = "diseaseVideo";
    //试题练习-首页
    public static String cateSelected = "cateSelected";
    //会议科室与时间
    public static String classification = "classification";
    //会议首页数据
    public static String specialIndex = "specialIndex";
    //会议详情数据
    public static String specialDetail = "specialDetail";
    //会议议题选择
    public static String secSpecial = "secSpecial";
    //会议专题更多视频
    public static String moreVideo = "moreVideo";
    //绑定改绑手机获取验证码
    public static String updateTel = "updateTel";
    //绑定改绑手机验证-验证码
    public static String telCheck = "telCheck";
    //播放视频前判断是否需要完善资料
    public static String isPerfectdata = "isPerfectdata";
    //学习记录学习汇总
    public static String studySummary = "studySummary";
    //学习记录数据
    public static String dataRecord = "dataRecord";
    //学习记录我的考核
    public static String myExamine = "myExamine";
    //新搜索接口
    public static String secVideoSearch = "secVideoSearch";
    //新搜索条件接口
    public static String appNewSearch = "appNewSearch";
    //VIP 首页接口 new 20180601
    public static String vipIndex = "vipIndex";
    //VIP 首页 科室数据 new 20180601
    public static String vipVideo = "vipVideo";
    //VIP 首页 形式内容板块 更多视频
    public static String moreVideos = "moreVideos";
    //VIP 特权接口
    public static String privilege = "privilege";
    //VIP 所有小科室数据
    public static String selKeshi = "selKeshi";
    //VIP 提交选中的小科室数据
    public static String doKeshi = "doKeshi";
    //练习 条件选择
    public static String questionsCate = "questionsCate";
    //练习 获取题目 new 20180607
    public static String getQuestion = "getQuestion";
    //练习 收藏 new 20180607
    public static String quesCollect = "quesCollect";
    //练习 答题卡 new 20180607
    public static String answerSheet = "answerSheet";
    //新本院资源子级目录
    public static String resourceChild = "resourceChild";
    //新本院资源搜索数据
    public static String searchData = "searchData";
    //新本院资源获取数据
    public static String getResource = "getResource";
    //新本院资源--搜索条件数据
    public static String getSearchInfo = "getSearchInfo";
    //新本院资源获取数据 20190423
    public static String getResourceBySear = "getResourceBySear";
    //练习 答题 再来一轮
    public static String reStart = "reStart";
    //VIP 特权生日礼包领取反馈接口
    public static String receiveGift = "receiveGift";
    //云管家 获取身份
    public static String gpRole = "gpRole";
    //云管家 获取功能
    public static String gpRoleList = "gpRoleList";
    //云管家 考核管理界面考核列表接口
    public static String dailyExamList = "dailyExamList";
    //云管家 入科最外层列表
    public static String rkList = "rkList";
    //云管家 入科 预览
    public static String rkNextMonth = "rkNextMonth";
    //云管家 入科 入科-本月[或者历史]查看列表
    public static String rkInfoList = "rkInfoList";
    //云管家 入科 入科-老师列表
    public static String rkTeacherList = "rkTeacherList";
    //云管家 入科 入科-用户数据编辑
    public static String rkUserInfo = "rkUserInfo";
    //云管家 入科 入科-操作数据入库
    public static String rkEdit = "rkEdit";
    //云管家 带教学员列表 月份
    public static String studentList = "studentList";
    //云管家 带教学员详细
    public static String studentInfo = "studentInfo";
    //云管家 轮转计划列表
    public static String userCyclePlan = "userCyclePlan";
    //云管家 审核
    public static String workFlowList = "workFlowList";
    //云管家 考核 日常考核学生列表
    public static String dailyStaList = "dailyStaList";
    //云管家 考核 是否发送出科考核通知
    public static String leaveKsNotice = "leaveKsNotice";
    //云管家 考核 发送出科考核通知
    public static String sendLeaveNotice = "sendLeaveNotice";
    //云管家 考核 日常考核详细信息
    public static String dailyStaInfo = "dailyStaInfo";
    //云管家 考核 日常考核学生成绩信息
    public static String dailyStaScoreInfo = "dailyStaScoreInfo";
    //云管家 考核 日常考核提交详细信息
    public static String dailyStaAdd = "dailyStaAdd";
    //云管家 考核 出科考核住培生查看出科考核地点+考核成绩结果信息
    public static String userlistDetail = "userlistDetail";
    //云管家 考核 出科考核学生列表
    public static String leaveKsUsersList = "leaveKsUsersList";
    //云管家 考核 出科考核科目
    public static String leaveKsUserItems = "leaveKsUserItems";
    //云管家 考核 日常考核提交审核
    public static String dailyManSubmit = "dailyManSubmit";
    //云管家 考核 出科考核详细信息
    public static String getItemForms = "getItemForms";
    //云管家 考核 出科考核详细信息 表单详情
    public static String getItemFormDetail = "getItemFormDetail";
    //云管家 月度评价总接口（包括 学员/科室主任、科室教秘/基地主任、基地教秘 不同身份登录）
    public static String allAppraiseIndex = "allAppraiseIndex";
    //云管家 学员评价带教列表【学员身份】
    public static String allAppraiseUserList = "allAppraiseUserList";
    //云管家 学员评价带教详情页面【学员身份】
    public static String userAppraiseDetail = "userAppraiseDetail";
    //云管家 学员给带教老师评分页面 ---- 提交信息接口【学员身份】
    public static String addDjscore = "addDjscore";
    //云管家 年度评优一：参数接口（所有科室、所有状态）
    public static String yearGoodTeacherParam = "yearGoodTeacherParam";
    //云管家 年度评优二：列表接口
    public static String yearGoodTeachersList = "yearGoodTeachersList";
    //云管家 年度评优三：投票接口
    public static String yearGoodTeacherAddVote = "yearGoodTeacherAddVote";
    //云管家 年度评优四：取消投票
    public static String yearGoodTeacherDelVote = "yearGoodTeacherDelVote";
    //联系我们
    public static String myClient = "myClient";
    //云管家 审核 日常考核审核学员列表
    public static String dailyManWork = "dailyManWork";
    //云管家 审核 提交日常考核审核
    public static String dailyManWorkOperate = "dailyManWorkOperate";
    //ccmtv 上传 上传病例图片新接口
    public static String uploadCasePicture = "uploadCasePicture";
    //ccmtv 上传 上传病例删除图片接口
    public static String delCasePicture = "delCasePicture";
    //ccmtv 上传 上传病例所有数据
    public static String uploadAllCase = "uploadAllCase";
    //ccmtv 上传 新查看病例详情
    public static String casedetail = "casedetail";
    //ccmtv 上传 新查看所有已上传数据
    public static String myUploadCase = "myUploadCase";
    //云管家 考核 出科考核成绩录入接口（缺考+不考核）
    public static String leaveKsEnterSatus = "leaveKsEnterSatus";
    //云管家 考核 出科考核成绩整体提交接口
    public static String leaveKsBatchSub = "leaveKsBatchSub";
    //云管家 考核 出科考核录入成绩接口一：正常
    public static String leaveKsNewEnterGrade = "leaveKsNewEnterGrade";
    //云管家 考核 出科考核获取日常考核详情信息
    public static String getDailyScoreFromLeaveKs = "getDailyScoreFromLeaveKs";
    //云管家 考核 出科考核上传图片
    public static String uploadImg = "uploadImg";
    //云管家 考核 出科考核删除图片
    public static String deleteImg = "deleteImg";
    //云管家 审核 出科考核审核学员列表
    public static String leaveKsCheckUserList = "leaveKsCheckUserList";
    //云管家 审核 提交出科考核审核
    public static String leaveKsCheck = "leaveKsCheck";
    //ccmtv 上传 获取上传规则
    public static String getUploadRules = "getUploadRules";
    //云管家 教学活动列表
    public static String getDataList = "getDataList";
    //云管家 教学活动列表 全院
    public static String getActivities = "getActivities";
    //云管家 获取教学活动类型
    public static String getTypes = "getTypes";
    //云管家 获取可选择的学员
    public static String getUsersNew = "getUsersNew";
    //云管家 文件上传接口
    public static String activitiesUpload = "activitiesUpload";
    //云管家 发布 教学活动
    public static String activitiesAdd = "activitiesAdd";
    //云管家 查看教学活动详情
    public static String activitiesSelct = "activitiesSelct";
    //云管家 获取教学活动的学员
    public static String getActivitiesUsers = "getActivitiesUsers";
    //云管家 删除 已上传的文件
    public static String deleteUpload = "deleteUpload";
    //ccmtv 首页 空中拜访是否显示小图标
    public static String indexIsShowRed = "indexIsShowRed";
    //ccmtv 空中拜访 药讯助手栏目是否显示小图标
    public static String columnIsShowRed = "columnIsShowRed";
    //ccmtv 空中拜访 记录用户点击板块进来时间
    public static String recordUserTime = "recordUserTime";
    //ccmtv 空中拜访 记录用户点推荐助手个人中心
    public static String recordUserHelper = "recordUserHelper";
    //云管家 阶段性考核 阶段性考核列表
    public static String stageExaminerIndex = "stageExaminerIndex";
    //云管家 阶段性考核 阶段性考核考站列表
    public static String stageExaminerList = "stageExaminerList";
    //云管家 阶段性考核 阶段性考核学员列表
    public static String stageExaminerUserList = "stageExaminerUserList";
    //云管家 阶段性考核 阶段性考核表单列表
    public static String stageExaminerDetailInfo = "stageExaminerDetailInfo";
    //云管家 阶段性考核 阶段性考核表单详情
    public static String stageExaminerDetailForm = "stageExaminerDetailForm";
    //云管家 阶段性考核 阶段性考核上传图片
    public static String stageExaminerImgup = "stageExaminerImgup";
    //云管家 阶段性考核 阶段性考核删除图片
    public static String stageExaminerImgdel = "stageExaminerImgdel";
    //云管家 阶段性考核 阶段性考核提交表单详情
    public static String stageExaminerUpDetail = "stageExaminerUpDetail";
    //云管家 阶段性考核 阶段性考核学员首页
    public static String stageUserIndex = "stageUserIndex";
    //云管家 阶段性考核 阶段性考核学员考站列表
    public static String stageUserExamInfo = "stageUserExamInfo";
    //云管家 阶段性考核 阶段性考核学员成绩列表
    public static String stageUserScoreList = "stageUserScoreList";
    //云管家 阶段性考核 阶段性考核学员查看考核考官列表
    public static String stageUserExaminerList = "stageUserExaminerList";
    //云管家 阶段性考核 阶段性考核学员根据考官查看详情
    public static String stageUserScoreInfo = "stageUserScoreInfo";
    //云管家 编辑 教学活动
    public static String updateData = "updateData";
    //云管家 扫描签到
    public static String activitiesSign = "activitiesSign";
    //云管家 提交教学活动
    public static String submitData = "submitData";
    //云管家 获取二维码
    public static String activitiesCode = "activitiesCode";
    //云管家 删除教学活动
    public static String deleteData = "deleteData";
    //云管家 审核列表下拉选项
    public static String workFlowOptionList = "workFlowOptionList";
    //云管家 教学活动 审核 通过 or 退回
    public static String activitiesWork = "activitiesWork";
    //【系统消息】1.消息列表-系统消息
    public static String commonInfoList = "commonInfoList";
    //【公告通知】1.消息列表-公告通知
    public static String noticeInfoList = "noticeInfoList";
    //【公共消息接口】3.清空 某一分类下所有个人消息
    public static String clearMyInfoByCat = "clearMyInfoByCat";
    //【公共消息接口】1.所有消息头部是否显示未读的标识（头部三大分类是否显示红点）
    public static String allInfoStatusList = "allInfoStatusList";
    //【公告通知】2.公告详情
    public static String noticeInfoDetail = "noticeInfoDetail";
    //【系统消息】2.通用-系统详情（除‘赞 ’外，赞单独走另外接口）
    public static String systemInfoDetail = "systemInfoDetail";
    //【系统消息】3.关注某人/取消关注
    public static String attentionSomeone = "attentionSomeone";
    //【系统消息】4.是否关注
    public static String isAttentionSomeone = "isAttentionSomeone";
    //【互动】3.私信好友列表
    public static String myLetterFriends = "myLetterFriends";
    //【互动】4.发送私信
    public static String sendMyLetter = "sendMyLetter";
    //【互动】2.我接收到的私信列表
    public static String giveMyLetters = "giveMyLetters";
    //【互动】6.两个用户之间所有的私信列表
    public static String CompletePrivateLetter = "CompletePrivateLetter";
    //【系统消息】5.赞的列表
    public static String giveMyZansAndComment = "giveMyZansAndComment";
    //【公共消息接口】4.所有未读消息的数量
    public static String allNoReadCount = "allNoReadCount";
    //更新用户手机设备信息  推送服务需要
    public static String updateUserVersion = "updateUserVersion";
    //完善信息  是否需要验证身份证
    public static String revisePerfection = "revisePerfection";
    // 【勋章】  获取勋章列表
    public static String getMyMedal = "getMyMedal";
    // 【勋章】  查看勋章详情
    public static String seeMedal = "seeMedal";
    // 【勋章】  在刚进入App时提醒后台查看勋章详情
    public static String obtainMedal = "obtainMedal";
    // 【成长值】  查看用户的成长值信息
    public static String getUserRankInfo = "getUserRankInfo";
    //资料库  获取 首页的科室 类型 年份
    public static String getArticle = "getArticle";
    //资料库  获取医学资料数据
    public static String articelData = "articelData";
    //资料库  医学资料库详情
    public static String articelInfo = "articelInfo";
    //资料库  点赞
    public static String addZan = "addZan";
    //资料库  医学资料库 收藏 和取消收藏
    public static String addCollection = "addCollection";
    //考勤 考勤首页
    public static String getUserSignInfo = "getUserSignInfo";
    //考勤 获取服务器时间戳
    public static String getTimestamp = "getTimestamp";
    //考勤 加密
    public static String key = "7304235C9BD66D626AA8828AD3D2245C";
    //考勤 签到/签退
    public static String userSign = "userSign";
    //考勤 二维码签到
    public static String userSignQrCode = "userSignQrCode";
    //考勤 老师展示二维码
    public static String getManageQrCode = "getManageQrCode";
    //考勤 二维码签到记录
    public static String getQrCodeLogList = "getQrCodeLogList";
    //考勤 地理位置签到记录
    public static String userSignLogList = "userSignLogList";
    //勋章 分享页面
    public static String shareMadal = "shareMadal";
    //评论  添加新评论
    public static String addRecomment = "addRecomment";
    //评论  评论列表
    public static String getRecommentList = "getRecommentList";
    //评论  删除自己的评论（同时有子评论也一并删除）
    public static String delFatherAndChilds = "delFatherAndChilds";
    //评论  主评论+所有其子评论（?子评论是否需要分页）
    public static String getFatherAndChilds = "getFatherAndChilds";
    //文章评论  获取基地列表
    public static String getBase = "getBase";
    public static String getScreenData = "getScreenData";
    //大病历最外层数据列表
    public static String getBigCaseYearMonthList = "getBigCaseYearMonthList";
    //大病历 内层用户已上传列表
    public static String getBigCaseUserUploadList = "getBigCaseUserUploadList";
    //大病历 上传图片
    public static String userUploadImg = "userUploadImg";
    //用户已上传列表查看
    public static String getBigCaseUserUploadInfo = "getBigCaseUserUploadInfo";
    //大病历 删除图片
    public static String userDelImg = "userDelImg";
    //大病例添加
    public static String userAddBigCase = "userAddBigCase";
    //大病例编辑提交
    public static String userEditBigCase = "userEditBigCase";
    //大病历  人员列表接口
    public static String getBigCaseUserList = "getBigCaseUserList";
    //入科教育 根据用户长uid，获取其轮转科室
    public static String myCycleKs = "myCycleKs";
    //入科教育 根据本院科室id获取当前正在使用的入科教育模板
    public static String getHosksRkedu = "getHosksRkedu";
    //入科教育 反馈确认
    public static String isConfirmRefund = "isConfirmRefund";
    //意见反馈 首页
    public static String commonQuestionList = "commonQuestionList";
    //意见反馈 常见问题详情
    public static String commonQuestionDetail = "commonQuestionDetail";
    //意见反馈 我的反馈列表
    public static String myRefundList = "myRefundList";
    //意见反馈 我的反馈详情
    public static String myRefundDetail = "myRefundDetail";
    //考勤 请假类型
    public static String getLeaveType = "getLeaveType";
    //考勤 请假
    public static String addAskLeave = "addAskLeave";
    //约课  约课列数数据
    public static String ykList = "ykList";
    //院级活动 列表
    public static String activitysList = "activitysList";
    //院级活动 详情
    public static String activityInfo = "activityInfo";
    //院级活动 发布-获取分类
    public static String activitysCateList = "activitysCateList";
    //院级活动 签到签退
    public static String activitysUserSign = "activitysUserSign";
    //考勤 请假列表
    public static String askLeaveList = "askLeaveList";
    //师资管理 教秘端 带教列表
    public static String djListApi = "djListApi";
    //师资管理 教秘端 带教列表 查询审核状态
    public static String checkStatusApi = "checkStatusApi";
    //师资管理 教秘端 提交审核
    public static String tjcheckApi = "tjcheckApi";
    //师资管理 审核人员列表
    public static String getfacultyListApi = "getfacultyListApi";
    //师资管理 审核 通过 不通过
    public static String facultycheckApi = "facultycheckApi";
    //师资管理 添加带教的列表
    public static String manageListApi = "manageListApi";
    //师资管理 添加带教的列表
    public static String addManageApi = "addManageApi";
    //教学活动 提交请假接口
    public static String activitiesAddLeave = "activitiesAddLeave";
    //教学活动 发布人 操作 学员的请假
    public static String activitiesSubmitLeave = "activitiesSubmitLeave";
    //教学活动 查看课后评价内容
    public static String activitiesTempInfo = "activitiesTempInfo";
    //教学活动 添加课后评价模板
    public static String activitiesAddTemp = "activitiesAddTemp";
    //约课  约课详细
    public static String ykInfo = "ykInfo";
    //约课  约课报名
    public static String ykSignUp = "ykSignUp";
    //约课  约课调研表单
    public static String ykSurvey = "ykSurvey";
    //约课  约课调研报告提交
    public static String ykSurveyScore = "ykSurveyScore";
    //约课  约课签到
    public static String ykSign = "ykSign";
    //入科分配科室列表(月份)
    public static String getAssginKsIndex = "getAssginKsIndex";
    //入科分配科室每月人员列表
    public static String getAssginKsUsers = "getAssginKsUsers";
    //入科分配科室编辑查看
    public static String getAssginKsEdit = "getAssginKsEdit";
    //入科分配科室提交操作
    public static String getAssginKsSubmit = "getAssginKsSubmit";
    //用户成长等级 -- 观看视频5分钟，获取经验值
    public static String videoGetjyz = "videoGetjyz";
    //360评价  带教老师评估住院医师一：index
    public static String manageTeacherAppraiseIndex = "manageTeacherAppraiseIndex";
    //360评价  带教老师评估住院医师二：UserList
    public static String manageTeacherAppraiseUserList = "manageTeacherAppraiseUserList";
    //360评价  带教老师评估住院医师三：ShowDetail
    public static String manageTeacherAppraiseShowDetail = "manageTeacherAppraiseShowDetail";
    //360评价  带教老师评估住院医师4：提交
    public static String manageTeacherAppraiseUpDetail = "manageTeacherAppraiseUpDetail";
    //360评价  基地评价一
    public static String getUserBaseList = "getUserBaseList";
    //360评价  基地详情
    public static String getUserBaseDetail = "getUserBaseDetail";
    //360评价  基地详情提交
    public static String getUserBaseUpDetail = "getUserBaseUpDetail";
    //出科 学生列表  搜索条件 【专业/基地 + 规培年限】
    public static String getCkSearchInfo = "getCkSearchInfo";
    //出科 选择多套模板
    public static String getModelList = Interface_URL1 + "admin.php/AndroidGpApi/index";
    //教学活动 发布  李惠利医院 专属 选择带教老师
    public static String getTeachers = "getTeachers";
    //我的同事关注的人员列表
    public static String getWorkmateAttention = "workmateAttention";
    //直播 直播按钮权限
    public static String getLiveInfo = "getLiveInfo";
    //教学活动发布选择讲师列表
    public static String getSpeaker = "getSpeaker";
    public static String getSpeakerNew = "getSpeakerNew";
    //院级活动 发布-提交数据
    public static String activitysAdd = "activitysAdd";
    //院级活动 活动详细签到情况
    public static String activitysUserSignList = "activitysUserSignList";
    //院级活动 活动详细
    public static String activitysInfo = "activitysInfo";
    //院级活动 发布-添加用户搜索条件
    public static String activitysUserListWhere = "activitysUserListWhere";
    //院级活动 发布-选择用户
    public static String activitysUserList = "activitysUserList";
    //院级活动 发布-文件上传
    public static String activitysUploadFile = "activitysUploadFile";
    //院级活动 签到签退二维码
    public static String activitysSignQrCode = "activitysSignQrCode";
    //全员教学活动 活动详情
    public static String getActivitiesDetail = "getActivitiesDetail";
    //vip首页 换一换
    public static String getanotherVideos = "anotherVideos";
    //基地出科学员列表
    public static String getleaveKsBaseUsersList = "leaveKsBaseUsersList";
    //评价科室列表
    public static String getUserKsList = "getUserKsList";
    //评价科室打分列表
    public static String getUserScoreForm = "getUserScoreForm";
    //评价科室提交打分
    public static String getksScoreFormPost = "ksScoreFormPost";
    //单站考核和多站考核首页列表
    public static String getStageExaminerIndex = "stageExaminerIndex";
    //单站考核详情页
    public static String getStageExaminerList = "stageExaminerList";
    //单多站考核学生列表页面
    public static String getStageExaminerUserList = "stageExaminerUserList";
    //单多站考核学生端首页列表
    public static String getStageUserIndex = "stageUserIndex";
    //单多站学生端详情数据
    public static String getStageUserExamInfo = "stageUserExamInfo";
    //学生成绩单
    public static String getStageUserScoreList = "stageUserScoreList";
    //督导管理 督查任务列表
    public static String getSupDutyList = "getSupDutyList";
    //督导管理 科室评估
    public static String getKsEvalue = "getKsEvalue";
    //督导管理 科室评估 评估表单
    public static String getSupKsForm = "getSupKsForm";
    //督导管理 科室评估 评估表单
    public static String addSupFormData = "addSupFormData";
    //导师管理 学员列表
    public static String TutorGetUserList = "TutorGetUserList";
    //导师管理 学员详细
    public static String TutorGetUserInfo = "TutorGetUserInfo";
    //学习任务 记录签到亮起记录
    public static String signLight = "signLight";
    //延时出科 申请列表
    public static String getPostponeList = "getPostponeList";
    //延时出科 提交申请
    public static String postponeApply = "postponeApply";
    //正式考试 人脸识别  图片上传
    public static String examFaceRecord = "examFaceRecord";
    //实体书 购买 填写地址
    public static String createBookOrder = "createBookOrder";

    // 活动详情 上传视频
    public static String activitiesUploadVideo = "activitiesUploadVideo";
}
