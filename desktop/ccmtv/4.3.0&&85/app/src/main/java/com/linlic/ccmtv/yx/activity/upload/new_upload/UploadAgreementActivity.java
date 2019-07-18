package com.linlic.ccmtv.yx.activity.upload.new_upload;

import android.os.Bundle;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UploadAgreementActivity extends BaseActivity {

    @Bind(R.id.tv_agreement)
    TextView tvAgreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_agreement);
        ButterKnife.bind(this);
        tvAgreement.setText("CCMTV用户内容上传服务协议\n" +
                "本协议被视为《CCMTV用户协议》（链接地址：http://www.ccmtv.cn/ccmtvtp/Loginreg/Reg/index.html，若链接地址变更的，则以变更后的链接地址所对应的内容为准）的附件，如本协议与《CCMTV用户协议》存在冲突的，以本协议为准。\n" +
                "如您需使用CCMTV内容上传功能，您应当仔细阅读并遵守《CCMTV用户内容上传服务协议》（以下简称“本协议”），请您务必审慎阅读、充分理解各条款内容。\n" +
                "一、关于CCMTV平台用户内容上传服务\n" +
                "您使用的CCMTV内容上传服务是CCMTV第三方平台为用户提供由用户自行上传其享有著作权或获得授权的内容服务模块（包括但不限于视频和文档），CCMTV作为平台方仅负责为用户提供第三方网络存储空间服务。\n" +
                "二、服务协议与权利说明\n" +
                "1.您在使用CCMTV内容上传服务的同时应同时遵守《CCMTV用户协议》，承诺将最大限度共同维护CCMTV平台内容服务模块安全健康的网络环境。\n" +
                "2.您知晓并同意，对您所上传的内容拥有完整著作权或已获得合法授权，未侵犯任何第三方之合法权益，能够合法发布并公开于CCMTV平台。经CCMTV发现侵犯任何第三方合法权益内容并核实的，您同意，CCMTV有权直接做删除处理并有权对您提出相应警告，多次发现的，CCMTV有权直接停用您账号。\n" +
                "3.您需对使用CCMTV内容上传服务的行为负责，对任何通过CCMTV平台发布、公开的内容负责，及对其上传内容所产生的任何后果承担全部责任。\n" +
                "4.您上传内容时应自觉遵守法律法规、社会主义制度、国家利益、公民合法权益、社会公共秩序、道德风尚和信息真实性等要求，否则公司将立即采取相应处理措施。用户不得发表下列信息：\n" +
                "（1）违反国家法律、危害国家安全统一、社会稳定、公序良俗、社会公德以及侮辱、诽谤、淫秽或含有任何性或性暗示的、暴力的内容；\n" +
                "（2）侵害他人名誉权、肖像权、知识产权、商业秘密等合法权利的内容；\n" +
                "（3）涉及他人隐私、个人信息或资料的；\n" +
                "（4）骚扰、广告信息及垃圾信息；\n" +
                "（5）其他违反法律法规、政策及公序良俗、社会公德或干扰内容正常运营和侵犯其他用户或第三方合法权益内容的信息。\n" +
                "5.您知晓并同意，您发布于CCMTV平台并由您享有著作权的原创内容，CCMTV享有永久使用权；\n" +
                "6.您同意不会使用任何第三方软件工具，对CCMTV平台内容服务模块的正常运行进行干扰、破坏、损害等行为，不利用CCMTV平台进行任何危害计算机网络安全的行为。\n" +
                "7.CCMTV有权将委托任何旗下主体公司及其关联公司进行运营管理及履行本协议项下权利义务，CCMTV无需另行向您取得授权。");
    }
}
